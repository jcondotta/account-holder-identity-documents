package com.jcondotta.service;

import com.jcondotta.argument_provider.SupportedMediaTypesArgumentProvider;
import com.jcondotta.argument_provider.UnsupportedMediaTypesArgumentProvider;
import com.jcondotta.factory.ValidatorTestFactory;
import com.jcondotta.helper.TestAccountHolder;
import com.jcondotta.helper.TestFilenameBuilder;
import com.jcondotta.service.request.UploadIdentityDocumentRequest;
import io.micronaut.http.MediaType;
import io.micronaut.http.multipart.CompletedFileUpload;
import io.micronaut.objectstorage.aws.AwsS3Operations;
import io.micronaut.objectstorage.request.UploadRequest;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UploadIdentityDocumentServiceTest {

    private static final UUID ACCOUNT_HOLDER_ID_JEFFERSON = TestAccountHolder.JEFFERSON.getAccountHolderId();
    private static final Validator VALIDATOR = ValidatorTestFactory.getValidator();

    @Mock
    private AwsS3Operations awsS3Operations;

    @Mock
    private CompletedFileUpload fileUpload;

    private UploadIdentityDocumentService uploadIdentityDocumentService;

    @BeforeEach
    void beforeEach() {
        uploadIdentityDocumentService = new UploadIdentityDocumentService(awsS3Operations, VALIDATOR);
    }

    @ParameterizedTest
    @ArgumentsSource(SupportedMediaTypesArgumentProvider.class)
    void shouldUploadIdentityDocument_whenMediaTypeIsSupported(MediaType supportedMediaType) {
        var filename = TestFilenameBuilder.build(supportedMediaType);

        when(fileUpload.getFilename()).thenReturn(filename);
        when(fileUpload.getContentType()).thenReturn(Optional.of(supportedMediaType));

        var identityDocumentStorageRequest = new UploadIdentityDocumentRequest(ACCOUNT_HOLDER_ID_JEFFERSON, fileUpload);
        uploadIdentityDocumentService.upload(identityDocumentStorageRequest);

        verify(awsS3Operations).upload(Mockito.any(UploadRequest.class));
        verifyNoMoreInteractions(awsS3Operations);
    }

    @ParameterizedTest
    @ArgumentsSource(UnsupportedMediaTypesArgumentProvider.class)
    void shouldThrowConstraintViolationException_whenMediaTypeIsUnsupported(MediaType unsupportedMediaType) {
        var filename = TestFilenameBuilder.build(unsupportedMediaType);
        when(fileUpload.getFilename()).thenReturn(filename);

        var identityDocumentStorageRequest = new UploadIdentityDocumentRequest(ACCOUNT_HOLDER_ID_JEFFERSON, fileUpload);
        var exception = assertThrows(ConstraintViolationException.class, () -> uploadIdentityDocumentService.upload(identityDocumentStorageRequest));
        assertThat(exception.getConstraintViolations())
                .hasSize(1);

        verifyNoInteractions(awsS3Operations);
    }

    @ParameterizedTest
    @ArgumentsSource(SupportedMediaTypesArgumentProvider.class)
    void shouldThrowConstraintViolationException_whenAccountHolderIdIsNull(MediaType supportedMediaType) {
        var filename = TestFilenameBuilder.build(supportedMediaType);
        when(fileUpload.getFilename()).thenReturn(filename);

        var identityDocumentStorageRequest = new UploadIdentityDocumentRequest(null, fileUpload);
        var exception = assertThrows(ConstraintViolationException.class, () -> uploadIdentityDocumentService.upload(identityDocumentStorageRequest));
        assertThat(exception.getConstraintViolations())
                .hasSize(1);

        verifyNoInteractions(awsS3Operations);
    }

    @Test
    void shouldThrowConstraintViolationException_whenFileUploadIsNull() {
        var identityDocumentStorageRequest = new UploadIdentityDocumentRequest(ACCOUNT_HOLDER_ID_JEFFERSON, null);
        var exception = assertThrows(ConstraintViolationException.class, () -> uploadIdentityDocumentService.upload(identityDocumentStorageRequest));
        assertThat(exception.getConstraintViolations())
                .hasSize(1);

        verifyNoInteractions(awsS3Operations);
    }

    @ParameterizedTest
    @ArgumentsSource(SupportedMediaTypesArgumentProvider.class)
    void shouldThrowStateException_whenFileUploadContentTypeIsMissing(MediaType supportedMediaType) {
        var filename = TestFilenameBuilder.build(supportedMediaType);
        when(fileUpload.getFilename()).thenReturn(filename);
        when(fileUpload.getContentType()).thenReturn(Optional.empty());

        var identityDocumentStorageRequest = new UploadIdentityDocumentRequest(ACCOUNT_HOLDER_ID_JEFFERSON, fileUpload);
        assertThatThrownBy(() -> uploadIdentityDocumentService.upload(identityDocumentStorageRequest))
                .isInstanceOf(IllegalStateException.class);

        verifyNoMoreInteractions(awsS3Operations);
    }
}
