/*
 * Based on OpenGL ES sample code, which is:
 *
 * Copyright (C) 2011 The Android Open Source Project
 * Licensed under the Apache License, Version 2.0 (the "License");
 *      http://www.apache.org/licenses/LICENSE-2.0
 */

package net.vidner.threedbuildinginstructions;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * A view container where OpenGL ES graphics can be drawn on screen.
 * This view can also be used to capture touch events, such as a user
 * interacting with drawn objects.
 */
public class MyGLSurfaceView extends GLSurfaceView {

    private MyGLRenderer mRenderer;

    public MyGLSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyGLSurfaceView(Context context) {
        super(context);
    }

    @Override
    public void setRenderer(Renderer renderer)
    {
        mRenderer = (MyGLRenderer) renderer;
        super.setRenderer(renderer);
    }

    private final float TOUCH_SCALE_FACTOR = 180.0f / 640;
    private float mPreviousX;
    private float mPreviousY;

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        // MotionEvent reports input details from the touch screen
        // and other input controls. In this case, you are only
        // interested in events where the touch position changed.

        float x = e.getX();
        float y = e.getY();

        switch (e.getAction()) {
            case MotionEvent.ACTION_MOVE:

                float dx = x - mPreviousX;
                float dy = y - mPreviousY;

                // note X-angle gets dY: if you move vertically, you do want an X axis rotation
                mRenderer.setXAngle(mRenderer.getXAngle() +
                                    (dy * TOUCH_SCALE_FACTOR));  // = 180.0f / 320
                mRenderer.setZAngle(mRenderer.getZAngle() +
                                    (dx * TOUCH_SCALE_FACTOR));  // = 180.0f / 320

                requestRender();
        }

        mPreviousX = x;
        mPreviousY = y;
        return true;
    }

}
