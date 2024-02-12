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

import by.arvisit.modsenlibapp.libraryservice.dto.BorrowedBookResponseDto;
import by.arvisit.modsenlibapp.libraryservice.dto.ReturnBookRequestDto;
import by.arvisit.modsenlibapp.libraryservice.dto.ReturnedBookResponseDto;
import jakarta.annotation.security.RolesAllowed;

@RestController
@RequestMapping("/api/v1/books")
@Validated
public class LibraryController {

    @PostMapping
    @RolesAllowed("ADMIN")
    public ResponseEntity<String> addNewBook() {
        // TODO implement!
        return new ResponseEntity<>(null, HttpStatus.CREATED);
    }

    @GetMapping("/available")
    @RolesAllowed({ "USER", "ADMIN" })
    public List<String> getAvailableBooks() {
        // TODO implement!
        return null;
    }

    @GetMapping("/borrowed")
    @RolesAllowed({ "USER", "ADMIN" })
    public List<String> getBorrowedBooks() {
        // TODO implement!
        return null;
    }

    @PostMapping("/available/{id}/borrow")
    @RolesAllowed("USER")
    public ResponseEntity<BorrowedBookResponseDto> borrowBook(@PathVariable @UUID String id) {
        // TODO implement!
        return new ResponseEntity<>(null, HttpStatus.CREATED);
    }

    @PatchMapping("/borrowed/{id}/return")
    @RolesAllowed("ADMIN")
    public ReturnedBookResponseDto returnBook(@PathVariable @UUID String id,
            @RequestBody ReturnBookRequestDto request) {
        // TODO implement!
        return null;
    }

    @GetMapping("/borrowed/{id}")
    @RolesAllowed("ADMIN")
    public BorrowedBookResponseDto getBorrowedBookInfo(@PathVariable @UUID String id) {
        // TODO implement!
        return null;
    }
}
