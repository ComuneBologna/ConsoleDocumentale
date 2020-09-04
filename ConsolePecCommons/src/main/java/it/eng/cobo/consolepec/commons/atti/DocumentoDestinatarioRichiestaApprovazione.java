package it.eng.cobo.consolepec.commons.atti;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author GiacomoFM
 * @since 09/mar/2018
 */
public abstract class DocumentoDestinatarioRichiestaApprovazione {

	@Getter @Setter protected String stato;
	
	public abstract void accept(DocumentoDestinatarioVisitor v);
	
	@NoArgsConstructor
	public static class DocumentoDestinatarioGruppoRichiestaApprovazione extends DocumentoDestinatarioRichiestaApprovazione implements Comparable<DocumentoDestinatarioGruppoRichiestaApprovazione> {
		@Getter @Setter private String nomeGruppo;

		public DocumentoDestinatarioGruppoRichiestaApprovazione(String stato, String nomeGruppo) {
			super();
			this.stato = stato;
			this.nomeGruppo = nomeGruppo;
		}

		@Override
		public int compareTo(DocumentoDestinatarioGruppoRichiestaApprovazione o) {
			return nomeGruppo.compareTo(o.getNomeGruppo());
		}

		@Override
		public void accept(DocumentoDestinatarioVisitor v) {
			v.visit(this);
		}
	}

	@NoArgsConstructor
	public static class DocumentoDestinatarioUtenteRichiestaApprovazione extends DocumentoDestinatarioRichiestaApprovazione implements Comparable<DocumentoDestinatarioUtenteRichiestaApprovazione> {
		@Setter @Getter private String nomeUtente;
		@Setter @Getter private String nome;
		@Setter @Getter private String cognome;
		@Setter @Getter private String matricola;
		@Setter @Getter private String settore;

		public DocumentoDestinatarioUtenteRichiestaApprovazione(String nomeUtente, String nome, String cognome, String matricola, String settore,
				String stato) {
			this.nomeUtente = nomeUtente;
			this.nome = nome;
			this.cognome = cognome;
			this.matricola = matricola;
			this.settore = settore;
			this.stato = stato;
		}

		@Override
		public int compareTo(DocumentoDestinatarioUtenteRichiestaApprovazione o) {
			return nomeUtente.compareTo(o.getNomeUtente());
		}

		@Override
		public void accept(DocumentoDestinatarioVisitor v) {
			v.visit(this);
		}
	}

	public static interface DocumentoDestinatarioVisitor {
		void visit(DocumentoDestinatarioUtenteRichiestaApprovazione du);
		void visit(DocumentoDestinatarioGruppoRichiestaApprovazione dg);
	}
}
