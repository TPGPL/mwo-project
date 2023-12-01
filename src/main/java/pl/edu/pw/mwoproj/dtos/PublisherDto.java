package pl.edu.pw.mwoproj.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PublisherDto {
    private int id;
    private String name;
    @Builder.Default
    private List<BookDto> books = new ArrayList<>();
}
