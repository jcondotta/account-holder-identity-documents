package com.jcondotta.annotation;

import com.jcondotta.annotation.validator.ValidatedFileExtensionsValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ValidatedFileExtensionsValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidatedFileExtensions {

    String message() default "accountHolder.fileUpload.unsupportedFileExtension";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}