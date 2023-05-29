package oz.guigenerator;

public class GUIElement {
	private Object guiComponent;
	private ValidationRuleEnum validationRule;

	public GUIElement(final Object guiComponent, final ValidationRuleEnum validationRule) {
		this.guiComponent = guiComponent;
		this.validationRule = validationRule;
	}

	public final Object getGuiComponent() {
		return guiComponent;
	}

	public final ValidationRuleEnum getValidationRule() {
		return validationRule;
	}
}
