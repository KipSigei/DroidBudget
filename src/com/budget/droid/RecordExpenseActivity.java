package com.budget.droid;

import java.util.Calendar;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class RecordExpenseActivity extends ActionBarActivity {

	Button date;
	Spinner categories;
	DataStore ds;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_record_expense);

		ds = new DataStore(this);
		
		categories = (Spinner) findViewById(R.id.categories);
		categories.setAdapter(new CategoriesAdapter(this, ds.getAllCategories()));
		
		date = (Button) findViewById(R.id.date);
		
		Calendar c = Calendar.getInstance();		
		date.setText(c.get(Calendar.DAY_OF_MONTH) + "/" + c.get(Calendar.MONTH) + "/" + c.get(Calendar.YEAR));
		
		int category = getIntent().getIntExtra("categoryId", -1);
		
		if(category != -1){
			for(int i = 0; i<categories.getCount(); i++){
				if(categories.getItemIdAtPosition(i) == category){
					categories.setSelection(i);
					break;
				}
			}
		}
	}

	public void buttonClicked(View v) {
		switch (v.getId()) {
		case R.id.cancel:
			finish();
			break;
		case R.id.ok:
			save();
			break;
		case R.id.date:
			showDatePicker();
			break;
		default:
			break;
		}
	}

	private void save() {
		EditText amount = (EditText) findViewById(R.id.amount);
		String sAmount = amount.getText().toString();
		String cat = categories.getSelectedItem().toString();
		String d = date.getText().toString();
		String errorMsg = null;
		if (Util.isEmpty(sAmount)) {
			errorMsg = "You have not entered an amount";
		} else if (Util.isEmpty(d)) {

		} else {
			try {
				double amt = Double.parseDouble(sAmount);

				if (ds.addExpense(cat, amt, d)) {
					finish();
					return;
				} else {
					errorMsg = "Unable to save expense. Try again";
				}
			} catch (NumberFormatException e) {
				errorMsg = "Enter a valid number";
			}
		}
		Toast.makeText(this, errorMsg, Toast.LENGTH_LONG).show();
	}
	
	private void showDatePicker() {
		Calendar c = Calendar.getInstance();
		DatePickerDialog dpd = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				date.setText(dayOfMonth + "/" + monthOfYear + "/" + year);
			}
		}, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
		dpd.show();
	}
}
