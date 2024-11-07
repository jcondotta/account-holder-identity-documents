package com.jcondotta.helper;

import io.micronaut.http.MediaType;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Instant;
import java.util.Objects;

public class TestTempFileCreator {

    public static File createFile(MediaType mediaType) {
        return createTempFile("file_" + Instant.now().toEpochMilli(), mediaType, null);
    }

    public static File createFile(MediaType mediaType, Integer sizeInBytes) {
        return createTempFile("file_" + Instant.now().toEpochMilli(), mediaType, sizeInBytes);
    }

    private static File createTempFile(String baseFileName, MediaType mediaType, Integer sizeInBytes) {
        Objects.requireNonNull(baseFileName);
        Objects.requireNonNull(mediaType);

        try {
            File createdTempFile = Files
                    .createTempFile(baseFileName, ".".concat(mediaType.getExtension()))
                    .toFile();

            createdTempFile.deleteOnExit();

            if (sizeInBytes != null && sizeInBytes > 0) {
                try (FileOutputStream outputStream = new FileOutputStream(createdTempFile)) {
                    byte[] buffer = new byte[1024]; // 1KB buffer
                    int remainingBytes = sizeInBytes;

                    while (remainingBytes > 0) {
                        int bytesToWrite = Math.min(remainingBytes, buffer.length);
                        outputStream.write(buffer, 0, bytesToWrite);
                        remainingBytes -= bytesToWrite;
                    }
                }
            }
            return createdTempFile;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
