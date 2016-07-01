package de.homelab.madgaksha.shader;

import com.badlogic.gdx.graphics.glutils.ShaderProgram;

import de.homelab.madgaksha.logging.Logger;

public class FragmentShaderDefault extends FragmentShader {
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(FragmentShaderDefault.class);
	
	private final static String PROGRAM;
	static {
		PROGRAM = "#ifdef GL_ES\n" //
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
		
	@Override
	protected String requestedProgram() {
		return PROGRAM;
	}
	@Override
	public void update(float deltaTime) {
	}
	@Override
	void setUniforms(ShaderProgram sp) {
	}
}
