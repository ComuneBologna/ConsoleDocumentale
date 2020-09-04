package it.eng.portlet.consolepec.gwt.client.widget.operazioni;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Widget;

import it.eng.portlet.consolepec.gwt.client.presenter.Command;

public class OperazioneWidget extends Composite {

	private static OperazioneWidgetUiBinder uiBinder = GWT.create(OperazioneWidgetUiBinder.class);

	interface OperazioneWidgetUiBinder extends UiBinder<Widget, OperazioneWidget> {/**/}

	@UiField
	HTMLPanel operazionePanel;
	@UiField
	CheckBox checkBox;
	@UiField
	InlineLabel labelOperazione;

	boolean checked;

	private Command<Void, SelezionaOperazione> selezionaOperazioneCommand;

	public OperazioneWidget() {
		super();
		initWidget(uiBinder.createAndBindUi(this));
	}

	public void mostraDettaglio(final String operazione) {
		/* aggiunta checkbox di selezione */
		checkBox.setEnabled(selezionaOperazioneCommand != null);
		if (checkBox.isEnabled()) {
			checkBox.setValue(checked);
			checkBox.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
				@Override
				public void onValueChange(ValueChangeEvent<Boolean> event) {
					boolean checked = event.getValue();
					SelezionaOperazione so = new SelezionaOperazione();
					so.setOperazione(operazione);
					so.setChecked(checked);
					selezionaOperazioneCommand.exe(so);
				}
			});
		}
		labelOperazione.setVisible(true);
		labelOperazione.setText(operazione);
	}

	public Command<Void, SelezionaOperazione> getSelezionaOperazioneCommand() {
		return selezionaOperazioneCommand;
	}

	public void setSelezionaOperazioneCommand(Command<Void, SelezionaOperazione> selezionaOperazioneCommand) {
		this.selezionaOperazioneCommand = selezionaOperazioneCommand;
	}

	public class SelezionaOperazione {

		private String operazione;
		private boolean checked;

		public String getOperazione() {
			return operazione;
		}

		public void setOperazione(String operazione) {
			this.operazione = operazione;
		}

		public boolean isChecked() {
			return checked;
		}

		public void setChecked(boolean checked) {
			this.checked = checked;
		}
	}

	public void setChecked(Boolean checked) {
		this.checked = checked;
	}

}
