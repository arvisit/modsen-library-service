package by.arvisit.modsenlibapp.libraryservice.persistence.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import by.arvisit.modsenlibapp.libraryservice.persistence.model.Book;

public interface BookRepository extends JpaRepository<Book, UUID> {

    @Query("select b from Book b where b not in (select b from Book b join b.records r where r.borrowedDate is not null and r.returnedDate is null)")
    List<Book> findAvailableBooks();

    @Query("select b from Book b join b.records r where r.borrowedDate is not null and r.returnedDate is null")
    List<Book> findBorrowedBooks();

    @Query("select count(b) > 0 from Book b where b.id = :id and b not in (select b from Book b join b.records r where r.borrowedDate is not null and r.returnedDate is null)")
    boolean isAvailable(@Param("id") UUID id);
}
