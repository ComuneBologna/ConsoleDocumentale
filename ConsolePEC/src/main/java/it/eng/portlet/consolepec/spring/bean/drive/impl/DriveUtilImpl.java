package it.eng.portlet.consolepec.spring.bean.drive.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import it.eng.cobo.consolepec.commons.configurazioni.abilitazione.GestioneDriveAbilitazione;
import it.eng.cobo.consolepec.commons.drive.Dizionario;
import it.eng.cobo.consolepec.commons.drive.Nomenclatura;
import it.eng.portlet.consolepec.gwt.server.rest.client.DriveClient;
import it.eng.portlet.consolepec.spring.bean.drive.DriveUtil;
import it.eng.portlet.consolepec.spring.bean.profilazione.GestioneProfilazioneUtente;

public class DriveUtilImpl implements DriveUtil {

	@Autowired
	DriveClient driveClient;

	@Autowired
	GestioneProfilazioneUtente gestioneProfilazioneUtente;

	private static final Logger logger = LoggerFactory.getLogger(DriveUtilImpl.class);

	private List<Dizionario> dizionari = new ArrayList<Dizionario>();
	private List<Nomenclatura> nomenclature = new ArrayList<Nomenclatura>();

	public void init() {

		if (gestioneProfilazioneUtente.getAutorizzazioniUtente().isAbilitato(GestioneDriveAbilitazione.class)) {
			try {
				this.dizionari = driveClient.getDizionari();
				this.dizionari.removeAll(Collections.singleton(null));
				this.nomenclature = driveClient.getNomenclature();
				this.nomenclature.removeAll(Collections.singleton(null));

			} catch (Exception e) {
				logger.error("Errore durante il recupero delle configurazioni drive", e);
			}
		}

	}

	@Override
	public List<Dizionario> getDizionari() {
		return new ArrayList<>(this.dizionari);
	}

	@Override
	public List<Nomenclatura> getNomenclature() {
		return new ArrayList<>(this.nomenclature);
	}

}
