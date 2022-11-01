package shop.kokodo.productservice.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter
@NoArgsConstructor
public class ProductAndProductDetailDto {
    private long id;
    private long categoryId;
    private List<ProductDetailDto> productDetailList;
    private String name;
    private int price;
    private String displayName;
    private int stock;
    private String deadline;
    private String thumbnail;
    private long sellerId;
    private int deliveryFee;

    @Builder
    public ProductAndProductDetailDto(long id, long categoryId, List<ProductDetailDto> productDetailList, String name, int price, String displayName, int stock, LocalDateTime deadline, String thumbnail, long sellerId, int deliveryFee) {
        this.id = id;
        this.categoryId = categoryId;
        this.productDetailList = productDetailList;
        this.name = name;
        this.price = price;
        this.displayName = displayName;
        this.stock = stock;
        this.deadline = deadline.toString();
        this.thumbnail = thumbnail;
        this.sellerId = sellerId;
        this.deliveryFee = deliveryFee;
    }
}
