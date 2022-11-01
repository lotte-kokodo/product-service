package shop.kokodo.productservice.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class ProductDetailDto {

    private long id;
    private String image;
    private int orders;

    @Builder
    public ProductDetailDto(long id, String image, int orders) {
        this.id = id;
        this.image = image;
        this.orders = orders;
    }
}
