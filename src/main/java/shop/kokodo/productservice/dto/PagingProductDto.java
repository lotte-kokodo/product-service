package shop.kokodo.productservice.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class PagingProductDto {
    List<ProductDto> productDtoList;
    long totalCount;

    @Builder
    public PagingProductDto(List<ProductDto> productDtoList, long totalCount) {
        this.productDtoList = productDtoList;
        this.totalCount = totalCount;
    }
}
