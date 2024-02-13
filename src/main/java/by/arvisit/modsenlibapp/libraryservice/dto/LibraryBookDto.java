package by.arvisit.modsenlibapp.libraryservice.dto;

import org.hibernate.validator.constraints.UUID;

import by.arvisit.modsenlibapp.libraryservice.validation.IsBookNotExist;
import lombok.Builder;

@Builder(setterPrefix = "with")
public record LibraryBookDto(@UUID @IsBookNotExist String id) {

}
