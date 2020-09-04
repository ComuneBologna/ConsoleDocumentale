package it.eng.portlet.consolepec.spring.bean.search;

import it.eng.consolepec.spagicclient.search.SearchPraticheFilter;
import it.eng.consolepec.spagicclient.search.SearchPraticheSort;
import it.eng.portlet.consolepec.gwt.shared.action.CercaPratiche;
import it.eng.portlet.consolepec.gwt.shared.action.cartellafirma.CercaDocumentoFirmaVistoAction;

public interface NewMongoDbSearchDocumentRequestGenerator {

	SearchPraticheFilter generaFilter(CercaPratiche filtri);
	SearchPraticheSort generaSort(CercaPratiche cercaPraticheAction);

	SearchPraticheFilter generaFilter(CercaDocumentoFirmaVistoAction action);
	SearchPraticheSort generaSort(CercaDocumentoFirmaVistoAction action);

}
