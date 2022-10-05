package shop.kokodo.productservice.repository;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import shop.kokodo.productservice.entity.Product;

@Repository
public interface ProductRepository extends CrudRepository<Product, Long> {

    List<Product> findByIdIn(List<Long> ids);

}
