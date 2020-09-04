package it.eng.cobo.consolepec.commons.pratica.fascicolo.composizione;

import java.util.Date;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author GiacomoFM
 * @since 15/gen/2019
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ElementoProtocollato extends ElementoComposizione {

	private static final long serialVersionUID = -6050013625383680632L;

	private boolean capofila;
	private String numeroPgCapofila;
	private String annoPgCapofila;

	@Builder
	private ElementoProtocollato(int uid, boolean protocollato, String numeroPg, String annoPg, String nome, String versione, Date dataCaricamento, String stato, String clientID, //
			boolean capofila, String numeroPgCapofila, String annoPgCapofila) {
		super(uid, protocollato, numeroPg, annoPg, nome, versione, dataCaricamento, stato, clientID);
		this.capofila = capofila;
		this.numeroPgCapofila = numeroPgCapofila;
		this.annoPgCapofila = annoPgCapofila;
	}

}
