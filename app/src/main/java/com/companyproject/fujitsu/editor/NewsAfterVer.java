package com.companyproject.fujitsu.editor;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.net.MalformedURLException;
import java.net.URL;

public class NewsAfterVer extends AppCompatActivity implements View.OnClickListener {

    TextView mtype, mheadline, mcontent, mcaption;
    ImageView mnewsimmage;
    String type, headline, content, caption, image,id;
    URL url;
    ImageView mbackomage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_after_ver);

        mtype = (TextView) findViewById(R.id.newstype);
        mheadline = (TextView) findViewById(R.id.newsheadline);
        mcontent = (TextView) findViewById(R.id.newscontent);
        //   mcaption = (TextView) findViewById(R.id.newsimgcaption);
        mnewsimmage = (ImageView) findViewById(R.id.newsimage);

        mbackomage = (ImageView) findViewById(R.id.back_image);
        mbackomage.setOnClickListener(this);

        Intent intent = getIntent();
        type = intent.getStringExtra("type");
        headline = intent.getStringExtra("headline");
        content = intent.getStringExtra("content");
        // caption = intent.getStringExtra("caption");
        image = intent.getStringExtra("image");
        id = intent.getStringExtra("id");

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
    public void onClick(View v) {
        NewsAfterVer.this.finish();
    }
}
