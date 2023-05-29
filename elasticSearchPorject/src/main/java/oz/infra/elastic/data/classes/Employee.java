package oz.infra.elastic.data.classes;

public class Employee {
	private int id;
	private String firstName = null;
	private String lastName = null;
	private int age;
	private int salary;
	private long phoneNumber;
	private String department;
	private String maritalStatus;

	public int getAge() {
		return age;
	}

	public String getDepartment() {
		return department;
	}

	public String getFirstName() {
		return firstName;
	}

	public int getId() {
		return id;
	}

	public String getLastName() {
		return lastName;
	}

	public String getMaritalStatus() {
		return maritalStatus;
	}

	public long getPhoneNumber() {
		return phoneNumber;
	}

	public int getSalary() {
		return salary;
	}

	public void setAge(final int age) {
		this.age = age;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public void setFirstName(final String firstName) {
		this.firstName = firstName;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setLastName(final String lastName) {
		this.lastName = lastName;
	}

	public void setMaritalStatus(String maritalStatus) {
		this.maritalStatus = maritalStatus;
	}

	public void setPhoneNumber(final long phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public void setSalary(final int salary) {
		this.salary = salary;
	}

}
