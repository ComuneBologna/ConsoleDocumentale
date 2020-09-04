package it.eng.portlet.consolepec.gwt.shared.model;

import it.eng.cobo.consolepec.commons.configurazioni.TipologiaPratica;

import java.util.ArrayList;
import java.util.List;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * Superclasse Template
 *
 * @author biagiot
 *
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class BaseTemplateDTO extends PraticaDTO {

	@Getter
	@Setter
	private String nome;
	
	@Getter
	@Setter
	private String descrizione;
	
	@Getter
	private List<CampoTemplateDTO> campi = new ArrayList<CampoTemplateDTO>();
	
	@Getter
	private List<TipologiaPratica> fascicoliAbilitati = new ArrayList<>();
	
	@Getter
	@Setter
	private StatoTemplateDTO statoTemplate;
	
	@Getter
	@Setter
	private boolean salvaButtonAbilitato, eliminaButtonAbilitato, creaTemplatePerCopiaAbilitato;
	
	public abstract void accept(ModelloVisitor v);
	
	public BaseTemplateDTO(String alfrescoPath) {
		super(alfrescoPath);
	}

	public enum StatoTemplateDTO {
		IN_GESTIONE("In gestione"), BOZZA("Bozza");

		private String descrizioneStato;

		StatoTemplateDTO(String descrizioneStato) {
			this.descrizioneStato = descrizioneStato;
		}

		public String getDescrizioneStato() {
			return descrizioneStato;
		}

		public static StatoTemplateDTO fromLabel(String label) {
			for (StatoTemplateDTO st : StatoTemplateDTO.values())
				if (st.getDescrizioneStato().equalsIgnoreCase(label))
					return st;
			return null;
		}
	}
	
	public static interface ModelloVisitor {
		void visit(TemplatePdfDTO modelloPdf);
		void visit(TemplateDTO modelloMail);
	}
}
