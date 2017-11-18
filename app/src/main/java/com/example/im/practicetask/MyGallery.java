package com.example.im.practicetask;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Im on 16-11-2017.
 */

public class MyGallery extends MainActivity {
    FragmentManager manager = getFragmentManager();
    ResetDialogFragment dialog = new ResetDialogFragment();
    TextView info;
    Button createBn, viewBn, resetBn, logOutBn, gmaillogOutBn;
    ImageView pic;
    String name, profilePic, email;
    int req = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mygallery);
        info = (TextView) findViewById(R.id.info);
        logOutBn = (Button) findViewById(R.id.logOutBn);
        logOutBn.setOnClickListener(this);
        gmaillogOutBn = (Button) findViewById(R.id.gmailogOutBn);
        gmaillogOutBn.setOnClickListener(this);
        createBn = (Button) findViewById(R.id.createBn);
        createBn.setOnClickListener(this);
        viewBn = (Button) findViewById(R.id.viewBn);
        viewBn.setOnClickListener(this);
        resetBn = (Button) findViewById(R.id.resetbn);
        resetBn.setOnClickListener(this);
        pic = (ImageView) findViewById(R.id.pic);
        c = dbhelp.checkLogin();
        email = c.getEmail();
        profilePic = c.getPic();
        name = c.getName();
//         display(name,email,profilePic);
        info.setText(name + "\n" + email);
        if (c.getApp().equals("Google")) {
            if (profilePic.equals("Nopic")) {
                pic.setImageResource(R.drawable.noic1);
            } else {
                Glide.with(getApplicationContext()).load(profilePic)
                        .thumbnail(0.5f)
                        .crossFade()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .transform(new CircleTransform(MyGallery.this))
                        .into(pic);
            }
            gmaillogOutBn.setVisibility(View.VISIBLE);
        } else if (c.getApp().equals("Facebook")) {
            logOutBn.setVisibility(View.VISIBLE);
            Glide.with(getApplicationContext()).load(profilePic)
                    .thumbnail(0.5f)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .transform(new CircleTransform(MyGallery.this)) // applying the image transformer
                    .into(pic);


        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.logOutBn: {
                LoginManager.getInstance().logOut();  //logs user out of fb
                logOutBn.setVisibility(View.INVISIBLE);
                dbhelp.delete();
                Intent intent = new Intent(MyGallery.this, MainActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.gmailogOutBn: {
                signOut();
                gmaillogOutBn.setVisibility(View.INVISIBLE);
                dbhelp.delete();
                Intent intent = new Intent(MyGallery.this, MainActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.createBn: {
                openFileManager();
                break;
            }
            case R.id.viewBn: {
                Intent gotoViewGallery = new Intent(MyGallery.this, ViewGallery.class);
                startActivity(gotoViewGallery);
                break;
            }
            case R.id.resetbn: {
                dialog.show(manager, "YourDialog");
                break;
            }
        }
    }


    public void display(String name, String email, String profilePic) {
        info.setText(name + "\n" + email);

        Glide.with(getApplicationContext()).load(profilePic)
                .thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(pic);
    }


    void openFileManager() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, req);
    }

    ArrayList<String> imagearray = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Checks if the requestCode is same as req.
        if (requestCode == req && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                //Display an error
                return;
            }
//            ArrayList<String> imagearray = new ArrayList<>();
            ClipData clipData = data.getClipData();//Clipdata is a Complex type and can store instances of more than one item.
            //In Case of Single Image
            if (clipData == null) {
                Uri img = data.getData();
                imagearray.add(0, img.toString());
                c.setGalleryImage(img.toString());
                c.setDate(DateFormat.getDateTimeInstance().format(new Date()));
                dbhelp.insert(c);
            }
            //In Case of Multiple Images
            if (clipData != null) {
                for (int i = 0; i < clipData.getItemCount(); i++) {
                    ClipData.Item item = clipData.getItemAt(i);
                    Uri uri = item.getUri();       //Returns Item URI
                    imagearray.add(i, uri.toString());
                    c.setGalleryImage(uri.toString());
                    c.setDate(DateFormat.getDateTimeInstance().format(new Date()));
                    dbhelp.insert(c);
                }
            }
        }
    }
//public ArrayList<String> galleryimages(){
//    return imagearray;
//}
public void delGalleryImages()
{
  dbhelp.deleteGalleryImage();
}
}
