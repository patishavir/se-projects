/*
 * XML Type:  TimeStatisticType
 * Namespace: 
 * Java type: noNamespace.TimeStatisticType
 *
 * Automatically generated - do not modify.
 */
package noNamespace.impl;

/**
 * An XML TimeStatisticType(@).
 * 
 * This is an atomic type that is a restriction of
 * noNamespace.TimeStatisticType.
 */
public class TimeStatisticTypeImpl extends org.apache.xmlbeans.impl.values.JavaStringHolderEx
		implements noNamespace.TimeStatisticType {
	private static final long serialVersionUID = 1L;

	public TimeStatisticTypeImpl(org.apache.xmlbeans.SchemaType sType) {
		super(sType, true);
	}

	protected TimeStatisticTypeImpl(org.apache.xmlbeans.SchemaType sType, boolean b) {
		super(sType, b);
	}

	private static final javax.xml.namespace.QName ID$0 = new javax.xml.namespace.QName("", "ID");
	private static final javax.xml.namespace.QName LASTSAMPLETIME$2 = new javax.xml.namespace.QName(
			"", "lastSampleTime");
	private static final javax.xml.namespace.QName MAX$4 = new javax.xml.namespace.QName("", "max");
	private static final javax.xml.namespace.QName MIN$6 = new javax.xml.namespace.QName("", "min");
	private static final javax.xml.namespace.QName NAME$8 = new javax.xml.namespace.QName("",
			"name");
	private static final javax.xml.namespace.QName STARTTIME$10 = new javax.xml.namespace.QName("",
			"startTime");
	private static final javax.xml.namespace.QName TOTALTIME$12 = new javax.xml.namespace.QName("",
			"totalTime");
	private static final javax.xml.namespace.QName UNIT$14 = new javax.xml.namespace.QName("",
			"unit");

	/**
	 * Gets the "ID" attribute
	 */
	public byte getID() {
		synchronized (monitor()) {
			check_orphaned();
			org.apache.xmlbeans.SimpleValue target = null;
			target = (org.apache.xmlbeans.SimpleValue) get_store().find_attribute_user(ID$0);
			if (target == null) {
				return 0;
			}
			return target.getByteValue();
		}
	}

	/**
	 * Gets (as xml) the "ID" attribute
	 */
	public org.apache.xmlbeans.XmlByte xgetID() {
		synchronized (monitor()) {
			check_orphaned();
			org.apache.xmlbeans.XmlByte target = null;
			target = (org.apache.xmlbeans.XmlByte) get_store().find_attribute_user(ID$0);
			return target;
		}
	}

	/**
	 * True if has "ID" attribute
	 */
	public boolean isSetID() {
		synchronized (monitor()) {
			check_orphaned();
			return get_store().find_attribute_user(ID$0) != null;
		}
	}

	/**
	 * Sets the "ID" attribute
	 */
	public void setID(byte id) {
		synchronized (monitor()) {
			check_orphaned();
			org.apache.xmlbeans.SimpleValue target = null;
			target = (org.apache.xmlbeans.SimpleValue) get_store().find_attribute_user(ID$0);
			if (target == null) {
				target = (org.apache.xmlbeans.SimpleValue) get_store().add_attribute_user(ID$0);
			}
			target.setByteValue(id);
		}
	}

	/**
	 * Sets (as xml) the "ID" attribute
	 */
	public void xsetID(org.apache.xmlbeans.XmlByte id) {
		synchronized (monitor()) {
			check_orphaned();
			org.apache.xmlbeans.XmlByte target = null;
			target = (org.apache.xmlbeans.XmlByte) get_store().find_attribute_user(ID$0);
			if (target == null) {
				target = (org.apache.xmlbeans.XmlByte) get_store().add_attribute_user(ID$0);
			}
			target.set(id);
		}
	}

	/**
	 * Unsets the "ID" attribute
	 */
	public void unsetID() {
		synchronized (monitor()) {
			check_orphaned();
			get_store().remove_attribute(ID$0);
		}
	}

	/**
	 * Gets the "lastSampleTime" attribute
	 */
	public long getLastSampleTime() {
		synchronized (monitor()) {
			check_orphaned();
			org.apache.xmlbeans.SimpleValue target = null;
			target = (org.apache.xmlbeans.SimpleValue) get_store().find_attribute_user(
					LASTSAMPLETIME$2);
			if (target == null) {
				return 0L;
			}
			return target.getLongValue();
		}
	}

	/**
	 * Gets (as xml) the "lastSampleTime" attribute
	 */
	public org.apache.xmlbeans.XmlLong xgetLastSampleTime() {
		synchronized (monitor()) {
			check_orphaned();
			org.apache.xmlbeans.XmlLong target = null;
			target = (org.apache.xmlbeans.XmlLong) get_store()
					.find_attribute_user(LASTSAMPLETIME$2);
			return target;
		}
	}

	/**
	 * True if has "lastSampleTime" attribute
	 */
	public boolean isSetLastSampleTime() {
		synchronized (monitor()) {
			check_orphaned();
			return get_store().find_attribute_user(LASTSAMPLETIME$2) != null;
		}
	}

	/**
	 * Sets the "lastSampleTime" attribute
	 */
	public void setLastSampleTime(long lastSampleTime) {
		synchronized (monitor()) {
			check_orphaned();
			org.apache.xmlbeans.SimpleValue target = null;
			target = (org.apache.xmlbeans.SimpleValue) get_store().find_attribute_user(
					LASTSAMPLETIME$2);
			if (target == null) {
				target = (org.apache.xmlbeans.SimpleValue) get_store().add_attribute_user(
						LASTSAMPLETIME$2);
			}
			target.setLongValue(lastSampleTime);
		}
	}

	/**
	 * Sets (as xml) the "lastSampleTime" attribute
	 */
	public void xsetLastSampleTime(org.apache.xmlbeans.XmlLong lastSampleTime) {
		synchronized (monitor()) {
			check_orphaned();
			org.apache.xmlbeans.XmlLong target = null;
			target = (org.apache.xmlbeans.XmlLong) get_store()
					.find_attribute_user(LASTSAMPLETIME$2);
			if (target == null) {
				target = (org.apache.xmlbeans.XmlLong) get_store().add_attribute_user(
						LASTSAMPLETIME$2);
			}
			target.set(lastSampleTime);
		}
	}

	/**
	 * Unsets the "lastSampleTime" attribute
	 */
	public void unsetLastSampleTime() {
		synchronized (monitor()) {
			check_orphaned();
			get_store().remove_attribute(LASTSAMPLETIME$2);
		}
	}

	/**
	 * Gets the "max" attribute
	 */
	public int getMax() {
		synchronized (monitor()) {
			check_orphaned();
			org.apache.xmlbeans.SimpleValue target = null;
			target = (org.apache.xmlbeans.SimpleValue) get_store().find_attribute_user(MAX$4);
			if (target == null) {
				return 0;
			}
			return target.getIntValue();
		}
	}

	/**
	 * Gets (as xml) the "max" attribute
	 */
	public org.apache.xmlbeans.XmlInt xgetMax() {
		synchronized (monitor()) {
			check_orphaned();
			org.apache.xmlbeans.XmlInt target = null;
			target = (org.apache.xmlbeans.XmlInt) get_store().find_attribute_user(MAX$4);
			return target;
		}
	}

	/**
	 * True if has "max" attribute
	 */
	public boolean isSetMax() {
		synchronized (monitor()) {
			check_orphaned();
			return get_store().find_attribute_user(MAX$4) != null;
		}
	}

	/**
	 * Sets the "max" attribute
	 */
	public void setMax(int max) {
		synchronized (monitor()) {
			check_orphaned();
			org.apache.xmlbeans.SimpleValue target = null;
			target = (org.apache.xmlbeans.SimpleValue) get_store().find_attribute_user(MAX$4);
			if (target == null) {
				target = (org.apache.xmlbeans.SimpleValue) get_store().add_attribute_user(MAX$4);
			}
			target.setIntValue(max);
		}
	}

	/**
	 * Sets (as xml) the "max" attribute
	 */
	public void xsetMax(org.apache.xmlbeans.XmlInt max) {
		synchronized (monitor()) {
			check_orphaned();
			org.apache.xmlbeans.XmlInt target = null;
			target = (org.apache.xmlbeans.XmlInt) get_store().find_attribute_user(MAX$4);
			if (target == null) {
				target = (org.apache.xmlbeans.XmlInt) get_store().add_attribute_user(MAX$4);
			}
			target.set(max);
		}
	}

	/**
	 * Unsets the "max" attribute
	 */
	public void unsetMax() {
		synchronized (monitor()) {
			check_orphaned();
			get_store().remove_attribute(MAX$4);
		}
	}

	/**
	 * Gets the "min" attribute
	 */
	public short getMin() {
		synchronized (monitor()) {
			check_orphaned();
			org.apache.xmlbeans.SimpleValue target = null;
			target = (org.apache.xmlbeans.SimpleValue) get_store().find_attribute_user(MIN$6);
			if (target == null) {
				return 0;
			}
			return target.getShortValue();
		}
	}

	/**
	 * Gets (as xml) the "min" attribute
	 */
	public org.apache.xmlbeans.XmlShort xgetMin() {
		synchronized (monitor()) {
			check_orphaned();
			org.apache.xmlbeans.XmlShort target = null;
			target = (org.apache.xmlbeans.XmlShort) get_store().find_attribute_user(MIN$6);
			return target;
		}
	}

	/**
	 * True if has "min" attribute
	 */
	public boolean isSetMin() {
		synchronized (monitor()) {
			check_orphaned();
			return get_store().find_attribute_user(MIN$6) != null;
		}
	}

	/**
	 * Sets the "min" attribute
	 */
	public void setMin(short min) {
		synchronized (monitor()) {
			check_orphaned();
			org.apache.xmlbeans.SimpleValue target = null;
			target = (org.apache.xmlbeans.SimpleValue) get_store().find_attribute_user(MIN$6);
			if (target == null) {
				target = (org.apache.xmlbeans.SimpleValue) get_store().add_attribute_user(MIN$6);
			}
			target.setShortValue(min);
		}
	}

	/**
	 * Sets (as xml) the "min" attribute
	 */
	public void xsetMin(org.apache.xmlbeans.XmlShort min) {
		synchronized (monitor()) {
			check_orphaned();
			org.apache.xmlbeans.XmlShort target = null;
			target = (org.apache.xmlbeans.XmlShort) get_store().find_attribute_user(MIN$6);
			if (target == null) {
				target = (org.apache.xmlbeans.XmlShort) get_store().add_attribute_user(MIN$6);
			}
			target.set(min);
		}
	}

	/**
	 * Unsets the "min" attribute
	 */
	public void unsetMin() {
		synchronized (monitor()) {
			check_orphaned();
			get_store().remove_attribute(MIN$6);
		}
	}

	/**
	 * Gets the "name" attribute
	 */
	public java.lang.String getName() {
		synchronized (monitor()) {
			check_orphaned();
			org.apache.xmlbeans.SimpleValue target = null;
			target = (org.apache.xmlbeans.SimpleValue) get_store().find_attribute_user(NAME$8);
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
			target = (org.apache.xmlbeans.XmlString) get_store().find_attribute_user(NAME$8);
			return target;
		}
	}

	/**
	 * True if has "name" attribute
	 */
	public boolean isSetName() {
		synchronized (monitor()) {
			check_orphaned();
			return get_store().find_attribute_user(NAME$8) != null;
		}
	}

	/**
	 * Sets the "name" attribute
	 */
	public void setName(java.lang.String name) {
		synchronized (monitor()) {
			check_orphaned();
			org.apache.xmlbeans.SimpleValue target = null;
			target = (org.apache.xmlbeans.SimpleValue) get_store().find_attribute_user(NAME$8);
			if (target == null) {
				target = (org.apache.xmlbeans.SimpleValue) get_store().add_attribute_user(NAME$8);
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
			target = (org.apache.xmlbeans.XmlString) get_store().find_attribute_user(NAME$8);
			if (target == null) {
				target = (org.apache.xmlbeans.XmlString) get_store().add_attribute_user(NAME$8);
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
			get_store().remove_attribute(NAME$8);
		}
	}

	/**
	 * Gets the "startTime" attribute
	 */
	public long getStartTime() {
		synchronized (monitor()) {
			check_orphaned();
			org.apache.xmlbeans.SimpleValue target = null;
			target = (org.apache.xmlbeans.SimpleValue) get_store()
					.find_attribute_user(STARTTIME$10);
			if (target == null) {
				return 0L;
			}
			return target.getLongValue();
		}
	}

	/**
	 * Gets (as xml) the "startTime" attribute
	 */
	public org.apache.xmlbeans.XmlLong xgetStartTime() {
		synchronized (monitor()) {
			check_orphaned();
			org.apache.xmlbeans.XmlLong target = null;
			target = (org.apache.xmlbeans.XmlLong) get_store().find_attribute_user(STARTTIME$10);
			return target;
		}
	}

	/**
	 * True if has "startTime" attribute
	 */
	public boolean isSetStartTime() {
		synchronized (monitor()) {
			check_orphaned();
			return get_store().find_attribute_user(STARTTIME$10) != null;
		}
	}

	/**
	 * Sets the "startTime" attribute
	 */
	public void setStartTime(long startTime) {
		synchronized (monitor()) {
			check_orphaned();
			org.apache.xmlbeans.SimpleValue target = null;
			target = (org.apache.xmlbeans.SimpleValue) get_store()
					.find_attribute_user(STARTTIME$10);
			if (target == null) {
				target = (org.apache.xmlbeans.SimpleValue) get_store().add_attribute_user(
						STARTTIME$10);
			}
			target.setLongValue(startTime);
		}
	}

	/**
	 * Sets (as xml) the "startTime" attribute
	 */
	public void xsetStartTime(org.apache.xmlbeans.XmlLong startTime) {
		synchronized (monitor()) {
			check_orphaned();
			org.apache.xmlbeans.XmlLong target = null;
			target = (org.apache.xmlbeans.XmlLong) get_store().find_attribute_user(STARTTIME$10);
			if (target == null) {
				target = (org.apache.xmlbeans.XmlLong) get_store().add_attribute_user(STARTTIME$10);
			}
			target.set(startTime);
		}
	}

	/**
	 * Unsets the "startTime" attribute
	 */
	public void unsetStartTime() {
		synchronized (monitor()) {
			check_orphaned();
			get_store().remove_attribute(STARTTIME$10);
		}
	}

	/**
	 * Gets the "totalTime" attribute
	 */
	public int getTotalTime() {
		synchronized (monitor()) {
			check_orphaned();
			org.apache.xmlbeans.SimpleValue target = null;
			target = (org.apache.xmlbeans.SimpleValue) get_store()
					.find_attribute_user(TOTALTIME$12);
			if (target == null) {
				return 0;
			}
			return target.getIntValue();
		}
	}

	/**
	 * Gets (as xml) the "totalTime" attribute
	 */
	public org.apache.xmlbeans.XmlInt xgetTotalTime() {
		synchronized (monitor()) {
			check_orphaned();
			org.apache.xmlbeans.XmlInt target = null;
			target = (org.apache.xmlbeans.XmlInt) get_store().find_attribute_user(TOTALTIME$12);
			return target;
		}
	}

	/**
	 * True if has "totalTime" attribute
	 */
	public boolean isSetTotalTime() {
		synchronized (monitor()) {
			check_orphaned();
			return get_store().find_attribute_user(TOTALTIME$12) != null;
		}
	}

	/**
	 * Sets the "totalTime" attribute
	 */
	public void setTotalTime(int totalTime) {
		synchronized (monitor()) {
			check_orphaned();
			org.apache.xmlbeans.SimpleValue target = null;
			target = (org.apache.xmlbeans.SimpleValue) get_store()
					.find_attribute_user(TOTALTIME$12);
			if (target == null) {
				target = (org.apache.xmlbeans.SimpleValue) get_store().add_attribute_user(
						TOTALTIME$12);
			}
			target.setIntValue(totalTime);
		}
	}

	/**
	 * Sets (as xml) the "totalTime" attribute
	 */
	public void xsetTotalTime(org.apache.xmlbeans.XmlInt totalTime) {
		synchronized (monitor()) {
			check_orphaned();
			org.apache.xmlbeans.XmlInt target = null;
			target = (org.apache.xmlbeans.XmlInt) get_store().find_attribute_user(TOTALTIME$12);
			if (target == null) {
				target = (org.apache.xmlbeans.XmlInt) get_store().add_attribute_user(TOTALTIME$12);
			}
			target.set(totalTime);
		}
	}

	/**
	 * Unsets the "totalTime" attribute
	 */
	public void unsetTotalTime() {
		synchronized (monitor()) {
			check_orphaned();
			get_store().remove_attribute(TOTALTIME$12);
		}
	}

	/**
	 * Gets the "unit" attribute
	 */
	public java.lang.String getUnit() {
		synchronized (monitor()) {
			check_orphaned();
			org.apache.xmlbeans.SimpleValue target = null;
			target = (org.apache.xmlbeans.SimpleValue) get_store().find_attribute_user(UNIT$14);
			if (target == null) {
				return null;
			}
			return target.getStringValue();
		}
	}

	/**
	 * Gets (as xml) the "unit" attribute
	 */
	public org.apache.xmlbeans.XmlString xgetUnit() {
		synchronized (monitor()) {
			check_orphaned();
			org.apache.xmlbeans.XmlString target = null;
			target = (org.apache.xmlbeans.XmlString) get_store().find_attribute_user(UNIT$14);
			return target;
		}
	}

	/**
	 * True if has "unit" attribute
	 */
	public boolean isSetUnit() {
		synchronized (monitor()) {
			check_orphaned();
			return get_store().find_attribute_user(UNIT$14) != null;
		}
	}

	/**
	 * Sets the "unit" attribute
	 */
	public void setUnit(java.lang.String unit) {
		synchronized (monitor()) {
			check_orphaned();
			org.apache.xmlbeans.SimpleValue target = null;
			target = (org.apache.xmlbeans.SimpleValue) get_store().find_attribute_user(UNIT$14);
			if (target == null) {
				target = (org.apache.xmlbeans.SimpleValue) get_store().add_attribute_user(UNIT$14);
			}
			target.setStringValue(unit);
		}
	}

	/**
	 * Sets (as xml) the "unit" attribute
	 */
	public void xsetUnit(org.apache.xmlbeans.XmlString unit) {
		synchronized (monitor()) {
			check_orphaned();
			org.apache.xmlbeans.XmlString target = null;
			target = (org.apache.xmlbeans.XmlString) get_store().find_attribute_user(UNIT$14);
			if (target == null) {
				target = (org.apache.xmlbeans.XmlString) get_store().add_attribute_user(UNIT$14);
			}
			target.set(unit);
		}
	}

	/**
	 * Unsets the "unit" attribute
	 */
	public void unsetUnit() {
		synchronized (monitor()) {
			check_orphaned();
			get_store().remove_attribute(UNIT$14);
		}
	}
}
