package by.arvisit.modsenlibapp.libraryservice.validation.provider;

import by.arvisit.modsenlibapp.libraryservice.service.BookService;
import by.arvisit.modsenlibapp.libraryservice.validation.IsBookExist;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class IsBookExistValidator implements ConstraintValidator<IsBookExist, String> {

    private final BookService bookService;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        return bookService.isBookExist(value);
    }

}
