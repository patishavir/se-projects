/*
 * XML Type:  ServerType
 * Namespace: 
 * Java type: noNamespace.ServerType
 *
 * Automatically generated - do not modify.
 */
package noNamespace.impl;

/**
 * An XML ServerType(@).
 * 
 * This is a complex type.
 */
public class ServerTypeImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements
		noNamespace.ServerType {
	private static final long serialVersionUID = 1L;

	public ServerTypeImpl(org.apache.xmlbeans.SchemaType sType) {
		super(sType);
	}

	private static final javax.xml.namespace.QName STAT$0 = new javax.xml.namespace.QName("",
			"Stat");
	private static final javax.xml.namespace.QName NAME$2 = new javax.xml.namespace.QName("",
			"name");

	/**
	 * Gets the "Stat" element
	 */
	public noNamespace.StatType getStat() {
		synchronized (monitor()) {
			check_orphaned();
			noNamespace.StatType target = null;
			target = (noNamespace.StatType) get_store().find_element_user(STAT$0, 0);
			if (target == null) {
				return null;
			}
			return target;
		}
	}

	/**
	 * Sets the "Stat" element
	 */
	public void setStat(noNamespace.StatType stat) {
		generatedSetterHelperImpl(stat, STAT$0, 0,
				org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
	}

	/**
	 * Appends and returns a new empty "Stat" element
	 */
	public noNamespace.StatType addNewStat() {
		synchronized (monitor()) {
			check_orphaned();
			noNamespace.StatType target = null;
			target = (noNamespace.StatType) get_store().add_element_user(STAT$0);
			return target;
		}
	}

	/**
	 * Gets the "name" attribute
	 */
	public java.lang.String getName() {
		synchronized (monitor()) {
			check_orphaned();
			org.apache.xmlbeans.SimpleValue target = null;
			target = (org.apache.xmlbeans.SimpleValue) get_store().find_attribute_user(NAME$2);
			if (target == null) {
				return null;
			}
			return target.getStringValue();
		}
	}

	/**
	 * Gets (as xml) the "name" attribute
	 */
	public org.apache.xmlbeans.XmlString xgetName() {
		synchronized (monitor()) {
			check_orphaned();
			org.apache.xmlbeans.XmlString target = null;
			target = (org.apache.xmlbeans.XmlString) get_store().find_attribute_user(NAME$2);
			return target;
		}
	}

	/**
	 * True if has "name" attribute
	 */
	public boolean isSetName() {
		synchronized (monitor()) {
			check_orphaned();
			return get_store().find_attribute_user(NAME$2) != null;
		}
	}

	/**
	 * Sets the "name" attribute
	 */
	public void setName(java.lang.String name) {
		synchronized (monitor()) {
			check_orphaned();
			org.apache.xmlbeans.SimpleValue target = null;
			target = (org.apache.xmlbeans.SimpleValue) get_store().find_attribute_user(NAME$2);
			if (target == null) {
				target = (org.apache.xmlbeans.SimpleValue) get_store().add_attribute_user(NAME$2);
			}
			target.setStringValue(name);
		}
	}

	/**
	 * Sets (as xml) the "name" attribute
	 */
	public void xsetName(org.apache.xmlbeans.XmlString name) {
		synchronized (monitor()) {
			check_orphaned();
			org.apache.xmlbeans.XmlString target = null;
			target = (org.apache.xmlbeans.XmlString) get_store().find_attribute_user(NAME$2);
			if (target == null) {
				target = (org.apache.xmlbeans.XmlString) get_store().add_attribute_user(NAME$2);
			}
			target.set(name);
		}
	}

	/**
	 * Unsets the "name" attribute
	 */
	public void unsetName() {
		synchronized (monitor()) {
			check_orphaned();
			get_store().remove_attribute(NAME$2);
		}
	}
}
