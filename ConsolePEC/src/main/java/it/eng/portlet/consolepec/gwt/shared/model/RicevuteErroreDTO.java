package it.eng.portlet.consolepec.gwt.shared.model;

import com.google.gwt.user.client.rpc.IsSerializable;

public class RicevuteErroreDTO implements IsSerializable{
	
	
	private String dest;
	private String errMsg;
	
	public RicevuteErroreDTO() {
		// serializzazione?
	}
	
	public String getDest() {
		return dest;
	}
	public void setDest(String dest) {
		this.dest = dest;
	}
	public String getErrMsg() {
		return errMsg;
	}
	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

}
