package com.example.moshemandel.gifyourself;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class GifActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gif);
        Intent intent = getIntent();
        String imgPath = intent.getExtras().getString("imgPath");
        ServerComm serverComm = new ServerComm(this);
        serverComm.execute(imgPath);

       /* GifImageView gifImageView = (GifImageView) findViewById(R.id.GifImageView);
        gifImageView.setGifImageResource(R.drawable.simpsons);
        gifImageView.setVisibility(View.VISIBLE);*/
    }
}
