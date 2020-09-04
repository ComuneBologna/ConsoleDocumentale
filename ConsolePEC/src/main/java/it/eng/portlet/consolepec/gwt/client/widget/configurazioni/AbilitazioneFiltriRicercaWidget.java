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

import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaFascicolo;
import it.eng.cobo.consolepec.commons.configurazioni.Azione;
import it.eng.cobo.consolepec.commons.configurazioni.abilitazione.FiltroDatoAggiuntivoAbilitazione;
import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.DatoAggiuntivo;
import it.eng.portlet.consolepec.gwt.client.handler.configurazioni.ConfigurazioniHandler;
import it.eng.portlet.consolepec.gwt.client.handler.profilazione.ProfilazioneUtenteHandler;
import it.eng.portlet.consolepec.gwt.client.presenter.Command;
import it.eng.portlet.consolepec.gwt.client.widget.ListaCampiWidget;
import it.eng.portlet.consolepec.gwt.shared.dto.Campo.TipoWidget;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 *
 * @author biagiot
 *
 */
public class AbilitazioneFiltriRicercaWidget extends Composite {

	interface AbilitazioneFiltriRicercaWidgetBinder extends UiBinder<Widget, AbilitazioneFiltriRicercaWidget> {}

	private static AbilitazioneFiltriRicercaWidgetBinder uiBinder = GWT.create(AbilitazioneFiltriRicercaWidgetBinder.class);

	@UiField
	HTMLPanel abilitazioneFiltriRicercaPanel;

	private ConfigurazioniHandler configurazioniHandler;
	private ProfilazioneUtenteHandler profilazioneUtenteHandler;
	private CampiAbilitazioneFiltriRicercaWidget campiWidget;

	private List<FiltroSingolo> filtriDaAggiungere = new ArrayList<FiltroSingolo>();
	private List<FiltroSingolo> filtriDaRimuovere = new ArrayList<FiltroSingolo>();

	@Getter
	private List<Azione> azioni = new ArrayList<Azione>();

	private List<DatoAggiuntivo> filtriOrigin = new ArrayList<>();

	public AbilitazioneFiltriRicercaWidget() {
		super();
		initWidget(uiBinder.createAndBindUi(this));
	}

	public void render(ConfigurazioniHandler configurazioniHandler, ProfilazioneUtenteHandler profilazioneUtenteHandler, List<FiltroDatoAggiuntivoAbilitazione> abilitazioni, String selectedItem,
			String[] etichetteDatiAggiuntivi) {

		this.configurazioniHandler = configurazioniHandler;
		this.profilazioneUtenteHandler = profilazioneUtenteHandler;

		this.azioni.clear();
		this.filtriDaAggiungere.clear();
		this.filtriDaRimuovere.clear();

		this.abilitazioneFiltriRicercaPanel.clear();
		this.campiWidget = new CampiAbilitazioneFiltriRicercaWidget(selectedItem, etichetteDatiAggiuntivi, 100);
		this.campiWidget.render(true, true);
		if (abilitazioni != null && !abilitazioni.isEmpty()) {
			for (FiltroSingolo fs : toFiltriSingoli(abilitazioni)) {
				this.campiWidget.add(fs);
				filtriDaAggiungere.add(fs);
			}
		}
		this.abilitazioneFiltriRicercaPanel.add(campiWidget);

		this.campiWidget.setAggiungiCommand(new AggiungiCampoCommand());
		this.campiWidget.setEliminaCommand(new EliminaCampoCommand());
	}

	private class FascicoloChangeCommand implements Command<Void, String> {

		@Override
		public Void exe(String t) {

			String[] nomiDatiAggiuntivi = null;
			String selectedItem = "";

			if (t != null) {
				AnagraficaFascicolo af = configurazioniHandler.getAnagraficaFascicoloByEtichetta(t);
				if (af != null && af.getDatiAggiuntivi() != null) {
					selectedItem = t;
					nomiDatiAggiuntivi = new String[af.getDatiAggiuntivi().size()];

					int i = 0;
					for (DatoAggiuntivo dag : af.getDatiAggiuntivi()) {
						nomiDatiAggiuntivi[i] = dag.getDescrizione();
						i++;
					}
				}
			}

			if (nomiDatiAggiuntivi == null) {
				nomiDatiAggiuntivi = new String[] { "" };
			}

			render(configurazioniHandler, profilazioneUtenteHandler, getAbilitazioniDaAggiungere(), selectedItem, nomiDatiAggiuntivi);
			return null;
		}
	}

	public List<FiltroDatoAggiuntivoAbilitazione> getAbilitazioniDaAggiungere() {
		return toAbilitazioni(filtriDaAggiungere);
	}

	public List<FiltroDatoAggiuntivoAbilitazione> getAbilitazioniDaRimuovere() {
		return toAbilitazioni(filtriDaRimuovere);
	}

	public class CampiAbilitazioneFiltriRicercaWidget extends ListaCampiWidget<FiltroSingolo> {

		private String[] etichetteDatiAggiuntivi;
		private String selectedItem;

		public CampiAbilitazioneFiltriRicercaWidget(String selectedItem, String[] etichetteDatiAggiuntivi, Integer limit) {
			super(limit);
			this.selectedItem = selectedItem;
			this.etichetteDatiAggiuntivi = etichetteDatiAggiuntivi;
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

			creaCampo("tipoFascicoloMetadati", "Fascicolo", TipoWidget.LISTBOX, 0).obbligatorio(true).lista(tipiFascArr).addOnChangeCommand(new FascicoloChangeCommand()).setSelectedValue(
					selectedItem);
			creaCampo("metadatiDatiAggiuntivi", "Dato aggiuntivo", TipoWidget.LISTBOX, 0).obbligatorio(true).lista(etichetteDatiAggiuntivi);
		}

		@Override
		protected FiltroSingolo converti(Object[] riga) {

			FiltroSingolo result = null;

			if (riga != null && riga[0] != null && riga[1] != null) {
				AnagraficaFascicolo af = configurazioniHandler.getAnagraficaFascicoloByEtichetta((String) riga[0]);
				DatoAggiuntivo dag = null;
				if (af != null && (dag = getDatoAggiuntivo(af, (String) riga[1])) != null) {
					result = new FiltroSingolo((String) riga[0], dag.getNome(), (String) riga[1]);

				}
			}

			return result;
		}

		@Override
		protected Object[] converti(FiltroSingolo riga) {

			String riga1 = null;
			String riga2 = null;

			if (riga != null) {
				riga1 = riga.getEtichettaFascicolo();
				riga2 = riga.getDescrizioneDatoAggiuntivo();
				return new Object[] { riga1, riga2 };
			}

			return null;
		}

		@Override
		protected boolean validaInserimento(FiltroSingolo riga, List<String> errori) {

			if (riga == null) {
				errori.add("Dato non valido");
				return false;
			}

			if (riga.getDescrizioneDatoAggiuntivo() == null || riga.getDescrizioneDatoAggiuntivo().isEmpty()) {
				errori.add("Dato aggiuntivo non valido");
				return false;
			}

			if (riga.getEtichettaFascicolo() == null || riga.getEtichettaFascicolo().trim().isEmpty()
					|| configurazioniHandler.getAnagraficaFascicoloByEtichetta(riga.getEtichettaFascicolo()) == null) {
				errori.add("Fascicolo non valido");
				return false;
			}

			for (FiltroSingolo fs : getValori()) {
				if (fs.getDescrizioneDatoAggiuntivo().equalsIgnoreCase(riga.getDescrizioneDatoAggiuntivo())) {
					errori.add("Filtro con id " + riga.getIdDatoAggiuntivo() + " e descrizione " + riga.getDescrizioneDatoAggiuntivo() + " già inserito");
					return false;
				}
			}

			if (getValori().contains(riga)) {
				errori.add("Filtro già inserito");
				return false;
			}

			return true;
		}
	}

	@Data
	private static class FiltroSingolo {
		private String etichettaFascicolo;
		private String idDatoAggiuntivo;
		private String descrizioneDatoAggiuntivo;

		public FiltroSingolo(String etichettaFascicolo, String idDatoAggiuntivo, String descrizioneDatoAggiuntivo) {
			this.etichettaFascicolo = etichettaFascicolo;
			this.idDatoAggiuntivo = idDatoAggiuntivo;
			this.descrizioneDatoAggiuntivo = descrizioneDatoAggiuntivo;
		}

		@EqualsAndHashCode.Exclude
		private String usernameCreazione;
		@EqualsAndHashCode.Exclude
		private Date dataCreazione;
	}

	private class AggiungiCampoCommand implements Command<Void, FiltroSingolo> {

		@Override
		public Void exe(FiltroSingolo t) {
			campiWidget.add(t);

			Date date = new Date();
			String usernameUtente = profilazioneUtenteHandler.getDatiUtente().getUsername();

			t.setDataCreazione(date);
			t.setUsernameCreazione(usernameUtente);

			if (!filtriDaAggiungere.contains(t)) {
				filtriDaAggiungere.add(t);
				azioni.add(new Azione(usernameUtente, date,
						"aggiunta abilitazione filtro di ricerca per dato aggiuntivo " + t.getDescrizioneDatoAggiuntivo() + " del fascicolo " + t.getEtichettaFascicolo()));
			}

			return null;
		}

	}

	private class EliminaCampoCommand implements Command<Void, FiltroSingolo> {

		@Override
		public Void exe(FiltroSingolo t) {
			campiWidget.remove(t);

			Date date = new Date();
			String usernameUtente = profilazioneUtenteHandler.getDatiUtente().getUsername();

			if (filtriDaAggiungere.contains(t)) {
				filtriDaAggiungere.remove(t);
			}

			if (!filtriDaRimuovere.contains(t)) {
				filtriDaRimuovere.add(t);
				azioni.add(new Azione(usernameUtente, date,
						"rimozione abilitazione filtro di ricerca per dato aggiuntivo " + t.getDescrizioneDatoAggiuntivo() + " del fascicolo " + t.getEtichettaFascicolo()));

			}

			return null;

		}
	}

	private List<FiltroDatoAggiuntivoAbilitazione> toAbilitazioni(List<FiltroSingolo> filtri) {
		List<FiltroDatoAggiuntivoAbilitazione> res = new ArrayList<FiltroDatoAggiuntivoAbilitazione>();

		for (FiltroSingolo f : filtri) {
			AnagraficaFascicolo af = configurazioniHandler.getAnagraficaFascicoloByEtichetta(f.getEtichettaFascicolo());

			DatoAggiuntivo dag = null;
			if ((af != null) && ((dag = getDatoAggiuntivo(af, f.getDescrizioneDatoAggiuntivo())) != null)) {
				FiltroDatoAggiuntivoAbilitazione ab = new FiltroDatoAggiuntivoAbilitazione(dag, af.getNomeTipologia());

				if (!res.contains(ab)) {
					res.add(ab);
				}
			}
		}

		return res;
	}

	private DatoAggiuntivo getDatoAggiuntivo(AnagraficaFascicolo af, String descrizioneDatoAggiuntivo) {

		for (DatoAggiuntivo dag : af.getDatiAggiuntivi()) {
			if (dag.getDescrizione().equals(descrizioneDatoAggiuntivo)) {
				return dag;
			}
		}

		for (DatoAggiuntivo dag : filtriOrigin) {
			if (dag.getDescrizione().equals(descrizioneDatoAggiuntivo)) {
				return dag;
			}
		}

		return null;
	}

	private List<FiltroSingolo> toFiltriSingoli(List<FiltroDatoAggiuntivoAbilitazione> filtroDatoAgg) {
		filtriOrigin.clear();

		List<FiltroSingolo> res = new ArrayList<FiltroSingolo>();

		for (FiltroDatoAggiuntivoAbilitazione f : filtroDatoAgg) {
			filtriOrigin.add(f.getFiltroDatoAggiuntivo());

			AnagraficaFascicolo af = configurazioniHandler.getAnagraficaFascicolo(f.getTipoFascicolo());
			if (af != null) {

				FiltroSingolo r = new FiltroSingolo(af.getEtichettaTipologia(), f.getFiltroDatoAggiuntivo().getNome(), f.getFiltroDatoAggiuntivo().getDescrizione());
				r.setUsernameCreazione(f.getUsernameCreazione());
				r.setDataCreazione(f.getDataCreazione());

				if (!res.contains(r)) {
					res.add(r);
				}
			}
		}

		return res;
	}

	public void clear() {
		if (campiWidget != null) {
			campiWidget.getValori().clear();
		}

		filtriOrigin.clear();
	}
}
