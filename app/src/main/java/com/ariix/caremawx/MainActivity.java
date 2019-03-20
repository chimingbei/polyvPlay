package com.ariix.caremawx;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.ariix.caremawx.utils.UploadUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import java.io.File;
import java.io.FileInputStream;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int PERMISSIONS_OPEN_CAMERA_CODE = 1;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.bt1).setOnClickListener(this);
        findViewById(R.id.bt2).setOnClickListener(this);

        imageView = findViewById(R.id.img_video);

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO
                            , Manifest.permission.READ_EXTERNAL_STORAGE
                            ,Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSIONS_OPEN_CAMERA_CODE);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt1:
                 CameraActivity.lanuchForPhoto(this);

                break;
            case R.id.bt2:
                Intent intent = new Intent(MainActivity.this, MediaPlayerActivity.class);
                startActivity(intent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSIONS_OPEN_CAMERA_CODE) {
            //CameraActivity.lanuchForPhoto(this);
        }
    }

    @Override
    protected void onActivityResult(final int requestCode, int resultCode, @Nullable Intent data) {
        Log.d("MainActivity", resultCode + "-------------------");
        if(resultCode ==2) {
            final String path = data.getStringExtra("recorderPath");
            Log.d("MainActivity", path);

            SimpleTarget<Drawable> simpleTarget = new SimpleTarget<Drawable>() {
                @Override
                public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                    Bitmap bg = ((BitmapDrawable)resource).getBitmap();
//                    Bitmap fg = BitmapFactory.decodeResource(getResources(), R.drawable.ic_play_circle_outline_black_24dp);
                    Bitmap fg = ((BitmapDrawable)getResources().getDrawable(R.drawable.ic_action_name)).getBitmap();

                    float scaleWidth = 8.0f;
                    float scaleHeight = 8.0f;

                    Matrix matrix = new Matrix();
                    matrix.postScale(scaleWidth,scaleHeight);
                    fg=Bitmap.createBitmap(fg,0,0,fg.getWidth(),fg.getHeight(),matrix,true);

                    int bgWidth = bg.getWidth();
                    int bgHeight = bg.getHeight();
                    int fgWidth = fg.getWidth();


                    Bitmap newbmp = Bitmap.createBitmap(bgWidth, bgHeight, Bitmap.Config.RGB_565);
                    Canvas canvas = new Canvas(newbmp);
                    canvas.drawBitmap(bg, 0, 0, null);
                    canvas.drawBitmap(fg, (bgWidth - fgWidth) / 2, bgHeight / 2 -fg.getHeight()/2, null);


//                    Matrix matrix = new Matrix();
//                    matrix.setScale(0.5f, 0.5f);
//                    Bitmap combmp = Bitmap.createBitmap(newbmp, 0, 0, newbmp.getWidth(),
//                            newbmp.getHeight(), matrix, true);
                    Glide.with(MainActivity.this).load(newbmp).into(imageView);

                }
            };

            Glide.with(MainActivity.this).load(Uri.fromFile(new File(path))).into(simpleTarget);


            new Thread(
                    new Runnable() {
                        @Override
                        public void run() {
//                            UploadUtils.uploadToPolyV(new File(path), new BaseCallback(){
//                                @Override
//                                public void onSucceed(String status) {
//                                    Log.d("Upload", status);
//                                }
//
//                                @Override
//                                public void onFailed(String message) {
//                                    Log.d("Upload", message);
//                                }
//                            });

                           // String retval = UploadUtils.uploadToPolyV1(new File(path));
                            //Log.d("uploadret", retval + "--------------");
                        }
                    }
            ).start();
//            imageView.setImageDrawable(Drawable.createFromPath(path));
        }
    }
}
