package br.com.paulopinheiro.sampledb.persistence.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true) // autoApply guarantees this runs automatically for all Boolean fields with @Convert
public class BooleanToStringConverter implements AttributeConverter<Boolean, String> {

    @Override
    public String convertToDatabaseColumn(Boolean bool) {
        // Safe null handling: if the entity property is null, maps to "FALSE" or returns null depending on DB constraints
        if (bool == null) {
            return "FALSE";
        }
        return bool ? "TRUE" : "FALSE";
    }

    @Override
    public Boolean convertToEntityAttribute(String string) {
        // High-performance native null check instead of Optional alocation
        if (string == null || string.isEmpty()) {
            return Boolean.FALSE;
        }

        // Using standard case-insensitive comparison for higher reliability
        return "TRUE".equalsIgnoreCase(string.trim());
    }
}