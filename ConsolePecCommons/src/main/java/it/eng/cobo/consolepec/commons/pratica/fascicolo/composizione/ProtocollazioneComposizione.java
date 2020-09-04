package it.eng.cobo.consolepec.commons.pratica.fascicolo.composizione;

import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import lombok.Getter;

/**
 * @author GiacomoFM
 * @since 30/gen/2019
 */
@Getter
public class ProtocollazioneComposizione implements Serializable {

	private static final long serialVersionUID = 5652986275610682139L;

	private boolean protocollata;
	private boolean capofila;
	private String numeroPg;
	private String annoPg;
	private String numeroPgCapofila;
	private String annoPgCapofila;

	private List<ElementoProtocollato> elementi = new LinkedList<>();
	private List<ProtocollazioneComposizione> nonCapofila = new LinkedList<>();

	public ProtocollazioneComposizione() {
		super();
		this.protocollata = false;
		this.nonCapofila = Collections.<ProtocollazioneComposizione> emptyList();
	}

	public ProtocollazioneComposizione(String numeroPg, String annoPg) {
		super();
		this.protocollata = true;
		this.capofila = true;
		this.numeroPg = numeroPg;
		this.annoPg = annoPg;
	}

	public ProtocollazioneComposizione(String numeroPg, String annoPg, String numeroPgCapofila, String annoPgCapofila) {
		super();
		this.protocollata = true;
		this.capofila = false;
		this.numeroPg = numeroPg;
		this.annoPg = annoPg;
		this.numeroPgCapofila = numeroPgCapofila;
		this.annoPgCapofila = annoPgCapofila;
	}

}
