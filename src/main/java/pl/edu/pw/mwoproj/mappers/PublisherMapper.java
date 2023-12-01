package pl.edu.pw.mwoproj.mappers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.edu.pw.mwoproj.dtos.PublisherDto;
import pl.edu.pw.mwoproj.models.Publisher;

@Component
public class PublisherMapper implements EntityMapper<Publisher, PublisherDto> {
    private final BookMapper bookMapper;

    @Autowired
    public PublisherMapper(BookMapper bookMapper) {
        this.bookMapper = bookMapper;
    }

    @Override
    public PublisherDto mapToDto(Publisher entity) {
        if (entity == null) return null;

        PublisherDto dto = PublisherDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .build();

        for (var book : entity.getBooks()) {
            dto.getBooks().add(bookMapper.mapToDto(book));
        }

        return dto;
    }

    @Override
    public Publisher mapToEntity(PublisherDto dto) {
        if (dto == null) return null;

        return Publisher.builder()
                .name(dto.getName())
                .build();
    }
}
