package com.budget.droid;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CategoriesAdapter extends BaseAdapter {
	LayoutInflater _inflater;
	Category[] _data;
	
	CategoriesAdapter(Context context, Category[] data){
		_inflater = LayoutInflater.from(context);
		_data = data;
	}
	
	@Override
	public int getCount() {
		return _data.length;
	}

	@Override
	public Object getItem(int position) {
		return _data[position];
	}

	@Override
	public long getItemId(int position) {
		return _data[position].getId();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null){
			convertView = _inflater.inflate(R.layout.spinner_item, null);
		}
		TextView textView = (TextView) convertView;
		textView.setText(_data[position].getName());
		return textView;
	}
}
