package com.budget.droid;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ExpensesAdapter extends BaseAdapter {
	private Cursor _data;
	private LayoutInflater _inflater;
	private DataStore _ds;

	public ExpensesAdapter(Cursor data, Context context) {
		_data = data;
		_inflater = LayoutInflater.from(context);
		_ds = new DataStore(context);
	}

	@Override
	public int getCount() {
		return _data.getCount();
	}

	@Override
	public Object getItem(int position) {

		_data.moveToPosition(position);
		return _data.getString(_data.getColumnIndex(Schema.Categories.Columns.NAME));
	}

	@Override
	public long getItemId(int position) {
		_data.moveToPosition(position);
		return _data.getInt(_data.getColumnIndex(Schema.Categories.Columns._ID));
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		ViewHolder holder;
		if (view == null) {
			holder = new ViewHolder();
			view = _inflater.inflate(R.layout.expense_list_item, null);
			holder.title = (TextView) view.findViewById(R.id.exp_title);
			holder.spent = (TextView) view.findViewById(R.id.exp_spent);
			holder.graph = (ProgressBar) view.findViewById(R.id.exp_graph);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
			holder.graph.setProgress(0);
			holder.graph.setSecondaryProgress(0);
			holder.spent.setText(null);
		}
		_data.moveToPosition(position);

		int category = _data.getInt(_data.getColumnIndex(Schema.Categories.Columns._ID));
		double expense = _ds.getExpense(category, null);
		double budget = _ds.getBudget(category);

		holder.title.setText(_data.getString(_data.getColumnIndex(Schema.Categories.Columns.NAME)));
		if (expense > 0) {
			holder.spent.setText(Double.toString(expense));
			if (budget > 0) {
				holder.spent.append(" of "+Double.toString(budget));
				if (expense >= budget) {
					holder.graph.setProgress((int) (budget * 100 / expense));
					holder.graph.setSecondaryProgress(100);
				} else {
					holder.graph.setProgress((int) (expense * 100 / budget));
				}
			}
		}
		return view;
	}

	public static class ViewHolder {
		public TextView title;
		public TextView spent;
		public ProgressBar graph;
	}

}
