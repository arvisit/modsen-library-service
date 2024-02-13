package by.arvisit.modsenlibapp.libraryservice.util;

import by.arvisit.modsenlibapp.libraryservice.dto.LibraryBookDto;

public final class LibraryTestData {

    public static final String NEW_BOOK_ID = "66701adf-364d-44bd-8d46-46ef01d2af4d";
    
    public static final String URL_BOOKS_ENDPOINT = "/api/v1/books";
    
    private LibraryTestData() {
    }
    
    public static LibraryBookDto.LibraryBookDtoBuilder getNewBook() {
        return LibraryBookDto.builder()
                .withId(NEW_BOOK_ID);
    }
}