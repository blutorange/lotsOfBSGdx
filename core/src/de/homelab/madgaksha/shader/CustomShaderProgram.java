package de.homelab.madgaksha.shader;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

import de.homelab.madgaksha.logging.Logger;

public class CustomShaderProgram extends ShaderProgram {
	private final static Logger LOG = Logger.getLogger(CustomShaderProgram.class);

	private final FragmentShader fs;
	private final VertexShader vs;
	private final boolean compiled;

	public CustomShaderProgram() {
		this(VertexShader.getDefault(), FragmentShader.getDefault());
	}

	public CustomShaderProgram(FragmentShader fs) {
		this(VertexShader.getDefault(), fs);
	}

	public CustomShaderProgram(VertexShader vs, FragmentShader fs) {
		super(vs.getProgram(), fs.getProgram());
		if (!isCompiled()) {
			LOG.error("could not compile shader program");
			compiled = false;
			this.fs = null;
			this.vs = null;
			return;
		}
		this.fs = fs;
		this.vs = vs;
		this.compiled = true;
		fs.setShaderProgram(this);
		vs.setShaderProgram(this);
	}

	public void update(float deltaTime) {
		if (!compiled)
			return;
		fs.update(deltaTime);
		vs.update(deltaTime);
	}

	public void apply(SpriteBatch batch) {
		if (!compiled)
			return;
		batch.setShader(this);
	}

	@Override
	public void begin() {
		if (!compiled)
			return;
		super.begin();
		fs.setUniforms(this);
		vs.setUniforms(this);
	}
}