package it.eng.consolepec.xmlplugin.pratica.template;

public interface TemplateEmail extends AbstractTemplate<DatiTemplateEmail> {
	
	@Override
	public DatiTemplateEmail getDati();
}
