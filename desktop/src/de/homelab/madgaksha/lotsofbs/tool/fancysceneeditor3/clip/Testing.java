package de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor3.clip;

import javax.naming.InsufficientResourcesException;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Json;

import de.homelab.madgaksha.common.IdProvider;
import de.homelab.madgaksha.lotsofbs.enums.RichterScale;
import de.homelab.madgaksha.lotsofbs.resourcecache.ETexture;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor3.clip.drawableproperty.EShadow;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor3.clip.drawableproperty.PPosition;

public class Testing {

	public static void main(final String[] args) {
		final ClipDrawable c1 = new ClipSprite(ETexture.BULLET_FLOWER_BLACK);
		final ClipShake c2 = new ClipShake(RichterScale.M4);
		c1.add(new PPosition(1f, 2, 3, 0, 0));
		c1.add(new EShadow(0.2f));
		try {
			System.out.println(c1.compileJava(new IdProvider(), 0f));
		} catch (final InsufficientResourcesException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		final Json json = new Json();
		final Color c = new Color();
		System.out.println(json.prettyPrint(new IClip[]{c1,c2}));
	}

}
