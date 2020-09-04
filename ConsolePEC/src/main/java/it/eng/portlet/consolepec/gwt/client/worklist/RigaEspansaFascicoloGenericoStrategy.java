package it.eng.portlet.consolepec.gwt.client.worklist;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTMLPanel;

import it.eng.portlet.consolepec.gwt.client.db.PecInPraticheDB;
import it.eng.portlet.consolepec.gwt.client.db.PecInPraticheDB.PraticaComunicazioneLoaded;
import it.eng.portlet.consolepec.gwt.client.db.PecInPraticheDB.PraticaEmaiInlLoaded;
import it.eng.portlet.consolepec.gwt.client.db.PecInPraticheDB.PraticaEmailOutLoaded;
import it.eng.portlet.consolepec.gwt.client.db.PecInPraticheDB.PraticaModulisticaLoaded;
import it.eng.portlet.consolepec.gwt.client.presenter.Command;
import it.eng.portlet.consolepec.gwt.client.widget.ElementoAllegatoElencoWidget;
import it.eng.portlet.consolepec.gwt.client.widget.ElementoComunicazioneElencoWidget;
import it.eng.portlet.consolepec.gwt.client.widget.ElementoComunicazioneElencoWidget.MostraComunicazione;
import it.eng.portlet.consolepec.gwt.client.widget.ElementoPECElencoWidget;
import it.eng.portlet.consolepec.gwt.client.widget.ElementoPECElencoWidget.MostraPEC;
import it.eng.portlet.consolepec.gwt.client.widget.ElementoPraticaModulisticaElencoWidget;
import it.eng.portlet.consolepec.gwt.client.widget.ElementoPraticaModulisticaElencoWidget.MostraPraticaModulistica;
import it.eng.portlet.consolepec.gwt.client.widget.ElementoProtocollazioneBA01ElencoWidget;
import it.eng.portlet.consolepec.gwt.client.widget.SitemapMenu;
import it.eng.portlet.consolepec.gwt.client.worklist.WorklistStrategy.EspandiRigaEventListener;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.action.pec.TipoRiferimentoPEC;
import it.eng.portlet.consolepec.gwt.shared.model.AllegatoDTO;
import it.eng.portlet.consolepec.gwt.shared.model.CapofilaFromBA01DTO;
import it.eng.portlet.consolepec.gwt.shared.model.ComunicazioneDTO;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO.ElementoAllegato;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO.ElementoComunicazioneRiferimento;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO.ElementoElenco;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO.ElementoElencoVisitor;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO.ElementoGruppo;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO.ElementoGruppoProtocollato;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO.ElementoGruppoProtocollatoCapofila;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO.ElementoGrupppoNonProtocollato;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO.ElementoPECRiferimento;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO.ElementoPraticaModulisticaRiferimento;
import it.eng.portlet.consolepec.gwt.shared.model.PecInDTO;
import it.eng.portlet.consolepec.gwt.shared.model.PecOutDTO;
import it.eng.portlet.consolepec.gwt.shared.model.PraticaDTO;
import it.eng.portlet.consolepec.gwt.shared.model.PraticaModulisticaDTO;

public class RigaEspansaFascicoloGenericoStrategy extends AbstractRigaEspansaStrategy {

	private DivElement boxMailDiv;
	private final Command<Void, AllegatoDTO> downloadCommand;
	private final Command<Void, MostraPEC> mostraPECCommand;
	private final Command<Void, MostraPraticaModulistica> mostraPraticaModulisticaCommand;
	private final Command<Void, MostraComunicazione> mostraComunicazioneCommand;
	private final PecInPraticheDB praticheDB;
	private final Command<Void, AllegatoDTO> dettaglioAllegatoCommand;
	private final SitemapMenu sitemapMenu;

	public RigaEspansaFascicoloGenericoStrategy(PecInPraticheDB praticheDB, Command<Void, AllegatoDTO> downloadCommand, Command<Void, AllegatoDTO> dettaglioAllegatoCommand,
			Command<Void, MostraPEC> mostraPECCommand, Command<Void, MostraPraticaModulistica> mostraPraticaModulisticaCommand, Command<Void, MostraComunicazione> mostraComunicazioneCommand,
			SitemapMenu sitemapMenu) {
		this.downloadCommand = downloadCommand;
		this.mostraPECCommand = mostraPECCommand;
		this.mostraPraticaModulisticaCommand = mostraPraticaModulisticaCommand;
		this.mostraComunicazioneCommand = mostraComunicazioneCommand;
		this.dettaglioAllegatoCommand = dettaglioAllegatoCommand;
		this.praticheDB = praticheDB;
		this.sitemapMenu = sitemapMenu;
	}

	@Override
	public void disegnaDettaglio(HTMLPanel dettaglioContent, PraticaDTO pratica) {
		// Dati dei bean
		// id documentale
		Element label = DOM.createSpan();
		label.addClassName("label");
		label.setInnerText("ID documentale");
		dettaglioContent.getElement().appendChild(label);
		HTMLPanel panel = new HTMLPanel(pratica.getNumeroRepertorio());
		panel.addStyleName("abstract_note");
		dettaglioContent.add(panel);

		// TODO custom di fascicolo
		label = DOM.createSpan();
		label.addClassName("label");
		label.setInnerText("Note");
		dettaglioContent.getElement().appendChild(label);
		String note = sanitizeNull(((FascicoloDTO) pratica).getNote());
		if (note.length() > ConsolePecConstants.MAX_NUMERO_CARATTERI_TESTO_LUNGO)
			note = note.substring(0, ConsolePecConstants.MAX_NUMERO_CARATTERI_TESTO_LUNGO);
		panel = new HTMLPanel(note);
		panel.addStyleName("abstract_note");
		dettaglioContent.add(panel);

		RigaEspansaFascicoloVisitor visitor = new RigaEspansaFascicoloVisitor();
		visitor.start(dettaglioContent, (FascicoloDTO) pratica);
	}

	@Override
	public void disegnaOperazioni(HTMLPanel operations, final PraticaDTO pratica, final EspandiRigaEventListener listener) {

		/* Pulsante di chiusura del dettaglio */
		Button chiudiButton = new Button();
		chiudiButton.setText("Chiudi");
		chiudiButton.setStyleName("btn black");
		chiudiButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				listener.onEspandiRiga(pratica.getClientID(), pratica.getTipologiaPratica(), true);
			}
		});
		operations.add(chiudiButton);
	}

	private class RigaEspansaFascicoloVisitor implements ElementoElencoVisitor {

		private HTMLPanel panel;

		private Set<ElementoElenco> sortEmailTesta(Collection<ElementoElenco> insieme) {
			Set<ElementoElenco> res = new LinkedHashSet<FascicoloDTO.ElementoElenco>();
			for (ElementoElenco elem : insieme)
				if (elem instanceof ElementoPECRiferimento)
					res.add(elem);
			for (ElementoElenco elem : insieme)
				if (!res.contains(elem))
					res.add(elem);

			return res;
		}

		private Set<ElementoElenco> sortNonProtTesta(Collection<ElementoElenco> insieme) {
			Set<ElementoElenco> res = new LinkedHashSet<FascicoloDTO.ElementoElenco>();
			for (ElementoElenco elem : insieme)
				if (elem instanceof ElementoGrupppoNonProtocollato)
					res.add(elem);
			for (ElementoElenco elem : insieme)
				if (!res.contains(elem))
					res.add(elem);

			return res;
		}

		public void start(HTMLPanel panel, FascicoloDTO pratica) {
			this.panel = panel;
			for (ElementoElenco elem : sortNonProtTesta(pratica.getElenco()))
				elem.accept(this);
		}

		@Override
		public void visit(ElementoAllegato allegato) {
			ElementoAllegatoElencoWidget w = new ElementoAllegatoElencoWidget();
			panel.add(w, boxMailDiv);
			w.setDownloadAllegatoCommand(downloadCommand);
			w.setMostraDettaglioAllegatoCommand(dettaglioAllegatoCommand);
			w.setCheckBoxVisible(false);
			w.mostraDettaglio(allegato);
		}

		@Override
		public void visit(ElementoPECRiferimento pec) {
			final ElementoPECElencoWidget w = new ElementoPECElencoWidget();
			panel.add(w, boxMailDiv);
			w.setCheckBoxVisible(false);
			w.setMostraDettaglioPEC(mostraPECCommand);
			if (pec.getTipo().equals(TipoRiferimentoPEC.IN) || pec.getTipo().equals(TipoRiferimentoPEC.EPROTO)) {
				RigaEspansaFascicoloGenericoStrategy.this.praticheDB.getPecInByPath(pec.getRiferimento(), sitemapMenu.containsLink(pec.getRiferimento()), new PraticaEmaiInlLoaded() {
					@Override
					public void onPraticaLoaded(PecInDTO pec) {
						w.mostraDettaglio(pec);
						w.setCheckBoxEnabled(false);
					}

					@Override
					public void onPraticaError(String error) {
						// TODO Auto-generated method stub

					}
				});
			} else {// PECOUT
				RigaEspansaFascicoloGenericoStrategy.this.praticheDB.getPecOutByPath(pec.getRiferimento(), sitemapMenu.containsLink(pec.getRiferimento()), new PraticaEmailOutLoaded() {

					@Override
					public void onPraticaLoaded(PecOutDTO pec) {
						w.mostraDettaglio(pec);
						w.setCheckBoxEnabled(false);
					}

					@Override
					public void onPraticaError(String error) {
						// nop
					}
				});
			}

		}

		@Override
		public void visit(ElementoGruppo gruppo) {
			// nop
		}

		@Override
		public void visit(ElementoGruppoProtocollatoCapofila capofila) {
			creaStrutturaElenco(capofila.getNumeroPG() + "/" + capofila.getAnnoPG());

			if (capofila.getOggetto() != null) {
				ElementoProtocollazioneBA01ElencoWidget w = new ElementoProtocollazioneBA01ElencoWidget();
				w.mostraCapofila(new CapofilaFromBA01DTO(capofila.getOggetto(), capofila.getNumeroPG(), capofila.getAnnoPG(), false, capofila.getDataProtocollazione()));
				w.setCheckBoxVisibility(false);
				panel.add(w, boxMailDiv);
			}

			for (ElementoElenco ee : sortEmailTesta(capofila.getElementi())) {
				ee.accept(this);
			}
		}

		private void creaStrutturaElenco(String titoloLabel) {
			SpanElement span = Document.get().createSpanElement();
			span.setClassName("label label-allegati");
			span.setInnerText(titoloLabel);
			panel.getElement().appendChild(span);

			DivElement abstractDiv = Document.get().createDivElement();
			abstractDiv.setClassName("abstract");
			panel.getElement().appendChild(abstractDiv);
			DivElement corpoDiv = Document.get().createDivElement();
			corpoDiv.setClassName("corpo");
			abstractDiv.appendChild(corpoDiv);
			boxMailDiv = Document.get().createDivElement();
			boxMailDiv.setClassName("box-mail");
			corpoDiv.appendChild(boxMailDiv);
		}

		@Override
		public void visit(ElementoGruppoProtocollato subProt) {
			// nop
		}

		@Override
		public void visit(ElementoGrupppoNonProtocollato nonProt) {
			if (nonProt.getElementi().size() > 0) {
				creaStrutturaElenco("Non protocollati");
				for (ElementoElenco ee : sortEmailTesta(nonProt.getElementi())) {
					ee.accept(this);
				}
			}
		}

		@Override
		public void visit(ElementoPraticaModulisticaRiferimento pm) {
			final ElementoPraticaModulisticaElencoWidget w = new ElementoPraticaModulisticaElencoWidget();
			panel.add(w, boxMailDiv);
			w.setCheckBoxVisible(false);
			w.setMostraDettaglioPraticaModulistica(mostraPraticaModulisticaCommand);
			RigaEspansaFascicoloGenericoStrategy.this.praticheDB.getPraticaModulisticaByPath(pm.getRiferimento(), sitemapMenu.containsLink(pm.getRiferimento()), new PraticaModulisticaLoaded() {

				@Override
				public void onPraticaModulisticaLoaded(PraticaModulisticaDTO pratica) {
					w.mostraDettaglio(pratica);
				}

				@Override
				public void onPraticaModulisticaError(String error) {
					// nop
				}
			});
		}

		@Override
		public void visit(ElementoComunicazioneRiferimento c) {
			final ElementoComunicazioneElencoWidget w = new ElementoComunicazioneElencoWidget();
			panel.add(w, boxMailDiv);
			w.setCheckBoxVisible(false);
			w.setMostraDettaglioComunicazione(mostraComunicazioneCommand);
			RigaEspansaFascicoloGenericoStrategy.this.praticheDB.getComunicazioneByPath(c.getRiferimento(), sitemapMenu.containsLink(c.getRiferimento()), new PraticaComunicazioneLoaded() {

				@Override
				public void onPraticaLoaded(ComunicazioneDTO comunicazione) {
					w.mostraDettaglio(comunicazione);
				}

				@Override
				public void onPraticaError(String error) {
					// nop
				}

			});
		}
	}

}
