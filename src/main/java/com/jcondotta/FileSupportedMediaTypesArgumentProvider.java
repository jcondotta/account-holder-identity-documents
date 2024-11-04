package com.jcondotta;

import io.micronaut.http.MediaType;

import java.util.Arrays;
import java.util.List;

public class FileSupportedMediaTypesArgumentProvider {

    private static final List<MediaType> SUPPORTED_MEDIA_TYPES = Arrays.asList(
            MediaType.IMAGE_PNG_TYPE,
            MediaType.IMAGE_JPEG_TYPE
    );

    public static List<MediaType> getSupportedMediaTypes() {
        return SUPPORTED_MEDIA_TYPES;
    }
}
