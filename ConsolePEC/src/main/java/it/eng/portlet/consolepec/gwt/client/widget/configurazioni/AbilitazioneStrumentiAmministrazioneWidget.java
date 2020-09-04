package it.eng.portlet.consolepec.gwt.client.widget.configurazioni;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaComunicazione;
import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaModello;
import it.eng.cobo.consolepec.commons.configurazioni.Azione;
import it.eng.cobo.consolepec.commons.configurazioni.abilitazione.Abilitazione;
import it.eng.cobo.consolepec.commons.configurazioni.abilitazione.CreazioneComunicazioneAbilitazione;
import it.eng.cobo.consolepec.commons.configurazioni.abilitazione.CreazioneModelloAbilitazione;
import it.eng.portlet.consolepec.gwt.client.handler.configurazioni.ConfigurazioniHandler;
import it.eng.portlet.consolepec.gwt.client.handler.profilazione.ProfilazioneUtenteHandler;
import it.eng.portlet.consolepec.gwt.client.widget.ListaCampiWidget;
import it.eng.portlet.consolepec.gwt.shared.dto.Campo.TipoWidget;
import lombok.Getter;

/**
 *
 * @author biagiot
 *
 */
public class AbilitazioneStrumentiAmministrazioneWidget extends Composite {

	interface AbilitazioneStrumentiAmministrazioneWidgetBinder extends UiBinder<Widget, AbilitazioneStrumentiAmministrazioneWidget> {}

	private static AbilitazioneStrumentiAmministrazioneWidgetBinder uiBinder = GWT.create(AbilitazioneStrumentiAmministrazioneWidgetBinder.class);

	@UiField
	HTMLPanel strumentiAmmPanel;

	private ConfigurazioniHandler configurazioniHandler;
	private CampiStrumentiAmministrazioneWidget campiWidget;
	private ProfilazioneUtenteHandler profilazioneUtenteHandler;

	@Getter
	private List<Azione> azioni = new ArrayList<Azione>();

	private List<Abilitazione> abilitazioniDaAggiungere = new ArrayList<Abilitazione>();
	private List<Abilitazione> abilitazioniDaRimuovere = new ArrayList<Abilitazione>();

	public AbilitazioneStrumentiAmministrazioneWidget() {
		super();
		initWidget(uiBinder.createAndBindUi(this));

	}

	public void render(ConfigurazioniHandler configurazioniHandler, ProfilazioneUtenteHandler profilazioneUtenteHandler, List<CreazioneModelloAbilitazione> abilitazioniModello,
			List<CreazioneComunicazioneAbilitazione> abilitazioniComunicazione) {
		this.abilitazioniDaAggiungere = new ArrayList<Abilitazione>();
		this.abilitazioniDaRimuovere = new ArrayList<Abilitazione>();
		this.azioni = new ArrayList<Azione>();

		this.profilazioneUtenteHandler = profilazioneUtenteHandler;
		this.configurazioniHandler = configurazioniHandler;

		this.strumentiAmmPanel.clear();
		this.campiWidget = new CampiStrumentiAmministrazioneWidget(20);
		this.campiWidget.render(true, true);

		if (abilitazioniModello != null && !abilitazioniModello.isEmpty()) {
			for (CreazioneModelloAbilitazione fa : abilitazioniModello) {
				this.abilitazioniDaAggiungere.add(fa);
				this.campiWidget.add(fa);
			}
		}

		if (abilitazioniComunicazione != null && !abilitazioniComunicazione.isEmpty()) {
			for (CreazioneComunicazioneAbilitazione fa : abilitazioniComunicazione) {
				this.abilitazioniDaAggiungere.add(fa);
				this.campiWidget.add(fa);
			}
		}
		this.strumentiAmmPanel.add(campiWidget);

		this.campiWidget.setAggiungiCommand(new AggiungiCampoCommand());
		this.campiWidget.setEliminaCommand(new EliminaCampoCommand());
	}

	public List<Abilitazione> getAbilitazioniDaAggiungere() {
		return abilitazioniDaAggiungere;
	}

	public List<Abilitazione> getAbilitazioniDaRimuovere() {
		return abilitazioniDaRimuovere;
	}

	public void clear() {
		if (campiWidget != null) {
			campiWidget.getValori().clear();
		}
	}

	public static enum TipologiaStrumentiAmministrazioneAbilitazione {
		CREAZIONE("Creazione");

		@Getter
		private String descrizione;

		TipologiaStrumentiAmministrazioneAbilitazione(String descrizione) {
			this.descrizione = descrizione;
		}

		public static TipologiaStrumentiAmministrazioneAbilitazione from(String descrizione) {
			for (TipologiaStrumentiAmministrazioneAbilitazione t : TipologiaStrumentiAmministrazioneAbilitazione.values()) {
				if (t.getDescrizione().equals(descrizione))
					return t;
			}

			return null;
		}
	}

	public class CampiStrumentiAmministrazioneWidget extends ListaCampiWidget<Abilitazione> {

		public CampiStrumentiAmministrazioneWidget(Integer limit) {
			super(limit);
		}

		@Override
		protected void definisciCampi() {

			List<String> gruppiList = new ArrayList<String>();
			for (AnagraficaComunicazione af : configurazioniHandler.getAnagraficheComunicazioni(false)) {
				gruppiList.add(af.getEtichettaTipologia());
			}

			for (AnagraficaModello af : configurazioniHandler.getAnagraficheModelli(false)) {
				gruppiList.add(af.getEtichettaTipologia());
			}

			String[] gruppi = new String[gruppiList.size()];
			gruppi = gruppiList.toArray(gruppi);

			creaCampo("tipoStrumento", "Strumento", TipoWidget.LISTBOX, 0).obbligatorio(true).lista(gruppi);
			creaCampo("tipoAbilitazioneStrumento", "Tipologia abilitazione", TipoWidget.LISTBOX, 1).obbligatorio(true).lista(
					new String[] { TipologiaStrumentiAmministrazioneAbilitazione.CREAZIONE.getDescrizione() });
		}

		@Override
		protected Abilitazione converti(Object[] riga) {

			Abilitazione abilitazione = null;

			if (riga != null && riga[0] != null && riga[1] != null) {

				String tipoAbilitazioneStrumento = (String) riga[1];
				String etichetta = (String) riga[0];

				AnagraficaComunicazione ac = configurazioniHandler.getAnagraficaComunicazioneByEtichetta(etichetta);
				AnagraficaModello am = configurazioniHandler.getAnagraficaModelloByEtichetta(etichetta);

				if (ac != null || am != null) {

					TipologiaStrumentiAmministrazioneAbilitazione t = TipologiaStrumentiAmministrazioneAbilitazione.from(tipoAbilitazioneStrumento);

					if (t != null) {
						switch (t) {
						case CREAZIONE:
							abilitazione = ac != null ? new CreazioneComunicazioneAbilitazione(ac.getNomeTipologia()) : new CreazioneModelloAbilitazione(am.getNomeTipologia());
							break;

						default:
							break;
						}
					}
				}
			}

			return abilitazione;
		}

		@Override
		protected Object[] converti(Abilitazione riga) {

			String tipoAbilitazioneStrumento = null;
			String etichetta = null;

			if (riga != null) {
				if (riga instanceof CreazioneComunicazioneAbilitazione) {
					tipoAbilitazioneStrumento = TipologiaStrumentiAmministrazioneAbilitazione.CREAZIONE.getDescrizione();
					String tipo = ((CreazioneComunicazioneAbilitazione) riga).getTipo();
					AnagraficaComunicazione ac = configurazioniHandler.getAnagraficaComunicazione(tipo);
					if (ac != null) {
						etichetta = ac.getEtichettaTipologia();
						return new Object[] { etichetta, tipoAbilitazioneStrumento };
					}

				} else if (riga instanceof CreazioneModelloAbilitazione) {
					tipoAbilitazioneStrumento = TipologiaStrumentiAmministrazioneAbilitazione.CREAZIONE.getDescrizione();
					String tipo = ((CreazioneModelloAbilitazione) riga).getTipo();
					AnagraficaModello ac = configurazioniHandler.getAnagraficaModello(tipo);
					if (ac != null) {
						etichetta = ac.getEtichettaTipologia();
						return new Object[] { etichetta, tipoAbilitazioneStrumento };
					}

				}
			}

			return null;
		}

		@Override
		protected boolean validaInserimento(Abilitazione riga, List<String> errori) {

			if (riga == null) {
				errori.add("Abilitazione non valida");
				return false;
			}

			if (riga instanceof CreazioneComunicazioneAbilitazione) {

				CreazioneComunicazioneAbilitazione ca = (CreazioneComunicazioneAbilitazione) riga;
				if (ca.getTipo() == null || ca.getTipo().trim().isEmpty()) {
					errori.add("Tipo comunicazione non valida");
					return false;
				}

			} else if (riga instanceof CreazioneModelloAbilitazione) {

				CreazioneModelloAbilitazione ma = (CreazioneModelloAbilitazione) riga;
				if (ma.getTipo() == null || ma.getTipo().trim().isEmpty()) {
					errori.add("Tipo modello non valido");
					return false;
				}

			} else {
				errori.add("Abilitazione non valida");
				return false;
			}

			if (getValori().contains(riga)) {
				errori.add("L'abilitazione esiste già");
				return false;
			}

			return true;
		}
	}

	public class AggiungiCampoCommand implements it.eng.portlet.consolepec.gwt.client.presenter.Command<Void, Abilitazione> {

		@Override
		public Void exe(Abilitazione t) {
			Azione azioneC = null;
			Date date = new Date();
			String usernameUtente = profilazioneUtenteHandler.getDatiUtente().getUsername();
			t.setDataCreazione(date);
			t.setUsernameCreazione(usernameUtente);

			if (t instanceof CreazioneModelloAbilitazione) {
				azioneC = new Azione(usernameUtente, date, "aggiunta abilitazione per creazione modello di tipo: " + ((CreazioneModelloAbilitazione) t).getTipo());

			} else if (t instanceof CreazioneComunicazioneAbilitazione) {
				azioneC = new Azione(usernameUtente, date, "aggiunta abilitazione per creazione comunicazione di tipo: " + ((CreazioneModelloAbilitazione) t).getTipo());
			}

			if (!abilitazioniDaAggiungere.contains(t)) {
				abilitazioniDaAggiungere.add(t);
				if (azioneC != null) {
					azioni.add(azioneC);
				}
			}

			campiWidget.add(t);
			return null;
		}

	}

	public class EliminaCampoCommand implements it.eng.portlet.consolepec.gwt.client.presenter.Command<Void, Abilitazione> {

		@Override
		public Void exe(Abilitazione t) {

			Azione azioneC = null;

			Date date = new Date();
			String usernameUtente = profilazioneUtenteHandler.getDatiUtente().getUsername();

			if (t instanceof CreazioneModelloAbilitazione) {
				azioneC = new Azione(usernameUtente, date, "rimozione abilitazione per creazione modello di tipo: " + ((CreazioneModelloAbilitazione) t).getTipo());

			} else if (t instanceof CreazioneComunicazioneAbilitazione) {
				azioneC = new Azione(usernameUtente, date, "rimozione abilitazione per creazione comunicazione di tipo: " + ((CreazioneModelloAbilitazione) t).getTipo());
			}

			if (abilitazioniDaAggiungere.contains(t)) {
				abilitazioniDaAggiungere.remove(t);
			}

			if (!abilitazioniDaRimuovere.contains(t)) {
				abilitazioniDaRimuovere.add(t);
				if (azioneC != null) {
					azioni.add(azioneC);
				}
			}

			campiWidget.remove(t);
			return null;
		}

	}
}
