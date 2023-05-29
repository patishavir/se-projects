/*
 * An XML document type.
 * Localname: PerformanceMonitor
 * Namespace: 
 * Java type: noNamespace.PerformanceMonitorDocument
 *
 * Automatically generated - do not modify.
 */
package noNamespace.impl;

/**
 * A document containing one PerformanceMonitor(@) element.
 * 
 * This is a complex type.
 */
public class PerformanceMonitorDocumentImpl extends
		org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements
		noNamespace.PerformanceMonitorDocument {
	private static final long serialVersionUID = 1L;

	public PerformanceMonitorDocumentImpl(org.apache.xmlbeans.SchemaType sType) {
		super(sType);
	}

	private static final javax.xml.namespace.QName PERFORMANCEMONITOR$0 = new javax.xml.namespace.QName(
			"", "PerformanceMonitor");

	/**
	 * Gets the "PerformanceMonitor" element
	 */
	public noNamespace.PerformanceMonitorType getPerformanceMonitor() {
		synchronized (monitor()) {
			check_orphaned();
			noNamespace.PerformanceMonitorType target = null;
			target = (noNamespace.PerformanceMonitorType) get_store().find_element_user(
					PERFORMANCEMONITOR$0, 0);
			if (target == null) {
				return null;
			}
			return target;
		}
	}

	/**
	 * Sets the "PerformanceMonitor" element
	 */
	public void setPerformanceMonitor(noNamespace.PerformanceMonitorType performanceMonitor) {
		generatedSetterHelperImpl(performanceMonitor, PERFORMANCEMONITOR$0, 0,
				org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
	}

	/**
	 * Appends and returns a new empty "PerformanceMonitor" element
	 */
	public noNamespace.PerformanceMonitorType addNewPerformanceMonitor() {
		synchronized (monitor()) {
			check_orphaned();
			noNamespace.PerformanceMonitorType target = null;
			target = (noNamespace.PerformanceMonitorType) get_store().add_element_user(
					PERFORMANCEMONITOR$0);
			return target;
		}
	}
}
