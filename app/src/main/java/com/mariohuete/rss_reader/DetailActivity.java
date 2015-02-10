package com.mariohuete.rss_reader;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.koushikdutta.ion.Ion;
import com.mariohuete.rss_reader.utils.Connect;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * Created by mariobama on 10/02/15.
 */
public class DetailActivity extends Activity {

    //ATTRIBUTES:
    private String URL_PHOTOS;// = MainActivity.END_POINT+"/photos/";
    private String name;
    private String desc;
    private String phot;
    private Intent browserIntent;
    @InjectView(R.id.nameView) protected TextView nameView;
    @InjectView(R.id.descView) protected TextView descView;
    @InjectView(R.id.photoView) protected ImageView photView;
    @InjectView(R.id.button) protected Button buttonView;

    //METHODS:
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        URL_PHOTOS = getString(R.string.end_point1)+getString(R.string.photos1);
        ButterKnife.inject(this);
        // Get data from previous activity
        name = getIntent().getExtras().getString("mName");
        desc = getIntent().getExtras().getString("mDesc");
        phot = getIntent().getExtras().getString("mPhot");
        nameView.setText(name);
        descView.setText(desc);
        Ion.with(this)
                .load((URL_PHOTOS+phot).toString())
                .withBitmap()
                /*.placeholder(R.drawable.placeholder_image)*/
                .error(R.mipmap.ic_launcher)
                /*.animateLoad(spinAnimation)
                .animateIn(fadeInAnimation)*/
                .intoImageView(photView);
        buttonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Connect.isOnline(getApplicationContext())) {
                    // Intent for launch web browser
                    browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(URL_PHOTOS + phot));
                    startActivity(browserIntent);
                }
                else {

                }
            }
        });
    }
}
