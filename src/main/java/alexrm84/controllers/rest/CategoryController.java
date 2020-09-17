package alexrm84.controllers.rest;

import alexrm84.entities.Category;
import alexrm84.errorHandlers.CategoryException;
import alexrm84.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/category")
public class CategoryController {

    private CategoryService categoryService;

    @Autowired
    public void setCategoryService(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("")
    public ResponseEntity<List<Category>> getCategories(){
        List<Category> categories = categoryService.findAll();
        if (categories.isEmpty() || categories == null) {
            throw new CategoryException("Categories not found");
        }
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }
}
