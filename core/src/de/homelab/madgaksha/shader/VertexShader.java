package de.homelab.madgaksha.shader;

import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public class VertexShader {
	public final static String DEFAULT_PROGRAM;
	static {
		DEFAULT_PROGRAM = "attribute vec4 " + ShaderProgram.POSITION_ATTRIBUTE + ";\n" //
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
	private final static VertexShader DEFAULT = new VertexShader(DEFAULT_PROGRAM);
	public static VertexShader getDefault() {
		return DEFAULT;
	}
	private final String program;
	public VertexShader(String program) {
		this.program = program;
	}
	public String getProgram() {
		return program;
	}
	public void update(float deltaTime) {}
	public void forShaderProgram(ShaderProgram sp) {}
}
