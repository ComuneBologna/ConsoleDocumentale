package it.eng.portlet.consolepec.spring.bean.search;

import java.util.Map;

import it.eng.portlet.consolepec.gwt.shared.action.CercaPratiche;

public interface PraticaProcediSearchUtil {

	Map<String, Object> createQueryFilters(CercaPratiche cercaPraticheAction);

	Map<String, Boolean> createSortFilters(CercaPratiche cercaPraticheAction);

}
