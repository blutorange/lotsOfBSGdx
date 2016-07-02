package de.homelab.madgaksha.shader;

import com.badlogic.gdx.graphics.glutils.ShaderProgram;

import de.homelab.madgaksha.Game;

public abstract class Shader {

	private final String program;
	private ShaderProgram shaderProgram;

	public Shader() {
		this.program = requestedProgram();
	}

	public String getProgram() {
		return program;
	}

	/**
	 * Called once to fetch the program for this shader.
	 * 
	 * @return Program to be used by this shader.
	 */
	protected abstract String requestedProgram();

	/**
	 * Called each frame to update the shader. Can be used for animations etc.
	 * 
	 * @param deltaTime
	 *            Time since the last frame, scaled by
	 *            {@link Game#setGlobalTimeScale(float)}.
	 */
	public abstract void update(float deltaTime);

	/**
	 * Called when uniforms can be set on the shader. It is scalled after
	 * {@link Shader#begin} has been called. The shader needs to be bound or
	 * setting uniforms will not have any effect.
	 * 
	 * @param shaderProgram
	 *            The shader program to which this fragment shader belongs to.
	 */
	abstract void setUniforms(ShaderProgram shaderProgram);

	void setShaderProgram(ShaderProgram shaderProgram) {
		this.shaderProgram = shaderProgram;
		forShaderProgram(shaderProgram);
	}

	/**
	 * May be overwritten for custom initialization when this shader is added to
	 * a {@link CustomShaderProgram}.
	 * 
	 * @param sp
	 */
	protected void forShaderProgram(ShaderProgram sp) {
	};

	/**
	 * @return The shader program to which this shader belongs to.
	 */
	protected ShaderProgram getShaderProgram() {
		return shaderProgram;
	}
}
