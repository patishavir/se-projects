/*
 * XML Type:  PerformanceMonitorType
 * Namespace: 
 * Java type: noNamespace.PerformanceMonitorType
 *
 * Automatically generated - do not modify.
 */
package noNamespace.impl;

/**
 * An XML PerformanceMonitorType(@).
 * 
 * This is a complex type.
 */
public class PerformanceMonitorTypeImpl extends
		org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements
		noNamespace.PerformanceMonitorType {
	private static final long serialVersionUID = 1L;

	public PerformanceMonitorTypeImpl(org.apache.xmlbeans.SchemaType sType) {
		super(sType);
	}

	private static final javax.xml.namespace.QName NODE$0 = new javax.xml.namespace.QName("",
			"Node");
	private static final javax.xml.namespace.QName RESPONSESTATUS$2 = new javax.xml.namespace.QName(
			"", "responseStatus");
	private static final javax.xml.namespace.QName VERSION$4 = new javax.xml.namespace.QName("",
			"version");

	/**
	 * Gets a List of "Node" elements
	 */
	public java.util.List<noNamespace.NodeType> getNodeList() {
		final class NodeList extends java.util.AbstractList<noNamespace.NodeType> {
			@Override
			public noNamespace.NodeType get(int i) {
				return PerformanceMonitorTypeImpl.this.getNodeArray(i);
			}

			@Override
			public noNamespace.NodeType set(int i, noNamespace.NodeType o) {
				noNamespace.NodeType old = PerformanceMonitorTypeImpl.this.getNodeArray(i);
				PerformanceMonitorTypeImpl.this.setNodeArray(i, o);
				return old;
			}

			@Override
			public void add(int i, noNamespace.NodeType o) {
				PerformanceMonitorTypeImpl.this.insertNewNode(i).set(o);
			}

			@Override
			public noNamespace.NodeType remove(int i) {
				noNamespace.NodeType old = PerformanceMonitorTypeImpl.this.getNodeArray(i);
				PerformanceMonitorTypeImpl.this.removeNode(i);
				return old;
			}

			@Override
			public int size() {
				return PerformanceMonitorTypeImpl.this.sizeOfNodeArray();
			}

		}

		synchronized (monitor()) {
			check_orphaned();
			return new NodeList();
		}
	}

	/**
	 * Gets array of all "Node" elements
	 * 
	 * @deprecated
	 */
	@Deprecated
	public noNamespace.NodeType[] getNodeArray() {
		synchronized (monitor()) {
			check_orphaned();
			java.util.List<noNamespace.NodeType> targetList = new java.util.ArrayList<noNamespace.NodeType>();
			get_store().find_all_element_users(NODE$0, targetList);
			noNamespace.NodeType[] result = new noNamespace.NodeType[targetList.size()];
			targetList.toArray(result);
			return result;
		}
	}

	/**
	 * Gets ith "Node" element
	 */
	public noNamespace.NodeType getNodeArray(int i) {
		synchronized (monitor()) {
			check_orphaned();
			noNamespace.NodeType target = null;
			target = (noNamespace.NodeType) get_store().find_element_user(NODE$0, i);
			if (target == null) {
				throw new IndexOutOfBoundsException();
			}
			return target;
		}
	}

	/**
	 * Returns number of "Node" element
	 */
	public int sizeOfNodeArray() {
		synchronized (monitor()) {
			check_orphaned();
			return get_store().count_elements(NODE$0);
		}
	}

	/**
	 * Sets array of all "Node" element WARNING: This method is not atomicaly
	 * synchronized.
	 */
	public void setNodeArray(noNamespace.NodeType[] nodeArray) {
		check_orphaned();
		arraySetterHelper(nodeArray, NODE$0);
	}

	/**
	 * Sets ith "Node" element
	 */
	public void setNodeArray(int i, noNamespace.NodeType node) {
		synchronized (monitor()) {
			check_orphaned();
			noNamespace.NodeType target = null;
			target = (noNamespace.NodeType) get_store().find_element_user(NODE$0, i);
			if (target == null) {
				throw new IndexOutOfBoundsException();
			}
			target.set(node);
		}
	}

	/**
	 * Inserts and returns a new empty value (as xml) as the ith "Node" element
	 */
	public noNamespace.NodeType insertNewNode(int i) {
		synchronized (monitor()) {
			check_orphaned();
			noNamespace.NodeType target = null;
			target = (noNamespace.NodeType) get_store().insert_element_user(NODE$0, i);
			return target;
		}
	}

	/**
	 * Appends and returns a new empty value (as xml) as the last "Node" element
	 */
	public noNamespace.NodeType addNewNode() {
		synchronized (monitor()) {
			check_orphaned();
			noNamespace.NodeType target = null;
			target = (noNamespace.NodeType) get_store().add_element_user(NODE$0);
			return target;
		}
	}

	/**
	 * Removes the ith "Node" element
	 */
	public void removeNode(int i) {
		synchronized (monitor()) {
			check_orphaned();
			get_store().remove_element(NODE$0, i);
		}
	}

	/**
	 * Gets the "responseStatus" attribute
	 */
	public java.lang.String getResponseStatus() {
		synchronized (monitor()) {
			check_orphaned();
			org.apache.xmlbeans.SimpleValue target = null;
			target = (org.apache.xmlbeans.SimpleValue) get_store().find_attribute_user(
					RESPONSESTATUS$2);
			if (target == null) {
				return null;
			}
			return target.getStringValue();
		}
	}

	/**
	 * Gets (as xml) the "responseStatus" attribute
	 */
	public org.apache.xmlbeans.XmlString xgetResponseStatus() {
		synchronized (monitor()) {
			check_orphaned();
			org.apache.xmlbeans.XmlString target = null;
			target = (org.apache.xmlbeans.XmlString) get_store().find_attribute_user(
					RESPONSESTATUS$2);
			return target;
		}
	}

	/**
	 * True if has "responseStatus" attribute
	 */
	public boolean isSetResponseStatus() {
		synchronized (monitor()) {
			check_orphaned();
			return get_store().find_attribute_user(RESPONSESTATUS$2) != null;
		}
	}

	/**
	 * Sets the "responseStatus" attribute
	 */
	public void setResponseStatus(java.lang.String responseStatus) {
		synchronized (monitor()) {
			check_orphaned();
			org.apache.xmlbeans.SimpleValue target = null;
			target = (org.apache.xmlbeans.SimpleValue) get_store().find_attribute_user(
					RESPONSESTATUS$2);
			if (target == null) {
				target = (org.apache.xmlbeans.SimpleValue) get_store().add_attribute_user(
						RESPONSESTATUS$2);
			}
			target.setStringValue(responseStatus);
		}
	}

	/**
	 * Sets (as xml) the "responseStatus" attribute
	 */
	public void xsetResponseStatus(org.apache.xmlbeans.XmlString responseStatus) {
		synchronized (monitor()) {
			check_orphaned();
			org.apache.xmlbeans.XmlString target = null;
			target = (org.apache.xmlbeans.XmlString) get_store().find_attribute_user(
					RESPONSESTATUS$2);
			if (target == null) {
				target = (org.apache.xmlbeans.XmlString) get_store().add_attribute_user(
						RESPONSESTATUS$2);
			}
			target.set(responseStatus);
		}
	}

	/**
	 * Unsets the "responseStatus" attribute
	 */
	public void unsetResponseStatus() {
		synchronized (monitor()) {
			check_orphaned();
			get_store().remove_attribute(RESPONSESTATUS$2);
		}
	}

	/**
	 * Gets the "version" attribute
	 */
	public java.lang.String getVersion() {
		synchronized (monitor()) {
			check_orphaned();
			org.apache.xmlbeans.SimpleValue target = null;
			target = (org.apache.xmlbeans.SimpleValue) get_store().find_attribute_user(VERSION$4);
			if (target == null) {
				return null;
			}
			return target.getStringValue();
		}
	}

	/**
	 * Gets (as xml) the "version" attribute
	 */
	public org.apache.xmlbeans.XmlString xgetVersion() {
		synchronized (monitor()) {
			check_orphaned();
			org.apache.xmlbeans.XmlString target = null;
			target = (org.apache.xmlbeans.XmlString) get_store().find_attribute_user(VERSION$4);
			return target;
		}
	}

	/**
	 * True if has "version" attribute
	 */
	public boolean isSetVersion() {
		synchronized (monitor()) {
			check_orphaned();
			return get_store().find_attribute_user(VERSION$4) != null;
		}
	}

	/**
	 * Sets the "version" attribute
	 */
	public void setVersion(java.lang.String version) {
		synchronized (monitor()) {
			check_orphaned();
			org.apache.xmlbeans.SimpleValue target = null;
			target = (org.apache.xmlbeans.SimpleValue) get_store().find_attribute_user(VERSION$4);
			if (target == null) {
				target = (org.apache.xmlbeans.SimpleValue) get_store()
						.add_attribute_user(VERSION$4);
			}
			target.setStringValue(version);
		}
	}

	/**
	 * Sets (as xml) the "version" attribute
	 */
	public void xsetVersion(org.apache.xmlbeans.XmlString version) {
		synchronized (monitor()) {
			check_orphaned();
			org.apache.xmlbeans.XmlString target = null;
			target = (org.apache.xmlbeans.XmlString) get_store().find_attribute_user(VERSION$4);
			if (target == null) {
				target = (org.apache.xmlbeans.XmlString) get_store().add_attribute_user(VERSION$4);
			}
			target.set(version);
		}
	}

	/**
	 * Unsets the "version" attribute
	 */
	public void unsetVersion() {
		synchronized (monitor()) {
			check_orphaned();
			get_store().remove_attribute(VERSION$4);
		}
	}
}
