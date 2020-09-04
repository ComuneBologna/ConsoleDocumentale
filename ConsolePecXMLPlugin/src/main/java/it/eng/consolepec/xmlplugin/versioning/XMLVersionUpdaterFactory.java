package it.eng.consolepec.xmlplugin.versioning;


public class XMLVersionUpdaterFactory {
	
	private XMLVersionUpdater initChain(){
		Versione1 v1 = new Versione1();
		Versione1_1 v1_1 = new Versione1_1();
		v1.setNext(v1_1);
		v1_1.setNext(null);//qui aggiungo i prossimi
		return v1;
	}
	
	public XMLVersionUpdater newVersionUpdaterInstance(){
		return initChain();
	}
}
