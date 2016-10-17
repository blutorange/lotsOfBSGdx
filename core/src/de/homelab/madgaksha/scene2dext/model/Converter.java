package de.homelab.madgaksha.scene2dext.model;

public interface Converter<A, B> {
	public A convertToA(B b);

	public B convertToB(A a);
}