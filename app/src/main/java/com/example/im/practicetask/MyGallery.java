package com.example.im.practicetask;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;

/**
 * Created by Im on 16-11-2017.
 */

public class MyGallery extends MainActivity {
TextView info;
 Button createBn,viewBn,resetBn,logOutBn;
    ImageView pic;
     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mygallery);
         info= (TextView) findViewById(R.id.info);
         logOutBn= (Button) findViewById(R.id.logOutBn);
         logOutBn.setOnClickListener(this);
         createBn= (Button) findViewById(R.id.createBn);
         createBn.setOnClickListener(this);
         viewBn= (Button) findViewById(R.id.viewBn);
         viewBn.setOnClickListener(this);
         resetBn= (Button) findViewById(R.id.resetbn);
         resetBn.setOnClickListener(this);
         pic= (ImageView) findViewById(R.id.pic);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.logOutBn: {
                LoginManager.getInstance().logOut();  //logs user out of fb
                Intent intent = new Intent(MyGallery.this, MainActivity.class);
                startActivity(intent);
                break;
            }

        }
    }

                public void display(String name, String email, String profilePic)
    {
       info.setText(name+"\n" +email );

        Glide.with(getApplicationContext()).load(profilePic)
                .thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(pic);
    }

}
