package it.eng.portlet.consolepec.gwt.client.widget.configurazioni;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaRuolo;
import it.eng.cobo.consolepec.commons.configurazioni.Azione;
import it.eng.cobo.consolepec.commons.configurazioni.abilitazione.ModificaRuoloAbilitazione;
import it.eng.portlet.consolepec.gwt.client.handler.configurazioni.ConfigurazioniHandler;
import it.eng.portlet.consolepec.gwt.client.handler.profilazione.ProfilazioneUtenteHandler;
import it.eng.portlet.consolepec.gwt.client.widget.ListaCampiWidget;
import it.eng.portlet.consolepec.gwt.shared.dto.Campo.TipoWidget;

/**
 *
 * @author biagiot
 *
 */
public class AbilitazioneSupervisoreWidget extends AbilitazioneWidget<ModificaRuoloAbilitazione> {

	interface AbilitazioneSupervisoreWidgetBinder extends UiBinder<Widget, AbilitazioneSupervisoreWidget> {}

	private static AbilitazioneSupervisoreWidgetBinder uiBinder = GWT.create(AbilitazioneSupervisoreWidgetBinder.class);

	@UiField
	HTMLPanel abilitazioniSupPanel;

	private ConfigurazioniHandler configurazioniHandler;
	private ProfilazioneUtenteHandler profilazioneUtenteHandler;

	private CampiAbilitazioniSupervisoreWidget campiWidget;
	private String ruoloSupervisore;

	public AbilitazioneSupervisoreWidget() {
		super();
		initWidget(uiBinder.createAndBindUi(this));
	}

	public void render(ConfigurazioniHandler configurazioniHandler, ProfilazioneUtenteHandler profilazioneUtenteHandler, List<ModificaRuoloAbilitazione> abilitazioni, String ruoloSupervisore) {
		super.render(abilitazioni, profilazioneUtenteHandler);

		this.profilazioneUtenteHandler = profilazioneUtenteHandler;
		this.configurazioniHandler = configurazioniHandler;
		this.ruoloSupervisore = ruoloSupervisore;

		this.abilitazioniSupPanel.clear();
		this.campiWidget = new CampiAbilitazioniSupervisoreWidget(abilitazioni != null ? abilitazioni.size() + 50 : 200);
		this.campiWidget.render(true, true);
		if (abilitazioni != null && !abilitazioni.isEmpty()) {
			for (ModificaRuoloAbilitazione fa : abilitazioni) {
				this.campiWidget.add(fa);
			}
		}
		this.abilitazioniSupPanel.add(campiWidget);

		this.campiWidget.setAggiungiCommand(new AggiungiCampoCommand());
		this.campiWidget.setEliminaCommand(new EliminaCampoCommand());
	}

	public void clear() {
		if (campiWidget != null) {
			campiWidget.getValori().clear();
		}
	}

	public class CampiAbilitazioniSupervisoreWidget extends ListaCampiWidget<ModificaRuoloAbilitazione> {

		public CampiAbilitazioniSupervisoreWidget(Integer limit) {
			super(limit);
		}

		@Override
		protected void definisciCampi() {

			List<String> gruppiList = new ArrayList<String>();
			for (AnagraficaRuolo af : configurazioniHandler.getAnagraficheRuoli()) {
				gruppiList.add(af.getEtichetta());
			}

			String[] gruppi = new String[gruppiList.size()];
			gruppi = gruppiList.toArray(gruppi);

			creaCampo("ruoloSubordinato", "Gruppo associato", TipoWidget.LISTBOX, 0).obbligatorio(true).lista(gruppi);
		}

		@Override
		protected ModificaRuoloAbilitazione converti(Object[] riga) {

			ModificaRuoloAbilitazione modificaRuoloAbilitazione = null;

			if (riga != null && riga[0] != null) {
				String etichettaRuolo = (String) riga[0];
				AnagraficaRuolo ar = configurazioniHandler.getAnagraficaRuoloByEtichetta(etichettaRuolo);

				if (ar != null) {
					String ldap = ar.getRuolo();
					modificaRuoloAbilitazione = new ModificaRuoloAbilitazione(ldap);
				}
			}

			return modificaRuoloAbilitazione;
		}

		@Override
		protected Object[] converti(ModificaRuoloAbilitazione riga) {

			if (riga != null) {
				AnagraficaRuolo ar = configurazioniHandler.getAnagraficaRuolo(riga.getRuolo());
				if (ar != null) {
					return new Object[] { ar.getEtichetta() };
				}
			}

			return null;
		}

		@Override
		protected boolean validaInserimento(ModificaRuoloAbilitazione riga, List<String> errori) {

			if (riga == null) {
				errori.add("Abilitazione non valida");
				return false;
			}

			if (riga.getRuolo() == null || riga.getRuolo().trim().isEmpty() || configurazioniHandler.getAnagraficaRuolo(riga.getRuolo()) == null) {
				errori.add("Ruolo non valido");
				return false;
			}

			if (getValori().contains(riga)) {
				errori.add("L'abilitazione esiste già");
				return false;
			}

			if (ruoloSupervisore != null && configurazioniHandler.isDipendenzaCircolare(ruoloSupervisore, riga.getRuolo(), ModificaRuoloAbilitazione.class)) {
				errori.add("Il ruolo selezionato non può essere subordinato poichè supervisore di " + ruoloSupervisore);
				return false;
			}

			return true;
		}
	}

	public class AggiungiCampoCommand extends AggiungiAbilitazioneCommand {

		@Override
		public void execute(ModificaRuoloAbilitazione t) {

			Azione azione = new Azione(profilazioneUtenteHandler.getDatiUtente().getUsername(), new Date(), "aggiunta abilitazione superutente. Ruolo subordinato: " + t.getRuolo());

			campiWidget.add(t);
			getAzioni().add(azione);
		}

	}

	public class EliminaCampoCommand extends EliminaAbilitazioneCommand {

		@Override
		public void execute(ModificaRuoloAbilitazione t) {
			Azione azione = new Azione(profilazioneUtenteHandler.getDatiUtente().getUsername(), new Date(), "rimozione abilitazione superutente. Ruolo subordinato: " + t.getRuolo());

			campiWidget.remove(t);
			getAzioni().add(azione);

		}

	}
}
