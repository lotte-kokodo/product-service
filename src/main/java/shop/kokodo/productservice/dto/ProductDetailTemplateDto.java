package shop.kokodo.productservice.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ProductDetailTemplateDto {

    long categoryId;
    private String name;
    private int price;
    private String displayName;
    private int stock;
    private LocalDateTime deadline;
    private String thumbnail;
    private long sellerId;
    private int deliveryFee;
    private TemplateDto templateDto;

    @Builder
    public ProductDetailTemplateDto(long categoryId, String name, int price, String displayName, int stock,
                                LocalDateTime deadline, String thumbnail, long sellerId, int deliveryFee, TemplateDto templateDto) {
        this.categoryId = categoryId;
        this.name = name;
        this.price = price;
        this.displayName = displayName;
        this.stock = stock;
        this.deadline = deadline;
        this.thumbnail = thumbnail;
        this.sellerId = sellerId;
        this.deliveryFee = deliveryFee;
        this.templateDto = templateDto;
    }
}
