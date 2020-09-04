package it.eng.portlet.consolepec.gwt.shared.dto;

import java.util.LinkedList;


public class Group extends Element{

	private static final long serialVersionUID = 2984257530726791050L;
	
	private LinkedList<Element>  elements = new LinkedList<Element>();
	public LinkedList<Element> getElements() {
		return elements;
	}

	@SuppressWarnings("unused")
	private Group(){
		super();
	}
	
	public Group(String name){
		super();
		super.name = name;	
	}
	
	public Group(String name,Element element){
		super.name = name;
		elements.addLast(element);
	}
	public void addElement(Element element){
		elements.addLast(element);
	}
	public void accept(VistorElement v){
		v.visit(this);
//		for(Element e : elements){
//			v.vist(e);
//		}
	}
}
