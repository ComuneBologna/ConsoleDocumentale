package it.eng.portlet.consolepec.gwt.shared.action.cartellafirma;

import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaRuolo;
import it.eng.cobo.consolepec.commons.configurazioni.TipologiaPratica;
import it.eng.portlet.consolepec.gwt.shared.action.LiferayPortletUnsecureActionImpl;
import it.eng.portlet.consolepec.gwt.shared.model.ColonnaWorklist;
import it.eng.portlet.consolepec.gwt.shared.model.cartellafirma.StatoDestinatarioTaskFirmaDTO;
import it.eng.portlet.consolepec.gwt.shared.model.cartellafirma.StatoTaskFirmaDTO;
import it.eng.portlet.consolepec.gwt.shared.model.cartellafirma.TipoPropostaTaskFirmaDTO;
import it.eng.portlet.consolepec.gwt.shared.model.cartellafirma.TipoStatoTaskFirmaDTO;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author biagiot
 *
 */

@Getter
@Setter
public class CercaDocumentoFirmaVistoAction extends LiferayPortletUnsecureActionImpl<CercaDocumentoFirmaVistoActionResult>{

	// FILTRI DI RICERCA
	private String oggetto;
	private String dataDa;
	private String dataA;
	private List<AnagraficaRuolo> proponenti = new ArrayList<AnagraficaRuolo>();
	private TipoPropostaTaskFirmaDTO tipoProposta;
	private TipoStatoTaskFirmaDTO tipoStato;
	private StatoDestinatarioTaskFirmaDTO statoDestinatario;
	private StatoTaskFirmaDTO stato;
	private String idDocumentaleFascicolo;
	private String titoloFascicolo;
	private TipologiaPratica tipologiaPratica;
	private String mittenteOriginale;
	private String dataScadenzaDa;
	private String dataScadenzaA;

	// CAMPI VALORIZZATI IN BASE AI FILTRI DI RICERCA
	private boolean ricercaDaDestinatario;

	private boolean count;
	private ColonnaWorklist campoOrdinamento;
	private boolean ordinamentoAsc;
	private int inizio;
	private int fine;

}
