package it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.tabella;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 
 * @author biagiot
 *
 */
@ToString
@EqualsAndHashCode
@NoArgsConstructor
public abstract class ValoreCellaDatoAggiuntivo implements Serializable {

	private static final long serialVersionUID = -4813581642229117619L;

	public abstract boolean isEmpty();
	public abstract void accept(ValoreCellaVisitor v);
	
	public interface ValoreCellaVisitor {
		void visit(ValoreCellaDatoAggiuntivoAnagrafica cella);
		void visit(ValoreCellaDatoAggiuntivoMultiplo cella);
		void visit(ValoreCellaDatoAggiuntivoSingolo cella);
	}
	
	@EqualsAndHashCode(callSuper=false)
	@NoArgsConstructor
	public static class ValoreCellaDatoAggiuntivoAnagrafica extends ValoreCellaDatoAggiuntivo {
		
		private static final long serialVersionUID = -32554754680325061L;

		@Getter
		Double idAnagrafica;
		
		@Getter
		String valore;
		
		public ValoreCellaDatoAggiuntivoAnagrafica(Double idAnagrafica, String valore) {
			this.valore = valore;
			this.idAnagrafica = idAnagrafica;
		}

		@Override
		public void accept(ValoreCellaVisitor v) {
			v.visit(this);
		}
		
		@Override
		public String toString() {
			return valore;
		}

		@Override
		public boolean isEmpty() {
			return idAnagrafica == null || idAnagrafica <= 0 || valore == null;
		}
	}
	
	@EqualsAndHashCode(callSuper=false)
	@NoArgsConstructor
	public static class ValoreCellaDatoAggiuntivoMultiplo extends ValoreCellaDatoAggiuntivo {
				
		private static final long serialVersionUID = -8437374963211710216L;
		
		@Getter
		List<String> valori = new ArrayList<String>();
		
		public ValoreCellaDatoAggiuntivoMultiplo(List<String> valori) {
			this.valori = valori;
		}

		@Override
		public void accept(ValoreCellaVisitor v) {
			v.visit(this);
		}
		
		@Override
		public String toString() {
			return valori.toString();
		}

		@Override
		public boolean isEmpty() {
			return valori == null;
		}
	}
	
	@EqualsAndHashCode(callSuper=false)
	@NoArgsConstructor
	public static class ValoreCellaDatoAggiuntivoSingolo extends ValoreCellaDatoAggiuntivo {
				
		private static final long serialVersionUID = 8276441276770976332L;
		
		@Getter
		String valore;
		
		public ValoreCellaDatoAggiuntivoSingolo(String valore) {
			this.valore = valore;
		}

		@Override
		public void accept(ValoreCellaVisitor v) {
			v.visit(this);
		}
		
		@Override
		public String toString() {
			return valore;
		}

		@Override
		public boolean isEmpty() {
			return valore == null;
		}
	}
}
