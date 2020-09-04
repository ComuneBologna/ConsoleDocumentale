package it.eng.cobo.consolepec.commons.configurazioni;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@EqualsAndHashCode(callSuper = true, of = "indirizzo")
@NoArgsConstructor
public class AnagraficaIngresso extends TipologiaPratica implements Configurabile, Serializable {

	private static final long serialVersionUID = -8268331102699462001L;

	@NonNull
	private String indirizzo;

	private String server;

	private String utenza;
	private String password;

	private String folderIn;
	private String folderTo;

	private boolean scaricoRicevute;

	private boolean cancellazioneAutomatica;
	private Integer giorniCancellazione;

	private List<Azione> azioni = new ArrayList<>();
	private Date dataCreazione;
	private String usernameCreazione;
}
