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
 * @since 22/gen/2019
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AllegatoComposizione extends ElementoComposizione {

	private static final long serialVersionUID = -4752497285763080921L;

	private String folderOriginPath;
	private String folderOriginName;

	private List<String> tag;

	@Builder
	public AllegatoComposizione(int uID, boolean protocollato, String numeroPg, String annoPg, String nome, String versione, Date dataCaricamento, String stato, String clientID,
			String folderOriginPath, String folderOriginName, List<String> tag) {
		super(uID, protocollato, numeroPg, annoPg, nome, versione, dataCaricamento, stato, clientID);
		this.folderOriginPath = folderOriginPath;
		this.folderOriginName = folderOriginName;
		this.tag = tag == null ? new ArrayList<String>() : tag;
	}

}
