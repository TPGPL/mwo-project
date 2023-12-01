package pl.edu.pw.mwoproj.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Publishers")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@NotNull(message = "The publisher must not be null.")
public class Publisher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Size(min = 2, max = 50, message = "The publisher name must be between 2 and 50 characters.")
    @NotNull(message = "The publisher name must not be null.")
    private String name;
    @OneToMany(mappedBy = "publisher", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @NotNull(message = "The publisher books must not be null.")
    private List<Book> books = new ArrayList<>();
}
