package de.homelab.madgaksha.shader;

import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public abstract class FragmentShader {

	private final static FragmentShader DEFAULT = new FragmentShaderDefault();
	private final String program;
	private ShaderProgram shaderProgram;
	
	public FragmentShader() {
		this.program = requestedProgram();
	}
	public String getProgram() {
		return program;
	}

	protected abstract String requestedProgram();
	public abstract void update(float deltaTime);
	abstract void setUniforms(ShaderProgram shaderProgram);

	void setShaderProgram(ShaderProgram shaderProgram) {
		this.shaderProgram = shaderProgram;
		forShaderProgram(shaderProgram);
	}
	protected void forShaderProgram(ShaderProgram sp){
	};
	protected ShaderProgram getShaderProgram() {
		return shaderProgram;
	}
	
	public static FragmentShader getDefault() {
		return DEFAULT;
	}
}
