package it.eng.portlet.consolepec.gwt.client.widget.formricerca;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.gwt.user.client.ui.TextBox;

import it.eng.cobo.consolepec.util.console.ConsoleConstants;
import it.eng.portlet.consolepec.gwt.client.widget.suggestBox.oracle.IndirizziEmailSuggestOracle;
import it.eng.portlet.consolepec.gwt.client.widget.suggestBox.oracle.SpacebarSuggestOracle;
import it.eng.portlet.consolepec.gwt.shared.action.CercaPratiche;
import it.eng.portlet.consolepec.gwt.shared.model.PecOutDTO.StatoDTO;
import it.eng.portlet.consolepec.gwt.shared.model.TipoEmail;

public abstract class AbstractGruppoFiltriPecWidget extends AbstractGruppoFiltriWidget {

	@UiField(provided = true)
	SuggestBox statoSuggestBox;
	@UiField(provided = true)
	SuggestBox destinatariSuggestBox;
	@UiField(provided = true)
	SuggestBox destinatariCCSuggestBox;
	@UiField(provided = true)
	SuggestBox tipoMailSuggestBox;
	@UiField
	TextBox idEmailTextBox;

	private boolean resetDestinatario = true;

	public AbstractGruppoFiltriPecWidget() {
		super();

		initSuggestBoxStati();
		initSuggestBoxTipoMail();
		initSuggestBoxDestinatari();
	}

	/**
	 * Il metodo viene invocato dalla View su cui è utilizzata la form. Deve essere chiamato una volta sola
	 *
	 * @param cercaCommand - Command invocato quando si preme il pulsante Cerca, oppure invio su di una widget
	 */
	@Override
	public void configura(final com.google.gwt.user.client.Command cercaCommand) {
		super.configura(cercaCommand);

		KeyDownHandler invioBt = new KeyDownHandler() {
			@Override
			public void onKeyDown(KeyDownEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					cercaCommand.execute();
				}
			}
		};
		setPlaceholder(this.statoSuggestBox.getElement(), "Stato");
		setPlaceholder(idEmailTextBox.getElement(), "ID mail");
		idEmailTextBox.addKeyDownHandler(invioBt);
		setPlaceholder(destinatariSuggestBox.getElement(), "Destinatari");
		destinatariSuggestBox.addKeyDownHandler(invioBt);
		setPlaceholder(destinatariCCSuggestBox.getElement(), "Destinatari CC");
		destinatariCCSuggestBox.addKeyDownHandler(invioBt);
		setPlaceholder(tipoMailSuggestBox.getElement(), "Tipo mail");
	}

	@Override
	public void serializza(CercaPratiche dto) {
		List<String> stati = new ArrayList<String>();
		it.eng.portlet.consolepec.gwt.shared.model.PecInDTO.StatoDTO statoPecIn = it.eng.portlet.consolepec.gwt.shared.model.PecInDTO.StatoDTO.fromLabel(statoSuggestBox.getText().trim());
		if (statoPecIn != null) {
			stati.add(statoPecIn.name());
		} else {
			List<StatoDTO> statiPecOut = it.eng.portlet.consolepec.gwt.shared.model.PecOutDTO.StatoDTO.fromLabel(statoSuggestBox.getText().trim());
			for (StatoDTO s : statiPecOut)
				stati.add(s.name());
		}
		if (!stati.isEmpty())
			dto.setStato(stati.toArray(new String[stati.size()]));

		if (this.destinatariSuggestBox.isEnabled())
			dto.setDestinatario(this.destinatariSuggestBox.getValue());
		else
			dto.setDestinatario(this.destinatariSuggestBox.getValue());

		dto.setCc(destinatariCCSuggestBox.getValue());
		dto.setIdEmail(idEmailTextBox.getValue());
		TipoEmail tipoEmail = TipoEmail.fromLabel(tipoMailSuggestBox.getValue().trim());
		dto.setTipoEmail(tipoEmail);
	}

	@Override
	public void reset() {
		statoSuggestBox.setValue(null);
		destinatariCCSuggestBox.setValue(null);
		idEmailTextBox.setValue(null);
		tipoMailSuggestBox.setValue(null);

		if (resetDestinatario) {
			destinatariSuggestBox.setValue(null);
		}
	}

	/* metodi interni */

	protected abstract void initSuggestBoxStati();

	private void initSuggestBoxTipoMail() {
		List<String> suggestions = new ArrayList<String>();
		for (TipoEmail stato : TipoEmail.values()) {

			suggestions.add(stato.getLabel());
		}
		SpacebarSuggestOracle oracle = new SpacebarSuggestOracle(suggestions);
		tipoMailSuggestBox = new SuggestBox(oracle);
	}

	private void initSuggestBoxDestinatari() {
		SuggestOracle so = new IndirizziEmailSuggestOracle(new ArrayList<String>());
		destinatariSuggestBox = new SuggestBox(so);
		destinatariCCSuggestBox = new SuggestBox(so);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setParametriFissiWorklist(Map<String, Object> parametriFissiWorklist) {
		super.setParametriFissiWorklist(parametriFissiWorklist);
		resetDestinatario = true;
		List<String> destinatari = new ArrayList<String>();

		if (parametriFissiWorklist.get(ConsoleConstants.FiltriRicerca.DESTINATARI.getFiltro()) != null) {
			// destinatari
			for (String des : (List<String>) parametriFissiWorklist.get(ConsoleConstants.FiltriRicerca.DESTINATARI.getFiltro())) {
				destinatari.add(des);
			}

		} else if (parametriFissiWorklist.get(ConsoleConstants.FiltriRicerca.DESTINATARIO.getFiltro()) != null) {
			// destinatario
			destinatari.add((String) parametriFissiWorklist.get(ConsoleConstants.FiltriRicerca.DESTINATARIO.getFiltro()));
		}

		if (!destinatari.isEmpty()) {

			if (destinatari.size() == 1) {
				destinatariSuggestBox.setValue(destinatari.get(0));

				if (destinatariSuggestBox.isEnabled()) {
					destinatariSuggestBox.setEnabled(false);
				}

				if (!"testo disabilitato".equalsIgnoreCase(destinatariSuggestBox.getStyleName())) {
					destinatariSuggestBox.setStyleName("testo disabilitato");
				}

				resetDestinatario = false;

			} else {
				IndirizziEmailSuggestOracle so = (IndirizziEmailSuggestOracle) destinatariSuggestBox.getSuggestOracle();
				so.setElenco(destinatari);
				destinatariSuggestBox.removeStyleName("disabilitato");
				destinatariSuggestBox.setEnabled(true);
				destinatariSuggestBox.setValue("");
			}

		} else {
			destinatariSuggestBox.removeStyleName("disabilitato");
			destinatariSuggestBox.setEnabled(true);
			destinatariSuggestBox.setValue("");
		}

	}
}
