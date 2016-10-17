package de.homelab.madgaksha.converter;

import com.badlogic.gdx.graphics.Color;
import com.google.common.base.Converter;

public enum ColorLongConverter {
	HEX(new Converter<Color, Long>() {
		@Override
		protected Long doForward(final Color color) {
			return (long) (255 * color.r) << 24 | (long) (255 * color.g) << 16 | (long) (255 * color.b) << 8
					| (long) (255 * color.a);
		}
		@Override
		protected Color doBackward(final Long value) {
			final float r = ((value & 0xFF000000L) >> 24) / 255f;
			final float g = ((value & 0x00FF0000L) >> 16) / 255f;
			final float b = ((value & 0x0000FF00L) >> 8) / 255f;
			final float a = (value & 0x000000FFL) / 255f;
			return new Color(r, g, b, a);
		}
	});
	private final Converter<Color, Long> converter;

	private ColorLongConverter(final Converter<Color, Long> converter) {
		this.converter = converter;
	}

	public Converter<Color, Long> get() {
		return converter;
	}
}
