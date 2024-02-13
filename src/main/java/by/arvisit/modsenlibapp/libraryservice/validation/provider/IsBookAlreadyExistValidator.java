package by.arvisit.modsenlibapp.libraryservice.validation.provider;

import by.arvisit.modsenlibapp.libraryservice.service.BookService;
import by.arvisit.modsenlibapp.libraryservice.validation.IsBookAlreadyExist;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class IsBookAlreadyExistValidator implements ConstraintValidator<IsBookAlreadyExist, String> {

    private final BookService bookService;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        return !bookService.isBookExist(value);
    }

}
