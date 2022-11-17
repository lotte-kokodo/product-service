package shop.kokodo.productservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import net.bytebuddy.implementation.bind.annotation.BindingPriority;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.stereotype.Service;

import javax.persistence.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import shop.kokodo.productservice.exception.ExceptionMessage;

import static javax.persistence.FetchType.EAGER;
import static javax.persistence.FetchType.LAZY;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "product")
public class Product extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private long id;

    @ManyToOne(fetch = LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "category_id")
    @JsonIgnore
    @Column(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "product", cascade = CascadeType.PERSIST)
    @Column(name = "product_detail_list")
    private List<ProductDetail> productDetailList = new ArrayList<>();

    @OneToOne(fetch = LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "template_rec_id")
    @Column(name = "template_rec")
    private TemplateRec templateRec;

    @Column(nullable = false, name = "name")
    private String name;

    @Column(nullable = false, name = "price")
    private int price;

    @Column(nullable = false, name = "displayName")
    private String displayName;

    @Column(nullable = false, name = "stock")
    private int stock;

    @Column(nullable = false, name = "dead_line")
    private LocalDateTime deadline;

    @Column(nullable = false)
    private String thumbnail;

    @Column(nullable = false, name = "seller_id")
    private long sellerId;

    @ColumnDefault(value = "3000")
    @Column(name="delivery_fee")
    private int deliveryFee;

    @Enumerated(EnumType.STRING)
    @Column(name = "detail_flag")
    private DetailFlag detailFlag;

    //== 연관관계 메서드 ==//
    public void setCategory(Category category) {
        this.category = category;
        category.getProductList().add(this);
    }

    public void addProductDetail(ProductDetail productDetail) {
        productDetailList.add(productDetail);
    }

    /*
        setter
     */
    public void changeProductDetail(List<ProductDetail> productDetailList){
        this.productDetailList = productDetailList;
    }
    public void changeTemplateRec(TemplateRec templateRec){
        this.templateRec = templateRec;
    }


    @Builder
    public Product(long id, Category category, String name, int price, String displayName,
        int stock, LocalDateTime deadline, String thumbnail, long sellerId, int deliveryFee, TemplateRec templateRec,
                   DetailFlag detailFlag) {
        this.id = id;
        setCategory(category);
        this.name = name;
        this.price = price;
        this.displayName = displayName;
        this.stock = stock;
        this.deadline = deadline;
        this.thumbnail = thumbnail;
        this.sellerId = sellerId;
        this.deliveryFee = deliveryFee;
        this.templateRec = templateRec;
        this.detailFlag = detailFlag;
    }


    /*
     * 주문 시 상품 재고 수정
     * */
    public void decreaseStock(Integer qty) {
        if (stock - qty < 0) {
            throw new IllegalStateException(ExceptionMessage.createProductNotEnoughStockMsg(id));
        }
        stock -= qty;
    }

    public void increaseStock(Integer qty) {
        stock += qty;
    }
}
