package it.eng.portlet.consolepec.gwt.client.widget.configurazioni;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaIngresso;
import it.eng.cobo.consolepec.commons.configurazioni.Azione;
import it.eng.cobo.consolepec.commons.configurazioni.TipologiaPratica;
import it.eng.cobo.consolepec.commons.configurazioni.abilitazione.IngressoAbilitazione;
import it.eng.cobo.consolepec.commons.configurazioni.abilitazione.LetturaIngressoAbilitazione;
import it.eng.cobo.consolepec.commons.configurazioni.properties.ProprietaGenerali.Server;
import it.eng.portlet.consolepec.gwt.client.handler.configurazioni.ConfigurazioniHandler;
import it.eng.portlet.consolepec.gwt.client.handler.profilazione.ProfilazioneUtenteHandler;
import it.eng.portlet.consolepec.gwt.client.widget.ListaCampiWidget;
import it.eng.portlet.consolepec.gwt.shared.dto.Campo.TipoWidget;

/**
 *
 * @author biagiot
 *
 */
public class AbilitazioneIngressiWidget extends AbilitazioneWidget<IngressoAbilitazione> {

	interface AbilitazioneIngressiWidgetBinder extends UiBinder<Widget, AbilitazioneIngressiWidget> {/**/}

	private static AbilitazioneIngressiWidgetBinder uiBinder = GWT.create(AbilitazioneIngressiWidgetBinder.class);

	private final String EPROTOCOLLO_LABEL = " (EProtocollo)";

	@UiField
	HTMLPanel abilitazioneIngressiPnl;

	private CampiAbilitazioniIngressoWidget campiWidget;
	private ConfigurazioniHandler configurazioniHandler;
	private ProfilazioneUtenteHandler profilazioneUtenteHandler;

	public AbilitazioneIngressiWidget() {
		super();
		initWidget(uiBinder.createAndBindUi(this));

	}

	public void render(ConfigurazioniHandler configurazioniHandler, ProfilazioneUtenteHandler profilazioneUtenteHandler, List<IngressoAbilitazione> abilitazioni) {
		super.render(abilitazioni, profilazioneUtenteHandler);

		this.configurazioniHandler = configurazioniHandler;
		this.profilazioneUtenteHandler = profilazioneUtenteHandler;

		this.abilitazioneIngressiPnl.clear();
		this.campiWidget = new CampiAbilitazioniIngressoWidget(100);
		this.campiWidget.render(true, true);
		if (abilitazioni != null && !abilitazioni.isEmpty()) {
			for (IngressoAbilitazione fa : abilitazioni) {
				if (fa instanceof LetturaIngressoAbilitazione)
					this.campiWidget.add(fa);
			}
		}
		this.abilitazioneIngressiPnl.add(campiWidget);

		this.campiWidget.setAggiungiCommand(new AggiungiCampoCommand());
		this.campiWidget.setEliminaCommand(new EliminaCampoCommand());

	}

	public void clear() {
		if (campiWidget != null) {
			campiWidget.getValori().clear();
		}
	}

	public class CampiAbilitazioniIngressoWidget extends ListaCampiWidget<IngressoAbilitazione> {

		public CampiAbilitazioniIngressoWidget(Integer limit) {
			super(limit);
		}

		@Override
		protected void definisciCampi() {

			List<String> tipi = new ArrayList<String>();
			for (Server server : configurazioniHandler.getProprietaGenerali().getServerIngressiAbilitati())
				tipi.add(server.getNome());

			List<String> indirizziEmail = new ArrayList<String>();
			for (AnagraficaIngresso ai : configurazioniHandler.getAnagraficheIngressi(false)) {

				String indirizzo = ai.getIndirizzo();

				if (ai.getNomeTipologia().equals(TipologiaPratica.EMAIL_EPROTOCOLLO.getNomeTipologia())) {
					indirizzo = indirizzo + EPROTOCOLLO_LABEL;
				}

				indirizziEmail.add(indirizzo);
			}

			String[] tipiArr = new String[tipi.size()];
			tipiArr = tipi.toArray(tipiArr);

			String[] indirizzi = new String[indirizziEmail.size()];
			indirizzi = indirizziEmail.toArray(indirizzi);

			creaCampo("ingresso", "Ingresso", TipoWidget.LISTBOX, 0).obbligatorio(true).lista(indirizzi);
			creaCampo("tipoIngresso", "Tipologia ingresso", TipoWidget.LISTBOX, 1).obbligatorio(true).lista(tipiArr);
			creaCampo("primoAssegnatario", "Primo assegnatario", TipoWidget.YESNORADIOBUTTON, 1).obbligatorio(true);
		}

		@Override
		protected IngressoAbilitazione converti(Object[] riga) {

			LetturaIngressoAbilitazione a = null;

			if (riga != null && riga[0] != null && riga[1] != null && riga[2] != null) {

				String indirizzo = (String) riga[0];

				AnagraficaIngresso anIngr = null;
				if (indirizzo.contains(EPROTOCOLLO_LABEL)) {
					indirizzo = indirizzo.replace(EPROTOCOLLO_LABEL, "");
					anIngr = configurazioniHandler.getAnagraficaIngresso(TipologiaPratica.EMAIL_EPROTOCOLLO.getNomeTipologia(), indirizzo);

				} else {
					anIngr = configurazioniHandler.getAnagraficaIngresso(TipologiaPratica.EMAIL_IN.getNomeTipologia(), indirizzo);
				}

				if (anIngr != null) {

					String riga1 = (String) riga[1];
					Server s = null;
					for (Server server : configurazioniHandler.getProprietaGenerali().getServerIngressiAbilitati()) {
						if (server.getNome().equals(riga1)) {
							s = server;
							break;
						}
					}

					boolean primoAssegnatario = (boolean) riga[2];

					if (anIngr.getServer().equals(s.getNome())) {
						a = new LetturaIngressoAbilitazione(anIngr.getNomeTipologia(), indirizzo);
						a.setPrimoAssegnatario(primoAssegnatario);
					}

				}
			}

			return a;
		}

		@Override
		protected Object[] converti(IngressoAbilitazione riga) {

			if (riga != null && riga instanceof LetturaIngressoAbilitazione) {
				LetturaIngressoAbilitazione r = (LetturaIngressoAbilitazione) riga;
				AnagraficaIngresso ai = null;
				String tipo = null;
				String indirizzo = null;

				if (TipologiaPratica.EMAIL_IN.getNomeTipologia().equals(r.getTipo())) {
					ai = configurazioniHandler.getAnagraficaIngresso(TipologiaPratica.EMAIL_IN.getNomeTipologia(), r.getIndirizzo());
					if (ai != null)
						indirizzo = ai.getIndirizzo();

				} else {
					ai = configurazioniHandler.getAnagraficaIngresso(TipologiaPratica.EMAIL_EPROTOCOLLO.getNomeTipologia(), r.getIndirizzo());
					if (ai != null)
						indirizzo = ai.getIndirizzo() + EPROTOCOLLO_LABEL;
				}

				if (ai != null) {
					boolean primoAssegnatario = r.isPrimoAssegnatario();

					for (Server server : configurazioniHandler.getProprietaGenerali().getServerIngressiAbilitati()) {
						if (server.getNome().equals(ai.getServer())) {
							tipo = server.getNome();
							break;
						}
					}

					return new Object[] { indirizzo, tipo, primoAssegnatario };
				}
			}

			return null;
		}

		@Override
		protected boolean validaInserimento(IngressoAbilitazione riga, List<String> errori) {

			if (riga == null) {
				errori.add("Abilitazione non valida");
				return false;
			}

			if (!(riga instanceof LetturaIngressoAbilitazione)) {
				errori.add("Abilitazione non valida");
				return false;
			}

			AnagraficaIngresso anagraficaIngresso = configurazioniHandler.getAnagraficaIngresso(TipologiaPratica.EMAIL_IN.getNomeTipologia(), riga.getIndirizzo());
			AnagraficaIngresso anagraficaIngressoProtocollo = configurazioniHandler.getAnagraficaIngresso(TipologiaPratica.EMAIL_EPROTOCOLLO.getNomeTipologia(), riga.getIndirizzo());
			// XXX: mancherebbe il modo di controllare correttamente la tipologia
			// boolean controlloValidita = !((anagraficaIngresso != null && anagraficaIngresso.getTipo().equals(riga.getTipo())) //
			// || (anagraficaIngressoProtocollo != null && anagraficaIngressoProtocollo.getTipo().equals(riga.getTipo())));
			boolean controlloValidita = !(anagraficaIngresso != null || anagraficaIngressoProtocollo != null);
			if (riga.getTipo() == null || riga.getTipo().trim().isEmpty() || riga.getIndirizzo() == null || riga.getIndirizzo().trim().isEmpty() || controlloValidita) {
				errori.add("Tipo ingresso non valido");
				return false;
			}

			if (getValori().contains(riga)) {
				errori.add("L'abilitazione esiste già");
				return false;
			}

			if (((LetturaIngressoAbilitazione) riga).isPrimoAssegnatario() && configurazioniHandler.esistePrimoAssegnatario(riga.getTipo(), riga.getIndirizzo())) {
				errori.add("Esiste già un primo assegnatario per l'indirizzo specificato. Per modificare il primo assegnatario accedere al dettaglio dell'ingresso");
				return false;
			}

			return true;
		}

	}

	public class AggiungiCampoCommand extends AggiungiAbilitazioneCommand {

		@Override
		public void execute(IngressoAbilitazione t) {
			Date date = new Date();
			getAzioni().add(new Azione(profilazioneUtenteHandler.getDatiUtente().getUsername(), date, "aggiunta abilitazione per lettura ingresso con indirizzo " + t.getIndirizzo()));
			campiWidget.add(t);
		}

	}

	public class EliminaCampoCommand extends EliminaAbilitazioneCommand {

		@Override
		public void execute(IngressoAbilitazione t) {
			Date date = new Date();
			campiWidget.remove(t);
			getAzioni().add(new Azione(profilazioneUtenteHandler.getDatiUtente().getUsername(), date, "rimozione abilitazione per lettura ingresso con indirizzo " + t.getIndirizzo()));
		}

	}

}
