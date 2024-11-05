package com.jcondotta.argument_provider;

import io.micronaut.http.MediaType;
import org.junit.jupiter.api.Named;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.stream.Stream;

public class UnsupportedMediaTypesArgumentProvider implements ArgumentsProvider {

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
        return Stream.of(
                Arguments.of(Named.of("GIF", MediaType.IMAGE_GIF_TYPE)),
                Arguments.of(Named.of("PDF", MediaType.APPLICATION_PDF_TYPE)),
                Arguments.of(Named.of("EXCEL", MediaType.MICROSOFT_EXCEL_TYPE)),
                Arguments.of(Named.of("JSON", MediaType.APPLICATION_JSON_TYPE))
        );
    }
}
