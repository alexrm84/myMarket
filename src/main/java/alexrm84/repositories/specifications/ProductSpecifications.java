package alexrm84.repositories.specifications;


import alexrm84.entities.Category;
import alexrm84.entities.Product;
import alexrm84.entities.User;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.ListJoin;
import java.util.List;

public class ProductSpecifications {
    public static Specification<Product> titleContains(String word) {
        return (Specification<Product>) (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.like(root.get("title"), "%" + word + "%");
    }

    public static Specification<Product> priceGreaterThanOrEq(double value) {
        return (Specification<Product>) (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.greaterThanOrEqualTo(root.get("price"), value);
    }

    public static Specification<Product> priceLesserThanOrEq(double value) {
        return (Specification<Product>) (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.lessThanOrEqualTo(root.get("price"), value);
    }

    public static Specification<Product> categoryId(Long id) {
        return (Specification<Product>) (root, criteriaQuery, criteriaBuilder) ->
            criteriaBuilder.equal(root.get("category").get("id"), id);

    }

//    public static Specification<Product> categoriesContains(String word) {
//        return (Specification<Product>) (root, criteriaQuery, criteriaBuilder) -> {
//            Join<Product, Category> categories = root.join("categories");
//            return criteriaBuilder.equal(categories.get("title"), word);
//        };
//    }
}
