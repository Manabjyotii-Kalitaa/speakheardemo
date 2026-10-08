package com.mnbjyt.speakhear;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import android.Manifest;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.material.snackbar.Snackbar;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.InstallState;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.OnSuccessListener;
import com.mnbjyt.speakhear.databinding.ActivityMain2Binding;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity2 extends AppCompatActivity {
    ActivityMain2Binding activityMain2Binding;
    private AppUpdateManager appUpdateManager;
    private static final int speakhearupdate = 100;
    int count=0;
    String speechresult;
    SpeechRecognizer speechRecognizer;
    TextToSpeech textToSpeech;
    Intent speechrecognizerIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        activityMain2Binding = DataBindingUtil.setContentView(this,R.layout.activity_main2);
        setSupportActionBar(activityMain2Binding.toolbar);
        appUpdateManager = AppUpdateManagerFactory.create(this);
        appUpdateManager.getAppUpdateInfo().addOnSuccessListener(new OnSuccessListener<AppUpdateInfo>() {
            @Override
            public void onSuccess(AppUpdateInfo result) {
                if (result.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE && result.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE))
                {
                    try {
                        appUpdateManager.startUpdateFlowForResult(result,AppUpdateType.FLEXIBLE, MainActivity2.this,speakhearupdate);
                    } catch (IntentSender.SendIntentException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        appUpdateManager.registerListener(installStateUpdatedListener);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, PackageManager.PERMISSION_GRANTED);
        }
        textToSpeech = new TextToSpeech(this, i -> {
            if (i == TextToSpeech.SUCCESS){
                int iresult = textToSpeech.setLanguage(Locale.ENGLISH);
                if (iresult == TextToSpeech.LANG_MISSING_DATA || iresult == TextToSpeech.LANG_NOT_SUPPORTED){
                    Toast.makeText(getApplicationContext(), "Not supported", Toast.LENGTH_SHORT).show();
                    activityMain2Binding.speaker.setEnabled(false);
                }else {
                    activityMain2Binding.speaker.setEnabled(true);
                }
            }else{
                Toast.makeText(getApplicationContext(), "Not supported", Toast.LENGTH_SHORT).show();
            }
        });

        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        speechrecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) {

            }

            @Override
            public void onBeginningOfSpeech() {

            }

            @Override
            public void onRmsChanged(float v) {

            }

            @Override
            public void onBufferReceived(byte[] bytes) {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int i) {
                activityMain2Binding.speaker.setEnabled(true);
                count=0;
            }

            @Override
            public void onResults(Bundle bundle) {
                ArrayList<String> data = bundle.getStringArrayList(speechRecognizer.RESULTS_RECOGNITION);
                speechresult = data.get(0);
                activityMain2Binding.speaker.setEnabled(true);
                count=0;
            }

            @Override
            public void onPartialResults(Bundle bundle) {

            }

            @Override
            public void onEvent(int i, Bundle bundle) {

            }
        });
        activityMain2Binding.microphone.setOnClickListener(view -> {
            if (count==0){
                speechRecognizer.startListening(speechrecognizerIntent);
                activityMain2Binding.speaker.setEnabled(false);
                count=1;
            }else{
                speechRecognizer.stopListening();
                count=0;
            }
        });
        activityMain2Binding.speaker.setOnClickListener(view -> {
            try {
                if (!speechresult.isEmpty()){
                    textToSpeech.speak(speechresult,TextToSpeech.QUEUE_FLUSH, null, null);
                    activityMain2Binding.speakerSpeech.setText(speechresult);
                }else{
                    Toast.makeText(getApplicationContext(), "Speak something first", Toast.LENGTH_SHORT).show();
                }
            }catch (Exception e){
                Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private InstallStateUpdatedListener installStateUpdatedListener = new InstallStateUpdatedListener() {
        @Override
        public void onStateUpdate(InstallState state) {
            if (state.installStatus()== InstallStatus.DOWNLOADED){
                showcompleteUpdate();
            }
        }
    };

    private void showcompleteUpdate() {
        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Update is ready to install ", Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction("Install", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                appUpdateManager.completeUpdate();
            }
        });
        snackbar.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == speakhearupdate && resultCode != RESULT_OK ){
            Toast.makeText(getApplicationContext(), "Cancelled", Toast.LENGTH_SHORT).show();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater= getMenuInflater();
        inflater.inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){

            case R.id.privacypolicy:
                startActivity(new Intent(this,MainActivity3.class));
                break;
            case R.id.helpcenter:
                startActivity(new Intent(this,MainActivity4.class));
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        if (textToSpeech!= null){
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        if (appUpdateManager !=null){
            appUpdateManager.unregisterListener(installStateUpdatedListener);
        }
        super.onStop();
    }
}