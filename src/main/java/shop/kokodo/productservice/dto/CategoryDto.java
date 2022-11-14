package shop.kokodo.productservice.dto;

import lombok.*;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class CategoryDto implements Serializable{

    long id;
    String name;

    @Builder
    public CategoryDto(long id, String name) {
        this.id = id;
        this.name = name;
    }
}
