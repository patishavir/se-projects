/*
 * XML Type:  StatType
 * Namespace: 
 * Java type: noNamespace.StatType
 *
 * Automatically generated - do not modify.
 */
package noNamespace.impl;

/**
 * An XML StatType(@).
 * 
 * This is a complex type.
 */
public class StatTypeImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements
		noNamespace.StatType {
	private static final long serialVersionUID = 1L;

	public StatTypeImpl(org.apache.xmlbeans.SchemaType sType) {
		super(sType);
	}

	private static final javax.xml.namespace.QName STAT$0 = new javax.xml.namespace.QName("",
			"Stat");
	private static final javax.xml.namespace.QName BOUNDEDRANGESTATISTIC$2 = new javax.xml.namespace.QName(
			"", "BoundedRangeStatistic");
	private static final javax.xml.namespace.QName COUNTSTATISTIC$4 = new javax.xml.namespace.QName(
			"", "CountStatistic");
	private static final javax.xml.namespace.QName RANGESTATISTIC$6 = new javax.xml.namespace.QName(
			"", "RangeStatistic");
	private static final javax.xml.namespace.QName TIMESTATISTIC$8 = new javax.xml.namespace.QName(
			"", "TimeStatistic");
	private static final javax.xml.namespace.QName NAME$10 = new javax.xml.namespace.QName("",
			"name");

	/**
	 * Gets a List of "Stat" elements
	 */
	public java.util.List<noNamespace.StatType> getStatList() {
		final class StatList extends java.util.AbstractList<noNamespace.StatType> {
			@Override
			public noNamespace.StatType get(int i) {
				return StatTypeImpl.this.getStatArray(i);
			}

			@Override
			public noNamespace.StatType set(int i, noNamespace.StatType o) {
				noNamespace.StatType old = StatTypeImpl.this.getStatArray(i);
				StatTypeImpl.this.setStatArray(i, o);
				return old;
			}

			@Override
			public void add(int i, noNamespace.StatType o) {
				StatTypeImpl.this.insertNewStat(i).set(o);
			}

			@Override
			public noNamespace.StatType remove(int i) {
				noNamespace.StatType old = StatTypeImpl.this.getStatArray(i);
				StatTypeImpl.this.removeStat(i);
				return old;
			}

			@Override
			public int size() {
				return StatTypeImpl.this.sizeOfStatArray();
			}

		}

		synchronized (monitor()) {
			check_orphaned();
			return new StatList();
		}
	}

	/**
	 * Gets array of all "Stat" elements
	 * 
	 * @deprecated
	 */
	@Deprecated
	public noNamespace.StatType[] getStatArray() {
		synchronized (monitor()) {
			check_orphaned();
			java.util.List<noNamespace.StatType> targetList = new java.util.ArrayList<noNamespace.StatType>();
			get_store().find_all_element_users(STAT$0, targetList);
			noNamespace.StatType[] result = new noNamespace.StatType[targetList.size()];
			targetList.toArray(result);
			return result;
		}
	}

	/**
	 * Gets ith "Stat" element
	 */
	public noNamespace.StatType getStatArray(int i) {
		synchronized (monitor()) {
			check_orphaned();
			noNamespace.StatType target = null;
			target = (noNamespace.StatType) get_store().find_element_user(STAT$0, i);
			if (target == null) {
				throw new IndexOutOfBoundsException();
			}
			return target;
		}
	}

	/**
	 * Returns number of "Stat" element
	 */
	public int sizeOfStatArray() {
		synchronized (monitor()) {
			check_orphaned();
			return get_store().count_elements(STAT$0);
		}
	}

	/**
	 * Sets array of all "Stat" element WARNING: This method is not atomicaly
	 * synchronized.
	 */
	public void setStatArray(noNamespace.StatType[] statArray) {
		check_orphaned();
		arraySetterHelper(statArray, STAT$0);
	}

	/**
	 * Sets ith "Stat" element
	 */
	public void setStatArray(int i, noNamespace.StatType stat) {
		synchronized (monitor()) {
			check_orphaned();
			noNamespace.StatType target = null;
			target = (noNamespace.StatType) get_store().find_element_user(STAT$0, i);
			if (target == null) {
				throw new IndexOutOfBoundsException();
			}
			target.set(stat);
		}
	}

	/**
	 * Inserts and returns a new empty value (as xml) as the ith "Stat" element
	 */
	public noNamespace.StatType insertNewStat(int i) {
		synchronized (monitor()) {
			check_orphaned();
			noNamespace.StatType target = null;
			target = (noNamespace.StatType) get_store().insert_element_user(STAT$0, i);
			return target;
		}
	}

	/**
	 * Appends and returns a new empty value (as xml) as the last "Stat" element
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
	 * Removes the ith "Stat" element
	 */
	public void removeStat(int i) {
		synchronized (monitor()) {
			check_orphaned();
			get_store().remove_element(STAT$0, i);
		}
	}

	/**
	 * Gets a List of "BoundedRangeStatistic" elements
	 */
	public java.util.List<noNamespace.BoundedRangeStatisticType> getBoundedRangeStatisticList() {
		final class BoundedRangeStatisticList extends
				java.util.AbstractList<noNamespace.BoundedRangeStatisticType> {
			@Override
			public noNamespace.BoundedRangeStatisticType get(int i) {
				return StatTypeImpl.this.getBoundedRangeStatisticArray(i);
			}

			@Override
			public noNamespace.BoundedRangeStatisticType set(int i,
					noNamespace.BoundedRangeStatisticType o) {
				noNamespace.BoundedRangeStatisticType old = StatTypeImpl.this
						.getBoundedRangeStatisticArray(i);
				StatTypeImpl.this.setBoundedRangeStatisticArray(i, o);
				return old;
			}

			@Override
			public void add(int i, noNamespace.BoundedRangeStatisticType o) {
				StatTypeImpl.this.insertNewBoundedRangeStatistic(i).set(o);
			}

			@Override
			public noNamespace.BoundedRangeStatisticType remove(int i) {
				noNamespace.BoundedRangeStatisticType old = StatTypeImpl.this
						.getBoundedRangeStatisticArray(i);
				StatTypeImpl.this.removeBoundedRangeStatistic(i);
				return old;
			}

			@Override
			public int size() {
				return StatTypeImpl.this.sizeOfBoundedRangeStatisticArray();
			}

		}

		synchronized (monitor()) {
			check_orphaned();
			return new BoundedRangeStatisticList();
		}
	}

	/**
	 * Gets array of all "BoundedRangeStatistic" elements
	 * 
	 * @deprecated
	 */
	@Deprecated
	public noNamespace.BoundedRangeStatisticType[] getBoundedRangeStatisticArray() {
		synchronized (monitor()) {
			check_orphaned();
			java.util.List<noNamespace.BoundedRangeStatisticType> targetList = new java.util.ArrayList<noNamespace.BoundedRangeStatisticType>();
			get_store().find_all_element_users(BOUNDEDRANGESTATISTIC$2, targetList);
			noNamespace.BoundedRangeStatisticType[] result = new noNamespace.BoundedRangeStatisticType[targetList
					.size()];
			targetList.toArray(result);
			return result;
		}
	}

	/**
	 * Gets ith "BoundedRangeStatistic" element
	 */
	public noNamespace.BoundedRangeStatisticType getBoundedRangeStatisticArray(int i) {
		synchronized (monitor()) {
			check_orphaned();
			noNamespace.BoundedRangeStatisticType target = null;
			target = (noNamespace.BoundedRangeStatisticType) get_store().find_element_user(
					BOUNDEDRANGESTATISTIC$2, i);
			if (target == null) {
				throw new IndexOutOfBoundsException();
			}
			return target;
		}
	}

	/**
	 * Returns number of "BoundedRangeStatistic" element
	 */
	public int sizeOfBoundedRangeStatisticArray() {
		synchronized (monitor()) {
			check_orphaned();
			return get_store().count_elements(BOUNDEDRANGESTATISTIC$2);
		}
	}

	/**
	 * Sets array of all "BoundedRangeStatistic" element WARNING: This method is
	 * not atomicaly synchronized.
	 */
	public void setBoundedRangeStatisticArray(
			noNamespace.BoundedRangeStatisticType[] boundedRangeStatisticArray) {
		check_orphaned();
		arraySetterHelper(boundedRangeStatisticArray, BOUNDEDRANGESTATISTIC$2);
	}

	/**
	 * Sets ith "BoundedRangeStatistic" element
	 */
	public void setBoundedRangeStatisticArray(int i,
			noNamespace.BoundedRangeStatisticType boundedRangeStatistic) {
		synchronized (monitor()) {
			check_orphaned();
			noNamespace.BoundedRangeStatisticType target = null;
			target = (noNamespace.BoundedRangeStatisticType) get_store().find_element_user(
					BOUNDEDRANGESTATISTIC$2, i);
			if (target == null) {
				throw new IndexOutOfBoundsException();
			}
			target.set(boundedRangeStatistic);
		}
	}

	/**
	 * Inserts and returns a new empty value (as xml) as the ith
	 * "BoundedRangeStatistic" element
	 */
	public noNamespace.BoundedRangeStatisticType insertNewBoundedRangeStatistic(int i) {
		synchronized (monitor()) {
			check_orphaned();
			noNamespace.BoundedRangeStatisticType target = null;
			target = (noNamespace.BoundedRangeStatisticType) get_store().insert_element_user(
					BOUNDEDRANGESTATISTIC$2, i);
			return target;
		}
	}

	/**
	 * Appends and returns a new empty value (as xml) as the last
	 * "BoundedRangeStatistic" element
	 */
	public noNamespace.BoundedRangeStatisticType addNewBoundedRangeStatistic() {
		synchronized (monitor()) {
			check_orphaned();
			noNamespace.BoundedRangeStatisticType target = null;
			target = (noNamespace.BoundedRangeStatisticType) get_store().add_element_user(
					BOUNDEDRANGESTATISTIC$2);
			return target;
		}
	}

	/**
	 * Removes the ith "BoundedRangeStatistic" element
	 */
	public void removeBoundedRangeStatistic(int i) {
		synchronized (monitor()) {
			check_orphaned();
			get_store().remove_element(BOUNDEDRANGESTATISTIC$2, i);
		}
	}

	/**
	 * Gets a List of "CountStatistic" elements
	 */
	public java.util.List<noNamespace.CountStatisticType> getCountStatisticList() {
		final class CountStatisticList extends
				java.util.AbstractList<noNamespace.CountStatisticType> {
			@Override
			public noNamespace.CountStatisticType get(int i) {
				return StatTypeImpl.this.getCountStatisticArray(i);
			}

			@Override
			public noNamespace.CountStatisticType set(int i, noNamespace.CountStatisticType o) {
				noNamespace.CountStatisticType old = StatTypeImpl.this.getCountStatisticArray(i);
				StatTypeImpl.this.setCountStatisticArray(i, o);
				return old;
			}

			@Override
			public void add(int i, noNamespace.CountStatisticType o) {
				StatTypeImpl.this.insertNewCountStatistic(i).set(o);
			}

			@Override
			public noNamespace.CountStatisticType remove(int i) {
				noNamespace.CountStatisticType old = StatTypeImpl.this.getCountStatisticArray(i);
				StatTypeImpl.this.removeCountStatistic(i);
				return old;
			}

			@Override
			public int size() {
				return StatTypeImpl.this.sizeOfCountStatisticArray();
			}

		}

		synchronized (monitor()) {
			check_orphaned();
			return new CountStatisticList();
		}
	}

	/**
	 * Gets array of all "CountStatistic" elements
	 * 
	 * @deprecated
	 */
	@Deprecated
	public noNamespace.CountStatisticType[] getCountStatisticArray() {
		synchronized (monitor()) {
			check_orphaned();
			java.util.List<noNamespace.CountStatisticType> targetList = new java.util.ArrayList<noNamespace.CountStatisticType>();
			get_store().find_all_element_users(COUNTSTATISTIC$4, targetList);
			noNamespace.CountStatisticType[] result = new noNamespace.CountStatisticType[targetList
					.size()];
			targetList.toArray(result);
			return result;
		}
	}

	/**
	 * Gets ith "CountStatistic" element
	 */
	public noNamespace.CountStatisticType getCountStatisticArray(int i) {
		synchronized (monitor()) {
			check_orphaned();
			noNamespace.CountStatisticType target = null;
			target = (noNamespace.CountStatisticType) get_store().find_element_user(
					COUNTSTATISTIC$4, i);
			if (target == null) {
				throw new IndexOutOfBoundsException();
			}
			return target;
		}
	}

	/**
	 * Returns number of "CountStatistic" element
	 */
	public int sizeOfCountStatisticArray() {
		synchronized (monitor()) {
			check_orphaned();
			return get_store().count_elements(COUNTSTATISTIC$4);
		}
	}

	/**
	 * Sets array of all "CountStatistic" element WARNING: This method is not
	 * atomicaly synchronized.
	 */
	public void setCountStatisticArray(noNamespace.CountStatisticType[] countStatisticArray) {
		check_orphaned();
		arraySetterHelper(countStatisticArray, COUNTSTATISTIC$4);
	}

	/**
	 * Sets ith "CountStatistic" element
	 */
	public void setCountStatisticArray(int i, noNamespace.CountStatisticType countStatistic) {
		synchronized (monitor()) {
			check_orphaned();
			noNamespace.CountStatisticType target = null;
			target = (noNamespace.CountStatisticType) get_store().find_element_user(
					COUNTSTATISTIC$4, i);
			if (target == null) {
				throw new IndexOutOfBoundsException();
			}
			target.set(countStatistic);
		}
	}

	/**
	 * Inserts and returns a new empty value (as xml) as the ith
	 * "CountStatistic" element
	 */
	public noNamespace.CountStatisticType insertNewCountStatistic(int i) {
		synchronized (monitor()) {
			check_orphaned();
			noNamespace.CountStatisticType target = null;
			target = (noNamespace.CountStatisticType) get_store().insert_element_user(
					COUNTSTATISTIC$4, i);
			return target;
		}
	}

	/**
	 * Appends and returns a new empty value (as xml) as the last
	 * "CountStatistic" element
	 */
	public noNamespace.CountStatisticType addNewCountStatistic() {
		synchronized (monitor()) {
			check_orphaned();
			noNamespace.CountStatisticType target = null;
			target = (noNamespace.CountStatisticType) get_store()
					.add_element_user(COUNTSTATISTIC$4);
			return target;
		}
	}

	/**
	 * Removes the ith "CountStatistic" element
	 */
	public void removeCountStatistic(int i) {
		synchronized (monitor()) {
			check_orphaned();
			get_store().remove_element(COUNTSTATISTIC$4, i);
		}
	}

	/**
	 * Gets a List of "RangeStatistic" elements
	 */
	public java.util.List<noNamespace.RangeStatisticType> getRangeStatisticList() {
		final class RangeStatisticList extends
				java.util.AbstractList<noNamespace.RangeStatisticType> {
			@Override
			public noNamespace.RangeStatisticType get(int i) {
				return StatTypeImpl.this.getRangeStatisticArray(i);
			}

			@Override
			public noNamespace.RangeStatisticType set(int i, noNamespace.RangeStatisticType o) {
				noNamespace.RangeStatisticType old = StatTypeImpl.this.getRangeStatisticArray(i);
				StatTypeImpl.this.setRangeStatisticArray(i, o);
				return old;
			}

			@Override
			public void add(int i, noNamespace.RangeStatisticType o) {
				StatTypeImpl.this.insertNewRangeStatistic(i).set(o);
			}

			@Override
			public noNamespace.RangeStatisticType remove(int i) {
				noNamespace.RangeStatisticType old = StatTypeImpl.this.getRangeStatisticArray(i);
				StatTypeImpl.this.removeRangeStatistic(i);
				return old;
			}

			@Override
			public int size() {
				return StatTypeImpl.this.sizeOfRangeStatisticArray();
			}

		}

		synchronized (monitor()) {
			check_orphaned();
			return new RangeStatisticList();
		}
	}

	/**
	 * Gets array of all "RangeStatistic" elements
	 * 
	 * @deprecated
	 */
	@Deprecated
	public noNamespace.RangeStatisticType[] getRangeStatisticArray() {
		synchronized (monitor()) {
			check_orphaned();
			java.util.List<noNamespace.RangeStatisticType> targetList = new java.util.ArrayList<noNamespace.RangeStatisticType>();
			get_store().find_all_element_users(RANGESTATISTIC$6, targetList);
			noNamespace.RangeStatisticType[] result = new noNamespace.RangeStatisticType[targetList
					.size()];
			targetList.toArray(result);
			return result;
		}
	}

	/**
	 * Gets ith "RangeStatistic" element
	 */
	public noNamespace.RangeStatisticType getRangeStatisticArray(int i) {
		synchronized (monitor()) {
			check_orphaned();
			noNamespace.RangeStatisticType target = null;
			target = (noNamespace.RangeStatisticType) get_store().find_element_user(
					RANGESTATISTIC$6, i);
			if (target == null) {
				throw new IndexOutOfBoundsException();
			}
			return target;
		}
	}

	/**
	 * Returns number of "RangeStatistic" element
	 */
	public int sizeOfRangeStatisticArray() {
		synchronized (monitor()) {
			check_orphaned();
			return get_store().count_elements(RANGESTATISTIC$6);
		}
	}

	/**
	 * Sets array of all "RangeStatistic" element WARNING: This method is not
	 * atomicaly synchronized.
	 */
	public void setRangeStatisticArray(noNamespace.RangeStatisticType[] rangeStatisticArray) {
		check_orphaned();
		arraySetterHelper(rangeStatisticArray, RANGESTATISTIC$6);
	}

	/**
	 * Sets ith "RangeStatistic" element
	 */
	public void setRangeStatisticArray(int i, noNamespace.RangeStatisticType rangeStatistic) {
		synchronized (monitor()) {
			check_orphaned();
			noNamespace.RangeStatisticType target = null;
			target = (noNamespace.RangeStatisticType) get_store().find_element_user(
					RANGESTATISTIC$6, i);
			if (target == null) {
				throw new IndexOutOfBoundsException();
			}
			target.set(rangeStatistic);
		}
	}

	/**
	 * Inserts and returns a new empty value (as xml) as the ith
	 * "RangeStatistic" element
	 */
	public noNamespace.RangeStatisticType insertNewRangeStatistic(int i) {
		synchronized (monitor()) {
			check_orphaned();
			noNamespace.RangeStatisticType target = null;
			target = (noNamespace.RangeStatisticType) get_store().insert_element_user(
					RANGESTATISTIC$6, i);
			return target;
		}
	}

	/**
	 * Appends and returns a new empty value (as xml) as the last
	 * "RangeStatistic" element
	 */
	public noNamespace.RangeStatisticType addNewRangeStatistic() {
		synchronized (monitor()) {
			check_orphaned();
			noNamespace.RangeStatisticType target = null;
			target = (noNamespace.RangeStatisticType) get_store()
					.add_element_user(RANGESTATISTIC$6);
			return target;
		}
	}

	/**
	 * Removes the ith "RangeStatistic" element
	 */
	public void removeRangeStatistic(int i) {
		synchronized (monitor()) {
			check_orphaned();
			get_store().remove_element(RANGESTATISTIC$6, i);
		}
	}

	/**
	 * Gets a List of "TimeStatistic" elements
	 */
	public java.util.List<noNamespace.TimeStatisticType> getTimeStatisticList() {
		final class TimeStatisticList extends java.util.AbstractList<noNamespace.TimeStatisticType> {
			@Override
			public noNamespace.TimeStatisticType get(int i) {
				return StatTypeImpl.this.getTimeStatisticArray(i);
			}

			@Override
			public noNamespace.TimeStatisticType set(int i, noNamespace.TimeStatisticType o) {
				noNamespace.TimeStatisticType old = StatTypeImpl.this.getTimeStatisticArray(i);
				StatTypeImpl.this.setTimeStatisticArray(i, o);
				return old;
			}

			@Override
			public void add(int i, noNamespace.TimeStatisticType o) {
				StatTypeImpl.this.insertNewTimeStatistic(i).set(o);
			}

			@Override
			public noNamespace.TimeStatisticType remove(int i) {
				noNamespace.TimeStatisticType old = StatTypeImpl.this.getTimeStatisticArray(i);
				StatTypeImpl.this.removeTimeStatistic(i);
				return old;
			}

			@Override
			public int size() {
				return StatTypeImpl.this.sizeOfTimeStatisticArray();
			}

		}

		synchronized (monitor()) {
			check_orphaned();
			return new TimeStatisticList();
		}
	}

	/**
	 * Gets array of all "TimeStatistic" elements
	 * 
	 * @deprecated
	 */
	@Deprecated
	public noNamespace.TimeStatisticType[] getTimeStatisticArray() {
		synchronized (monitor()) {
			check_orphaned();
			java.util.List<noNamespace.TimeStatisticType> targetList = new java.util.ArrayList<noNamespace.TimeStatisticType>();
			get_store().find_all_element_users(TIMESTATISTIC$8, targetList);
			noNamespace.TimeStatisticType[] result = new noNamespace.TimeStatisticType[targetList
					.size()];
			targetList.toArray(result);
			return result;
		}
	}

	/**
	 * Gets ith "TimeStatistic" element
	 */
	public noNamespace.TimeStatisticType getTimeStatisticArray(int i) {
		synchronized (monitor()) {
			check_orphaned();
			noNamespace.TimeStatisticType target = null;
			target = (noNamespace.TimeStatisticType) get_store().find_element_user(TIMESTATISTIC$8,
					i);
			if (target == null) {
				throw new IndexOutOfBoundsException();
			}
			return target;
		}
	}

	/**
	 * Returns number of "TimeStatistic" element
	 */
	public int sizeOfTimeStatisticArray() {
		synchronized (monitor()) {
			check_orphaned();
			return get_store().count_elements(TIMESTATISTIC$8);
		}
	}

	/**
	 * Sets array of all "TimeStatistic" element WARNING: This method is not
	 * atomicaly synchronized.
	 */
	public void setTimeStatisticArray(noNamespace.TimeStatisticType[] timeStatisticArray) {
		check_orphaned();
		arraySetterHelper(timeStatisticArray, TIMESTATISTIC$8);
	}

	/**
	 * Sets ith "TimeStatistic" element
	 */
	public void setTimeStatisticArray(int i, noNamespace.TimeStatisticType timeStatistic) {
		synchronized (monitor()) {
			check_orphaned();
			noNamespace.TimeStatisticType target = null;
			target = (noNamespace.TimeStatisticType) get_store().find_element_user(TIMESTATISTIC$8,
					i);
			if (target == null) {
				throw new IndexOutOfBoundsException();
			}
			target.set(timeStatistic);
		}
	}

	/**
	 * Inserts and returns a new empty value (as xml) as the ith "TimeStatistic"
	 * element
	 */
	public noNamespace.TimeStatisticType insertNewTimeStatistic(int i) {
		synchronized (monitor()) {
			check_orphaned();
			noNamespace.TimeStatisticType target = null;
			target = (noNamespace.TimeStatisticType) get_store().insert_element_user(
					TIMESTATISTIC$8, i);
			return target;
		}
	}

	/**
	 * Appends and returns a new empty value (as xml) as the last
	 * "TimeStatistic" element
	 */
	public noNamespace.TimeStatisticType addNewTimeStatistic() {
		synchronized (monitor()) {
			check_orphaned();
			noNamespace.TimeStatisticType target = null;
			target = (noNamespace.TimeStatisticType) get_store().add_element_user(TIMESTATISTIC$8);
			return target;
		}
	}

	/**
	 * Removes the ith "TimeStatistic" element
	 */
	public void removeTimeStatistic(int i) {
		synchronized (monitor()) {
			check_orphaned();
			get_store().remove_element(TIMESTATISTIC$8, i);
		}
	}

	/**
	 * Gets the "name" attribute
	 */
	public java.lang.String getName() {
		synchronized (monitor()) {
			check_orphaned();
			org.apache.xmlbeans.SimpleValue target = null;
			target = (org.apache.xmlbeans.SimpleValue) get_store().find_attribute_user(NAME$10);
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
			target = (org.apache.xmlbeans.XmlString) get_store().find_attribute_user(NAME$10);
			return target;
		}
	}

	/**
	 * True if has "name" attribute
	 */
	public boolean isSetName() {
		synchronized (monitor()) {
			check_orphaned();
			return get_store().find_attribute_user(NAME$10) != null;
		}
	}

	/**
	 * Sets the "name" attribute
	 */
	public void setName(java.lang.String name) {
		synchronized (monitor()) {
			check_orphaned();
			org.apache.xmlbeans.SimpleValue target = null;
			target = (org.apache.xmlbeans.SimpleValue) get_store().find_attribute_user(NAME$10);
			if (target == null) {
				target = (org.apache.xmlbeans.SimpleValue) get_store().add_attribute_user(NAME$10);
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
			target = (org.apache.xmlbeans.XmlString) get_store().find_attribute_user(NAME$10);
			if (target == null) {
				target = (org.apache.xmlbeans.XmlString) get_store().add_attribute_user(NAME$10);
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
			get_store().remove_attribute(NAME$10);
		}
	}
}
