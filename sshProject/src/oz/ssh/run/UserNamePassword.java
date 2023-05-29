package oz.ssh.run;

class UserNamePassword {
	private String userName = null;
	private String password = null;
	private boolean decryptionRequired = true;

	UserNamePassword(final String userName, final String password, final boolean decryptionRequired) {
		this.userName = userName;
		this.password = password;
		this.decryptionRequired = decryptionRequired;
	}

	public String getPassword() {
		return password;
	}

	public String getUserName() {
		return userName;
	}

	public boolean isDecryptionRequired() {
		return decryptionRequired;
	}

}