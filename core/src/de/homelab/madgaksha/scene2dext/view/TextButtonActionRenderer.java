package de.homelab.madgaksha.scene2dext.view;

import java.util.Collection;
import java.util.HashSet;
import java.util.Locale;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableSet;

import de.homelab.madgaksha.lotsofbs.logging.Logger;
import de.homelab.madgaksha.scene2dext.model.Action;
import de.homelab.madgaksha.scene2dext.model.Model;
import de.homelab.madgaksha.scene2dext.model.ModelProvider;

public class TextButtonActionRenderer<T extends Model> extends AbstractActionRenderer<T> {
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(TextButtonActionRenderer.class);
	public final static Integer ONE = 1;
	public final static Integer TWO = 2;
	public final static Integer THREE = 3;

	private String text;
	private final Collection<Integer> onTapCount = new HashSet<Integer>();

	public TextButtonActionRenderer() {
		this(null);
	}

	public TextButtonActionRenderer(final String text) {
		this.text = text;
		onTapCount.add(ONE);
	}

	@Override
	public ItemView<T> render(final ModelProvider<T> provider, final Action<T> action, final Skin skin) {
		final String t = text != null ?  text : action.getName(Locale.ROOT);
		return new ButtonView<T>(provider, action, t, ImmutableSet.copyOf(onTapCount), skin);
	}

	/**
	 * Sets the text to the {@link Action#getName(Locale)} of the action. Use {@link Locale#ROOT}.
	 * @param provider For the model on which the action will be run.
	 * @param action Action to be run on the model when the button is clicked.
	 * @param skin Skin for styling.
	 * @return The rendered item.
	 * @see #render(ModelProvider, Action, Skin)
	 */
	public ItemView<T> renderWithDefaultText(final ModelProvider<T> provider, final Action<T> action, final Skin skin) {
		return renderWithDefaultText(provider, action, Locale.ROOT, skin);
	}

	/**
	 * Sets the text to the {@link Action#getName(Locale)} of the action.
	 * @param provider For the model on which the action will be run.
	 * @param action Action to be run on the model when the button is clicked.
	 * @param locale Locale for the name of the action.
	 * @param skin Skin for styling.
	 * @return The rendered item.
	 * @see #render(ModelProvider, Action, Skin)
	 */
	public ItemView<T> renderWithDefaultText(final ModelProvider<T> provider, final Action<T> action, final Locale locale, final Skin skin) {
		return new ButtonView<T>(provider, action, action.getName(locale), ImmutableSet.copyOf(onTapCount), skin);
	}

	public TextButtonActionRenderer<T> setText(final String text) {
		this.text = text;
		return this;
	}

	public TextButtonActionRenderer<T> onTapCount(final Integer tapCount, final boolean performAction) {
		if (performAction) {
			onTapCount.add(tapCount);
		} else {
			onTapCount.remove(tapCount);
		}
		return this;
	}


	public static class ButtonView<T extends Model> extends TextButton implements ItemView<T> {
		private final ModelProvider<T> provider;
		private final Action<T> action;
		private final Collection<Integer> onTapCount;

		public ButtonView(final ModelProvider<T> provider, final Action<T> action, final String text, final ImmutableCollection<Integer> onTapCount, final Skin skin) {
			super(text, skin);
			this.provider = provider;
			this.action = action;
			this.onTapCount = onTapCount;
			createView();
		}

		private void createView() {
			addListener(new ClickListener() {
				@Override
				public void clicked(final InputEvent event, final float x, final float y) {
					if (onTapCount.contains(getTapCount())) {
						action.actOnModel(provider.getProvidedObject());
					}
				}
			});
		}

		@Override
		public ModelProvider<T> getModelProvider() {
			return provider;
		}

		@Override
		public Actor asActor() {
			return this;
		}
	}
}
