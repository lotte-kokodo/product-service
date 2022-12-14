package shop.kokodo.productservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import shop.kokodo.productservice.exception.ExceptionMessage;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductDto {

    long id;
    long categoryId;
    String categoryName;
    String name;
    private int price;
    private String displayName;
    private int stock;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
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
