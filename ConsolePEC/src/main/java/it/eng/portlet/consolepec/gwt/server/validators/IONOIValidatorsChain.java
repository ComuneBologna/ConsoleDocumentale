package it.eng.portlet.consolepec.gwt.server.validators;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.gwtplatform.dispatch.server.actionvalidator.ActionValidator;
import com.gwtplatform.dispatch.shared.Action;
import com.gwtplatform.dispatch.shared.ActionException;
import com.gwtplatform.dispatch.shared.Result;

public class IONOIValidatorsChain implements ActionValidator {
	List<ActionValidator> chain = new ArrayList<ActionValidator>();
	Map<Class<?>, ActionValidator> map = new ConcurrentHashMap<Class<?>, ActionValidator>();

	@Override
	public boolean isValid(Action<? extends Result> action) throws ActionException {
		boolean res = true;
		/* eseguo i validatori di framework */
		for (ActionValidator validator : chain) {
			if (res)
				res &= validator.isValid(action);
		}
		/* eseguo il validatore specifico della action */
		if (res && map.containsKey(action.getClass()))
			res &= map.get(action.getClass()).isValid(action);

		return res;
	}

	public <A extends Action<R>, R extends Result> void addUserValidator(Class<A> actionClass, ActionValidator actionValidator) {

		map.put(actionClass, actionValidator);
	}

	public void setChain(List<ActionValidator> list) {
		chain.addAll(list);
	}
}
