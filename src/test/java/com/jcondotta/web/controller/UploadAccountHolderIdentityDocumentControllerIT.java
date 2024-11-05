package com.jcondotta.web.controller;


import com.jcondotta.argument_provider.SupportedMediaTypesArgumentProvider;
import com.jcondotta.container.LocalStackTestContainer;
import com.jcondotta.helper.TestAccountHolder;
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
//
//    @ParameterizedTest
//    @ArgumentsSource(FileUnsupportedMediaTypesArgumentProvider.class)
//    public void shouldNotUploadIdentityDocument_whenMediaTypeIsUnsupported(String supportedMediaTypeFilename) {
//        File file = createTempFile(supportedMediaTypeFilename);
//
//        given()
//            .spec(requestSpecification)
//                .pathParam("account-holder-id", ACCOUNT_HOLDER_ID_JEFFERSON)
//                .multiPart("fileUpload", file)
//        .when()
//            .post()
//        .then()
//            .statusCode(HttpStatus.BAD_REQUEST.getCode()); //TODO mudar pra Unsupported media type
//
////        assertThat(awsS3Operations.exists(storageKey)).isFalse();
//    }
//
//    @ParameterizedTest
//    @ArgumentsSource(FileUnsupportedMediaTypesArgumentProvider.class)
//    public void shouldNotUploadIdentityDocument_whenFileExceeds20MbSize(String supportedMediaTypeFilename) {
//
//
//    }
//
//    @ParameterizedTest
//    @ValueSource(strings = { MediaType.IMAGE_PNG, MediaType.IMAGE_JPEG })
//    void givenSupportedMediaTypeFiles_whenUploadAccountHolderIdentityDocument_thenUploadFile(MediaType supportedMediaType) throws IOException {
//        File file = createTempFile(supportedMediaType);
//
//        given()
//            .spec(requestSpecification)
//                .multiPart("fileUpload", file)
//                .pathParam("account-holder-id", accountHolderId)
//        .when()
//            .post()
//        .then()
//            .statusCode(HttpStatus.CREATED.getCode())
//                .header("location", equalTo("/api/v1/account-holders/account-holder-id/6635471134/upload-identity-document"))
//                .header("ETag", notNullValue());
//
//        var expectedS3ObjectKey = S3ObjectKeyBuilder.build(accountHolderId, file.getName());
//        Assertions.assertThat(awsS3Operations.exists(expectedS3ObjectKey)).isTrue();
//
//        await().pollDelay(100, TimeUnit.MILLISECONDS).untilAsserted(() -> {
//            var receiveMessageResponse = sqsClient.receiveMessage(builder -> builder.queueUrl(accountHolderIdentityDocumentQueueURL).build());
//            assertThat(receiveMessageResponse.messages().size()).isEqualTo(1);
//
//            var message = receiveMessageResponse.messages().get(0);
//            var uploadedIdentityDocumentMessage = jsonMapper.readValue(message.body(), UploadedIdentityDocumentMessage.class);
//
//            assertThat(uploadedIdentityDocumentMessage.accountHolderId()).isEqualTo(accountHolderId);
//            assertThat(uploadedIdentityDocumentMessage.accountHolderIdDocumentKey()).isEqualTo(expectedS3ObjectKey);
//        });
//    }
//
//    @ParameterizedTest
//    @ValueSource(strings = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_PDF, MediaType.IMAGE_GIF, MediaType.TEXT_PLAIN })
//    void givenUnsupportedMediaTypeFiles_whenUploadAccountHolderIdentityDocument_thenReturnBadRequest(MediaType unsupportedMediaType) throws IOException {
//        File file = createTempFile(unsupportedMediaType);
//
//        given()
//            .spec(requestSpecification)
//                .multiPart("fileUpload", file)
//                .pathParam("account-holder-id", accountHolderId)
//        .when()
//            .post()
//        .then()
//            .statusCode(HttpStatus.BAD_REQUEST.getCode())
//            .rootPath("_embedded")
//                .body("errors", hasSize(1))
//                .body("errors[0].message", equalTo("Only .png and .jpeg files are accepted"));
//
//
//        var nonExistentS3ObjectKey = S3ObjectKeyBuilder.build(accountHolderId, file.getName());
//        Assertions.assertThat(awsS3Operations.exists(nonExistentS3ObjectKey)).isFalse();
//
//        await().pollDelay(100, TimeUnit.MILLISECONDS).untilAsserted(() -> {
//            var receiveMessageResponse = sqsClient.receiveMessage(builder -> builder.queueUrl(accountHolderIdentityDocumentQueueURL).build());
//            assertThat(receiveMessageResponse.messages().size()).isEqualTo(0);
//        });
//    }
}
