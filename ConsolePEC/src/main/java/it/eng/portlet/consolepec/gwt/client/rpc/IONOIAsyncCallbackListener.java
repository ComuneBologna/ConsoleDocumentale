package it.eng.portlet.consolepec.gwt.client.rpc;

/**
 * Interfaccia che deve implementare ogni listener di AsyncCallback
 * 
 * @author pluttero
 * 
 * @param <T>
 */
public interface IONOIAsyncCallbackListener {

	public void onFailure(FailureEvent caught);

	public void onSuccess(SuccessEvent event);

	abstract class Event {
		private boolean prop;

		Event(boolean prop) {
			this.prop = prop;
		}

		/**
		 * Gestisce se l'evento debba essere propagato fino al callback handler del client (Tutti i listener sono comunque invocati)
		 */
		public void preventPropagation() {
			this.prop = false;
		}

		boolean isProp() {
			return prop;
		}
	}

	/**
	 * Classe che contiene i dati di un evento di failure
	 * 
	 * @author pluttero
	 * 
	 */
	public class FailureEvent extends Event {
		private final Throwable caught;

		public FailureEvent(Throwable t) {
			super(true);
			this.caught = t;
		}

		public Throwable getCaught() {
			return caught;
		}

	}

	/**
	 * Evento di successo alla chiamata asicrona
	 * 
	 * @author pluttero
	 * 
	 */
	public class SuccessEvent extends Event {
		private final Object result;

		public SuccessEvent(Object result) {
			super(true);
			this.result = result;
		}

		public Object getResult() {
			return result;
		}
	}
}
