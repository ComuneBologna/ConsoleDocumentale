package it.eng.consolepec.xmlplugin.pratica.template;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.eng.consolepec.xmlplugin.exception.PraticaException;
import it.eng.consolepec.xmlplugin.factory.DatiPratica;
import it.eng.consolepec.xmlplugin.factory.DatiPratica.Allegato;
import it.eng.consolepec.xmlplugin.jaxb.Documento;
import it.eng.consolepec.xmlplugin.jaxb.Template;
import it.eng.consolepec.xmlplugin.jaxb.Template.Allegati;
import it.eng.consolepec.xmlplugin.util.XmlPluginUtil;

public class XMLTemplateDocumentoPDF extends XMLAbstractTemplate<DatiTemplateDocumentoPDF> implements TemplateDocumentoPDF {

	protected Logger logger = LoggerFactory.getLogger(XMLTemplateDocumentoPDF.class);

	public XMLTemplateDocumentoPDF() {
		// richiesto da reflection
	}

	@Override
	public DatiTemplateDocumentoPDF getDati() {
		return dati;
	}

	@Override
	protected void initPratica(DatiTemplateDocumentoPDF dati) throws PraticaException {
		this.dati = dati;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof XMLTemplateDocumentoPDF) {
			XMLTemplateDocumentoPDF other = (XMLTemplateDocumentoPDF) obj;
			return other.getAlfrescoPath().equalsIgnoreCase(getAlfrescoPath());
		} else {
			return false;
		}
	}

	@Override
	protected void serializeDatiTemplate(it.eng.consolepec.xmlplugin.jaxb.Pratica jaxb) {

		Template t = new it.eng.consolepec.xmlplugin.jaxb.Template();

		t.setNome(getDati().getNome());
		t.setDescrizione(getDati().getDescrizione());

		t.setAllegati(new Allegati());
		t.getAllegati().getDocumento().addAll(getAllegati());

		t.getFascicoliAbilitati().addAll(getDati().getFascicoliAbilitati());
		t.setTitoloFile(getDati().getTitoloFile());
		t.getCampo().addAll(getCampi());

		t.setEventiIter(new it.eng.consolepec.xmlplugin.jaxb.Template.EventiIter());
		t.getEventiIter().getEventoIter().addAll(getEventiIter());

		jaxb.setTemplate(t);
	}

	@Override
	protected void loadDatiTemplate(it.eng.consolepec.xmlplugin.jaxb.Pratica jaxb) {

		DatiTemplateDocumentoPDF.Builder builder = new DatiTemplateDocumentoPDF.Builder();
		loadDatiComuniBuilder(builder, jaxb);

		Template t = jaxb.getTemplate();

		builder.setNome(t.getNome());
		builder.setDescrizione(t.getDescrizione());

		List<CampoTemplate> campi = new ArrayList<CampoTemplate>();
		campi.addAll(getCampi(t.getCampo()));
		builder.setCampi(campi);

		builder.setFascicoliAbilitati(t.getFascicoliAbilitati());
		builder.setTitoloFile(t.getTitoloFile());

		dati = builder.construct();

		Allegati allg = t.getAllegati();
		Set<Allegato> allegati = new LinkedHashSet<DatiPratica.Allegato>();
		if (allg != null) {
			for (Documento doc : allg.getDocumento()) {
				Allegato allegato = XmlPluginUtil.getAllegatoFromDocumento(doc, dati);
				allegati.add(allegato);
			}
		}
		dati.getAllegati().addAll(new ArrayList<Allegato>(allegati));

		dati.getIter().addAll(getEventiIterPratica(t.getEventiIter().getEventoIter()));
	}
}
