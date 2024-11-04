package com.jcondotta.argument_provider;

import io.micronaut.core.util.StringUtils;
import org.junit.jupiter.api.Named;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.stream.Stream;

public class BlankValuesArgumentProvider implements ArgumentsProvider {

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
        return Stream.of(
                Arguments.of(Named.of("Empty String", StringUtils.EMPTY_STRING)),
                Arguments.of(Named.of("Space String", StringUtils.SPACE)),
                Arguments.of(Named.of("Null Value", (String) null))
        );
    }
}