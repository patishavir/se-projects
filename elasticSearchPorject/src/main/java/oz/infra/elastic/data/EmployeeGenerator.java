package oz.infra.elastic.data;

import oz.infra.elastic.data.classes.Employee;
import oz.infra.random.RandomNumbersUtils;

public class EmployeeGenerator {
	private static final String[] FIRST_NAMES = { "Dan", "Dan", "Ran", "Noah", "Hanoch", "David", "Omer", "Alon",
			"Boaz", "Guy", "Roy", "Oded", "Moshe", "Dov", "Ilan", "Israel", "Eyal", "Nimrod", "Sara", "Yael", "Tamar",
			"Shira", "Rotem", "Dana", "Tali", "Bela", "Talma", "Gideon", "Gerson", "Noam", "Ami", "Avihoo", "Rafi" };
	private static final String[] LAST_NAMES = { "Rosen", "Segal", "Peretz", "Dayan", "Lampel", "Pnooeli", "Sharvit",
			"Berman", "Kisos", "Shemesh", "Zipori", "Cohen", "Levi", "Gaon", "Kasher", "Ben Yosef", "Dagan", "Tirosh",
			"Peer", "Hadar", "Shefa", "Danieli", "Shirazi", "Baron", "Bar", "Sela", "Banayi", "Barzel", "Barilay" };
	private static final String[] DEPARTMENTS = { "R&D", "Operations", "Accounting", "Human resources", "Security",
			"Data security", "System", "Marketing", "Sales", "Credit" };
	private static final String[] MARITAL_STATUS = { "Bachelor", "Spinster", "Engaged", "Married", "Divorced", "Widow",
			"Widower", "Seperated" };

	public static Employee getEmployee(final int id) {
		Employee employee = new Employee();
		employee.setId(id);
		employee.setFirstName(FIRST_NAMES[RandomNumbersUtils.getRandomIntLessThan(FIRST_NAMES.length)]);
		employee.setLastName(LAST_NAMES[RandomNumbersUtils.getRandomIntLessThan(LAST_NAMES.length)]);
		employee.setAge(RandomNumbersUtils.getRandomIntWithinRange(20, 70));
		employee.setSalary(RandomNumbersUtils.getRandomIntWithinRange(30000, 120000));
		employee.setPhoneNumber(RandomNumbersUtils.getRandomIntWithinRange(052000000, 0525555555));
		employee.setDepartment(DEPARTMENTS[RandomNumbersUtils.getRandomIntLessThan(DEPARTMENTS.length)]);
		employee.setMaritalStatus(MARITAL_STATUS[RandomNumbersUtils.getRandomIntLessThan(MARITAL_STATUS.length)]);
		return employee;
	}
}