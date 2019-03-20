package com.ariix.caremawx;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.easefun.polyvsdk.PolyvBitRate;
import com.easefun.polyvsdk.video.PolyvSeekType;
import com.easefun.polyvsdk.video.PolyvVideoView;

public class PolyvPlayerActivity extends AppCompatActivity {
    private PolyvVideoView videoView = null;

    private PolyvPlayerMediaController mediaController = null;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.polyv_player_layout);
        videoView = findViewById(R.id.polyv_video_view);
        videoView.setMediaController(new PolyvPlayerMediaController(PolyvPlayerActivity.this));
        videoView.setOpenAd(true);
        videoView.setOpenTeaser(true);
        videoView.setOpenQuestion(true);
        videoView.setOpenSRT(true);
        videoView.setOpenPreload(true, 2);
        videoView.setOpenMarquee(true);
        videoView.setAutoContinue(true);
        videoView.setNeedGestureDetector(true);
        videoView.setSeekType(PolyvSeekType.SEEKTYPE_NORMAL);
        videoView.setLoadTimeoutSecond(25);//加载超时时间，单位：秒
        videoView.setBufferTimeoutSecond(15);//缓冲超时时间，单位：秒
        videoView.disableScreenCAP(this, false);//防录屏开关，true为开启，如果开启防录屏，投屏功能将不可用


        videoView.setVid("ca6dfdb0a0a54a1071d6546a2799ebee_c", PolyvBitRate.ziDong.getNum(), false);

    }
}
