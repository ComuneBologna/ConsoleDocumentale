package it.eng.portlet.consolepec.gwt.client.widget.operazioni;

import it.eng.portlet.consolepec.gwt.client.view.OpenCloseImagePanel;
import it.eng.portlet.consolepec.gwt.client.widget.operazioni.OperazioneWidget.SelezionaOperazione;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Widget;

public class ElencoOperazioniWidget extends Composite {

	private static ElencoOperazioniWidgetUiBinder uiBinder = GWT.create(ElencoOperazioniWidgetUiBinder.class);

	interface ElencoOperazioniWidgetUiBinder extends UiBinder<Widget, ElencoOperazioniWidget> {
	}
	
	@UiField
	HTMLPanel elencoPanel;
	@UiField(provided = true)
	DisclosurePanel disclosurePanel = new DisclosurePanel(OpenCloseImagePanel.DEFAULT_IMAGES.disclosurePanelOpen(), OpenCloseImagePanel.DEFAULT_IMAGES.disclosurePanelClosed(), "Operazioni");
	@UiField
	InlineLabel titoloLabel;
	@UiField
	Image iconaImage;
	@UiField
	Button eliminaButton;

	private List<String> operazioniSelezionate = new ArrayList<String>();
	private Command onSelezioneCommand;
	private Command onEliminaCommand;

	public ElencoOperazioniWidget() {
		super();
		initWidget(uiBinder.createAndBindUi(this));
	}

	public void init(String titolo, ImageResource imageResource, HashMap<String, Boolean> operazioni, boolean mostraOperazioni, boolean mostraPulsanteElimina) {
		titoloLabel.setText(titolo);
		iconaImage.setResource(imageResource);
		iconaImage.setTitle(titolo);
		
		TreeSet<String> keySet = new TreeSet<String>();
		keySet.addAll(operazioni.keySet());
		
		for (String operazione : keySet) {
			String operazioneLeggibile = formattaOperazione(operazione);
			OperazioneWidget operazioneWidget = new OperazioneWidget();
			operazioneWidget.setChecked(operazioni.get(operazione));
			operazioneWidget.setSelezionaOperazioneCommand(new it.eng.portlet.consolepec.gwt.client.presenter.Command<Void, OperazioneWidget.SelezionaOperazione>() {

				@Override
				public Void exe(SelezionaOperazione so) {
					if (so.isChecked()) {
						operazioniSelezionate.add(ripristinaOperazione(so.getOperazione()));
					} else {
						operazioniSelezionate.remove(ripristinaOperazione(so.getOperazione()));
					}
					if(onSelezioneCommand != null)
						onSelezioneCommand.execute();
					return null;
				}

			});
			if(operazioni.get(operazione))
				operazioniSelezionate.add(operazione);
			operazioneWidget.mostraDettaglio(operazioneLeggibile);
			elencoPanel.add(operazioneWidget);
			disclosurePanel.setOpen(mostraOperazioni);
		}
		eliminaButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				onEliminaCommand.execute();
			}
		});
		eliminaButton.setVisible(mostraPulsanteElimina);
	}
	
	public void setOnSelezioneCommand(Command onSelezioneCommand){
		this.onSelezioneCommand = onSelezioneCommand;
	}

	private String ripristinaOperazione(String operazione) {
		String[] tokens = operazione.split(" ");
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < tokens.length; i++) {
			sb.append(tokens[i].toUpperCase());
			if (i != (tokens.length - 1))
				sb.append("_");
		}
		return sb.toString();
	}

	private String formattaOperazione(String operazione) {
		String[] tokens = operazione.split("_");
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < tokens.length; i++) {
			sb.append(tokens[i].substring(0, 1).toUpperCase());
			sb.append(tokens[i].substring(1).toLowerCase());
			if (i != (tokens.length - 1))
				sb.append(" ");
		}
		return sb.toString();
	}

	public List<String> getOperazioniSelezionate() {
		return operazioniSelezionate;
	}

	public void setOnEliminaCommand(Command onEliminaCommand) {
		this.onEliminaCommand = onEliminaCommand;
	}
	
	public void attivaPulsanteElimina(boolean enabled){
		eliminaButton.setEnabled(enabled);
	}

}
