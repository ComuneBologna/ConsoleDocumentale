package it.eng.portlet.consolepec.gwt.client.worklist;

import it.eng.portlet.consolepec.gwt.client.widget.datagrid.DataGridWidget;
import it.eng.portlet.consolepec.gwt.client.worklist.WorklistStrategy.EspandiRigaEventListener;
import it.eng.portlet.consolepec.gwt.shared.model.PraticaDTO;

import com.google.gwt.user.client.ui.HTMLPanel;

/**
 * Interfaccia implementata dalle classi che disegnano la riga espansa di una {@link DataGridWidget}
 * 
 * @author pluttero
 * 
 */
public interface RigaEspansaStrategy {

	/**
	 * Disegna il dettaglio di una riga espansa. L'implementazione deve essere idempotente (nel caso di riga già espansa, si tratta di un update)
	 * 
	 * @param destination
	 * @param pratica
	 */
	public void disegnaDettaglio(HTMLPanel destination, PraticaDTO pratica);

	/**
	 * Disegna il dettaglio di una riga espansa. L'implementazione deve essere idempotente (nel caso di riga già espansa, si tratta di un update)
	 * 
	 * @param operations
	 * @param pratica
	 * @param listener
	 */
	public void disegnaOperazioni(HTMLPanel operations, PraticaDTO pratica, EspandiRigaEventListener listener);

}
