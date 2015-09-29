package br.concatto.fonicsynth;

import javafx.beans.binding.StringBinding;
import javafx.beans.property.SimpleIntegerProperty;

public class InstrumentProperty extends SimpleIntegerProperty {
	public InstrumentProperty(int initialValue) {
		super(initialValue);
	}

	@Override
	public StringBinding asString() {
		return new StringBinding() {
			{
				bind(InstrumentProperty.this);
			}

			protected String computeValue() {
				return Instruments.get(InstrumentProperty.this.get());
			}
		};
	}
}