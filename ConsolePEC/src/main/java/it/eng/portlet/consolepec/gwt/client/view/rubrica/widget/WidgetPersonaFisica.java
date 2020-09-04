package it.eng.portlet.consolepec.gwt.client.view.rubrica.widget;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.gwt.user.datepicker.client.DateBox.Format;

import it.eng.cobo.consolepec.commons.profilazione.Settore;
import it.eng.cobo.consolepec.commons.rubrica.Anagrafica.Stato;
import it.eng.cobo.consolepec.commons.rubrica.Email;
import it.eng.cobo.consolepec.commons.rubrica.Indirizzo;
import it.eng.cobo.consolepec.commons.rubrica.PersonaFisica;
import it.eng.cobo.consolepec.commons.rubrica.Telefono;
import it.eng.cobo.consolepec.util.console.ConsoleConstants;
import it.eng.cobo.consolepec.util.console.SettoriUtils;

/**
 * @author GiacomoFM
 * @since 17/ott/2017
 */
public class WidgetPersonaFisica extends Composite {

	private static DateTimeFormat dateFormat = DateTimeFormat.getFormat(ConsoleConstants.FORMATO_DATA);

	private List<Settore> settori;

	@UiField Button importaLagButton;
	private HandlerRegistration clickReg;

	@UiField HTMLPanel cfPanel;

	@UiField TextBox codiceFiscale;

	@UiField TextBox nome;

	@UiField TextBox cognome;

	@UiField DateBox dataNascita;

	@UiField TextBox comuneNascita;

	@UiField ListBox stato;

	@UiField WidgetListaIndirizzi listaIndirizziWidget;

	@UiField WidgetListaTelefoni listaTelefoniWidget;

	@UiField WidgetListaEmail listaEmailWidget;

	private static Binder binder = GWT.create(Binder.class);

	// private boolean enableButtonListDettaglioAnagrafica = false;

	public interface Binder extends UiBinder<Widget, WidgetPersonaFisica> {/**/}

	public WidgetPersonaFisica(List<Settore> settori) {
		super();
		this.settori = settori;
		initWidget(binder.createAndBindUi(this));

		Format format = new DateBox.DefaultFormat(dateFormat);
		dataNascita.setFormat(format);
		dataNascita.getDatePicker().setYearArrowsVisible(true);

		for (Stato s : Stato.values()) {
			stato.addItem(s.toString());
		}
	}

	public String getCodiceFiscale() {
		if (codiceFiscale != null) {
			return codiceFiscale.getValue();
		}
		return null;
	}

	public PersonaFisica get() {
		PersonaFisica pf = new PersonaFisica();
		pf.getGruppi().addAll(SettoriUtils.transform(settori));
		pf.setCodiceFiscale(codiceFiscale.getText());
		pf.setNome(nome.getText());
		pf.setCognome(cognome.getText());

		pf.setDataNascita(dataNascita.getValue());
		pf.setLuogoNascita(comuneNascita.getText());

		pf.setStato(Stato.valueOf(stato.getSelectedItemText()));
		pf.getIndirizzi().addAll(listaIndirizziWidget.get());
		pf.getTelefoni().addAll(listaTelefoniWidget.get());
		pf.getEmail().addAll(listaEmailWidget.get());
		return pf;
	}

	public void set(PersonaFisica personaFisica) {
		if (personaFisica.getCodiceFiscale() != null && !personaFisica.getCodiceFiscale().isEmpty()) {
			codiceFiscale.setEnabled(false);
			codiceFiscale.setStylePrimaryName("disabilitato");
			codiceFiscale.setText(personaFisica.getCodiceFiscale());

			hideImportaLagButton();
			cfPanel.getElement().removeAttribute("style");
		}

		nome.setText(personaFisica.getNome());
		cognome.setText(personaFisica.getCognome());

		dataNascita.setValue(personaFisica.getDataNascita());
		comuneNascita.setText(personaFisica.getLuogoNascita());

		if (Stato.DISATTIVA.equals(personaFisica.getStato())) {
			nome.setEnabled(false);
			nome.setStylePrimaryName("disabilitato");
			cognome.setEnabled(false);
			cognome.setStylePrimaryName("disabilitato");

			dataNascita.setEnabled(false);
			dataNascita.setStylePrimaryName("disabilitato");
			comuneNascita.setEnabled(false);
			comuneNascita.setStylePrimaryName("disabilitato");
		} else {
			nome.setEnabled(true);
			nome.removeStyleName("disabilitato");
			cognome.setEnabled(true);
			cognome.removeStyleName("disabilitato");

			dataNascita.setEnabled(true);
			dataNascita.removeStyleName("disabilitato");
			comuneNascita.setEnabled(true);
			comuneNascita.removeStyleName("disabilitato");
		}

		for (int i = 0; i < stato.getItemCount(); i++) {
			stato.setItemSelected(i, stato.getItemText(i).equals(personaFisica.getStato().toString()));
		}

		listaIndirizziWidget.set(personaFisica.getIndirizzi(), personaFisica.getStato());
		listaTelefoniWidget.set(personaFisica.getTelefoni(), personaFisica.getStato());
		listaEmailWidget.set(personaFisica.getEmail(), personaFisica.getStato());

	}

	public void reset() {
		hideImportaLagButton();
		codiceFiscale.setEnabled(true);
		codiceFiscale.removeStyleName("disabilitato");
		codiceFiscale.setValue("");
		nome.setValue("");
		cognome.setValue("");

		dataNascita.setValue(null);
		comuneNascita.setValue("");

		stato.setItemSelected(0, true);
		listaIndirizziWidget.set(new ArrayList<Indirizzo>(), Stato.ATTIVA);
		listaTelefoniWidget.set(new ArrayList<Telefono>(), Stato.ATTIVA);
		listaEmailWidget.set(new ArrayList<Email>(), Stato.ATTIVA);
	}

	public void setImportaLagCommand(final Command command) {
		if (clickReg != null) {
			clickReg.removeHandler();
		}
		clickReg = importaLagButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				command.execute();
				event.stopPropagation();
			}
		});
	}

	public void hideImportaLagButton() {
		importaLagButton.setEnabled(false);
		importaLagButton.setVisible(false);
	}

	public void showImportaLagButton() {
		importaLagButton.setEnabled(true);
		importaLagButton.setVisible(true);
		importaLagButton.getElement().setAttribute("style", "margin-left: 15px; margin-bottom: 5px;");
		cfPanel.getElement().setAttribute("style", "float: left;");
	}

}
