/*
 * XML Type:  PerformanceMonitorType
 * Namespace: 
 * Java type: noNamespace.PerformanceMonitorType
 *
 * Automatically generated - do not modify.
 */
package noNamespace;

/**
 * An XML PerformanceMonitorType(@).
 * 
 * This is a complex type.
 */
public interface PerformanceMonitorType extends org.apache.xmlbeans.XmlObject {
	public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType) org.apache.xmlbeans.XmlBeans
			.typeSystemForClassLoader(PerformanceMonitorType.class.getClassLoader(),
					"schemaorg_apache_xmlbeans.system.sCAE5236ABB51CCDB84E3164358F8EB30")
			.resolveHandle("performancemonitortype2f2dtype");

	/**
	 * Gets a List of "Node" elements
	 */
	java.util.List<noNamespace.NodeType> getNodeList();

	/**
	 * Gets array of all "Node" elements
	 * 
	 * @deprecated
	 */
	@Deprecated
	noNamespace.NodeType[] getNodeArray();

	/**
	 * Gets ith "Node" element
	 */
	noNamespace.NodeType getNodeArray(int i);

	/**
	 * Returns number of "Node" element
	 */
	int sizeOfNodeArray();

	/**
	 * Sets array of all "Node" element
	 */
	void setNodeArray(noNamespace.NodeType[] nodeArray);

	/**
	 * Sets ith "Node" element
	 */
	void setNodeArray(int i, noNamespace.NodeType node);

	/**
	 * Inserts and returns a new empty value (as xml) as the ith "Node" element
	 */
	noNamespace.NodeType insertNewNode(int i);

	/**
	 * Appends and returns a new empty value (as xml) as the last "Node" element
	 */
	noNamespace.NodeType addNewNode();

	/**
	 * Removes the ith "Node" element
	 */
	void removeNode(int i);

	/**
	 * Gets the "responseStatus" attribute
	 */
	java.lang.String getResponseStatus();

	/**
	 * Gets (as xml) the "responseStatus" attribute
	 */
	org.apache.xmlbeans.XmlString xgetResponseStatus();

	/**
	 * True if has "responseStatus" attribute
	 */
	boolean isSetResponseStatus();

	/**
	 * Sets the "responseStatus" attribute
	 */
	void setResponseStatus(java.lang.String responseStatus);

	/**
	 * Sets (as xml) the "responseStatus" attribute
	 */
	void xsetResponseStatus(org.apache.xmlbeans.XmlString responseStatus);

	/**
	 * Unsets the "responseStatus" attribute
	 */
	void unsetResponseStatus();

	/**
	 * Gets the "version" attribute
	 */
	java.lang.String getVersion();

	/**
	 * Gets (as xml) the "version" attribute
	 */
	org.apache.xmlbeans.XmlString xgetVersion();

	/**
	 * True if has "version" attribute
	 */
	boolean isSetVersion();

	/**
	 * Sets the "version" attribute
	 */
	void setVersion(java.lang.String version);

	/**
	 * Sets (as xml) the "version" attribute
	 */
	void xsetVersion(org.apache.xmlbeans.XmlString version);

	/**
	 * Unsets the "version" attribute
	 */
	void unsetVersion();

	/**
	 * A factory class with static methods for creating instances of this type.
	 */

	public static final class Factory {
		public static noNamespace.PerformanceMonitorType newInstance() {
			return (noNamespace.PerformanceMonitorType) org.apache.xmlbeans.XmlBeans
					.getContextTypeLoader().newInstance(type, null);
		}

		public static noNamespace.PerformanceMonitorType newInstance(
				org.apache.xmlbeans.XmlOptions options) {
			return (noNamespace.PerformanceMonitorType) org.apache.xmlbeans.XmlBeans
					.getContextTypeLoader().newInstance(type, options);
		}

		/**
		 * @param xmlAsString
		 *            the string value to parse
		 */
		public static noNamespace.PerformanceMonitorType parse(java.lang.String xmlAsString)
				throws org.apache.xmlbeans.XmlException {
			return (noNamespace.PerformanceMonitorType) org.apache.xmlbeans.XmlBeans
					.getContextTypeLoader().parse(xmlAsString, type, null);
		}

		public static noNamespace.PerformanceMonitorType parse(java.lang.String xmlAsString,
				org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
			return (noNamespace.PerformanceMonitorType) org.apache.xmlbeans.XmlBeans
					.getContextTypeLoader().parse(xmlAsString, type, options);
		}

		/**
		 * @param file
		 *            the file from which to load an xml document
		 */
		public static noNamespace.PerformanceMonitorType parse(java.io.File file)
				throws org.apache.xmlbeans.XmlException, java.io.IOException {
			return (noNamespace.PerformanceMonitorType) org.apache.xmlbeans.XmlBeans
					.getContextTypeLoader().parse(file, type, null);
		}

		public static noNamespace.PerformanceMonitorType parse(java.io.File file,
				org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException,
				java.io.IOException {
			return (noNamespace.PerformanceMonitorType) org.apache.xmlbeans.XmlBeans
					.getContextTypeLoader().parse(file, type, options);
		}

		public static noNamespace.PerformanceMonitorType parse(java.net.URL u)
				throws org.apache.xmlbeans.XmlException, java.io.IOException {
			return (noNamespace.PerformanceMonitorType) org.apache.xmlbeans.XmlBeans
					.getContextTypeLoader().parse(u, type, null);
		}

		public static noNamespace.PerformanceMonitorType parse(java.net.URL u,
				org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException,
				java.io.IOException {
			return (noNamespace.PerformanceMonitorType) org.apache.xmlbeans.XmlBeans
					.getContextTypeLoader().parse(u, type, options);
		}

		public static noNamespace.PerformanceMonitorType parse(java.io.InputStream is)
				throws org.apache.xmlbeans.XmlException, java.io.IOException {
			return (noNamespace.PerformanceMonitorType) org.apache.xmlbeans.XmlBeans
					.getContextTypeLoader().parse(is, type, null);
		}

		public static noNamespace.PerformanceMonitorType parse(java.io.InputStream is,
				org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException,
				java.io.IOException {
			return (noNamespace.PerformanceMonitorType) org.apache.xmlbeans.XmlBeans
					.getContextTypeLoader().parse(is, type, options);
		}

		public static noNamespace.PerformanceMonitorType parse(java.io.Reader r)
				throws org.apache.xmlbeans.XmlException, java.io.IOException {
			return (noNamespace.PerformanceMonitorType) org.apache.xmlbeans.XmlBeans
					.getContextTypeLoader().parse(r, type, null);
		}

		public static noNamespace.PerformanceMonitorType parse(java.io.Reader r,
				org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException,
				java.io.IOException {
			return (noNamespace.PerformanceMonitorType) org.apache.xmlbeans.XmlBeans
					.getContextTypeLoader().parse(r, type, options);
		}

		public static noNamespace.PerformanceMonitorType parse(javax.xml.stream.XMLStreamReader sr)
				throws org.apache.xmlbeans.XmlException {
			return (noNamespace.PerformanceMonitorType) org.apache.xmlbeans.XmlBeans
					.getContextTypeLoader().parse(sr, type, null);
		}

		public static noNamespace.PerformanceMonitorType parse(javax.xml.stream.XMLStreamReader sr,
				org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
			return (noNamespace.PerformanceMonitorType) org.apache.xmlbeans.XmlBeans
					.getContextTypeLoader().parse(sr, type, options);
		}

		public static noNamespace.PerformanceMonitorType parse(org.w3c.dom.Node node)
				throws org.apache.xmlbeans.XmlException {
			return (noNamespace.PerformanceMonitorType) org.apache.xmlbeans.XmlBeans
					.getContextTypeLoader().parse(node, type, null);
		}

		public static noNamespace.PerformanceMonitorType parse(org.w3c.dom.Node node,
				org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
			return (noNamespace.PerformanceMonitorType) org.apache.xmlbeans.XmlBeans
					.getContextTypeLoader().parse(node, type, options);
		}

		/** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
		@Deprecated
		public static noNamespace.PerformanceMonitorType parse(
				org.apache.xmlbeans.xml.stream.XMLInputStream xis)
				throws org.apache.xmlbeans.XmlException,
				org.apache.xmlbeans.xml.stream.XMLStreamException {
			return (noNamespace.PerformanceMonitorType) org.apache.xmlbeans.XmlBeans
					.getContextTypeLoader().parse(xis, type, null);
		}

		/** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
		@Deprecated
		public static noNamespace.PerformanceMonitorType parse(
				org.apache.xmlbeans.xml.stream.XMLInputStream xis,
				org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException,
				org.apache.xmlbeans.xml.stream.XMLStreamException {
			return (noNamespace.PerformanceMonitorType) org.apache.xmlbeans.XmlBeans
					.getContextTypeLoader().parse(xis, type, options);
		}

		/** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
		@Deprecated
		public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(
				org.apache.xmlbeans.xml.stream.XMLInputStream xis)
				throws org.apache.xmlbeans.XmlException,
				org.apache.xmlbeans.xml.stream.XMLStreamException {
			return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(
					xis, type, null);
		}

		/** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
		@Deprecated
		public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(
				org.apache.xmlbeans.xml.stream.XMLInputStream xis,
				org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException,
				org.apache.xmlbeans.xml.stream.XMLStreamException {
			return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(
					xis, type, options);
		}

		private Factory() {
		} // No instance of this class allowed
	}
}
