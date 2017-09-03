package models;

import java.util.ArrayList;

public class Database {

	private String name;
	private ArrayList<Table> tables;

	public Database() {
		name = "no name";
		tables = new ArrayList<>();
	}

	public Database(String n) {
		this.name = n;
		tables = new ArrayList<Table>();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<Table> getTables() {
		return tables;
	}

	public void setTables(ArrayList<Table> tables) {
		this.tables = tables;
	}

	public void add(Table t){
		this.tables.add(t);
	}

}
