package shop.kokodo.productservice.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import shop.kokodo.productservice.entity.Product;
import shop.kokodo.productservice.entity.QProduct;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class ProductCustomRepositoryImpl implements ProductCustomRepository{

    private final JPAQueryFactory jpaQueryFactory;

    private static QProduct product=QProduct.product;

    public ProductCustomRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }


    @Override
    public List<Product> findProduct(String name, Integer status, LocalDateTime startDate, LocalDateTime endDate) {

        product = QProduct.product;
        return jpaQueryFactory.selectFrom(product)
                .where(eqName(name), eqStatus(status),eqDate(startDate, endDate))
                .fetchAll().fetch();

    }

    private BooleanExpression eqName(String name){
        if(StringUtils.isEmpty(name)) return null;

        return product.name.contains(name);
    }

    private BooleanExpression eqStatus(Integer status){
        if(status==null || status==0) return null; // status가 오지 않거나 0이면 전체 조회 -> 조건 걸지 않는다
        else if(status==1) return product.stock.gt(1);
        else if(status==2) return product.stock.eq(0);
        else return null;
    }

    private BooleanExpression eqRegDate(LocalDateTime regDate){
        if(regDate==null) return null;

        return product.lastModifiedDate.eq(regDate);
    }

    private BooleanExpression eqDate(LocalDateTime startDate, LocalDateTime endDate){
        if(startDate==null && endDate==null) return null;
        else if(startDate!=null && endDate==null) return product.lastModifiedDate.goe(startDate);
        else if(startDate==null && endDate!=null) return product.lastModifiedDate.loe(endDate);
        else return product.lastModifiedDate.goe(startDate).and( product.lastModifiedDate.loe(endDate));
    }
}
