package by.arvisit.modsenlibapp.libraryservice.service.impl;

import java.text.MessageFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import by.arvisit.modsenlibapp.libraryservice.dto.LibraryBookDto;
import by.arvisit.modsenlibapp.libraryservice.dto.BorrowedBookResponseDto;
import by.arvisit.modsenlibapp.libraryservice.dto.ReturnedBookResponseDto;
import by.arvisit.modsenlibapp.libraryservice.mapper.BookMapper;
import by.arvisit.modsenlibapp.libraryservice.mapper.BookRecordMapper;
import by.arvisit.modsenlibapp.libraryservice.persistence.model.Book;
import by.arvisit.modsenlibapp.libraryservice.persistence.model.BookRecord;
import by.arvisit.modsenlibapp.libraryservice.persistence.repository.BookRecordRepository;
import by.arvisit.modsenlibapp.libraryservice.persistence.repository.BookRepository;
import by.arvisit.modsenlibapp.libraryservice.service.LibraryService;
import by.arvisit.modsenlibapp.libraryservice.util.AuthService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class LibraryServiceImpl implements LibraryService {

    private final BookRepository bookRepository;
    private final BookRecordRepository bookRecordRepository;
    private final BookRecordMapper bookRecordMapper;
    private final BookMapper bookMapper;
    private final AuthService authService;

    @Transactional
    @Override
    public LibraryBookDto addNewBook(String id) {
        log.debug("Call for LibraryService.addNewBook() with id {}", id);
        Book savedBook = bookRepository.save(Book.builder()
                .withId(UUID.fromString(id))
                .build());
        return bookMapper.fromEntityToDto(savedBook);
    }

    @Transactional(readOnly = true)
    @Override
    public List<LibraryBookDto> getAvailableBooks() {
        log.debug("Call for LibraryService.getAvailableBooks()");
        return bookRepository.findAvailableBooks().stream()
                .map(bookMapper::fromEntityToDto)
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public List<LibraryBookDto> getBorrowedBooks() {
        log.debug("Call for LibraryService.getBorrowedBooks()");
        return bookRepository.findBorrowedBooks().stream()
                .map(bookMapper::fromEntityToDto)
                .toList();
    }

    @Transactional
    @Override
    public BorrowedBookResponseDto borrowBook(String id) {
        log.debug("Call for LibraryService.borrowBook() with id {}", id);
        if (!bookRepository.isAvailable(UUID.fromString(id))) {
            throw new EntityNotFoundException(MessageFormat.format("Book with id {0} is not available", id));
        }
        LocalDate borrowedDate = LocalDate.now();
        BookRecord borrowedRecord = BookRecord.builder()
                .withBookId(UUID.fromString(id))
                .withUsername(authService.getUsername())
                .withBorrowedDate(borrowedDate)
                .withReturnDueDate(borrowedDate.plusMonths(1L))
                .build();
        BookRecord saved = bookRecordRepository.save(borrowedRecord);
        return bookRecordMapper.fromEntityToBorrowedBookDto(saved);
    }

    @Transactional
    @Override
    public ReturnedBookResponseDto returnBook(String id) {
        log.debug("Call for LibraryService.returnBook() with id {}", id);
        BookRecord borrowedBook  = bookRecordRepository.findByBookIdAndBorrowedDateIsNotNullAndReturnedDateIsNull(UUID.fromString(id))
                .orElseThrow(() -> new EntityNotFoundException(MessageFormat.format("Book with id {0} is not borrowed", id)));
        LocalDate returnedDate = LocalDate.now();
        LocalDate borrowedDate = borrowedBook.getBorrowedDate();
        if (!returnedDate.isAfter(borrowedDate)) {
            throw new IllegalArgumentException("Returned date could not be before borrowed date");
        }
        borrowedBook.setReturnedDate(returnedDate);
        BookRecord saved = bookRecordRepository.save(borrowedBook);
        return bookRecordMapper.fromEntityToReturnedBookDto(saved);
    }

    @Transactional(readOnly = true)
    @Override
    public BorrowedBookResponseDto getBorrowedBookInfo(String id) {
        log.debug("Call for LibraryService.getBorrowedBookInfo() with id {}", id);
        BookRecord borrowedBook  = bookRecordRepository.findByBookIdAndBorrowedDateIsNotNullAndReturnedDateIsNull(UUID.fromString(id))
                .orElseThrow(() -> new EntityNotFoundException(MessageFormat.format("Book with id {0} is not borrowed", id)));
        return bookRecordMapper.fromEntityToBorrowedBookDto(borrowedBook);
    }

}
