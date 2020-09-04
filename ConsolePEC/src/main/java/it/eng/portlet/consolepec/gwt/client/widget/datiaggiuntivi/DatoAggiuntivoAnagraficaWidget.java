package it.eng.portlet.consolepec.gwt.client.widget.datiaggiuntivi;

import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.Presenter;

import it.eng.cobo.consolepec.commons.rubrica.FiltriRicerca;
import it.eng.portlet.consolepec.gwt.client.presenter.rubrica.command.DettaglioAnagraficaCommand;
import it.eng.portlet.consolepec.gwt.client.presenter.rubrica.command.SelezionaAnagraficaCommand;
import it.eng.portlet.consolepec.gwt.shared.action.rubrica.RicercaAnagrafiche;
import it.eng.portlet.consolepec.gwt.shared.action.rubrica.RicercaAnagraficheResult;

public class DatoAggiuntivoAnagraficaWidget extends Composite {

	private final Label textBox = new Label();
	private Button button = new Button();
	private Double idAnagrafica;
	private String valoreDatoAggiuntivo = null;
	private boolean anagraficaSelezionata = false;

	private HTML linkText = null;

	private final DispatchAsync dispatch;

	final DettaglioAnagraficaCommand dettaglioAnagraficaCommand;

	private void goToAnagrafica(Double idAnagrafica) {

		RicercaAnagrafiche ricercaAnagrafiche = new RicercaAnagrafiche();
		ricercaAnagrafiche.getFiltri().put(FiltriRicerca.IDENTIFICATIVO, idAnagrafica);
		this.dispatch.execute(ricercaAnagrafiche, new AsyncCallback<RicercaAnagraficheResult>() {

			@Override
			public void onFailure(Throwable caught) {
				// FIXME
			}

			@Override
			public void onSuccess(RicercaAnagraficheResult result) {
				if (result.isError()) {
					// FIXME
				} else {
					dettaglioAnagraficaCommand.setAnagrafica(result.getAnagrafiche().get(0));
					dettaglioAnagraficaCommand.execute();
				}
			}
		});
	}

	public DatoAggiuntivoAnagraficaWidget(String valoreDatoAggiuntivo, final Command selezionaAnagraficaCommand, boolean navigabile, DispatchAsync dispatch,
			final FormDatiAggiuntiviWidget formDatiAggiuntiviWidget) {
		HorizontalPanel panel = new HorizontalPanel();
		Presenter<?, ?> presenter = (Presenter<?, ?>) ((SelezionaAnagraficaCommand) selezionaAnagraficaCommand).getOpeningRequestor();
		this.dispatch = dispatch;
		dettaglioAnagraficaCommand = new DettaglioAnagraficaCommand(presenter);

		linkText = new HTML("");
		linkText.getElement().getStyle().setMarginLeft(10, Unit.PX);
		linkText.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent paramClickEvent) {

				if (idAnagrafica != null) goToAnagrafica(idAnagrafica);
			}
		});

		textBox.setText("Anagrafica obbligatoria");
		textBox.getElement().getStyle().setDisplay(Display.NONE);
		textBox.getElement().getStyle().setColor("#870000");

		setValoreDatoAggiuntivo(valoreDatoAggiuntivo);

		panel.add(button);
		panel.add(linkText);
		panel.add(textBox);

		if (this.valoreDatoAggiuntivo == null) {
			anagraficaSelezionata = false;
			button.setText("Seleziona");
		} else {
			anagraficaSelezionata = true;
			button.setText("Rimuovi");
		}

		button.setEnabled(true);
		button.setStyleName("btn");
		button.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent paramClickEvent) {
				if (anagraficaSelezionata) {
					setValoreDatoAggiuntivo(null);
					formDatiAggiuntiviWidget.setAnagrafica(((SelezionaAnagraficaCommand) selezionaAnagraficaCommand).getNomeDatoAggiuntivo(), null, null);
				} else {
					if (selezionaAnagraficaCommand != null) {
						selezionaAnagraficaCommand.execute();
					}
				}
			}
		});
		initWidget(panel);

	}

	public void validate(boolean bool) {
		if (bool) {
			textBox.getElement().getStyle().setDisplay(Display.NONE);
		} else {
			textBox.getElement().getStyle().setDisplay(Display.BLOCK);
		}
	}

	public void setValoreDatoAggiuntivo(String valoreDatoAggiuntivo) {
		textBox.getElement().getStyle().setDisplay(Display.NONE);
		this.valoreDatoAggiuntivo = valoreDatoAggiuntivo;
		String valoreVisualizzato = valoreDatoAggiuntivo != null ? valoreDatoAggiuntivo : "";
		SafeHtmlBuilder sb = new SafeHtmlBuilder();
		sb.appendHtmlConstant("<a href=\"javascript:;\">" + valoreVisualizzato + "</a>");
		linkText.setHTML(sb.toSafeHtml());
	}

	public void setEditable(boolean enabled) {
		button.setEnabled(enabled);
	}

	public String getValoreDatoAggiuntivo() {
		return valoreDatoAggiuntivo;
	}

	public void setIdAnagrafica(Double idAnagrafica) {
		this.idAnagrafica = idAnagrafica;
	}

	public Double getIdAnagrafica() {
		return idAnagrafica;
	}

	public Button getButton() {
		return button;
	}

	public void setButton(Button button) {
		this.button = button;
	}

	public boolean isAnagraficaSelezionata() {
		return anagraficaSelezionata;
	}

	public void setAnagraficaSelezionata(boolean anagraficaSelezionata) {
		this.anagraficaSelezionata = anagraficaSelezionata;
	}

}
