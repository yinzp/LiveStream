package com.blueberry.media;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;

// 视频角度问题：
//http://blog.csdn.net/veilling/article/details/52421930
//
public class MainActivity extends AppCompatActivity implements SurfaceHolder.Callback2 {

    private static final String TAG = "MainActivity";

    private Button btnToggle;
    private SurfaceView mSurfaceView;

    private SurfaceHolder mSurfaceHolder;
    private boolean isPublished;

    private MediaPublisher mMediaPublisher = new MediaPublisher();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i(TAG, "onCreate: ");
        initView();
        mMediaPublisher.init();
    }

    private void initView() {
        btnToggle = (Button) findViewById(R.id.btn_toggle);
        mSurfaceView = (SurfaceView) findViewById(R.id.surface_view);
        mSurfaceView.setKeepScreenOn(true);
        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.addCallback(this);
        btnToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchPublish();
            }
        });
    }

    private void switchPublish() {
        if (isPublished) {
            stop();
        } else {
            start();
        }
        btnToggle.setText(isPublished ? "停止" : "开始");
    }

    private void start() {
        //初始化声音采集
        mMediaPublisher.initAudioGatherer();
        //初始化编码器
        mMediaPublisher.initEncoders();
        //开始采集
        mMediaPublisher.startGather();
        //开始编码
        mMediaPublisher.startEncoder();
        //开始推送
        mMediaPublisher.starPublish();
        isPublished = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume: ");
        mMediaPublisher.initVideoGatherer(this, mSurfaceHolder);

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "onPause: ");
        stop();
    }

    private void stop() {
        mMediaPublisher.stopPublish();
        mMediaPublisher.stopGather();
        mMediaPublisher.stopEncoder();
        isPublished=false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMediaPublisher.release();
    }

    @Override
    public void surfaceRedrawNeeded(SurfaceHolder holder) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.i(TAG, "surfaceChanged: ");
        mMediaPublisher.initVideoGatherer(MainActivity.this, holder);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

}
