package com.example.closetstylist;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class ItemDataAdapter extends CursorAdapter {
	
	public ItemDataAdapter(Context context, Cursor cursor) {
		super(context, cursor);
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		LayoutInflater inflater = LayoutInflater.from(parent.getContext());
		View view = inflater.inflate(R.layout.my_closet, parent, false);
		return view;
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		// TODO Auto-generated method stub
		TextView timeTextView = (TextView) view.findViewById(R.id.closet_value_name);
		timeTextView.setText(cursor.getString(1));
		TextView notesTextView = (TextView) view.findViewById(R.id.closet_value_description);
		notesTextView.setText(cursor.getString(2));
	}

}
