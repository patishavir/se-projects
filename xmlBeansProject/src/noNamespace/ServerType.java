/*
 * XML Type:  ServerType
 * Namespace: 
 * Java type: noNamespace.ServerType
 *
 * Automatically generated - do not modify.
 */
package noNamespace;

/**
 * An XML ServerType(@).
 * 
 * This is a complex type.
 */
public interface ServerType extends org.apache.xmlbeans.XmlObject {
	public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType) org.apache.xmlbeans.XmlBeans
			.typeSystemForClassLoader(ServerType.class.getClassLoader(),
					"schemaorg_apache_xmlbeans.system.sCAE5236ABB51CCDB84E3164358F8EB30")
			.resolveHandle("servertype9066type");

	/**
	 * Gets the "Stat" element
	 */
	noNamespace.StatType getStat();

	/**
	 * Sets the "Stat" element
	 */
	void setStat(noNamespace.StatType stat);

	/**
	 * Appends and returns a new empty "Stat" element
	 */
	noNamespace.StatType addNewStat();

	/**
	 * Gets the "name" attribute
	 */
	java.lang.String getName();

	/**
	 * Gets (as xml) the "name" attribute
	 */
	org.apache.xmlbeans.XmlString xgetName();

	/**
	 * True if has "name" attribute
	 */
	boolean isSetName();

	/**
	 * Sets the "name" attribute
	 */
	void setName(java.lang.String name);

	/**
	 * Sets (as xml) the "name" attribute
	 */
	void xsetName(org.apache.xmlbeans.XmlString name);

	/**
	 * Unsets the "name" attribute
	 */
	void unsetName();

	/**
	 * A factory class with static methods for creating instances of this type.
	 */

	public static final class Factory {
		public static noNamespace.ServerType newInstance() {
			return (noNamespace.ServerType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader()
					.newInstance(type, null);
		}

		public static noNamespace.ServerType newInstance(org.apache.xmlbeans.XmlOptions options) {
			return (noNamespace.ServerType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader()
					.newInstance(type, options);
		}

		/**
		 * @param xmlAsString
		 *            the string value to parse
		 */
		public static noNamespace.ServerType parse(java.lang.String xmlAsString)
				throws org.apache.xmlbeans.XmlException {
			return (noNamespace.ServerType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader()
					.parse(xmlAsString, type, null);
		}

		public static noNamespace.ServerType parse(java.lang.String xmlAsString,
				org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
			return (noNamespace.ServerType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader()
					.parse(xmlAsString, type, options);
		}

		/**
		 * @param file
		 *            the file from which to load an xml document
		 */
		public static noNamespace.ServerType parse(java.io.File file)
				throws org.apache.xmlbeans.XmlException, java.io.IOException {
			return (noNamespace.ServerType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader()
					.parse(file, type, null);
		}

		public static noNamespace.ServerType parse(java.io.File file,
				org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException,
				java.io.IOException {
			return (noNamespace.ServerType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader()
					.parse(file, type, options);
		}

		public static noNamespace.ServerType parse(java.net.URL u)
				throws org.apache.xmlbeans.XmlException, java.io.IOException {
			return (noNamespace.ServerType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader()
					.parse(u, type, null);
		}

		public static noNamespace.ServerType parse(java.net.URL u,
				org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException,
				java.io.IOException {
			return (noNamespace.ServerType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader()
					.parse(u, type, options);
		}

		public static noNamespace.ServerType parse(java.io.InputStream is)
				throws org.apache.xmlbeans.XmlException, java.io.IOException {
			return (noNamespace.ServerType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader()
					.parse(is, type, null);
		}

		public static noNamespace.ServerType parse(java.io.InputStream is,
				org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException,
				java.io.IOException {
			return (noNamespace.ServerType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader()
					.parse(is, type, options);
		}

		public static noNamespace.ServerType parse(java.io.Reader r)
				throws org.apache.xmlbeans.XmlException, java.io.IOException {
			return (noNamespace.ServerType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader()
					.parse(r, type, null);
		}

		public static noNamespace.ServerType parse(java.io.Reader r,
				org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException,
				java.io.IOException {
			return (noNamespace.ServerType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader()
					.parse(r, type, options);
		}

		public static noNamespace.ServerType parse(javax.xml.stream.XMLStreamReader sr)
				throws org.apache.xmlbeans.XmlException {
			return (noNamespace.ServerType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader()
					.parse(sr, type, null);
		}

		public static noNamespace.ServerType parse(javax.xml.stream.XMLStreamReader sr,
				org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
			return (noNamespace.ServerType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader()
					.parse(sr, type, options);
		}

		public static noNamespace.ServerType parse(org.w3c.dom.Node node)
				throws org.apache.xmlbeans.XmlException {
			return (noNamespace.ServerType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader()
					.parse(node, type, null);
		}

		public static noNamespace.ServerType parse(org.w3c.dom.Node node,
				org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
			return (noNamespace.ServerType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader()
					.parse(node, type, options);
		}

		/** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
		@Deprecated
		public static noNamespace.ServerType parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis)
				throws org.apache.xmlbeans.XmlException,
				org.apache.xmlbeans.xml.stream.XMLStreamException {
			return (noNamespace.ServerType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader()
					.parse(xis, type, null);
		}

		/** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
		@Deprecated
		public static noNamespace.ServerType parse(
				org.apache.xmlbeans.xml.stream.XMLInputStream xis,
				org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException,
				org.apache.xmlbeans.xml.stream.XMLStreamException {
			return (noNamespace.ServerType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader()
					.parse(xis, type, options);
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
