package by.arvisit.modsenlibapp.libraryservice.util;

import java.time.LocalDate;
import java.util.List;

import by.arvisit.modsenlibapp.innerfilterstarter.dto.UserDto;
import by.arvisit.modsenlibapp.libraryservice.dto.LibraryBookDto;
import by.arvisit.modsenlibapp.libraryservice.dto.BorrowedBookResponseDto;
import by.arvisit.modsenlibapp.libraryservice.dto.ReturnedBookResponseDto;

public final class LibraryITData {

    public static final String NEW_BOOK_ID = "66701adf-364d-44bd-8d46-46ef01d2af4d";
    public static final String AVAILABLE_NOT_BORROWED_YET_BOOK_ID = "f4b27fe4-0ea1-423d-add6-daa2ee63802f";
    public static final String AVAILABLE_RETURNED_BOOK_ID = "c79eb50b-2e71-4e98-87ab-2074c7441713";
    public static final String BORROWED_RETURNED_BOOK_ID = "0319cc17-a6e0-4bfd-a9ef-2ab5cca8abca";

    public static final String URL_BOOKS_ENDPOINT = "/api/v1/library/books";
    public static final String URL_AVAILABLE_BOOKS_ENDPOINT = "/api/v1/library/books/available";
    public static final String URL_BORROWED_BOOKS_ENDPOINT = "/api/v1/library/books/borrowed";
    public static final String URL_BORROWED_BOOK_INFO_ENDPOINT = "/api/v1/library/books/borrowed/{id}";
    public static final String URL_BORROW_BOOK_ENDPOINT = "/api/v1/library/books/available/{id}/borrow";
    public static final String URL_RETURN_BOOK_ENDPOINT = "/api/v1/library/books/borrowed/{id}/return";

    public static final String USERNAME = "user@mail.com";

    private LibraryITData() {
    }

    public static LibraryBookDto.LibraryBookDtoBuilder getNewBook() {
        return LibraryBookDto.builder()
                .withId(NEW_BOOK_ID);
    }

    public static LibraryBookDto.LibraryBookDtoBuilder getAvailableNotBorrowedBook() {
        return LibraryBookDto.builder()
                .withId(AVAILABLE_NOT_BORROWED_YET_BOOK_ID);
    }

    public static LibraryBookDto.LibraryBookDtoBuilder getAvailableReturnedBook() {
        return LibraryBookDto.builder()
                .withId(AVAILABLE_RETURNED_BOOK_ID);
    }

    public static LibraryBookDto.LibraryBookDtoBuilder getBorrowedReturnedBook() {
        return LibraryBookDto.builder()
                .withId(BORROWED_RETURNED_BOOK_ID);
    }

    public static UserDto getUser() {
        return new UserDto(USERNAME, List.of("ROLE_USER"));
    }

    public static UserDto getAdmin() {
        return new UserDto(USERNAME, List.of("ROLE_ADMIN"));
    }

    public static BorrowedBookResponseDto.BorrowedBookResponseDtoBuilder getNewBorrowedBookRecord() {
        LocalDate borrowedDate = LocalDate.now();
        return BorrowedBookResponseDto.builder()
                .withBookId(AVAILABLE_NOT_BORROWED_YET_BOOK_ID)
                .withBorrowerUsername(USERNAME)
                .withBorrowedDate(borrowedDate)
                .withReturnDueDate(borrowedDate.plusMonths(1L));
    }
    
    public static BorrowedBookResponseDto.BorrowedBookResponseDtoBuilder getBorrowedBookRecordFromDB() {
        return BorrowedBookResponseDto.builder()
                .withBookId(BORROWED_RETURNED_BOOK_ID)
                .withBorrowerUsername(USERNAME)
                .withBorrowedDate(LocalDate.parse("2021-01-01"))
                .withReturnDueDate(LocalDate.parse("2021-02-01"));
    }

    public static ReturnedBookResponseDto.ReturnedBookResponseDtoBuilder getReturnedBookRecord() {
        LocalDate returnDate = LocalDate.now();
        return ReturnedBookResponseDto.builder()
                .withBookId(BORROWED_RETURNED_BOOK_ID)
                .withBorrowerUsername(USERNAME)
                .withBorrowedDate(LocalDate.parse("2021-01-01"))
                .withReturnDueDate(LocalDate.parse("2021-02-01"))
                .withReturnedDate(returnDate);
    }
}
