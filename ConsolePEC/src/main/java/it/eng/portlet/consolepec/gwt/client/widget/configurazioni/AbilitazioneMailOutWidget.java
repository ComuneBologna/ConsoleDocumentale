package it.eng.portlet.consolepec.gwt.client.widget.configurazioni;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaEmailOut;
import it.eng.cobo.consolepec.commons.configurazioni.Azione;
import it.eng.cobo.consolepec.commons.configurazioni.TipologiaPratica;
import it.eng.cobo.consolepec.commons.configurazioni.abilitazione.CreazioneEmailOutAbilitazione;
import it.eng.cobo.consolepec.commons.configurazioni.abilitazione.EmailOutAbilitazione;
import it.eng.portlet.consolepec.gwt.client.handler.configurazioni.ConfigurazioniHandler;
import it.eng.portlet.consolepec.gwt.client.handler.profilazione.ProfilazioneUtenteHandler;
import it.eng.portlet.consolepec.gwt.client.widget.ListaCampiWidget;
import it.eng.portlet.consolepec.gwt.shared.dto.Campo.TipoWidget;

/**
 *
 * @author biagiot
 *
 */
public class AbilitazioneMailOutWidget extends AbilitazioneWidget<EmailOutAbilitazione> {

	interface AbilitazioneMailOutWidgetBinder extends UiBinder<Widget, AbilitazioneMailOutWidget> {}

	private static AbilitazioneMailOutWidgetBinder uiBinder = GWT.create(AbilitazioneMailOutWidgetBinder.class);

	@UiField
	HTMLPanel abilitazioneMailOutPanel;

	private CampiAbilitazioniEmailOutWidget campiWidget;
	private ConfigurazioniHandler configurazioniHandler;
	private ProfilazioneUtenteHandler profilazioneUtenteHandler;

	public AbilitazioneMailOutWidget() {
		super();
		initWidget(uiBinder.createAndBindUi(this));

	}

	public void render(ConfigurazioniHandler configurazioniHandler, ProfilazioneUtenteHandler profilazioneUtenteHandler, List<EmailOutAbilitazione> abilitazioni) {
		super.render(abilitazioni, profilazioneUtenteHandler);

		this.configurazioniHandler = configurazioniHandler;
		this.profilazioneUtenteHandler = profilazioneUtenteHandler;

		this.abilitazioneMailOutPanel.clear();
		this.campiWidget = new CampiAbilitazioniEmailOutWidget(100);
		this.campiWidget.render(true, true);
		if (abilitazioni != null && !abilitazioni.isEmpty()) {
			for (EmailOutAbilitazione fa : abilitazioni) {
				if (fa instanceof CreazioneEmailOutAbilitazione)
					this.campiWidget.add(fa);
			}
		}
		this.abilitazioneMailOutPanel.add(campiWidget);

		this.campiWidget.setAggiungiCommand(new AggiungiCampoCommand());
		this.campiWidget.setEliminaCommand(new EliminaCampoCommand());
	}

	public void clear() {
		if (campiWidget != null) {
			campiWidget.getValori().clear();
		}
	}

	public class CampiAbilitazioniEmailOutWidget extends ListaCampiWidget<EmailOutAbilitazione> {

		public CampiAbilitazioniEmailOutWidget(Integer limit) {
			super(limit);
		}

		@Override
		protected void definisciCampi() {

			List<String> indirizziEmail = new ArrayList<String>();
			for (AnagraficaEmailOut ai : configurazioniHandler.getAnagraficheMailInUscita(false)) {
				indirizziEmail.add(ai.getIndirizzo());
			}

			String[] indirizzi = new String[indirizziEmail.size()];
			indirizzi = indirizziEmail.toArray(indirizzi);

			creaCampo("mailOut", "Mail in uscita", TipoWidget.LISTBOX, 0).obbligatorio(true).lista(indirizzi);
		}

		@Override
		protected EmailOutAbilitazione converti(Object[] riga) {

			EmailOutAbilitazione a = null;

			if (riga != null && riga[0] != null) {

				String indirizzo = (String) riga[0];
				AnagraficaEmailOut ai = configurazioniHandler.getAnagraficaMailInUscita(TipologiaPratica.EMAIL_OUT.getNomeTipologia(), indirizzo);

				if (ai != null) {
					a = new CreazioneEmailOutAbilitazione(ai.getNomeTipologia(), indirizzo);
				}
			}

			return a;
		}

		@Override
		protected Object[] converti(EmailOutAbilitazione riga) {

			String indirizzo = null;

			if (riga != null && riga instanceof CreazioneEmailOutAbilitazione) {
				AnagraficaEmailOut ai = configurazioniHandler.getAnagraficaMailInUscita(TipologiaPratica.EMAIL_OUT.getNomeTipologia(), riga.getIndirizzo());

				if (ai != null && ai.getNomeTipologia().equals(riga.getTipo())) {
					indirizzo = ai.getIndirizzo();
					return new Object[] { indirizzo };
				}
			}

			return null;
		}

		@Override
		protected boolean validaInserimento(EmailOutAbilitazione riga, List<String> errori) {

			if (riga == null) {
				errori.add("Abilitazione non valida");
				return false;
			}

			if (!(riga instanceof CreazioneEmailOutAbilitazione)) {
				errori.add("Abilitazione non valida");
				return false;
			}

			if (riga.getTipo() == null || riga.getTipo().trim().isEmpty() || riga.getIndirizzo() == null || riga.getIndirizzo().trim().isEmpty()
					|| configurazioniHandler.getAnagraficaMailInUscita(TipologiaPratica.EMAIL_OUT.getNomeTipologia(), riga.getIndirizzo()) == null
					|| !configurazioniHandler.getAnagraficaMailInUscita(TipologiaPratica.EMAIL_OUT.getNomeTipologia(), riga.getIndirizzo()).getNomeTipologia().equals(riga.getTipo())) {

				errori.add("Tipo mail in uscita non valido");
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
		public void execute(EmailOutAbilitazione t) {
			Date date = new Date();

			campiWidget.add(t);
			getAzioni().add(new Azione(profilazioneUtenteHandler.getDatiUtente().getUsername(), date, "aggiunta abilitazione per creazione email in uscita con indirizzo " + t.getIndirizzo()));
		}

	}

	public class EliminaCampoCommand extends EliminaAbilitazioneCommand {

		@Override
		public void execute(EmailOutAbilitazione t) {
			Date date = new Date();

			campiWidget.remove(t);
			getAzioni().add(new Azione(profilazioneUtenteHandler.getDatiUtente().getUsername(), date, "rimozione abilitazione per creazione email in uscita con indirizzo " + t.getIndirizzo()));
		}
	}

}
