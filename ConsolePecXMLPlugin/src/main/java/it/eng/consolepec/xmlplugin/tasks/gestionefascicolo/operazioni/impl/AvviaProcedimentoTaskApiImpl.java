package it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.impl;

import it.eng.consolepec.xmlplugin.pratica.fascicolo.DatiFascicolo.Procedimento;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.DatiFascicolo.ProtocollazioneCapofila;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.DatiFascicolo.Stato;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.XMLTaskFascicolo;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.fascicolo.DatiGestioneFascicoloTask;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.AvviaProcedimentoTaskApi;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.TipoApiTask;
import it.eng.consolepec.xmlplugin.util.GestioneProcedimentoBean;
import it.eng.consolepec.xmlplugin.util.XmlPluginUtil;

import java.util.List;
import java.util.TreeSet;

public class AvviaProcedimentoTaskApiImpl<T extends DatiGestioneFascicoloTask> extends AbstractTaskApiImpl<T> implements AvviaProcedimentoTaskApi {

	public AvviaProcedimentoTaskApiImpl(XMLTaskFascicolo<T> task) {
		super(task);
	}

	@Override
	public void avviaProcedimento(GestioneProcedimentoBean avviaProcedimentoBean) {
		Procedimento p = XmlPluginUtil.convertiGestioneProcedimentoBean(avviaProcedimentoBean);

		boolean isCapofila = false;
		List<ProtocollazioneCapofila> capofila = getDatiFascicolo().getProtocollazioniCapofila();
		for (ProtocollazioneCapofila pc : capofila)
			if (pc.getAnnoPG().equals(avviaProcedimentoBean.getPg().getAnnoPG().intValue()) && pc.getNumeroPG().equals(avviaProcedimentoBean.getPg().getNumeroPG()))
				isCapofila = true;

		if (!isCapofila)
			throw new IllegalArgumentException("il PG non è capofila");

		TreeSet<Procedimento> procedimenti = getDatiFascicolo().getProcedimenti();
		for (Procedimento pc : procedimenti)
			if (pc.getAnnoPG().equals(avviaProcedimentoBean.getPg().getAnnoPG().intValue()) && pc.getNumeroPG().equals(avviaProcedimentoBean.getPg().getNumeroPG()) && pc.getCodTipologiaProcedimento() == p.getCodTipologiaProcedimento())
				throw new IllegalArgumentException("il capofila ha già un procedimento associato");

		getDatiFascicolo().getProcedimenti().add(p);

		generaEvento( EventiIterFascicolo.AVVIA_PROCEDIMENTO, task.getCurrentUser(), p.getCodTipologiaProcedimento(), p.getNumeroPG(), p.getAnnoPG());

	}

	@Override
	protected boolean controllaAbilitazioneInterna() {
		return (getDatiFascicolo().getStato().equals(Stato.IN_GESTIONE) || getDatiFascicolo().getStato().equals(Stato.IN_VISIONE) || getDatiFascicolo().getStato().equals(Stato.IN_AFFISSIONE) || getDatiFascicolo().getStato().equals(Stato.DA_INOLTRARE_ESTERNO)) && getDatiFascicolo().getProtocollazioniCapofila().size() > 0;
	}

	@Override
	protected TipoApiTask getTipoApiTask() {
		return TipoApiTask.AVVIA_PROCEDIMENTO;
	}

}
