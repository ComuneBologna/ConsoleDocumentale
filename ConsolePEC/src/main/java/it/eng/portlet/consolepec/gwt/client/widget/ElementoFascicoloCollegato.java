package it.eng.portlet.consolepec.gwt.client.widget;

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

import it.eng.portlet.consolepec.gwt.client.presenter.Command;
import it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.DettaglioFascicoloGenericoPresenter.DettaglioFascicoloCollegatoCommand;
import it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.DettaglioFascicoloGenericoPresenter.DownloadAllegatoCommand;
import it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.DettaglioFascicoloGenericoPresenter.MostraDettaglioAllegatoCommand;
import it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.DettaglioFascicoloGenericoPresenter.MostraDettaglioBozzaCommand;
import it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.DettaglioFascicoloGenericoPresenter.MostraDettaglioEmailCommand;
import it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.command.GoToDettaglioModuloCommand;
import it.eng.portlet.consolepec.gwt.client.widget.composizione.AllegatoComposizioneWidget;
import it.eng.portlet.consolepec.gwt.client.widget.composizione.PecComposizioneWidget;
import it.eng.portlet.consolepec.gwt.client.widget.composizione.PecComposizioneWidget.MostraPEC;
import it.eng.portlet.consolepec.gwt.client.widget.composizione.PraticaModulisticaComposizioneWidget;
import it.eng.portlet.consolepec.gwt.client.widget.composizione.PraticaModulisticaComposizioneWidget.MostraPraticaModulistica;
import it.eng.portlet.consolepec.gwt.client.widget.composizione.SlideAnimation;
import it.eng.portlet.consolepec.gwt.client.widget.images.ConsolePECIcons;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.TextUtils;
import it.eng.portlet.consolepec.gwt.shared.action.pec.TipoRiferimentoPEC;
import it.eng.portlet.consolepec.gwt.shared.dto.CollegamentoDto;
import it.eng.portlet.consolepec.gwt.shared.model.AllegatoDTO;
import it.eng.portlet.consolepec.gwt.shared.model.PecDTO;
import it.eng.portlet.consolepec.gwt.shared.model.PraticaModulisticaDTO;

public class ElementoFascicoloCollegato extends Composite {

	private static ElementoFascicoloCollegatoUiBinder uiBinder = GWT.create(ElementoFascicoloCollegatoUiBinder.class);

	interface ElementoFascicoloCollegatoUiBinder extends UiBinder<Widget, ElementoFascicoloCollegato> {/**/}

	public ElementoFascicoloCollegato() {
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
	Anchor linkFascicoloCollegato;
	@UiField
	Button mostraDettaglioFascicoloCollegato;

	private HTMLPanel dettaglioPanel;
	private SlideAnimation slideAnimation;

	public ElementoFascicoloCollegato(final CollegamentoDto collegamentoDto, final it.eng.portlet.consolepec.gwt.client.presenter.Command<Void, CollegamentoDto> linkCommand,
			final it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.DettaglioFascicoloGenericoPresenter.DettaglioFascicoloCollegatoCommand dettaglioFascicoloCollegatoCommand,
			final ClickHandler checkBoxClickHandler) {
		initWidget(uiBinder.createAndBindUi(this));
		setCheckBoxVisibility(true);
		String oggetto = collegamentoDto.getOggetto();
		if (oggetto.length() > ConsolePecConstants.DESCR_WIDGET_ELEMENTI_MAX_LEN) {
			oggetto = collegamentoDto.getOggetto().substring(0, ConsolePecConstants.DESCR_WIDGET_ELEMENTI_MAX_LEN).concat("...");
			SpanElement labelDettaglio = Document.get().createSpanElement();
			labelDettaglio.setClassName("documento");
			getElement().appendChild(labelDettaglio);
		}
		StringBuilder sb = new StringBuilder(oggetto);
		linkFascicoloCollegato.setText(sb.append(" (" + TextUtils.getIdDocumentaleFromClientID(collegamentoDto.getClientId()) + ")").toString());
		iconaLabel.setResource(ConsolePECIcons._instance.fascicoloBa01());

		if (collegamentoDto.isAccessibileInLettura()) {
			checkBox.setEnabled(true);
			linkFascicoloCollegato.setHref("javascript:;");
			linkFascicoloCollegato.setTitle(collegamentoDto.getOggetto());
			linkFascicoloCollegato.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					linkCommand.exe(collegamentoDto);
					Window.scrollTo(0, 0);
				}
			});
		} else {
			linkFascicoloCollegato.getElement().setPropertyString("style", "color: darkgray;");
			checkBox.setEnabled(false);
		}

		checkBox.addClickHandler(checkBoxClickHandler);

		if (dettaglioFascicoloCollegatoCommand != null && collegamentoDto.isAccessibileInLettura()) {

			dettaglioPanel = new HTMLPanel("");
			dettaglioPanel.setVisible(false);
			dettaglioPanel.setStyleName("box-mail last");
			slideAnimation = new SlideAnimation(dettaglioPanel);

			mainPanel.add(dettaglioPanel);

			if (collegamentoDto.getAllegati().isEmpty() && collegamentoDto.getElencoPEC().isEmpty() && collegamentoDto.getElencoPraticheModulistica().isEmpty()) {
				mostraDettaglioFascicoloCollegato.setEnabled(false);
			}

			mostraDettaglioFascicoloCollegato.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					runMostraDettaglioFascicoloCollegatoCommand(dettaglioFascicoloCollegatoCommand, collegamentoDto);
				}
			});

		} else {
			mostraDettaglioFascicoloCollegato.setEnabled(false);
		}

	}

	private void runMostraDettaglioFascicoloCollegatoCommand(DettaglioFascicoloCollegatoCommand dettaglioFascicoloCollegatoCommand, CollegamentoDto collegamentoDto) {
		dettaglioFascicoloCollegatoCommand.exe(this, collegamentoDto);
	}

	public void setCheckBoxVisibility(boolean visible) {
		checkBox.setVisible(visible);
	}

	public void addAllegatoWidget(AllegatoDTO allegato, final String clientID, final DownloadAllegatoCommand downloadAllegatoCommand,
			final MostraDettaglioAllegatoCommand mostraDettaglioAllegatoCommand) {
		final AllegatoComposizioneWidget rigaAllegato = new AllegatoComposizioneWidget();
		rigaAllegato.setCheckBoxVisible(false);

		rigaAllegato.setDownloadCommand(new Command<Void, AllegatoDTO>() {
			@Override
			public Void exe(AllegatoDTO t) {
				downloadAllegatoCommand.exe(t);
				return null;
			}
		});

		rigaAllegato.setMostraCommand(new it.eng.portlet.consolepec.gwt.client.presenter.Command<Void, AllegatoDTO>() {
			@Override
			public Void exe(AllegatoDTO allegato) {
				mostraDettaglioAllegatoCommand.exe(clientID, allegato);
				return null;
			}
		});

		dettaglioPanel.add(rigaAllegato);
		rigaAllegato.mostraDettaglio(allegato);

	}

	public void addPecWidget(final PecDTO pec, final DownloadAllegatoCommand downloadAllegatoCommand, final MostraDettaglioAllegatoCommand mostraDettaglioAllegatoCommand,
			final MostraDettaglioEmailCommand mostraDettaglioEmailCommand, final MostraDettaglioBozzaCommand mostraDettaglioBozzaCommand) {
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

	public void addPraticaModulisticaWidget(final PraticaModulisticaDTO praticaModulistica, final DownloadAllegatoCommand downloadAllegatoCommand,
			final MostraDettaglioAllegatoCommand mostraDettaglioAllegatoCommand, final GoToDettaglioModuloCommand goToDettaglioModuloCommand) {
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
