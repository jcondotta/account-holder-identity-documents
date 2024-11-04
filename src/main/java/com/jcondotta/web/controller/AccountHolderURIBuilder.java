package com.jcondotta.web.controller;

import io.micronaut.http.uri.UriBuilder;
import jakarta.validation.constraints.NotNull;

import java.net.URI;
import java.util.Map;
import java.util.UUID;

public interface AccountHolderURIBuilder {

    String BASE_PATH_API_V1_MAPPING = "/api/v1/account-holders";
    String IDENTITY_DOCUMENT_API_V1_MAPPING = BASE_PATH_API_V1_MAPPING + "/account-holder-id/{account-holder-id}/identity-document";

    static URI uploadIdentityDocumentURI(@NotNull UUID accountHolderId) {
        return UriBuilder.of(IDENTITY_DOCUMENT_API_V1_MAPPING)
                .expand(Map.of("account-holder-id", accountHolderId.toString()));
    }
}
