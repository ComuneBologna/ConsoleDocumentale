package it.eng.portlet.consolepec.spring.bean.session.user;
/**
 * Generica Exception lanciata in caso di errori nella gestione utente
 * @author pluttero
 *
 */
public class UserException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UserException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}
}
