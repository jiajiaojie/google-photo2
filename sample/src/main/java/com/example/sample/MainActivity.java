package com.example.sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.example.jiaojiejia.googlephoto.bean.GalleryBuilder;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTextView = findViewById(R.id.textView);
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GalleryBuilder.from(MainActivity.this)
                        .minPickCount(1)
                        .maxPickCount(10)
                        .start();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GalleryBuilder.REQUEST_CODE && data != null) {
            List<String> paths = GalleryBuilder.getPhotoCompressedPaths(data);
            mTextView.setText(paths.toString());
        }
    }
}
