package oz.infra.json.jackson.test;

import oz.infra.json.jackson.JacksonUtils;

public class JacksonUtilsTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		testJjavaObject2Json();
	}

	private static void testJjavaObject2Json() {
		Student std = new Student();
		std.setId(001);
		std.setName("Krishna ho ha");
		std.setAge(30);
		std.setPhone(9848022338L);
		// Creating the ObjectMapper object
		// ObjectMapper mapper = new ObjectMapper();
		// Converting the Object to JSONString
		// String jsonString = mapper.writeValueAsString(std);
		System.out.println(JacksonUtils.javaObject2Json(std));
	}
}

class Student {
	private int id;
	private String name;
	private int age;
	private long phone;

	public int getId() {
		return id;
	}

	public void setId(final int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(final int age) {
		this.age = age;
	}

	public long getPhone() {
		return phone;
	}

	public void setPhone(final long phone) {
		this.phone = phone;
	}
}