package com.jcondotta.helper;

import io.micronaut.http.MediaType;

import java.time.Instant;
import java.util.Objects;

public class TestFilenameBuilder {

    public static String build(MediaType mediaType) {
        return buildFilename("file_" + Instant.now().toEpochMilli(), mediaType);
    }

    private static String buildFilename(String baseFileName, MediaType mediaType) {
        Objects.requireNonNull(baseFileName);
        Objects.requireNonNull(mediaType);

        return baseFileName + "." + mediaType.getExtension();
    }
}
