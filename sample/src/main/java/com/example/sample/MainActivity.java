package com.example.sample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.jiaojiejia.googlephoto.bean.GalleryBuilder;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GalleryBuilder.from(MainActivity.this)
                        .type(1)
                        .minPickPhoto(1)
                        .maxPickPhoto(10)
                        .start();
            }
        });
    }
}
