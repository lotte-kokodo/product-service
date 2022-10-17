package shop.kokodo.productservice.dto;

import lombok.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@ToString
public class ProductDto {

    long id;
    long categoryId;
    String categoryName;
    private String name;
    private int price;
    private String displayName;
    private int stock;
    private LocalDateTime deadline;
    private String thumbnail;
    private long sellerId;
    private int deliveryFee;

    public ProductDto(long id, long categoryId, String name, int price, String displayName, int stock, LocalDateTime deadline, String thumbnail, long sellerId, int deliveryFee) {
        this.id = id;
        this.categoryId = categoryId;
        this.name = name;
        this.price = price;
        this.displayName = displayName;
        this.stock = stock;
        this.deadline = deadline;
        this.thumbnail = thumbnail;
        this.sellerId = sellerId;
        this.deliveryFee = deliveryFee;
    }

    @Builder
    public ProductDto(long id, long categoryId, String name, int price, String displayName, int stock, LocalDateTime deadline, String thumbnail, long sellerId, int deliveryFee,
                      String categoryName) {
        this.id = id;
        this.categoryId = categoryId;
        this.name = name;
        this.price = price;
        this.displayName = displayName;
        this.stock = stock;
        this.deadline = deadline;
        this.thumbnail = thumbnail;
        this.sellerId = sellerId;
        this.deliveryFee = deliveryFee;
        this.categoryName=categoryName;
    }
}
