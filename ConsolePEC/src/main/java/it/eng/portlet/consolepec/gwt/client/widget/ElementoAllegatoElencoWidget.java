package it.eng.portlet.consolepec.gwt.client.widget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Widget;

import it.eng.portlet.consolepec.gwt.client.presenter.Command;
import it.eng.portlet.consolepec.gwt.client.widget.images.ConsolePECIcons;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecUtils;
import it.eng.portlet.consolepec.gwt.shared.model.AllegatoDTO;
import it.eng.portlet.consolepec.gwt.shared.model.FileDTO;

public class ElementoAllegatoElencoWidget extends Composite {

	private static ElementoAllegatoElencoWidgetUiBinder uiBinder = GWT.create(ElementoAllegatoElencoWidgetUiBinder.class);

	interface ElementoAllegatoElencoWidgetUiBinder extends UiBinder<Widget, ElementoAllegatoElencoWidget> {}

	@UiField
	HTMLPanel mainPanel;
	@UiField
	CheckBox checkBox;
	@UiField
	Anchor linkFirma;
	@UiField
	Anchor linkDownload;
	@UiField
	InlineLabel labelAllegato;
	@UiField
	Image iconaLabel;

	private Command<Void, AllegatoDTO> mostraDettaglioAllegatoDTOCommand;
	private Command<Void, AllegatoDTO> downloadAllegatoDTOCommand;
	private Command<Void, SelezioneAllegato> selezionaAllegatoDTOCommand;

	private Command<Void, SelezioneFile> selezionaFileCommand;

	public ElementoAllegatoElencoWidget() {
		super();
		initWidget(uiBinder.createAndBindUi(this));
	}

	public void setCheckBoxVisible(boolean show) {
		checkBox.setVisible(show);
	}

	public void setCheckBoxEnabled(boolean show) {
		checkBox.setEnabled(show);
	}

	public void mostraDettaglio(final FileDTO file) {

		checkBox.setEnabled(selezionaFileCommand != null);
		if (checkBox.isEnabled()) {
			checkBox.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					boolean checked = ((CheckBox) event.getSource()).getValue();

					SelezioneFile sf = new SelezioneFile();
					sf.setFile(file);
					sf.setChecked(checked);

					selezionaFileCommand.exe(sf);
				}
			});
		}

		ImageResource imgResource = null;

		if (file.getInformazioniFirmaDigitale() != null && file.getInformazioniFirmaDigitale().getStatoFirma() != null) {
			switch (file.getInformazioniFirmaDigitale().getStatoFirma()) {
			case FIRMATO:
				imgResource = ConsolePECIcons._instance.firmato();
				break;
			case FIRMANONVALIDA:
				imgResource = ConsolePECIcons._instance.firmanonvalida();
				break;
			case NONFIRMATO:
				imgResource = ConsolePECIcons._instance.nonfirmato();
				break;
			}
		} else {
			imgResource = ConsolePECIcons._instance.nonfirmato();
		}

		Image iconaLink = new Image(imgResource);
		linkFirma.getElement().appendChild(iconaLink.getElement());

		String label = file.getNome();
		linkDownload.setVisible(false);
		labelAllegato.setVisible(true);
		labelAllegato.setText(label);
	}

	public void mostraDettaglio(final AllegatoDTO allegato) {
		/* aggiunta checkbox di selezione */
		checkBox.setEnabled(selezionaAllegatoDTOCommand != null);
		if (checkBox.isEnabled()) {
			checkBox.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					boolean checked = ((CheckBox) event.getSource()).getValue();
					SelezioneAllegato sa = new SelezioneAllegato();
					sa.allegato = allegato;
					sa.checked = checked;
					selezionaAllegatoDTOCommand.exe(sa);
				}
			});
		}
		/* aggiunta icona di firma */
		// Image iconaFirma = new Image();

		if (mostraDettaglioAllegatoDTOCommand != null) {
			linkFirma.setTitle("Visualizza dettagli");
		}
		linkFirma.setHref("javascript:;");
		linkFirma.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (mostraDettaglioAllegatoDTOCommand != null) {
					ElementoAllegatoElencoWidget.this.mostraDettaglioAllegatoDTOCommand.exe(allegato);
				}
			}
		});
		Image iconaLink = new Image(allegato.getIconaStato(ConsolePECIcons._instance));
		linkFirma.getElement().appendChild(iconaLink.getElement());

		String label = ConsolePecUtils.getLabelExtended(allegato);
		if (downloadAllegatoDTOCommand != null) {
			labelAllegato.setVisible(false);
			linkDownload.setVisible(true);
			linkDownload.setHref("javascript:;");
			linkDownload.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					downloadAllegatoDTOCommand.exe(allegato);
				}
			});
			linkDownload.setTitle(allegato.getLabel());
			linkDownload.setText(label);
		} else {
			linkDownload.setVisible(false);
			labelAllegato.setVisible(true);
			labelAllegato.setText(label);
		}
	}

	public Command<Void, AllegatoDTO> getDownloadAllegatoCommand() {
		return downloadAllegatoDTOCommand;
	}

	public void setDownloadAllegatoCommand(Command<Void, AllegatoDTO> downloadAllegatoCommand) {
		this.downloadAllegatoDTOCommand = downloadAllegatoCommand;
	}

	public Command<Void, AllegatoDTO> getMostraDettaglioAllegatoCommand() {
		return mostraDettaglioAllegatoDTOCommand;
	}

	public void setMostraDettaglioAllegatoCommand(Command<Void, AllegatoDTO> mostraDettaglioAllegatoCommand) {
		this.mostraDettaglioAllegatoDTOCommand = mostraDettaglioAllegatoCommand;
	}

	public Command<Void, SelezioneAllegato> getSelezionaAllegatoCommand() {
		return selezionaAllegatoDTOCommand;
	}

	public void setSelezionaAllegatoCommand(Command<Void, SelezioneAllegato> selezionaAllegatoCommand) {
		this.selezionaAllegatoDTOCommand = selezionaAllegatoCommand;
	}

	public Command<Void, SelezioneFile> getSelezionaFileCommand() {
		return selezionaFileCommand;
	}

	public void setSelezionaFileCommand(Command<Void, SelezioneFile> selezionaFileCommand) {
		this.selezionaFileCommand = selezionaFileCommand;
	}

	public class SelezioneAllegato {
		private AllegatoDTO allegato;
		private boolean checked;

		public AllegatoDTO getAllegato() {
			return allegato;
		}

		public void setAllegato(AllegatoDTO allegato) {
			this.allegato = allegato;
		}

		public boolean isChecked() {
			return checked;
		}

		public void setChecked(boolean checked) {
			this.checked = checked;
		}
	}

	public class SelezioneFile {
		private FileDTO file;
		private boolean checked;
		private String nomeAllegato;

		public String getNomeAllegato() {
			return nomeAllegato;
		}

		public void setNomeAllegato(String nomeAllegato) {
			this.nomeAllegato = nomeAllegato;
		}

		public boolean isChecked() {
			return checked;
		}

		public void setChecked(boolean checked) {
			this.checked = checked;
		}

		public FileDTO getFile() {
			return file;
		}

		public void setFile(FileDTO file) {
			this.file = file;
		}
	}
}
