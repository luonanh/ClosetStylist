package com.example.closetstylist;

import android.content.Context;
import android.widget.ImageView;

public class ImageSubSampler {

	/*
	 * This function is used to subsample an image resId by a power of 2 
	 * and set the imageView to the newly subsampled image. 
	 */
	public static void subSample(int resId, ImageView imageView, Context context) {
		BitmapWorkerTask task = new BitmapWorkerTask(imageView, context);
	    task.execute(resId);
	}
	
	public static void subSampleCroppedUri(ItemData itemData, ImageView imageView, Context context) {
		UriWorkerTask task = new UriWorkerTask(imageView, context, true);
	    task.execute(itemData);
	}
	
	public static void subSampleOriginalUri(ItemData itemData, ImageView imageView, Context context) {
		UriWorkerTask task = new UriWorkerTask(imageView, context, false);
	    task.execute(itemData);
	}
}
