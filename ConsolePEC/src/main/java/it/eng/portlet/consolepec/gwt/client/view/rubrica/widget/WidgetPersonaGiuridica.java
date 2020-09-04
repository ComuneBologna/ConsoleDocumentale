package it.eng.portlet.consolepec.gwt.client.view.rubrica.widget;

import it.eng.cobo.consolepec.commons.profilazione.Settore;
import it.eng.cobo.consolepec.commons.rubrica.Anagrafica.Stato;
import it.eng.cobo.consolepec.commons.rubrica.Email;
import it.eng.cobo.consolepec.commons.rubrica.Indirizzo;
import it.eng.cobo.consolepec.commons.rubrica.PersonaGiuridica;
import it.eng.cobo.consolepec.commons.rubrica.Telefono;
import it.eng.cobo.consolepec.util.console.SettoriUtils;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author GiacomoFM
 * @since 17/ott/2017
 */
public class WidgetPersonaGiuridica extends Composite {

	private List<Settore> settori;

	@UiField TextBox partitaIva;

	@UiField TextBox ragioneSociale;

	@UiField ListBox stato;

	@UiField WidgetListaIndirizzi listaIndirizziWidget;

	@UiField WidgetListaTelefoni listaTelefoniWidget;

	@UiField WidgetListaEmail listaEmailWidget;

	private static Binder binder = GWT.create(Binder.class);

	public interface Binder extends UiBinder<Widget, WidgetPersonaGiuridica> {
		//
	}

	public WidgetPersonaGiuridica(List<Settore> settori) {
		super();
		this.settori = settori;
		initWidget(binder.createAndBindUi(this));

		for (Stato s : Stato.values()) {
			stato.addItem(s.toString());
		}
	}

	public PersonaGiuridica get() {
		PersonaGiuridica pg = new PersonaGiuridica();
		pg.getGruppi().addAll(SettoriUtils.transform(settori));
		pg.setPartitaIva(partitaIva.getText());
		pg.setRagioneSociale(ragioneSociale.getText());
		pg.setStato(Stato.valueOf(stato.getSelectedItemText()));
		pg.getIndirizzi().addAll(listaIndirizziWidget.get());
		pg.getTelefoni().addAll(listaTelefoniWidget.get());
		pg.getEmail().addAll(listaEmailWidget.get());
		return pg;
	}

	public void set(PersonaGiuridica personaGiuridica) {
		partitaIva.setEnabled(false);
		partitaIva.setStylePrimaryName("disabilitato");
		partitaIva.setText(personaGiuridica.getPartitaIva());

		ragioneSociale.setText(personaGiuridica.getRagioneSociale());
		if (Stato.DISATTIVA.equals(personaGiuridica.getStato())) {
			ragioneSociale.setEnabled(false);
			ragioneSociale.setStylePrimaryName("disabilitato");
		} else {
			ragioneSociale.setEnabled(true);
			ragioneSociale.removeStyleName("disabilitato");
		}

		for (int i = 0; i < stato.getItemCount(); i++) {
			stato.setItemSelected(i, stato.getItemText(i).equals(personaGiuridica.getStato().toString()));
		}

		listaIndirizziWidget.set(personaGiuridica.getIndirizzi(), personaGiuridica.getStato());
		listaTelefoniWidget.set(personaGiuridica.getTelefoni(), personaGiuridica.getStato());
		listaEmailWidget.set(personaGiuridica.getEmail(), personaGiuridica.getStato());

	}

	public void reset() {
		partitaIva.setValue("");
		ragioneSociale.setValue("");
		stato.setItemSelected(0, true);
		listaIndirizziWidget.set(new ArrayList<Indirizzo>(), Stato.ATTIVA);
		listaTelefoniWidget.set(new ArrayList<Telefono>(), Stato.ATTIVA);
		listaEmailWidget.set(new ArrayList<Email>(), Stato.ATTIVA);
	}

}
