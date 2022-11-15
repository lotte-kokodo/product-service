package shop.kokodo.productservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * packageName : shop.kokodo.productservice.dto
 * fileName : ProductFeignDto
 * author : BTC-N24
 * date : 2022-11-15
 * description :
 * ======================================================
 * DATE                AUTHOR                NOTE
 * ======================================================
 * 2022-11-15           BTC-N24              최초 생성
 */
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ProductFeignDto {
    private Long id;
    private String name;
    private String displayName;
    private String thumbnail;
}
