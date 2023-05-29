package oz.infra.reflection;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import oz.infra.logging.jul.JulUtils;
import oz.infra.map.MapUtils;

public enum TypeEnum {
	STRING("java.lang.String"), LOCALE("java.util.Locale"), INT("int"), BOOLEAN("boolean"), LONG(
			"long"), FLOAT("float"), COLOR("java.awt.Color");
	private static Map<String, TypeEnum> classTypeEnumMap = new HashMap<String, TypeEnum>();
	private String enumClassName;

	TypeEnum(final String classNameParameter) {
		this.enumClassName = classNameParameter;
	}

	private static Logger logger = JulUtils.getLogger();

	static {
		for (TypeEnum typeEnum1 : TypeEnum.values()) {
			classTypeEnumMap.put(typeEnum1.getEnumClassName(), typeEnum1);
		}
		MapUtils.printMap(classTypeEnumMap, Level.FINEST);
	}

	public static TypeEnum getTypeEnum(final String classNameParameter) {
		return classTypeEnumMap.get(classNameParameter);
	}

	public String getEnumClassName() {
		return enumClassName;
	}
}
