package com.budget.droid;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class OpenHelper extends SQLiteOpenHelper {
	Context _context;

	public OpenHelper(Context context) {
		super(context, Schema.DATABASE_NAME, null, Schema.DATABASE_VERSION);
		_context = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(Schema.Categories.create());
		db.execSQL(Schema.Expenses.create());
		db.execSQL(Schema.ProjectedExpenses.create());
		insertInitialCategories(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		String sql = "DROP TABLE IF EXISTS ";
		db.execSQL(sql + Schema.Expenses.TABLE_NAME);
		db.execSQL(sql + Schema.ProjectedExpenses.TABLE_NAME);
		db.execSQL(sql + Schema.Categories.TABLE_NAME);
		onCreate(db);
	}

	public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		onUpgrade(db, oldVersion, newVersion);
	}

	private void insertInitialCategories(SQLiteDatabase db) {
		String[] categories = _context.getResources().getStringArray(R.array.initial_categories);
		for (String category : categories) {
			ContentValues values = new ContentValues();
			values.put(Schema.Categories.Columns.NAME, category);
			db.insert(Schema.Categories.TABLE_NAME, null, values);
			Log.d("OpenHelper", "Category \'" + category + "\' added");
		}
	}

}
