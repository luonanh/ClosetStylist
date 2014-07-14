package com.example.closetstylist;

import java.io.File;

import android.net.Uri;

public class GoogleAppEngineStorage implements StorageInterface {
	ItemDatabaseHelper dbHelper;
	
	public GoogleAppEngineStorage(ItemDatabaseHelper dbHelper) {
		this.dbHelper = dbHelper;
	}

	@Override
	public Uri getOutputImageFileUri(int type, boolean isCrop) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public File getOutputImageFile(int type, boolean isCrop) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteItemImagesFromStorage(ItemData item) {
		// TODO Auto-generated method stub
		
	}

}
