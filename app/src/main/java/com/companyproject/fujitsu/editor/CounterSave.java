package com.companyproject.fujitsu.editor;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Fujitsu on 25/06/2017.
 */

public class CounterSave {
    private static final String SHARED_PREF_NAME = "FCMSharedPref";
    private static final String TAG_TOKEN = "tagtoken";
    private static final String TAG_TOKEN2 = "tagtoken2";
    private static final String TAG_TOKEN3 = "tagtoken3";
    private static final String TAG_TOKENFCM = "tagtokenfcm";


    private static CounterSave mInstance;
    private static Context mCtx;

    private CounterSave(Context context) {
        mCtx = context;
    }

    public static synchronized CounterSave getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new CounterSave(context);
        }
        return mInstance;
    }

    //this method will save the device token to shared preferences
    public boolean saveDeviceToken(String pending,String approved,String disapproved){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TAG_TOKEN, pending);
        editor.putString(TAG_TOKEN2, approved);
        editor.putString(TAG_TOKEN3, disapproved);
        editor.apply();
        editor.commit();
        return true;
    }

    //this method will fetch the device token from shared preferences
    public String getDeviceToken1(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
      String s1 =  sharedPreferences.getString(TAG_TOKEN, "2");
        return  s1;
    }
    public String getDeviceToken2(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String s2 = sharedPreferences.getString(TAG_TOKEN2, "8");
        return s2;
    }
    public String getDeviceToken3(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String s3 =   sharedPreferences.getString(TAG_TOKEN3, "6");
        return s3;
    }

    public void Clearpend(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(TAG_TOKEN);
        editor.clear();
    }
    public void Clearver(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(TAG_TOKEN2);
        editor.clear();
    }
    public void Clearrej(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(TAG_TOKEN3);
        editor.clear();
    }

    public boolean saveToken(String token){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TAG_TOKENFCM, token);
        editor.apply();
        editor.commit();
        return true;
    }


    public String getToken(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return  sharedPreferences.getString(TAG_TOKENFCM, null);
    }

}

