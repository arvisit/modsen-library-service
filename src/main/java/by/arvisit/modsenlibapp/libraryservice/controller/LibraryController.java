package by.arvisit.modsenlibapp.libraryservice.controller;

import java.util.List;

import org.hibernate.validator.constraints.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import by.arvisit.modsenlibapp.libraryservice.controller.openapi.LibraryOpenApi;
import by.arvisit.modsenlibapp.libraryservice.dto.BorrowedBookResponseDto;
import by.arvisit.modsenlibapp.libraryservice.dto.LibraryBookDto;
import by.arvisit.modsenlibapp.libraryservice.dto.ReturnedBookResponseDto;
import by.arvisit.modsenlibapp.libraryservice.service.LibraryService;
import by.arvisit.modsenlibapp.libraryservice.validation.IsBookExist;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/books")
@RequiredArgsConstructor
@Validated
@Slf4j
public class LibraryController implements LibraryOpenApi {

    private final LibraryService libraryService;
    
    @Override
    @PostMapping
    @RolesAllowed("ADMIN")
    public ResponseEntity<LibraryBookDto> addNewBook(@RequestBody @Valid LibraryBookDto newBook) {
        LibraryBookDto response = libraryService.addNewBook(newBook);
        log.debug("New book with id {} was added", response);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Override
    @GetMapping("/available")
    @RolesAllowed({ "USER", "ADMIN" })
    public List<LibraryBookDto> getAvailableBooks() {
        List<LibraryBookDto> response = libraryService.getAvailableBooks();
        log.debug("Got all available books with amount of {}", response.size());
        return response;
    }

    @Override
    @GetMapping("/borrowed")
    @RolesAllowed({ "USER", "ADMIN" })
    public List<LibraryBookDto> getBorrowedBooks() {
        List<LibraryBookDto> response = libraryService.getBorrowedBooks();
        log.debug("Got all borrowed books with amount of {}", response.size());
        return response;
    }

    @Override
    @PostMapping("/available/{id}/borrow")
    @RolesAllowed("USER")
    public ResponseEntity<BorrowedBookResponseDto> borrowBook(@PathVariable @UUID @IsBookExist String id) {
        BorrowedBookResponseDto response = libraryService.borrowBook(id);
        log.debug("Book with id {} is now borrowed", id);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Override
    @PatchMapping("/borrowed/{id}/return")
    @RolesAllowed("ADMIN")
    public ReturnedBookResponseDto returnBook(@PathVariable @UUID @IsBookExist String id) {
        ReturnedBookResponseDto response = libraryService.returnBook(id);
        log.debug("Book with id {} was returned and now available", id);
        return response;
    }

    @Override
    @GetMapping("/borrowed/{id}")
    @RolesAllowed("ADMIN")
    public BorrowedBookResponseDto getBorrowedBookInfo(@PathVariable @UUID @IsBookExist String id) {
        BorrowedBookResponseDto response = libraryService.getBorrowedBookInfo(id);
        log.debug("Got info about borrowed book with id {}: {}", id, response);
        return response;
    }
}
