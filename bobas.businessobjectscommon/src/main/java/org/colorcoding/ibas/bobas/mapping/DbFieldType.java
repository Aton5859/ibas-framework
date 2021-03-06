package org.colorcoding.ibas.bobas.mapping;

public enum DbFieldType {
	/**
	 * 未知
	 */
	UNKNOWN,
	/**
	 * 字母数字
	 */
	ALPHANUMERIC,
	/**
	 * 长字符串
	 */
	MEMO,
	/**
	 * 数字
	 */
	NUMERIC,
	/**
	 * 日期
	 */
	DATE,
	/**
	 * 小数
	 */
	DECIMAL,
	/**
	 * 字节
	 */
	BYTES;
}
