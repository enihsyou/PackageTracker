package com.enihsyou.shane.packagetracker.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import com.enihsyou.shane.packagetracker.R;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        TextView textView3 = (TextView)findViewById(R.id.textView3);
        TextView textView4 = (TextView)findViewById(R.id.textView4);
        TextView textView5 = (TextView)findViewById(R.id.textView5);
        TextView textView6 = (TextView)findViewById(R.id.textView6);

        textView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent web = new Intent(Intent.ACTION_VIEW);
                web.setData(Uri.parse("https://tsubasa.moe/"));
                startActivity(web);
            }
        });
        textView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent web = new Intent(Intent.ACTION_VIEW);
                web.setData(Uri.parse("https://github.com/Sleaf"));
                startActivity(web);
            }
        });
        textView5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent web = new Intent(Intent.ACTION_VIEW);
                web.setData(Uri.parse("https://github.com/rainbow-0926"));
                startActivity(web);
            }
        });
        textView6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent web = new Intent(Intent.ACTION_VIEW);
                web.setData(Uri.parse("https://github.com/cvb628"));
                startActivity(web);
            }
        });
    }
}
