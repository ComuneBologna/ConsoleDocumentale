package it.eng.portlet.consolepec.gwt.client.presenter.template;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gwt.user.client.Command;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.annotations.ProxyEvent;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;

import it.eng.portlet.consolepec.gwt.client.db.PecInPraticheDB;
import it.eng.portlet.consolepec.gwt.client.operazioni.template.TemplateSceltaWizardApiClient;
import it.eng.portlet.consolepec.gwt.client.place.NameTokens;
import it.eng.portlet.consolepec.gwt.client.presenter.MainPresenter;
import it.eng.portlet.consolepec.gwt.client.presenter.template.event.GoToInserimentoCampiTemplateEmailEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.template.event.GoToInserimentoCampiTemplateEmailEvent.GoToInserimentoCampiTemplateEmailHandler;
import it.eng.portlet.consolepec.gwt.client.presenter.template.event.GoToInserimentoCampiTemplatePdfEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.template.event.GoToInserimentoCampiTemplatePdfEvent.GoToInserimentoCampiTemplatePdfHandler;
import it.eng.portlet.consolepec.gwt.client.widget.SitemapMenu;
import it.eng.portlet.consolepec.gwt.shared.model.CampoTemplateDTO;

public class CompilaCampiTemplatePresenter extends Presenter<CompilaCampiTemplatePresenter.MyView, CompilaCampiTemplatePresenter.MyProxy> implements GoToInserimentoCampiTemplateEmailHandler, GoToInserimentoCampiTemplatePdfHandler {

	private TemplateSceltaWizardApiClient templateSceltaWizard;

	public interface MyView extends View {

		void mostraCampi(List<CampoTemplateDTO> campi);

		boolean controllaCampi();

		void setAvantiCommand(Command annullaCommand);

		void setAnnullaCommand(Command annullaCommand);

		void setIndietroCommand(Command annullaCommand);

		Map<CampoTemplateDTO, Object> getValori();

		void mostraCampoFileName(boolean b);

		String getFileName();

		boolean hasFileName();

		void clear();
	}

	@ProxyCodeSplit
	@NameToken(NameTokens.compilacampitemplate)
	public interface MyProxy extends ProxyPlace<CompilaCampiTemplatePresenter> {}

	@Inject
	public CompilaCampiTemplatePresenter(final EventBus eventBus, final MyView view, final MyProxy proxy, final PecInPraticheDB pecInDb, final SitemapMenu sitemapMenu, final DispatchAsync dispatcher,
			final TemplateSceltaWizardApiClient templateSceltaWizard) {
		super(eventBus, view, proxy);
		this.templateSceltaWizard = templateSceltaWizard;
	}

	@Override
	protected void revealInParent() {
		RevealContentEvent.fire(this, MainPresenter.TYPE_SetMainContent, this);
	}

	@Override
	protected void onBind() {
		super.onBind();

		getView().setAnnullaCommand(new AnnullaCommand());
		getView().setIndietroCommand(new IndietroCommand());
	}

	@Override
	@ProxyEvent
	public void onGoToInserimentoCampiTemplateEmail(GoToInserimentoCampiTemplateEmailEvent event) {

		getView().setAvantiCommand(new AvantiEmailCommand());
		getView().mostraCampoFileName(false);
		getView().mostraCampi(event.getCampi());

		revealInParent();
	}

	@Override
	@ProxyEvent
	public void onGoToInserimentoCampiTemplatePdf(GoToInserimentoCampiTemplatePdfEvent event) {

		getView().setAvantiCommand(new AvantiPdfCommand());
		getView().mostraCampoFileName(!event.isTitoloAutomatico());
		getView().mostraCampi(event.getCampi());

		revealInParent();
	}

	private class AvantiEmailCommand implements Command {

		@Override
		public void execute() {
			if (getView().controllaCampi()) {
				Map<String, String> valori = new LinkedHashMap<String, String>();
				for (Entry<CampoTemplateDTO, Object> entry : getView().getValori().entrySet()) {
					if (entry.getValue() != null) {
						valori.put(entry.getKey().getNome(), entry.getValue().toString());
					}
				}
				templateSceltaWizard.creaBozzaDaTemplate(valori);
			}
		}

	}

	private class AvantiPdfCommand implements Command {

		@Override
		public void execute() {
			if (getView().controllaCampi()) {
				Map<String, String> valori = new LinkedHashMap<String, String>();
				for (Entry<CampoTemplateDTO, Object> entry : getView().getValori().entrySet()) {
					if (entry.getValue() != null) {
						valori.put(entry.getKey().getNome(), entry.getValue().toString());
					}
				}

				String filename = null;

				if (getView().hasFileName()) {
					filename = getView().getFileName();
					filename = filename + ".pdf";
				}

				templateSceltaWizard.creaPdfDaTemplate(valori, filename);
			}
		}

	}

	private class IndietroCommand implements Command {

		@Override
		public void execute() {
			getView().clear();
			templateSceltaWizard.backToSceltaTemplate();
		}

	}

	private class AnnullaCommand implements Command {

		@Override
		public void execute() {
			getView().clear();
			templateSceltaWizard.goBackToPratica();
		}

	}
}
