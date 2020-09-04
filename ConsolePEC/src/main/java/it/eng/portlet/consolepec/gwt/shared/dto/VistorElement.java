package it.eng.portlet.consolepec.gwt.shared.dto;

import java.io.Serializable;

public interface VistorElement extends Serializable{
	public void visit(Element e);
	
	public void visit(Group g);
	
	public void visit(Row r);
}

//TO DELETE
class VisitorImpl implements VistorElement{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2231771965253985467L;

	@Override
	public void visit(Element e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(Group g) {
		//apri tag
		g.getName();
		
		for(Element e : g.getElements())
			e.accept(this);
		
		//chiudi tag
		
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(Row r) {
		//apri tag
		
		// chiudi tag
		
	}
	
}