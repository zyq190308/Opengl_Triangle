package com.example.opengl_triangle;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import android.opengl.GLES20;
import android.opengl.Matrix;

public class Triangle {



   // 设置颜色，分别为red, green, blue 和alpha (opacity)
  
	float []vertexs = {
			0.0f,0.5f,0.0f,
			-0.5f,-0.5f,0.0f,
			0.5f,-0.5f,0.0f
	};
	float color[] = {

			1.0f, 0.0f, 0.0f, 1.0f, //red
			0.0f,1.0f,0.0f,1.0f,
			0.0f,0.0f,1.0f,1.0f
			
			
	};
	float[] mVMatrix = new float[16];
	float num = 0;
	int mProgram;
	FloatBuffer vertexBuffer;
	FloatBuffer colorBuffer;
	String vertexShaderCode =
			"attribute vec3 vPosition;" +
					"attribute vec4 aColor;"+
					"varying  vec4 vColor;"+
					"uniform mat4 mVMatrix; "+//总变换矩阵
					"void main() {" +
					"  gl_Position = mVMatrix*vec4(vPosition,1.0);" +
					"	vColor = aColor;"+
					"}";

	String fragmentShaderCode =
			"precision mediump float;" +
					"varying vec4 vColor;" +
					"void main() {" +
					"  gl_FragColor = vColor;" +
					"}";

	public Triangle() {

		ByteBuffer vbb = ByteBuffer.allocateDirect(vertexs.length * 4);
		vbb.order(ByteOrder.nativeOrder());
		vertexBuffer = vbb.asFloatBuffer();
		vertexBuffer.put(vertexs);
		vertexBuffer.position(0);

		ByteBuffer cbb = ByteBuffer.allocateDirect(color.length * 4);
		cbb.order(ByteOrder.nativeOrder());
		colorBuffer = cbb.asFloatBuffer();
		colorBuffer.put(color);
		colorBuffer.position(0);

		int vertexShader = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER);
		int fragmentShader = GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER);

		GLES20.glShaderSource(vertexShader, vertexShaderCode);
		GLES20.glShaderSource(fragmentShader,fragmentShaderCode);

		GLES20.glCompileShader(vertexShader);
		GLES20.glCompileShader(fragmentShader);

		mProgram = GLES20.glCreateProgram();  
		GLES20.glAttachShader(mProgram, vertexShader);   
		GLES20.glAttachShader(mProgram, fragmentShader);
		GLES20.glLinkProgram(mProgram);
	}
	
	public void draw() {
		num+=1;
		
		GLES20.glUseProgram(mProgram);
		int mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
		int maColorHandle = GLES20.glGetAttribLocation(mProgram, "aColor");
		int muMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "mVMatrix"); 
		
		GLES20.glEnableVertexAttribArray(mPositionHandle);
		GLES20.glEnableVertexAttribArray(maColorHandle);  
	
		GLES20.glVertexAttribPointer(mPositionHandle, 3,GLES20.GL_FLOAT, false,0,vertexBuffer);
		GLES20.glVertexAttribPointer(maColorHandle, 4, GLES20.GL_FLOAT, false,0,colorBuffer); 




		
		
		Matrix.setIdentityM(mVMatrix, 0);
//		Matrix.scaleM(mVMatrix, 0, 0.4f, 0.4f, 0.4f);
		Matrix.rotateM(mVMatrix, 0, num, 1.0f, 1.0f, 0.0f);
		GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1,false,mVMatrix,0);

		// 画三角形
		GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 3);
	}

}
