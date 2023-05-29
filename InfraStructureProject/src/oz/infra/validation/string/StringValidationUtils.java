package oz.infra.validation.string;

import oz.infra.constants.OzConstants;
import oz.infra.string.StringUtils;

public class StringValidationUtils {
	public static boolean validateString(final String sourceString, final String checkString,
			final StringValidationCriteria stringValidationCriterion) {
		boolean returnCode = false;
		switch (stringValidationCriterion) {
		case STRING_EQUAL:
			returnCode = sourceString.equals(checkString);
			break;
		case STRING_CONTAINS:
			returnCode = sourceString.indexOf(checkString) > OzConstants.STRING_NOT_FOUND;
			break;
		case STRING_STARTS_WITH:
			returnCode = sourceString.startsWith(checkString);
			break;
		case STRING_ENDS_WITH:
			returnCode = sourceString.endsWith(checkString);
			break;
		case INT_EQUAL:
			returnCode = (Integer.parseInt(sourceString) == Integer.parseInt(checkString));
			break;
		case INT_GREATER_THAN:
			returnCode = (Integer.parseInt(sourceString) > Integer.parseInt(checkString));
			break;
		}
		return returnCode;
	}

	public static boolean validateString(final String sourceString, final String checkString, final int checkCount,
			final StringValidationCriteria stringValidationCriterion) {
		int actualCount = StringUtils.getNumberOfOccurrences(sourceString, checkString);
		return (actualCount == checkCount);
	}
}