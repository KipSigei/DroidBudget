package com.budget.droid;

import java.util.Locale;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class ChangeBudgetActivity extends ActionBarActivity {

	Spinner categories, time_units;
	EditText etAmount, etFor;
	DataStore ds;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_change_budget);

		etAmount = (EditText) findViewById(R.id.amount);
		etFor = (EditText) findViewById(R.id._for);

		etFor.setText(Integer.toString(1));
		
		ds = new DataStore(this);

		categories = (Spinner) findViewById(R.id.categories);
		categories.setAdapter(new CategoriesAdapter(this, ds.getAllCategories()));

		time_units = (Spinner) findViewById(R.id.time_units);
		time_units.setAdapter(new ArrayAdapter<String>(this, R.layout.spinner_item, getResources().getStringArray(
				R.array.time_units)));

		categories.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				double budget = ds.getBudget((int) categories.getSelectedItemId());
				if (budget > 0) {
					etAmount.setText(Double.toString(budget));
				} else {
					etAmount.getText().clear();
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				
			}			
		});
	}

	public void buttonClicked(View v) {
		switch (v.getId()) {
		case R.id.ok:
			edit();
			break;
		case R.id.cancel:
			finish();
		default:
			break;
		}
	}

	private void edit() {
		String sAmount = etAmount.getText().toString();
		String sFor = etFor.getText().toString();
		String toastMsg = null;

		if (Util.isEmpty(sAmount)) {
			toastMsg = "No amount has been entered";
		} else {
			try {
				Double amount = Double.parseDouble(sAmount);
				try {
					Double per = Double.parseDouble(sFor);
					Double days = 1.0;
					switch (time_units.getSelectedItemPosition()) {
					case 0:
						days = 1.0;
						break;
					case 1:
						days = 7.0;
						break;
					case 2:
						days = 30.4375;
						break;
					default:
						break;
					}
					
					int categoryId = (int) categories.getSelectedItemId();
					if (ds.editBudget(categoryId, amount, per * days)) {
						finish();
						toastMsg = amount + " budgeted per " + sFor + " " + time_units.getSelectedItem().toString().toLowerCase(Locale.getDefault())
								+ " for " + categories.getSelectedItem().toString();
					}

				} catch (NumberFormatException e) {
					toastMsg = "Inavlid value for amount";
				}
			} catch (NumberFormatException e) {
				toastMsg = "Inavlid value for time unit";
			}
		}
		Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();		
	}
}
