package ru.mts.media.platform.umc.domain.scalar;

import com.netflix.graphql.dgs.DgsScalar;
import graphql.language.StringValue;
import graphql.schema.Coercing;
import graphql.schema.CoercingParseLiteralException;
import graphql.schema.CoercingParseValueException;
import graphql.schema.CoercingSerializeException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@DgsScalar(name = "LocalDateTime")
public class LocalDateTimeScalar implements Coercing<LocalDateTime, String> {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    @Override
    public String serialize(Object dataFetcherResult) throws CoercingSerializeException {
        if (dataFetcherResult instanceof LocalDateTime ldt) {
            return ldt.format(FORMATTER);
        }
        throw new CoercingSerializeException(
                "Invalid value for LocalDateTime serialization: expected LocalDateTime, got " + dataFetcherResult.getClass().getSimpleName());
    }

    @Override
    public LocalDateTime parseValue(Object input) throws CoercingParseValueException {
        try {
            return LocalDateTime.parse(input.toString());
        } catch (DateTimeParseException e) {
            throw new CoercingParseValueException("Invalid LocalDateTime input value: " + input + ". Expected format: yyyy-MM-dd'T'HH:mm:ss");
        }
    }

    @Override
    public LocalDateTime parseLiteral(Object input) throws CoercingParseLiteralException {
        if (input instanceof StringValue stringValue) {
            try {
                return LocalDateTime.parse(stringValue.getValue());
            } catch (DateTimeParseException e) {
                throw new CoercingParseLiteralException(
                        "Invalid LocalDateTime literal: " + stringValue.getValue() + ". Expected format: yyyy-MM-dd'T'HH:mm:ss");
            }
        }
        throw new CoercingParseLiteralException("Expected AST type 'StringValue' for LocalDateTime scalar, got: " + input.getClass().getSimpleName());
    }
}
