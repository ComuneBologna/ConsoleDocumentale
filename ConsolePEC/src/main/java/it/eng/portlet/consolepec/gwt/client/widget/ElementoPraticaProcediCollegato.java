package it.eng.portlet.consolepec.gwt.client.widget;

import it.eng.cobo.consolepec.commons.urbanistica.AllegatoProcedi;
import it.eng.cobo.consolepec.commons.urbanistica.PraticaProcedi;
import it.eng.portlet.consolepec.gwt.client.presenter.Command;
import it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.DettaglioFascicoloGenericoPresenter.DettaglioPraticaProcediCollegatoCommand;
import it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.DettaglioFascicoloGenericoPresenter.DownloadAllegatoCommand;
import it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.DettaglioFascicoloGenericoPresenter.MostraDettaglioAllegatoCommand;
import it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.DettaglioFascicoloGenericoPresenter.MostraDettaglioBozzaCommand;
import it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.DettaglioFascicoloGenericoPresenter.MostraDettaglioEmailCommand;
import it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.command.DownloadAllegatoProcediCollegamentoCommand;
import it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.command.GoToDettaglioModuloCommand;
import it.eng.portlet.consolepec.gwt.client.widget.composizione.AllegatoComposizioneWidget;
import it.eng.portlet.consolepec.gwt.client.widget.composizione.PecComposizioneWidget;
import it.eng.portlet.consolepec.gwt.client.widget.composizione.PecComposizioneWidget.MostraPEC;
import it.eng.portlet.consolepec.gwt.client.widget.composizione.PraticaModulisticaComposizioneWidget;
import it.eng.portlet.consolepec.gwt.client.widget.composizione.PraticaModulisticaComposizioneWidget.MostraPraticaModulistica;
import it.eng.portlet.consolepec.gwt.client.widget.composizione.SlideAnimation;
import it.eng.portlet.consolepec.gwt.client.widget.images.ConsolePECIcons;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.action.pec.TipoRiferimentoPEC;
import it.eng.portlet.consolepec.gwt.shared.model.AllegatoDTO;
import it.eng.portlet.consolepec.gwt.shared.model.PecDTO;
import it.eng.portlet.consolepec.gwt.shared.model.PraticaModulisticaDTO;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;

public class ElementoPraticaProcediCollegato extends Composite {

	private static ElementoPraticaProcediCollegatoUiBinder uiBinder = GWT.create(ElementoPraticaProcediCollegatoUiBinder.class);

	interface ElementoPraticaProcediCollegatoUiBinder extends UiBinder<Widget, ElementoPraticaProcediCollegato> {
	}

	public ElementoPraticaProcediCollegato() {
		initWidget(uiBinder.createAndBindUi(this));
		setCheckBoxVisibility(true);
	}

	@UiField
	DivElement documentoContainer;
	@UiField
	HTMLPanel mainPanel;
	@UiField
	CheckBox checkBox;
	@UiField
	Image iconaLabel;
	@UiField
	Anchor linkPraticaProcediCollegato;
	@UiField
	Button mostraDettaglioPraticaProcediCollegato;
	
	private HTMLPanel dettaglioPanel;
	private SlideAnimation slideAnimation;
	
	public ElementoPraticaProcediCollegato(final PraticaProcedi praticaProcedi, final it.eng.portlet.consolepec.gwt.client.presenter.Command<Void, PraticaProcedi> linkCommand, 
			final it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.DettaglioFascicoloGenericoPresenter.DettaglioPraticaProcediCollegatoCommand dettaglioPraticaProcediCollegatoCommand, final ClickHandler checkBoxClickHandler) {
		initWidget(uiBinder.createAndBindUi(this));
		setCheckBoxVisibility(true);
		String oggetto = praticaProcedi.getOggetto();
		if (oggetto.length() > ConsolePecConstants.DESCR_WIDGET_ELEMENTI_MAX_LEN) {
			oggetto = praticaProcedi.getOggetto().substring(0, ConsolePecConstants.DESCR_WIDGET_ELEMENTI_MAX_LEN).concat("...");
			SpanElement labelDettaglio = Document.get().createSpanElement();
			labelDettaglio.setClassName("documento");
			getElement().appendChild(labelDettaglio);
		}
		StringBuilder sb = new StringBuilder(oggetto);
		linkPraticaProcediCollegato.setText(sb.toString());
		iconaLabel.setResource(ConsolePECIcons._instance.fascicoloBa01());
		linkPraticaProcediCollegato.setHref("javascript:;");
		linkPraticaProcediCollegato.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				linkCommand.exe(praticaProcedi);
				Window.scrollTo (0 ,0);
			}
		});

		checkBox.addClickHandler(checkBoxClickHandler);
		
		if (dettaglioPraticaProcediCollegatoCommand != null) {
			
			dettaglioPanel = new HTMLPanel("");
			dettaglioPanel.setVisible(false);
			dettaglioPanel.setStyleName("box-mail last");
			slideAnimation = new SlideAnimation(dettaglioPanel);
			
			mainPanel.add(dettaglioPanel);
			if(!praticaProcedi.getAllegati().isEmpty()){
				mostraDettaglioPraticaProcediCollegato.setEnabled(true);
				mostraDettaglioPraticaProcediCollegato.addClickHandler(new ClickHandler() {
					
					@Override
					public void onClick(ClickEvent event) {
						runMostraDettaglioFascicoloCollegatoCommand(dettaglioPraticaProcediCollegatoCommand, praticaProcedi);
					}
				});
			} else {
				mostraDettaglioPraticaProcediCollegato.setEnabled(false);
			}
		} else {
			mostraDettaglioPraticaProcediCollegato.setEnabled(false);
		}
	}
	
	private void runMostraDettaglioFascicoloCollegatoCommand(DettaglioPraticaProcediCollegatoCommand dettaglioPraticaProcediCollegatoCommand, PraticaProcedi praticaProcedi) {
		dettaglioPraticaProcediCollegatoCommand.exe(this, praticaProcedi);
	}
	
	public void setCheckBoxVisibility(boolean visible) {
		checkBox.setVisible(visible);
	}
	
	public void cleanDettaglioPanel(){
		dettaglioPanel.clear();
	}
	
	public void addAllegatoWidget(AllegatoProcedi allegato, final DownloadAllegatoProcediCollegamentoCommand downloadAllegatoProcediCollegamentoCommand) {		
		final AllegatoComposizioneWidget rigaAllegato = new AllegatoComposizioneWidget();
		rigaAllegato.setCheckBoxVisible(false);
		
		rigaAllegato.setDownloadAllegatoProcediCommand(downloadAllegatoProcediCollegamentoCommand);
		dettaglioPanel.add(rigaAllegato);
		rigaAllegato.mostraDettaglio(allegato);
		
	}
	
	public void addPecWidget(final PecDTO pec, final DownloadAllegatoCommand downloadAllegatoCommand, final MostraDettaglioAllegatoCommand mostraDettaglioAllegatoCommand, final MostraDettaglioEmailCommand mostraDettaglioEmailCommand, final MostraDettaglioBozzaCommand mostraDettaglioBozzaCommand) {
		final PecComposizioneWidget rigaPec = new PecComposizioneWidget();
		rigaPec.setCheckBoxEnabled(false);
		rigaPec.setCheckBoxVisible(false);
		
		rigaPec.setDownloadAllegatoCommand(new Command<Void, AllegatoDTO>() {
			
			@Override
			public Void exe(AllegatoDTO allegato) {
				downloadAllegatoCommand.exe(allegato);
				return null;
			}
		});
		
		rigaPec.setMostraAllegatoCommand(new Command<Void, AllegatoDTO>() {
			
			@Override
			public Void exe(AllegatoDTO allegato) {
				mostraDettaglioAllegatoCommand.exe(pec.getClientID(), allegato);
				return null;
			}
		});
		
		rigaPec.setMostraDettaglioPEC(new Command<Void, PecComposizioneWidget.MostraPEC>() {
			
			@Override
			public Void exe(MostraPEC t) {
				
				if (t.getTipo().equals(TipoRiferimentoPEC.IN) || t.getTipo().equals(TipoRiferimentoPEC.EPROTO))
					mostraDettaglioEmailCommand.exe(t.getClientID());
				else
					mostraDettaglioBozzaCommand.exe(t.getClientID());
				return null;
			}
		});
		
		dettaglioPanel.add(rigaPec);
		rigaPec.mostraDettaglio(pec);
		
	}
	
	public void addPraticaModulisticaWidget(final PraticaModulisticaDTO praticaModulistica, final DownloadAllegatoCommand downloadAllegatoCommand, final MostraDettaglioAllegatoCommand mostraDettaglioAllegatoCommand, final GoToDettaglioModuloCommand goToDettaglioModuloCommand) {
		 final PraticaModulisticaComposizioneWidget rigaPraticaModulistica = new PraticaModulisticaComposizioneWidget();
		 rigaPraticaModulistica.setCheckBoxVisible(false);
		 rigaPraticaModulistica.setCheckBoxEnabled(false);
		
		 rigaPraticaModulistica.setMostraDettaglioPraticaModulistica(new Command<Void, PraticaModulisticaComposizioneWidget.MostraPraticaModulistica>() {
			
			@Override
			public Void exe(MostraPraticaModulistica t) {
				goToDettaglioModuloCommand.execute();
				return null;
			}
		});
		 
		 rigaPraticaModulistica.setDownloadAllegatoCommand(new Command<Void, AllegatoDTO>() {
			@Override
			public Void exe(AllegatoDTO t) {
				downloadAllegatoCommand.exe(t);
				return null;
			}
		 });
		 
		 rigaPraticaModulistica.setMostraAllegatoCommand(new Command<Void, AllegatoDTO>() {
			
			@Override
			public Void exe(AllegatoDTO t) {
				mostraDettaglioAllegatoCommand.exe(praticaModulistica.getClientID(), t);
				return null;
			}
		});
		 
		 dettaglioPanel.add(rigaPraticaModulistica);
		 rigaPraticaModulistica.mostraDettaglio(praticaModulistica);
	}
	
	public void openDettaglioPanel() {
		slideAnimation.run(200);
	}
	
}
