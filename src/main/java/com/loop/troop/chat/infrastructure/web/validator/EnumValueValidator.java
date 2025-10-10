package com.loop.troop.chat.infrastructure.web.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EnumValueValidator implements ConstraintValidator<EnumValue, String> {

    private String[] acceptedValues;

    @Override
    public void initialize(EnumValue annotation) {
        acceptedValues = new String[annotation.enumClass().getEnumConstants().length];
        int i = 0;
        for (Enum<?> e : annotation.enumClass().getEnumConstants()) {
            acceptedValues[i++] = e.name();
        }
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) return false;
        for (String s : acceptedValues) {
            if (s.equalsIgnoreCase(value)) return true;
        }
        return false;
    }
}
