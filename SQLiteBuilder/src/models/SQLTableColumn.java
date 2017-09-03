package models;

import utils.ColType;

public class SQLTableColumn {

	private String name;
	private ColType type;
	boolean notNull;

	public SQLTableColumn(String name, ColType type, boolean notNull) {
		super();
		this.name = name;
		this.type = type;
		this.notNull = notNull;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ColType getType() {
		return type;
	}

	public void setType(ColType type) {
		this.type = type;
	}

	public boolean isNotNull() {
		return notNull;
	}

	public void setNotNull(boolean notNull) {
		this.notNull = notNull;
	}

}
