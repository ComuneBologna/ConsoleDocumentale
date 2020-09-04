package it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import it.eng.consolepec.xmlplugin.exception.PraticaException;
import it.eng.consolepec.xmlplugin.factory.DatiPratica;
import it.eng.consolepec.xmlplugin.factory.DatiPratica.Allegato;
import it.eng.consolepec.xmlplugin.factory.Pratica;
import it.eng.consolepec.xmlplugin.pratica.email.DatiEmail.PG;
import it.eng.consolepec.xmlplugin.pratica.email.DatiEmail.ProtocollazionePECBuilder;
import it.eng.consolepec.xmlplugin.pratica.email.DatiPraticaEmailTaskAdapter;
import it.eng.consolepec.xmlplugin.pratica.email.PraticaEmail;
import it.eng.consolepec.xmlplugin.pratica.email.PraticaEmailIn;
import it.eng.consolepec.xmlplugin.pratica.email.XMLPraticaEmail;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.DatiFascicolo;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.DatiFascicolo.Protocollazione;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.DatiFascicolo.ProtocollazioneCapofila;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.DatiFascicolo.Stato;
import it.eng.consolepec.xmlplugin.pratica.modulistica.DatiModulistica.ProtocollazionePraticaModulisticaBuilder;
import it.eng.consolepec.xmlplugin.pratica.modulistica.DatiPraticaModulisticaTaskAdapter;
import it.eng.consolepec.xmlplugin.pratica.modulistica.XMLPraticaModulistica;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.XMLTaskFascicolo;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.fascicolo.DatiGestioneFascicoloTask;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.ProtocollaTaskApi;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.TipoApiTask;
import it.eng.consolepec.xmlplugin.util.TaskDiFirmaUtil;
import it.eng.consolepec.xmlplugin.util.XmlPluginUtil;

public class ProtocollaTaskApiImpl<T extends DatiGestioneFascicoloTask> extends AbstractTaskApiImpl<T> implements ProtocollaTaskApi {

	public ProtocollaTaskApiImpl(XMLTaskFascicolo<T> task) {
		super(task);
	}

	@Override
	public void protocolla(ProtocollazioneBean protocollazioneBean) {

		controllaProtocollazioneEsistente(protocollazioneBean.getNumeroPG(), protocollazioneBean.getAnnoPG());

		List<DatiFascicolo.PraticaCollegata> praticheCollegateProtocollate = new ArrayList<DatiFascicolo.PraticaCollegata>();
		for (Pratica<?> praticaCollegata : protocollazioneBean.getPraticheCollegate()) {
			praticheCollegateProtocollate.add(getDatiFascicolo().new PraticaCollegata(praticaCollegata.getAlfrescoPath(), praticaCollegata.getDati().getTipo().getNomeTipologia(),
					praticaCollegata.getDati().getDataCreazione()));
		}

		List<DatiPratica.Allegato> allegatiProtocollati = new ArrayList<DatiPratica.Allegato>();
		for (Allegato allegato : protocollazioneBean.getAllegati()) {
			// se cerco di protocollare più volte lo stesso file
			if (XmlPluginUtil.isAllegatoProtocollato(allegato, task.getEnclosingPratica()))
				throw new PraticaException("L'allegato " + allegato.getNome() + " non puo' essere protocollato piu' volte");
			allegatiProtocollati.add(allegato);
		}

		DatiFascicolo.ProtocollazioneCapofilaBuilder protocollazioneCapofilaBuilder = new DatiFascicolo.ProtocollazioneCapofilaBuilder(getDatiFascicolo());

		protocollazioneCapofilaBuilder.setAllegatiProtocollati(allegatiProtocollati);
		protocollazioneCapofilaBuilder.setAnnoPG(protocollazioneBean.getAnnoPG());
		protocollazioneCapofilaBuilder.setDataprotocollazione(protocollazioneBean.getDataprotocollazione());

		protocollazioneCapofilaBuilder.setDataArrivo(protocollazioneBean.getDataArrivo());
		protocollazioneCapofilaBuilder.setOraArrivo(protocollazioneBean.getOraArrivo());

		protocollazioneCapofilaBuilder.setNumeroFascicolo(protocollazioneBean.getNumeroFascicolo());
		protocollazioneCapofilaBuilder.setAnnoFascicolo(protocollazioneBean.getAnnoFascicolo());
		protocollazioneCapofilaBuilder.setNumeroPG(protocollazioneBean.getNumeroPG());
		protocollazioneCapofilaBuilder.setOggetto(protocollazioneBean.getOggetto());
		protocollazioneCapofilaBuilder.setPraticheCollegateProtocollate(praticheCollegateProtocollate);
		protocollazioneCapofilaBuilder.setProtocollazioniCollegate(new ArrayList<DatiFascicolo.Protocollazione>());
		protocollazioneCapofilaBuilder.setProvenienza(protocollazioneBean.getProvenienza());
		protocollazioneCapofilaBuilder.setRubrica(protocollazioneBean.getRubrica());
		protocollazioneCapofilaBuilder.setSezione(protocollazioneBean.getSezione());
		protocollazioneCapofilaBuilder.setTipologiadocumento(protocollazioneBean.getTipologiadocumento());
		protocollazioneCapofilaBuilder.setTitolo(protocollazioneBean.getTitolo());
		protocollazioneCapofilaBuilder.setNote(protocollazioneBean.getNote());
		protocollazioneCapofilaBuilder.setUtenteprotocollazione(protocollazioneBean.getUtenteprotocollazione());
		protocollazioneCapofilaBuilder.setAnnoRegistro(protocollazioneBean.getAnnoRegistro());
		protocollazioneCapofilaBuilder.setNumeroRegistro(protocollazioneBean.getNumeroRegistro());
		protocollazioneCapofilaBuilder.setTipoProtocollazione(protocollazioneBean.getTipoProtocollazione());

		List<ProtocollazioneCapofila> protocollazioniCapofila = getDatiFascicolo().getProtocollazioniCapofila();
		if (protocollazioniCapofila == null)
			protocollazioniCapofila = new ArrayList<ProtocollazioneCapofila>();
		ProtocollazioneCapofila protocollazioneCapofila = protocollazioneCapofilaBuilder.construct();
		protocollazioniCapofila.add(protocollazioneCapofilaBuilder.construct());
		aggiungiProtocollazioneAPratiche(protocollazioneBean.getPraticheCollegate(), protocollazioneCapofila);

		// Operazione di tracciatura eventuale dell'iter
		generaEventoPerProtocollazione(EventiIterFascicolo.PROTOCOLLA, protocollazioneBean.getNumeroPG(), protocollazioneBean.getAnnoPG(), task.getDati().getAssegnatario().getEtichetta(),
				task.getCurrentUser(), protocollazioneBean.getNumeroPG(), String.valueOf(protocollazioneBean.getAnnoPG()));

	}

	@Override
	public void protocolla(ProtocollazioneBean protocollazioneBean, String numeroPgCapofila, String annoPgCapofila) {

		controllaProtocollazioneEsistente(protocollazioneBean.getNumeroPG(), protocollazioneBean.getAnnoPG());

		List<ProtocollazioneCapofila> protocollazioniCapofila = getDatiFascicolo().getProtocollazioniCapofila();
		ProtocollazioneCapofila capofila = null;
		for (ProtocollazioneCapofila protocollazioneCapofila : protocollazioniCapofila) {
			if (protocollazioneCapofila.getAnnoPG().toString().equals(annoPgCapofila) && protocollazioneCapofila.getNumeroPG().equals(numeroPgCapofila)) {
				capofila = protocollazioneCapofila;
				break;
			}
		}
		if (capofila == null)
			throw new PraticaException("Il capofila " + numeroPgCapofila + "/" + annoPgCapofila + " non esiste");

		List<DatiFascicolo.PraticaCollegata> praticheCollegateProtocollate = new ArrayList<DatiFascicolo.PraticaCollegata>();
		for (Pratica<?> praticaCollegata : protocollazioneBean.getPraticheCollegate()) {
			praticheCollegateProtocollate.add(getDatiFascicolo().new PraticaCollegata(praticaCollegata.getAlfrescoPath(), praticaCollegata.getDati().getTipo().getNomeTipologia(),
					praticaCollegata.getDati().getDataCreazione()));
		}

		List<DatiPratica.Allegato> allegatiProtocollati = new ArrayList<DatiPratica.Allegato>();
		for (Allegato allegato : protocollazioneBean.getAllegati()) {
			if (!check(allegato))
				throw new PraticaException("L'allegato " + allegato.getNome() + " non puo' essere protocollato. Potrebbe essere in un task di approvazione e/o gia' protocollato");

			allegatiProtocollati.add(allegato);
		}

		DatiFascicolo.ProtocollazioneBuilder builder = new DatiFascicolo.ProtocollazioneBuilder(getDatiFascicolo());

		builder.setAllegatiProtocollati(allegatiProtocollati);
		builder.setAnnoPG(protocollazioneBean.getAnnoPG());
		builder.setDataprotocollazione(protocollazioneBean.getDataprotocollazione());

		builder.setDataArrivo(protocollazioneBean.getDataArrivo());
		builder.setOraArrivo(protocollazioneBean.getOraArrivo());

		builder.setNumeroFascicolo(protocollazioneBean.getNumeroFascicolo());
		builder.setAnnoFascicolo(protocollazioneBean.getAnnoFascicolo());
		builder.setNumeroPG(protocollazioneBean.getNumeroPG());
		builder.setOggetto(protocollazioneBean.getOggetto());
		builder.setPraticheCollegateProtocollate(praticheCollegateProtocollate);
		builder.setProvenienza(protocollazioneBean.getProvenienza());
		builder.setRubrica(protocollazioneBean.getRubrica());
		builder.setSezione(protocollazioneBean.getSezione());
		builder.setTipologiadocumento(protocollazioneBean.getTipologiadocumento());
		builder.setTitolo(protocollazioneBean.getTitolo());
		builder.setNote(protocollazioneBean.getNote());
		builder.setUtenteprotocollazione(protocollazioneBean.getUtenteprotocollazione());
		builder.setAnnoRegistro(protocollazioneBean.getAnnoRegistro());
		builder.setNumeroRegistro(protocollazioneBean.getNumeroRegistro());
		builder.setTipoProtocollazione(protocollazioneBean.getTipoProtocollazione());

		Protocollazione protocollazione = builder.construct();

		List<Protocollazione> protocollazioniCollegate = capofila.getProtocollazioniCollegate();
		if (protocollazioniCollegate == null)
			protocollazioniCollegate = new ArrayList<DatiFascicolo.Protocollazione>();
		protocollazioniCollegate.add(protocollazione);

		aggiungiProtocollazioneAPratiche(protocollazioneBean.getPraticheCollegate(), protocollazione, numeroPgCapofila, annoPgCapofila);

		// Operazione di tracciatura eventuale dell'iter
		generaEventoPerProtocollazione(EventiIterFascicolo.PROTOCOLLA_CON_CAPOFILA, protocollazioneBean.getNumeroPG(), protocollazioneBean.getAnnoPG(), task.getCurrentUser(),
				protocollazioneBean.getNumeroPG(), protocollazioneBean.getAnnoPG().toString(), numeroPgCapofila, annoPgCapofila);

	}

	@Override
	protected boolean controllaAbilitazioneInterna() {
		return !getDatiFascicolo().getStato().equals(Stato.ARCHIVIATO);
	}

	@Override
	protected TipoApiTask getTipoApiTask() {
		return TipoApiTask.PROTOCOLLAZIONE;
	}

	/* metodi privati */
	private void aggiungiProtocollazioneAPratiche(Collection<Pratica<?>> praticheCollegate, ProtocollazioneCapofila protocollazioneCapofila) {
		ProtocollazionePECBuilder protocollazionePECBuilder = new ProtocollazionePECBuilder();

		protocollazionePECBuilder.setAnnoPG(protocollazioneCapofila.getAnnoPG().intValue());
		if (protocollazioneCapofila.getAnnoRegistro() != null)
			protocollazionePECBuilder.setAnnoRegistro(protocollazioneCapofila.getAnnoRegistro().intValue());
		protocollazionePECBuilder.setDataprotocollazione(protocollazioneCapofila.getDataprotocollazione());
		protocollazionePECBuilder.setNumeroFascicolo(protocollazioneCapofila.getNumeroFascicolo());
		protocollazionePECBuilder.setAnnoFascicolo(protocollazioneCapofila.getAnnoFascicolo());
		protocollazionePECBuilder.setNumeroPG(protocollazioneCapofila.getNumeroPG());
		protocollazionePECBuilder.setNumeroRegistro(protocollazioneCapofila.getNumeroRegistro());
		protocollazionePECBuilder.setOggetto(protocollazioneCapofila.getOggetto());
		protocollazionePECBuilder.setProvenienza(protocollazioneCapofila.getProvenienza());
		protocollazionePECBuilder.setRubrica(protocollazioneCapofila.getRubrica());
		protocollazionePECBuilder.setSezione(protocollazioneCapofila.getSezione());
		protocollazionePECBuilder.setTipologiadocumento(protocollazioneCapofila.getTipologiadocumento());
		protocollazionePECBuilder.setTitolo(protocollazioneCapofila.getTitolo());
		protocollazionePECBuilder.setUtenteprotocollazione(protocollazioneCapofila.getUtenteprotocollazione());
		protocollazionePECBuilder.setTipoProtocollazione(protocollazioneCapofila.getTipoProtocollazione());

		ProtocollazionePraticaModulisticaBuilder protocollazionePraticaModulisticaBuilder = new ProtocollazionePraticaModulisticaBuilder();
		protocollazionePraticaModulisticaBuilder.setAnnoPG(protocollazioneCapofila.getAnnoPG().intValue());
		if (protocollazioneCapofila.getAnnoRegistro() != null)
			protocollazionePraticaModulisticaBuilder.setAnnoRegistro(protocollazioneCapofila.getAnnoRegistro().intValue());
		protocollazionePraticaModulisticaBuilder.setDataprotocollazione(protocollazioneCapofila.getDataprotocollazione());
		protocollazionePraticaModulisticaBuilder.setNumeroFascicolo(protocollazioneCapofila.getNumeroFascicolo());
		protocollazionePraticaModulisticaBuilder.setAnnoFascicolo(protocollazioneCapofila.getAnnoFascicolo());
		protocollazionePraticaModulisticaBuilder.setNumeroPG(protocollazioneCapofila.getNumeroPG());
		protocollazionePraticaModulisticaBuilder.setNumeroRegistro(protocollazioneCapofila.getNumeroRegistro());
		protocollazionePraticaModulisticaBuilder.setOggetto(protocollazioneCapofila.getOggetto());
		protocollazionePraticaModulisticaBuilder.setProvenienza(protocollazioneCapofila.getProvenienza());
		protocollazionePraticaModulisticaBuilder.setRubrica(protocollazioneCapofila.getRubrica());
		protocollazionePraticaModulisticaBuilder.setSezione(protocollazioneCapofila.getSezione());
		protocollazionePraticaModulisticaBuilder.setTipologiadocumento(protocollazioneCapofila.getTipologiadocumento());
		protocollazionePraticaModulisticaBuilder.setTitolo(protocollazioneCapofila.getTitolo());
		protocollazionePraticaModulisticaBuilder.setUtenteprotocollazione(protocollazioneCapofila.getUtenteprotocollazione());
		protocollazionePraticaModulisticaBuilder.setTipoProtocollazione(protocollazioneCapofila.getTipoProtocollazione());

		for (Pratica<?> pratica : praticheCollegate) {
			if (pratica instanceof PraticaEmail) {
				DatiPraticaEmailTaskAdapter datiPraticaEmailTaskAdapter = (DatiPraticaEmailTaskAdapter) ((XMLPraticaEmail) pratica).getDatiPraticaTaskAdapter();
				datiPraticaEmailTaskAdapter.setProtocollazionePEC(protocollazionePECBuilder.construct());
				if (pratica instanceof PraticaEmailIn)
					datiPraticaEmailTaskAdapter.setStato(it.eng.consolepec.xmlplugin.pratica.email.DatiEmail.Stato.ARCHIVIATA);
			} else {
				DatiPraticaModulisticaTaskAdapter datiPraticaModulisticaTaskAdapter = ((XMLPraticaModulistica) pratica).getDatiPraticaTaskAdapter();
				datiPraticaModulisticaTaskAdapter.setProtocollazionePaticaModulistica(protocollazionePraticaModulisticaBuilder.construct());
				datiPraticaModulisticaTaskAdapter.setStato(it.eng.consolepec.xmlplugin.pratica.modulistica.DatiModulistica.Stato.ARCHIVIATA);
			}
		}
	}

	private void aggiungiProtocollazioneAPratiche(Collection<Pratica<?>> praticheCollegate, Protocollazione protocollazione, String numeroPgCapofila,
			String annoPgCapofila) throws NumberFormatException {

		ProtocollazionePECBuilder protocollazionePECBuilder = new ProtocollazionePECBuilder();

		protocollazionePECBuilder.setAnnoPG(protocollazione.getAnnoPG().intValue());
		if (protocollazione.getAnnoRegistro() != null)
			protocollazionePECBuilder.setAnnoRegistro(protocollazione.getAnnoRegistro().intValue());
		protocollazionePECBuilder.setDataprotocollazione(protocollazione.getDataprotocollazione());
		protocollazionePECBuilder.setNumeroFascicolo(protocollazione.getNumeroFascicolo());
		protocollazionePECBuilder.setAnnoFascicolo(protocollazione.getAnnoFascicolo());
		protocollazionePECBuilder.setNumeroPG(protocollazione.getNumeroPG());
		protocollazionePECBuilder.setNumeroRegistro(protocollazione.getNumeroRegistro());
		protocollazionePECBuilder.setOggetto(protocollazione.getOggetto());
		protocollazionePECBuilder.setProvenienza(protocollazione.getProvenienza());
		protocollazionePECBuilder.setRubrica(protocollazione.getRubrica());
		protocollazionePECBuilder.setSezione(protocollazione.getSezione());
		protocollazionePECBuilder.setTipologiadocumento(protocollazione.getTipologiadocumento());
		protocollazionePECBuilder.setTitolo(protocollazione.getTitolo());
		protocollazionePECBuilder.setUtenteprotocollazione(protocollazione.getUtenteprotocollazione());
		protocollazionePECBuilder.setTipoProtocollazione(protocollazione.getTipoProtocollazione());

		PG capofila = new PG();
		capofila.setAnnoPG(Integer.parseInt(annoPgCapofila));
		capofila.setNumeroPG(numeroPgCapofila);
		protocollazionePECBuilder.setCapofila(capofila);

		ProtocollazionePraticaModulisticaBuilder protocollazionePraticaModulisticaBuilder = new ProtocollazionePraticaModulisticaBuilder();
		if (protocollazione.getAnnoRegistro() != null)
			protocollazionePraticaModulisticaBuilder.setAnnoRegistro(protocollazione.getAnnoRegistro().intValue());
		protocollazionePraticaModulisticaBuilder.setDataprotocollazione(protocollazione.getDataprotocollazione());
		protocollazionePraticaModulisticaBuilder.setNumeroFascicolo(protocollazione.getNumeroFascicolo());
		protocollazionePraticaModulisticaBuilder.setAnnoFascicolo(protocollazione.getAnnoFascicolo());
		protocollazionePraticaModulisticaBuilder.setNumeroPG(protocollazione.getNumeroPG());
		protocollazionePraticaModulisticaBuilder.setNumeroRegistro(protocollazione.getNumeroRegistro());
		protocollazionePraticaModulisticaBuilder.setOggetto(protocollazione.getOggetto());
		protocollazionePraticaModulisticaBuilder.setProvenienza(protocollazione.getProvenienza());
		protocollazionePraticaModulisticaBuilder.setRubrica(protocollazione.getRubrica());
		protocollazionePraticaModulisticaBuilder.setSezione(protocollazione.getSezione());
		protocollazionePraticaModulisticaBuilder.setTipologiadocumento(protocollazione.getTipologiadocumento());
		protocollazionePraticaModulisticaBuilder.setTitolo(protocollazione.getTitolo());
		protocollazionePraticaModulisticaBuilder.setUtenteprotocollazione(protocollazione.getUtenteprotocollazione());
		protocollazionePraticaModulisticaBuilder.setTipoProtocollazione(protocollazione.getTipoProtocollazione());

		it.eng.consolepec.xmlplugin.pratica.modulistica.DatiModulistica.PG capofilaModulistica = new it.eng.consolepec.xmlplugin.pratica.modulistica.DatiModulistica.PG();
		capofilaModulistica.setAnnoPG(Integer.parseInt(annoPgCapofila));
		capofilaModulistica.setNumeroPG(numeroPgCapofila);
		protocollazionePraticaModulisticaBuilder.setCapofila(capofilaModulistica);

		for (Pratica<?> pratica : praticheCollegate) {
			if (pratica instanceof PraticaEmail) {
				DatiPraticaEmailTaskAdapter datiPraticaEmailTaskAdapter = (DatiPraticaEmailTaskAdapter) ((XMLPraticaEmail) pratica).getDatiPraticaTaskAdapter();
				datiPraticaEmailTaskAdapter.setProtocollazionePEC(protocollazionePECBuilder.construct());
				if (pratica instanceof PraticaEmailIn)
					datiPraticaEmailTaskAdapter.setStato(it.eng.consolepec.xmlplugin.pratica.email.DatiEmail.Stato.ARCHIVIATA);
			} else {
				DatiPraticaModulisticaTaskAdapter datiPraticaModulisticaTaskAdapter = ((XMLPraticaModulistica) pratica).getDatiPraticaTaskAdapter();
				datiPraticaModulisticaTaskAdapter.setProtocollazionePaticaModulistica(protocollazionePraticaModulisticaBuilder.construct());
				datiPraticaModulisticaTaskAdapter.setStato(it.eng.consolepec.xmlplugin.pratica.modulistica.DatiModulistica.Stato.ARCHIVIATA);
			}

		}
	}

	private void controllaProtocollazioneEsistente(String numeroPG, Integer annoPG) {
		for (ProtocollazioneCapofila protocollazione : getDatiFascicolo().getProtocollazioniCapofila()) {
			if (protocollazione.getNumeroPG().equals(numeroPG) && protocollazione.getAnnoPG().equals(annoPG)) {
				throw new PraticaException("Protocollazione già esistente: " + numeroPG + "/" + annoPG);
			}
			for (Protocollazione protcollazioneCollegata : protocollazione.getProtocollazioniCollegate()) {
				if (protcollazioneCollegata.getNumeroPG().equals(numeroPG) && protcollazioneCollegata.getAnnoPG().equals(annoPG)) {
					throw new PraticaException("Protocollazione già esistente: " + numeroPG + "/" + annoPG);
				}
			}
		}
	}

	private boolean check(Allegato allegato) {
		return (TaskDiFirmaUtil.getApprovazioneFirmaTaskAttivoByAllegato(task.getEnclosingPratica(), allegato) == null) //
				&& (allegato.getLock() == null || !Boolean.TRUE.equals(allegato.getLock())) //
				&& (!XmlPluginUtil.isAllegatoProtocollato(allegato, task.getEnclosingPratica())); //
	}
}
