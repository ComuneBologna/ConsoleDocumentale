package it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi;

import java.io.Serializable;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public abstract class DatoAggiuntivo implements Serializable {
	private static final long serialVersionUID = -8157133780577839765L;

	String nome;
	String descrizione;
	TipoDato tipo;
	Integer posizione;
	boolean visibile;

	public DatoAggiuntivo(String nome, String descrizione, TipoDato tipo, Integer posizione, boolean visibile) {
		super();
		this.nome = nome;
		this.descrizione = descrizione;
		this.tipo = tipo;
		this.posizione = posizione;
		this.visibile = visibile;
	}

	public abstract void accept(DatoAggiuntivoVisitor v);

	public abstract DatoAggiuntivo clona();

	public static interface DatoAggiuntivoVisitor {

		void visit(DatoAggiuntivoValoreSingolo datoAggiuntivoValoreSingolo);

		void visit(DatoAggiuntivoValoreMultiplo datoAggiuntivoValoreMultiplo);

		void visit(DatoAggiuntivoAnagrafica datoAggiuntivoAnagrafica);

		void visit(DatoAggiuntivoTabella datoAggiuntivoTabella);

	}

	public static abstract class DatoAggiuntivoVisitorAdapter implements DatoAggiuntivoVisitor {

		@Override
		public void visit(DatoAggiuntivoValoreSingolo datoAggiuntivoValoreSingolo) {/**/}

		@Override
		public void visit(DatoAggiuntivoValoreMultiplo datoAggiuntivoValoreMultiplo) {/**/}

		@Override
		public void visit(DatoAggiuntivoAnagrafica datoAggiuntivoAnagrafica) {/**/}

		@Override
		public void visit(DatoAggiuntivoTabella datoAggiuntivoTabella) {/**/}
	}

	public static abstract class DatoAggiuntivoValoreVisitorAdapter implements DatoAggiuntivoVisitor {

		@Override
		public final void visit(DatoAggiuntivoValoreSingolo dag) {
			visitValore(dag);
		}

		@Override
		public final void visit(DatoAggiuntivoValoreMultiplo dag) {
			visitValore(dag);
		}

		@Override
		public final void visit(DatoAggiuntivoAnagrafica dag) {
			visitValore(dag);
		}

		@Override
		public void visit(DatoAggiuntivoTabella dag) {/**/}

		public abstract void visitValore(DatoAggiuntivoValore dag);
	}
}
