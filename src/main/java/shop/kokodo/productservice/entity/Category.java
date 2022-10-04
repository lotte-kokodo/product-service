package shop.kokodo.productservice.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Category extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private long id;
    private String name;

    @OneToMany(mappedBy = "category", orphanRemoval = true)
    private List<Product> productList = new ArrayList<>();

    @Builder
    public Category(long id, String name) {
        this.id = id;
        this.name = name;
    }
}
