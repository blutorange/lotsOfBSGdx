package de.homelab.madgaksha.scene2dext.model;

import com.google.common.base.Converter;

public class NumberConversionModel<A, B extends Number, MODELA extends SingleValueModel<A>> extends NumberModel<B> {
	private final ModelProvider<MODELA> provider;
	private final Converter<A, B> converter;

	public NumberConversionModel(final Converter<A, B> converter, final ModelProvider<MODELA> provider) {
		super(converter.convert(provider.getProvidedObject().getValue()));
		this.provider = provider;
		this.converter = converter;
	}

	@Override
	public void setValue(final B value) {
		provider.getProvidedObject().setValue(converter.reverse().convert(value));
	}

	@Override
	public B getValue() {
		return converter.convert(provider.getProvidedObject().getValue());
	}
}
