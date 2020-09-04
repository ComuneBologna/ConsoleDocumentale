package it.eng.portlet.consolepec.gwt.server;

import it.eng.portlet.consolepec.gwt.server.validators.IONOIValidatorsChain;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.server.actionvalidator.ActionValidator;
import com.gwtplatform.dispatch.server.spring.HandlerModule;
import com.gwtplatform.dispatch.server.spring.utils.SpringUtils;
import com.gwtplatform.dispatch.shared.Action;
import com.gwtplatform.dispatch.shared.Result;

@Configuration
/**
 * Classe da cui far estender ServerModule applicativo, nel caso in cui l'applicazione utilizzi Spring e Gwtp spring integration
 * @author pluttero
 *
 */
public abstract class IONOHandlerModule extends HandlerModule implements ApplicationContextAware {

	@Autowired
	IONOIValidatorsChain chain;

	@Bean
	public IONOIValidatorsChain getIONOIValidatorsChain() {
		return new IONOIValidatorsChain();
	}

	@Override
	protected <A extends Action<R>, R extends Result> void bindHandler(Class<A> actionClass, Class<? extends ActionHandler<A, R>> handlerClass) {
		super.bindHandler(actionClass, handlerClass, IONOIValidatorsChain.class);
	}

	@Override
	protected <A extends com.gwtplatform.dispatch.shared.Action<R>, R extends Result> void bindHandler(java.lang.Class<A> actionClass, java.lang.Class<? extends com.gwtplatform.dispatch.server.actionhandler.ActionHandler<A, R>> handlerClass,
			java.lang.Class<? extends com.gwtplatform.dispatch.server.actionvalidator.ActionValidator> actionValidator) {
		ActionValidator instance = SpringUtils.getOrCreate(applicationContext, actionValidator);
		chain.addUserValidator(actionClass, instance);
	};

	@Required
	public void setInterceptors(List<ActionValidator> validators) {
		chain.setChain(validators);
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

}
