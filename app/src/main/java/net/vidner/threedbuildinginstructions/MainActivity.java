/*
 * Based on OpenGL ES sample code, which is:
 *
 * Copyright (C) 2011 The Android Open Source Project
 * Licensed under the Apache License, Version 2.0 (the "License");
 *      http://www.apache.org/licenses/LICENSE-2.0
 */
package net.vidner.threedbuildinginstructions;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity {

    private MyGLSurfaceView mGLView;
    private MyGLRenderer mRenderer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);
        mGLView = (MyGLSurfaceView) findViewById(R.id.gl_surface_view);

        // Request an OpenGL ES 2.0 compatible context.
        mGLView.setEGLContextClientVersion(2);
        // Set the Renderer for drawing on the GLSurfaceView
        mRenderer = new MyGLRenderer();
        mGLView.setRenderer(mRenderer);
        mRenderer.setView(mGLView);

        // Render the view only when there is a change in the drawing data
        mGLView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);


        findViewById(R.id.button_prev).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                previousStep();
            }
        });

        findViewById(R.id.button_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextStep();
            }
        });

    }

    private void previousStep() {
        mGLView.queueEvent(new Runnable() {
            @Override
            public void run() {
                mRenderer.previousStep();
            }
        });
    }


    private void nextStep() {
        mGLView.queueEvent(new Runnable() {
            @Override
            public void run() {
                mRenderer.nextStep();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        // The following call pauses the rendering thread.
        // If your OpenGL application is memory intensive,
        // you should consider de-allocating objects that
        // consume significant memory here.
        mGLView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // The following call resumes a paused rendering thread.
        // If you de-allocated graphic objects for onPause()
        // this is a good place to re-allocate them.
        mGLView.onResume();
    }
}