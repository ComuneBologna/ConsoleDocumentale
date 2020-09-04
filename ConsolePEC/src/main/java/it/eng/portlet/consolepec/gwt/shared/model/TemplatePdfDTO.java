package it.eng.portlet.consolepec.gwt.shared.model;

/**
 *
 * Template PDF
 *
 * @author biagiot
 *
 */
public class TemplatePdfDTO extends BaseTemplateDTO {

	private AllegatoDTO modelloOdt;
	private String titoloFile;

	private boolean caricaModelloOdtAbilitato;

	public TemplatePdfDTO(String alfrescoPath) {
		super(alfrescoPath);
	}

	public TemplatePdfDTO() {
		super();
	}

	public AllegatoDTO getModelloOdt() {
		return modelloOdt;
	}

	public void setModelloOdt(AllegatoDTO modelloOdt) {
		this.modelloOdt = modelloOdt;
	}

	public boolean isCaricaModelloOdtAbilitato() {
		return caricaModelloOdtAbilitato;
	}

	public void setCaricaModelloOdtAbilitato(boolean caricaModelloOdtAbilitato) {
		this.caricaModelloOdtAbilitato = caricaModelloOdtAbilitato;
	}

	@Override
	public void accept(ModelloVisitor v) {
		v.visit(this);
	}

	public void setTitoloFile(String titoloFile) {
		this.titoloFile = titoloFile;
	}

	public String getTitoloFile() {
		return titoloFile;
	}
}
