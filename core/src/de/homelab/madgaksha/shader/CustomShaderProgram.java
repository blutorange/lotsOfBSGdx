package de.homelab.madgaksha.shader;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

import de.homelab.madgaksha.logging.Logger;

public class CustomShaderProgram {
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(CustomShaderProgram.class);
	
	private final ShaderProgram sp;
	private final FragmentShader fs;
	private final VertexShader vs;
	
	public CustomShaderProgram() {
		this(VertexShader.getDefault(), FragmentShader.getDefault());
	}
	
	public CustomShaderProgram(FragmentShader fs) {
		this(VertexShader.getDefault(), fs);
	}
	
	public CustomShaderProgram(VertexShader vs, FragmentShader fs) {
		this.fs = fs;
		this.vs = vs;
		sp = new ShaderProgram(vs.getProgram(), fs.getProgram());
		fs.forShaderProgram(sp);
		vs.forShaderProgram(sp);
	}
	
	public void update(float deltaTime) {
		fs.update(deltaTime);
		vs.update(deltaTime);
	}
	
	public void apply(SpriteBatch batch) {
		batch.setShader(sp);
	}
	
	public void dispose() {
		if (sp != null) sp.dispose();
	}
}
