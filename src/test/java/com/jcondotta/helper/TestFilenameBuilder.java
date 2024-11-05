package com.jcondotta.helper;

import io.micronaut.http.MediaType;

import java.time.Instant;

public record TestFilenameBuilder(String filename, MediaType mediaType) {

    public TestFilenameBuilder(MediaType mediaType) {
        this("test-file" + Instant.now().toEpochMilli(), mediaType);
    }

    public String fullFilename(){
        return filename + "." + mediaType.getExtension();
    }
}
