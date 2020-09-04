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
import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaRuolo;
import it.eng.cobo.consolepec.commons.configurazioni.Azione;
import it.eng.cobo.consolepec.commons.configurazioni.abilitazione.VisibilitaAbilitazione;
import it.eng.cobo.consolepec.commons.configurazioni.abilitazione.VisibilitaFascicoloAbilitazione;
import it.eng.cobo.consolepec.commons.configurazioni.abilitazione.VisibilitaRuoloAbilitazione;
import it.eng.portlet.consolepec.gwt.client.handler.configurazioni.ConfigurazioniHandler;
import it.eng.portlet.consolepec.gwt.client.handler.profilazione.ProfilazioneUtenteHandler;
import it.eng.portlet.consolepec.gwt.client.operazioni.amministrazione.AmministrazioneAbilitazioniVisibilitaApiClient.TipoAbilitazione;
import it.eng.portlet.consolepec.gwt.client.operazioni.amministrazione.AmministrazioneAbilitazioniVisibilitaApiClient.TipoAccessoAbilitazione;
import it.eng.portlet.consolepec.gwt.client.widget.ListaCampiWidget;
import it.eng.portlet.consolepec.gwt.shared.dto.Campo.TipoWidget;

public class AbilitazioneMatriceVisibilitaWidget extends AbilitazioneWidget<VisibilitaAbilitazione> {

	interface AbilitazioneMatriceVisibilitaWidgetWidgetBinder extends UiBinder<Widget, AbilitazioneMatriceVisibilitaWidget> {/**/}

	private static AbilitazioneMatriceVisibilitaWidgetWidgetBinder uiBinder = GWT.create(AbilitazioneMatriceVisibilitaWidgetWidgetBinder.class);

	@UiField
	HTMLPanel abilitazioniMatrVisPanel;

	private ConfigurazioniHandler configurazioniHandler;
	private ProfilazioneUtenteHandler profilazioneUtenteHandler;
	private CampiAbilitazioniMatriceVisibilitaWidget campiWidget;

	public AbilitazioneMatriceVisibilitaWidget() {
		super();
		initWidget(uiBinder.createAndBindUi(this));
	}

	public void render(ConfigurazioniHandler configurazioniHandler, ProfilazioneUtenteHandler profilazioneUtenteHandler, List<VisibilitaAbilitazione> abilitazioni) {
		super.render(abilitazioni, profilazioneUtenteHandler);

		this.configurazioniHandler = configurazioniHandler;
		this.profilazioneUtenteHandler = profilazioneUtenteHandler;

		this.abilitazioniMatrVisPanel.clear();
		this.campiWidget = new CampiAbilitazioniMatriceVisibilitaWidget(abilitazioni != null ? abilitazioni.size() + 50 : 100);
		this.campiWidget.render(true, true);
		if (abilitazioni != null && !abilitazioni.isEmpty()) {
			for (VisibilitaAbilitazione fa : abilitazioni) {
				if (fa instanceof VisibilitaFascicoloAbilitazione || fa instanceof VisibilitaRuoloAbilitazione)
					this.campiWidget.add(fa);
			}
		}
		this.abilitazioniMatrVisPanel.add(campiWidget);

		this.campiWidget.setAggiungiCommand(new AggiungiCampoCommand());
		this.campiWidget.setEliminaCommand(new EliminaCampoCommand());
	}

	public void clear() {
		if (campiWidget != null) {
			campiWidget.getValori().clear();
		}
	}

	public class CampiAbilitazioniMatriceVisibilitaWidget extends ListaCampiWidget<VisibilitaAbilitazione> {

		public CampiAbilitazioniMatriceVisibilitaWidget(Integer limit) {
			super(limit);
		}

		@Override
		protected void definisciCampi() {
			List<String> tipiFascicoli = new ArrayList<String>();
			tipiFascicoli.add("");
			for (AnagraficaFascicolo af : configurazioniHandler.getAnagraficheFascicoli(false)) {
				tipiFascicoli.add(af.getEtichettaTipologia());
			}
			String[] tipiFascArr = new String[tipiFascicoli.size()];
			tipiFascArr = tipiFascicoli.toArray(tipiFascArr);

			List<String> gruppiList = new ArrayList<String>();
			gruppiList.add("");
			for (AnagraficaRuolo af : configurazioniHandler.getAnagraficheRuoli()) {
				gruppiList.add(af.getEtichetta());
			}
			String[] gruppi = new String[gruppiList.size()];
			gruppi = gruppiList.toArray(gruppi);

			creaCampo("tipoAbilitazioneMatriceVisibilita", "Abilitazione", TipoWidget.LISTBOX, 0).obbligatorio(true).lista(new String[] { TipoAccessoAbilitazione.Lettura.toString() });
			creaCampo("tipoMatriceVisibilita", "Tipo", TipoWidget.LISTBOX, 1).obbligatorio(true).lista(new String[] { TipoAbilitazione.Fascicolo.toString(), TipoAbilitazione.Gruppo.toString() });
			creaCampo("tipoFascicoloMatriceVisibilita", "Fascicolo", TipoWidget.LISTBOX, 2).obbligatorio(false).lista(tipiFascArr);
			creaCampo("tipoGruppoMatriceVisibilita", "Gruppo", TipoWidget.LISTBOX, 3).obbligatorio(false).lista(gruppi);
		}

		@Override
		protected VisibilitaAbilitazione converti(Object[] riga) {

			VisibilitaAbilitazione abilitazione = null;

			if (riga != null && riga[0] != null && riga[1] != null) {
				String tipoAccesso = (String) riga[0];
				String tipo = (String) riga[1];
				String fascicolo = (String) riga[2];
				String gruppo = (String) riga[3];

				if (tipoAccesso != null && TipoAccessoAbilitazione.Lettura.equals(TipoAccessoAbilitazione.from(tipoAccesso))) {

					if (tipo != null) {

						if (TipoAbilitazione.Fascicolo.equals(TipoAbilitazione.from(tipo))) {

							AnagraficaFascicolo af = configurazioniHandler.getAnagraficaFascicoloByEtichetta(fascicolo);

							if (af != null) {
								abilitazione = new VisibilitaFascicoloAbilitazione(af.getNomeTipologia());
							}

						} else if (TipoAbilitazione.Gruppo.equals(TipoAbilitazione.from(tipo))) {

							AnagraficaRuolo ar = configurazioniHandler.getAnagraficaRuoloByEtichetta(gruppo);

							if (ar != null) {
								abilitazione = new VisibilitaRuoloAbilitazione(ar.getRuolo());
							}
						}
					}

				}
			}

			return abilitazione;
		}

		@Override
		protected Object[] converti(VisibilitaAbilitazione riga) {

			String tipoAbilitazioneMatriceVisibilita = TipoAccessoAbilitazione.Lettura.toString();
			String tipoMatriceVisibilita = "";
			String tipoFascicoloMatriceVisibilita = "";
			String tipoGruppoMatriceVisibilita = "";

			if (riga != null) {
				if (riga instanceof VisibilitaFascicoloAbilitazione) {
					tipoMatriceVisibilita = TipoAbilitazione.Fascicolo.toString();

					AnagraficaFascicolo af = configurazioniHandler.getAnagraficaFascicolo(((VisibilitaFascicoloAbilitazione) riga).getTipo());

					if (af != null) {
						tipoFascicoloMatriceVisibilita = af.getEtichettaTipologia();
						return new Object[] { tipoAbilitazioneMatriceVisibilita, tipoMatriceVisibilita, tipoFascicoloMatriceVisibilita, tipoGruppoMatriceVisibilita };
					}

				} else if (riga instanceof VisibilitaRuoloAbilitazione) {
					tipoMatriceVisibilita = TipoAbilitazione.Gruppo.toString();

					AnagraficaRuolo ar = configurazioniHandler.getAnagraficaRuolo(((VisibilitaRuoloAbilitazione) riga).getRuolo());

					if (ar != null) {
						tipoGruppoMatriceVisibilita = ar.getEtichetta();
						return new Object[] { tipoAbilitazioneMatriceVisibilita, tipoMatriceVisibilita, tipoFascicoloMatriceVisibilita, tipoGruppoMatriceVisibilita };
					}
				}
			}

			return null;
		}

		@Override
		protected boolean validaInserimento(VisibilitaAbilitazione riga, List<String> errori) {

			if (riga == null) {
				errori.add("Riga non valida");
				return false;
			}

			if (riga instanceof VisibilitaRuoloAbilitazione) {
				String ruolo = ((VisibilitaRuoloAbilitazione) riga).getRuolo();
				AnagraficaRuolo ar = configurazioniHandler.getAnagraficaRuolo(ruolo);

				if (ar == null) {
					errori.add("Gruppo non valido");
					return false;
				}

				for (VisibilitaAbilitazione a : abilitazioniDaAggiungere) {
					if (a instanceof VisibilitaRuoloAbilitazione) {
						String r = ((VisibilitaRuoloAbilitazione) a).getRuolo();
						if (ruolo.equals(r)) {
							errori.add("Esiste già una riga con lo stesso gruppo");
							return false;
						}
					}
				}

			} else if (riga instanceof VisibilitaFascicoloAbilitazione) {
				String fascicolo = ((VisibilitaFascicoloAbilitazione) riga).getTipo();
				AnagraficaFascicolo af = configurazioniHandler.getAnagraficaFascicolo(fascicolo);

				if (af == null) {
					errori.add("Fascicolo non valido");
					return false;
				}

				for (VisibilitaAbilitazione a : abilitazioniDaAggiungere) {
					if (a instanceof VisibilitaFascicoloAbilitazione) {
						String f = ((VisibilitaFascicoloAbilitazione) a).getTipo();
						if (fascicolo.equals(f)) {
							errori.add("Esiste già una riga con la stessa tipologia di fascicolo");
							return false;
						}
					}
				}

			} else {
				errori.add("Riga non valida");
				return false;
			}

			return true;
		}

	}

	public class AggiungiCampoCommand extends AggiungiAbilitazioneCommand {

		@Override
		public void execute(VisibilitaAbilitazione t) {

			Azione azione = null;
			Date date = new Date();

			if (t instanceof VisibilitaFascicoloAbilitazione) {

				azione = new Azione(profilazioneUtenteHandler.getDatiUtente().getUsername(), date,
						"aggiunta abilitazione di matrice di visibilità per fascicolo di tipo " + ((VisibilitaFascicoloAbilitazione) t).getTipo());

			} else if (t instanceof VisibilitaRuoloAbilitazione) {
				azione = new Azione(profilazioneUtenteHandler.getDatiUtente().getUsername(), date,
						"aggiunta abilitazione di matrice di visibilità per ruolo " + ((VisibilitaRuoloAbilitazione) t).getRuolo());
			}

			campiWidget.add(t);

			if (azione != null) {
				getAzioni().add(azione);
			}
		}

	}

	public class EliminaCampoCommand extends EliminaAbilitazioneCommand {

		@Override
		public void execute(VisibilitaAbilitazione t) {
			Azione azione = null;
			Date date = new Date();

			if (t instanceof VisibilitaFascicoloAbilitazione) {

				azione = new Azione(profilazioneUtenteHandler.getDatiUtente().getUsername(), date,
						"rimozione abilitazione di matrice di visibilità per fascicolo di tipo " + ((VisibilitaFascicoloAbilitazione) t).getTipo());

			} else if (t instanceof VisibilitaRuoloAbilitazione) {
				azione = new Azione(profilazioneUtenteHandler.getDatiUtente().getUsername(), date,
						"rimozione abilitazione di matrice di visibilità per ruolo " + ((VisibilitaRuoloAbilitazione) t).getRuolo());
			}

			campiWidget.remove(t);
			if (azione != null) {
				getAzioni().add(azione);
			}
		}

	}
}
