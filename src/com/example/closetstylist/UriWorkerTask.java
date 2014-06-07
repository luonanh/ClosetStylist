package com.example.closetstylist;

import java.io.File;
import java.lang.ref.WeakReference;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

/*
 * ALDBG follows
 * 	- http://developer.android.com/training/displaying-bitmaps/load-bitmap.html 
 * 	- http://developer.android.com/training/displaying-bitmaps/process-bitmap.html
 * 
 */
public class UriWorkerTask extends AsyncTask<ItemData, Void, Bitmap>{
	private final static String LOG_TAG = UriWorkerTask.class.getCanonicalName();
	private final WeakReference<ImageView> imageViewReference;
	private ItemData mItemData = null;
	private Context mContext = null;
	private boolean mIsCropped = true; // false - original; true - cropped

	public UriWorkerTask(ImageView imageView, Context context, boolean isCropped) {
		// Use a WeakReference to ensure the ImageView can be garbage collected
		imageViewReference = new WeakReference<ImageView>(imageView);
		mContext = context.getApplicationContext();
		mIsCropped = isCropped;
	}

	@Override
	protected Bitmap doInBackground(ItemData... params) {
		mItemData = params[0];
		return decodeSampledBitmapFromResource(mContext.getResources(), 
				mItemData, mItemData.getCropHeight(), mItemData.getCropWidth());
	}

	// Once complete, see if ImageView is still around and set bitmap.
	@Override
	protected void onPostExecute(Bitmap bitmap) {
		// associate the bitmap with the imageView created in the constructor 
		if (imageViewReference != null && bitmap != null) {
			final ImageView imageView = imageViewReference.get();
			if (imageView != null) {
				imageView.setImageBitmap(bitmap);
			}
		}
	}

	public Bitmap decodeSampledBitmapFromResource(Resources res, ItemData itemData,
			int reqWidth, int reqHeight) {

		// Convert Uri (in ItemData) to Bitmap 
		// Follow http://stackoverflow.com/questions/6936898/converting-image-to-bitmap-in-android
		// Examples:
		// Bitmap bmp=BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
		// public static Bitmap decodeStream (InputStream is, Rect outPadding, BitmapFactory.Options opts)
		// Bitmap bmp=BitmapFactory.decodeStream(getContentResolver().openInputStream(uri), null, options);

		try {
			// First decode with inJustDecodeBounds=true to check dimensions
			final BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			String imageLink = null;
			
			if (true == mIsCropped) {
				imageLink = itemData.getCropImageLink();
			} else {
				imageLink = itemData.getImageLink();
			}
			BitmapFactory.decodeStream(mContext.getContentResolver()
					.openInputStream(Uri.parse(imageLink)), null, options);				

			// Calculate inSampleSize
			options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

			// Decode bitmap with inSampleSize set
			options.inJustDecodeBounds = false;
			
			return BitmapFactory.decodeStream(mContext.getContentResolver()
					.openInputStream(Uri.parse(imageLink)), null, options);

			/*
			 * Try to programmatically rotate the images based on 
			 * android.media.ExifInterface constants but this doesn't work.
			 * 
			Matrix matrix = new Matrix();
			matrix.postRotate(getImageOrientation(imageLink));
			return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
			 *
			 */
		} catch (Exception e) {
			Log.d(LOG_TAG, "Error in decodeSampledBitmapFromResource " + e.toString());
			return null;
		}
	}

	/*
	 * Subsample an image to load a much smaller version of the original image
	 * into memory (by power of 2, based on the target width and height).  
	 */
	public static int calculateInSampleSize(
			BitmapFactory.Options options, int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			final int halfHeight = height / 2;
			final int halfWidth = width / 2;

			// Calculate the largest inSampleSize value that is a power of 2 and keeps both
			// height and width larger than the requested height and width.
			while ((halfHeight / inSampleSize) > reqHeight
					&& (halfWidth / inSampleSize) > reqWidth) {
				inSampleSize *= 2;
			}
		}

		return inSampleSize;
	}
	
	/*
	 * This function is NOT used.
	 * Get rotation angle based on android.media.ExifInterface constants.
	 * Somehow the value got back is always 0 ???
	 */
	private static int getImageOrientation(String imageLink) {
		int rotate = 0;
	     try {
	         File imageFile = new File(imageLink);
	         ExifInterface exif = new ExifInterface(imageFile.getAbsolutePath());
	         int orientation = exif.getAttributeInt(
	                 ExifInterface.TAG_ORIENTATION,
	                 ExifInterface.ORIENTATION_NORMAL);

	         switch (orientation) {
	         case ExifInterface.ORIENTATION_ROTATE_270:
	             rotate = 270;
	             break;
	         case ExifInterface.ORIENTATION_ROTATE_180:
	             rotate = 180;
	             break;
	         case ExifInterface.ORIENTATION_ROTATE_90:
	             rotate = 90;
	             break;
	         }
	         Log.d(LOG_TAG, "Exif orientation: " + orientation);
	     } catch (Exception e) {
	         e.printStackTrace();
	     }
		return rotate;
	}
}

