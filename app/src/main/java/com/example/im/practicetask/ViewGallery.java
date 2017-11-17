package com.example.im.practicetask;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;

/**
 * Created by Im on 17-11-2017.
 */

public class ViewGallery extends AppCompatActivity {
    Contact c= new Contact();
    Databasehelper dbhelp= new Databasehelper(this);
//    MyGallery myGallery=new MyGallery();
    ViewPager viewPager;
    MyCustomPagerAdapter myCustomPagerAdapter;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewgallery);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        ArrayList<String> galleryimages;
        galleryimages=dbhelp.getGalleryImage();
        myCustomPagerAdapter = new MyCustomPagerAdapter(ViewGallery.this, galleryimages);
        viewPager.setAdapter(myCustomPagerAdapter);
    }
}
