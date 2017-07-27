package com.companyproject.fujitsu.editor;


import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class BlankFragment extends Fragment implements View.OnClickListener {

    Button mpending, mverified, mrejected;
    TextView mpendingtxt, mverifiedtxt, mrejectedtxt;

    String pend, ver, rej;

    SharedPreferences pref;

    public static final String JSON_URL = "http://excel.ap-south-1.elasticbeanstalk.com/lumen/public/counter/posts";

    public BlankFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_blank, container, false);

        mpendingtxt = (TextView) view.findViewById(R.id.pending_txt);
        mverifiedtxt = (TextView) view.findViewById(R.id.verified_txt);
        mrejectedtxt = (TextView) view.findViewById(R.id.rejected_txt);

        mpending = (Button) view.findViewById(R.id.pending);
        mverified = (Button) view.findViewById(R.id.verified);
        mrejected = (Button) view.findViewById(R.id.rejected);

        mpending.setOnClickListener(this);
        mverified.setOnClickListener(this);
        mrejected.setOnClickListener(this);

        getcounter();

    //    Toast.makeText(getContext(),pend+ver+rej,Toast.LENGTH_SHORT).show();



        return view;
    }

    @Override
    public void onClick(View view) {


        switch (view.getId()) {

            case R.id.pending:
                CounterSave.getInstance(getContext()).Clearpend();
                Intent pendingintent = new Intent(getContext(), Pending.class);
                startActivity(pendingintent);
                mpendingtxt.setVisibility(View.INVISIBLE);

                break;
            case R.id.verified:
                CounterSave.getInstance(getContext()).Clearver();
                Intent verifiedint = new Intent(getContext(), Verified.class);
                startActivity(verifiedint);
                mverifiedtxt.setVisibility(View.INVISIBLE);

                break;

            case R.id.rejected:
                CounterSave.getInstance(getContext()).Clearrej();
                Intent rejectedint = new Intent(getContext(), Rejected.class);
                startActivity(rejectedint);
                mrejectedtxt.setVisibility(View.INVISIBLE);


                break;

        }
    }

    public void getcounter(){

        StringRequest stringRequest = new StringRequest(JSON_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject obj = new JSONObject(response);
                            String pending = obj.getString("Pending");
                            String approved = obj.getString("Approved");
                            String disapproved = obj.getString("Disapproved");


                            mpendingtxt.setText(pending);
                            mverifiedtxt.setText(approved);
                            mrejectedtxt.setText(disapproved);

//                            CounterSave.getInstance(getContext()).saveDeviceToken(pending,approved,disapproved);
//                            Log.d("pendvarrej00","val "+pending+approved+disapproved);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //     Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
        {

            RequestQueue requestQueue = Volley.newRequestQueue(getContext());
            requestQueue.add(stringRequest);
        }

//        pend = CounterSave.getInstance(getContext()).getDeviceToken1();
//        ver = CounterSave.getInstance(getContext()).getDeviceToken2();
//        rej = CounterSave.getInstance(getContext()).getDeviceToken3();




    }

    }



