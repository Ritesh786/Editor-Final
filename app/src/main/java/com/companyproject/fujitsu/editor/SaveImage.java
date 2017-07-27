package com.companyproject.fujitsu.editor;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.companyproject.fujitsu.editor.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;

public class SaveImage extends AppCompatActivity implements View.OnClickListener {

    String ntypestr, nhesdstr, mcontentstr, mcaptionstr, nidstr, image;
    URL url;
    ImageView mdownloadimg;
    Button mdownloadbtn, muploadnewsbtn;
    Bitmap bitmap;
    ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_image);

        mdownloadimg = (ImageView) findViewById(R.id.download_img);
        mdownloadbtn = (Button) findViewById(R.id.download_btn);
        muploadnewsbtn = (Button) findViewById(R.id.upload_btn);

        mdownloadbtn.setOnClickListener(this);
        muploadnewsbtn.setOnClickListener(this);

        Intent intent = getIntent();
        ntypestr = intent.getStringExtra("type");
        nhesdstr = intent.getStringExtra("headline");
        mcontentstr = intent.getStringExtra("content");
        mcaptionstr = intent.getStringExtra("caption");
        nidstr = intent.getStringExtra("id");
        image = intent.getStringExtra("image");
           Log.d("imgvalu00" , "img "+image);
        try {
            url = new URL(image);
           bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            Log.d("bitmp00","bmp "+bitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Picasso.with(getApplicationContext()).load(String.valueOf(url)).resize(1024, 1024).into(mdownloadimg);

    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.download_btn:

//                try {
//                    String Filename = String.valueOf(System.currentTimeMillis())+".png";
//                    saveImage(bitmap,Filename);
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                }
//                Toast.makeText(this,"Image Saved In Folder Editor...",Toast.LENGTH_SHORT).show();
//                break;

                pDialog = new ProgressDialog(SaveImage.this);
                pDialog.setMessage("Please Wait ...");
                pDialog.setIndeterminate(false);
                pDialog.setCancelable(true);
                pDialog.show();
                saveimg();


            case R.id.upload_btn:
                Intent newsestinnt = new Intent(SaveImage.this,UploadWithImg.class);
                newsestinnt.putExtra("id",nidstr);
                newsestinnt.putExtra("type",ntypestr);
                newsestinnt.putExtra("headline",nhesdstr);
                newsestinnt.putExtra("content",mcontentstr);
                newsestinnt.putExtra("caption",mcaptionstr);
                startActivity(newsestinnt);
                break;

        }
    }

    public  void saveImage(Bitmap image,String imagename) throws FileNotFoundException {
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory(),"Editor");
        String fileName=imagename;
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.e("App", "failed to create directory");
            }
        }
        else {

            File file = new File(mediaStorageDir.getAbsolutePath() + File.separator + fileName);
            FileOutputStream fo = new FileOutputStream(file);
            if (image != null) {
                image.compress(Bitmap.CompressFormat.PNG, 100, fo);
            }
        }
    }
//    class getadsdetail extends AsyncTask<String, Void, String> {
//        @Override
//        protected String doInBackground(String... params) {
//
//        }


    public void saveimg(){

        String  ImagePath = MediaStore.Images.Media.insertImage(
                getContentResolver(),
                bitmap,
                "demo_image",
                "demo_image"
        );

       // Uri  URI = Uri.parse(ImagePath);
        pDialog.dismiss();
        Toast.makeText(SaveImage.this, "Image Saved Successfully", Toast.LENGTH_LONG).show();

    }

}
