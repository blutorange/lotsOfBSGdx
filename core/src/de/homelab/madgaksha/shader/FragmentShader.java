package de.homelab.madgaksha.shader;

import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public class FragmentShader {
	private final static String DEFAULT_PROGRAM;
	static {
		DEFAULT_PROGRAM = "#ifdef GL_ES\n" //
				+ "#define LOWP lowp\n" //
				+ "precision mediump float;\n" //
				+ "#else\n" //
				+ "#define LOWP \n" //
				+ "#endif\n" //
				+ "varying LOWP vec4 v_color;\n" //
				+ "varying vec2 v_texCoords;\n" //
				+ "uniform sampler2D u_texture;\n" //
				+ "void main()\n"//
				+ "{\n" //
				+ "  gl_FragColor = v_color * texture2D(u_texture, v_texCoords);\n" //
				+ "}";
	}
	private final static FragmentShader DEFAULT = new FragmentShader(DEFAULT_PROGRAM);

	public static FragmentShader getDefault() {
		return DEFAULT;
	}

	private final String program;

	public FragmentShader(String program) {
		this.program = program;
	}

	public String getProgram() {
		return program;
	}

	public void update(float deltaTime) {
	};

	public void forShaderProgram(ShaderProgram sp) {
	};
}
