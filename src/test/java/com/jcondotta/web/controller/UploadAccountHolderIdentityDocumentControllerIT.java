package com.jcondotta.web.controller;


import com.jcondotta.argument_provider.SupportedMediaTypesArgumentProvider;
import com.jcondotta.argument_provider.UnsupportedMediaTypesArgumentProvider;
import com.jcondotta.container.LocalStackTestContainer;
import com.jcondotta.helper.TestAccountHolder;
import com.jcondotta.helper.TestTempFileCreator;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.objectstorage.aws.AwsS3Operations;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import jakarta.inject.Inject;
import org.junit.Ignore;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.io.File;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.notNullValue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@MicronautTest(transactional = false)
class UploadAccountHolderIdentityDocumentControllerIT implements LocalStackTestContainer {

    private static final UUID ACCOUNT_HOLDER_ID_JEFFERSON = TestAccountHolder.JEFFERSON.getAccountHolderId();

    @Inject
    protected AwsS3Operations awsS3Operations;

    @Inject
    private RequestSpecification requestSpecification;


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

    @Test
    @Ignore
    void shouldNotUploadIdentityDocument_whenFileSizeExceeds20MB(){

    }

    @ParameterizedTest
    @ArgumentsSource(SupportedMediaTypesArgumentProvider.class)
    void shouldUploadIdentityDocument_whenMediaTypeIsSupported(MediaType supportedMediaType) {
        File file = TestTempFileCreator.createFile(supportedMediaType);

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
    void shouldReturnStatus415UnsupportedMediaType_whenMediaTypeIsUnsupported(MediaType unsupportedMediaType) {
        File file = TestTempFileCreator.createFile(unsupportedMediaType);

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
    void shouldReturnStatus400BadRequest_whenFileUploadIdIsMissing() {
        given()
            .spec(requestSpecification)
                .pathParam("account-holder-id", ACCOUNT_HOLDER_ID_JEFFERSON)
        .when()
            .post()
        .then()
            .statusCode(HttpStatus.BAD_REQUEST.getCode());
    }
}
