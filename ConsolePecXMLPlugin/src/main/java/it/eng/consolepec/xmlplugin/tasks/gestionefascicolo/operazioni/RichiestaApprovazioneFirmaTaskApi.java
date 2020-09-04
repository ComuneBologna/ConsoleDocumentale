package it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni;

import java.util.Date;
import java.util.List;
import java.util.TreeSet;

import it.eng.consolepec.xmlplugin.factory.DatiPratica.Allegato;
import it.eng.consolepec.xmlplugin.tasks.richiestafirma.DatiApprovazioneFirmaTask.DestinatarioRichiestaApprovazioneFirmaTask;
import it.eng.consolepec.xmlplugin.tasks.richiestafirma.DatiApprovazioneFirmaTask.TipoPropostaApprovazioneFirmaTask;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

public interface RichiestaApprovazioneFirmaTaskApi extends ITaskApi {

	@ToString
	@EqualsAndHashCode
	public static class RichiestaApprovazioneFirmaBean {

		@Setter
		@Getter
		private Allegato allegato;
		@Setter
		@Getter
		private TipoPropostaApprovazioneFirmaTask tipoRichiestaFirma;
		@Setter
		@Getter
		private TreeSet<DestinatarioRichiestaApprovazioneFirmaTask> destinatari = new TreeSet<>();
		@Setter
		@Getter
		private String oggettoDocumento;
		@Setter
		@Getter
		private String gruppoProponente;
		@Getter
		@Setter
		private String mittenteOriginale;
		@Getter
		@Setter
		private Date dataScadenza;
	}

	public Integer inviaInApprovazione(RichiestaApprovazioneFirmaBean bean, List<String> destinatariNotifica, String note, String fullNameUtente);
}
