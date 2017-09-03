package utils;

import java.util.Arrays;
import java.util.List;

public class SQLNameValidator {

	public static boolean isValid(String name) {

		if(name == null || name.length()==0)
			return true;

		boolean isValid =
				charsAreLegal(name)
			&& firstCharCheck(name)
			&& !isKeyword(name);

		return isValid;

	}

	private static boolean firstCharCheck(String name) {
		char current = name.charAt(0);
		if (!((current >= 'A' && current <= 'Z') || (current >= 'a' && current <= 'z'))) {
			return false;
		}
		return true;
	}

	private static boolean charsAreLegal(String name) {
		for (int i = 0; i < name.length(); i++) {
			char current = name.charAt(i);
			if (!charIsLegal(current))
				return false;
		}
		return true;
	}

	private static boolean charIsLegal(char current) {
		if (!((current >= 'A' && current <= 'Z') || (current >= 'a' && current <= 'z')
				|| (current >= '0' && current <= '9'))) {
			return false;
		}
		return true;
	}

	private static boolean isKeyword(String name) {

		String[] keywords = { "ABORT", "ACTION", "ADD", "AFTER", "ALL", "ALTER", "ANALYZE", "AND", "AS", "ASC",
				"ATTACH", "AUTOINCREMENT", "BEFORE", "BEGIN", "BETWEEN", "BY", "CASCADE", "CASE", "CAST", "CHECK",
				"COLLATE", "COLUMN", "COMMIT", "CONFLICT", "CONSTRAINT", "CREATE", "CROSS", "CURRENT_DATE",
				"CURRENT_TIME", "CURRENT_TIMESTAMP", "DATABASE", "DEFAULT", "DEFERRABLE", "DEFERRED", "DELETE", "DESC",
				"DETACH", "DISTINCT", "DROP", "EACH", "ELSE", "END", "ESCAPE", "EXCEPT", "EXCLUSIVE", "EXISTS",
				"EXPLAIN", "FAIL", "FOR", "FOREIGN", "FROM", "FULL", "GLOB", "GROUP", "HAVING", "IF", "IGNORE",
				"IMMEDIATE", "IN", "INDEX", "INDEXED", "INITIALLY", "INNER", "INSERT", "INSTEAD", "INTERSECT", "INTO",
				"IS", "ISNULL", "JOIN", "KEY", "LEFT", "LIKE", "LIMIT", "MATCH", "NATURAL", "NO", "NOT", "NOTNULL",
				"NULL", "OF", "OFFSET", "ON", "OR", "ORDER", "OUTER", "PLAN", "PRAGMA", "PRIMARY", "QUERY", "RAISE",
				"RECURSIVE", "REFERENCES", "REGEXP", "REINDEX", "RELEASE", "RENAME", "REPLACE", "RESTRICT", "RIGHT",
				"ROLLBACK", "ROW", "SAVEPOINT", "SELECT", "SET", "TABLE", "TEMP", "TEMPORARY", "THEN", "TO",
				"TRANSACTION", "TRIGGER", "UNION", "UNIQUE", "UPDATE", "USING", "VACUUM", "VALUES", "VIEW", "VIRTUAL",
				"WHEN", "WHERE", "WITH", "WITHOUT" };
		List<String> kw = Arrays.asList(keywords);
		if (kw.contains(name))
			return true;

		return false;

	}

}
