package it.eng.cobo.consolepec.commons.dto;

import java.util.ArrayList;
import java.util.List;

import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaFascicolo.StepIter;
import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.DatoAggiuntivo;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Fascicolo extends Pratica {

	private static final long serialVersionUID = 8616533472553654951L;

	private String titolo;
	private String tipologia;
	private String usernameCreazione;
	private String operatore;
	private StepIter stepIter;

	@Setter(AccessLevel.NONE)
	private List<DatoAggiuntivo> datiAggiuntivi = new ArrayList<>();
	@Setter(AccessLevel.NONE)
	private List<ProtocollazioneCapofila> protocollazioniCapofila = new ArrayList<>();
	@Setter(AccessLevel.NONE)
	private List<Procedimento> procedimenti = new ArrayList<>();
	@Setter(AccessLevel.NONE)
	private List<FascicoloCollegato> fascicoliCollegati = new ArrayList<>();
	@Setter(AccessLevel.NONE)
	private List<PraticaCollegata> praticheCollegate = new ArrayList<>();
	@Setter(AccessLevel.NONE)
	private List<String> praticheProcediCollegate = new ArrayList<>();
	@Setter(AccessLevel.NONE)
	private List<Azione> azioni = new ArrayList<>();

	public Fascicolo() {
		super(Tipo.FASCICOLO);
	}

}
