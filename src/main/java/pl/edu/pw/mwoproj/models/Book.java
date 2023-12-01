package pl.edu.pw.mwoproj.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.sql.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "Books")
@NotNull(message = "The Book must not be null.")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Size(min = 2, max = 100, message = "The book title must be between 2 and 100 characters.")
    @NotNull(message = "The book title must not be null.")
    private String title;
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "author_id", referencedColumnName = "id")
    @NotNull(message = "The book author must not be null.")
    private Author author;
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "publisher_id", referencedColumnName = "id")
    @NotNull(message = "The book publisher must not be null.")
    private Publisher publisher;
    @Positive(message = "The book page count must be positive.")
    @Column(name = "page_count")
    private int pageCount;
    @NotNull(message = "The book release date must not be null.")
    @Column(name = "release_date")
    private Date releaseDate;
    @NotNull(message = "The book ISBN must not be null.")
    @Pattern(regexp = "^[0-9]{13}$", message = "The book ISBN must contain 13 digits.")
    private String isbn;
}
