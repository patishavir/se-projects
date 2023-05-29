package oz.infra.elastic.data;

import oz.infra.elastic.data.classes.Student;
import oz.infra.json.jackson.JacksonUtils;

public class JacksonEStudentJasonString1 {
	public static void main(String args[]) throws Exception {
		Student student = getStudent();
		// Creating the ObjectMapper object
		String jsonString = JacksonUtils.javaObject2Json(student);
		System.out.println(jsonString);
	}

	private static Student getStudent() {
		Student student = new Student();
		student.setId(001);
		student.setName("Krishna");
		student.setAge(30);
		student.setPhone(9848022338L);
		return student;
	}
}