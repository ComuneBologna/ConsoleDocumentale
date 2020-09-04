package it.eng.portlet.consolepec.gwt.client.view.protocollazione;

import it.eng.cobo.consolepec.util.console.ConsoleConstants;
import it.eng.portlet.consolepec.gwt.client.presenter.protocollazione.RiepilogoAvvioChiusuraProcedimentoPresenter;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.GestioneProcedimentoResult;

import java.util.Date;

import com.google.gwt.dom.client.HeadingElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;

public class RiepilogoAvvioChiusuraProcedimentoView extends ViewImpl implements RiepilogoAvvioChiusuraProcedimentoPresenter.MyView {

	private static DateTimeFormat DTF = DateTimeFormat.getFormat(ConsoleConstants.FORMATO_DATA);
	
	@UiField
	HTMLPanel riepilogoProcedimentoPanel;
	@UiField
	Button chiudiButton;
	@UiField
	HeadingElement titolo;
	
	private final Widget widget;

	public interface Binder extends UiBinder<Widget, RiepilogoAvvioChiusuraProcedimentoView> {
	}

	@Inject
	public RiepilogoAvvioChiusuraProcedimentoView(final Binder binder) {
		widget = binder.createAndBindUi(this);
	}

	@Override
	public Widget asWidget() {
		return widget;
	}
	
	@Override
	public void initRiepilogo(GestioneProcedimentoResult avvioProcedimentoResult){
		if (avvioProcedimentoResult.getDataChiusuraProcedimento() == null)
			titolo.setInnerText("Avvio del procedimento avvenuto correttamente");
		else 
			titolo.setInnerText("Chiusura del procedimento avvenuta correttamente");
		riepilogoProcedimentoPanel.clear();
		riepilogoProcedimentoPanel.getElement().setInnerHTML("");
		riepilogoProcedimentoPanel.getElement().setInnerHTML(getHtmlRiepilogo(avvioProcedimentoResult));
	}
	
	private String getHtmlRiepilogo(GestioneProcedimentoResult result) {
		StringBuilder sb = new StringBuilder();
		sb.append(" <div class='cell'><span class='label'>Codice: </span>" + "<div class='abstract'><span>" + checkString("" + result.getCodiceProcedimento()) + " </span></div></div>");
		sb.append(" <div class='cell'><span class='label'>Descrizione: </span>" + "<div class='abstract'><span>" + checkString(result.getDescrizioneProcedimento()) + " </span></div></div>");
		sb.append(" <div class='cell'><span class='label'>Inizio decorrenza: </span>" + "<div class='abstract'><span>" + checkDate(result.getDataInizioProcedimento()) + " </span></div></div>");
		if(result.getDataChiusuraProcedimento() != null)
			sb.append(" <div class='cell'><span class='label'>Chiusura: </span>" + "<div class='abstract'><span>" + checkDate(result.getDataChiusuraProcedimento()) + " </span></div></div>");
		if(result.getTempoNormatoInGiorni() != null)
			sb.append(" <div class='cell'><span class='label'>Termine normato: </span>" + "<div class='abstract'><span>" + checkString("" + result.getTempoNormatoInGiorni()) + " </span></div></div>");
		if(result.getDurataProcedimento() != null)
			sb.append(" <div class='cell'><span class='label'>Durata: </span>" + "<div class='abstract'><span>" + checkString("" + result.getDurataProcedimento()) + " </span></div></div>");
		return sb.toString();
	}
	
	@Override
	public void setChiudiCommand(final Command chiudiCommand){
		chiudiButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				chiudiCommand.execute();
			}
		});
	}
	
	private String checkDate(Date data) {
		if (data == null)
			return " - ";
		return DTF.format(data);
	}

	private String checkString(String value) {
		if (value == null)
			return " - ";
		return value.trim();
	}
}
