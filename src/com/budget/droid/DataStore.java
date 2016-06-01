package com.budget.droid;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DataStore {
	private OpenHelper _helper;

	public DataStore(Context context) {
		_helper = new OpenHelper(context);
	}

	public Category[] getAllCategories() {
		SQLiteDatabase db = _helper.getReadableDatabase();
		String[] columns = { 
				Schema.Categories.Columns._ID, 
				Schema.Categories.Columns.NAME
		};
		Cursor c = db.query(Schema.Categories.TABLE_NAME, columns, null, null, null, null, null, null);
		int count = c.getCount();
		Category[] categories = null;
		if (count > 0) {
			categories = new Category[count];
			int i = 0;
			while (c.moveToNext()) {
				categories[i++] = new Category(
						c.getInt(c.getColumnIndex(Schema.Categories.Columns._ID)), 
						c.getString(c.getColumnIndex(Schema.Categories.Columns.NAME))
				);
			}
		}
		db.close();
		return categories;
	}

	public boolean addExpense(String category, double amount, String date) {
		SQLiteDatabase db = _helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		int cat = getCategoryId(category, db);
		if (cat == -1) {
			values.putNull(Schema.Expenses.Columns.CATEGORY);
		} else {
			values.put(Schema.Expenses.Columns.CATEGORY, cat);
		}
		values.put(Schema.Expenses.Columns.AMOUNT, amount);
		values.put(Schema.Expenses.Columns.TIME, date);
		if (db.insert(Schema.Expenses.TABLE_NAME, null, values) == -1) {
			db.close();
			return false;
		} else {
			db.close();
			return true;
		}
	}

	private int getCategoryId(String category, SQLiteDatabase db) {
		int id = -1;
		String[] columns = { Schema.Categories.Columns._ID, Schema.Categories.Columns.NAME, };
		String[] args = { category };
		Cursor c = db.query(Schema.Categories.TABLE_NAME, columns, Schema.Categories.Columns.NAME + "=?", args, null,
				null, null, null);
		if (c.moveToFirst()) {
			id = c.getInt(c.getColumnIndex(Schema.Categories.Columns._ID));
		}
		return id;
	}

	public double getExpense(int category, String date) {
		SQLiteDatabase db = _helper.getWritableDatabase();
		String[] columns = { Schema.Expenses.Columns._ID, Schema.Expenses.Columns.AMOUNT, };
		String[] args = { Integer.toString(category) };
		Cursor c = db.query(Schema.Expenses.TABLE_NAME, columns, Schema.Expenses.Columns.CATEGORY + "=?", args, null,
				null, null, null);
		if (c.moveToFirst()) {
			db.close();
			return c.getDouble(c.getColumnIndex(Schema.Expenses.Columns.AMOUNT));
		}
		db.close();
		return 0;
	}

	public double getBudget(int category) {
		SQLiteDatabase db = _helper.getWritableDatabase();
		String[] columns = { Schema.ProjectedExpenses.Columns._ID, Schema.ProjectedExpenses.Columns.AMOUNT, };
		String[] args = { Integer.toString(category) };
		Cursor c = db.query(Schema.ProjectedExpenses.TABLE_NAME, columns, Schema.ProjectedExpenses.Columns.CATEGORY
				+ "=?", args, null, null, null, null);
		if (c.moveToFirst()) {
			db.close();
			return c.getDouble(c.getColumnIndex(Schema.ProjectedExpenses.Columns.AMOUNT));
		}
		db.close();
		return 0;
	}

	public boolean editBudget(int categoryId, double amount, double perDays) {
		SQLiteDatabase db = _helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(Schema.ProjectedExpenses.Columns.CATEGORY, categoryId);
		values.put(Schema.ProjectedExpenses.Columns.AMOUNT, amount);
		if (db.insert(Schema.ProjectedExpenses.TABLE_NAME, null, values) == -1) {
			db.close();
			return false;
		} else {
			db.close();
			return true;
		}
	}
}
