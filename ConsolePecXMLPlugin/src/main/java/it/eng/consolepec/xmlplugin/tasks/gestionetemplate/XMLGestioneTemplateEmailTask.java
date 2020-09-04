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
import it.eng.consolepec.xmlplugin.jaxb.GestioneTemplate;
import it.eng.consolepec.xmlplugin.jaxb.Task;
import it.eng.consolepec.xmlplugin.pratica.template.TemplateEmail;
import it.eng.consolepec.xmlplugin.pratica.template.XMLTemplateEmail;
import it.eng.consolepec.xmlplugin.tasks.gestionetemplate.operazioni.AggiungiAllegatoTemplateTaskApi;
import it.eng.consolepec.xmlplugin.tasks.gestionetemplate.operazioni.RimuoviAllegatoTemplateTaskApi;
import it.eng.consolepec.xmlplugin.tasks.gestionetemplate.operazioni.TipoApiTaskTemplate;
import it.eng.consolepec.xmlplugin.tasks.gestionetemplate.operazioni.impl.AggiungiAllegatoTemplateTaskApiImpl;
import it.eng.consolepec.xmlplugin.tasks.gestionetemplate.operazioni.impl.CreaComunicazioneDaTemplateTaskApiImpl;
import it.eng.consolepec.xmlplugin.tasks.gestionetemplate.operazioni.impl.RimuoviAllegatoTaskApiImpl;

public class XMLGestioneTemplateEmailTask extends XMLGestioneAbstractTemplateTask<DatiGestioneTemplateEmailTask> implements GestioneTemplateEmailTask {

	private DatiGestioneTemplateEmailTask dati = null;
	protected Logger logger = LoggerFactory.getLogger(XMLGestioneTemplateEmailTask.class);
	protected XMLTemplateEmail pratica;

	/* constructor */

	public XMLGestioneTemplateEmailTask() {

	}

	@Override
	public DatiGestioneTemplateEmailTask getDati() {
		return dati;
	}

	@Override
	public TemplateEmail getEnclosingPratica() {

		return pratica;
	}

	/* metodi protected da XMLTask */

	@Override
	protected void loadDatiTask(Pratica<?> pratica, Task t) {

		if (!(pratica instanceof XMLTemplateEmail) || t.getGestioneTemplate() == null)
			throw new PraticaException("Il task di tipo GestioneTemplate supporta solo pratiche di tipo Template e deve essere basato su Task GestioneTemplate");
		this.pratica = (XMLTemplateEmail) pratica;

		DatiGestioneTemplateEmailTask.Builder builder = new DatiGestioneTemplateEmailTask.Builder();
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
			DatiGestioneTemplateEmailTask dati = getDati();

			super.serializeDatiTaskGenerico(out, dati, praticajaxb);
			out.setGestioneTemplate(new GestioneTemplate());
			praticajaxb.getTasks().getTask().add(out);

		} catch (Throwable t) {
			throw new PraticaException(t, "Errore in fase di serializzazione task gestionePEC");
		}
	}

	@Override
	protected void initTask(Pratica<?> pratica, DatiGestioneTemplateEmailTask dati) {

		if (!(pratica instanceof XMLTemplateEmail))
			throw new PraticaException("Il task di tipo GestioneTemplate supporta solo pratiche di tipo XMLPraticaTemplate");
		this.pratica = (XMLTemplateEmail) pratica;
		this.dati = dati;
		dati.setId(getNextId(pratica));
		dati.setAttivo(true);
		pratica.getDati().setAssegnatarioCorrente(dati.getAssegnatario().getNome());
		initApiTask();
	}

	@Override
	public void aggiungiAllegato(Allegato allegato, AggiungiAllegato handler) throws ApplicationException, InvalidArgumentException {
		checkAbilitazione(TipoApiTaskTemplate.AGGIUNGI_ALLEGATO);
		((AggiungiAllegatoTemplateTaskApi) operazioni.get(TipoApiTaskTemplate.AGGIUNGI_ALLEGATO)).aggiungiAllegato(allegato, handler);
	}

	@Override
	public void rimuoviAllegato(Allegato allegato) {
		checkAbilitazione(TipoApiTaskTemplate.RIMUOVI_ALLEGATO);
		((RimuoviAllegatoTemplateTaskApi) operazioni.get(TipoApiTaskTemplate.RIMUOVI_ALLEGATO)).rimuoviAllegato(allegato);
	}

	@Override
	protected void initApiTask() {

		super.initApiTask();
		operazioni.put(TipoApiTaskTemplate.AGGIUNGI_ALLEGATO, new AggiungiAllegatoTemplateTaskApiImpl<DatiGestioneTemplateEmailTask>(this));
		operazioni.put(TipoApiTaskTemplate.RIMUOVI_ALLEGATO, new RimuoviAllegatoTaskApiImpl<DatiGestioneTemplateEmailTask>(this));
		operazioni.put(TipoApiTaskTemplate.CREA_COMUNICAZIONE, new CreaComunicazioneDaTemplateTaskApiImpl<DatiGestioneTemplateEmailTask>(this));

	}
}
