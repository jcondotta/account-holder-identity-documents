package com.jcondotta.service;

import com.jcondotta.service.request.UploadIdentityDocumentRequest;
import io.micronaut.objectstorage.aws.AwsS3Operations;
import io.micronaut.objectstorage.request.UploadRequest;
import io.micronaut.objectstorage.response.UploadResponse;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.inject.Singleton;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

@Singleton
public class UploadIdentityDocumentService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UploadIdentityDocumentService.class);

    private final AwsS3Operations awsS3Operations;
    private final Validator validator;

    @Inject
    public UploadIdentityDocumentService(@Named("account-holder-identity-document") AwsS3Operations awsS3Operations, Validator validator) {
        this.awsS3Operations = awsS3Operations;
        this.validator = validator;
    }

    public UploadResponse<PutObjectResponse> upload(@NotNull UploadIdentityDocumentRequest uploadIdentityDocumentRequest) {
        var accountHolderId = uploadIdentityDocumentRequest.accountHolderId();

        LOGGER.info("[AccountHolderId={}] Starting upload of identity document.", accountHolderId);

        var constraintViolations = validator.validate(uploadIdentityDocumentRequest);
        if (!constraintViolations.isEmpty()) {
            LOGGER.warn("[AccountHolderId={}] Validation errors for request. Violations: {}",
                    uploadIdentityDocumentRequest.accountHolderId(), constraintViolations);
            throw new ConstraintViolationException(constraintViolations);
        }

        var storageKey = uploadIdentityDocumentRequest.storageKey();
        var uploadRequest = UploadRequest.fromCompletedFileUpload(uploadIdentityDocumentRequest.fileUpload(), storageKey);

        var uploadResponse = awsS3Operations.upload(uploadRequest);

        LOGGER.info("[AccountHolderId={}] Identity document uploaded successfully with S3 key: {}", accountHolderId, storageKey);

        return uploadResponse;
    }
}
