package com.jcondotta.helper;

import io.micronaut.http.MediaType;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Instant;
import java.util.Objects;

public class TestTempFileCreator {

    public static File createFile(MediaType mediaType) {
        return createTempFile("file_" + Instant.now().toEpochMilli(), mediaType);
    }

    private static File createTempFile(String baseFileName, MediaType mediaType) {
        Objects.requireNonNull(baseFileName);
        Objects.requireNonNull(mediaType);

        try {
            return Files.createTempFile(baseFileName, ".".concat(mediaType.getExtension())).toFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
