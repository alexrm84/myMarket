package alexrm84.services;


import alexrm84.entities.Product;
import alexrm84.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@Transactional
public class ProductService {

    private ProductRepository productRepository;

    @Autowired
    public void setProductRepository(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public void flush() {
        productRepository.flush();
    }

    public <S extends Product> S save(S s) {
        return productRepository.save(s);
    }

    public void deleteById(Long s) {
        productRepository.deleteById(s);
    }

    public Product findByTitle(String s) {
        return productRepository.findOneByTitle(s);
    }

    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }

    public Page<Product> findAllByPagingAndFiltering(Specification<Product> specification, Pageable pageable) {
        return productRepository.findAll(specification, pageable);
    }

    public List<Product> findAllById(Iterable<Long> iterable) {
        return productRepository.findAllById(iterable);
    }

    public List<Product> findAllByCategoryId(Long id) {
        return productRepository.findAllByCategory_id(id);
    }
}
