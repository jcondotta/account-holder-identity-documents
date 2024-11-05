package com.jcondotta.web.controller;

import com.jcondotta.service.UploadIdentityDocumentService;
import com.jcondotta.service.request.UploadIdentityDocumentRequest;
import io.micronaut.http.*;
import io.micronaut.http.annotation.*;
import io.micronaut.http.multipart.CompletedFileUpload;
import io.micronaut.objectstorage.response.UploadResponse;
import io.micronaut.validation.Validated;
import jakarta.inject.Inject;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import java.util.UUID;

@Validated
@Controller(AccountHolderURIBuilder.IDENTITY_DOCUMENT_API_V1_MAPPING)
public class UploadAccountHolderIdentityDocumentController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UploadAccountHolderIdentityDocumentController.class);

    private final UploadIdentityDocumentService uploadIdDocumentService;

    @Inject
    public UploadAccountHolderIdentityDocumentController(UploadIdentityDocumentService uploadIdDocumentService){
        this.uploadIdDocumentService = uploadIdDocumentService;
    }

    @Status(HttpStatus.CREATED)
    @Post(consumes = MediaType.MULTIPART_FORM_DATA)
    public HttpResponse<?> uploadIdentityDocument(@PathVariable("account-holder-id") UUID accountHolderId, @Part CompletedFileUpload fileUpload){
        LOGGER.info("[AccountHolderId={}] Received request to upload identity document.", accountHolderId);

        var uploadIdentityDocumentRequest = new UploadIdentityDocumentRequest(accountHolderId, fileUpload);
        var uploadResponse = uploadIdDocumentService.upload(uploadIdentityDocumentRequest);

        return HttpResponse
                .created(uploadResponse.getKey())
                .header(HttpHeaders.ETAG, uploadResponse.getETag());

    }
}
