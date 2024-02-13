package by.arvisit.modsenlibapp.libraryservice.util;

import java.time.LocalDate;

import by.arvisit.modsenlibapp.libraryservice.dto.BorrowedBookResponseDto;
import by.arvisit.modsenlibapp.libraryservice.dto.LibraryBookDto;
import by.arvisit.modsenlibapp.libraryservice.dto.ReturnedBookResponseDto;

public final class LibraryTestData {

    public static final String NEW_BOOK_ID = "66701adf-364d-44bd-8d46-46ef01d2af4d";
    public static final String USERNAME = "user@mail.com";

    public static final String URL_BOOKS_ENDPOINT = "/api/v1/books";
    public static final String URL_AVAILABLE_BOOKS_ENDPOINT = "/api/v1/books/available";
    public static final String URL_BORROWED_BOOKS_ENDPOINT = "/api/v1/books/borrowed";
    public static final String URL_BORROW_BOOK_ENDPOINT = "/api/v1/books/available/{id}/borrow";
    public static final String URL_RETURN_BOOK_ENDPOINT = "/api/v1/books/borrowed/{id}/return";
    public static final String URL_BORROWED_BOOK_INFO_ENDPOINT = "/api/v1/books/borrowed/{id}";

    private LibraryTestData() {
    }

    public static LibraryBookDto.LibraryBookDtoBuilder getNewBook() {
        return LibraryBookDto.builder()
                .withId(NEW_BOOK_ID);
    }

    public static BorrowedBookResponseDto.BorrowedBookResponseDtoBuilder getNewBorrowedBookRecord() {
        return BorrowedBookResponseDto.builder()
                .withBookId(NEW_BOOK_ID)
                .withBorrowerUsername(USERNAME)
                .withBorrowedDate(LocalDate.parse("2020-01-01"))
                .withReturnDueDate(LocalDate.parse("2020-02-01"));
    }
    

    public static ReturnedBookResponseDto.ReturnedBookResponseDtoBuilder getReturnedBookRecord() {
        return ReturnedBookResponseDto.builder()
                .withBookId(NEW_BOOK_ID)
                .withBorrowerUsername(USERNAME)
                .withBorrowedDate(LocalDate.parse("2021-01-01"))
                .withReturnDueDate(LocalDate.parse("2021-02-01"))
                .withReturnedDate(LocalDate.parse("2021-01-15"));
    }
}
