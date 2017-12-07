package org.colorcoding.ibas.bobas.common;

import java.util.ArrayList;
import java.util.List;

import org.colorcoding.ibas.bobas.data.KeyValue;
import org.colorcoding.ibas.bobas.serialization.Serializable;

/**
 * 数据库存储过程
 * 
 * @author Niuren.Zhu
 *
 */
public class SqlStoredProcedure extends Serializable implements ISqlStoredProcedure {

	private static final long serialVersionUID = 8400088898134597897L;

	public SqlStoredProcedure() {

	}

	public SqlStoredProcedure(String name) {
		this();
		this.setName(name);
	}

	private String name;

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public void setName(String value) {
		this.name = value;
	}

	private List<KeyValue> parameters;

	@Override
	public List<KeyValue> getParameters() {
		if (this.parameters == null) {
			this.parameters = new ArrayList<>();
		}
		return this.parameters;
	}

	@Override
	public void addParameters(String name, Object value) {
		this.getParameters().add(new KeyValue(name, value));
	}

}
