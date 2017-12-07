package org.colorcoding.ibas.bobas.repository;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;
import java.util.UUID;

import javax.xml.bind.JAXBException;

import org.colorcoding.ibas.bobas.MyConfiguration;
import org.colorcoding.ibas.bobas.bo.BOException;
import org.colorcoding.ibas.bobas.bo.BOUtilities;
import org.colorcoding.ibas.bobas.bo.IBODocument;
import org.colorcoding.ibas.bobas.bo.IBOKeysManager;
import org.colorcoding.ibas.bobas.bo.IBOMasterData;
import org.colorcoding.ibas.bobas.bo.IBOSimple;
import org.colorcoding.ibas.bobas.bo.IBOStorageTag;
import org.colorcoding.ibas.bobas.common.IOperationResult;
import org.colorcoding.ibas.bobas.common.OperationResult;
import org.colorcoding.ibas.bobas.core.IBusinessObjectBase;
import org.colorcoding.ibas.bobas.core.ITrackStatusOperator;
import org.colorcoding.ibas.bobas.core.RepositoryException;
import org.colorcoding.ibas.bobas.core.SaveActionListener;
import org.colorcoding.ibas.bobas.core.SaveActionSupport;
import org.colorcoding.ibas.bobas.core.SaveActionType;
import org.colorcoding.ibas.bobas.data.KeyValue;
import org.colorcoding.ibas.bobas.i18n.I18N;
import org.colorcoding.ibas.bobas.message.Logger;

public class BORepository4File extends BORepository4FileReadonly implements IBORepository4File {
	protected static final String MSG_REPOSITORY_DELETED_DATA_FILE = "repository: deleted data file [%s].";
	protected static final String MSG_REPOSITORY_WRITED_DATA_FILE = "repository: writed data in file [%s].";

	private IBOKeysManager keysManager;

	public final IBOKeysManager getKeysManager() {
		if (this.keysManager == null) {
			this.keysManager = new IBOKeysManager() {
				public KeyValue[] usePrimaryKeys(IBusinessObjectBase bo, String workFolder, String transId)
						throws IOException {
					KeyValue[] keys = null;
					if (bo instanceof IBOStorageTag) {
						IBOStorageTag tagBO = (IBOStorageTag) bo;
						String companyId = MyConfiguration.getConfigValue(MyConfiguration.CONFIG_ITEM_COMPANY, "CC")
								.toLowerCase();
						File file = new File(workFolder + File.separator + companyId + "_sys" + File.separator
								+ "bo_keys.properties");
						if (!file.exists()) {
							file.getParentFile().mkdirs();
							file.createNewFile();
						}
						Properties props = new Properties();
						props.load(new FileInputStream(file));
						String value = props.getProperty(tagBO.getObjectCode());
						if (value == null || value.isEmpty()) {
							value = "1";
						}
						int key = 1, nextKey = 1;
						key = Integer.parseInt(value);
						nextKey = key + 1;
						if (bo instanceof IBODocument) {
							IBODocument item = (IBODocument) bo;
							item.setDocEntry(key);
							keys = new KeyValue[] { new KeyValue("DocEntry", key) };
						} else if (bo instanceof IBOMasterData) {
							IBOMasterData item = (IBOMasterData) bo;
							item.setDocEntry(key);
							keys = new KeyValue[] { new KeyValue("DocEntry", key) };
						} else if (bo instanceof IBOSimple) {
							IBOSimple item = (IBOSimple) bo;
							item.setObjectKey(key);
							keys = new KeyValue[] { new KeyValue("ObjectKey", key) };
						}
						OutputStream fos = new FileOutputStream(file);
						props.setProperty(tagBO.getObjectCode(), String.valueOf(nextKey));
						props.store(fos, String.format("fixed by transaction [%s].", transId));
					}
					return keys;
				}

				@Override
				public KeyValue[] usePrimaryKeys(IBusinessObjectBase bo, Object... others) throws BOException {
					String workFolder = (String) others[0];
					String transId = (String) others[1];
					try {
						return this.usePrimaryKeys(bo, workFolder, transId);
					} catch (Exception e) {
						throw new BOException(e);
					}
				}

				@Override
				public void applyPrimaryKeys(IBusinessObjectBase bo, KeyValue[] keys) {
					throw new UnsupportedOperationException();
				}

				@Override
				public KeyValue[] usePrimaryKeys(IBusinessObjectBase[] bos, Object... others) throws BOException {
					throw new UnsupportedOperationException();
				}

				@Override
				public KeyValue useSeriesKey(IBusinessObjectBase bo, Object... others) throws BOException {
					throw new UnsupportedOperationException();
				}

				@Override
				public KeyValue useSeriesKey(IBusinessObjectBase[] bos, Object... others) throws BOException {
					throw new UnsupportedOperationException();
				}

				@Override
				public void applySeriesKey(IBusinessObjectBase bo, KeyValue key) {
					throw new UnsupportedOperationException();
				}

			};
		}
		return keysManager;
	}

	public final void setKeysManager(IBOKeysManager value) {
		this.keysManager = value;
	}

	@Override
	public boolean beginTransaction() throws RepositoryException {
		if (this.inTransaction()) {
			return false;
		}
		this.inTransaction = true;
		this.setTransactionId();// 创建新的事物，才创建新id
		return true;
	}

	@Override
	public void rollbackTransaction() throws RepositoryException {
		this.inTransaction = false;
		this.setTransactionId(null);
	}

	@Override
	public void commitTransaction() throws RepositoryException {
		this.inTransaction = false;
		this.setTransactionId(null);
	}

	private boolean inTransaction;

	@Override
	public boolean inTransaction() {
		return this.inTransaction;
	}

	private volatile SaveActionSupport saveActionsSupport;

	/**
	 * 通知事务
	 * 
	 * @param type
	 *            事务类型
	 * @param bo
	 *            发生业务对象
	 * @throws SaveActionException
	 *             运行时错误
	 */
	private void fireAction(SaveActionType type, IBusinessObjectBase bo) throws Exception {
		if (this.saveActionsSupport == null) {
			return;
		}
		this.saveActionsSupport.fireAction(type, bo);
	}

	/**
	 * 添加事务监听
	 * 
	 * @param listener
	 */
	@Override
	public final void registerListener(SaveActionListener listener) {
		if (this.saveActionsSupport == null) {
			this.saveActionsSupport = new SaveActionSupport(this);
		}
		this.saveActionsSupport.registerListener(listener);
	}

	/**
	 * 移出事务监听
	 * 
	 * @param listener
	 */
	@Override
	public final void removeListener(SaveActionListener listener) {
		if (this.saveActionsSupport == null) {
			return;
		}
		this.saveActionsSupport.removeListener(listener);
	}

	@Override
	public <T extends IBusinessObjectBase> IOperationResult<T> save(T bo) {
		return this.saveEx(bo);
	}

	@Override
	public <T extends IBusinessObjectBase> IOperationResult<T> saveEx(T bo) {
		OperationResult<T> operationResult = new OperationResult<>();
		try {
			IBusinessObjectBase nBO = this.mySave(bo);
			if (nBO instanceof ITrackStatusOperator) {
				// 保存成功，标记对象为OLD
				ITrackStatusOperator operator = (ITrackStatusOperator) nBO;
				operator.markOld(true);
			}
			operationResult.addResultObjects(nBO);
		} catch (Exception e) {
			operationResult.setError(e);
		}
		return operationResult;
	}

	private IBusinessObjectBase mySave(IBusinessObjectBase bo) throws RepositoryException {
		if (bo == null) {
			throw new RepositoryException(I18N.prop("msg_bobas_invalid_bo"));
		}
		if (bo.isDirty()) {
			// 仅修过的数据进行处理
			boolean myTrans = false;// 自己打开的事务
			try {
				// 获取对象工作目录
				String boCode = "not_classified";
				if (bo instanceof IBOStorageTag) {
					IBOStorageTag boTag = (IBOStorageTag) bo;
					if (boTag.getObjectCode() != null && !boTag.getObjectCode().isEmpty()) {
						boCode = boTag.getObjectCode().toLowerCase();
					}
				}
				String boFolder = this.getRepositoryFolder() + File.separator + boCode;
				// 开始保存数据
				myTrans = this.beginTransaction();
				this.tagStorage(bo);// 存储标记
				if (bo.isNew()) {
					// 新建的对象
					this.getKeysManager().usePrimaryKeys(bo, this.getRepositoryFolder(), this.getTransactionId());
					this.fireAction(SaveActionType.BEFORE_ADDING, bo);
					String fileName = String.format("%s%s%s.bo", boFolder, File.separator, this.getFileName(bo));
					this.writeBOFile(bo, fileName);
					this.fireAction(SaveActionType.ADDED, bo);
				} else if (bo.isDeleted()) {
					// 删除对象
					this.fireAction(SaveActionType.BEFORE_DELETING, bo);
					this.deleteBOFile(bo);
					this.fireAction(SaveActionType.DELETED, bo);
				} else {
					// 修改对象，先删除数据，再添加新的实例
					this.fireAction(SaveActionType.BEFORE_UPDATING, bo);
					this.deleteBOFile(bo);
					String fileName = String.format("%s%s%s.bo", boFolder, File.separator, this.getFileName(bo));
					this.writeBOFile(bo, fileName);
					this.fireAction(SaveActionType.UPDATED, bo);
				}
				if (myTrans) {
					// 自己打开的事务
					this.commitTransaction();// 关闭事务
				}
			} catch (Exception e) {
				if (myTrans) {
					// 自己打开的事务
					this.rollbackTransaction();// 关闭事务
				}
				throw new RepositoryException(I18N.prop("msg_bobas_to_save_bo_faild", e.getMessage()), e);
			}
		}
		return bo;
	}

	private String getFileName(IBusinessObjectBase bo) {
		return UUID.randomUUID().toString();// bo.toString();
	}

	private BOFile getBOFile(IBusinessObjectBase bo) throws RepositoryException, JAXBException {
		BOFile[] boFiles = this.myFetchEx(bo.getCriteria(), bo.getClass());
		if (boFiles.length == 0) {
			throw new RepositoryException(I18N.prop("msg_bobas_not_found_bo_copy", bo));
		}
		return boFiles[0];
	}

	private boolean deleteBOFile(IBusinessObjectBase bo) throws RepositoryException, JAXBException {
		BOFile boFile = this.getBOFile(bo);
		File file = new File(this.getRepositoryFolder() + File.separator + boFile.getFilePath());
		if (file.exists()) {
			Logger.log(MSG_REPOSITORY_DELETED_DATA_FILE, file.getPath());
			return file.delete();
		}
		return false;
	}

	private void writeBOFile(IBusinessObjectBase bo, String path) throws IOException {
		File file = new File(path);
		if (!file.exists()) {
			file.getParentFile().mkdirs();
			file.createNewFile();
		}
		FileOutputStream out = new FileOutputStream(file, false);
		if (bo instanceof ITrackStatusOperator) {
			ITrackStatusOperator operator = (ITrackStatusOperator) bo;
			// 清理标记删除的数据
			BOUtilities.removeDeleted(bo);
			// 重置状态
			operator.markOld(true);
		}
		out.write(bo.toString("xml").getBytes("utf-8"));
		out.close();
		String type = String.format("%s%s%s.type", file.getParentFile().getPath(), File.separator,
				bo.getClass().getName());
		file = new File(type);
		if (!file.exists()) {
			file.createNewFile();
		}
		Logger.log(MSG_REPOSITORY_WRITED_DATA_FILE, path);
	}
}
