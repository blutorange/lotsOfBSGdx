package de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.view;

import java.util.Locale;
import java.util.Scanner;

import org.apache.commons.io.IOUtils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.scenes.scene2d.utils.FocusListener;

import de.homelab.madgaksha.lotsofbs.logging.Logger;

public class NumericInput extends TextField {

	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(TextField.class);

	private float oldValue;
	private float min, max;
	private float quantizer = 0f;
	private float triggerUp = -1f, triggerDown = -1f;
	private float repeatThreshold = 0.5f, repeatSpeed = 0.05f;
	private float dragThreshold = 16f;
	private final Color errorColor = new Color(Color.RED);
	private String format = "%.2f";

	public NumericInput(float value, Skin skin) {
		this(value, skin.get(TextFieldStyle.class));
	}

	public NumericInput(float value, Skin skin, String styleName) {
		this(value, skin.get(styleName, TextFieldStyle.class));
	}

	public NumericInput(float value, TextFieldStyle style) {
		super(String.valueOf(value), style);
		this.min = -Float.MAX_VALUE;
		this.max = Float.MAX_VALUE;
		init();
		setValue(value);
	}

	private final void init() {
		addListener(changeListener);
		addListener(inputListener);
		addListener(focusListener);
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		if (triggerUp >= 0f) {
			triggerUp += Gdx.graphics.getRawDeltaTime();
			if (triggerUp >= repeatThreshold) {
				triggerUp = repeatThreshold - repeatSpeed;
				setValue(getValue() + quantizer);
			}
		} else if (triggerDown >= 0f) {
			triggerDown += Gdx.graphics.getRawDeltaTime();
			if (triggerDown >= repeatThreshold) {
				triggerDown = repeatThreshold - repeatSpeed;
				setValue(getValue() - quantizer);
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
		if (quantizer != 0f) {
			final float dq = (value % quantizer);
			if (Math.abs(dq) < quantizer * 0.5f)
				value = value - dq;
			else
				value = value - dq + ((dq < 0f) ? -quantizer : quantizer);
		}
		value = MathUtils.clamp(value, min, max);
		final boolean hasChanged = value != oldValue;
		oldValue = value;
		final String formattedValue = String.format(Locale.ROOT, format, value);
		setText(formattedValue);
		setColor(Color.WHITE);
		if (hasChanged) {
			fire(new NumericInputChangeEvent(value));
		}
	}

	/**
	 * @param color
	 *            The color with which the text field will be tinted when an
	 *            invalid value has been entered.
	 */
	public final void setErrorColor(Color color) {
		if (color != null)
			errorColor.set(color);
	}

	public final void setRepeatTreshold(float threshold) {
		this.repeatThreshold = threshold;
		triggerDown = triggerUp = -1f;
	}

	public final void setDragThreshold(float threshold) {
		this.dragThreshold = Math.max(0f, threshold);
	}

	public final void setRepeatSpeed(float speed) {
		this.repeatSpeed = speed;
		triggerDown = triggerUp = -1f;
	}

	/**
	 * @param format
	 *            For formatting the value with
	 *            {@link String#format(String, Object...)}.
	 */
	public final void setFormat(String format) {
		if (format != null) {
			this.format = format;
			refreshValue();
		}
	}

	public final void setQuantizer(float q) {
		quantizer = Math.max(q, 0f);
		refreshValue();
	}

	public final void setMinMax(float min, float max) {
		setMin(min);
		setMax(max);
	}

	public final void setMin(float min) {
		this.min = Math.min(min, max);
		refreshValue();
	}

	public final void setMax(float max) {
		this.max = Math.max(min, max);
		refreshValue();
	}

	private void refreshValue() {
		setValue(getValue());
	}

	private final FocusListener focusListener = new FocusListener() {
		public void scrollFocusChanged(FocusEvent event, Actor actor, boolean focused) {
			refreshValue();
		}

		public void keyboardFocusChanged(FocusEvent event, Actor actor, boolean focused) {
			refreshValue();
		}
	};

	private final InputListener inputListener = new InputListener() {
		private final Vector2 touchStart = new Vector2();
		private float touchStartValue;

		public boolean keyDown(InputEvent event, int keycode) {
			switch (keycode) {
			case Keys.PLUS:
			case Keys.UP:
			case Keys.PAGE_UP:
				triggerUp = 0f;
				triggerDown = -1f;
				setValue(getValue() + quantizer);
				return true;
			case Keys.MINUS:
			case Keys.DOWN:
			case Keys.PAGE_DOWN:
				triggerDown = 0f;
				triggerUp = -1f;
				setValue(getValue() - quantizer);
				return true;
			default:
				return false;
			}
		}

		public boolean keyUp(InputEvent event, int keycode) {
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

		public boolean scrolled(InputEvent event, float x, float y, int amount) {
			if (Math.abs(amount) > dragThreshold) {
				float amountFloat = amount < 0 ? amount + dragThreshold : amount - dragThreshold;
				setValue(getValue() + amountFloat * 0.01f * (max - min));
			}
			return true;
		}

		public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
			if (pointer > 0)
				return false;
			touchStart.set(x, y);
			touchStartValue = getValue();
			return true;
		}

		public void touchDragged(InputEvent event, float x, float y, int pointer) {
			float amount = y - touchStart.y;
			if (Math.abs(amount) > dragThreshold) {
				amount = amount < 0 ? amount + dragThreshold : amount - dragThreshold;
				setValue(touchStartValue + amount * 0.001f * (max - min));
			}
		}
	};

	private final ChangeListener changeListener = new ChangeListener() {
		@Override
		public void changed(ChangeEvent event, Actor actor) {
			if ((actor instanceof NumericInput)) {
				final NumericInput input = (NumericInput) actor;
				final String value = input.getText();
				if (value.isEmpty())
					return;
				final Float floatValue = parseFloat(value);
				if (floatValue != null) {
					if (floatValue >= min && floatValue <= max) {
						setValue(floatValue);
						setColor(255, 255, 255, 1);
					} else {
						setColor(255, 0, 0, 1);
					}
				} else {
					setColor(255, 0, 0, 1);

				}
			} else {
				setColor(255, 0, 0, 1);
			}
		}

		private Float parseFloat(String value) {
			final Scanner s = new Scanner(value);
			final Float f;
			f = s.hasNextFloat() ? s.nextFloat() : null;
			IOUtils.closeQuietly(s);
			return f;
		}
	};

	public static abstract class NumericInputChangeListener extends ChangeListener {
		@Override
		public void changed(ChangeEvent event, Actor actor) {
			if (event instanceof NumericInputChangeEvent) {
				changed(((NumericInputChangeEvent) event).value, actor);
			}
		}

		protected abstract void changed(float value, Actor actor);
	}

	public static class NumericInputChangeEvent extends ChangeEvent {
		public final float value;

		public NumericInputChangeEvent(float value) {
			this.value = value;
		}
	}
}
