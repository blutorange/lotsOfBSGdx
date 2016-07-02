package de.homelab.madgaksha.shader;

public abstract class FragmentShader extends Shader {
	private final static FragmentShader DEFAULT = new FragmentShaderDefault();

	/**
	 * @return The default fragment shader as used by libGDX.
	 */
	public static FragmentShader getDefault() {
		return DEFAULT;
	}
}
