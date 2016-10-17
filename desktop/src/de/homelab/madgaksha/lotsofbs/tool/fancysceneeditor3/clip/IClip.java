package de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor3.clip;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;

import javax.naming.InsufficientResourcesException;

import com.badlogic.gdx.utils.Json.Serializable;

import de.homelab.madgaksha.common.IdProvidable;
import de.homelab.madgaksha.lotsofbs.cutscenesystem.AFancyEvent;

public interface IClip extends Serializable {
	public abstract Collection<AFancyEvent> compileJava(IdProvidable id, float deltaTime) throws InsufficientResourcesException;
	public abstract float getStartTime();
	public abstract float getDuration();
	public void compileBinary(final OutputStream os, final IdProvidable id, final float deltaTime) throws IOException, InsufficientResourcesException;
	public byte[] compileBinary(final IdProvidable id, final float deltaTime) throws IOException, InsufficientResourcesException;
}