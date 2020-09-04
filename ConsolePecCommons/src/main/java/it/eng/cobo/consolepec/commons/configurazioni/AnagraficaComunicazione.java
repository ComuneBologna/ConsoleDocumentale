package it.eng.cobo.consolepec.commons.configurazioni;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true, of = {})
public class AnagraficaComunicazione extends TipologiaPratica implements Configurabile, Serializable {
	private static final long serialVersionUID = -6632910366703038533L;

	private Date dataCreazione;
	private List<Azione> azioni = new ArrayList<Azione>();
	private String usernameCreazione;
}
