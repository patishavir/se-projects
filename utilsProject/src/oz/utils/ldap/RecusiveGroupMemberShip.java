package oz.utils.ldap;

import java.util.Properties;
import java.util.logging.Logger;

import oz.infra.ldap.LdapHelperUtils;
import oz.infra.logging.jul.JulUtils;
import oz.infra.properties.PropertiesUtils;

public class RecusiveGroupMemberShip {
	private static Logger logger = JulUtils.getLogger();

	public static void main(final String[] args) {
		// TODO Auto-generated method stub
		String propertiesFilePath = args[0];
		Properties properties = PropertiesUtils.loadPropertiesFile(propertiesFilePath);
		StringBuilder sb = LdapHelperUtils.getRecursiveGroupMembershipByTokenGroup(properties);
		logger.info("\n"+ sb.toString());
	}

}
