package com.example.closetstylist;

import java.io.File;

import android.net.Uri;

public interface StorageInterface {
	//public Uri getOutputPathUri(int type);
	public Uri getOutputImageFileUri(int type, boolean isCrop);
	//public File getOutputPath(int type);
	public File getOutputImageFile(int type, boolean isCrop);
	public void deleteItemImagesFromStorage(ItemData item);
}
