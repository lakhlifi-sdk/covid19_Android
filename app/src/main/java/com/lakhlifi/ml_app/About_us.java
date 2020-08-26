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

        String desc="Our story began in 2009 when Marko Anastasov and Darko Fabijan, two friends from university, set up the company while working at the same time on their social web startup and for clients. From the beginning we were passionate about both making software and the effects great products have on people. We have combined those in creating Semaphore, which we launched in 2012, and transitioned to become a product company. Now that we are responsible for a service used in over 100 countries, we feel like we're only getting started. ";

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
//                    .addInstagram("medyo80")

                .create();

        LinearLayout Linear_about =(LinearLayout)findViewById(R.id.aboutus);
        Linear_about.addView(aboutPage);

    }
}


