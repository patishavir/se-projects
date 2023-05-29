/*
 * XML Type:  BoundedRangeStatisticType
 * Namespace: 
 * Java type: noNamespace.BoundedRangeStatisticType
 *
 * Automatically generated - do not modify.
 */
package noNamespace.impl;

/**
 * An XML BoundedRangeStatisticType(@).
 * 
 * This is an atomic type that is a restriction of
 * noNamespace.BoundedRangeStatisticType.
 */
public class BoundedRangeStatisticTypeImpl extends
		org.apache.xmlbeans.impl.values.JavaStringHolderEx implements
		noNamespace.BoundedRangeStatisticType {
	private static final long serialVersionUID = 1L;

	public BoundedRangeStatisticTypeImpl(org.apache.xmlbeans.SchemaType sType) {
		super(sType, true);
	}

	protected BoundedRangeStatisticTypeImpl(org.apache.xmlbeans.SchemaType sType, boolean b) {
		super(sType, b);
	}

	private static final javax.xml.namespace.QName ID$0 = new javax.xml.namespace.QName("", "ID");
	private static final javax.xml.namespace.QName HIGHWATERMARK$2 = new javax.xml.namespace.QName(
			"", "highWaterMark");
	private static final javax.xml.namespace.QName INTEGRAL$4 = new javax.xml.namespace.QName("",
			"integral");
	private static final javax.xml.namespace.QName LASTSAMPLETIME$6 = new javax.xml.namespace.QName(
			"", "lastSampleTime");
	private static final javax.xml.namespace.QName LOWWATERMARK$8 = new javax.xml.namespace.QName(
			"", "lowWaterMark");
	private static final javax.xml.namespace.QName LOWERBOUND$10 = new javax.xml.namespace.QName(
			"", "lowerBound");
	private static final javax.xml.namespace.QName MEAN$12 = new javax.xml.namespace.QName("",
			"mean");
	private static final javax.xml.namespace.QName NAME$14 = new javax.xml.namespace.QName("",
			"name");
	private static final javax.xml.namespace.QName STARTTIME$16 = new javax.xml.namespace.QName("",
			"startTime");
	private static final javax.xml.namespace.QName UNIT$18 = new javax.xml.namespace.QName("",
			"unit");
	private static final javax.xml.namespace.QName UPPERBOUND$20 = new javax.xml.namespace.QName(
			"", "upperBound");
	private static final javax.xml.namespace.QName VALUE$22 = new javax.xml.namespace.QName("",
			"value");

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
	 * Gets the "highWaterMark" attribute
	 */
	public int getHighWaterMark() {
		synchronized (monitor()) {
			check_orphaned();
			org.apache.xmlbeans.SimpleValue target = null;
			target = (org.apache.xmlbeans.SimpleValue) get_store().find_attribute_user(
					HIGHWATERMARK$2);
			if (target == null) {
				return 0;
			}
			return target.getIntValue();
		}
	}

	/**
	 * Gets (as xml) the "highWaterMark" attribute
	 */
	public org.apache.xmlbeans.XmlInt xgetHighWaterMark() {
		synchronized (monitor()) {
			check_orphaned();
			org.apache.xmlbeans.XmlInt target = null;
			target = (org.apache.xmlbeans.XmlInt) get_store().find_attribute_user(HIGHWATERMARK$2);
			return target;
		}
	}

	/**
	 * True if has "highWaterMark" attribute
	 */
	public boolean isSetHighWaterMark() {
		synchronized (monitor()) {
			check_orphaned();
			return get_store().find_attribute_user(HIGHWATERMARK$2) != null;
		}
	}

	/**
	 * Sets the "highWaterMark" attribute
	 */
	public void setHighWaterMark(int highWaterMark) {
		synchronized (monitor()) {
			check_orphaned();
			org.apache.xmlbeans.SimpleValue target = null;
			target = (org.apache.xmlbeans.SimpleValue) get_store().find_attribute_user(
					HIGHWATERMARK$2);
			if (target == null) {
				target = (org.apache.xmlbeans.SimpleValue) get_store().add_attribute_user(
						HIGHWATERMARK$2);
			}
			target.setIntValue(highWaterMark);
		}
	}

	/**
	 * Sets (as xml) the "highWaterMark" attribute
	 */
	public void xsetHighWaterMark(org.apache.xmlbeans.XmlInt highWaterMark) {
		synchronized (monitor()) {
			check_orphaned();
			org.apache.xmlbeans.XmlInt target = null;
			target = (org.apache.xmlbeans.XmlInt) get_store().find_attribute_user(HIGHWATERMARK$2);
			if (target == null) {
				target = (org.apache.xmlbeans.XmlInt) get_store().add_attribute_user(
						HIGHWATERMARK$2);
			}
			target.set(highWaterMark);
		}
	}

	/**
	 * Unsets the "highWaterMark" attribute
	 */
	public void unsetHighWaterMark() {
		synchronized (monitor()) {
			check_orphaned();
			get_store().remove_attribute(HIGHWATERMARK$2);
		}
	}

	/**
	 * Gets the "integral" attribute
	 */
	public float getIntegral() {
		synchronized (monitor()) {
			check_orphaned();
			org.apache.xmlbeans.SimpleValue target = null;
			target = (org.apache.xmlbeans.SimpleValue) get_store().find_attribute_user(INTEGRAL$4);
			if (target == null) {
				return 0.0f;
			}
			return target.getFloatValue();
		}
	}

	/**
	 * Gets (as xml) the "integral" attribute
	 */
	public org.apache.xmlbeans.XmlFloat xgetIntegral() {
		synchronized (monitor()) {
			check_orphaned();
			org.apache.xmlbeans.XmlFloat target = null;
			target = (org.apache.xmlbeans.XmlFloat) get_store().find_attribute_user(INTEGRAL$4);
			return target;
		}
	}

	/**
	 * True if has "integral" attribute
	 */
	public boolean isSetIntegral() {
		synchronized (monitor()) {
			check_orphaned();
			return get_store().find_attribute_user(INTEGRAL$4) != null;
		}
	}

	/**
	 * Sets the "integral" attribute
	 */
	public void setIntegral(float integral) {
		synchronized (monitor()) {
			check_orphaned();
			org.apache.xmlbeans.SimpleValue target = null;
			target = (org.apache.xmlbeans.SimpleValue) get_store().find_attribute_user(INTEGRAL$4);
			if (target == null) {
				target = (org.apache.xmlbeans.SimpleValue) get_store().add_attribute_user(
						INTEGRAL$4);
			}
			target.setFloatValue(integral);
		}
	}

	/**
	 * Sets (as xml) the "integral" attribute
	 */
	public void xsetIntegral(org.apache.xmlbeans.XmlFloat integral) {
		synchronized (monitor()) {
			check_orphaned();
			org.apache.xmlbeans.XmlFloat target = null;
			target = (org.apache.xmlbeans.XmlFloat) get_store().find_attribute_user(INTEGRAL$4);
			if (target == null) {
				target = (org.apache.xmlbeans.XmlFloat) get_store().add_attribute_user(INTEGRAL$4);
			}
			target.set(integral);
		}
	}

	/**
	 * Unsets the "integral" attribute
	 */
	public void unsetIntegral() {
		synchronized (monitor()) {
			check_orphaned();
			get_store().remove_attribute(INTEGRAL$4);
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
					LASTSAMPLETIME$6);
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
					.find_attribute_user(LASTSAMPLETIME$6);
			return target;
		}
	}

	/**
	 * True if has "lastSampleTime" attribute
	 */
	public boolean isSetLastSampleTime() {
		synchronized (monitor()) {
			check_orphaned();
			return get_store().find_attribute_user(LASTSAMPLETIME$6) != null;
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
					LASTSAMPLETIME$6);
			if (target == null) {
				target = (org.apache.xmlbeans.SimpleValue) get_store().add_attribute_user(
						LASTSAMPLETIME$6);
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
					.find_attribute_user(LASTSAMPLETIME$6);
			if (target == null) {
				target = (org.apache.xmlbeans.XmlLong) get_store().add_attribute_user(
						LASTSAMPLETIME$6);
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
			get_store().remove_attribute(LASTSAMPLETIME$6);
		}
	}

	/**
	 * Gets the "lowWaterMark" attribute
	 */
	public int getLowWaterMark() {
		synchronized (monitor()) {
			check_orphaned();
			org.apache.xmlbeans.SimpleValue target = null;
			target = (org.apache.xmlbeans.SimpleValue) get_store().find_attribute_user(
					LOWWATERMARK$8);
			if (target == null) {
				return 0;
			}
			return target.getIntValue();
		}
	}

	/**
	 * Gets (as xml) the "lowWaterMark" attribute
	 */
	public org.apache.xmlbeans.XmlInt xgetLowWaterMark() {
		synchronized (monitor()) {
			check_orphaned();
			org.apache.xmlbeans.XmlInt target = null;
			target = (org.apache.xmlbeans.XmlInt) get_store().find_attribute_user(LOWWATERMARK$8);
			return target;
		}
	}

	/**
	 * True if has "lowWaterMark" attribute
	 */
	public boolean isSetLowWaterMark() {
		synchronized (monitor()) {
			check_orphaned();
			return get_store().find_attribute_user(LOWWATERMARK$8) != null;
		}
	}

	/**
	 * Sets the "lowWaterMark" attribute
	 */
	public void setLowWaterMark(int lowWaterMark) {
		synchronized (monitor()) {
			check_orphaned();
			org.apache.xmlbeans.SimpleValue target = null;
			target = (org.apache.xmlbeans.SimpleValue) get_store().find_attribute_user(
					LOWWATERMARK$8);
			if (target == null) {
				target = (org.apache.xmlbeans.SimpleValue) get_store().add_attribute_user(
						LOWWATERMARK$8);
			}
			target.setIntValue(lowWaterMark);
		}
	}

	/**
	 * Sets (as xml) the "lowWaterMark" attribute
	 */
	public void xsetLowWaterMark(org.apache.xmlbeans.XmlInt lowWaterMark) {
		synchronized (monitor()) {
			check_orphaned();
			org.apache.xmlbeans.XmlInt target = null;
			target = (org.apache.xmlbeans.XmlInt) get_store().find_attribute_user(LOWWATERMARK$8);
			if (target == null) {
				target = (org.apache.xmlbeans.XmlInt) get_store()
						.add_attribute_user(LOWWATERMARK$8);
			}
			target.set(lowWaterMark);
		}
	}

	/**
	 * Unsets the "lowWaterMark" attribute
	 */
	public void unsetLowWaterMark() {
		synchronized (monitor()) {
			check_orphaned();
			get_store().remove_attribute(LOWWATERMARK$8);
		}
	}

	/**
	 * Gets the "lowerBound" attribute
	 */
	public int getLowerBound() {
		synchronized (monitor()) {
			check_orphaned();
			org.apache.xmlbeans.SimpleValue target = null;
			target = (org.apache.xmlbeans.SimpleValue) get_store().find_attribute_user(
					LOWERBOUND$10);
			if (target == null) {
				return 0;
			}
			return target.getIntValue();
		}
	}

	/**
	 * Gets (as xml) the "lowerBound" attribute
	 */
	public org.apache.xmlbeans.XmlInt xgetLowerBound() {
		synchronized (monitor()) {
			check_orphaned();
			org.apache.xmlbeans.XmlInt target = null;
			target = (org.apache.xmlbeans.XmlInt) get_store().find_attribute_user(LOWERBOUND$10);
			return target;
		}
	}

	/**
	 * True if has "lowerBound" attribute
	 */
	public boolean isSetLowerBound() {
		synchronized (monitor()) {
			check_orphaned();
			return get_store().find_attribute_user(LOWERBOUND$10) != null;
		}
	}

	/**
	 * Sets the "lowerBound" attribute
	 */
	public void setLowerBound(int lowerBound) {
		synchronized (monitor()) {
			check_orphaned();
			org.apache.xmlbeans.SimpleValue target = null;
			target = (org.apache.xmlbeans.SimpleValue) get_store().find_attribute_user(
					LOWERBOUND$10);
			if (target == null) {
				target = (org.apache.xmlbeans.SimpleValue) get_store().add_attribute_user(
						LOWERBOUND$10);
			}
			target.setIntValue(lowerBound);
		}
	}

	/**
	 * Sets (as xml) the "lowerBound" attribute
	 */
	public void xsetLowerBound(org.apache.xmlbeans.XmlInt lowerBound) {
		synchronized (monitor()) {
			check_orphaned();
			org.apache.xmlbeans.XmlInt target = null;
			target = (org.apache.xmlbeans.XmlInt) get_store().find_attribute_user(LOWERBOUND$10);
			if (target == null) {
				target = (org.apache.xmlbeans.XmlInt) get_store().add_attribute_user(LOWERBOUND$10);
			}
			target.set(lowerBound);
		}
	}

	/**
	 * Unsets the "lowerBound" attribute
	 */
	public void unsetLowerBound() {
		synchronized (monitor()) {
			check_orphaned();
			get_store().remove_attribute(LOWERBOUND$10);
		}
	}

	/**
	 * Gets the "mean" attribute
	 */
	public float getMean() {
		synchronized (monitor()) {
			check_orphaned();
			org.apache.xmlbeans.SimpleValue target = null;
			target = (org.apache.xmlbeans.SimpleValue) get_store().find_attribute_user(MEAN$12);
			if (target == null) {
				return 0.0f;
			}
			return target.getFloatValue();
		}
	}

	/**
	 * Gets (as xml) the "mean" attribute
	 */
	public org.apache.xmlbeans.XmlFloat xgetMean() {
		synchronized (monitor()) {
			check_orphaned();
			org.apache.xmlbeans.XmlFloat target = null;
			target = (org.apache.xmlbeans.XmlFloat) get_store().find_attribute_user(MEAN$12);
			return target;
		}
	}

	/**
	 * True if has "mean" attribute
	 */
	public boolean isSetMean() {
		synchronized (monitor()) {
			check_orphaned();
			return get_store().find_attribute_user(MEAN$12) != null;
		}
	}

	/**
	 * Sets the "mean" attribute
	 */
	public void setMean(float mean) {
		synchronized (monitor()) {
			check_orphaned();
			org.apache.xmlbeans.SimpleValue target = null;
			target = (org.apache.xmlbeans.SimpleValue) get_store().find_attribute_user(MEAN$12);
			if (target == null) {
				target = (org.apache.xmlbeans.SimpleValue) get_store().add_attribute_user(MEAN$12);
			}
			target.setFloatValue(mean);
		}
	}

	/**
	 * Sets (as xml) the "mean" attribute
	 */
	public void xsetMean(org.apache.xmlbeans.XmlFloat mean) {
		synchronized (monitor()) {
			check_orphaned();
			org.apache.xmlbeans.XmlFloat target = null;
			target = (org.apache.xmlbeans.XmlFloat) get_store().find_attribute_user(MEAN$12);
			if (target == null) {
				target = (org.apache.xmlbeans.XmlFloat) get_store().add_attribute_user(MEAN$12);
			}
			target.set(mean);
		}
	}

	/**
	 * Unsets the "mean" attribute
	 */
	public void unsetMean() {
		synchronized (monitor()) {
			check_orphaned();
			get_store().remove_attribute(MEAN$12);
		}
	}

	/**
	 * Gets the "name" attribute
	 */
	public java.lang.String getName() {
		synchronized (monitor()) {
			check_orphaned();
			org.apache.xmlbeans.SimpleValue target = null;
			target = (org.apache.xmlbeans.SimpleValue) get_store().find_attribute_user(NAME$14);
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
			target = (org.apache.xmlbeans.XmlString) get_store().find_attribute_user(NAME$14);
			return target;
		}
	}

	/**
	 * True if has "name" attribute
	 */
	public boolean isSetName() {
		synchronized (monitor()) {
			check_orphaned();
			return get_store().find_attribute_user(NAME$14) != null;
		}
	}

	/**
	 * Sets the "name" attribute
	 */
	public void setName(java.lang.String name) {
		synchronized (monitor()) {
			check_orphaned();
			org.apache.xmlbeans.SimpleValue target = null;
			target = (org.apache.xmlbeans.SimpleValue) get_store().find_attribute_user(NAME$14);
			if (target == null) {
				target = (org.apache.xmlbeans.SimpleValue) get_store().add_attribute_user(NAME$14);
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
			target = (org.apache.xmlbeans.XmlString) get_store().find_attribute_user(NAME$14);
			if (target == null) {
				target = (org.apache.xmlbeans.XmlString) get_store().add_attribute_user(NAME$14);
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
			get_store().remove_attribute(NAME$14);
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
					.find_attribute_user(STARTTIME$16);
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
			target = (org.apache.xmlbeans.XmlLong) get_store().find_attribute_user(STARTTIME$16);
			return target;
		}
	}

	/**
	 * True if has "startTime" attribute
	 */
	public boolean isSetStartTime() {
		synchronized (monitor()) {
			check_orphaned();
			return get_store().find_attribute_user(STARTTIME$16) != null;
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
					.find_attribute_user(STARTTIME$16);
			if (target == null) {
				target = (org.apache.xmlbeans.SimpleValue) get_store().add_attribute_user(
						STARTTIME$16);
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
			target = (org.apache.xmlbeans.XmlLong) get_store().find_attribute_user(STARTTIME$16);
			if (target == null) {
				target = (org.apache.xmlbeans.XmlLong) get_store().add_attribute_user(STARTTIME$16);
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
			get_store().remove_attribute(STARTTIME$16);
		}
	}

	/**
	 * Gets the "unit" attribute
	 */
	public java.lang.String getUnit() {
		synchronized (monitor()) {
			check_orphaned();
			org.apache.xmlbeans.SimpleValue target = null;
			target = (org.apache.xmlbeans.SimpleValue) get_store().find_attribute_user(UNIT$18);
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
			target = (org.apache.xmlbeans.XmlString) get_store().find_attribute_user(UNIT$18);
			return target;
		}
	}

	/**
	 * True if has "unit" attribute
	 */
	public boolean isSetUnit() {
		synchronized (monitor()) {
			check_orphaned();
			return get_store().find_attribute_user(UNIT$18) != null;
		}
	}

	/**
	 * Sets the "unit" attribute
	 */
	public void setUnit(java.lang.String unit) {
		synchronized (monitor()) {
			check_orphaned();
			org.apache.xmlbeans.SimpleValue target = null;
			target = (org.apache.xmlbeans.SimpleValue) get_store().find_attribute_user(UNIT$18);
			if (target == null) {
				target = (org.apache.xmlbeans.SimpleValue) get_store().add_attribute_user(UNIT$18);
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
			target = (org.apache.xmlbeans.XmlString) get_store().find_attribute_user(UNIT$18);
			if (target == null) {
				target = (org.apache.xmlbeans.XmlString) get_store().add_attribute_user(UNIT$18);
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
			get_store().remove_attribute(UNIT$18);
		}
	}

	/**
	 * Gets the "upperBound" attribute
	 */
	public int getUpperBound() {
		synchronized (monitor()) {
			check_orphaned();
			org.apache.xmlbeans.SimpleValue target = null;
			target = (org.apache.xmlbeans.SimpleValue) get_store().find_attribute_user(
					UPPERBOUND$20);
			if (target == null) {
				return 0;
			}
			return target.getIntValue();
		}
	}

	/**
	 * Gets (as xml) the "upperBound" attribute
	 */
	public org.apache.xmlbeans.XmlInt xgetUpperBound() {
		synchronized (monitor()) {
			check_orphaned();
			org.apache.xmlbeans.XmlInt target = null;
			target = (org.apache.xmlbeans.XmlInt) get_store().find_attribute_user(UPPERBOUND$20);
			return target;
		}
	}

	/**
	 * True if has "upperBound" attribute
	 */
	public boolean isSetUpperBound() {
		synchronized (monitor()) {
			check_orphaned();
			return get_store().find_attribute_user(UPPERBOUND$20) != null;
		}
	}

	/**
	 * Sets the "upperBound" attribute
	 */
	public void setUpperBound(int upperBound) {
		synchronized (monitor()) {
			check_orphaned();
			org.apache.xmlbeans.SimpleValue target = null;
			target = (org.apache.xmlbeans.SimpleValue) get_store().find_attribute_user(
					UPPERBOUND$20);
			if (target == null) {
				target = (org.apache.xmlbeans.SimpleValue) get_store().add_attribute_user(
						UPPERBOUND$20);
			}
			target.setIntValue(upperBound);
		}
	}

	/**
	 * Sets (as xml) the "upperBound" attribute
	 */
	public void xsetUpperBound(org.apache.xmlbeans.XmlInt upperBound) {
		synchronized (monitor()) {
			check_orphaned();
			org.apache.xmlbeans.XmlInt target = null;
			target = (org.apache.xmlbeans.XmlInt) get_store().find_attribute_user(UPPERBOUND$20);
			if (target == null) {
				target = (org.apache.xmlbeans.XmlInt) get_store().add_attribute_user(UPPERBOUND$20);
			}
			target.set(upperBound);
		}
	}

	/**
	 * Unsets the "upperBound" attribute
	 */
	public void unsetUpperBound() {
		synchronized (monitor()) {
			check_orphaned();
			get_store().remove_attribute(UPPERBOUND$20);
		}
	}

	/**
	 * Gets the "value" attribute
	 */
	public int getValue() {
		synchronized (monitor()) {
			check_orphaned();
			org.apache.xmlbeans.SimpleValue target = null;
			target = (org.apache.xmlbeans.SimpleValue) get_store().find_attribute_user(VALUE$22);
			if (target == null) {
				return 0;
			}
			return target.getIntValue();
		}
	}

	/**
	 * Gets (as xml) the "value" attribute
	 */
	public org.apache.xmlbeans.XmlInt xgetValue() {
		synchronized (monitor()) {
			check_orphaned();
			org.apache.xmlbeans.XmlInt target = null;
			target = (org.apache.xmlbeans.XmlInt) get_store().find_attribute_user(VALUE$22);
			return target;
		}
	}

	/**
	 * True if has "value" attribute
	 */
	public boolean isSetValue() {
		synchronized (monitor()) {
			check_orphaned();
			return get_store().find_attribute_user(VALUE$22) != null;
		}
	}

	/**
	 * Sets the "value" attribute
	 */
	public void setValue(int value) {
		synchronized (monitor()) {
			check_orphaned();
			org.apache.xmlbeans.SimpleValue target = null;
			target = (org.apache.xmlbeans.SimpleValue) get_store().find_attribute_user(VALUE$22);
			if (target == null) {
				target = (org.apache.xmlbeans.SimpleValue) get_store().add_attribute_user(VALUE$22);
			}
			target.setIntValue(value);
		}
	}

	/**
	 * Sets (as xml) the "value" attribute
	 */
	public void xsetValue(org.apache.xmlbeans.XmlInt value) {
		synchronized (monitor()) {
			check_orphaned();
			org.apache.xmlbeans.XmlInt target = null;
			target = (org.apache.xmlbeans.XmlInt) get_store().find_attribute_user(VALUE$22);
			if (target == null) {
				target = (org.apache.xmlbeans.XmlInt) get_store().add_attribute_user(VALUE$22);
			}
			target.set(value);
		}
	}

	/**
	 * Unsets the "value" attribute
	 */
	public void unsetValue() {
		synchronized (monitor()) {
			check_orphaned();
			get_store().remove_attribute(VALUE$22);
		}
	}
}
