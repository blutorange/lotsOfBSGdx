package de.homelab.madgaksha.scene2dext.widget;

import java.util.Locale;
import java.util.Scanner;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.FocusListener;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pool.Poolable;

import de.homelab.madgaksha.lotsofbs.logging.Logger;

public class NumericInput extends TextField implements Poolable {

	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(TextField.class);
	private static Pool<NumericInput> Pool;

	private float oldValue;
	private float min, max;
	private float step = 0f;
	private float triggerUp = -1f, triggerDown = -1f;
	private float repeatThreshold = 0.5f, repeatSpeed = 0.05f;
	private float dragThreshold = 16f;
	private final Color errorColor = new Color(Color.RED);
	private String format = "%.2f";

	public NumericInput(final float value, final Skin skin) {
		this(value, skin.get(TextFieldStyle.class));
		if (Pool == null)
			Pool = new Pool<NumericInput>() {
			@Override
			protected NumericInput newObject() {
				return new NumericInput(0f ,skin);
			}
		};
	}

	public NumericInput(final float value, final Skin skin, final String styleName) {
		this(value, skin.get(styleName, TextFieldStyle.class));
	}

	public NumericInput(final float value, final TextFieldStyle style) {
		super(String.valueOf(value), style);
		min = -Float.MAX_VALUE;
		max = Float.MAX_VALUE;
		init();
		setValue(value);
	}

	public void setSkin(final Skin skin) {
		setStyle(skin.get(TextFieldStyle.class));
	}

	private final void init() {
		setProgrammaticChangeEvents(false);
		addListener(changeListener);
		addListener(inputListener);
		addListener(focusListener);
	}

	@Override
	public void draw(final Batch batch, final float parentAlpha) {
		if (triggerUp >= 0f) {
			triggerUp += Gdx.graphics.getRawDeltaTime();
			if (triggerUp >= repeatThreshold) {
				triggerUp = repeatThreshold - repeatSpeed;
				setValue(getValue() + step);
			}
		} else if (triggerDown >= 0f) {
			triggerDown += Gdx.graphics.getRawDeltaTime();
			if (triggerDown >= repeatThreshold) {
				triggerDown = repeatThreshold - repeatSpeed;
				setValue(getValue() - step);
			}
		}
		super.draw(batch, parentAlpha);
	}

	/** @return The last known valid value. */
	public final float getValue() {
		return oldValue;
	}

	public final float getDragThreshold() {
		return dragThreshold;
	}

	public final void setValue(float value) {
		value = MathUtils.clamp(value, min, max);
		final boolean hasChanged = value != oldValue;
		oldValue = value;
		final String formattedValue = String.format(Locale.ROOT, format, value);
		setText(formattedValue);
		setColor(Color.WHITE);
		if (hasChanged)
			fire(new NumericInputEvent(value));
	}

	/**
	 * @param color
	 *            The color with which the text field will be tinted when an
	 *            invalid value has been entered.
	 */
	public final void setErrorColor(final Color color) {
		if (color != null)
			errorColor.set(color);
	}

	public final void setRepeatTreshold(final float threshold) {
		repeatThreshold = threshold;
		triggerDown = triggerUp = -1f;
	}

	public final void setDragThreshold(final float threshold) {
		dragThreshold = Math.max(0f, threshold);
	}

	public final void setRepeatSpeed(final float speed) {
		repeatSpeed = speed;
		triggerDown = triggerUp = -1f;
	}

	/**
	 * @param format
	 *            For formatting the value with
	 *            {@link String#format(String, Object...)}.
	 */
	public final void setFormat(final String format) {
		if (format != null) {
			this.format = format;
			refreshMaxLength();
			refreshValue();
		}
	}

	public final void setStep(final float q) {
		step = Math.max(q, 0f);
		refreshValue();
	}

	private void refreshMaxLength() {
		final String n1 = String.format(Locale.ROOT, format, min);
		final String n2 = String.format(Locale.ROOT, format, max);
		setMaxLength(Math.max(n1.length(), n2.length()));
		invalidateHierarchy();
	}

	public final void setMinMax(final float min, final float max) {
		setMin(min);
		setMax(max);
	}

	public final void setMin(final float min) {
		this.min = Math.min(min, max);
		refreshMaxLength();
		refreshValue();
	}


	public final void setMax(final float max) {
		this.max = Math.max(min, max);
		refreshMaxLength();
		refreshValue();
	}

	private void refreshValue() {
		setValue(getValue());
	}

	private final FocusListener focusListener = new FocusListener() {
		@Override
		public void scrollFocusChanged(final FocusEvent event, final Actor actor, final boolean focused) {
			refreshValue();
		}

		@Override
		public void keyboardFocusChanged(final FocusEvent event, final Actor actor, final boolean focused) {
			refreshValue();
		}
	};

	private final InputListener inputListener = new InputListener() {
		private final Vector2 touchStart = new Vector2();
		private float touchStartValue;

		@Override
		public boolean keyDown(final InputEvent event, final int keycode) {
			switch (keycode) {
			case Keys.PLUS:
			case Keys.UP:
			case Keys.PAGE_UP:
				triggerUp = 0f;
				triggerDown = -1f;
				setValue(getValue() + getStep());
				return true;
			case Keys.MINUS:
			case Keys.DOWN:
			case Keys.PAGE_DOWN:
				triggerDown = 0f;
				triggerUp = -1f;
				setValue(getValue() - getStep());
				return true;
			default:
				return false;
			}
		}

		@Override
		public boolean keyUp(final InputEvent event, final int keycode) {
			switch (keycode) {
			case Keys.PLUS:
			case Keys.UP:
			case Keys.PAGE_UP:
				triggerUp = triggerDown = -1f;
				return true;
			case Keys.MINUS:
			case Keys.DOWN:
			case Keys.PAGE_DOWN:
				triggerUp = triggerDown = -1f;
				return true;
			default:
				return false;
			}
		}

		@Override
		public boolean scrolled(final InputEvent event, final float x, final float y, final int amount) {
			if (Math.abs(amount) > dragThreshold) {
				final float amountFloat = amount < 0 ? amount + dragThreshold : amount - dragThreshold;
				setValue(getValue() + amountFloat * getStep());
			}
			return true;
		}

		@Override
		public boolean touchDown(final InputEvent event, final float x, final float y, final int pointer, final int button) {
			if (pointer > 0)
				return false;
			touchStart.set(x, y);
			touchStartValue = getValue();
			return true;
		}

		@Override
		public void touchDragged(final InputEvent event, final float x, final float y, final int pointer) {
			float amount = y - touchStart.y;
			if (Math.abs(amount) > dragThreshold) {
				amount = amount < 0 ? amount + dragThreshold : amount - dragThreshold;
				setValue(touchStartValue + amount * 0.5f * getStep());
			}
		}
	};

	private final ChangeListener changeListener = new ChangeListener() {
		@Override
		public void changed(final ChangeEvent event, final Actor actor) {
			if (actor instanceof NumericInput) {
				final NumericInput input = (NumericInput) actor;
				final String value = input.getText();
				if (value.isEmpty())
					return;
				final Float floatValue = parseFloat(value);
				if (floatValue != null) {
					if (floatValue >= min && floatValue <= max) {
						setValue(floatValue);
						setColor(255, 255, 255, 1);
					} else
						setColor(255, 0, 0, 1);
				} else
					setColor(255, 0, 0, 1);
			} else
				setColor(255, 0, 0, 1);
		}

		private Float parseFloat(final String value) {
			final Scanner s = new Scanner(value);
			final Float f;
			f = s.hasNextFloat() ? s.nextFloat() : null;
			IOUtils.closeQuietly(s);
			return f;
		}
	};

	public static abstract class NumericInputListener implements EventListener {
		@Override
		public boolean handle (final Event event) {
			if (!(event instanceof NumericInputEvent)) return false;
			changed(((NumericInputEvent)event).value, event.getTarget());
			return false;
		}

		protected abstract void changed(float value, Actor actor);
	}

	public static class NumericInputEvent extends Event {
		public final float value;

		public NumericInputEvent(final float value) {
			this.value = value;
		}
	}

	@Override
	public float getPrefWidth() {
		final TextFieldStyle style = getStyle();
		if (style == null || style.font == null) return super.getPrefWidth();
		return getMaxLength() * style.font.getLineHeight() * 0.75f;
	}

	@Override
	public String toString() {
		return "NumericInput@" + oldValue;
	}

	public float getStep() {
		return step;
	}

	public static NumericInput obtain() {
		return Pool.obtain();
	}

	public static void free(final NumericInput object) {
		Pool.free(object);
	}

	@Override
	public void reset() {
		setStep(0f);
		setErrorColor(Color.RED);
		setMinMax(0f, 1f);
		setValue(0f);
		setVisible(true);
		setMessageText(StringUtils.EMPTY);
		setDragThreshold(16f);
		setText(StringUtils.EMPTY);
		setBlinkTime(0.32f);
		setMaxLength(0);
		setDisabled(false);
	}
}
