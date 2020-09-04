package it.eng.consolepec.xmlplugin.versioning;

public class JsonVersionUpdaterFactory {

	private JsonVersionUpdater initChain(){
		JsonVersione1 v1 = new JsonVersione1();
		JsonVersione1_1 v1_1 = new JsonVersione1_1();
		v1.setNext(v1_1);
		return v1;
	}
	
	public JsonVersionUpdater newVersionUpdaterInstance(){
		return initChain();
	}

}
