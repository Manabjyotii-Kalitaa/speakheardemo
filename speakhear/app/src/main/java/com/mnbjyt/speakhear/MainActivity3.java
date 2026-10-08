package com.mnbjyt.speakhear;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebViewClient;

import com.mnbjyt.speakhear.databinding.ActivityMain3Binding;


public class MainActivity3 extends AppCompatActivity {
    ActivityMain3Binding activityMain3Binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        activityMain3Binding = DataBindingUtil.setContentView(this,R.layout.activity_main3);
    }
    @Override
    protected void onStart() {
        activityMain3Binding.policyWebview.setWebViewClient(new WebViewClient());
        activityMain3Binding.policyWebview.loadUrl("file:///android_asset/privacypolicy.html");
        WebSettings webSettings = activityMain3Binding.policyWebview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);
        activityMain3Binding.privacyBackImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        super.onStart();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}