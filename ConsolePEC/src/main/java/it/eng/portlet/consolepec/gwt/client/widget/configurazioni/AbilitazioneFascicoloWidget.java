package it.eng.portlet.consolepec.gwt.client.widget.configurazioni;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaFascicolo;
import it.eng.cobo.consolepec.commons.configurazioni.Azione;
import it.eng.cobo.consolepec.commons.configurazioni.abilitazione.CreazioneFascicoloAbilitazione;
import it.eng.cobo.consolepec.commons.configurazioni.abilitazione.FascicoloAbilitazione;
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
public class AbilitazioneFascicoloWidget extends AbilitazioneWidget<FascicoloAbilitazione> {

	interface AbilitazioniFascicoloWidgetBinder extends UiBinder<Widget, AbilitazioneFascicoloWidget> {}

	private static AbilitazioniFascicoloWidgetBinder uiBinder = GWT.create(AbilitazioniFascicoloWidgetBinder.class);

	@UiField
	HTMLPanel abilitazioneFascPanel;

	private ConfigurazioniHandler configurazioniHandler;
	private ProfilazioneUtenteHandler profilazioneUtenteHandler;

	private CampiAbilitazioniFascicoloWidget campiWidget;

	public AbilitazioneFascicoloWidget() {
		super();
		initWidget(uiBinder.createAndBindUi(this));
	}

	public void render(ConfigurazioniHandler configurazioniHandler, ProfilazioneUtenteHandler profilazioneUtenteHandler, List<FascicoloAbilitazione> abilitazioni) {
		super.render(abilitazioni, profilazioneUtenteHandler);

		this.configurazioniHandler = configurazioniHandler;
		this.profilazioneUtenteHandler = profilazioneUtenteHandler;

		this.abilitazioneFascPanel.clear();
		this.campiWidget = new CampiAbilitazioniFascicoloWidget(200);
		this.campiWidget.render(true, true);
		if (abilitazioni != null && !abilitazioni.isEmpty()) {
			for (FascicoloAbilitazione fa : abilitazioni) {
				if (fa instanceof CreazioneFascicoloAbilitazione)
					this.campiWidget.add(fa);
			}
		}
		this.abilitazioneFascPanel.add(campiWidget);

		this.campiWidget.setAggiungiCommand(new AggiungiCampoCommand());
		this.campiWidget.setEliminaCommand(new EliminaCampoCommand());
	}

	public void clear() {
		if (campiWidget != null) {
			campiWidget.getValori().clear();
		}
	}

	public static enum TipologiaFascicoloAbilitazione {
		MODIFICA("Modifica"), CREAZIONE("Creazione");

		@Getter
		private String descrizione;

		TipologiaFascicoloAbilitazione(String descrizione) {
			this.descrizione = descrizione;
		}

		public static TipologiaFascicoloAbilitazione from(String descrizione) {
			for (TipologiaFascicoloAbilitazione t : TipologiaFascicoloAbilitazione.values()) {
				if (t.getDescrizione().equals(descrizione))
					return t;
			}

			return null;
		}
	}

	public class CampiAbilitazioniFascicoloWidget extends ListaCampiWidget<FascicoloAbilitazione> {

		public CampiAbilitazioniFascicoloWidget(Integer limit) {
			super(limit);
		}

		@Override
		protected void definisciCampi() {

			List<String> tipiFascicoli = new ArrayList<String>();
			for (AnagraficaFascicolo af : configurazioniHandler.getAnagraficheFascicoli(false)) {
				tipiFascicoli.add(af.getEtichettaTipologia());
			}

			String[] tipiFascArr = new String[tipiFascicoli.size()];
			tipiFascArr = tipiFascicoli.toArray(tipiFascArr);

			creaCampo("tipoAbilitazioneFascicolo", "Tipologia abilitazione", TipoWidget.LISTBOX, 0).obbligatorio(true).lista(
					new String[] { TipologiaFascicoloAbilitazione.CREAZIONE.getDescrizione() });
			creaCampo("tipoFascicolo", "Tipologia fascicolo", TipoWidget.LISTBOX, 1).obbligatorio(true).lista(tipiFascArr);
		}

		@Override
		protected FascicoloAbilitazione converti(Object[] riga) {

			FascicoloAbilitazione fascicoloAbilitazione = null;

			if (riga != null && riga[0] != null && riga[1] != null) {

				String tipologiaAbilitazione = (String) riga[0];
				String etichettaTipologia = (String) riga[1];
				String tipologiaFascicolo = configurazioniHandler.getAnagraficaFascicoloByEtichetta(etichettaTipologia).getNomeTipologia();
				TipologiaFascicoloAbilitazione t = TipologiaFascicoloAbilitazione.from(tipologiaAbilitazione);

				if (t != null) {
					switch (t) {
					case CREAZIONE:
						fascicoloAbilitazione = new CreazioneFascicoloAbilitazione(tipologiaFascicolo);
						break;

					default:
						break;
					}
				}
			}

			return fascicoloAbilitazione;
		}

		@Override
		protected Object[] converti(FascicoloAbilitazione riga) {
			String tipologiaFascicolo = "";
			String tipologiaAbilitazione = "";

			if (riga != null && riga instanceof CreazioneFascicoloAbilitazione) {
				AnagraficaFascicolo af = configurazioniHandler.getAnagraficaFascicolo(riga.getTipo());

				tipologiaAbilitazione = TipologiaFascicoloAbilitazione.CREAZIONE.getDescrizione();

				if (af != null) {
					tipologiaFascicolo = af.getEtichettaTipologia();
					return new Object[] { tipologiaAbilitazione, tipologiaFascicolo };
				}
			}

			return null;
		}

		@Override
		protected boolean validaInserimento(FascicoloAbilitazione riga, List<String> errori) {

			if (riga == null) {
				errori.add("Abilitazione non valida");
				return false;
			}

			if (riga.getTipo() == null || riga.getTipo().trim().isEmpty() || configurazioniHandler.getAnagraficaFascicolo(riga.getTipo()) == null) {
				errori.add("Tipo fascicolo non valido");
				return false;
			}

			if (getValori().contains(riga)) {
				errori.add("L'abilitazione esiste già");
				return false;
			}

			return true;
		}
	}

	public class AggiungiCampoCommand extends AggiungiAbilitazioneCommand {

		@Override
		public void execute(FascicoloAbilitazione t) {
			campiWidget.add(t);
			getAzioni().add(new Azione(profilazioneUtenteHandler.getDatiUtente().getUsername(), new Date(), "aggiunta abilitazione per creazione fascicolo di tipo " + t.getTipo()));
		}

	}

	public class EliminaCampoCommand extends EliminaAbilitazioneCommand {

		@Override
		public void execute(FascicoloAbilitazione t) {
			campiWidget.remove(t);
			getAzioni().add(new Azione(profilazioneUtenteHandler.getDatiUtente().getUsername(), new Date(), "rimozione abilitazione per creazione fascicolo di tipo " + t.getTipo()));
		}

	}
}
