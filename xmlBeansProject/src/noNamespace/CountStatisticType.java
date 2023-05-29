/*
 * XML Type:  CountStatisticType
 * Namespace: 
 * Java type: noNamespace.CountStatisticType
 *
 * Automatically generated - do not modify.
 */
package noNamespace;

/**
 * An XML CountStatisticType(@).
 * 
 * This is an atomic type that is a restriction of
 * noNamespace.CountStatisticType.
 */
public interface CountStatisticType extends org.apache.xmlbeans.XmlString {
	public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType) org.apache.xmlbeans.XmlBeans
			.typeSystemForClassLoader(CountStatisticType.class.getClassLoader(),
					"schemaorg_apache_xmlbeans.system.sCAE5236ABB51CCDB84E3164358F8EB30")
			.resolveHandle("countstatistictype0224type");

	/**
	 * Gets the "ID" attribute
	 */
	byte getID();

	/**
	 * Gets (as xml) the "ID" attribute
	 */
	org.apache.xmlbeans.XmlByte xgetID();

	/**
	 * True if has "ID" attribute
	 */
	boolean isSetID();

	/**
	 * Sets the "ID" attribute
	 */
	void setID(byte id);

	/**
	 * Sets (as xml) the "ID" attribute
	 */
	void xsetID(org.apache.xmlbeans.XmlByte id);

	/**
	 * Unsets the "ID" attribute
	 */
	void unsetID();

	/**
	 * Gets the "count" attribute
	 */
	int getCount();

	/**
	 * Gets (as xml) the "count" attribute
	 */
	org.apache.xmlbeans.XmlInt xgetCount();

	/**
	 * True if has "count" attribute
	 */
	boolean isSetCount();

	/**
	 * Sets the "count" attribute
	 */
	void setCount(int count);

	/**
	 * Sets (as xml) the "count" attribute
	 */
	void xsetCount(org.apache.xmlbeans.XmlInt count);

	/**
	 * Unsets the "count" attribute
	 */
	void unsetCount();

	/**
	 * Gets the "lastSampleTime" attribute
	 */
	long getLastSampleTime();

	/**
	 * Gets (as xml) the "lastSampleTime" attribute
	 */
	org.apache.xmlbeans.XmlLong xgetLastSampleTime();

	/**
	 * True if has "lastSampleTime" attribute
	 */
	boolean isSetLastSampleTime();

	/**
	 * Sets the "lastSampleTime" attribute
	 */
	void setLastSampleTime(long lastSampleTime);

	/**
	 * Sets (as xml) the "lastSampleTime" attribute
	 */
	void xsetLastSampleTime(org.apache.xmlbeans.XmlLong lastSampleTime);

	/**
	 * Unsets the "lastSampleTime" attribute
	 */
	void unsetLastSampleTime();

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
	 * Gets the "startTime" attribute
	 */
	long getStartTime();

	/**
	 * Gets (as xml) the "startTime" attribute
	 */
	org.apache.xmlbeans.XmlLong xgetStartTime();

	/**
	 * True if has "startTime" attribute
	 */
	boolean isSetStartTime();

	/**
	 * Sets the "startTime" attribute
	 */
	void setStartTime(long startTime);

	/**
	 * Sets (as xml) the "startTime" attribute
	 */
	void xsetStartTime(org.apache.xmlbeans.XmlLong startTime);

	/**
	 * Unsets the "startTime" attribute
	 */
	void unsetStartTime();

	/**
	 * Gets the "unit" attribute
	 */
	java.lang.String getUnit();

	/**
	 * Gets (as xml) the "unit" attribute
	 */
	org.apache.xmlbeans.XmlString xgetUnit();

	/**
	 * True if has "unit" attribute
	 */
	boolean isSetUnit();

	/**
	 * Sets the "unit" attribute
	 */
	void setUnit(java.lang.String unit);

	/**
	 * Sets (as xml) the "unit" attribute
	 */
	void xsetUnit(org.apache.xmlbeans.XmlString unit);

	/**
	 * Unsets the "unit" attribute
	 */
	void unsetUnit();

	/**
	 * A factory class with static methods for creating instances of this type.
	 */

	public static final class Factory {
		public static noNamespace.CountStatisticType newInstance() {
			return (noNamespace.CountStatisticType) org.apache.xmlbeans.XmlBeans
					.getContextTypeLoader().newInstance(type, null);
		}

		public static noNamespace.CountStatisticType newInstance(
				org.apache.xmlbeans.XmlOptions options) {
			return (noNamespace.CountStatisticType) org.apache.xmlbeans.XmlBeans
					.getContextTypeLoader().newInstance(type, options);
		}

		/**
		 * @param xmlAsString
		 *            the string value to parse
		 */
		public static noNamespace.CountStatisticType parse(java.lang.String xmlAsString)
				throws org.apache.xmlbeans.XmlException {
			return (noNamespace.CountStatisticType) org.apache.xmlbeans.XmlBeans
					.getContextTypeLoader().parse(xmlAsString, type, null);
		}

		public static noNamespace.CountStatisticType parse(java.lang.String xmlAsString,
				org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
			return (noNamespace.CountStatisticType) org.apache.xmlbeans.XmlBeans
					.getContextTypeLoader().parse(xmlAsString, type, options);
		}

		/**
		 * @param file
		 *            the file from which to load an xml document
		 */
		public static noNamespace.CountStatisticType parse(java.io.File file)
				throws org.apache.xmlbeans.XmlException, java.io.IOException {
			return (noNamespace.CountStatisticType) org.apache.xmlbeans.XmlBeans
					.getContextTypeLoader().parse(file, type, null);
		}

		public static noNamespace.CountStatisticType parse(java.io.File file,
				org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException,
				java.io.IOException {
			return (noNamespace.CountStatisticType) org.apache.xmlbeans.XmlBeans
					.getContextTypeLoader().parse(file, type, options);
		}

		public static noNamespace.CountStatisticType parse(java.net.URL u)
				throws org.apache.xmlbeans.XmlException, java.io.IOException {
			return (noNamespace.CountStatisticType) org.apache.xmlbeans.XmlBeans
					.getContextTypeLoader().parse(u, type, null);
		}

		public static noNamespace.CountStatisticType parse(java.net.URL u,
				org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException,
				java.io.IOException {
			return (noNamespace.CountStatisticType) org.apache.xmlbeans.XmlBeans
					.getContextTypeLoader().parse(u, type, options);
		}

		public static noNamespace.CountStatisticType parse(java.io.InputStream is)
				throws org.apache.xmlbeans.XmlException, java.io.IOException {
			return (noNamespace.CountStatisticType) org.apache.xmlbeans.XmlBeans
					.getContextTypeLoader().parse(is, type, null);
		}

		public static noNamespace.CountStatisticType parse(java.io.InputStream is,
				org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException,
				java.io.IOException {
			return (noNamespace.CountStatisticType) org.apache.xmlbeans.XmlBeans
					.getContextTypeLoader().parse(is, type, options);
		}

		public static noNamespace.CountStatisticType parse(java.io.Reader r)
				throws org.apache.xmlbeans.XmlException, java.io.IOException {
			return (noNamespace.CountStatisticType) org.apache.xmlbeans.XmlBeans
					.getContextTypeLoader().parse(r, type, null);
		}

		public static noNamespace.CountStatisticType parse(java.io.Reader r,
				org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException,
				java.io.IOException {
			return (noNamespace.CountStatisticType) org.apache.xmlbeans.XmlBeans
					.getContextTypeLoader().parse(r, type, options);
		}

		public static noNamespace.CountStatisticType parse(javax.xml.stream.XMLStreamReader sr)
				throws org.apache.xmlbeans.XmlException {
			return (noNamespace.CountStatisticType) org.apache.xmlbeans.XmlBeans
					.getContextTypeLoader().parse(sr, type, null);
		}

		public static noNamespace.CountStatisticType parse(javax.xml.stream.XMLStreamReader sr,
				org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
			return (noNamespace.CountStatisticType) org.apache.xmlbeans.XmlBeans
					.getContextTypeLoader().parse(sr, type, options);
		}

		public static noNamespace.CountStatisticType parse(org.w3c.dom.Node node)
				throws org.apache.xmlbeans.XmlException {
			return (noNamespace.CountStatisticType) org.apache.xmlbeans.XmlBeans
					.getContextTypeLoader().parse(node, type, null);
		}

		public static noNamespace.CountStatisticType parse(org.w3c.dom.Node node,
				org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
			return (noNamespace.CountStatisticType) org.apache.xmlbeans.XmlBeans
					.getContextTypeLoader().parse(node, type, options);
		}

		/** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
		@Deprecated
		public static noNamespace.CountStatisticType parse(
				org.apache.xmlbeans.xml.stream.XMLInputStream xis)
				throws org.apache.xmlbeans.XmlException,
				org.apache.xmlbeans.xml.stream.XMLStreamException {
			return (noNamespace.CountStatisticType) org.apache.xmlbeans.XmlBeans
					.getContextTypeLoader().parse(xis, type, null);
		}

		/** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
		@Deprecated
		public static noNamespace.CountStatisticType parse(
				org.apache.xmlbeans.xml.stream.XMLInputStream xis,
				org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException,
				org.apache.xmlbeans.xml.stream.XMLStreamException {
			return (noNamespace.CountStatisticType) org.apache.xmlbeans.XmlBeans
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
