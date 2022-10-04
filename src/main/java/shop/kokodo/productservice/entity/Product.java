package shop.kokodo.productservice.entity;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.kokodo.productservice.exception.ExceptionMessage;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    private String name;
    private String displayName;

    private Integer price;
    private Integer stock;

    private LocalDateTime deadline;
    private String thumbnail;

    private Long sellerId;

    private Integer deliveryFee;

    @OneToOne(mappedBy = "product")
    private ProductDetail productDetail;


    /*
    * 주문 시 상품 재고 수정
    * */
    public void decreaseStock(Integer qty) {
        if (stock - qty < 0) {
            throw new IllegalStateException(ExceptionMessage.NOT_ENOUGH_STOCK);
        }
    }

    public void increaseStock(Integer qty) {
        stock -= qty;
    }
}
