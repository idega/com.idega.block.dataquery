/*
 * Created on May 26, 2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code Template
 */
package com.idega.block.dataquery.data.xml;

import java.util.StringTokenizer;

import com.idega.data.IDOEntityDefinition;
import com.idega.data.IDOEntityField;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.xml.XMLAttribute;
import com.idega.xml.XMLElement;

/**
 * <p>Title: idegaWeb</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: idega Software</p>
 * @author aron 
 * @version 1.0
 */

public class QueryFieldPart implements QueryPart {
	
	private IDOEntityField idoField = null;
	private IDOEntityDefinition entityDef = null;
	private String name = null;
	private String entity = null;
	private String path = null;
	private String columns = null;
	private String function = null;
	private String display = null;
	private String typeClass = null;
	private boolean locked = false;
	private boolean hidden = false;
	
	public QueryFieldPart(String name, String entity, String path, String[] columns,String function, String display,String typeClass, String hidden){
		this(name,entity, path, "",function,display,typeClass, Boolean.getBoolean(hidden));
		this.columns = stringArrayToCommaList(columns);
	}
	
	public QueryFieldPart(String name,String entity,String path, String column,String function,String display,String typeClass, String hidden){
		this( name, entity, path, column, function, display, typeClass, Boolean.getBoolean(hidden));
	}
	
	
	
	public QueryFieldPart(String name,String entity,String path, String column,String function,String display,String typeClass, boolean hidden){
		this.name = name;
		this.entity = entity;
		this.path = path;
		this.columns = column;
		this.function = function;
		this.display = display;
		this.typeClass = typeClass;
		this.hidden = hidden;
	}
	
	public QueryFieldPart(XMLElement xml){
		name = xml.getAttribute(QueryXMLConstants.NAME).getValue();
		entity = xml.getAttribute(QueryXMLConstants.ENTITY).getValue();
		path = xml.getAttribute(QueryXMLConstants.PATH).getValue();
		columns = xml.getAttribute(QueryXMLConstants.PROPERTIES).getValue();
		XMLAttribute func =  xml.getAttribute(QueryXMLConstants.FUNCTION);
		if(func!=null)
			function = func.getValue();
		typeClass = xml.getAttribute(QueryXMLConstants.TYPE).getValue();
		if(xml.hasChildren()){
			XMLElement xmlDisplay = xml.getChild(QueryXMLConstants.DISPLAY);
			display = xmlDisplay.getTextTrim();
			XMLElement xmlLock = xml.getChild(QueryXMLConstants.LOCK);
			locked = xmlLock!=null;
			XMLElement hidden = xml.getChild(QueryXMLConstants.HIDDEN);
			this.hidden = hidden!= null;
		}
		
		
	}
	
	

	/* (non-Javadoc)
	 * @see com.idega.block.dataquery.business.QueryPart#getQueryElement()
	 */
	public XMLElement getQueryElement() {
		XMLElement el = new XMLElement(QueryXMLConstants.FIELD);
		el.setAttribute(QueryXMLConstants.NAME,name);
		el.setAttribute(QueryXMLConstants.ENTITY,entity);
		el.setAttribute(QueryXMLConstants.PATH, path);
		el.setAttribute(QueryXMLConstants.PROPERTIES,this.columns);
	  	if(this.function!=null && !this.function.equalsIgnoreCase("null"))
	  		el.setAttribute(QueryXMLConstants.FUNCTION,function);
	  	if(this.typeClass!=null && !this.typeClass.equalsIgnoreCase("null"))
	  		el.setAttribute(QueryXMLConstants.TYPE,typeClass);
	  	XMLElement xmlDisplay = new XMLElement(QueryXMLConstants.DISPLAY);
	  	xmlDisplay.addContent(this.display);
	  	el.addContent(xmlDisplay);
	  	if(locked) {
				el.addContent(new XMLElement(QueryXMLConstants.LOCK));
	  	}
			if (hidden) {
				el.addContent(new XMLElement(QueryXMLConstants.HIDDEN));
			}

		return el;
	}

	/**
	 * @return
	 */
	public String getDisplay() {
		return display;
	}

	/**
	 * @return
	 */
	public String getEntity() {
		return entity;
	}
	
	public String getPath() {
		return path;
	}

	/**
	 * @return
	 */
	public String getFunction() {
		return function;
	}

	/**
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return
	 */
	public String[] getColumns() {
		return commaListToArray(columns);
	}

	/**
	 * @param string
	 */
	public void setDisplay(String string) {
		display = string;
	}

	/**
	 * @param string
	 */
	public void setEntity(String string) {
		entity = string;
	}

	/**
	 * @param string
	 */
	public void setFunction(String string) {
		function = string;
	}

	/**
	 * @param string
	 */
	public void setName(String string) {
		name = string;
	}

	/**
	 * @param strings
	 */
	public void setColumns(String[] columnStrings) {
		columns = stringArrayToCommaList(columnStrings);
	}
	
	public void addColumn(String column){
		if(getColumns()!=null && getColumns().length>0){
			this.columns+=","+column;
		}
		else
			this.columns = column;
	}
	
	public void addColumn(String[] columns){
		if(columns!=null){
			addColumn(stringArrayToCommaList(columns));
		}
	}
	
	public String stringArrayToCommaList(String[] array){
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < array.length; i++) {
			if(i>0)
				buf.append(",");
			buf.append(array[i]);				
		}
		return buf.toString();
	}
	
	public String[] commaListToArray(String commaList){
		StringTokenizer tokenizer = new StringTokenizer(commaList,",");
		String[] array = new String[tokenizer.countTokens()];
		for (int i = 0; i < array.length; i++) {
			array[i] = tokenizer.nextToken();
		}
		return array;
	}
	
	public String encode(){
		StringBuffer buffer = new StringBuffer();
		buffer.append(name).append(';');
		buffer.append(entity).append(';');
		buffer.append(path).append(';');
		buffer.append(columns).append(';');
		buffer.append(function).append(';');
		buffer.append(display).append(';');
		buffer.append(typeClass).append(';');
		buffer.append(hidden);		
		return buffer.toString();
	}
	
	public static QueryFieldPart  decode(String encoded){
			StringTokenizer toker = new StringTokenizer(encoded,";");
			if(toker.countTokens()==8){
				return new QueryFieldPart(toker.nextToken(),toker.nextToken(), toker.nextToken(), toker.nextToken(),toker.nextToken(),toker.nextToken(),toker.nextToken(), toker.nextToken());
				
			}
			return null;
	}

	/**
	 * @return
	 */
	public String getTypeClass() {
		return typeClass;
	}

	/**
	 * @param string
	 */
	public void setTypeClass(String string) {
		typeClass = string;
	}

	/* (non-Javadoc)
	 * @see com.idega.block.dataquery.business.QueryPart#isLocked()
	 */
	public boolean isLocked() {
		return locked;
	}

	public boolean isHidden()	{
		return hidden;
	}
	
	public void setHidden(boolean hidden)	{
		this.hidden = hidden;
	}
	
	
	/* (non-Javadoc)
	 * @see com.idega.block.dataquery.business.QueryPart#setLocked(boolean)
	 */
	public void setLocked(boolean locked) {
		this.locked = locked;
	}
	
	private IDOEntityDefinition getIDOEntityDefinition() throws IDOLookupException, ClassNotFoundException{
		if(entityDef==null){
			entityDef = IDOLookup.getEntityDefinitionForClass(Class.forName(entity));
		}
		return entityDef;
	}

	public IDOEntityField getIDOEntityField() throws IDOLookupException, ClassNotFoundException{
		if(idoField==null){
			IDOEntityDefinition def = getIDOEntityDefinition();
			if(def != null){
				IDOEntityField[] fields = def.getFields();
				for (int i = 0; i < fields.length; i++) {
					if(fields[i].getUniqueFieldName().equals(name)){
						idoField = fields[i];
						return idoField;
					}
				}
			}
		}
		return idoField;
	}


}