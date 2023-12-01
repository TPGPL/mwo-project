package pl.edu.pw.mwoproj.dtos;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthorDto {
    private int id;
    private String name;
    private String surname;
    private String email;
    @Builder.Default
    private List<BookDto> books = new ArrayList<>();
}
