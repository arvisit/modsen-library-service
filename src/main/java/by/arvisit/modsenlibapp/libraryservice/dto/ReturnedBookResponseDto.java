package by.arvisit.modsenlibapp.libraryservice.dto;

import java.time.LocalDate;

public record ReturnedBookResponseDto(Long id, String bookId, String borrowerUsername, LocalDate borrowedDate,
        LocalDate returnDueDate, LocalDate returnedDate) {

}
