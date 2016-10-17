package de.homelab.madgaksha.scene2dext.model;

public class ModelRuntimeException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	public ModelRuntimeException() {
	}
	public ModelRuntimeException(final String msg) {
		super(msg);
	}
	public ModelRuntimeException(final String msg, final Throwable throwable) {
		super(msg, throwable);
	}
	public ModelRuntimeException(final Throwable throwable) {
		super(throwable);
	}
	public ModelRuntimeException(final String msg, final Throwable throwable, final boolean b1, final boolean b2) {
		super(msg, throwable, b1, b2);
	}
}