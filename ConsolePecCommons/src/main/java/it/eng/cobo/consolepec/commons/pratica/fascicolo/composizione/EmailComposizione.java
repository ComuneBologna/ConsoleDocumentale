package it.eng.cobo.consolepec.commons.pratica.fascicolo.composizione;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author GiacomoFM
 * @since 18/gen/2019
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EmailComposizione extends ElementoComposizione {

	private static final long serialVersionUID = -9010274692414003166L;

	private boolean pecOut;
	private boolean reply;
	private String mailID;
	private String tipo;
	private String oggetto;
	private String mittente;
	private List<String> destinatari;
	private List<String> destinatariCC;
	private List<AllegatoComposizione> allegatiEmail;
	private List<EmailComposizione> conversazione;

	@Builder
	private EmailComposizione(int uid, boolean protocollato, String numeroPg, String annoPg, String nome, String versione, Date dataCaricamento, String stato, String clientID, //
			boolean pecOut, boolean reply, String mailID, String tipo, String oggetto, String mittente, List<String> destinatari, List<String> destinatariCC, List<AllegatoComposizione> allegatiEmail,
			List<EmailComposizione> conversazione) {
		super(uid, protocollato, numeroPg, annoPg, nome, versione, dataCaricamento, stato, clientID);
		this.pecOut = pecOut;
		this.reply = reply;
		this.mailID = mailID;
		this.tipo = tipo;
		this.oggetto = oggetto;
		this.mittente = mittente;
		this.destinatari = destinatari == null ? new ArrayList<String>() : destinatari;
		this.destinatariCC = destinatariCC == null ? new ArrayList<String>() : destinatariCC;
		this.allegatiEmail = allegatiEmail == null ? new ArrayList<AllegatoComposizione>() : allegatiEmail;
		this.conversazione = conversazione == null ? new ArrayList<EmailComposizione>() : conversazione;
	}

}
