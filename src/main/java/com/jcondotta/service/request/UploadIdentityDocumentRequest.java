package com.jcondotta.service.request;

import com.jcondotta.annotation.ValidatedFileExtensions;
import io.micronaut.http.multipart.CompletedFileUpload;
import io.micronaut.serde.annotation.Serdeable;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

@Serdeable
@Schema(description = "Request object for uploading an identity document for an account holder.")
public record UploadIdentityDocumentRequest(

        @NotNull(message = "accountHolder.accountHolderId.notNull")
        @Schema(description = "Unique identifier of the account holder.", example = "123e4567-e89b-12d3-a456-426614174000")
        UUID accountHolderId,

        @NotNull(message = "accountHolder.fileUpload.notNull")
        @ValidatedFileExtensions(message = "accountHolder.fileUpload.unsupportedFileExtension")
        @Schema(description = "The file to be uploaded. Only supported file extensions are allowed.", type = "string", format = "binary")
        CompletedFileUpload fileUpload)
{
        public String storageKey() {
                return accountHolderId.toString()
                        .concat(fileUpload.getContentType()
                                .map(contentType -> "." + contentType.getExtension())
                                .orElseThrow(() -> new IllegalStateException("accountHolder.fileUpload.contentType.notNull")));
        }
}
