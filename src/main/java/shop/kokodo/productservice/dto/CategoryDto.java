package shop.kokodo.productservice.dto;

import lombok.*;

@Data
@NoArgsConstructor
public class CategoryDto {

    long id;
    String name;

    @Builder
    public CategoryDto(long id, String name) {
        this.id = id;
        this.name = name;
    }
}
