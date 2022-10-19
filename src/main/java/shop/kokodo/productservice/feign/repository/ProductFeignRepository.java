package shop.kokodo.productservice.feign.repository;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import shop.kokodo.productservice.entity.Product;

@Repository
public interface ProductFeignRepository extends CrudRepository<Product, Long> {

    <T> T findById(Long id, Class<T> type);

    <T> List<T> findByIdIn(List<Long> ids, Class<T> type);


}
