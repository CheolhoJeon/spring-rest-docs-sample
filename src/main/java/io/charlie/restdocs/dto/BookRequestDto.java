package io.charlie.restdocs.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public interface BookRequestDto {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    class Register {
        private String title;
        private String author;
        private Long price;
        private String description;
    }

}


