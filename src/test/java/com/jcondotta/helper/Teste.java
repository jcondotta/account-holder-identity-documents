package com.jcondotta.helper;

import io.micronaut.http.MediaType;

public enum Teste {

    PNG(new TestFilenameBuilder(MediaType.IMAGE_PNG_TYPE)),
    JPEG(new TestFilenameBuilder(MediaType.IMAGE_JPEG_TYPE)),
    GIF(new TestFilenameBuilder(MediaType.IMAGE_GIF_TYPE)),
    PDF(new TestFilenameBuilder(MediaType.APPLICATION_PDF_TYPE)),
    JSON(new TestFilenameBuilder(MediaType.APPLICATION_JSON_TYPE));

    private TestFilenameBuilder testFilenameBuilder;

    Teste(TestFilenameBuilder testFilenameBuilder) {
        testFilenameBuilder = testFilenameBuilder;
    }
}
