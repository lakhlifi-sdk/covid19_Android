package com.lakhlifi.ml_app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.view.View;
import android.widget.LinearLayout;

import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;


public class About_us extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);


        String desc="This application is produced as part of the machine and deep learning mudule project.\n" +
                "The team of develepement is\n" +
                "-  HILALI Azzeddine    -  OUBAHA RACHID\n" +
                "-  LAKHLIFI Esseddiq    -  SABBARI Abdelah\n"+
                "-  OUCHATTI Khadija";
        Element versionElement = new Element();
        versionElement.setTitle("Version 0.2");
        versionElement.setIconDrawable(R.drawable.ic_baseline_developer_mode_24);

        View aboutPage = new AboutPage(this)
                .isRTL(false)
                .addItem(versionElement)
                .setImage(R.drawable.covid_logo)
                .addGroup("Connect with us")
                .addEmail("sim.fpt@usmba.ac.ma","Email")
                .setDescription(desc)
                .addWebsite("http://lakhlifisys.wordpress.com/","Website")
                .addFacebook("http://lakhlifisys.wordpress.com/")
                .addTwitter("http://lakhlifisys.wordpress.com/")
                .addYoutube("UCvBNh_qi1PrX4Y8kDKrkOgQ")
//                    .addPlayStore("com.ideashower.readitlater.pro")
                .addGitHub("lakhlifi-sdk")
        //        .addInstagram("https://www.instagram.com/ouchatti_khadija/")

                .create();

        LinearLayout Linear_about =(LinearLayout)findViewById(R.id.aboutus);
        Linear_about.addView(aboutPage);
    }
}


