package it.eng.portlet.consolepec.gwt.client.validation;

import it.eng.portlet.consolepec.gwt.shared.action.CercaPratiche;

import javax.validation.Validator;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.validation.client.AbstractGwtValidatorFactory;
import com.google.gwt.validation.client.GwtValidation;
import com.google.gwt.validation.client.impl.AbstractGwtValidator;

public class ConsolePECValidationFactory extends AbstractGwtValidatorFactory {

	@GwtValidation(CercaPratiche.class)
	public interface GwtValidator extends Validator {

	}

	@Override
	public AbstractGwtValidator createValidator() {
		return GWT.create(GwtValidator.class);
	}

}
