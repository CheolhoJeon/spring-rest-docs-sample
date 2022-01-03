package io.charlie.restdocs;

import io.charlie.restdocs.dto.BookResponseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Book {

    private Long id;
    private String title;
    private String author;
    private Long price;
    private String description;

    public BookResponseDto.Find toResponseFindDto() {
        return BookResponseDto.Find.builder()
            .id(id)
            .title(title)
            .author(author)
            .price(price)
            .build();
    }
}
