package it.eng.consolepec.xmlplugin.pratica.template;

import it.eng.consolepec.xmlplugin.exception.PraticaException;
import it.eng.consolepec.xmlplugin.factory.DatiPratica;
import it.eng.consolepec.xmlplugin.factory.DatiPratica.Allegato;
import it.eng.consolepec.xmlplugin.jaxb.Documento;
import it.eng.consolepec.xmlplugin.jaxb.Template;
import it.eng.consolepec.xmlplugin.jaxb.Template.Allegati;
import it.eng.consolepec.xmlplugin.util.XmlPluginUtil;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XMLTemplateEmail extends XMLAbstractTemplate<DatiTemplateEmail> implements TemplateEmail{


	protected Logger logger = LoggerFactory.getLogger(XMLTemplateEmail.class);

	public XMLTemplateEmail() {
		// richiesto da reflection
	}

	@Override
	public DatiTemplateEmail getDati() {
		return dati;
	}

	@Override
	protected void serializeDatiTemplate(it.eng.consolepec.xmlplugin.jaxb.Pratica jaxb) {

		Template t = new it.eng.consolepec.xmlplugin.jaxb.Template();

		t.setNome(getDati().getNome());
		t.setDescrizione(getDati().getDescrizione());

		t.setOggettoMail(getDati().getOggettoMail());
		t.setCorpoMail(getDati().getCorpoMail());
		t.setMittente(getDati().getMittente());
		t.getDestinatari().addAll(getDati().getDestinatari());
		t.getDestinatarioCC().addAll(getDati().getDestinatariCC());

		t.getCampo().addAll(getCampi());
		t.getFascicoliAbilitati().addAll(getDati().getFascicoliAbilitati());

		t.setAllegati(new Allegati());
		t.getAllegati().getDocumento().addAll(getAllegati());

		t.setEventiIter(new it.eng.consolepec.xmlplugin.jaxb.Template.EventiIter());
		t.getEventiIter().getEventoIter().addAll(getEventiIter());


		jaxb.setTemplate(t);
	}

	@Override
	protected void loadDatiTemplate(it.eng.consolepec.xmlplugin.jaxb.Pratica jaxb){

		DatiTemplateEmail.Builder builder = new DatiTemplateEmail.Builder();
		loadDatiComuniBuilder(builder, jaxb);

		Template t = jaxb.getTemplate();

		builder.setNome(t.getNome());
		builder.setDescrizione(t.getDescrizione());

		builder.setOggettoMail(t.getOggettoMail());
		builder.setCorpoMail(t.getCorpoMail());
		builder.setMittente(t.getMittente());
		builder.setDestinatari(t.getDestinatari());
		builder.setDestinatariCC(t.getDestinatarioCC());

		List<CampoTemplate> campi = new ArrayList<CampoTemplate>();
		campi.addAll(getCampi(t.getCampo()));
		builder.setCampi(campi );

		builder.setFascicoliAbilitati(t.getFascicoliAbilitati());

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

	@Override
	protected void initPratica(DatiTemplateEmail dati) throws PraticaException {
		this.dati = dati;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof XMLTemplateEmail) {
			XMLTemplateEmail other = (XMLTemplateEmail) obj;
			return other.getAlfrescoPath().equalsIgnoreCase(getAlfrescoPath());
		} else
			return false;
	}
}
