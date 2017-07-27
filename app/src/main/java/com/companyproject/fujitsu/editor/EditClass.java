package com.companyproject.fujitsu.editor;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class EditClass extends AppCompatActivity implements View.OnClickListener {

    EditText mnewstypeedit,mnewsheadlineedit,mnewscontentedit,mnewscaptionedit;
    String ntypestr,nhesdstr,mcontentstr,mcaptionstr,nidstr,image;

    Button muolpadeditbtn,meditwithimgbtn;

    public static final String KEY_ID= "id";
    public static final String KEY_HHEADLINE = "headline";
    public static final String KEY_CONTENT = "content";
    public static final String KEY_TYPE = "category";
   // public static final String KEY_IMAGE = "image";
    public static final String KEY_CAPTION = "caption";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_class);

        mnewstypeedit = (EditText) findViewById(R.id.newstype_edit);
        mnewsheadlineedit = (EditText) findViewById(R.id.newsheadline_edit);
        mnewscontentedit = (EditText) findViewById(R.id.newscontent_edit);
        mnewscaptionedit= (EditText) findViewById(R.id.newscaption_edit);

        muolpadeditbtn = (Button) findViewById(R.id.uploadedt_btn);
        meditwithimgbtn = (Button) findViewById(R.id.editwithimg_btn);
        muolpadeditbtn.setOnClickListener(this);
        meditwithimgbtn.setOnClickListener(this);

        Intent intent = getIntent();
        ntypestr = intent.getStringExtra("type");
        nhesdstr = intent.getStringExtra("headline");
        mcontentstr = intent.getStringExtra("content");
        mcaptionstr = intent.getStringExtra("caption");
        nidstr = intent.getStringExtra("id");
        image = intent.getStringExtra("image");

        mnewstypeedit.setText(ntypestr);
        mnewsheadlineedit.setText(nhesdstr);
        mnewscontentedit.setText(mcontentstr);
        mnewscaptionedit.setText(mcaptionstr);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.uploadedt_btn:

                Editedupload();

                break;

            case R.id.editwithimg_btn:

                Intent newsestinnt = new Intent(EditClass.this,SaveImage.class);
                newsestinnt.putExtra("id",nidstr);
                newsestinnt.putExtra("type",ntypestr);
                newsestinnt.putExtra("headline",nhesdstr);
                newsestinnt.putExtra("content",mcontentstr);
                newsestinnt.putExtra("caption",mcaptionstr);
                newsestinnt.putExtra("image",image);
                startActivity(newsestinnt);
                break;

        }

    }

    public void Editedupload(){


        final String type = mnewstypeedit.getText().toString();
        final String headline = mnewsheadlineedit.getText().toString();
        final String content = mnewscontentedit.getText().toString();
        final String caption = mnewscaptionedit.getText().toString();
        final String idstr = nidstr;

        String url = null;
        String REGISTER_URL = "http://excel.ap-south-1.elasticbeanstalk.com/news/editNews.php";

        REGISTER_URL = REGISTER_URL.replaceAll(" ", "%20");
        try {
            URL sourceUrl = new URL(REGISTER_URL);
            url = sourceUrl.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        final ProgressDialog loading = ProgressDialog.show(EditClass.this, "Uploading...", "Please wait...", false, false);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("jabaedit", idstr);
                        try {
                            JSONObject jsonresponse = new JSONObject(response);
                            boolean success = jsonresponse.getBoolean("success");

                            if (success) {

                                Intent successin = new Intent(EditClass.this,Dashboard.class);
                                startActivity(successin);

                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(EditClass.this);
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
                        Toast.makeText(EditClass.this, response.toString(), Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("bada123", idstr);

                        loading.dismiss();
                        Toast.makeText(EditClass.this, error.toString(), Toast.LENGTH_LONG).show();
                        Log.d("error1234", error.toString());

                    }
                }) {


            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //Adding parameters to request
                params.put(KEY_ID, idstr);
                params.put(KEY_HHEADLINE, headline);
                params.put(KEY_CONTENT, content);
                params.put(KEY_TYPE, type);
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


        RequestQueue requestQueue = Volley.newRequestQueue(EditClass.this);
        requestQueue.add(stringRequest);



    }

}
