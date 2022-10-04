package shop.kokodo.productservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Getter
@Entity
@ToString
@NoArgsConstructor
public class Review extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private long id;

    @ManyToOne(fetch = LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "product_id")
    @JsonIgnore
    private Product product;

    @Column( nullable = false )
    private String content;
    @Column( nullable = false )
    private double rating;

    @Column
    private long memberId;

    @Builder
    public Review(long id, Product product, String content, double rating, long memberId) {
        this.id = id;
        this.product = product;
        this.content = content;
        this.rating = rating;
        this.memberId = memberId;
    }
}
