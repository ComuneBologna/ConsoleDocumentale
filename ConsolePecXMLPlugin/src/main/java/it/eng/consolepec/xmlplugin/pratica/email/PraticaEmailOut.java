package it.eng.consolepec.xmlplugin.pratica.email;

public interface PraticaEmailOut extends PraticaEmail {

	public boolean isInviata();
	
	public boolean isReinoltro();
	
	public boolean isReinoltrabile();
	
	public boolean isEmailInteroperabileInviabile();

}
