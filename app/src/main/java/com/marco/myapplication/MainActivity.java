package com.marco.myapplication;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import android.app.Activity;

import android.content.Intent;

import android.provider.MediaStore;
import android.view.View;

import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;

public class MainActivity extends Activity implements OnClickListener{

    // this is the action code we use in our intent,
    // this way we know we're looking at the response from our own action
    private static final int SELECT_PICTURE = 1;

    private String selectedImagePath;

    private ImageView imageview;

    @Override

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        findViewById(R.id.share_button).setOnClickListener(this);

        imageview = (ImageView) findViewById(R.id.imageView1);

        ((Button) findViewById(R.id.photo_button))
                .setOnClickListener(new OnClickListener() {

                    public void onClick(View arg0) {

                        // in onCreate or any event where your want the user to
                        // select a file
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(Intent.createChooser(intent,
                                "Take a photo"), 0);
                    }
                });

        ((Button) findViewById(R.id.picture_button))
                .setOnClickListener(new OnClickListener() {

                    public void onClick(View arg0) {

                        // in onCreate or any event where your want the user to
                        // select a file
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent,
                                "Select Picture"), SELECT_PICTURE);
                    }
                });

    }

    @Override
    public void onClick(View v) {

        if (v.getId()==R.id.share_button) {

            Intent intent = new Intent(Intent.ACTION_SEND);

            intent.setType("image/*");

            intent.putExtra(Intent.EXTRA_TEXT, "http://www.somelink.com");

            intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Check out this site!");

            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(selectedImagePath)));// this is for image . here filename_toshare is your file path.

            startActivity(Intent.createChooser(intent, "Share"));



        }

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch(requestCode) {
            case 0:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = imageReturnedIntent.getData();
                    selectedImagePath = getPath(selectedImage);
                    imageview.setImageBitmap(ExifUtils.decodeFile(selectedImagePath));
                }

                break;
            case 1:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = imageReturnedIntent.getData();
                    selectedImagePath = getPath(selectedImage);
                    imageview.setImageBitmap(ExifUtils.decodeFile(selectedImagePath));
                }
                break;
           }
    }


    /**
     * helper to retrieve the path of an image URI
     */
    public String getPath(Uri uri) {
        // just some safety built in
        if( uri == null ) {
            // TODO perform some logging or show user feedback
            return null;
        }
        // try to retrieve the image from the media store first
        // this will only work for images selected from gallery
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        if( cursor != null ){
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        // this is our fallback here
        return uri.getPath();
    }



}
