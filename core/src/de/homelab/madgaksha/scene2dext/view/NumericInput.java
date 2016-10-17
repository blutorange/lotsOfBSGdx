package de.homelab.madgaksha.scene2dext.view;

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
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.FocusListener;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pool.Poolable;

import de.homelab.madgaksha.lotsofbs.logging.Logger;
import de.homelab.madgaksha.scene2dext.connectible.ModelConnectibleRequest;
import de.homelab.madgaksha.scene2dext.connectible.ViewConnectible;

public abstract class NumericInput<T extends Number> extends TextField implements Poolable, ViewConnectible<T> {
	private final static Logger LOG = Logger.getLogger(TextField.class);

	private final T zero = zero();
	private T oldValue;
	private T min, max;
	private boolean editMode;
	private T step = zero;
	private float triggerUp = -1f, triggerDown = -1f;
	private float repeatThreshold = 0.5f, repeatSpeed = 0.05f;
	private float dragThreshold = 16f;
	private final Color errorColor = new Color(Color.RED);
	private T queuedChange;
	private boolean refreshMaxLength;

	public NumericInput(final T value, final Skin skin) {
		this(value, skin.get(TextFieldStyle.class));
	}

	public NumericInput(final T value, final Skin skin, final String styleName) {
		this(value, skin.get(styleName, TextFieldStyle.class));
	}

	public NumericInput(final T value, final TextFieldStyle style) {
		super(String.valueOf(value), style);
		min = getMinValue();
		max = getMaxValue();
		oldValue = value;
		init();
		refreshMaxLength = true;
	}

	protected abstract String formatNumber(T value);
	protected abstract T parseNumber(final String value);
	protected abstract T getMinValue();
	protected abstract T getMaxValue();
	protected abstract T add(T x, T y);
	protected abstract T subtract(T x, T y);
	protected abstract T scale(T x, float factor);
	/** @return -1 when x<y, 0 when x==y, 1 when x>y. */
	protected abstract int compare(T x, T y);

	/** May be overridden for performance, when necessary. */
	protected abstract T zero();
	/** May be overridden for performance, when necessary. */
	protected T min(final T x, final T y) {
		return compare(x,y) < 0 ? x : y;
	}
	/** May be overridden for performance, when necessary. */
	protected T max(final T x, final T y) {
		return compare(x,y) > 0 ? x : y;
	}
	/** May be overridden for performance, when necessary. */
	protected T clamp(final T value, final T min, final T max) {
		return min(max, max(value, min));
	}

	public boolean isEditMode() {
		return editMode;
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
	public void layout() {
		refreshValue();
		super.layout();
	}

	@Override
	public void validate() {
		if (refreshMaxLength) {
			refreshMaxLength = false;
			final String n1 = formatNumber(min);
			final String n2 = formatNumber(max);
			setMaxLength(Math.max(n1.length(), n2.length()));
			invalidateHierarchy();
		}
		super.validate();
	}

	@Override
	public void draw(final Batch batch, final float parentAlpha) {
		if (triggerUp >= 0f) {
			triggerUp += Gdx.graphics.getRawDeltaTime();
			if (triggerUp >= repeatThreshold) {
				triggerUp = repeatThreshold - repeatSpeed;
				setValue(add(getValue(),step));
			}
		} else if (triggerDown >= 0f) {
			triggerDown += Gdx.graphics.getRawDeltaTime();
			if (triggerDown >= repeatThreshold) {
				triggerDown = repeatThreshold - repeatSpeed;
				setValue(subtract(getValue(),step));
			}
		}
		super.draw(batch, parentAlpha);
	}

	/** @return The last known valid value. */
	public final T getValue() {
		return oldValue;
	}

	public final float getDragThreshold() {
		return dragThreshold;
	}

	@Override
	public void invalidate() {
		super.invalidate();
	}

	@Override
	public final void setValue(T value) {
		value = clamp(value, min, max);
		final boolean hasChanged = value != oldValue;
		if (hasChanged) {
			oldValue = value;
			final String formattedValue = formatNumber(value);
			super.setText(formattedValue);
			setColor(Color.WHITE);
			fireChangeEvent();
		}
	}

	private void fireChangeEvent() {
		fire(new NumericInputEvent<T>(oldValue));
		if (connectible != null) {
			connectible.requestValue(oldValue);
		}
	}

	/**
	 * @param color
	 *            The color with which the text field will be tinted when an
	 *            invalid value has been entered.
	 */
	public final void setErrorColor(final Color color) {
		if (color != null) {
			errorColor.set(color);
		}
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

	public final void setStep(final T step) {
		this.step = max(step, zero);
		refreshValue();
	}

	protected void refreshMaxLength() {
		refreshMaxLength = true;
	}

	public final void setMinMax(final T min, final T max) {
		setMin(min);
		setMax(max);
	}

	public final void setMin(final T min) {
		this.min = min(min, max);
		refreshMaxLength();
		refreshValue();
	}


	public final void setMax(final T max) {
		this.max = max(min, max);
		refreshMaxLength();
		refreshValue();
	}

	protected void refreshValue() {
		setValue(getValue());
	}

	private final FocusListener focusListener = new FocusListener() {
		@Override
		public void scrollFocusChanged(final FocusEvent event, final Actor actor, final boolean focused) {
			refreshValue();
			applyQueuedChange();
			editMode = false;
		}

		@Override
		public void keyboardFocusChanged(final FocusEvent event, final Actor actor, final boolean focused) {
			refreshValue();
			applyQueuedChange();
			editMode = false;
		}
	};

	private void applyQueuedChange() {
		if (queuedChange != null) {
			setValue(queuedChange);
			queuedChange = null;
		}
	}

	private final InputListener inputListener = new ClickListener() {
		private final Vector2 touchStart = new Vector2();
		private T touchStartValue;

		@Override
		public void clicked (final InputEvent event, final float x, final float y) {
			editMode = true;
		}

		@Override
		public boolean keyDown(final InputEvent event, final int keycode) {
			super.keyDown(event, keycode);
			switch (keycode) {
			case Keys.UP:
			case Keys.PAGE_UP:
				applyQueuedChange();
				triggerUp = 0f;
				triggerDown = -1f;
				setValue(add(getValue(),getStep()));
				return true;
			case Keys.DOWN:
			case Keys.PAGE_DOWN:
				applyQueuedChange();
				triggerDown = 0f;
				triggerUp = -1f;
				setValue(subtract(getValue(),getStep()));
				return true;
			default:
				return false;
			}
		}

		@Override
		public boolean keyUp(final InputEvent event, final int keycode) {
			super.keyUp(event, keycode);
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
			super.scrolled(event, x, y, amount);
			setValue(subtract(getValue(),scale(getStep(),amount)));
			return true;
		}

		@Override
		public boolean touchDown(final InputEvent event, final float x, final float y, final int pointer, final int button) {
			super.touchDown(event, x, y, pointer, button);
			final Stage stage = getStage();
			if (stage != null && !isDisabled()) {
				stage.setKeyboardFocus(NumericInput.this);
				stage.setScrollFocus(NumericInput.this);
			}
			if (pointer > 0)
				return false;
			applyQueuedChange();
			touchStart.set(x, y);
			touchStartValue = getValue();
			return true;
		}

		@Override
		public void touchDragged(final InputEvent event, final float x, final float y, final int pointer) {
			super.touchDragged(event, x, y, pointer);
			float amount = y - touchStart.y;
			if (Math.abs(amount) > dragThreshold) {
				amount = amount < 0 ? amount + dragThreshold : amount - dragThreshold;
				amount *= 0.5f;
				setValue(add(touchStartValue, scale(getStep(), amount)));
			}
		}
	};

	private final ChangeListener changeListener = new ChangeListener() {
		@Override
		public void changed(final ChangeEvent event, final Actor actor) {
			if (actor instanceof NumericInput) {
				final NumericInput<?> input = (NumericInput<?>) actor;
				final String value = input.getText();
				if (value.isEmpty())
					return;
				final T tValue = parseNumber(value);
				if (tValue != null) {
					if (clamp(tValue, min, max).equals(tValue)) {
						queuedChange = tValue;
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
	};
	private ModelConnectibleRequest<T> connectible;

	public static abstract class NumericInputListener<T extends Number> implements EventListener {
		@Override
		public boolean handle (final Event event) {
			if (!(event instanceof NumericInputEvent)) return false;
			final Number value = ((NumericInputEvent<?>)event).value;
			try {
				@SuppressWarnings("unchecked")
				final T tval = (T)value;
				changed(tval, event.getTarget());
			}
			catch (final ClassCastException e) {
				LOG.error("Wrong event passed to handler");
			}
			return false;
		}
		protected abstract void changed(T value, Actor actor);
	}

	public static class NumericInputEvent<T extends Number> extends Event {
		public final T value;
		public NumericInputEvent(final T value) {
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

	public T getStep() {
		return step;
	}

	@Override
	public void reset() {
		clearActions();
		clearListeners();
		clearSelection();
		setStep(zero);
		setErrorColor(Color.RED);
		setMinMax(getMinValue(), getMaxValue());
		setValue(zero);
		setVisible(true);
		setMessageText(StringUtils.EMPTY);
		setDragThreshold(16f);
		setText(StringUtils.EMPTY);
		setBlinkTime(0.32f);
		setMaxLength(0);
		setDisabled(false);
		addListener(getDefaultInputListener());
		init();
	}

	@Override
	public void onValueChange(final ModelConnectibleRequest<T> connectible) {
		this.connectible = connectible;
	}

	public static class FloatNumericInput extends NumericInput<Float> {
		private static Pool<FloatNumericInput> POOL;
		private String format = "%.02f";
		public FloatNumericInput(final float value, final Skin skin) {
			this(value, skin.get(TextFieldStyle.class));
		}
		public FloatNumericInput(final float value, final Skin skin, final String styleName) {
			this(value, skin.get(styleName, TextFieldStyle.class));
		}
		public FloatNumericInput(final float initialValue, final TextFieldStyle style) {
			super(initialValue, style);
		}

		@Override
		protected Float parseNumber(final String value) {
			final Scanner s = new Scanner(value);
			s.useLocale(Locale.ROOT);
			final Float f = s.hasNextFloat() ? s.nextFloat() : null;
			IOUtils.closeQuietly(s);
			return f;
		}

		@Override
		protected int compare(final Float x, final Float y) {
			return Float.compare(x, y);
		}
		@Override
		protected Float getMinValue() {
			return -Float.MAX_VALUE;
		}
		@Override
		protected Float getMaxValue() {
			return Float.MAX_VALUE;
		}
		@Override
		protected Float add(final Float x, final Float y) {
			return Float.sum(x, y);
		}
		@Override
		protected Float subtract(final Float x, final Float y) {
			return x-y;
		}
		@Override
		protected Float scale(final Float x, final float factor) {
			return x*factor;
		}
		@Override
		protected String formatNumber(final Float value) {
			return String.format(Locale.ROOT, format, value);
		}
		@Override
		protected Float zero() {
			return 0f;
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

		private static Pool<FloatNumericInput> pool(final TextFieldStyle style) {
			if (POOL == null) {
				POOL = new Pool<FloatNumericInput>() {
					@Override
					protected FloatNumericInput newObject() {
						return new FloatNumericInput(0f, style);
					}
				};
			}
			return POOL;
		}

		public static FloatNumericInput obtain(final Skin skin) {
			return obtain(skin.get(TextFieldStyle.class));
		}
		public static FloatNumericInput obtain(final Skin skin, final String styleName) {
			return obtain(skin.get(styleName, TextFieldStyle.class));
		}
		public static FloatNumericInput obtain(final TextFieldStyle style) {
			final FloatNumericInput input = pool(style).obtain();
			input.setStyle(style);
			return input;
		}

		public static void free(final FloatNumericInput input) {
			if (POOL != null) {
				POOL.free(input);
			}
		}
	}

	public static class IntegerNumericInput extends NumericInput<Integer> {
		private static Pool<IntegerNumericInput> POOL;
		private int radix;

		public IntegerNumericInput(final int value, final Skin skin) {
			this(value, 10, skin);
		}
		public IntegerNumericInput(final int value, final Skin skin, final String styleName) {
			this(value, 10, skin, styleName);
		}
		public IntegerNumericInput(final int initialValue, final TextFieldStyle style) {
			this(initialValue, 10, style);
		}
		public IntegerNumericInput(final int value, final int radix, final Skin skin) {
			this(value, radix, skin.get(TextFieldStyle.class));
		}
		public IntegerNumericInput(final int value, final int radix, final Skin skin, final String styleName) {
			this(value, radix, skin.get(styleName, TextFieldStyle.class));
		}
		public IntegerNumericInput(final int initialValue, final int radix, final TextFieldStyle style) {
			super(initialValue, style);
			this.radix = MathUtils.clamp(radix, 2, 36);
		}

		@Override
		protected Integer parseNumber(final String value) {
			final Scanner s = new Scanner(value);
			s.useLocale(Locale.ROOT);
			final Integer i = s.hasNextInt(radix) ? s.nextInt(radix) : null;
			IOUtils.closeQuietly(s);
			return i;
		}

		@Override
		protected Integer getMinValue() {
			return Integer.MIN_VALUE;
		}

		@Override
		protected Integer getMaxValue() {
			return Integer.MAX_VALUE;
		}

		@Override
		protected Integer add(final Integer x, final Integer y) {
			return Integer.sum(x,y);
		}

		@Override
		protected Integer subtract(final Integer x, final Integer y) {
			return x - y;
		}

		@Override
		protected Integer scale(final Integer x, final float factor) {
			return MathUtils.round(x*factor);
		}

		@Override
		protected int compare(final Integer x, final Integer y) {
			return Integer.compare(x, y);
		}
		@Override
		protected Integer zero() {
			return 0;
		}

		private static Pool<IntegerNumericInput> pool(final TextFieldStyle style) {
			if (POOL == null) {
				POOL = new Pool<IntegerNumericInput>() {
					@Override
					protected IntegerNumericInput newObject() {
						return new IntegerNumericInput(0, style);
					}
				};
			}
			return POOL;
		}

		public static IntegerNumericInput obtain(final int radix, final Skin skin) {
			return obtain(radix, skin.get(TextFieldStyle.class));
		}
		public static IntegerNumericInput obtain(final int radix, final Skin skin, final String styleName) {
			return obtain(radix, skin.get(styleName, TextFieldStyle.class));
		}
		public static IntegerNumericInput obtain(final int radix, final TextFieldStyle style) {
			final IntegerNumericInput input = pool(style).obtain();
			input.radix = radix;
			input.setStyle(style);
			input.refreshValue();
			return input;
		}

		public static void free(final IntegerNumericInput input) {
			if (POOL != null) {
				POOL.free(input);
			}
		}
		@Override
		protected String formatNumber(final Integer value) {
			return Integer.toString(value, radix);
		}
	}

	public static class LongNumericInput extends NumericInput<Long> {
		private static Pool<LongNumericInput> POOL;
		private int radix;

		public LongNumericInput(final long value, final Skin skin) {
			this(value, 10, skin);
		}
		public LongNumericInput(final long value, final Skin skin, final String styleName) {
			this(value, 10, skin, styleName);
		}
		public LongNumericInput(final long initialValue, final TextFieldStyle style) {
			this(initialValue, 10, style);
		}
		public LongNumericInput(final long value, final int radix, final Skin skin) {
			this(value, radix, skin.get(TextFieldStyle.class));
		}
		public LongNumericInput(final long value, final int radix, final Skin skin, final String styleName) {
			this(value, radix, skin.get(styleName, TextFieldStyle.class));
		}
		public LongNumericInput(final long initialValue, final int radix, final TextFieldStyle style) {
			super(initialValue, style);
			this.radix = MathUtils.clamp(radix, 2, 36);
		}

		@Override
		protected Long parseNumber(final String value) {
			final Scanner s = new Scanner(value);
			s.useLocale(Locale.ROOT);
			final Long l = s.hasNextLong(radix) ? s.nextLong(radix) : null;
			IOUtils.closeQuietly(s);
			return l;
		}

		@Override
		protected Long getMinValue() {
			return Long.MIN_VALUE;
		}

		@Override
		protected Long getMaxValue() {
			return Long.MAX_VALUE;
		}

		@Override
		protected Long add(final Long x, final Long y) {
			return Long.sum(x,y);
		}

		@Override
		protected Long subtract(final Long x, final Long y) {
			return x - y;
		}

		@Override
		protected Long scale(final Long x, final float factor) {
			return Math.round(x*(double)factor);
		}

		@Override
		protected int compare(final Long x, final Long y) {
			return Long.compare(x, y);
		}
		@Override
		protected Long zero() {
			return 0L;
		}

		private static Pool<LongNumericInput> pool(final TextFieldStyle style) {
			if (POOL == null) {
				POOL = new Pool<LongNumericInput>() {
					@Override
					protected LongNumericInput newObject() {
						return new LongNumericInput(0, style);
					}
				};
			}
			return POOL;
		}

		public static LongNumericInput obtain(final int radix, final Skin skin) {
			return obtain(radix, skin.get(TextFieldStyle.class));
		}
		public static LongNumericInput obtain(final int radix, final Skin skin, final String styleName) {
			return obtain(radix, skin.get(styleName, TextFieldStyle.class));
		}
		public static LongNumericInput obtain(final int radix, final TextFieldStyle style) {
			final LongNumericInput input = pool(style).obtain();
			input.radix = radix;
			input.setStyle(style);
			input.refreshValue();
			return input;
		}

		public static void free(final LongNumericInput input) {
			if (POOL != null) {
				POOL.free(input);
			}
		}
		@Override
		protected String formatNumber(final Long value) {
			return Long.toString(value, radix);
		}
	}
}
