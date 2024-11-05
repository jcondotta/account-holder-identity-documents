package com.jcondotta.annotation.validator;

import com.jcondotta.annotation.ValidatedFileExtensions;
import io.micronaut.core.annotation.AnnotationValue;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.core.util.StringUtils;
import io.micronaut.http.MediaType;
import io.micronaut.http.multipart.CompletedFileUpload;
import io.micronaut.validation.validator.constraints.ConstraintValidator;
import io.micronaut.validation.validator.constraints.ConstraintValidatorContext;
import jakarta.inject.Singleton;
import org.apache.commons.io.FilenameUtils;

import java.util.List;

@Singleton
public class ValidatedFileExtensionsValidator implements ConstraintValidator<ValidatedFileExtensions, CompletedFileUpload> {

    public static final List<MediaType> supportedMediaTypes = List.of(MediaType.IMAGE_PNG_TYPE, MediaType.IMAGE_JPEG_TYPE);

    public static final List<String> supportedFileExtensions = supportedMediaTypes.stream()
            .map(mediaType -> mediaType.getExtension())
            .toList();

    @Override
    public boolean isValid(@Nullable CompletedFileUpload fileUpload, @NonNull AnnotationValue<ValidatedFileExtensions> annotationMetadata, @NonNull ConstraintValidatorContext context) {
        if (fileUpload == null) {
            return true;
        }

        if(StringUtils.isNotEmpty(fileUpload.getFilename())){
            var fileExtension = FilenameUtils.getExtension(fileUpload.getFilename()).trim().toLowerCase();
            return supportedFileExtensions.contains(fileExtension);
        }

        return false;
    }
}
