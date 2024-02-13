package by.arvisit.modsenlibapp.libraryservice.dto;

import org.hibernate.validator.constraints.UUID;

import by.arvisit.modsenlibapp.libraryservice.validation.IsBookAlreadyExist;
import lombok.Builder;

@Builder(setterPrefix = "with")
public record LibraryBookDto(@UUID @IsBookAlreadyExist String id) {

}
