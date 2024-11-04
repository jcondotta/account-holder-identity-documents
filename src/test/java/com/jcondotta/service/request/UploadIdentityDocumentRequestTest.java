package com.jcondotta.service.request;

import com.jcondotta.argument_provider.SupportedMediaTypesArgumentProvider;
import com.jcondotta.factory.ValidatorTestFactory;
import com.jcondotta.helper.TestAccountHolder;
import io.micronaut.http.MediaType;
import io.micronaut.http.multipart.CompletedFileUpload;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UploadIdentityDocumentRequestTest {

    private static final UUID ACCOUNT_HOLDER_ID_JEFFERSON = TestAccountHolder.JEFFERSON.getAccountHolderId();
    private static final Validator VALIDATOR = ValidatorTestFactory.getValidator();

    @Mock
    private CompletedFileUpload fileUpload;

//    @ParameterizedTest
//    @ArgumentsSource(SupportedMediaTypesArgumentProvider.class)
//    void shouldNotDetectConstraintViolation_whenMediaTypeIsSupported(MediaType mediaType) {
//        when(fileUpload.getFilename()).thenReturn(mediaType.getExtension());
//
//        var uploadIdentityDocumentRequest = new UploadIdentityDocumentRequest(ACCOUNT_HOLDER_ID_JEFFERSON, fileUpload);
//        var constraintViolations = VALIDATOR.validate(uploadIdentityDocumentRequest);
//        assertThat(constraintViolations).isEmpty();
//    }
//
//    @ParameterizedTest
//    @ArgumentsSource(FileUnsupportedMediaTypesArgumentProvider.class)
//    void shouldDetectConstraintViolation_whenMediaTypeIsUnsupported(String supportedMediaTypeFilename) {
//        when(fileUpload.getFilename()).thenReturn(supportedMediaTypeFilename);
//
//        var uploadIdentityDocumentRequest = new UploadIdentityDocumentRequest(ACCOUNT_HOLDER_ID_JEFFERSON, fileUpload);
//        var constraintViolations = VALIDATOR.validate(uploadIdentityDocumentRequest);
//        assertThat(constraintViolations)
//                .hasSize(1)
//                .first()
//                .satisfies(violation -> {
//                    assertThat(violation.getMessage()).isEqualTo("accountHolder.fileUpload.unsupportedFileExtension");
//                    assertThat(violation.getPropertyPath()).hasToString("fileUpload");
//                });
//    }

    @Test
    void shouldDetectConstraintViolation_whenAccountHolderIdIsNull() {
        var uploadIdentityDocumentRequest = new UploadIdentityDocumentRequest(null, fileUpload);

        var constraintViolations = VALIDATOR.validate(uploadIdentityDocumentRequest);
        assertThat(constraintViolations)
                .hasSize(1)
                .first()
                .satisfies(violation -> {
                    assertThat(violation.getMessage()).isEqualTo("accountHolder.accountHolderId.notNull");
                    assertThat(violation.getPropertyPath()).hasToString("accountHolderId");
                });
    }

    @Test
    void shouldDetectConstraintViolation_whenFileUploadIsNull() {
        var uploadIdentityDocumentRequest = new UploadIdentityDocumentRequest(ACCOUNT_HOLDER_ID_JEFFERSON, null);

        var constraintViolations = VALIDATOR.validate(uploadIdentityDocumentRequest);
        assertThat(constraintViolations)
                .hasSize(1)
                .first()
                .satisfies(violation -> {
                    assertThat(violation.getMessage()).isEqualTo("accountHolder.fileUpload.notNull");
                    assertThat(violation.getPropertyPath()).hasToString("fileUpload");
                });
    }

    @Test
    void shouldDetectMultipleConstraintViolations_whenAllFieldsAreNull() {
        var request = new UploadIdentityDocumentRequest(null, null);

        var constraintViolations = VALIDATOR.validate(request);
        assertThat(constraintViolations)
                .hasSize(2)
                .anySatisfy(violation -> {
                    assertThat(violation.getMessage()).isEqualTo("accountHolder.accountHolderId.notNull");
                    assertThat(violation.getPropertyPath()).hasToString("accountHolderId");
                })
                .anySatisfy(violation -> {
                    assertThat(violation.getMessage()).isEqualTo("accountHolder.fileUpload.notNull");
                    assertThat(violation.getPropertyPath()).hasToString("fileUpload");
                });
    }

//    @ParameterizedTest
//    @ValueSource(strings = { MediaType.IMAGE_PNG, MediaType.IMAGE_JPEG })
//    void shouldReturnObjectStorageKey_whenContentTypeIsPresent(String imageType) {
//        var mediaType = MediaType.of(imageType);
//        when(fileUpload.getContentType()).thenReturn(Optional.of(mediaType));
//
//        var uploadIdentityDocumentRequest = new UploadIdentityDocumentRequest(ACCOUNT_HOLDER_ID_JEFFERSON, fileUpload);
//        String expectedStorageKey = ACCOUNT_HOLDER_ID_JEFFERSON + "." + mediaType.getExtension();
//
//        assertThat(uploadIdentityDocumentRequest.storageKey())
//                .isEqualTo(expectedStorageKey);
//    }
//
//    @Test
//    void shouldReturnObjectStorageKey_whenContentTypeIsAbsent() {
//        when(fileUpload.getContentType()).thenReturn(Optional.empty());
//
//        var uploadIdentityDocumentRequest = new UploadIdentityDocumentRequest(ACCOUNT_HOLDER_ID_JEFFERSON, fileUpload);
//
//        assertThatThrownBy(() -> uploadIdentityDocumentRequest.storageKey())
//                .isInstanceOf(IllegalStateException.class);
//    }
}