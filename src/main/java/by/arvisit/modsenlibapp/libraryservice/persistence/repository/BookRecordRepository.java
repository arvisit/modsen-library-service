package by.arvisit.modsenlibapp.libraryservice.persistence.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import by.arvisit.modsenlibapp.libraryservice.persistence.model.BookRecord;


public interface BookRecordRepository extends JpaRepository<BookRecord, Long> {

    Optional<BookRecord> findByBookIdAndBorrowedDateIsNotNullAndReturnedDateIsNull(UUID bookId);
}
