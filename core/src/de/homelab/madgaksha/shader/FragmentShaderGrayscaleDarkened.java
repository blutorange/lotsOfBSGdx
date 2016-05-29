package de.homelab.madgaksha.shader;

import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public class FragmentShaderGrayscaleDarkened extends FragmentShader {
	private final static String program =  
			"#ifdef GL_ES\n" //
			+ "#define LOWP lowp\n" //
			+ "precision mediump float;\n" //
			+ "#else\n" //
			+ "#define LOWP \n" //
			+ "#endif\n" //
			+ "varying LOWP vec4 v_color;\n" //
			+ "varying vec2 v_texCoords;\n" //
			+ "uniform sampler2D u_texture;\n" //
			+ "uniform float ratio;\n" //
			+ "void main()\n"//
			+ "{\n" //
			+ "  gl_FragColor = v_color * texture2D(u_texture, v_texCoords);\n" //
			+ "  gl_FragColor.r = (gl_FragColor.r+gl_FragColor.g+gl_FragColor.b)*0.33f;\n" //
			+ "  float gray = 0.299f * gl_FragColor.r+ 0.587f * gl_FragColor.g + 0.114f * gl_FragColor.b;\n" //
			+ "  gl_FragColor.r = gl_FragColor.r + 0.2f*(gray - gl_FragColor.r) -0.1f;\n" //
			+ "  gl_FragColor.g = gl_FragColor.g + 0.2f*(gray - gl_FragColor.g) -0.1f;\n" //
			+ "  gl_FragColor.b = gl_FragColor.b + 0.2f*(gray - gl_FragColor.b) -0.1f;\n" //
			+ "}";
//	private final float ratioStart;
//	private final float mRatio;
//	private ShaderProgram shaderProgram;
//	private float totalTime = 0.0f;


	public FragmentShaderGrayscaleDarkened(float ratioStart, float ratioEnd, float duration) {
		super(program);
//		this.ratioStart = ratioStart;
//		this.mRatio = (ratioEnd-ratioStart)/duration;
	}
	@Override
	public void forShaderProgram(ShaderProgram sp) {
	//	shaderProgram = sp;
	}
//	@Override
//	public void update(float deltaTime) {
//		totalTime += 0.02f;
//		float cur = ratioStart + totalTime * mRatio;
//		int locRatio = shaderProgram.fetchUniformLocation("ratio", true);
//		Gdx.gl20.glUniform1f(locRatio, MathUtils.clamp(cur, 0.0f, 1.0f));
//	}
}
