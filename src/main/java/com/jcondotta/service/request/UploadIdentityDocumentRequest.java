package com.jcondotta.service.request;

import com.jcondotta.annotation.ValidatedFileExtensions;
import com.jcondotta.service.UploadIdentityDocumentService;
import io.micronaut.http.multipart.CompletedFileUpload;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

@Serdeable
public record UploadIdentityDocumentRequest(
        @NotNull(message = "accountHolder.accountHolderId.notNull")
        UUID accountHolderId,

        @NotNull(message = "accountHolder.fileUpload.notNull")
        @ValidatedFileExtensions
        CompletedFileUpload fileUpload)
{
        public String storageKey() {
                return accountHolderId.toString()
                        .concat(fileUpload.getContentType()
                                .map(contentType -> "." + contentType.getExtension())
                                .orElseThrow((IllegalStateException::new)));
        }
}