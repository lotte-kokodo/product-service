package shop.kokodo.productservice.entity;

import lombok.*;
import net.bytebuddy.implementation.bind.annotation.BindingPriority;
import org.springframework.stereotype.Service;

import javax.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Product extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private long id;

    @ManyToOne(fetch = LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "product")
    private List<ProductDetail> productDetailList;

    @OneToOne(mappedBy = "product", fetch = LAZY)
    private TemplateRec templateRec;

    @OneToMany(mappedBy = "product")
    private List<Review> reviewList = new ArrayList<>();

    @OneToMany(mappedBy = "product")
    private List<ProductInquire> productInquireList = new ArrayList<>();

    private String name;
    private int price;
    private String displayName;
    private int stock;
    private LocalDateTime deadline;
    private String thumbnail;
    private long sellerId;
    private int deliveryFee;

    //== 연관관계 메서드 ==//
    public void setCategory(Category category) {
        this.category = category;
        category.getProductList().add(this);
    }

    @Builder
    public Product(long id, Category category, String name, int price, String displayName, int stock, LocalDateTime deadline, String thumbnail, long sellerId, int deliveryFee) {
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
    }
}
