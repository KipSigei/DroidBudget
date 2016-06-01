package com.budget.droid;

import java.lang.reflect.Field;
import java.util.Calendar;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ExpensesActivity extends ActionBarActivity {
	OpenHelper helper;
	
	ListView lstDayExpenses;
	
	TextView emptyText;

	String date;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start);
		
		lstDayExpenses = (ListView) findViewById(R.id.lst_summary);
		
		emptyText = (TextView) findViewById(R.id.no_expenses);

		Calendar c = Calendar.getInstance();
		
		
		date = Util.dateToString(c);
		helper = new OpenHelper(getApplicationContext());
		
		getOverflowMenu();

	}

	@Override
	protected void onResume() {
		super.onResume();
		displayDayExpenses();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.record:
			startActivity(new Intent(getApplicationContext(), RecordExpenseActivity.class));
			break;
		case R.id.budget:
			startActivity(new Intent(getApplicationContext(), ChangeBudgetActivity.class));
			break;
		default:
			return super.onOptionsItemSelected(item);
		}
		return true;
	}
	
	private void displayDayExpenses(){
		SQLiteDatabase db = helper.getWritableDatabase();
		String[] columns = { Schema.Categories.Columns._ID, Schema.Categories.Columns.NAME,
				Schema.Categories.Columns.PARENT };
		String selection = Schema.Categories.Columns.PARENT + " IS NULL";
		Cursor cursor = db.query(Schema.Categories.TABLE_NAME, columns, selection, null, null, null, null, null);
		if (cursor.moveToFirst()) {
			emptyText.setVisibility(View.GONE);
			lstDayExpenses.setVisibility(View.VISIBLE);
			lstDayExpenses.setAdapter(new ExpensesAdapter(cursor, this));
			lstDayExpenses.setOnItemLongClickListener(new OnItemLongClickListener() {

				@Override
				public boolean onItemLongClick (AdapterView<?> parent, View view, int position, long id) {
					ContextMenuDialogFragment menu = new ContextMenuDialogFragment();
					Bundle args = new Bundle();
					args.putString("category", lstDayExpenses.getItemAtPosition(position).toString());
					args.putInt("categoryId", (int) lstDayExpenses.getItemIdAtPosition(position));
					menu.setArguments(args);
					menu.show(getSupportFragmentManager(), "ContextMenuDialogFragment");
					return false;
				}
				
			});
		}
		db.close();
	}
	
	private void getOverflowMenu() {

		try {
			ViewConfiguration config = ViewConfiguration.get(this);
			Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
			if (menuKeyField != null) {
				menuKeyField.setAccessible(true);
				menuKeyField.setBoolean(config, false);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	
	public static class ContextMenuDialogFragment extends DialogFragment implements DialogInterface.OnClickListener{
		Bundle args;	
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			args = getArguments();
			return new AlertDialog.Builder(getActivity())
						.setTitle(args.getString("category"))
						.setItems(new String[] { "Add", "Budget", "Details" }, this)
						.create();
		}

		@Override
		public void onClick(DialogInterface dialog, int which) {
			switch (which) {
			case 0:
				startActivity(new Intent(getActivity(), RecordExpenseActivity.class).putExtra("categoryId", args.getInt("categoryId")));
				break;
			case 1:
				startActivity(new Intent(getActivity(), ChangeBudgetActivity.class).putExtra("categoryId", args.getInt("categoryId")));
				break;

			default:
				break;
			}
		}
	}
}
