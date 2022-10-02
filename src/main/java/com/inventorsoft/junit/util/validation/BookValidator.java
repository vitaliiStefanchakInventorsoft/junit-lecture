package com.inventorsoft.junit.util.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = BookValidatorConstraint.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface BookValidator {

  String message() default "Invalid request";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
