package pl.edu.pw.mwoproj.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookDto {
    private int id;
    private String title;
    private int authorId;
    private int publisherId;
    private int pageCount;
    private Date releaseDate;
    private String isbn;
}
