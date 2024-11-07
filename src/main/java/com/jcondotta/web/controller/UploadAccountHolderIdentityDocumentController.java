package com.jcondotta.web.controller;

import com.jcondotta.service.UploadIdentityDocumentService;
import com.jcondotta.service.request.UploadIdentityDocumentRequest;
import io.micronaut.http.HttpHeaders;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.micronaut.http.multipart.CompletedFileUpload;
import io.micronaut.validation.Validated;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    @Post(consumes = MediaType.MULTIPART_FORM_DATA, produces = MediaType.APPLICATION_JSON)
    @Operation(
            summary = "Upload an identity document",
            description = "Uploads an identity document file for a specific account holder. " +
                    "The request requires a multipart form-data upload, where the file is provided as 'fileUpload'."
    )
    @RequestBody(
            description = "Multipart file containing the identity document",
            required = true,
            content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA, schema = @Schema(type = "string", format = "binary"))
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Identity document successfully uploaded.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON),
                    headers = @Header(name = "ETag",
                            description = "Entity tag for the uploaded document",
                            schema = @Schema(type = "string"))
            ),
            @ApiResponse(responseCode = "400", description = "Bad Request: Required file part 'fileUpload' is missing."),
            @ApiResponse(responseCode = "413", description = "Payload too large. The uploaded file exceeds the maximum size limit."),
            @ApiResponse(responseCode = "500", description = "Internal server error. The upload service encountered an issue.")
    })
    public HttpResponse<String> uploadIdentityDocument(
            @PathVariable("account-holder-id") UUID accountHolderId,
            @Part(value = "fileUpload") CompletedFileUpload fileUpload) {

        LOGGER.info("[AccountHolderId={}] Received request to upload identity document.", accountHolderId);

        var uploadIdentityDocumentRequest = new UploadIdentityDocumentRequest(accountHolderId, fileUpload);
        var uploadResponse = uploadIdDocumentService.upload(uploadIdentityDocumentRequest);

        return HttpResponse
                .created(uploadResponse.getKey())
                .header(HttpHeaders.ETAG, uploadResponse.getETag());
    }
}
