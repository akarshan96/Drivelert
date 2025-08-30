package com.example.akarshan.drivelert;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.Typeface;
import android.hardware.Camera;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import com.google.android.material.snackbar.Snackbar;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.MultiProcessor;
import com.google.android.gms.vision.Tracker;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;
import com.example.akarshan.drivelert.ui.camera.CameraSourcePreview;
import com.example.akarshan.drivelert.ui.camera.GraphicOverlay;
import com.google.android.gms.vision.face.Landmark;

import java.io.IOException;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;

/**
 * Activity for the face tracker app.  This app detects faces with the rear facing camera, and draws
 * overlay graphics to indicate the position, size, and ID of each face.
 */
public final class FaceTrackerActivity extends AppCompatActivity {
    private static final String TAG = "FaceTracker";

    private CameraSource mCameraSource = null;
    private Button end_button;
    private ToggleButton n_mode;
    private TextView tv,tv_1,tv_2;
    static int count = 0,count1=0;
    private LinearLayout layout;
    private MediaPlayer mp;
    private CameraSourcePreview mPreview;
    private GraphicOverlay mGraphicOverlay;
    private String start_2;
    private String key ="facetrackeractivity";
    private String key_2 = "akarshan's project";
    private String key_3 = "hello";
    private String key_4 = "senstivity";
    private int s_status,s_time;
    private static final int RC_HANDLE_GMS = 9001;
    // permission request codes need to be < 256
    private static final int RC_HANDLE_CAMERA_PERM = 2;
    public int flag = 0;

    //==============================================================================================
    // Activity Methods
    //==============================================================================================
    /**
     * Initializes the UI and initiates the creation of a face detector.
     */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.main);
        mPreview = (CameraSourcePreview) findViewById(R.id.preview);
        mGraphicOverlay = (GraphicOverlay) findViewById(R.id.faceOverlay);
        end_button = (Button)findViewById(R.id.button);
        layout = (LinearLayout)findViewById(R.id.topLayout);
        n_mode=(ToggleButton)findViewById(R.id.toggleButton);
        n_mode.setTextOn("N-Mode ON");
        n_mode.setText("N-Mode");
        n_mode.setTextOff("N-Mode OFF");
        n_mode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    mPreview.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(),"Increase Brightness to maximum for higher accuracy",Toast.LENGTH_LONG).show();

                }
                else
                {
                    mPreview.setVisibility(View.VISIBLE);
                }
            }
        });
        tv = (TextView)findViewById(R.id.textView3);
        tv_1 = (TextView)findViewById(R.id.textView4);
        final AudioManager audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        int c = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        if(c==0)
        {
            Toast.makeText(getApplicationContext(),"Volume is MUTE",Toast.LENGTH_LONG).show();
        }
        Intent intent_2 = getIntent();
        final String start = intent_2.getStringExtra(key_2);
        start_2=start;
        String time_info = intent_2.getStringExtra(key_4);
        s_status = Integer.parseInt(time_info);
        if(s_status == 0)
        {
            s_time = 500;
        }
        else if(s_status == 1)
        {
            s_time = 750;
        }
        else if(s_status == 2)
        {
            s_time = 1000;
        }
        else if(s_status == 3)
        {
            s_time = 1250;
        }
        else if(s_status == 4)
        {
            s_time = 1500;
        }
        else if(s_status == 5)
        {
            s_time = 1750;
        }
        else if(s_status == 6)
        {
            s_time = 2000;
        }
        else if(s_status == 7)
        {
            s_time = 2250;
        }
        else if(s_status == 8)
        {
            s_time = 2500;
        }


        View decorview = getWindow().getDecorView(); //hide navigation bar
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        decorview.setSystemUiVisibility(uiOptions);

        end_button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Intent next = new Intent(FaceTrackerActivity.this,end.class);
                count=0;
                count1=0;
                next.putExtra(key_3,start);
                next.putExtra(key,tv_1.getText());
                next.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(next);
                FaceTrackerActivity.this.finish();
                return false;
            }
        });



                // Check for the camera permission before accessing the camera.  If the
        // permission is not granted yet, request permission.
        int rc = ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        if (rc == PackageManager.PERMISSION_GRANTED) {
            createCameraSource();
        } else {
            requestCameraPermission();
        }
    }
    
    private void requestCameraPermission() {
        Log.w(TAG, "Camera permission is not granted. Requesting permission");

        final String[] permissions = new String[]{Manifest.permission.CAMERA};

        if (!ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.CAMERA)) {
            ActivityCompat.requestPermissions(this, permissions, RC_HANDLE_CAMERA_PERM);
            return;
        }

        final Activity thisActivity = this;

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(thisActivity, permissions,
                        RC_HANDLE_CAMERA_PERM);
            }
        };

        Snackbar.make(mGraphicOverlay, R.string.permission_camera_rationale,
                Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.ok, listener)
                .show();

    }

    private void createCameraSource() {

        Context context = getApplicationContext();
        FaceDetector detector = new FaceDetector.Builder(context)
                .setClassificationType(FaceDetector.ALL_CLASSIFICATIONS)
                .build();

        detector.setProcessor(
                new MultiProcessor.Builder<>(new GraphicFaceTrackerFactory())
                        .build());

        if (!detector.isOperational()) {
            // Note: The first time that an app using face API is installed on a device, GMS will
            // download a native library to the device in order to do detection.  Usually this
            // completes before the app is run for the first time.  But if that download has not yet
            // completed, then the above call will not detect any faces.
            //
            // isOperational() can be used to check if the required native library is currently
            // available.  The detector will automatically become operational once the library
            // download completes on device.
            Toast.makeText(getApplicationContext(),"Dependencies are not yet available. ",Toast.LENGTH_LONG).show();
            Log.w(TAG, "Face detector dependencies are not yet available.");
        }

        mCameraSource = new CameraSource.Builder(context, detector)
                .setRequestedPreviewSize(640, 480)
                .setFacing(CameraSource.CAMERA_FACING_FRONT)
                .setRequestedFps(45.0f)
                .build();

    }

    
    @Override
    protected void onResume() {
        super.onResume();
        startCameraSource();

    }

    @Override
    protected void onPause() {
        super.onPause();
        mPreview.stop();
    }
   
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCameraSource != null) {
            mCameraSource.release();
        }
        stop_playing();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode != RC_HANDLE_CAMERA_PERM) {
            Log.d(TAG, "Got unexpected permission result: " + requestCode);
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            return;
        }

        if (grantResults.length != 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "Camera permission granted - initialize the camera source");
            // we have permission, so create the camerasource
            createCameraSource();
            return;
        }

        Log.e(TAG, "Permission not granted: results len = " + grantResults.length +
                " Result code = " + (grantResults.length > 0 ? grantResults[0] : "(empty)"));

        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                finish();
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("ALERT")
                .setMessage(R.string.no_camera_permission)
                .setPositiveButton(R.string.ok, listener)
                .show();
    }

    private void startCameraSource() {

        // check that the device has play services available.
        int code = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(
                getApplicationContext());
        if (code != ConnectionResult.SUCCESS) {
            Dialog dlg =
                    GoogleApiAvailability.getInstance().getErrorDialog(this, code, RC_HANDLE_GMS);
            dlg.show();
        }

        if (mCameraSource != null) {
            try {
                mPreview.start(mCameraSource, mGraphicOverlay);
            } catch (IOException e) {
                Log.e(TAG, "Unable to start camera source.", e);
                mCameraSource.release();
                mCameraSource = null;
            }
        }
    }

    public static int incrementer()
    {
        count++;
        return(count);
    }
    public static int incrementer_1()
    {
        count1++;
        return(count1);
    }
    public static int get_incrementer()
    {
        return(count);
    }

    public void play_media()
    {
        stop_playing();
        mp = MediaPlayer.create(this, R.raw.alarm);
        mp.start();
    }
    public void stop_playing()
    {
        if (mp != null) {
            mp.stop();
            mp.release();
            mp = null;
        }
    }

    public void alert_box()
    {   play_media();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                play_media();
                AlertDialog dig;
                dig = new AlertDialog.Builder(FaceTrackerActivity.this)
                        .setTitle("Drowsy Alert !!!")
                        .setMessage("Tracker suspects that the driver is experiencing Drowsiness, Touch OK to Stop the Alarm")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                stop_playing();
                                flag = 0;
                            }
                        }).setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
                dig.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        stop_playing();
                        flag = 0;
                    }
                });
            }
        });


    }

    // Graphic Face Tracker

    private class GraphicFaceTrackerFactory implements MultiProcessor.Factory<Face> {
        @Override
        public Tracker<Face> create(Face face) {
            return new GraphicFaceTracker(mGraphicOverlay);
        }
    }

    private class GraphicFaceTracker extends Tracker<Face> {
        private GraphicOverlay mOverlay;
        private FaceGraphic mFaceGraphic;

        GraphicFaceTracker(GraphicOverlay overlay) {
            mOverlay = overlay;
            mFaceGraphic = new FaceGraphic(overlay);
        }

        /**
         * Start tracking the detected face instance within the face overlay.
         */
        @Override
        public void onNewItem(int faceId, Face item) {
            mFaceGraphic.setId(faceId);
        }


        int state_i,state_f=-1;
        long start,end=System.currentTimeMillis();
        long begin,stop;
        int c;

        @Override
        public void onUpdate(FaceDetector.Detections<Face> detectionResults, Face face) {
            mOverlay.add(mFaceGraphic);
            mFaceGraphic.updateFace(face);
            if (flag == 0)
            {
                eye_tracking(face);
            }
        }

        @Override
        public void onMissing(FaceDetector.Detections<Face> detectionResults) {
            mOverlay.remove(mFaceGraphic);
            setText(tv_1,"Face Missing");

        }

        @Override
        public void onDone() {
            mOverlay.remove(mFaceGraphic);
        }

        private void setText(final TextView text,final String value){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    text.setText(value);
                }
            });
        }

        private void eye_tracking(Face face)
        {
            float l = face.getIsLeftEyeOpenProbability();
            float r = face.getIsRightEyeOpenProbability();
            if(l<0.50 && r<0.50)
            {
                state_i = 0;
            }
            else
            {
                state_i = 1;
            }
            if(state_i != state_f)
            {
                start = System.currentTimeMillis();
                if(state_f==0)
                {
                    c = incrementer_1();

                }
                end = start;
                stop = System.currentTimeMillis();
            }
            else if (state_i == 0 && state_f ==0 ) {
                begin = System.currentTimeMillis();
                if(begin - stop > s_time )
                {
                    c = incrementer();
                    alert_box();
                    flag = 1;
                }
                begin = stop;
            }
            state_f = state_i;
            status();
        }
        public void status()
        {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    int s = get_incrementer();
                    if(s<5)
                    {
                        setText(tv_1,"Active");
                        tv_1.setTextColor(Color.GREEN);
                        tv_1.setTypeface(Typeface.DEFAULT_BOLD);
                    }
                    if(s>4 )
                    {
                        setText(tv_1,"Sleepy");
                        tv_1.setTextColor(Color.YELLOW);
                        tv_1.setTypeface(Typeface.DEFAULT_BOLD);
                    }
                    if(s>8)
                    {
                        setText(tv_1,"Drowsy");
                        tv_1.setTextColor(Color.RED);
                        tv_1.setTypeface(Typeface.DEFAULT_BOLD);
                    }


                }
            });

    }

    }
    @Override
    public void onBackPressed() {
        Intent next = new Intent(FaceTrackerActivity.this,end.class);
        count=0;
        count1=0;
        next.putExtra(key_3,start_2);
        next.putExtra(key,tv_1.getText());
        next.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(next);
        FaceTrackerActivity.this.finish();
    }


}



