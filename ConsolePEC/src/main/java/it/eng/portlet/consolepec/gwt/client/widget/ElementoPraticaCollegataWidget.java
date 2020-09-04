package it.eng.portlet.consolepec.gwt.client.widget;

import it.eng.cobo.consolepec.commons.configurazioni.TipologiaPratica;
import it.eng.cobo.consolepec.util.pratica.PraticaUtil;
import it.eng.portlet.consolepec.gwt.client.presenter.Command;
import it.eng.portlet.consolepec.gwt.client.widget.images.ConsolePECIcons;
import it.eng.portlet.consolepec.gwt.shared.TextUtils;
import it.eng.portlet.consolepec.gwt.shared.model.PraticaDTO;

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
import com.google.gwt.user.client.ui.Widget;

public class ElementoPraticaCollegataWidget extends Composite {

	private static ElementoPraticaCollegataUiBinder uiBinder = GWT.create(ElementoPraticaCollegataUiBinder.class);

	interface ElementoPraticaCollegataUiBinder extends UiBinder<Widget, ElementoPraticaCollegataWidget> {
	}

	@UiField
	HTMLPanel mainPanel;
	@UiField
	CheckBox checkBox;
	@UiField
	Anchor linkFascicoloCollegato;
	@UiField
	Image iconaLabel;

	private Command<Void, PraticaDTO> command;
	private boolean checkBoxVisible;
	private boolean checkBoxEnabled;

	public ElementoPraticaCollegataWidget() {
		super();
		initWidget(uiBinder.createAndBindUi(this));
	}

	public void mostraDettaglio(final PraticaDTO pratica) {

		StringBuilder sb = new StringBuilder(pratica.getTitolo());
		linkFascicoloCollegato.setText(sb.append(" (" + TextUtils.getIdDocumentaleFromClientID(pratica.getClientID()) + ")").toString());
		iconaLabel.setResource(getIcon(pratica.getTipologiaPratica()));
		linkFascicoloCollegato.setHref("javascript:;");
		linkFascicoloCollegato.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				command.exe(pratica);
			}
		});
		checkBox.setVisible(checkBoxVisible);
		checkBox.setEnabled(checkBoxEnabled);
	}

	protected ImageResource getIcon(TipologiaPratica tp) {
		if (PraticaUtil.isEmailOut(tp) || PraticaUtil.isIngresso(tp))
			return ConsolePECIcons._instance.bustinaApertaEmail();
		else
			return ConsolePECIcons._instance.fascicolo();
	}

	public void setCheckBoxVisible(boolean show) {
		checkBoxVisible = show;
	}

	public void setCheckBoxEnabled(boolean enabled) {
		checkBoxEnabled = enabled;
	}
	
	public void setCheckBoxClickHandler(ClickHandler clickHandler){
		checkBox.addClickHandler(clickHandler);
	}
	
	public void setCommand(Command<Void, PraticaDTO> command){
		this.command = command;
	}

}
