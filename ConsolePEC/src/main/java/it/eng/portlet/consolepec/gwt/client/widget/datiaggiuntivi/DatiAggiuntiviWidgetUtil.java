package it.eng.portlet.consolepec.gwt.client.widget.datiaggiuntivi;

import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.DatoAggiuntivo;
import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.DatoAggiuntivoAnagrafica;
import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.DatoAggiuntivoTabella;
import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.DatoAggiuntivoValoreMultiplo;
import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.DatoAggiuntivoValoreSingolo;
import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.DatoAggiuntivo.DatoAggiuntivoVisitor;
import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.tabella.ValoreCellaDatoAggiuntivo;
import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.tabella.ValoreCellaDatoAggiuntivo.ValoreCellaDatoAggiuntivoAnagrafica;
import it.eng.cobo.consolepec.util.console.ConsoleConstants;
import it.eng.cobo.consolepec.util.datiaggiuntivi.DatiAggiuntiviUtil;
import it.eng.cobo.consolepec.util.generics.GenericsUtil;
import it.eng.cobo.consolepec.util.objects.Ref;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.gwt.user.datepicker.client.DateBox.Format;

public class DatiAggiuntiviWidgetUtil extends DatiAggiuntiviUtil {
	
	private static DateTimeFormat dateFormat = DateTimeFormat.getFormat(ConsoleConstants.FORMATO_DATA);

	public static void svuotaCampi(Map<String, Widget> campi) {
		
		for (Entry<String, Widget> entry : campi.entrySet()) {
			Widget campo = entry.getValue();
			if (campo instanceof TextBox)
				((TextBox) campo).setValue(null);
			if (campo instanceof SuggestBox)
				((SuggestBox) campo).setValue(null);
			if (campo instanceof DateBox)
				((DateBox) campo).setValue(null);
			if (campo instanceof DatiAggiuntiviNumberBox)
				((DatiAggiuntiviNumberBox) campo).setValue(null);
			if (campo instanceof DatiAggiuntiviListaBox)
				((DatiAggiuntiviListaBox) campo).setValue(null);
			if (campo instanceof Label && ((Label) campo).getText() != null && !((Label) campo).getText().trim().equals(""))
				((Label) campo).setText(null);
			if (campo instanceof DatoAggiuntivoMultiploWidget)
				((DatoAggiuntivoMultiploWidget) campo).reset();
			if (campo instanceof DatoAggiuntivoAnagraficaWidget){
				((DatoAggiuntivoAnagraficaWidget) campo).setValoreDatoAggiuntivo(null);
				((DatoAggiuntivoAnagraficaWidget) campo).setIdAnagrafica(null);
			}
		}
	}
	
	public static ValoreCellaDatoAggiuntivo calcolaValoreCella(DatoAggiuntivo da, final Widget campo){
		final Ref<ValoreCellaDatoAggiuntivo> vc = Ref.of(null);
		da.accept(new DatoAggiuntivoVisitor() {
			
			@Override
			public void visit(DatoAggiuntivoTabella d) {
				throw new UnsupportedOperationException();
			}
			
			@Override
			public void visit(DatoAggiuntivoAnagrafica d) {
				String valore = ((DatoAggiuntivoAnagraficaWidget) campo).getValoreDatoAggiuntivo();
				Double idAnagraica = ((DatoAggiuntivoAnagraficaWidget) campo).getIdAnagrafica();
				
				vc.set(new ValoreCellaDatoAggiuntivoAnagrafica(idAnagraica, valore));
			}
			
			@Override
			public void visit(DatoAggiuntivoValoreMultiplo d) {
				List<String> valori = new ArrayList<String>();
				valori.addAll(((DatoAggiuntivoMultiploWidget) campo).getItemSelected());
				
				vc.set(new ValoreCellaDatoAggiuntivo.ValoreCellaDatoAggiuntivoMultiplo(valori));
			}
				
			@Override
			public void visit(DatoAggiuntivoValoreSingolo d) {
				String valore = null;
				if (campo instanceof TextBox) {
					valore = GenericsUtil.normalizzaValoreTesto(((TextBox) campo).getValue());
				}
				if (campo instanceof SuggestBox) {
					valore = GenericsUtil.normalizzaValoreTesto(((SuggestBox) campo).getValue());
				}
				if (campo instanceof DateBox) {
					valore = ConsolePecUtils.normalizzaValoreData(((DateBox) campo).getValue(), dateFormat);
				}
				if (campo instanceof DatiAggiuntiviNumberBox) {
					valore = GenericsUtil.normalizzaValoreNumerico(((DatiAggiuntiviNumberBox) campo).getValue());
				}
				if (campo instanceof DatiAggiuntiviListaBox) {
					valore = GenericsUtil.normalizzaValoreTesto(((DatiAggiuntiviListaBox) campo).getValue());
				}
				if (campo instanceof Label && ((Label) campo).getText() != null && !((Label) campo).getText().trim().equals("")) {
					valore = ((Label) campo).getText();
					
				}
				
				vc.set(new ValoreCellaDatoAggiuntivo.ValoreCellaDatoAggiuntivoSingolo(valore));
			}
		});
		
		return vc.get();
	}

	
	public static boolean validazioneClient(Map<String, Widget> campi, List<Widget> campiObbligatori) {
		
		boolean valid = true;
		
		for (Widget campo : campi.values()) {
			if (campo instanceof TextBox && isWidgetObbligatorio(campo, campiObbligatori) && ((TextBox) campo).getValue().trim().equals("")) {
				valid = false;
				campo.getElement().setAttribute("required", "required");
				
			} else	if (campo instanceof SuggestBox && isWidgetObbligatorio(campo, campiObbligatori) && ((SuggestBox) campo).getValue().trim().equals("")) {
				valid = false;
				campo.getElement().setAttribute("required", "required");
				
			} else if (campo instanceof DateBox) {
				DateBox dateBox = (DateBox) campo;
				if ((isWidgetObbligatorio(campo, campiObbligatori) && dateBox.getValue() == null) || (dateBox.getValue() == null && !dateBox.getTextBox().getText().trim().equals(""))){
					valid = false;
					campo.getElement().setAttribute("required", "required");
				}
				else {
					campo.getElement().removeAttribute("required");
				}
				
			} else if (campo instanceof DatiAggiuntiviNumberBox) {
				DatiAggiuntiviNumberBox numberBox = (DatiAggiuntiviNumberBox) campo;
				if ((isWidgetObbligatorio(campo, campiObbligatori) && numberBox.getValue() == null) || (numberBox.getValue() == null && !numberBox.getText().trim().equals(""))) {
					valid = false;
					campo.getElement().setAttribute("required", "required");
				} else {
					campo.getElement().removeAttribute("required");
				}
				
			} else if (campo instanceof DatiAggiuntiviListaBox && isWidgetObbligatorio(campo, campiObbligatori) && ((DatiAggiuntiviListaBox) campo).getValue() == null) {
				valid = false;
				campo.getElement().setAttribute("required", "required");
				
			} else if(campo instanceof DatoAggiuntivoMultiploWidget && isWidgetObbligatorio(campo, campiObbligatori) && 
					((DatoAggiuntivoMultiploWidget) campo).getItemSelected().size() == 0 ){
				valid = false;
				((DatoAggiuntivoMultiploWidget) campo).setRequired(true);
				//campo.getElement().setAttribute("required", "required");
				
			} else if(campo instanceof DatoAggiuntivoAnagraficaWidget && isWidgetObbligatorio(campo, campiObbligatori) && (((DatoAggiuntivoAnagraficaWidget)campo).getValoreDatoAggiuntivo() == null || ((DatoAggiuntivoAnagraficaWidget)campo).getValoreDatoAggiuntivo().equals(""))) {
				valid=false;
				((DatoAggiuntivoAnagraficaWidget)campo).validate(false);
				
			} else{
				campo.getElement().removeAttribute("required");
			}
		}
		
		return valid;
	}
	
	private static boolean isWidgetObbligatorio(Widget w, List<Widget> campiObbligatori){
		return campiObbligatori.contains(w);
	}
	
	public static void setValoreAggiuntivo(Widget campo, DatoAggiuntivo valore) {
		switch (valore.getTipo()) {
			
			case Data: 
				if (campo instanceof DateBox) {
					Format format = new DateBox.DefaultFormat(dateFormat);
					((DateBox) campo).setFormat(format);
					((DateBox) campo).setValue((valore == null || getValore(valore) == null) ? null : dateFormat.parse(getValore(valore)));
					((DateBox) campo).getDatePicker().setYearArrowsVisible(true);
				}
				break;
	
			case IndirizzoCivico:
				if (campo instanceof DatiAggiuntiviNumberBox)
					((DatiAggiuntiviNumberBox) campo).setValue((valore == null || getValore(valore) == null) ? null : Long.valueOf(getValore(valore)));
				break;
			
			case IndirizzoEsponente:
				if (campo instanceof TextBox)
					((TextBox) campo).setValue(getValore(valore));
				break;
			
			case IndirizzoVia: 
				if (campo instanceof SuggestBox)
					((SuggestBox) campo).setValue(getValore(valore));
				break; 
			
			case Lista:
				if (campo instanceof DatiAggiuntiviListaBox)
					((DatiAggiuntiviListaBox) campo).setValue(getValore(valore));
				break;
	
			case MultiploTesto:
				if (campo instanceof DatoAggiuntivoMultiploWidget){
					((DatoAggiuntivoMultiploWidget) campo).reset();
	
					for(String val : getValori(valore)) {
						((DatoAggiuntivoMultiploWidget) campo).addValueItem(val);
					}
				}
				break;
	
			case Numerico:
				if (campo instanceof DatiAggiuntiviNumberBox)
					((DatiAggiuntiviNumberBox) campo).setValue((valore == null || getValore(valore) == null) ? null : Long.valueOf(getValore(valore)));
				break;
			
			case Suggest:
				if (campo instanceof SuggestBox)
					((SuggestBox) campo).setValue(getValore(valore));
				break;
			
			case Testo: 
				if (campo instanceof TextBox)
					((TextBox) campo).setValue(getValore(valore));
				break;
			
			case Anagrafica: 
				if (campo instanceof DatoAggiuntivoAnagraficaWidget){
					((DatoAggiuntivoAnagraficaWidget) campo).setValoreDatoAggiuntivo(getValore(valore));
					((DatoAggiuntivoAnagraficaWidget) campo).setIdAnagrafica(getIdAnagrafica(valore));
				}
				break;
				
			case Tabella:
				if (campo instanceof DatoAggiuntivoTabellaWidget) {
					DatoAggiuntivoTabella dag = (DatoAggiuntivoTabella) valore;
					((DatoAggiuntivoTabellaWidget) campo).setRigheDatiAggiuntivi(dag.getRighe());
					break;
				}
			
			default:
				throw new IllegalArgumentException("Tipo Dato Aggiuntivo non gestito: " + valore.getTipo());
		}
	}
	
	public static void pulisciIntestazioni(DatoAggiuntivoTabella dato) {
		
		for (DatoAggiuntivo dag : dato.getIntestazioni()) {
			
			dag.accept(new DatoAggiuntivoVisitor() {
				
				@Override
				public void visit(DatoAggiuntivoTabella datoAggiuntivoTabella) {	
					throw new UnsupportedOperationException();
				}
				
				@Override
				public void visit(DatoAggiuntivoAnagrafica datoAggiuntivoAnagrafica) {					
					datoAggiuntivoAnagrafica.setValore(null);
					datoAggiuntivoAnagrafica.setIdAnagrafica(null);
				}
				
				@Override
				public void visit(DatoAggiuntivoValoreMultiplo datoAggiuntivoValoreMultiplo) {
					datoAggiuntivoValoreMultiplo.getValori().clear();
				}
				
				@Override
				public void visit(DatoAggiuntivoValoreSingolo datoAggiuntivoValoreSingolo) {
					datoAggiuntivoValoreSingolo.setValore(null);
				}
			});
			
		}
		
	}
}
