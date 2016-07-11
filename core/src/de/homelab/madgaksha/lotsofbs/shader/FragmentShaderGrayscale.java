package de.homelab.madgaksha.lotsofbs.shader;

import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Interpolation;

public class FragmentShaderGrayscale extends FragmentShader {
	private final static String PROGRAM = "#ifdef GL_ES\n" //
			+ "#define LOWP lowp\n" //
			+ "precision mediump float;\n" //
			+ "#else\n" //
			+ "#define LOWP \n" //
			+ "#endif\n" //
			+ "varying LOWP vec4 v_color;\n" //
			+ "varying vec2 v_texCoords;\n" //
			+ "uniform sampler2D u_texture;\n" //
			+ "uniform float ratio;\n" //
			+ "uniform float contrast;\n" //
			+ "void main()\n"//
			+ "{\n" //
			+ "  gl_FragColor = v_color * texture2D(u_texture, v_texCoords);\n" //
			+ "  float gray = 0.299 * gl_FragColor.r+ 0.587 * gl_FragColor.g + 0.114 * gl_FragColor.b;\n" //
			+ "  gl_FragColor.r = contrast * (gl_FragColor.r + ratio*(gray - gl_FragColor.r));\n" //
			+ "  gl_FragColor.g = contrast * (gl_FragColor.g + ratio*(gray - gl_FragColor.g));\n" //
			+ "  gl_FragColor.b = contrast * (gl_FragColor.b + ratio*(gray - gl_FragColor.b));\n" //
			+ "}";

	private final float ratioStart;
	private final float ratioEnd;
	private final float contrastStart;
	private final float contrastEnd;
	private final float durationInverse;
	private final Interpolation interpolation;
	private float ratio = 1.0f;
	private float contrast = 1.0f;
	private float alpha = 0.0f;

	public FragmentShaderGrayscale() {
		this(1.0f, 1.0f, 1.0f, 1.0f, 1.0f, Interpolation.linear);
	}

	public FragmentShaderGrayscale(float ratioStart, float ratioEnd, float contrastStart, float contrastEnd,
			float duration) {
		this(ratioStart, ratioEnd, contrastStart, contrastEnd, duration, Interpolation.linear);
	}

	public FragmentShaderGrayscale(float ratioStart, float ratioEnd, float contrastStart, float contrastEnd,
			float duration, Interpolation interpolation) {
		this.ratioStart = ratioStart;
		this.ratioEnd = ratioEnd;
		this.contrastStart = contrastStart;
		this.contrastEnd = contrastEnd;
		this.ratio = ratioStart;
		this.durationInverse = 1.0f / Math.max(duration, 0.01f);
		this.interpolation = interpolation;
	}

	@Override
	public void update(float deltaTime) {
		alpha += deltaTime * durationInverse;
		alpha = alpha > 1.0f ? 1.0f : alpha;
		ratio = interpolation.apply(ratioStart, ratioEnd, alpha);
		contrast = interpolation.apply(contrastStart, contrastEnd, alpha);
	}

	@Override
	protected String requestedProgram() {
		return PROGRAM;
	}

	@Override
	void setUniforms(ShaderProgram sp) {
		sp.setUniformf("ratio", ratio);
		sp.setUniformf("contrast", contrast);
	}
}