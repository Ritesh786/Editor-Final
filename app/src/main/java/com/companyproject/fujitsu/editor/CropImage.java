package com.companyproject.fujitsu.editor;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class CropImage extends AppCompatActivity {

    CropImageView cropImageView;
    LinearLayout cropBtn;
    boolean cameraSource;
    String filename = "";
    String photoUri;
    Bitmap bitmap;
    Bitmap cropped;
    public static final String PHOTO_URI = "photouri";
    private static final int PICK_CROPIMAGE = 4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop_image);


        cropImageView=(CropImageView)findViewById(R.id.cropImageView);
        cropBtn=(LinearLayout)findViewById(R.id.cropBtn);


        photoUri = getIntent().getStringExtra("ramji");
        Log.d("ph00","uri "+photoUri);
        try {
            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.parse(photoUri));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d("ph00001","uri11 "+bitmap);
        cropImageView.setImageBitmap(bitmap);


        cropBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cropImage(bitmap);
            }
        });
    }

    public void cropImage(Bitmap bitmap)

    {
        cropped = cropImageView.getCroppedImage();
        String bitimage = getStringImage(cropped);
        Intent returnIntent = new Intent();
        returnIntent.putExtra("cropimage", bitimage);
        setResult(PICK_CROPIMAGE, returnIntent);
        finish();
    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 30, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    @Override
    public void onBackPressed() {

    }
}