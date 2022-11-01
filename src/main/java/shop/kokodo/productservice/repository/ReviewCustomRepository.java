package shop.kokodo.productservice.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import shop.kokodo.productservice.entity.Review;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.List;

@Repository
public class ReviewCustomRepository {

    private final EntityManager em;

    public ReviewCustomRepository(EntityManager em) {
        this.em = em;
    }

    public List<Review> findByProductIdPaging(long productId, Pageable pageable){
        List<Review> reviewList = em.createQuery("select r from Review r where r.product.id = ? order by r.id desc ",Review.class)
                .setParameter(1,productId)
                .setFirstResult(pageable.getPageNumber())
                .setMaxResults(pageable.getPageSize())
                .getResultList();

        return reviewList;
    }


}
