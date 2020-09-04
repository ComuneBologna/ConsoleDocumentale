package it.eng.portlet.consolepec.gwt.client.widget;

import it.eng.portlet.consolepec.gwt.client.presenter.Command;
import it.eng.portlet.consolepec.gwt.client.widget.images.ConsolePECIcons;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.model.ProcedimentoMiniDto;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Widget;

public class ElementoProcedimentoElencoWidget extends Composite {

	private static ElementoProcedimentoElencoWidgetUiBinder uiBinder = GWT.create(ElementoProcedimentoElencoWidgetUiBinder.class);

	interface ElementoProcedimentoElencoWidgetUiBinder extends UiBinder<Widget, ElementoProcedimentoElencoWidget> {
	}

	@UiField
	HTMLPanel mainPanel;
	@UiField
	CheckBox checkBox;
	@UiField
	InlineLabel labelProcedimento;
	@UiField
	Image iconaLabel;
	
	private boolean mostraData;
	private boolean checkBoxEnabled;

	private Command<Void, SelezioneProcedimento> selezionaProcedimentoCommand;

	public ElementoProcedimentoElencoWidget() {
		super();
		initWidget(uiBinder.createAndBindUi(this));
	}

	public void setCheckBoxVisible(boolean show) {
		checkBox.setVisible(show);
		if (show) 
			checkBoxEnabled = true; // se è visibile di defaul lo abilito;
	}
	
	public void setCheckBoxEnabled(boolean enabled) {
		checkBoxEnabled = enabled;
	}

	public void mostraDettaglio(final ProcedimentoMiniDto procedimento) {
		
		checkBox.setEnabled(checkBoxEnabled && selezionaProcedimentoCommand != null);
		
		if (checkBox.isEnabled()) {
			checkBox.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					boolean checked = ((CheckBox) event.getSource()).getValue();
					SelezioneProcedimento sp = new SelezioneProcedimento();
					sp.setProcedimento(procedimento);
					sp.setChecked(checked);
					selezionaProcedimentoCommand.exe(sp);
				}
			});
		}
		iconaLabel.setResource(ConsolePECIcons._instance.procedimento());
		StringBuffer sb = new StringBuffer(procedimento.getCodTipologiaProcedimento() + " - " + procedimento.getDescrTipologiaProcedimento());
		if(mostraData && procedimento.getDataInizioDecorrenzaProcedimento() != null)
			sb.append(" (").append(procedimento.getDataInizioDecorrenzaProcedimento()).append(")");
		String descrizione = sb.toString();
		if (descrizione.length() > ConsolePecConstants.DESCR_WIDGET_ELEMENTI_MAX_LEN)
			descrizione = descrizione.substring(0, ConsolePecConstants.DESCR_WIDGET_ELEMENTI_MAX_LEN).concat("...");
		labelProcedimento.setText(descrizione);
	}

	public Command<Void, SelezioneProcedimento> getSelezionaProcedimentoCommand() {
		return selezionaProcedimentoCommand;
	}

	public void setSelezionaProcedimentoCommand(Command<Void, SelezioneProcedimento> selezionaProcedimentoCommand) {
		this.selezionaProcedimentoCommand = selezionaProcedimentoCommand;
	}

	public class SelezioneProcedimento {
		
		private ProcedimentoMiniDto procedimento;
		private boolean checked;

		public ProcedimentoMiniDto getProcedimento() {
			return procedimento;
		}

		public void setProcedimento(ProcedimentoMiniDto procedimento) {
			this.procedimento = procedimento;
		}

		public boolean isChecked() {
			return checked;
		}

		public void setChecked(boolean checked) {
			this.checked = checked;
		}
	}

	public void setMostraData(boolean mostraData) {
		this.mostraData = mostraData;
	}
}
