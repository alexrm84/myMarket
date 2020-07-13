package alexrm84.controllers;

import alexrm84.entities.Product;
import alexrm84.errorHandlers.ProductException;
import alexrm84.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
public class ProductRestController {

    private ProductService productService;

    @Autowired
    public void setProductService(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("")
    public List<Product> findAll(){
        return productService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> findOne(@PathVariable(name = "id") Long id){
        if (productService.findById(id) == null) {
            throw new ProductException("Product with id: " + id + " not found");
        }
        return new ResponseEntity<>(productService.findById(id).get(), HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<Product> addProduct(@RequestBody Product product){
        if (product.getId() != null){
            throw new ProductException("Product must not contain id");
        }
        return new ResponseEntity<>(productService.save(product), HttpStatus.CREATED);
    }

    @PutMapping("")
    public ResponseEntity<Product> updateProduct(@RequestBody Product newProduct){
        if (productService.findById(newProduct.getId()) == null){
            throw new ProductException("Product with id: " + newProduct.getId() + " not found");
        }
        return new ResponseEntity<>(productService.save(newProduct), HttpStatus.OK);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable(name = "id") Long id){
        if (productService.findById(id) == null) {
            throw new ProductException("Product with id: " + id + " not found");
        }
        productService.deleteById(id);
        return new ResponseEntity(HttpStatus.OK);
    }
}
