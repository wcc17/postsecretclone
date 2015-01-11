package com.example.postsecretclone;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;

public class EditPhoto extends Activity 
{
	Uri mImageUri;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_photo);
		
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) 
		{
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
		
		//get the photo location from the intent
		Intent intent = getIntent();
		String fileLocation = intent.getStringExtra(MainActivity.EXTRA_PHOTO);
		Log.v("PostSecretClone", fileLocation);
		mImageUri = Uri.parse(fileLocation);
		
		//now display the image with the imageView variable from the layout
		ImageView imageView = (ImageView) findViewById(R.id.image);
		this.grabImage(imageView);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		switch (item.getItemId()) 
		{
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void grabImage(ImageView imageView)
	{
		this.getContentResolver().notifyChange(mImageUri, null);
		ContentResolver cr = this.getContentResolver();
		Bitmap bitmap;
		try
		{
			bitmap = android.provider.MediaStore.Images.Media.getBitmap(cr,  mImageUri);
			imageView.setImageBitmap(bitmap);
		}
		catch (Exception e)
		{
			Log.d("PostSecretClone", "Failed to load", e);
		}
	}

}
