package nuna.rami;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

public class ReadUserData {

	private ArrayList<Employee> employees = new ArrayList<Employee>();

	public static void main(String[] args) {
		new ReadUserData().readfile();

	}

	public void readfile() {
		try {
			File myFile = new File("c:\\temp\\myfile.txt");
			BufferedReader in = new BufferedReader(new FileReader(myFile));
			String line;
			while ((line = in.readLine()) != null) {
				String[] employeeArray = line.split(",");
				Employee emp = new Employee(employeeArray);
				employees.add(emp);
			}
			in.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		System.out.print(String.valueOf(employees.size()) + " employees.");

	}
}
