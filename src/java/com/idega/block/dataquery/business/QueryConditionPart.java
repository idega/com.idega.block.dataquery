/*
 * Created on May 27, 2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code Template
 */
package com.idega.block.dataquery.business;

import com.idega.xml.XMLElement;

/**
 * <p>Title: idegaWeb</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: idega Software</p>
 * @author aron 
 * @version 1.0
 */

public class QueryConditionPart implements QueryPart {

	private String field = null;
	private String type = null;
	private String pattern = null;
	
	public QueryConditionPart(String field, String type, String pattern){
		this.field = field;
		this.type = type;
		this.pattern = pattern;
	}
	
	public QueryConditionPart(XMLElement xml){
		field = xml.getAttribute(QueryXMLConstants.FIELD).getValue();
		type = xml.getAttribute(QueryXMLConstants.TYPE).getValue();
		if(xml.hasChildren()){
			XMLElement xmlPattern = xml.getChild(QueryXMLConstants.PATTERN);
			pattern = xmlPattern.getTextTrim();
		}
	}
	
	public XMLElement getQueryElement() {
		XMLElement el = new XMLElement(QueryXMLConstants.CONDITION);
		el.setAttribute(QueryXMLConstants.FIELD,field);
		el.setAttribute(QueryXMLConstants.TYPE,type);
		XMLElement xmlPattern = new XMLElement(QueryXMLConstants.PATTERN);
		xmlPattern.addContent(pattern);
		el.addContent(xmlPattern);
		return el;
	}

	/**
	 * @return
	 */
	public String getField() {
		return field;
	}

	/**
	 * @return
	 */
	public String getPattern() {
		return pattern;
	}

	/**
	 * @return
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param string
	 */
	public void setField(String string) {
		field = string;
	}

	/**
	 * @param string
	 */
	public void setPattern(String string) {
		pattern = string;
	}

	/**
	 * @param string
	 */
	public void setType(String string) {
		type = string;
	}

}