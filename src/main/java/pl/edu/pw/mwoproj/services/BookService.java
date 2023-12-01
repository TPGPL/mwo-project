package pl.edu.pw.mwoproj.services;

import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.pw.mwoproj.models.Book;
import pl.edu.pw.mwoproj.repositories.BookRepository;
import pl.edu.pw.mwoproj.utils.ServiceResponse;

@Service
public class BookService {
    private final BookRepository repository;
    private final Validator validator;

    @Autowired
    public BookService(BookRepository repository, Validator validator) {
        this.validator = validator;
        this.repository = repository;
    }

    public Book getById(int id) {
        return repository.findById(id).orElse(null);
    }

    public Iterable<Book> getAll() {
        return repository.findAll();
    }

    public ServiceResponse<Book> create(Book book) {
        var violations = validator.validate(book);

        if (!violations.isEmpty()) {
            return ServiceResponse.createInvalidResponse(violations);
        }

        return ServiceResponse.<Book>builder()
                .wasSuccessful(true)
                .data(repository.save(book))
                .build();
    }

    public ServiceResponse<Book> update(int id, Book book) {
        var bookToUpdate = getById(id);

        if (bookToUpdate == null) {
            return ServiceResponse.<Book>builder()
                    .message("The book with given ID was not found.")
                    .build();
        }

        bookToUpdate.setAuthor(book.getAuthor());
        bookToUpdate.setPublisher(book.getPublisher());
        bookToUpdate.setIsbn(book.getIsbn());
        bookToUpdate.setTitle(book.getTitle());
        bookToUpdate.setReleaseDate(book.getReleaseDate());
        bookToUpdate.setPageCount(book.getPageCount());

        var violations = validator.validate(bookToUpdate);

        if (!violations.isEmpty()) {
            return ServiceResponse.createInvalidResponse(violations);
        }

        return ServiceResponse.<Book>builder()
                .wasSuccessful(true)
                .data(repository.save(bookToUpdate))
                .build();
    }

    public ServiceResponse<Void> delete(int id) {
        var response = ServiceResponse.<Void>builder().wasSuccessful(true);

        if (!repository.existsById(id)) {
            return response.message("No book to delete.").build();
        }

        repository.deleteById(id);

        return response.message("The book was deleted successfully.").build();
    }
}
