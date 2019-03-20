package com.ariix.caremawx;


import android.content.Context;
import android.view.View;

import com.easefun.polyvsdk.video.PolyvBaseMediaController;

public class PolyvPlayerMediaController extends PolyvBaseMediaController {

    //显示的状态
    private boolean isShowing;

    //控制栏显示的时间
    private static final int longTime = 5000;

    // 控制栏是否处于一直显示的状态
    private boolean status_showalways;

    public PolyvPlayerMediaController(Context context) {
        super(context);
    }

    @Override
    public void hide() {
        if (isShowing) {
            setVisibility(View.GONE);
            isShowing = !isShowing;
        }
    }

    @Override
    public boolean isShowing() {
        return isShowing;
    }

    @Override
    public void setAnchorView(View view) {

    }

    @Override
    public void show(int timeout) {
        if (timeout < 0) {
            status_showalways = true;
        } else {
            status_showalways = false;
        }

        setVisibility(View.VISIBLE);
    }

    @Override
    public void show() {
        show(longTime);
    }
}
