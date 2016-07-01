package de.homelab.madgaksha.shader;

import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public abstract class VertexShader {
	
	private final static VertexShader DEFAULT = new VertexShaderDefault();
	private final String program;
	private ShaderProgram shaderProgram;
	
	public VertexShader() {
		program = requestedProgram();
	}
	public String getProgram() {
		return program;
	}

	protected abstract String requestedProgram();
	public abstract void update(float deltaTime);
	abstract void setUniforms(ShaderProgram sp);

	void setShaderProgram(ShaderProgram shaderProgram) {
		this.shaderProgram = shaderProgram;
		forShaderProgram(shaderProgram);
	}
	protected void forShaderProgram(ShaderProgram sp){
	};
	protected ShaderProgram getShaderProgram() {
		return shaderProgram;
	}
	
	public static VertexShader getDefault() {
		return DEFAULT;
	}	
}
