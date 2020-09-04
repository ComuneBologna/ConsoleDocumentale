package it.eng.portlet.consolepec.gwt.client.view.pec;

import it.eng.portlet.consolepec.gwt.client.db.PecInPraticheDB;
import it.eng.portlet.consolepec.gwt.client.db.PecInPraticheDB.PraticaEmaiInlLoaded;
import it.eng.portlet.consolepec.gwt.client.event.ShowMessageEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.Command;
import it.eng.portlet.consolepec.gwt.client.presenter.pec.CaricaAllegatiDaPraticaPresenter;
import it.eng.portlet.consolepec.gwt.client.util.VisualizzazioneElementiProtocollati;
import it.eng.portlet.consolepec.gwt.client.view.OpenCloseImagePanel;
import it.eng.portlet.consolepec.gwt.client.widget.ElementoAllegatoElencoWidget;
import it.eng.portlet.consolepec.gwt.client.widget.ElementoAllegatoElencoWidget.SelezioneAllegato;
import it.eng.portlet.consolepec.gwt.client.widget.MessageAlertWidget;
import it.eng.portlet.consolepec.gwt.client.widget.SitemapMenu;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.action.pec.TipoRiferimentoPEC;
import it.eng.portlet.consolepec.gwt.shared.model.AllegatoDTO;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.LIElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.dom.client.UListElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.ViewImpl;

public class CaricaAllegatiDaPraticaView extends ViewImpl implements CaricaAllegatiDaPraticaPresenter.MyView {

	private final Widget widget;
	/*
	 * @UiField Tree treeAllegati;
	 */

	@UiField
	Button caricaSelezionatiTreeButton;
	@UiField
	Button annullaButton;

	@UiField
	HTMLPanel treeAllegatiPanel;

	@UiField(provided = true)
	DisclosurePanel disclosurePanelFascicoloAllegati = new DisclosurePanel(OpenCloseImagePanel.DEFAULT_IMAGES.disclosurePanelOpen(), OpenCloseImagePanel.DEFAULT_IMAGES.disclosurePanelClosed(), "Seleziona allegati dal fascicolo");
	@UiField(provided = true)
	DisclosurePanel disclosurePanelPraticheAllegati = new DisclosurePanel(OpenCloseImagePanel.DEFAULT_IMAGES.disclosurePanelOpen(), OpenCloseImagePanel.DEFAULT_IMAGES.disclosurePanelClosed(), "Seleziona allegati dalle mail in ingresso");
	@UiField(provided = true)
	MessageAlertWidget messageWidget;

	private Map<String, List<AllegatoDTO>> allegatiTreeSel = new HashMap<String, List<AllegatoDTO>>();
	private final PecInPraticheDB db;
	private final SitemapMenu sitemapMenu;
	private final EventBus eventBus;

	public interface Binder extends UiBinder<Widget, CaricaAllegatiDaPraticaView> {
	}

	@Inject
	public CaricaAllegatiDaPraticaView(final Binder binder, final PecInPraticheDB pecInDb, final EventBus eventBus, final SitemapMenu sitemapMenu) {
		messageWidget = new MessageAlertWidget(eventBus);
		widget = binder.createAndBindUi(this);
		this.sitemapMenu = sitemapMenu;
		this.db = pecInDb;
		this.eventBus = eventBus;
		caricaSelezionatiTreeButton.setEnabled(false);
		// setupTreeAllegati();
	}

	@Override
	public Widget asWidget() {
		return widget;

	}

	@Override
	public void popolaAllegati(FascicoloDTO fascicolo) {
		disclosurePanelPraticheAllegati.clear();
		disclosurePanelPraticheAllegati.setVisible(false);
		disclosurePanelFascicoloAllegati.clear();
		disclosurePanelFascicoloAllegati.setVisible(false);
		allegatiTreeSel = new HashMap<String, List<AllegatoDTO>>();

		MostraAllegatiPraticaVisitor v = new MostraAllegatiPraticaVisitor(fascicolo);
		List<AllegatoDTO> allegati = v.getAllegatiFascicolo();
		List<PecInDTO> listaEmailConAllegati = v.getEmailConAllegati();

		if (allegati.size() > 0) {
			HTMLPanel allegatiFascicoloPanel = new HTMLPanel("");
			addAllegatiSection(fascicolo.getClientID(), fascicolo.getNumeroRepertorio(), allegati, allegatiFascicoloPanel, fascicolo);
			disclosurePanelFascicoloAllegati.setVisible(true);
			disclosurePanelFascicoloAllegati.add(allegatiFascicoloPanel);
		}

		if (listaEmailConAllegati.size() > 0) {
			HTMLPanel allegatiPratichePanel = new HTMLPanel("");
			for (PecInDTO pec : listaEmailConAllegati)
				addAllegatiSection(pec.getClientID(), pec.getNumeroRepertorio(), pec.getAllegati(), allegatiPratichePanel, fascicolo);
			disclosurePanelPraticheAllegati.setVisible(true);
			disclosurePanelPraticheAllegati.add(allegatiPratichePanel);
		}
	}

	protected void addAllegatiSection(final String clientId, String idDocumentale, final List<AllegatoDTO> allegati, HTMLPanel panelContenitore, FascicoloDTO fascicoloCollegato) {

		Label mailId = new Label(idDocumentale);
		UListElement ul = Document.get().createULElement();
		ul.addClassName("contenitore-lista-gruppi");
		LIElement li = Document.get().createLIElement();
		li.addClassName("gruppo last clearfix");
		ul.appendChild(li);
		SpanElement span = Document.get().createSpanElement();
		span.addClassName("label nessun-protocollo");
		// span.setInnerText( pg );
		li.appendChild(span);
		DivElement div = Document.get().createDivElement();
		div.setClassName("corpo");
		li.appendChild(div);

		HTMLPanel panel = new HTMLPanel("");
		panel.setStyleName("box-mail");

		Map<ElementoGruppoProtocollato, List<AllegatoDTO>> allegatiProtocollati = new LinkedHashMap<FascicoloDTO.ElementoGruppoProtocollato, List<AllegatoDTO>>();
		List<AllegatoDTO> allegatiNonProtocollati = new ArrayList<AllegatoDTO>();

		for (final AllegatoDTO allegato : allegati) {
			if (!Boolean.TRUE.equals(allegato.isLock())) {
				ElementoGruppoProtocollato elementoProtocollato = getProtocollazione(allegato, fascicoloCollegato);

				if(elementoProtocollato != null){
					if(allegatiProtocollati.containsKey(elementoProtocollato)){
						allegatiProtocollati.get(elementoProtocollato).add(allegato);
					} else {
						List<AllegatoDTO> alls = new ArrayList<AllegatoDTO>();
						alls.add(allegato);
						allegatiProtocollati.put(elementoProtocollato, alls);
					}
				} else {
					allegatiNonProtocollati.add(allegato);
				}
			}
		}

		if(allegatiNonProtocollati.size() > 0){
			HTMLPanel pannelloGruppoNonProtocollato = VisualizzazioneElementiProtocollati.buildGruppoPanel("Non Protocollati", panel);
			for (AllegatoDTO allegato : allegatiNonProtocollati) {

				pannelloGruppoNonProtocollato.add(getAllegatoWidget(clientId, allegato));

			}
		}


		for(Entry<ElementoGruppoProtocollato, List<AllegatoDTO>> entry : allegatiProtocollati.entrySet()){
			ElementoGruppoProtocollato egp = entry.getKey();
			List<AllegatoDTO> ap = entry.getValue();

			// questa parte di codice è stata presa dal dettaglio del fascicolo metodo: initNonProtocollati

			HTMLPanel pannelloGruppo = VisualizzazioneElementiProtocollati.buildGruppoPanel(egp.getNumeroPG() + "/" + egp.getAnnoPG(), panel);

			for (final AllegatoDTO allegato : ap) {

				ElementoAllegatoElencoWidget allgwidget = getAllegatoWidget(clientId, allegato);
				pannelloGruppo.add(allgwidget);

			}

		}

		panelContenitore.getElement().appendChild(ul);
		panelContenitore.add(mailId, span);
		panelContenitore.add(panel, div);

	}

	private ElementoAllegatoElencoWidget getAllegatoWidget(final String clientId, AllegatoDTO allegato){
		ElementoAllegatoElencoWidget dettaglioWiget = new ElementoAllegatoElencoWidget();
		dettaglioWiget.setCheckBoxVisible(true);
		dettaglioWiget.setSelezionaAllegatoCommand(new Command<Void, ElementoAllegatoElencoWidget.SelezioneAllegato>() {

			@Override
			public Void exe(SelezioneAllegato t) {
				List<AllegatoDTO> allegatiList;

				if (t.isChecked()) {
					allegatiList = allegatiTreeSel.get(clientId);
					if (allegatiList == null) {
						allegatiList = new ArrayList<AllegatoDTO>();
					}
					allegatiList.add(t.getAllegato());
					allegatiTreeSel.put(clientId, allegatiList);
					caricaSelezionatiTreeButton.setEnabled(true);
				} else {
					allegatiTreeSel.remove(clientId);
					caricaSelezionatiTreeButton.setEnabled(!allegatiTreeSel.isEmpty());
				}
				return null;

			}
		});
		dettaglioWiget.mostraDettaglio(allegato);
		return dettaglioWiget;
	}

	private ElementoGruppoProtocollato getProtocollazione(AllegatoDTO allegato, FascicoloDTO fascicoloCollegato) {
		for (ElementoElenco elementoElenco : fascicoloCollegato.getElenco()) {

			if (elementoElenco instanceof ElementoGruppoProtocollatoCapofila) {
				ElementoGruppoProtocollatoCapofila capofila = (ElementoGruppoProtocollatoCapofila) elementoElenco;

				for (ElementoElenco elementoDelCapofila : capofila.getElementi()) {


					if (elementoDelCapofila instanceof ElementoAllegato) {
						ElementoAllegato ea = (ElementoAllegato) elementoDelCapofila;

						if(allegato.getNome().equals(ea.getNome())) {
							return capofila;
						}
					}

					if(elementoDelCapofila instanceof ElementoGruppoProtocollato){
						ElementoGruppoProtocollato nonCapofila = (ElementoGruppoProtocollato) elementoDelCapofila;
						// controllo gli elementi della protocollazione collegata
						for (ElementoElenco elementoDelNonCapofila : nonCapofila.getElementi()){

							if (elementoDelNonCapofila instanceof ElementoAllegato) {
								ElementoAllegato ea = (ElementoAllegato) elementoDelNonCapofila;

								if(allegato.getNome().equals(ea.getNome())) {

									return nonCapofila;
								}
							}
						}
					}
				}

			}
		}
		return null;
	}

	class MostraAllegatiPraticaVisitor implements ElementoElencoVisitor {

		private Set<AllegatoDTO> allegatiFascicolo = new TreeSet<AllegatoDTO>();

		private List<PecInDTO> emailConAllegati = new ArrayList<PecInDTO>();

		public MostraAllegatiPraticaVisitor(FascicoloDTO pec) {
			start(pec);
		}

		public void start(FascicoloDTO pec) {
			for (ElementoElenco e : pec.getElenco())
				e.accept(this);
		}

		@Override
		public void visit(ElementoAllegato allegato) {
			allegatiFascicolo.add(allegato);
		}

		@Override
		public void visit(ElementoPECRiferimento elementPec) {
			if (elementPec.getTipo().equals(TipoRiferimentoPEC.IN) || elementPec.getTipo().equals(TipoRiferimentoPEC.EPROTO)) {
				db.getPecInByPath(elementPec.getRiferimento(), sitemapMenu.containsLink(elementPec.getRiferimento()), new PraticaEmaiInlLoaded() {

					@Override
					public void onPraticaLoaded(PecInDTO pec) {

						if (pec.getAllegati().size() > 0)
							emailConAllegati.add(pec);

					}

					@Override
					public void onPraticaError(String error) {
						ShowMessageEvent event = new ShowMessageEvent();
						event.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
						eventBus.fireEvent(event);
					}
				});
			}

		}

		@Override
		public void visit(ElementoGruppo gruppo) {

		}

		@Override
		public void visit(ElementoGruppoProtocollatoCapofila capofila) {
			for (ElementoElenco elemento : capofila.getElementi()) {
				elemento.accept(this);
			}
		}

		@Override
		public void visit(ElementoGruppoProtocollato subProt) {
			for (ElementoElenco elemento : subProt.getElementi()) {
				elemento.accept(this);
			}

		}

		@Override
		public void visit(ElementoGrupppoNonProtocollato nonProt) {
			for (ElementoElenco elemento : nonProt.getElementi()) {
				elemento.accept(this);
			}

		}

		public List<AllegatoDTO> getAllegatiFascicolo() {
			return new ArrayList<AllegatoDTO>(allegatiFascicolo);
		}

		public void setAllegati(List<AllegatoDTO> allegati) {
			this.allegatiFascicolo.addAll(allegati);
		}

		public List<PecInDTO> getEmailConAllegati() {
			return emailConAllegati;
		}

		public void setEmailConAllegati(List<PecInDTO> email) {
			this.emailConAllegati = email;
		}

		@Override
		public void visit(ElementoPraticaModulisticaRiferimento pm) {


		}

		@Override
		public void visit(ElementoComunicazioneRiferimento elementoComunicazioneRiferimento) {
			// nop
		}

	}

	@Override
	public void setCommandUploadAllegatoPraticheCollegate(final com.google.gwt.user.client.Command command) {
		caricaSelezionatiTreeButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				command.execute();
			}
		});
	}

	@Override
	public void setAnnullaCommand(final com.google.gwt.user.client.Command command) {
		annullaButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				command.execute();
			}
		});
	}

	@Override
	public Map<String, List<AllegatoDTO>> getAllegatiSelezionati() {
		return allegatiTreeSel;
	}



	/*
	 * private void setupTreeAllegati() {
	 *//**
	 * L'evento di selezione viene lanciato due volte
	 *
	 * @see http ://code.google.com/p/google-web-toolkit/issues/detail?id=3660
	 */
	/*
	 *
	 * treeAllegati.addSelectionHandler(new SelectionHandler<TreeItem>() {
	 *
	 * @Override public void onSelection(SelectionEvent<TreeItem> event) {
	 *
	 * TreeItem selItem = event.getSelectedItem(); TreeItem parent =
	 * selItem.getParentItem(); selItem.getTree().setSelectedItem(parent,
	 * false); // null is ok if (parent != null) parent.setSelected(false); //
	 * not compulsory
	 *
	 * if (!selItem.getState()) selItem.setState(true, false); else
	 * selItem.setState(false, false);
	 *
	 * } });
	 *
	 * }
	 */

}
