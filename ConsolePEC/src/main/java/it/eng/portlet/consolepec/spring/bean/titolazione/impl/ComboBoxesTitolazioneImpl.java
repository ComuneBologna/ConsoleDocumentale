package it.eng.portlet.consolepec.spring.bean.titolazione.impl;

import it.bologna.comune.spagic.combo.protocollazione.Combos;
import it.eng.consolepec.spagicclient.SpagicClientGetComboBoxesProtocollazione;
import it.eng.portlet.consolepec.spring.bean.session.user.UserSessionUtil;
import it.eng.portlet.consolepec.spring.bean.titolazione.ComboBoxesTitolazione;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;

public class ComboBoxesTitolazioneImpl implements ComboBoxesTitolazione {

	@Autowired
	SpagicClientGetComboBoxesProtocollazione spagicClientGetComboBoxesProtocollazione;
	
	@Autowired
	UserSessionUtil userSessionUtil;
	
	@Override
	@Cacheable(value = "comboBoxesProtocollazione", key = "#root.methodName")
	public Combos retrieveComboBoxes() {
		return spagicClientGetComboBoxesProtocollazione.getComboBoxes(userSessionUtil.getUtenteSpagic());
	}
}
