package com.jcondotta.annotation.validator;

import com.jcondotta.argument_provider.SupportedMediaTypesArgumentProvider;
import com.jcondotta.argument_provider.UnsupportedMediaTypesArgumentProvider;
import com.jcondotta.helper.TestFilenameBuilder;
import io.micronaut.http.MediaType;
import io.micronaut.http.multipart.CompletedFileUpload;
import io.micronaut.validation.validator.constraints.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testcontainers.shaded.org.apache.commons.lang3.StringUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ValidatedFileExtensionsValidatorTest {

    private ValidatedFileExtensionsValidator validator;

    @Mock
    private CompletedFileUpload fileUpload;

    @Mock
    private ConstraintValidatorContext context;

    @BeforeEach
    void beforeEach() {
        validator = new ValidatedFileExtensionsValidator();
    }

    @ParameterizedTest
    @ArgumentsSource(SupportedMediaTypesArgumentProvider.class)
    void shouldReturnTrue_whenFileExtensionIsSupported(MediaType supportedMediaType) {
        var filename = TestFilenameBuilder.build(supportedMediaType);
        when(fileUpload.getFilename()).thenReturn(filename);

        assertThat(validator.isValid(fileUpload, null, context)).isTrue();
    }

    @ParameterizedTest
    @ArgumentsSource(UnsupportedMediaTypesArgumentProvider.class)
    void shouldReturnFalse_whenFileExtensionIsUnsupported(MediaType unsupportedMediaType) {
        var filename = TestFilenameBuilder.build(unsupportedMediaType);
        when(fileUpload.getFilename()).thenReturn(filename);

        assertThat(validator.isValid(fileUpload, null, context)).isFalse();
    }

    @Test
    void shouldReturnTrue_whenFileUploadIsNull() {
        assertThat(validator.isValid(null, null, context)).isTrue();
    }

    @Test
    void shouldReturnFalse_whenFilenameIsEmpty() {
        when(fileUpload.getFilename()).thenReturn(StringUtils.EMPTY);

        assertThat(validator.isValid(fileUpload, null, context)).isFalse();
    }

    @Test
    void shouldReturnFalse_whenFilenameIsNull() {
        when(fileUpload.getFilename()).thenReturn(null);

        assertThat(validator.isValid(fileUpload, null, context)).isFalse();
    }

    @ParameterizedTest
    @ArgumentsSource(SupportedMediaTypesArgumentProvider.class)
    void shouldReturnTrue_whenSupportedFileExtensionIsNotLowerCase(MediaType supportedMediaType) {
        var filename = TestFilenameBuilder.build(supportedMediaType).toUpperCase();
        when(fileUpload.getFilename()).thenReturn(filename);

        assertThat(validator.isValid(fileUpload, null, context)).isTrue();
    }
}