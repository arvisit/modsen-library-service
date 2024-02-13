package by.arvisit.modsenlibapp.libraryservice.validation.provider;

import by.arvisit.modsenlibapp.libraryservice.service.BookService;
import by.arvisit.modsenlibapp.libraryservice.validation.IsBookNotExist;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class IsBookNotExistValidator implements ConstraintValidator<IsBookNotExist, String> {

    private final BookService bookService;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        return !bookService.isBookExist(value);
    }

}
