package it.eng.cobo.consolepec.commons.configurazioni;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.DatoAggiuntivo;
import it.eng.cobo.consolepec.commons.pratica.fascicolo.CondizioneEsecuzione;
import it.eng.cobo.consolepec.commons.pratica.fascicolo.ConseguenzaEsecuzione;
import it.eng.cobo.consolepec.commons.pratica.fascicolo.DatiProcedimento;
import it.eng.cobo.consolepec.commons.pratica.fascicolo.Titolazione;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true, of = {})
@NoArgsConstructor
public class AnagraficaFascicolo extends TipologiaPratica implements Configurabile {

	private static final long serialVersionUID = -4072267017685293255L;

	private List<StepIter> stepIterAbilitati = new ArrayList<>();
	private List<DatoAggiuntivo> datiAggiuntivi = new ArrayList<>();
	private String templateTitolo;
	private List<ConfigurazioneEsecuzione> configurazioneEsecuzioni = new ArrayList<>();
	private List<String> tipologieAllegato = new ArrayList<>();
	private Titolazione titolazione;
	private DatiProcedimento datiProcedimento;

	private boolean protocollazioneRiservata = false;

	@Data
	@NoArgsConstructor
	@EqualsAndHashCode(of="nome")
	public static class StepIter implements Serializable {
		private static final long serialVersionUID = -7315553477730673791L;

		private String nome;
		private boolean finale = false;
		private boolean iniziale = false;
		private boolean creaBozza = false;
		private List<String> destinatariNotifica = new ArrayList<String>();
		private Date dataAggiornamento;
	}

	@Data
	@NoArgsConstructor
	public static class ConfigurazioneEsecuzione implements Serializable {
		private static final long serialVersionUID = 8332677131080341953L;

		private CondizioneEsecuzione condizione;
		private List<ConseguenzaEsecuzione> esecuzioni = new ArrayList<ConseguenzaEsecuzione>();
	}

	private List<Azione> azioni = new ArrayList<>();
	private Date dataCreazione;
	private String usernameCreazione;
}