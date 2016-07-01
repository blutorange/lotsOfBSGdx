package de.homelab.madgaksha.shader;

import com.badlogic.gdx.graphics.glutils.ShaderProgram;

import de.homelab.madgaksha.logging.Logger;

public class VertexShaderDefault extends VertexShader {
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(VertexShaderDefault.class);

	public final static String PROGRAM;
	static {
		PROGRAM = "attribute vec4 " + ShaderProgram.POSITION_ATTRIBUTE + ";\n" //
				+ "attribute vec4 " + ShaderProgram.COLOR_ATTRIBUTE + ";\n" //
				+ "attribute vec2 " + ShaderProgram.TEXCOORD_ATTRIBUTE + "0;\n" //
				+ "uniform mat4 u_projTrans;\n" //
				+ "varying vec4 v_color;\n" //
				+ "varying vec2 v_texCoords;\n" //
				+ "\n" //
				+ "void main()\n" //
				+ "{\n" //
				+ "   v_color = " + ShaderProgram.COLOR_ATTRIBUTE + ";\n" //
				+ "   v_color.a = v_color.a * (255.0/254.0);\n" //
				+ "   v_texCoords = " + ShaderProgram.TEXCOORD_ATTRIBUTE + "0;\n" //
				+ "   gl_Position =  u_projTrans * " + ShaderProgram.POSITION_ATTRIBUTE + ";\n" //
				+ "}\n";
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
