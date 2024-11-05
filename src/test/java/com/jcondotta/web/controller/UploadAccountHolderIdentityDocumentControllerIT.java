package com.jcondotta.web.controller;


import com.jcondotta.argument_provider.SupportedMediaTypesArgumentProvider;
import com.jcondotta.argument_provider.UnsupportedMediaTypesArgumentProvider;
import com.jcondotta.container.LocalStackTestContainer;
import com.jcondotta.helper.TestAccountHolder;
import com.jcondotta.service.request.UploadIdentityDocumentRequest;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.objectstorage.aws.AwsS3Operations;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.notNullValue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@MicronautTest(transactional = false)
class UploadAccountHolderIdentityDocumentControllerIT implements LocalStackTestContainer {

    @Inject
    protected AwsS3Operations awsS3Operations;

    @Inject
    private RequestSpecification requestSpecification;

    private static final UUID ACCOUNT_HOLDER_ID_JEFFERSON = TestAccountHolder.JEFFERSON.getAccountHolderId();

    @BeforeAll
    public static void beforeAll(){
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @BeforeEach
    public void beforeEach(RequestSpecification requestSpecification) {
        this.requestSpecification = requestSpecification
                .contentType(ContentType.MULTIPART)
                .basePath(AccountHolderURIBuilder.IDENTITY_DOCUMENT_API_V1_MAPPING);
    }

    public File createTempFile(MediaType mediaType)  {
        Path path;
        try {
            path = Files.createTempFile("test-file-", ".".concat(mediaType.getExtension()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return path.toFile();
    }

    @ParameterizedTest
    @ArgumentsSource(SupportedMediaTypesArgumentProvider.class)
    public void shouldUploadIdentityDocument_whenMediaTypeIsSupported(MediaType supportedMediaType) {
        File file = createTempFile(supportedMediaType);

        var storageKey = given()
            .spec(requestSpecification)
                .pathParam("account-holder-id", ACCOUNT_HOLDER_ID_JEFFERSON)
                .multiPart("fileUpload", file)
        .when()
            .post()
        .then()
            .statusCode(HttpStatus.CREATED.getCode())
                .header("ETag", notNullValue())
                .extract().asString();

        assertThat(awsS3Operations.exists(storageKey)).isTrue();
    }

    @ParameterizedTest
    @ArgumentsSource(UnsupportedMediaTypesArgumentProvider.class)
    public void shouldReturnStatus415UnsupportedMediaType_whenMediaTypeIsUnsupported(MediaType unsupportedMediaType) {
        File file = createTempFile(unsupportedMediaType);

        given()
            .spec(requestSpecification)
                .pathParam("account-holder-id", ACCOUNT_HOLDER_ID_JEFFERSON)
                .multiPart("fileUpload", file)
        .when()
            .post()
        .then()
            .statusCode(HttpStatus.BAD_REQUEST.getCode());
    }

    @Test
    void shouldReturnStatus400BadRequest_whenFileUploadIdIsNull() {
        given()
            .spec(requestSpecification)
                .pathParam("account-holder-id", ACCOUNT_HOLDER_ID_JEFFERSON)
        .when()
            .post()
        .then()
            .statusCode(HttpStatus.BAD_REQUEST.getCode());
    }
}
