package nuna.rami;

public class Employee {

	private String clientnum;
	private String clientname;
	private String snif;
	private String account;

	public Employee() {
	}

	public Employee(final String clientnum, final String clientname, final String snif, final  String account) {
		this.clientnum = clientnum;
		this.clientname = clientname;
		this.snif = snif;
		this.account = account;
	}

	public Employee(final String[] employeeArray) {
		this.clientnum = employeeArray[0];
		this.clientname = employeeArray[1];
		this.snif = employeeArray[2];
		this.account = employeeArray[3];
		System.out.println(clientnum + ", " + clientname + ", " + snif + ", " + account);
	}

	public String getAccount() {
		return account;
	}

	public String getClientname() {
		return clientname;
	}

	public String getClientnum() {
		return clientnum;
	}

	public String getSnif() {
		return this.snif;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public void setClientname(String clientname) {
		this.clientname = clientname;
	}

	public void setClientnum(String clientnum) {
		this.clientnum = clientnum;
	}

	public void setSnif(String snif) {
		this.snif = snif;
	}

}
