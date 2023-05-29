/*
 * XML Type:  NodeType
 * Namespace: 
 * Java type: noNamespace.NodeType
 *
 * Automatically generated - do not modify.
 */
package noNamespace.impl;

/**
 * An XML NodeType(@).
 * 
 * This is a complex type.
 */
public class NodeTypeImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements
		noNamespace.NodeType {
	private static final long serialVersionUID = 1L;

	public NodeTypeImpl(org.apache.xmlbeans.SchemaType sType) {
		super(sType);
	}

	private static final javax.xml.namespace.QName SERVER$0 = new javax.xml.namespace.QName("",
			"Server");
	private static final javax.xml.namespace.QName NAME$2 = new javax.xml.namespace.QName("",
			"name");

	/**
	 * Gets a List of "Server" elements
	 */
	public java.util.List<noNamespace.ServerType> getServerList() {
		final class ServerList extends java.util.AbstractList<noNamespace.ServerType> {
			@Override
			public noNamespace.ServerType get(int i) {
				return NodeTypeImpl.this.getServerArray(i);
			}

			@Override
			public noNamespace.ServerType set(int i, noNamespace.ServerType o) {
				noNamespace.ServerType old = NodeTypeImpl.this.getServerArray(i);
				NodeTypeImpl.this.setServerArray(i, o);
				return old;
			}

			@Override
			public void add(int i, noNamespace.ServerType o) {
				NodeTypeImpl.this.insertNewServer(i).set(o);
			}

			@Override
			public noNamespace.ServerType remove(int i) {
				noNamespace.ServerType old = NodeTypeImpl.this.getServerArray(i);
				NodeTypeImpl.this.removeServer(i);
				return old;
			}

			@Override
			public int size() {
				return NodeTypeImpl.this.sizeOfServerArray();
			}

		}

		synchronized (monitor()) {
			check_orphaned();
			return new ServerList();
		}
	}

	/**
	 * Gets array of all "Server" elements
	 * 
	 * @deprecated
	 */
	@Deprecated
	public noNamespace.ServerType[] getServerArray() {
		synchronized (monitor()) {
			check_orphaned();
			java.util.List<noNamespace.ServerType> targetList = new java.util.ArrayList<noNamespace.ServerType>();
			get_store().find_all_element_users(SERVER$0, targetList);
			noNamespace.ServerType[] result = new noNamespace.ServerType[targetList.size()];
			targetList.toArray(result);
			return result;
		}
	}

	/**
	 * Gets ith "Server" element
	 */
	public noNamespace.ServerType getServerArray(int i) {
		synchronized (monitor()) {
			check_orphaned();
			noNamespace.ServerType target = null;
			target = (noNamespace.ServerType) get_store().find_element_user(SERVER$0, i);
			if (target == null) {
				throw new IndexOutOfBoundsException();
			}
			return target;
		}
	}

	/**
	 * Returns number of "Server" element
	 */
	public int sizeOfServerArray() {
		synchronized (monitor()) {
			check_orphaned();
			return get_store().count_elements(SERVER$0);
		}
	}

	/**
	 * Sets array of all "Server" element WARNING: This method is not atomicaly
	 * synchronized.
	 */
	public void setServerArray(noNamespace.ServerType[] serverArray) {
		check_orphaned();
		arraySetterHelper(serverArray, SERVER$0);
	}

	/**
	 * Sets ith "Server" element
	 */
	public void setServerArray(int i, noNamespace.ServerType server) {
		synchronized (monitor()) {
			check_orphaned();
			noNamespace.ServerType target = null;
			target = (noNamespace.ServerType) get_store().find_element_user(SERVER$0, i);
			if (target == null) {
				throw new IndexOutOfBoundsException();
			}
			target.set(server);
		}
	}

	/**
	 * Inserts and returns a new empty value (as xml) as the ith "Server"
	 * element
	 */
	public noNamespace.ServerType insertNewServer(int i) {
		synchronized (monitor()) {
			check_orphaned();
			noNamespace.ServerType target = null;
			target = (noNamespace.ServerType) get_store().insert_element_user(SERVER$0, i);
			return target;
		}
	}

	/**
	 * Appends and returns a new empty value (as xml) as the last "Server"
	 * element
	 */
	public noNamespace.ServerType addNewServer() {
		synchronized (monitor()) {
			check_orphaned();
			noNamespace.ServerType target = null;
			target = (noNamespace.ServerType) get_store().add_element_user(SERVER$0);
			return target;
		}
	}

	/**
	 * Removes the ith "Server" element
	 */
	public void removeServer(int i) {
		synchronized (monitor()) {
			check_orphaned();
			get_store().remove_element(SERVER$0, i);
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
