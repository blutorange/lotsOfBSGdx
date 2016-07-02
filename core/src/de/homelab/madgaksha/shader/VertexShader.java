package de.homelab.madgaksha.shader;

public abstract class VertexShader extends Shader {
	private final static VertexShader DEFAULT = new VertexShaderDefault();

	/**
	 * @return The default vertex shader as used by libGDX.
	 */
	public static VertexShader getDefault() {
		return DEFAULT;
	}
}
