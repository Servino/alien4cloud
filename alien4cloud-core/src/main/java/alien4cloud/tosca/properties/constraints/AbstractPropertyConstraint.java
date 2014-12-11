package alien4cloud.tosca.properties.constraints;

import alien4cloud.tosca.model.PropertyConstraint;
import alien4cloud.tosca.model.ToscaType;
import alien4cloud.tosca.properties.constraints.exception.ConstraintViolationException;
import alien4cloud.utils.version.ApplicationVersionException;

public abstract class AbstractPropertyConstraint implements PropertyConstraint {

    @Override
    public void validate(ToscaType toscaType, String propertyTextValue) throws ConstraintViolationException {
        try {
            validate(toscaType.convert(propertyTextValue));
        } catch (IllegalArgumentException | ApplicationVersionException e) {
            throw new ConstraintViolationException("String value [" + propertyTextValue + "] is not valid for type [" + toscaType + "]", e);
        }
    }
}