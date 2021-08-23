package ru.javabegins.springboot.business.contoller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.javabegins.springboot.business.entity.Category;
import ru.javabegins.springboot.business.search.CategorySearchValues;
import ru.javabegins.springboot.service.CategoryService;
import ru.javabegins.springboot.util.MyLogger;

import java.util.List;

@RestController
@RequestMapping("/category")

public class CategoryController {

    private CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping("/all")
    public List<Category> findAll(@RequestBody String email) {
        MyLogger.logInfo("findAll(" + email + ")");
        return categoryService.findAll(email);
    }

    @PutMapping("/add")
    public ResponseEntity<Category> add(@RequestBody Category category) {
        MyLogger.logInfo("CategoryController:add()----------------------------------------");
        if (category.getId() != null && category.getId() != 0)
            return new ResponseEntity("redundand param: id must be null", HttpStatus.NOT_ACCEPTABLE);
        if (category.getTitle() == null || category.getTitle().trim().length() == 0)
            return new ResponseEntity("missed param: title", HttpStatus.NOT_ACCEPTABLE);
        return ResponseEntity.ok(categoryService.add(category));
    }

    @PatchMapping("/patch")
    public ResponseEntity<Category> patch(@RequestBody Category category) {
        MyLogger.logInfo("CategoryController:patch()----------------------------------------");
        if (category.getId() == null || category.getId() == 0)
            return new ResponseEntity("redundand param: id cant be null", HttpStatus.NOT_ACCEPTABLE);
        if (category.getTitle() == null || category.getTitle().trim().length() == 0)
            return new ResponseEntity("missed param: title", HttpStatus.NOT_ACCEPTABLE);
        return ResponseEntity.ok(categoryService.add(category));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Category> delete(@RequestBody Long id) {
        MyLogger.logInfo("CategoryController:delete()----------------------------------------");
        if (id == null || id == 0)
            return new ResponseEntity("redundand param: id cant be null", HttpStatus.NOT_ACCEPTABLE);
        try {
            categoryService.delete(id);
        } catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
            return new ResponseEntity("id:" + id + "not found", HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/search")
    public ResponseEntity<List<Category>> searchByTitle(@RequestBody CategorySearchValues categorySearchValues) {
        MyLogger.logInfo("search: title=" + categorySearchValues.getTitle() + ", email=" + categorySearchValues.getEmail());
        List<Category> list = categoryService.search(categorySearchValues.getTitle(), categorySearchValues.getEmail());
        return ResponseEntity.ok(list);

    }

    @PostMapping("/id")
    public ResponseEntity findById(@RequestBody Long id){
        MyLogger.logInfo("findById: "+id);
        try{
            return new ResponseEntity(categoryService.findById(id), HttpStatus.OK);
        }
        catch (Exception e){
            System.out.println(e);
            return new ResponseEntity("id not found", HttpStatus.OK);
        }

    }


}
//aaaa@mail.com
