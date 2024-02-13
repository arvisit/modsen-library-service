package by.arvisit.modsenlibapp.libraryservice.service.impl;

import java.util.UUID;

import org.springframework.stereotype.Service;

import by.arvisit.modsenlibapp.libraryservice.persistence.repository.BookRepository;
import by.arvisit.modsenlibapp.libraryservice.service.BookService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    @Override
    public boolean isBookExist(String id) {
        return bookRepository.existsById(UUID.fromString(id));
    }

}
