package com.budget.droid;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import android.provider.BaseColumns;
import android.util.Log;

public class Schema {
	private Schema() {
	}

	public final static String DATABASE_NAME = "com.budget.droid.db";
	public final static int DATABASE_VERSION = 2;
	public final static String TYPE_INT = " INTEGER";
	public final static String TYPE_TEXT = " TEXT";

	public static abstract class Expenses {
		public static final String TABLE_NAME = "expenses";

		public static abstract class Columns implements BaseColumns {
			public static final String CATEGORY = "category";
			public static final String AMOUNT = "amount";
			public static final String TIME = "time";
		}

		public static String create() {
			SQLBuilder builder = new SQLBuilder(TABLE_NAME);
			builder.addColumn(Columns._ID, TYPE_INT)
					.addColumn(Columns.CATEGORY, TYPE_INT)
					.addColumn(Columns.AMOUNT, TYPE_INT)
					.addColumn(Columns.TIME, TYPE_TEXT)
					.addForeignKey(Columns.CATEGORY, TABLE_NAME);
			return builder.build();
		}
	}

	public static abstract class ProjectedExpenses {
		public static final String TABLE_NAME = "projected_expenses";

		public static abstract class Columns implements BaseColumns {
			public static final String CATEGORY = "category";
			public static final String AMOUNT = "amount";
			public static final String PER = "per";
			public static final String FROM = "time_from";
			public static final String TO = "time_to";
		}

		public static String create() {
			SQLBuilder builder = new SQLBuilder(TABLE_NAME);
			builder.addColumn(Columns._ID, TYPE_INT)
					.addColumn(Columns.CATEGORY, TYPE_INT)
					.addColumn(Columns.AMOUNT, TYPE_INT)
					.addColumn(Columns.FROM, TYPE_TEXT)
					.addColumn(Columns.TO, TYPE_TEXT)
					.addColumn(Columns.PER, TYPE_TEXT)
					.addForeignKey(Columns.CATEGORY, Categories.TABLE_NAME);
			return builder.build();
		}
	}

	public static abstract class Income {
		public static final String TABLE_NAME = "income";

		public static abstract class Columns implements BaseColumns {
			public static final String CATEGORY = "category";
			public static final String AMOUNT = "amount";
			public static final String TIME = "time";
		}
	}

	public static abstract class ProjectedIncome {
		public static final String TABLE_NAME = "projected_income";

		public static abstract class Columns implements BaseColumns {
			public static final String CATEGORY = "category";
			public static final String AMOUNT = "amount";
			public static final String FOR = "_for";
			public static final String PER = "per";
			//public static final String FROM = "from";
			//public static final String TO = "to";
		}
	}

	public static abstract class Categories {
		public static final String TABLE_NAME = "categories";

		public static abstract class Columns implements BaseColumns {
			public static final String NAME = "name";
			public static final String PARENT = "parent";
		}

		public static String create() {
			SQLBuilder builder = new SQLBuilder(TABLE_NAME);
			builder.addColumn(Columns._ID, TYPE_INT)
					.addColumn(Columns.NAME, TYPE_TEXT)
					.addColumn(Columns.PARENT, TYPE_INT);
			return builder.build();
		}
	}

	public static class SQLBuilder {
		private String name;
		private HashMap<String, String> columns;
		private HashMap<String, String> foreignKeys;

		public SQLBuilder(String table) {
			this.name = table;
			columns = new HashMap<String, String>();
			foreignKeys = new HashMap<String, String>();
		}

		public SQLBuilder(String name, Map<String, String> columns, Map<String, String> foreignKeys) {
			this.columns = new HashMap<String, String>(columns);
			this.foreignKeys = new HashMap<String, String>(foreignKeys);
		}

		public SQLBuilder addColumn(String name, String type) {
			columns.put(name, type);
			return this;
		}

		public SQLBuilder addForeignKey(String column, String table) {
			foreignKeys.put(column, table);
			return this;
		}

		public String build() {
			StringBuilder sb = new StringBuilder("CREATE TABLE ");
			sb.append(name);
			sb.append('(');
			Set<String> columnNames = columns.keySet();
			for (String column : columnNames) {
				sb.append(column);
				sb.append(columns.get(column));
				if (column == BaseColumns._ID) {
					sb.append(" PRIMARY KEY");
				}
				sb.append(", ");
			}
			columnNames = foreignKeys.keySet();
			for (String column : columnNames) {
				sb.append("FOREIGN KEY (");
				sb.append(column);
				sb.append(')');
				sb.append(" REFERENCES ");
				sb.append(foreignKeys.get(column));
				sb.append('(');
				sb.append(BaseColumns._ID);
				sb.append("), ");
			}
			int l = sb.length();
			sb.replace(l - 2, l,")");
			Log.d("SQLBuilder", sb.toString());
			return sb.toString();
		}
	}

}
