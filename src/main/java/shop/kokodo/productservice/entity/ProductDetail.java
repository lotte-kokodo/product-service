package shop.kokodo.productservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;

import java.io.Serializable;

import static javax.persistence.FetchType.LAZY;

@Getter
@Entity
@NoArgsConstructor
public class ProductDetail extends BaseEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_detail_id")
    private long id;

    @ManyToOne(fetch = LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "product_id")
    @JsonIgnore
    private Product product;

    private String image;
    private int orders;

    public void changeProduct(Product product){
        this.product=product;
        product.getProductDetailList().add(this);
    }

    @Builder
    public ProductDetail(long id, Product product, String image, int orders) {
        this.id = id;
        this.product = product;
        this.image = image;
        this.orders = orders;
    }
}
