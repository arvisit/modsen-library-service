package by.arvisit.modsenlibapp.libraryservice.dto;

import java.time.LocalDate;

import lombok.Builder;

@Builder(setterPrefix = "with")
public record BorrowedBookResponseDto(String bookId, String borrowerUsername, LocalDate borrowedDate,
        LocalDate returnDueDate) {

}
