package by.arvisit.modsenlibapp.libraryservice.validation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import by.arvisit.modsenlibapp.libraryservice.validation.provider.IsBookAlreadyExistValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Target({ FIELD, PARAMETER })
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = IsBookAlreadyExistValidator.class)
public @interface IsBookAlreadyExist {

    String message() default "{by.arvisit.modsenlibapp.libraryservice.validation.IsBookAlreadyExist.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
