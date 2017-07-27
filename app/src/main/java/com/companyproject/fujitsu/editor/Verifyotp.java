package com.companyproject.fujitsu.editor;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.telephony.SmsMessage;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Fujitsu on 10/06/2017.
 */

public class Verifyotp extends AppCompatActivity implements View.OnClickListener {


    static EditText mverifyotptext;
    String verifyotptxt;

    AppCompatButton mverifyotpbtn;

    BroadcastReceiver receiver = null;

    UserSessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verifyotp);

        IntentFilter filter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
        filter.setPriority(2147483647);

        receiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context arr, Intent brr) {

                processreceiver(arr,brr);

            }
        };

        registerReceiver(receiver,filter);

        session = new UserSessionManager(getApplicationContext());

        mverifyotptext = (EditText) findViewById(R.id.mobileotp);

        mverifyotpbtn = (AppCompatButton) findViewById(R.id.verifyotp_btn);
        mverifyotpbtn.setOnClickListener(this);



    }
    public void recivedSms(String message)
    {
        try
        {
            // String code = parseCode(message);
            mverifyotptext.setText(message.substring(27));
        }
        catch (Exception e)
        {
        }
    }

    private String parseCode(String message) {
        Pattern p = Pattern.compile("\\b\\d{6}\\b");
        Matcher m = p.matcher(message);
        String code = "";
        code = m.group(0);
        while (m.find()) {
        }
        return code;
    }

    @Override
    protected void onDestroy() {
        if (receiver != null) {
            unregisterReceiver(receiver);
            receiver = null;
        }
        super.onDestroy();
    }

    public void processreceiver(Context context , Intent intent) {

        Bundle data = intent.getExtras();

        Object[] pdus = (Object[]) data.get("pdus");
        String sms = "";

        for (int i = 0; i < pdus.length; i++) {
            SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdus[i]);

            String messageBody = smsMessage.getMessageBody();

            String sender = smsMessage.getDisplayOriginatingAddress();
            String senderNum = sender ;
            String asubstring = senderNum.substring(3);

            try
            {
                if (asubstring.equals("MITEST"))
                {
                    recivedSms(messageBody);
                    mverifyotpbtn.performClick();
                }
            }
            catch(Exception e){}


        }
    }


    @Override
    public void onClick(View v) {

        verifyotp();

    }

    private void verifyotp(){



        final String KEY_mobile = "otp";

        verifyotptxt = mverifyotptext.getText().toString().trim();

        if (TextUtils.isEmpty(verifyotptxt)) {
            mverifyotptext.requestFocus();
            mverifyotptext.setError("This Field Is Mandatory");
        }
        else{

            String url = null;
            String REGISTER_URL = "http://excel.ap-south-1.elasticbeanstalk.com/editor_login_verify.php";

            REGISTER_URL = REGISTER_URL.replaceAll(" ", "%20");
            try {
                URL sourceUrl = new URL(REGISTER_URL);
                url = sourceUrl.toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            //   Log.d("jaba", usernsme);
                            try {
                                JSONObject jsonresponse = new JSONObject(response);
                                boolean success = jsonresponse.getBoolean("success");

                                if (success) {

                                    String name = jsonresponse.getString("name");

                                    session.createUserLoginSession(name);

                                    Intent registerintent = new Intent(Verifyotp.this, Dashboard.class);
//                                    registerintent.putExtra("user_id",id);
//                                   Log.d("user1234","inte"+id);
                                    registerintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                                    // Add new Flag to start new Activity
                                    // registerintent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                    startActivity(registerintent);
                                    finish();



                                } else {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(Verifyotp.this);
                                    builder.setMessage("Registration Failed")
                                            .setNegativeButton("Retry", null)
                                            .create()
                                            .show();

                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            Toast.makeText(Verifyotp.this, response.toString(), Toast.LENGTH_LONG).show();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // Log.d("jabadi", usernsme);
                            Toast.makeText(Verifyotp.this, error.toString(), Toast.LENGTH_LONG).show();

                        }
                    }) {


                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    //Adding parameters to request


                    params.put(KEY_mobile, verifyotptxt);
                    return params;

                }

            };
            RequestQueue requestQueue = Volley.newRequestQueue(Verifyotp.this);
            requestQueue.add(stringRequest);
        }
    }


}


