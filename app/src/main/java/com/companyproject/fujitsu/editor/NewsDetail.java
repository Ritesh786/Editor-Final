package com.companyproject.fujitsu.editor;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class NewsDetail extends AppCompatActivity implements View.OnClickListener {

    TextView mtype, mheadline, mcontent, mcaption;
    ImageView mnewsimmage;
    String type, headline, content, caption, image,id;
    URL url;
    ImageView mbackomage;
    Button mverifiedbtn, meditbtn, mrejectedbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);

        mtype = (TextView) findViewById(R.id.newstype);
        mheadline = (TextView) findViewById(R.id.newsheadline);
        mcontent = (TextView) findViewById(R.id.newscontent);
//        mcaption = (TextView) findViewById(R.id.newsimgcaption);
        mnewsimmage = (ImageView) findViewById(R.id.newsimage);

        mverifiedbtn = (Button) findViewById(R.id.verified_btn);
        meditbtn = (Button) findViewById(R.id.edit_btn);
        mrejectedbtn = (Button) findViewById(R.id.rejected_btn);

        mverifiedbtn.setOnClickListener(this);
        mrejectedbtn.setOnClickListener(this);
        meditbtn.setOnClickListener(this);

        mbackomage = (ImageView) findViewById(R.id.back_image);
        mbackomage.setOnClickListener(this);

        Intent intent = getIntent();
        type = intent.getStringExtra("type");
        headline = intent.getStringExtra("headline");
        content = intent.getStringExtra("content");
         caption = intent.getStringExtra("caption");
        image = intent.getStringExtra("image");
        id = intent.getStringExtra("id");

        Log.d("con00","conte "+content);

        mtype.setText("News Type:" + type);
        mheadline.setText("News Headline:" + headline);
        mcontent.setText("News Content:" + content);
        //   mcaption.setText(caption);

        try {
            url = new URL(image);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Picasso.with(getApplicationContext()).load(String.valueOf(url)).resize(1024, 1024).into(mnewsimmage);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.back_image:

                NewsDetail.this.finish();
                break;

            case R.id.verified_btn:


                new AlertDialog.Builder(this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Confirmation Dialogue")
                        .setMessage("Are you sure you want to Verified this News?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                verified();

                            }

                        })
                        .setNegativeButton("No", null)
                        .show();




                break;

            case R.id.rejected_btn:

                new AlertDialog.Builder(this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Confirmation Dialogue")
                        .setMessage("Are you sure you want to Reject this News?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                rejected();

                            }

                        })
                        .setNegativeButton("No", null)
                        .show();

                break;

            case R.id.edit_btn:

                Intent newsestinnt = new Intent(NewsDetail.this,EditClass.class);
                newsestinnt.putExtra("id",id);
                newsestinnt.putExtra("type",type);
                newsestinnt.putExtra("headline",headline);
                newsestinnt.putExtra("content",content);
                newsestinnt.putExtra("caption",caption);
                newsestinnt.putExtra("image",image);
                startActivity(newsestinnt);
                break;

        }
    }


    private void verified(){

        final String KEY_id = "id";
        final String KEY_status = "status";
        final String VERSTATUS = "1";

        String url = null;
        String REGISTER_URL = "http://excel.ap-south-1.elasticbeanstalk.com/editor_post_edit.php";

        REGISTER_URL = REGISTER_URL.replaceAll(" ", "%20");
        try {
            URL sourceUrl = new URL(REGISTER_URL);
            url = sourceUrl.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, REGISTER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                           Log.d("jaba", String.valueOf(VERSTATUS));

                        Toast.makeText(NewsDetail.this, response.toString(), Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Log.d("jabadi", usernsme);
                        Toast.makeText(NewsDetail.this, error.toString(), Toast.LENGTH_LONG).show();

                    }
                }) {


            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //Adding parameters to request

                params.put(KEY_id, id);
                params.put(KEY_status, VERSTATUS);
                return params;

            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(NewsDetail.this);
        requestQueue.add(stringRequest);
    }

   public void rejected(){

       final String KEY_id = "id";
       final String KEY_status = "status";
       final String REJSTATUS = "0";

       String url = null;
       String REGISTER_URL = "http://excel.ap-south-1.elasticbeanstalk.com/editor_post_edit.php";

       REGISTER_URL = REGISTER_URL.replaceAll(" ", "%20");
       try {
           URL sourceUrl = new URL(REGISTER_URL);
           url = sourceUrl.toString();
       } catch (MalformedURLException e) {
           e.printStackTrace();
       }

       StringRequest stringRequest = new StringRequest(Request.Method.POST, REGISTER_URL,
               new Response.Listener<String>() {
                   @Override
                   public void onResponse(String response) {
                       Log.d("jaba000", String.valueOf(REJSTATUS));

                       Toast.makeText(NewsDetail.this, response.toString(), Toast.LENGTH_LONG).show();
                   }
               },
               new Response.ErrorListener() {
                   @Override
                   public void onErrorResponse(VolleyError error) {
                       // Log.d("jabadi", usernsme);
                       Toast.makeText(NewsDetail.this, error.toString(), Toast.LENGTH_LONG).show();

                   }
               }) {


           @Override
           protected Map<String, String> getParams() throws AuthFailureError {
               Map<String, String> params = new HashMap<>();
               //Adding parameters to request

               params.put(KEY_id, id);
               params.put(KEY_status, REJSTATUS);

               return params;

           }

       };
       RequestQueue requestQueue = Volley.newRequestQueue(NewsDetail.this);
       requestQueue.add(stringRequest);
   }


}


