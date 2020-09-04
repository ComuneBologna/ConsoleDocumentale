package it.eng.consolepec.xmlplugin.factory;

import it.eng.cobo.consolepec.util.pratica.PraticaUtil;
import it.eng.consolepec.xmlplugin.exception.PraticaException;
import it.eng.consolepec.xmlplugin.pratica.factory.FascicoliFactory;
import it.eng.consolepec.xmlplugin.tasks.gestionecomunicazione.XMLGestioneComunicazioneTask;
import it.eng.consolepec.xmlplugin.tasks.gestionemodulistica.XMLGestionePraticaModulisticaTask;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.XMLGestionePECInTask;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.XMLGestionePECOutTask;
import it.eng.consolepec.xmlplugin.tasks.gestionetemplate.XMLGestioneTemplateDocumentoPDFTask;
import it.eng.consolepec.xmlplugin.tasks.gestionetemplate.XMLGestioneTemplateEmailTask;
import it.eng.consolepec.xmlplugin.tasks.riattiva.XMLRiattivaFascicoloTask;
import it.eng.consolepec.xmlplugin.tasks.riattiva.XMLRiattivaPECInTask;
import it.eng.consolepec.xmlplugin.tasks.riattiva.XMLRiattivaPECOutTask;
import it.eng.consolepec.xmlplugin.tasks.riattiva.XMLRiattivaPraticaModulisticaTask;
import it.eng.consolepec.xmlplugin.tasks.riattiva.XMLRiattivaTemplateTask;
import it.eng.consolepec.xmlplugin.tasks.richiestafirma.XMLApprovazioneFirmaTask;

import java.io.Reader;
import java.util.HashSet;
import java.util.Set;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractXMLTaskFactory extends TaskFactory {

	private Logger logger = LoggerFactory.getLogger(XMLTaskFactory.class);

	private static JAXBContext jaxbContext = null;

	private JAXBContext getJaxbContext() {
		if (jaxbContext == null) {
			synchronized (this) {
				if (jaxbContext == null) {
					try {
						jaxbContext = JAXBContext.newInstance(it.eng.consolepec.xmlplugin.jaxb.Pratica.class);
					} catch (JAXBException e) {
						logger.error("Errore nella creazione del contesto Jaxb.", e);
					}
				}
			}
		}
		return jaxbContext;
	}

	// TODO refactor
	@Override
	public Set<Task<?>> loadPraticaTasks(Reader reader, Pratica<? extends DatiPratica> pratica) {
		Set<Task<?>> tasks = new HashSet<Task<?>>();
		// carica jaxb
		try {
			logger.debug("loadPraticaTasks");
			it.eng.consolepec.xmlplugin.jaxb.Pratica praticaJaxb = deserialize(reader);
			// scorri i tasks
			// TODO reflection?
			if (praticaJaxb.getTasks() != null) {
				for (it.eng.consolepec.xmlplugin.jaxb.Task pectask : praticaJaxb.getTasks().getTask()) {
					XMLTask<?> task = null;
					String tp = praticaJaxb.getTipo();
					if (pectask.getGestionePEC() != null) {
						if (PraticaUtil.isIngresso(tp))
							task = new XMLGestionePECInTask();
						else if (PraticaUtil.isEmailOut(tp))
							task = new XMLGestionePECOutTask();
					} else if (pectask.getGestioneFascicolo() != null) {
						task = FascicoliFactory.getTaskImpl(tp);
					} else if (pectask.getGestioneModulistica() != null) {
						task = new XMLGestionePraticaModulisticaTask();
					} else if (pectask.getGestioneTemplateDocumentoPDF() != null) {
						task = new XMLGestioneTemplateDocumentoPDFTask();
					}else if (pectask.getGestioneTemplate() != null) {
						task = new XMLGestioneTemplateEmailTask();
					} else if (pectask.getGestioneComunicazione() != null) {
						task = new XMLGestioneComunicazioneTask();
					} else if (pectask.getRiportaInGestione() != null) {
						if (PraticaUtil.isIngresso(tp))
							task = new XMLRiattivaPECInTask();
						else if (PraticaUtil.isEmailOut(tp))
							task = new XMLRiattivaPECOutTask();
						else if (PraticaUtil.isPraticaModulistica(tp))
							task = new XMLRiattivaPraticaModulisticaTask();
						else if (PraticaUtil.isModello(tp))
							task = new XMLRiattivaTemplateTask();
						else
							task = new XMLRiattivaFascicoloTask();
					} else if(pectask.getRichiestaFirma() != null){
						task = new XMLApprovazioneFirmaTask();
					} else
						throw new PraticaException("Tipo di task non determinato dall'xml");
					task.loadDatiTask(pratica, pectask);
					tasks.add(task);
				}
			}

			return tasks;
		} catch (Throwable t) {
			throw new PraticaException(t, "Errore in fase di deserializzazione dei task");
		}
	}

	protected it.eng.consolepec.xmlplugin.jaxb.Pratica deserialize(Reader reader) throws JAXBException {
		Unmarshaller un = getJaxbContext().createUnmarshaller();
		it.eng.consolepec.xmlplugin.jaxb.Pratica praticaJaxb = (it.eng.consolepec.xmlplugin.jaxb.Pratica) un.unmarshal(reader);
		return praticaJaxb;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends Task<?>, Z extends DatiTask> T newTaskInstance(Class<T> clazz, Pratica<?> pratica, Z bean) {
		try {

			@SuppressWarnings("rawtypes")
			XMLTask xmlTask = (XMLTask<?>) loadImplementation(clazz);
			xmlTask.initTask(pratica, bean);
			pratica.putTask(xmlTask);
			T res = (T) xmlTask;
			return res;
		} catch (Throwable t) {
			throw new PraticaException("Errore in fase di creazione istanza di Task");
		}
	}

	// private <T extends Task<?>> Task<?> loadImplementation(Class<T> clazz) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
	// Task<?> task = ServiceLoader.load(clazz, this.getClass().getClassLoader()).iterator().next();
	// return task;
	// }

	protected abstract <T extends Task<?>> Task<?> loadImplementation(Class<T> clazz) throws ClassNotFoundException, InstantiationException, IllegalAccessException;

}
