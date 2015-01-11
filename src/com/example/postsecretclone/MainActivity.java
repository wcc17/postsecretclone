package com.example.postsecretclone;

import java.io.File;
import java.util.List;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.View;

public class MainActivity extends Activity 
{
	private static final int CAPTURE_FROM_CAMERA = 1;
	public final static String EXTRA_PHOTO = "com.example.postsecretclone.PHOTO";
	private Uri mImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) 
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    /**this method is called whenever the Capture Photo button is pressed*/
    public void openCamera(View view)
    {
    	//Check here to see if the device has a camera
    	PackageManager pm = this.getPackageManager();
    	if (pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)) 
    	{
    		//if the device has a camera, open it
    		dispatchTakePictureIntent(CAPTURE_FROM_CAMERA);
    	}
    	
    	return;
    }
    
    /**this method creates the intent to take a picture and starts the camera activity*/
    private void dispatchTakePictureIntent(int actionCode)
    {
    	Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    	File photo;
    	
    	try
    	{
    		//place to store camera taken picture
    		photo = this.createTemporaryFile("picture", ".jpg");
    		photo.delete();
    	}
    	catch(Exception e)
    	{
    		Log.v("PostSecretClone", "Can't create file to take picture!");
    		return;
    	}
    	
    	mImageUri = Uri.fromFile(photo);
    	takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
    	
    	//if the intent is available (if an app can start the camera)
    	if(isIntentAvailable(this, MediaStore.ACTION_IMAGE_CAPTURE))
    	{
    		//start the camera intent
    		this.startActivityForResult(takePictureIntent, CAPTURE_FROM_CAMERA);
    	}
    	
    }
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent)
    {
    	if(requestCode==CAPTURE_FROM_CAMERA && resultCode==RESULT_OK)
    	{
    		//start the edit photo activity here!
    		Intent editPhotoIntent = new Intent(this, EditPhoto.class);
    		String ImageUri = mImageUri.toString();
    		editPhotoIntent.putExtra(EXTRA_PHOTO, ImageUri);
    		startActivity(editPhotoIntent);
    	}
    	super.onActivityResult(resultCode, resultCode, intent);
    }
    
    public void openGallery(View view)
    {
    	//open the gallery here
    }
    
    public void openSecrets(View view)
    {
    	//view saved secrets here
    }
   
    /**Used to create a temporary file and directory for images*/
    private File createTemporaryFile(String part, String ext) throws Exception
    {
    	File tempDir = Environment.getExternalStorageDirectory();
    	tempDir = new File(tempDir.getAbsolutePath()+"/.temp/");
    	if(!tempDir.exists())
    	{
    		tempDir.mkdir();
    	}
    	return File.createTempFile(part,  ext, tempDir);
    }
    
    /**Used to see if any activities can use this intent, if not, returns false*/
    public static boolean isIntentAvailable(Context context, String action)
    {
    	final PackageManager packageManager = context.getPackageManager();
    	final Intent intent = new Intent(action);
    	List<ResolveInfo> list = 
    			packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
    	return list.size() > 0;
    }
    
}
