package it.eng.portlet.consolepec.gwt.client.presenter.protocollazione.event.sceltacapofila;

import it.eng.cobo.consolepec.commons.configurazioni.TipologiaPratica;
import it.eng.cobo.consolepec.util.pratica.PraticaUtil;
import it.eng.portlet.consolepec.gwt.shared.action.pec.TipoRiferimentoPEC;
import it.eng.portlet.consolepec.gwt.shared.model.PraticaDTO;

import java.util.Set;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

public class MostraSceltaCapofilaEvent extends GwtEvent<MostraSceltaCapofilaEvent.MostraSceltaCapofilaHandler> {

	public static Type<MostraSceltaCapofilaHandler> TYPE = new Type<MostraSceltaCapofilaHandler>();
	private String idFascicolo;
	private Set<SelectedObject> listPec;
	private Set<String> allegati;
	private boolean fromSceltaFascicolo;
	private String idPraticaDaAggiungere;

	public interface MostraSceltaCapofilaHandler extends EventHandler {
		void onMostraSceltaCapofila(MostraSceltaCapofilaEvent event);
	}
	
	public MostraSceltaCapofilaEvent() {
	}

	@Override
	protected void dispatch(MostraSceltaCapofilaHandler handler) {
		handler.onMostraSceltaCapofila(this);
	}

	@Override
	public Type<MostraSceltaCapofilaHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<MostraSceltaCapofilaHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source, MostraSceltaCapofilaEvent event) {
		source.fireEvent(event);
	}

	public String getIdFascicolo() {
		return idFascicolo;
	}

	public void setIdFascicolo(String idFascicolo) {
		this.idFascicolo = idFascicolo;
	}

	public Set<SelectedObject> getListPec() {
		return listPec;
	}

	public void setListPec(Set<SelectedObject> listPec) {
		this.listPec = listPec;
	}

	public Set<String> getAllegati() {
		return allegati;
	}

	public void setAllegati(Set<String> allegati) {
		this.allegati = allegati;
	}

	public boolean isFromSceltaFascicolo() {
		return fromSceltaFascicolo;
	}

	public void setFromSceltaFascicolo(boolean fromSceltaFascicolo) {
		this.fromSceltaFascicolo = fromSceltaFascicolo;
	}

	public String getIdPraticaDaAggiungere() {
		return idPraticaDaAggiungere;
	}

	public void setIdPraticaDaAggiungere(String idPraticaDaAggiungere) {
		this.idPraticaDaAggiungere = idPraticaDaAggiungere;
	}


	public static class SelectedObject implements Comparable<SelectedObject> {

		public enum ObjectType {
			PEC_IN, PEC_OUT, ALLEGATO, PRATICA_MODULISTICA
		}

		private String idPec;
		private ObjectType type;

		public SelectedObject(String idPec, ObjectType type) {
			super();
			this.idPec = idPec;
			this.type = type;
		}

		public String getIdPec() {
			return idPec;
		}

		public ObjectType getType() {
			return type;
		}

		public static ObjectType getObjectType(TipoRiferimentoPEC tipoRiferimentoPEC){
			switch (tipoRiferimentoPEC) {
				case IN:
					return ObjectType.PEC_IN;
				case EPROTO:
					return ObjectType.PEC_IN;
				case OUT:
					return ObjectType.PEC_OUT;
				default:
					throw new IllegalStateException("Tipo riferimento PEC non trovato.");
				}
		} 
		
		public static ObjectType getObjectType(PraticaDTO praticaDTO){
		
			TipologiaPratica tp = praticaDTO.getTipologiaPratica();
			
			if (PraticaUtil.isIngresso(tp)) {
				return ObjectType.PEC_IN;

			} else if (PraticaUtil.isEmailOut(tp)) {
				return ObjectType.PEC_OUT;

			} else if (PraticaUtil.isPraticaModulistica(tp)) {
				return ObjectType.PRATICA_MODULISTICA;

			} else {
				throw new IllegalStateException("Tipo riferimento PEC non trovato.");
			}
		} 
		
		public static TipologiaPratica getTipologiaPratica(ObjectType tipo){
			
			switch (tipo) {
				case PEC_IN:
					return TipologiaPratica.EMAIL_IN;
					
				case PEC_OUT:
					return TipologiaPratica.EMAIL_OUT;
					
				case PRATICA_MODULISTICA:
					return TipologiaPratica.PRATICA_MODULISTICA;

				default:
					throw new IllegalStateException("Tipo riferimento PEC non trovato.");
				}
		} 

		
		@Override
		public int compareTo(SelectedObject o) {
			return this.idPec.compareTo(o.getIdPec());
		}

	}
}
