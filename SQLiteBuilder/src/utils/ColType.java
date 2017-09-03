package utils;

public enum ColType {

	INTEGER("INTEGER","int"),
	REAL("REAL","double"),
	BLOB("BLOB","String"),
	TEXT("TEXT","String");

	private String name;
	private String javaType;

	private ColType(String name,String java) {
		this.name = name;
		this.javaType = java;
	}

	public String getName() {
		return name;
	}

	public static ColType getTypeByString(String s) {

		if(s.equals("INTEGER")){
			return INTEGER;
		}else if(s.equals("REAL")){
			return REAL;
		}else if(s.equals("BLOB")){
			return BLOB;
		}else if(s.equals("TEXT")){
			return TEXT;
		}else{
			return null;
		}

	}

	public String getJavaType() {
		return javaType;
	}
}
