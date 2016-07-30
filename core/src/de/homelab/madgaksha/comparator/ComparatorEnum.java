package de.homelab.madgaksha.comparator;

import java.util.Comparator;

public class ComparatorEnum {
	public final static Comparator<Enum<?>> NAME_ASCENDING = new Comparator<Enum<?>>() {
		@Override
		public int compare(Enum<?> arg0, Enum<?> arg1) {
			return arg0.toString().compareTo(arg1.toString());
		}
	};
	public final static Comparator<Enum<?>> NAME_DESCENDING = new Comparator<Enum<?>>() {
		@Override
		public int compare(Enum<?> arg0, Enum<?> arg1) {
			return arg1.toString().compareTo(arg0.toString());
		}
	};

}
