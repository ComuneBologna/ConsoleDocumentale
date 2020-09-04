package it.eng.portlet.consolepec.spring.bean.session.user;

public interface UserCustomFields {
	/**
	 * Inserisce i custom field in {@link ConsolePecUser} prelevandoli da liferay
	 * @param cpu
	 * @throws UserException 
	 */
	public void popolaCustomFields(ConsolePecUser cpu) throws UserException;
	
	/**
	 * Aggiorna il custom field su liferay e su {@link ConsolePecUser}
	 * @param cpu
	 * @param attributeName
	 * @param attributeValue
	 * @throws UserException 
	 */
	public void updateCustomField(ConsolePecUser cpu, String attributeName, String attributeValue) throws UserException;

	/**
	 * Rimuove il custom field da liferay e da {@link ConsolePecUser}
	 * @param cpu
	 * @param attributeName
	 * @throws UserException 
	 */
	public void deleteCustomField(ConsolePecUser cpu, String attributeName) throws UserException;	
}
