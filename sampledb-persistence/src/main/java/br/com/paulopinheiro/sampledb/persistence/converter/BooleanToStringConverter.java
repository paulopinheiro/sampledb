package br.com.paulopinheiro.sampledb.persistence.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.Optional;

@Converter
public class BooleanToStringConverter implements AttributeConverter<Boolean, String> {

    @Override
    public String convertToDatabaseColumn(Boolean bool) {
        return bool ? "TRUE" : "FALSE";
    }

    @Override
    public Boolean convertToEntityAttribute(String string) {
        if((Optional.ofNullable(string)).isEmpty()) return false;

        return string.equals("TRUE");
    }
    
}
