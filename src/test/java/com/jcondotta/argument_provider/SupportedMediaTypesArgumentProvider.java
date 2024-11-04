package com.jcondotta.argument_provider;

import io.micronaut.http.MediaType;
import org.junit.jupiter.api.Named;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.stream.Stream;

public class SupportedMediaTypesArgumentProvider implements ArgumentsProvider {

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
        return Stream.of(
                Arguments.of(Named.of("PNG", MediaType.IMAGE_PNG_TYPE)),
                Arguments.of(Named.of("JPEG", MediaType.IMAGE_JPEG_TYPE))
        );
    }
}
