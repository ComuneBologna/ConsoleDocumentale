package it.eng.consolepec.xmlplugin.tasks.gestionetemplate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.eng.cobo.consolepec.commons.exception.ApplicationException;
import it.eng.cobo.consolepec.commons.exception.InvalidArgumentException;
import it.eng.consolepec.xmlplugin.exception.PraticaException;
import it.eng.consolepec.xmlplugin.factory.DatiPratica.Allegato;
import it.eng.consolepec.xmlplugin.factory.DatiTask.Assegnatario;
import it.eng.consolepec.xmlplugin.factory.Pratica;
import it.eng.consolepec.xmlplugin.factory.TaskObserver.AggiungiAllegato;
import it.eng.consolepec.xmlplugin.jaxb.GestioneTemplateDocumentoPDF;
import it.eng.consolepec.xmlplugin.jaxb.Task;
import it.eng.consolepec.xmlplugin.pratica.template.TemplateDocumentoPDF;
import it.eng.consolepec.xmlplugin.pratica.template.XMLTemplateDocumentoPDF;
import it.eng.consolepec.xmlplugin.tasks.gestionetemplate.operazioni.TipoApiTaskTemplate;
import it.eng.consolepec.xmlplugin.tasks.gestionetemplate.operazioni.impl.CaricaModelloOdtTemplateTaskApiImpl;

public class XMLGestioneTemplateDocumentoPDFTask extends XMLGestioneAbstractTemplateTask<DatiGestioneTemplateDocumentoPDFTask> implements GestioneTemplateDocumentoPDFTask {

	private DatiGestioneTemplateDocumentoPDFTask dati = null;
	protected Logger logger = LoggerFactory.getLogger(XMLGestioneTemplateDocumentoPDFTask.class);
	protected XMLTemplateDocumentoPDF pratica;

	/* constructor */

	public XMLGestioneTemplateDocumentoPDFTask() {

	}

	@Override
	public DatiGestioneTemplateDocumentoPDFTask getDati() {
		return dati;
	}

	@Override
	public TemplateDocumentoPDF getEnclosingPratica() {

		return pratica;
	}

	/* metodi protected da XMLTask */

	@Override
	protected void loadDatiTask(Pratica<?> pratica, Task t) {

		if (!(pratica instanceof XMLTemplateDocumentoPDF) || t.getGestioneTemplateDocumentoPDF() == null)
			throw new PraticaException("Il task di tipo GestioneTemplateDocumentoPDF supporta solo pratiche di tipo TemplateDocumentoPDF e deve essere basato su Task GestioneTemplateDocumentoPDF");
		this.pratica = (XMLTemplateDocumentoPDF) pratica;

		DatiGestioneTemplateDocumentoPDFTask.Builder builder = new DatiGestioneTemplateDocumentoPDFTask.Builder();
		Assegnatario asscorrente = assegnatarioFromJaxb(t.getAssegnatari().getAssegnatarioCorrente());
		pratica.getDati().setAssegnatarioCorrente(asscorrente.getNome());

		builder.setAssegnatario(asscorrente);
		builder.setAttivo(true);
		dati = builder.construct();

		initApiTask();

		super.loadDatiTaskGenerico(t, dati);
	}

	@Override
	protected void serializeDati(it.eng.consolepec.xmlplugin.jaxb.Pratica praticajaxb) {

		try {
			Task out = new Task();
			DatiGestioneTemplateDocumentoPDFTask dati = getDati();

			super.serializeDatiTaskGenerico(out, dati, praticajaxb);
			out.setGestioneTemplateDocumentoPDF(new GestioneTemplateDocumentoPDF());
			praticajaxb.getTasks().getTask().add(out);

		} catch (Throwable t) {
			throw new PraticaException(t, "Errore in fase di serializzazione task gestioneTemplateDocumentoPDF");
		}
	}

	@Override
	protected void initTask(Pratica<?> pratica, DatiGestioneTemplateDocumentoPDFTask dati) {
		if (!(pratica instanceof XMLTemplateDocumentoPDF))
			throw new PraticaException("Il task di tipo GestioneTemplateDocumentoPDF supporta solo pratiche di tipo XMLPraticaTemplateDocumentoPDF");
		this.pratica = (XMLTemplateDocumentoPDF) pratica;
		this.dati = dati;
		dati.setId(getNextId(pratica));
		dati.setAttivo(true);
		pratica.getDati().setAssegnatarioCorrente(dati.getAssegnatario().getNome());
		initApiTask();
	}

	@Override
	protected void initApiTask() {

		super.initApiTask();
		operazioni.put(TipoApiTaskTemplate.CARICA_ODT, new CaricaModelloOdtTemplateTaskApiImpl(this));
	}

	@Override
	public void caricaODT(Allegato doc, AggiungiAllegato handler) throws ApplicationException, InvalidArgumentException {
		checkAbilitazione(TipoApiTaskTemplate.CARICA_ODT);
		((CaricaModelloOdtTemplateTaskApiImpl) operazioni.get(TipoApiTaskTemplate.CARICA_ODT)).caricaODT(doc, handler);
	}
}
