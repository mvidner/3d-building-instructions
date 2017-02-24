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

/**
 * A two-dimensional square for use as a drawn object in OpenGL ES 2.0.
 */
public class Cube {

    private final String vertexShaderCode =
            // This matrix member variable provides a hook to manipulate
            // the coordinates of the objects that use this vertex shader
            "uniform mat4 uMVPMatrix;" +
            "attribute vec4 vPosition;" +
            "void main() {" +
            // The matrix must be included as a modifier of gl_Position.
            // Note that the uMVPMatrix factor *must be first* in order
            // for the matrix multiplication product to be correct.
            "  gl_Position = uMVPMatrix * vPosition;" +
            "}";

    private final String fragmentShaderCode =
            "precision mediump float;" +
            "uniform vec4 vColor;" +
            "void main() {" +
            "  gl_FragColor = vColor;" +
            "}";

    private final FloatBuffer vertexBuffer;
    private final ShortBuffer drawListBuffer;
    private final ShortBuffer edgeDrawListBuffer;

    private final int mProgram;
    private int mPositionHandle;
    private int mColorHandle;
    private int mMVPMatrixHandle;

    // number of coordinates per vertex in this array
    static final int COORDS_PER_VERTEX = 3;
    static float cubeCoords[] = {
            -0.5f,  0.5f, -0.5f,    // front top left
            -0.5f, -0.5f, -0.5f,    // front bottom left
             0.5f, -0.5f, -0.5f,    // front bottom right
             0.5f,  0.5f, -0.5f,    // front top right
            -0.5f,  0.5f,  0.5f,   // back top left
            -0.5f, -0.5f,  0.5f,   // back bottom left
             0.5f, -0.5f,  0.5f,   // back bottom right
             0.5f,  0.5f,  0.5f }; // back top right

    private final short drawOrder[] = {
            // front
            0, 1, 2,
            0, 2, 3,
            // right
            3, 2, 6,
            3, 6, 7,
            // back
            7, 6, 5,
            7, 5, 4,
            // left
            4, 5, 1,
            4, 1, 0,
            // bottom
            1, 5, 6,
            1, 6, 2,
            // top
            4, 0, 3,
            4, 3, 7
    }; // order to draw vertices

    private final short edgeDrawOrder[] = {
            0, 1,
            1, 2,
            2, 3,
            3, 0,
            4, 5,
            5, 6,
            6, 7,
            7, 4,
            0, 4,
            1, 5,
            2, 6,
            3, 7
    };

    private final int vertexStride = COORDS_PER_VERTEX * 4; // 4 bytes per vertex


    /**
     * Sets up the drawing object data for use in an OpenGL ES context.
     */
    public Cube() {
        // initialize vertex byte buffer for shape coordinates
        ByteBuffer bb = ByteBuffer.allocateDirect(
        // (# of coordinate values * 4 bytes per float)
                cubeCoords.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(cubeCoords);
        vertexBuffer.position(0);

        // initialize byte buffer for the draw list
        ByteBuffer dlb = ByteBuffer.allocateDirect(
                // (# of coordinate values * 2 bytes per short)
                drawOrder.length * 2);
        dlb.order(ByteOrder.nativeOrder());
        drawListBuffer = dlb.asShortBuffer();
        drawListBuffer.put(drawOrder);
        drawListBuffer.position(0);

        // initialize byte buffer for the edge draw list
        ByteBuffer edlb = ByteBuffer.allocateDirect(
                // (# of coordinate values * 2 bytes per short)
                edgeDrawOrder.length * 2);
        edlb.order(ByteOrder.nativeOrder());
        edgeDrawListBuffer = edlb.asShortBuffer();
        edgeDrawListBuffer.put(edgeDrawOrder);
        edgeDrawListBuffer.position(0);

        // prepare shaders and OpenGL program
        int vertexShader = MyGLRenderer.loadShader(
                GLES20.GL_VERTEX_SHADER,
                vertexShaderCode);
        int fragmentShader = MyGLRenderer.loadShader(
                GLES20.GL_FRAGMENT_SHADER,
                fragmentShaderCode);

        mProgram = GLES20.glCreateProgram();             // create empty OpenGL Program
        GLES20.glAttachShader(mProgram, vertexShader);   // add the vertex shader to program
        GLES20.glAttachShader(mProgram, fragmentShader); // add the fragment shader to program
        GLES20.glLinkProgram(mProgram);                  // create OpenGL program executables
    }

    /**
     * Encapsulates the OpenGL ES instructions for drawing this shape.
     *
     * @param mvpMatrix - The Model View Project matrix in which to draw
     * this shape.
     */
    public void draw(float[] mvpMatrix, float[] color, boolean justEdges) {
        // Add program to OpenGL environment
        GLES20.glUseProgram(mProgram);

        // get handle to vertex shader's vPosition member
        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
        MyGLRenderer.checkGlError("glGetAttribLocation vPosition");

        // Enable a handle to the triangle vertices
        GLES20.glEnableVertexAttribArray(mPositionHandle);
        MyGLRenderer.checkGlError("glEnableVertexAttribArray");

        // Prepare the triangle coordinate data
        GLES20.glVertexAttribPointer(
                mPositionHandle, COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false,
                vertexStride, vertexBuffer);
        MyGLRenderer.checkGlError("glVertexAttribPointer");

        // get handle to fragment shader's vColor member
        mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");
        MyGLRenderer.checkGlError("glGetUniformLocation vColor");

        // Set color for drawing the cube
        GLES20.glUniform4fv(mColorHandle, 1, color, 0);
        MyGLRenderer.checkGlError("glUniform4fv");

        // get handle to shape's transformation matrix
        mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
        MyGLRenderer.checkGlError("glGetUniformLocation uMVPMatrix");

        // Apply the projection and view transformation
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0);
        MyGLRenderer.checkGlError("glUniformMatrix4fv");

        // Draw the cube
        if (justEdges) {
            // edges
            GLES20.glDrawElements(
                    GLES20.GL_LINES, edgeDrawOrder.length,
                    GLES20.GL_UNSIGNED_SHORT, edgeDrawListBuffer);
        }
        else {
            // faces
            GLES20.glDrawElements(
                    GLES20.GL_TRIANGLES, drawOrder.length,
                    GLES20.GL_UNSIGNED_SHORT, drawListBuffer);
        }
        MyGLRenderer.checkGlError("glDrawElements");

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(mPositionHandle);
    }

}