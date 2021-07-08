package com.example.sound;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import java.io.File;
import java.io.IOException;

public class MainActivity  extends AppCompatActivity {
    private ProgressDialog mprocess;
    TextView txt1;
    ImageButton recordButton,playButton;

    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private static String fileName = null;

    MediaRecorder recorder;
    MediaPlayer player;

    StorageReference storageRef = FirebaseStorage.getInstance().getReference();

    private boolean permissionToRecordAccepted = false;
    private String [] permissions = {Manifest.permission.RECORD_AUDIO,Manifest.permission.WRITE_EXTERNAL_STORAGE};

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQUEST_RECORD_AUDIO_PERMISSION:
                permissionToRecordAccepted  = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
        }
        if (!permissionToRecordAccepted ) finish();

    }


    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_main);
        // Record to the external cache directory for visibility
        fileName = getExternalCacheDir().getAbsolutePath();
        fileName += "/audiorecordtest.mp3";

        ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION);

        recordButton=(ImageButton) findViewById(R.id.btnRecord);
        playButton=(ImageButton) findViewById(R.id.btnPlay);
        txt1=(TextView)findViewById(R.id.txt);

        recordButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                   startRecording();

                    return true;
                }
                if(event.getAction() == MotionEvent.ACTION_UP){
                    stopRecording();

                    return true;
                }
                return false;
            }
        });


        playButton.setOnClickListener(new View.OnClickListener() {
            boolean mStartPlaying = true;
            @Override
            public void onClick(View v) {
                onPlay(mStartPlaying);
                if (mStartPlaying) {

                } else {

                }
                mStartPlaying = !mStartPlaying;
            }
        }); {

        }

    }
    private void startPlaying() {
        player = new MediaPlayer();
        try {
            player.setDataSource(fileName);
            player.prepare();
            player.start();
        } catch (IOException e) {

        }
    }


    private void startRecording() {
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setOutputFile(fileName);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_WB);

        try {
            recorder.prepare();
        } catch (IOException e) {

        }

        recorder.start();
    }

    private void stopRecording() {
        recorder.stop();
        recorder.release();
        recorder = null;

        uploadaudio();

    }

    private void uploadaudio() {

    StorageReference filepath = storageRef.child("Audio").child("new_audio.3gp");
    String path = getExternalCacheDir().getAbsolutePath();
    path += "/audiorecordtest.mp3";
    Uri uri = Uri.fromFile(new File((path)));
    filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
        @Override
        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
            txt1.setText("Upload Success......");
        }
    });


    }


    private void onPlay(boolean start) {
        if (start) {
            startPlaying();

        } else {
            stopPlaying();
        }
    }

    private void stopPlaying() {
        player.release();
        player = null;
        uploadaudio();
    }



}