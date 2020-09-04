package it.eng.consolepec.xmlplugin.tasks.gestionetemplate;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import it.eng.consolepec.xmlplugin.exception.PraticaException;
import it.eng.consolepec.xmlplugin.factory.DatiPratica.Utente;
import it.eng.consolepec.xmlplugin.factory.DatiTask.TipoTask;
import it.eng.consolepec.xmlplugin.factory.XMLTask;
import it.eng.consolepec.xmlplugin.tasks.esecuzioni.ContestoEsecuzione;
import it.eng.consolepec.xmlplugin.tasks.gestionetemplate.operazioni.GestionePresaInCaricoApiTask;
import it.eng.consolepec.xmlplugin.tasks.gestionetemplate.operazioni.ModificaTemplateTaskApi;
import it.eng.consolepec.xmlplugin.tasks.gestionetemplate.operazioni.TipoApiTaskTemplate;
import it.eng.consolepec.xmlplugin.tasks.gestionetemplate.operazioni.impl.CreaTemplatePerCopiaTaskApiImpl;
import it.eng.consolepec.xmlplugin.tasks.gestionetemplate.operazioni.impl.EliminaTaskTemplateApiImpl;
import it.eng.consolepec.xmlplugin.tasks.gestionetemplate.operazioni.impl.GestionePresaInCaricoApiTaskImpl;
import it.eng.consolepec.xmlplugin.tasks.gestionetemplate.operazioni.impl.ModificaTaskTemplateApiImpl;

public abstract class XMLGestioneAbstractTemplateTask<T extends DatiGestioneAbstractTemplateTask> extends XMLTask<T> implements GestioneAbstractTemplateTask<T> {

	private final ContestoEsecuzione<GestioneAbstractTemplateTask<?>> contestoEsecuzione = new ContestoEsecuzione<GestioneAbstractTemplateTask<?>>();

	@SuppressWarnings("unchecked")
	@Override
	public <TS extends it.eng.consolepec.xmlplugin.factory.Task<?>> ContestoEsecuzione<TS> getContestoEsecuzione() {
		return (ContestoEsecuzione<TS>) contestoEsecuzione;
	}

	@Override
	public void prendiInCarico(Utente user) {
		super.prendiInCarico(user);
		((GestionePresaInCaricoApiTask) operazioni.get(TipoApiTaskTemplate.PRESA_IN_CARICO)).prendiInCarico(user);
	}

	@Override
	public void rilasciaInCarico(Utente user) {
		super.rilasciaInCarico(user);
		((GestionePresaInCaricoApiTask) operazioni.get(TipoApiTaskTemplate.PRESA_IN_CARICO)).rilasciaInCarico(user);
	}

	@Override
	public Map<String, Object> getMetadata() {

		Map<String, Object> map = new HashMap<String, Object>();

		map.put("pDataInizio", getDati().getAssegnatario().getDataInizio());
		map.put("pDataFine", getDati().getAssegnatario().getDataFine());

		return map;
	}

	@Override
	public boolean isAttivo() {

		return getDati().getAttivo();
	}

	/* metodi object */

	@Override
	public int hashCode() {

		return getDati().getId().hashCode();
	}

	@Override
	public String toString() {

		return getDati().toString();
	};

	@Override
	public void riattivaConCheck() {
		if (getEnclosingPratica().isRiattivabile()) {
			riattiva();
		} else {
			throw new PraticaException("Il template non puo' essere riattivato.");
		}
	}

	private void riattiva() {
		getDati().setAttivo(true);
		getDati().getAssegnatario().setDataFine(null);
	}

	@Override
	public void termina() {
		getDati().setAttivo(false);
		getDati().getAssegnatario().setDataFine(new Date());
	}

	@Override
	public TipoTask getTipo() {
		return TipoTask.GestioneTemplateTask;
	}

	@Override
	public boolean isOperazioneAbilitata() {
		return false;
	}

	@Override
	public void modificato() {
		checkAbilitazione(TipoApiTaskTemplate.MODIFICA);
		((ModificaTemplateTaskApi) operazioni.get(TipoApiTaskTemplate.MODIFICA)).modificato();

	}

	protected void initApiTask() {

		operazioni.put(TipoApiTaskTemplate.ELIMINA, new EliminaTaskTemplateApiImpl<T>(this));
		operazioni.put(TipoApiTaskTemplate.MODIFICA, new ModificaTaskTemplateApiImpl<T>(this));
		operazioni.put(TipoApiTaskTemplate.PRESA_IN_CARICO, new GestionePresaInCaricoApiTaskImpl<T>(this));
		operazioni.put(TipoApiTaskTemplate.CREA_TEMPLATE_PER_COPIA, new CreaTemplatePerCopiaTaskApiImpl<T>(this));
	}
}
