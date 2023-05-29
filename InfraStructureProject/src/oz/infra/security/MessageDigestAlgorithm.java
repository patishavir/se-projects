package oz.infra.security;

public enum MessageDigestAlgorithm {

	MD5("MD5"), SHA1("SHA-1"), SHA256("SHA-256");
	private String algorithmName;

	MessageDigestAlgorithm(final String algorithmName) {
		this.algorithmName = algorithmName;
	}

	public String getAlgorithmName() {
		return algorithmName;
	}
}
