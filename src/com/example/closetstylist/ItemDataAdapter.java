package com.example.closetstylist;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ItemDataAdapter extends CursorAdapter {
	Context context = null;
	
	public ItemDataAdapter(Context context, Cursor cursor) {
		super(context, cursor);
		context = context;
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		LayoutInflater inflater = LayoutInflater.from(parent.getContext());
		View view = inflater.inflate(R.layout.closet_item_view, parent, false);
		return view;
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		// build an ItemData object from Cursor 
		ItemData itemData = ItemDatabaseHelper.getItemDataFromCursor(cursor);
		
		// Populate name field
		TextView name = (TextView) view.findViewById(R.id.closet_value_name);
		if (itemData.getName().isEmpty()) {
			name.setText(context.getString(R.string.my_closet_item_view_value_no_name));
		} else {
			name.setText(context.getString(R.string.my_closet_item_view_label_name) 
					+ itemData.getName());
			
		}
		
		// Populate description field
		TextView description = (TextView) view.findViewById(R.id.closet_value_description);
		if (itemData.getDescription().isEmpty()) {
			description.setText(context.getString(R.string.my_closet_item_view_value_no_description));
		} else {
			description.setText(context.getString(R.string.my_closet_item_view_label_description) 
					+ itemData.getDescription());
		}
		
		// Populate image
		ImageView image = (ImageView) view.findViewById(R.id.closet_value_image);
		image.setImageURI(Uri.parse(itemData.getImageLink()));
	}

}