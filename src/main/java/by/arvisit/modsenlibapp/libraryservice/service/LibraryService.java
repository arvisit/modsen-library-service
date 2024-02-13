package by.arvisit.modsenlibapp.libraryservice.service;

import java.util.List;

import by.arvisit.modsenlibapp.libraryservice.dto.BorrowedBookResponseDto;
import by.arvisit.modsenlibapp.libraryservice.dto.LibraryBookDto;
import by.arvisit.modsenlibapp.libraryservice.dto.ReturnedBookResponseDto;

public interface LibraryService {

    LibraryBookDto addNewBook(String id);

    List<LibraryBookDto> getAvailableBooks();

    List<LibraryBookDto> getBorrowedBooks();

    BorrowedBookResponseDto borrowBook(String id);

    ReturnedBookResponseDto returnBook(String id);

    BorrowedBookResponseDto getBorrowedBookInfo(String id);
}
