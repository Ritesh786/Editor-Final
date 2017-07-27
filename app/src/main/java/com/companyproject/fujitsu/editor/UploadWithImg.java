package com.companyproject.fujitsu.editor;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class UploadWithImg extends AppCompatActivity implements View.OnClickListener {

    EditText mnewstype,mnewsheadline,mnewscontent,mnewsimagecaption;
    Button mchooseimagebtn,muploadnewsbtn;
    ImageView mnewsimage;
    String ntypestr, nhesdstr, mcontentstr, mcaptionstr, nidstr, image;
    private int PICK_IMAGE_REQUEST = 1;
    private static final int PICK_CROPIMAGE = 4;
    private Bitmap bitmap;
    String strtim;

    public static final String KEY_ID= "id";
    public static final String KEY_HHEADLINE = "headline";
    public static final String KEY_CONTENT = "content";
    public static final String KEY_TYPE = "category";
    public static final String KEY_IMAGE = "image";
    public static final String KEY_CAPTION = "caption";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_with_img);

        mnewsheadline = (EditText) findViewById(R.id.news_heasdline);
        mnewscontent = (EditText) findViewById(R.id.news_content);
        mnewsimagecaption = (EditText) findViewById(R.id.news_caption);
        mnewstype = (EditText) findViewById(R.id.news_type);
        mnewsimage = (ImageView) findViewById(R.id.news_Image);

        mchooseimagebtn = (Button) findViewById(R.id.chooseimage_btn);
        muploadnewsbtn = (Button) findViewById(R.id.uploadnews_btn);

        mchooseimagebtn.setOnClickListener(this);
        muploadnewsbtn.setOnClickListener(this);

        Intent intent = getIntent();
        ntypestr = intent.getStringExtra("type");
        nhesdstr = intent.getStringExtra("headline");
        mcontentstr = intent.getStringExtra("content");
        mcaptionstr = intent.getStringExtra("caption");
        nidstr = intent.getStringExtra("id");
    //    image = intent.getStringExtra("image");

        mnewstype.setText(ntypestr);
        mnewsheadline.setText(nhesdstr);
        mnewscontent.setText(mcontentstr);
        mnewsimagecaption.setText(mcaptionstr);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.chooseimage_btn:

                showfilechooser();

                break;

            case R.id.uploadnews_btn:

               uploadImage();

                break;


        }
    }

    public void showfilechooser(){

        Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(i, ""), PICK_IMAGE_REQUEST);
    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 30, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }


    public void uploadImage() {

            final String id = String.valueOf(nidstr);
            final String headline = mnewsheadline.getText().toString().trim();
            final String content = mnewscontent.getText().toString().trim();
            final String type = mnewstype.getText().toString().trim();
            final String caption = mnewsimagecaption.getText().toString().trim();
            final String image = getStringImage(bitmap);


            String url = null;
            String REGISTER_URL = "http://excel.ap-south-1.elasticbeanstalk.com/news/editNewsWithImage.php";

            REGISTER_URL = REGISTER_URL.replaceAll(" ", "%20");
            try {
                URL sourceUrl = new URL(REGISTER_URL);
                url = sourceUrl.toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            final ProgressDialog loading = ProgressDialog.show(UploadWithImg.this, "Uploading...", "Please wait...", false, false);

            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("jabaed", id);
                            try {
                                JSONObject jsonresponse = new JSONObject(response);
                                boolean success = jsonresponse.getBoolean("success");

                                if (success) {

                                    Intent successin = new Intent(UploadWithImg.this,Dashboard.class);
                                    successin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    successin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(successin);
                                    UploadWithImg.this.finish();

                                } else {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(UploadWithImg.this);
                                    builder.setMessage("Upoading Failed")
                                            .setNegativeButton("Retry", null)
                                            .create()
                                            .show();

                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            Log.d("jabadi", headline);
                            loading.dismiss();
                            Toast.makeText(UploadWithImg.this, response.toString(), Toast.LENGTH_LONG).show();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("bada123ed", id);

                            loading.dismiss();
                            Toast.makeText(UploadWithImg.this, error.toString(), Toast.LENGTH_LONG).show();
                            Log.d("error1234", error.toString());

                        }
                    }) {


                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    //Adding parameters to request
                    params.put(KEY_ID, id);
                    params.put(KEY_HHEADLINE, headline);
                    params.put(KEY_CONTENT, content);
                    params.put(KEY_TYPE, type);
                    params.put(KEY_IMAGE, image);
                    params.put(KEY_CAPTION, caption);
                    return params;

                }

            };
            // stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            stringRequest.setRetryPolicy(
                    new DefaultRetryPolicy(
                            500000,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                    )
            );


            RequestQueue requestQueue = Volley.newRequestQueue(UploadWithImg.this);
            requestQueue.add(stringRequest);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==PICK_IMAGE_REQUEST) {

            if(data==null){

                Toast.makeText(UploadWithImg.this," Please Select Image For Uploading.... ",Toast.LENGTH_LONG).show();

            }else {
                Uri filePath = data.getData();
                Intent intentcrop = new Intent(UploadWithImg.this, CropImage.class);
                intentcrop.putExtra("ramji", filePath.toString());
                startActivityForResult(intentcrop, PICK_CROPIMAGE);
            }
        }


        if(requestCode==PICK_CROPIMAGE)
        {
            strtim = data.getStringExtra("cropimage");
            Log.d("imageindash","imageindd "+strtim);
            bitmap = StringToBitMap(strtim);
            Log.d("imageinbitmap","imageinbit "+bitmap);
            mnewsimage.setImageBitmap(bitmap);
        }
    }


    public Bitmap StringToBitMap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0,
                    encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

}
