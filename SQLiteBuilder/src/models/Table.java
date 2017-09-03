package models;

import java.util.ArrayList;

public class Table {

	private String name;
	private ArrayList<SQLTableColumn> cols;

	public Table() {
		name = "unspecified";
		cols = new ArrayList<SQLTableColumn>();
	}

	public Table(String n){
		this.name = n;
		cols = new ArrayList<SQLTableColumn>();
	}

	public ArrayList<SQLTableColumn> getCols() {
		return cols;
	}

	public String getName() {
		return name;
	}

	public void setCols(ArrayList<SQLTableColumn> cols) {
		this.cols = cols;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void add(SQLTableColumn col){
		this.cols.add(col);
	}
}
