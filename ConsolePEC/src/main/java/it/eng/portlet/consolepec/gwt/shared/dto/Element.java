package it.eng.portlet.consolepec.gwt.shared.dto;

import java.io.Serializable;

public abstract class  Element implements Serializable{
	
	private static final long serialVersionUID = 1L;
	protected String name="";
	
	public Element(){
		
	}
	public  String getName(){
		return name;
	}
	
	public void accept(VistorElement v){
		v.visit(this);
	}
}
