package de.homelab.madgaksha.cutscenesystem;

import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.math.Vector2;

import de.homelab.madgaksha.resourcecache.EAnimation;
import de.homelab.madgaksha.resourcecache.ENinePatch;
import de.homelab.madgaksha.resourcecache.ETexture;
import de.homelab.madgaksha.resourcecache.ResourceCache;
import de.homelab.madgaksha.resourcepool.AtlasAnimation;
import de.homelab.madgaksha.resourcepool.PoolableAtlasSprite;

public final class FancySpriteWrapper {
	public PoolableAtlasSprite sprite = ETexture.DEFAULT.asSprite();
	public float spriteDpi = 1.0f;
	
	public NinePatch ninePatch = ResourceCache.getNinePatch(ENinePatch.DEFAULT);
	public Vector2 ninePatchDimensions = new Vector2();
	
	public AtlasAnimation atlasAnimation = ResourceCache.getAnimation(EAnimation.DEFAULT); 
	
	public Vector2 position = new Vector2();
	public float opacity = 1.0f;
	public Mode mode;
	public static enum Mode {
		TEXTURE,
		NINE_PATCH,
		ATLAS_ANIMATION;
	}
}