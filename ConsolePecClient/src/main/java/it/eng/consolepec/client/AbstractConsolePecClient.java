package it.eng.consolepec.client;

public class AbstractConsolePecClient {

	protected ClientInvoker clientInvoker;

	public AbstractConsolePecClient(ClientInvoker clientInvoker) {
		super();
		this.clientInvoker = clientInvoker;
	}
}
