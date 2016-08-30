package org.colorcoding.ibas.bobas;

import org.colorcoding.ibas.bobas.configuration.Configuration;

/**
 * 配置
 */
public class MyConfiguration extends Configuration {
	/**
	 * 配置项目-公司标记
	 */
	public static String CONFIG_ITEM_COMPANY_ID = "CompanyId";
	/**
	 * 配置项目-调试模式
	 */
	public static String CONFIG_ITEM_DEBUG_MODE = "DebugMode";
	/**
	 * 配置项目-开启自定义字段
	 */
	public static String CONFIG_ITEM_BO_ENABLED_USER_FIELDS = "EnabledUserFields";
	/**
	 * 配置项目-关闭业务对象版本检查
	 */
	public static String CONFIG_ITEM_BO_DISABLED_VERSION_CHECK = "DisabledVersionCheck";
	/**
	 * 配置项目-关闭业务对象事务通知
	 */
	public static String CONFIG_ITEM_BO_DISABLED_POST_TRANSACTION = "DisabledPostTransaction";
	/**
	 * 配置项目-关闭业务对象保存后重载
	 */
	public static String CONFIG_ITEM_BO_DISABLED_REFETCH = "DisabledRefetch";
	/**
	 * 配置项目-关闭仓库缓存
	 */
	public static String CONFIG_ITEM_BO_REPOSITORY_DISABLED_CACHE = "DisabledCache";
	/**
	 * 配置项目-关闭业务对象业务逻辑
	 */
	public static String CONFIG_ITEM_BO_DISABLED_BUSINESS_LOGICS = "DisabledBusinessLogics";
	/**
	 * 配置项目-关闭业务对象审批
	 */
	public static String CONFIG_ITEM_BO_DISABLED_BUSINESS_APPROVAL = "DisabledBusinessApproval";
	/**
	 * 配置项目-关闭业务对象智能主键
	 */
	public static String CONFIG_ITEM_BO_DISABLED_SMART_PRIMARY_KEY = "DisabledSmartKey";
	/**
	 * 配置项目-开启只读业务仓库
	 */
	public static String CONFIG_ITEM_ENABLED_READONLY_REPOSITORY = "EnabledReadonlyRepository";
	/**
	 * 配置项目-文件仓库目录
	 */
	public static String CONFIG_ITEM_FILE_REPOSITORY_FOLDER = "FileRepositoryFolder";
	/**
	 * 配置项目-数据库类型
	 */
	public static String CONFIG_ITEM_DB_TYPE = "DbType";
	/**
	 * 配置项目-数据库地址
	 */
	public static String CONFIG_ITEM_DB_SERVER = "DbServer";
	/**
	 * 配置项目-数据库名称
	 */
	public static String CONFIG_ITEM_DB_NAME = "DbName";
	/**
	 * 配置项目-数据库用户名称
	 */
	public static String CONFIG_ITEM_DB_USER_ID = "DbUserID";
	/**
	 * 配置项目-数据库用户密码
	 */
	public static String CONFIG_ITEM_DB_USER_PASSWORD = "DbUserPassword";

	/**
	 * 配置项目-数据库连接池大小
	 */
	public static String CONFIG_ITEM_DB_CONNECTION_POOL_SIZE = "DbPoolSize";
	/**
	 * 配置项目-国际化文件路径
	 */
	public static String CONFIG_ITEM_I18N_PATH = "i18nFolder";

	/**
	 * 配置项目-语言编码
	 */
	public static String CONFIG_ITEM_LANGUAGE_CODE = "LanguageCode";

	/**
	 * 配置项目-消息记录方式
	 */
	public static String CONFIG_ITEM_MESSAGE_RECORDER_WAY = "RecorderWay";
	/**
	 * 配置项目-日志文件保存路径
	 */
	public static String CONFIG_ITEM_LOG_PATH = "LogFolder";
	/**
	 * 配置项目-日志消息级别
	 */
	public static String CONFIG_ITEM_LOG_MESSAGE_LEVEL = "MsgLevel";
	/**
	 * 配置项目-日志文件输出频率(毫秒,默认5000)
	 */
	public static String CONFIG_ITEM_LOG_OUT_FREQUENCY = "LogOutFrequency";

	/**
	 * 配置项目-审批方式
	 */
	public static String CONFIG_ITEM_APPROVAL_WAY = "ApprovalWay";

	/**
	 * 配置项目-组织方式
	 */
	public static String CONFIG_ITEM_ORGANIZATION_WAY = "OrganizationWay";
	/**
	 * 配置项目-业务逻辑方式
	 */
	public static String CONFIG_ITEM_BUSINESS_LOGICS_WAY = "BizLogicsWay";
	/**
	 * 配置项目-权限判断方式
	 */
	public static String CONFIG_ITEM_OWNERSHIP_WAY = "OwnershipWay";
}
