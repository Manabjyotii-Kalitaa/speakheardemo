package com.mnbjyt.speakhear;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.pm.PackageInfoCompat;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.mnbjyt.speakhear.databinding.ActivityMain4Binding;

public class MainActivity4 extends AppCompatActivity {
    ActivityMain4Binding activityMain4Binding;
    public static String MY_VERSION_NAME;
    public static int MY_VERSION_CODE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        activityMain4Binding = DataBindingUtil.setContentView(this,R.layout.activity_main4);
    }

    @Override
    protected void onStart() {
        activityMain4Binding.helpBackImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        activityMain4Binding.linearLayout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Uri uri = Uri.parse("https://speak-hear.mystrikingly.com/");
                    Intent i = new Intent(Intent.ACTION_VIEW, uri);
                    if (i != null) {
                        startActivity(i);
                    } else {
                        Toast.makeText(getApplicationContext(), "Website is under maintenance", Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        activityMain4Binding.linearLayout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(MainActivity4.this);
                bottomSheetDialog.setContentView(R.layout.contact_form);
                bottomSheetDialog.setCanceledOnTouchOutside(false);
                TextView tv = bottomSheetDialog.findViewById(R.id.contact_us_textview);
                TextView bt = bottomSheetDialog.findViewById(R.id.send_textview);
                bt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            Uri uri = Uri.parse("https://speak-hear.mystrikingly.com/");
                            Intent i = new Intent(Intent.ACTION_VIEW, uri);
                            if (i != null) {
                                startActivity(i);
                            } else {
                                Toast.makeText(getApplicationContext(), "Website is under maintenance", Toast.LENGTH_SHORT).show();
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                });
                bottomSheetDialog.show();
            }
        });
        activityMain4Binding.linearLayout4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("plain/text");
                String body = "Share this app: ";
                String subbody = "https://play.google.com/store/apps/details?id="+getPackageName();
                share.putExtra(Intent.EXTRA_TEXT,body);
                share.putExtra(Intent.EXTRA_TEXT,subbody);
                startActivity(Intent.createChooser(share, "ShareVia"));
            }
        });
        activityMain4Binding.linearLayout3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(MainActivity4.this);
                bottomSheetDialog.setContentView(R.layout.version);
                bottomSheetDialog.setCanceledOnTouchOutside(false);
               // bottomSheetDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
               // bottomSheetDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.bg_dialog));
                PackageManager manager = getApplicationContext().getPackageManager();
                TextView tv1 = bottomSheetDialog.findViewById(R.id.appinfo_name_textview);
                TextView tv2 = bottomSheetDialog.findViewById(R.id.appinfo_version_textview);
                TextView tv3 = bottomSheetDialog.findViewById(R.id.appinfo_reserved_textview);
                ImageView im = bottomSheetDialog.findViewById(R.id.appinfo_image);
                try {
                    PackageInfo info = manager.getPackageInfo(getApplicationContext().getPackageName(), 0);
                    MY_VERSION_NAME = info.versionName;
                    MY_VERSION_CODE = (int) PackageInfoCompat.getLongVersionCode(info);
                    tv2.setText("VERSION: " +MY_VERSION_NAME + MY_VERSION_CODE);
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                    MY_VERSION_NAME = "Unknown-01";
                    tv2.setText(MY_VERSION_NAME);
                }
                bottomSheetDialog.show();
            }
        });
        super.onStart();
    }
}