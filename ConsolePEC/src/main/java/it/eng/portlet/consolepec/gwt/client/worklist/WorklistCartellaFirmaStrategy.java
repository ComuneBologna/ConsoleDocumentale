package it.eng.portlet.consolepec.gwt.client.worklist;

import it.eng.portlet.consolepec.gwt.client.presenter.Command;
import it.eng.portlet.consolepec.gwt.client.widget.datagrid.DataGridWidget;
import it.eng.portlet.consolepec.gwt.client.widget.datagrid.DataGridWidget.ProvidesElementoEvidenziatoInfo;
import it.eng.portlet.consolepec.gwt.shared.model.AllegatoDTO;
import it.eng.portlet.consolepec.gwt.shared.model.ColonnaWorklist;
import it.eng.portlet.consolepec.gwt.shared.model.cartellafirma.DocumentoFirmaVistoDTO;

import java.util.List;
import java.util.Set;

import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.view.client.ProvidesKey;
import com.google.web.bindery.event.shared.EventBus;

/**
 *
 * @author biagiot
 *
 */
public interface WorklistCartellaFirmaStrategy {

	public static final ProvidesKey<DocumentoFirmaVistoDTO> DOCUMENTO_FIRMA_VISTO_KEY_PROVIDER = new DocumentoFirmaVistoKeyProvider();
	public static final ProvidesElementoEvidenziatoInfo<DocumentoFirmaVistoDTO> DOCUMENTO_FIRMA_VISTO_PROVIDER = new ElementoEvidenziatoInfoProvider();
	public final int MAX_DESTINATARI_IN_WORKLIST = 2;

	public void setRigaEspansaStrategy(RigaEspansaCartellaFirmaStrategy rigaEspansaStrategy);
	public void addCheckRigaEventListener(CheckRigaEventListener listener);
	public void addEspandiRigaEventListener(EspandiRigaEventListener listener);
	public void setRicercaEventListener(RicercaEventListener listener);
	public void configuraGrid(final DataGridWidget<DocumentoFirmaVistoDTO> dataGrid, SimplePager pager);
	public void resetSelezioni();
	public void restartSearchDatiGrid();
	public void resetGrid();
	public void refreshDatiGrid();
	public Set<DocumentoFirmaVistoDTO> getDocumentiFirmaVistoSelezionati();
	public void aggiornaRiga(DocumentoFirmaVistoDTO documentoFirmaVistoDTO);
	public void espandiRiga(DocumentoFirmaVistoDTO documentoFirmaVistoDTO);
	public void setDownloadAllegatoCommand(Command<Void, AllegatoDTO> downloadAllegatoCommand);
	public void setDettaglioAllegatoCommand(Command<Void, AllegatoDTO> dettaglioAllegatoCommand);
	public void setDettaglioFascicoloCommand(Command<Void, DocumentoFirmaVistoDTO> dettaglioFascicoloCommand);
	public void setEventBus(EventBus eventBus);

	public interface RicercaCallback {
		public void setRisultati(List<DocumentoFirmaVistoDTO> list, int count, boolean estimate);
		public void setRisultati(List<DocumentoFirmaVistoDTO> list);
		public void setCount(int count, boolean estimate);
		public void setNoResult();
	}

	public interface CheckRigaEventListener {
		public void onCheckRiga(DocumentoFirmaVistoDTO documentoFirmaVisto, boolean checked);
	}

	public interface EspandiRigaEventListener {
		public void onEspandiRiga(DocumentoFirmaVistoDTO documentoFirmaVisto, boolean isEspansa);
	}

	public interface RicercaEventListener {
		public void onStartRicerca(int start, int length, ColonnaWorklist campoOrdinamento, boolean asc, RicercaCallback callback);
	}

	public class DocumentoFirmaVistoKeyProvider implements ProvidesKey<DocumentoFirmaVistoDTO> {

		@Override
		public Object getKey(DocumentoFirmaVistoDTO item) {
			return DocumentoFirmaVistoDTO.getKey(item);
		}
	}

	public class ElementoEvidenziatoInfoProvider implements ProvidesElementoEvidenziatoInfo<DocumentoFirmaVistoDTO> {

		@Override
		public boolean isEvidenziato(DocumentoFirmaVistoDTO item) {
			return !item.isLetto();
		}
	}
}
