package it.eng.portlet.consolepec.spring.firma;

import java.util.List;

import it.eng.cobo.consolepec.commons.exception.ApplicationException;
import it.eng.consolepec.xmlplugin.factory.Pratica;
import it.eng.portlet.consolepec.gwt.shared.model.AllegatoDTO.TipologiaFirma;
import it.eng.portlet.consolepec.spring.bean.session.user.UserException;
import it.eng.portlet.consolepec.spring.firma.impl.CredenzialiFirma;

public interface GestioneFirma {

	public Pratica<?> firmaDocumento(String praticaPath, List<String> allegati, CredenzialiFirma cf, TipologiaFirma tipologiaFirma,
			boolean isEMailOut) throws UserException, ApplicationException, Exception;

}
