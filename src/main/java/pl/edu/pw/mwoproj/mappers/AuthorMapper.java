package pl.edu.pw.mwoproj.mappers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.edu.pw.mwoproj.dtos.AuthorDto;
import pl.edu.pw.mwoproj.models.Author;

@Component
public class AuthorMapper implements EntityMapper<Author, AuthorDto> {
    private final BookMapper bookMapper;

    @Autowired
    public AuthorMapper(BookMapper bookMapper) {
        this.bookMapper = bookMapper;
    }

    @Override
    public AuthorDto mapToDto(Author entity) {
        if (entity == null) return null;

        AuthorDto dto = AuthorDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .surname(entity.getSurname())
                .email(entity.getEmail())
                .build();

        for (var book : entity.getBooks()) {
            dto.getBooks().add(bookMapper.mapToDto(book));
        }

        return dto;
    }

    @Override
    public Author mapToEntity(AuthorDto dto) {
        if (dto == null) return null;

        return Author.builder()
                .name(dto.getName())
                .surname(dto.getSurname())
                .email(dto.getEmail())
                .build();
    }
}
