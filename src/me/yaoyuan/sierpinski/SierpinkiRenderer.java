package me.yaoyuan.sierpinski;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.Random;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.util.Log;

import me.yaoyuan.sierpinski.util.ShaderHelper;
import me.yaoyuan.sierpinski.util.TextResourceReader;

import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.GL_POINTS;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniform4f;
import static android.opengl.GLES20.glUseProgram;
import static android.opengl.GLES20.glVertexAttribPointer;
import static android.opengl.GLES20.glViewport;

public class SierpinkiRenderer implements GLSurfaceView.Renderer {

    private static final int BYTES_PER_FLOAT = 4;
    private static final String A_POSITION = "a_Position";
    private static final String U_COLOR = "u_Color";
    private static final int POSITION_COMPONENT_COUNT = 2;
    private final Activity mContext;
    private FloatBuffer mVertexData;
    private int mProgram;
    private int aPositionLocation;
    private int uColorLocation;

    public SierpinkiRenderer(Activity activity) {
        mContext = activity;
//        float[] vertices = new float[]{
//                -0.5f, -0.5f,
//                0.5f, -0.5f,
//                0.0f, 0.5f
//        };
        float[] vertices = new float[10000];
        vertices[0] = -0.5f;
        vertices[1] = -0.5f;
        vertices[2] = 0.5f;
        vertices[3] = -0.5f;
        vertices[4] = 0.0f;
        vertices[5] = 0.5f;
        vertices[6] = -0.25f;
        vertices[7] = -0.25f;
        Random ran = new Random();
        for (int index = 8; index < 10000; index = index + 2) {
            int vertexIndex = ran.nextInt(3);
            vertices[index] = (vertices[vertexIndex * 2] + vertices[index - 2]) / 2;
            vertices[index + 1] = (vertices[vertexIndex * 2 + 1] + vertices[index - 1]) / 2;
            Log.d("DDDD", vertices[index] + " " + vertices[index + 1]);
        }

        mVertexData = ByteBuffer.allocateDirect(vertices.length * BYTES_PER_FLOAT).order(ByteOrder.nativeOrder()).asFloatBuffer();
        mVertexData.put(vertices);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        String vertexShaderSource = TextResourceReader.readTextFileFromResource(mContext, R.raw.vertex_shader);
        String fragmentShaderSouce = TextResourceReader.readTextFileFromResource(mContext, R.raw.fragment_shader);
        int vertexShader = ShaderHelper.compileVertexShader(vertexShaderSource);
        int fragmentShader = ShaderHelper.compileFragmentShader(fragmentShaderSouce);
        mProgram = ShaderHelper.linkProgram(vertexShader, fragmentShader);
        ShaderHelper.validateProgram(mProgram);
        glUseProgram(mProgram);
        uColorLocation = glGetUniformLocation(mProgram, U_COLOR);
        aPositionLocation = glGetAttribLocation(mProgram, A_POSITION);
        // position
        mVertexData.position(0);
        glVertexAttribPointer(aPositionLocation, POSITION_COMPONENT_COUNT, GL_FLOAT, false, 0, mVertexData);
        glEnableVertexAttribArray(aPositionLocation);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        glViewport(0, 0, width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        glClear(GL_COLOR_BUFFER_BIT);
//        glUniform4f(uColorLocation, 1.0f, 0.0f, 0.0f, 1.0f);
//        glDrawArrays(GL_POINTS, 0, 3);
        // Draw the first mallet blue.
        glUniform4f(uColorLocation, 0.0f, 0.0f, 1.0f, 1.0f);
        glDrawArrays(GL_POINTS, 0, 5000);
    }
}
