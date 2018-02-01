package org.colorcoding.ibas.bobas.core;

/**
 * 跟踪状态操作员
 */
public interface ITrackStatusOperator {
	/**
	 * 标记为未修改
	 */
	void markOld();

	/**
	 * 标记为新
	 */
	void markNew();

	/**
	 * 对象置为实际删除
	 */
	void markDeleted();

	/**
	 * 对象置为脏
	 */
	void markDirty();

	/**
	 * 标记为未修改
	 * 
	 * @param recursive
	 *            包括子项及属性
	 */
	void markOld(boolean recursive);

}
