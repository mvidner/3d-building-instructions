/*
 * Based on OpenGL ES sample code, which is:
 *
 * Copyright (C) 2011 The Android Open Source Project
 * Licensed under the Apache License, Version 2.0 (the "License");
 *      http://www.apache.org/licenses/LICENSE-2.0
 */

package net.vidner.threedbuildinginstructions;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import android.opengl.GLES20;
import android.opengl.Matrix;

/**
 * A block
 */
public class Block {
    private final float mX, mY, mZ;
    private final float mR, mG, mB;

    // a unit cube
    private static Cube mCube = new Cube();
    /**
     * Sets up the drawing object data for use in an OpenGL ES context.
     */
    public Block(float x, float y, float z, float r, float g, float b) {
        mX = x;
        mY = y;
        mZ = z;
        mR = r;
        mG = g;
        mB = b;
    }

    /**
     * Encapsulates the OpenGL ES instructions for drawing this shape.
     *
     * @param mvpMatrix - The Model View Project matrix in which to draw
     * this shape.
     */
    public void draw(float[] mvpMatrix) {
        float color[] = { mR, mG, mB, 0.0f };
        float white[] = { 1, 1, 1, 0};

        float scale = 0.8f;

        // Draw a translated cube
        float[] scaleMatrix = new float[16];
        Matrix.setIdentityM(scaleMatrix, 0);
        Matrix.scaleM(scaleMatrix, 0, scale, scale, scale);

        float[] translationMatrix = new float[16];
        Matrix.setIdentityM(translationMatrix, 0);
        Matrix.translateM(translationMatrix, 0, mX, mY, mZ);

        float[] scratch = new float[16];
        float[] scratch1 = new float[16];
        Matrix.multiplyMM(scratch1, 0, translationMatrix, 0, scaleMatrix, 0);
        Matrix.multiplyMM(scratch, 0, mvpMatrix, 0, scratch1, 0);

        mCube.draw(scratch, color, false); // colored faces
        mCube.draw(scratch, white, true);  // white edges
        //mCube.draw(scratch, color, true); // colored edges
    }

}
