package pl.edu.pw.mwoproj.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Authors")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@NotNull(message = "The author must not be null.")
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Size(min = 2, max = 50, message = "The author name must be between 2 and 50 characters.")
    @NotNull(message = "The author name must not be null..")
    private String name;
    @Size(min = 2, max = 50, message = "The author surname must be between 2 and 50 characters.")
    @NotNull(message = "The author surname must not be null.")
    private String surname;
    @Size(min = 7, max = 50, message = "The author email must be between 7 and 50 characters.")
    @NotNull(message = "The author email must not be null.")
    @Email(message = "The author email must be in valid format.")
    @Column(nullable = false, unique = true)
    private String email;
    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    @NotNull(message = "The author books must not be null.")
    @Builder.Default
    private List<Book> books = new ArrayList<>();
}
