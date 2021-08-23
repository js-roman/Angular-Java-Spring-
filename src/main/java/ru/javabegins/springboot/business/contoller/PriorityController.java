package ru.javabegins.springboot.business.contoller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.javabegins.springboot.business.entity.Priority;
import ru.javabegins.springboot.business.search.PrioritySearchValues;
import ru.javabegins.springboot.service.PriorityService;
import ru.javabegins.springboot.service.PriorityService;
import ru.javabegins.springboot.util.MyLogger;

import java.util.List;

@RestController
@RequestMapping("/priority")
public class PriorityController {
    private PriorityService priorityService;

    @Autowired
    public PriorityController(PriorityService priorityService) {
        this.priorityService = priorityService;
    }

    @PostMapping("/all")
    public List<Priority> findAll(@RequestBody String email) {
        MyLogger.logInfo("findAll(" + email + ")");
        return priorityService.findAll(email);
    }

    @PutMapping("/add")
    public ResponseEntity<Priority> add(@RequestBody Priority priority) {
        MyLogger.logInfo("PriorityController:add()----------------------------------------");
        if (priority.getId() != null && priority.getId() != 0)
            return new ResponseEntity("redundand param: id must be null", HttpStatus.NOT_ACCEPTABLE);
        if (priority.getTitle() == null || priority.getTitle().trim().length() == 0)
            return new ResponseEntity("missed param: title", HttpStatus.NOT_ACCEPTABLE);
        if (priority.getColor() == null || priority.getColor().trim().length() == 0)
            return new ResponseEntity("missed param: color", HttpStatus.NOT_ACCEPTABLE);
        return ResponseEntity.ok(priorityService.add(priority));
    }

    @PatchMapping("/patch")
    public ResponseEntity<Priority> patch(@RequestBody Priority priority) {
        MyLogger.logInfo("PriorityController:patch()----------------------------------------");
        if (priority.getId() == null || priority.getId() == 0)
            return new ResponseEntity("redundand param: id cant be null", HttpStatus.NOT_ACCEPTABLE);
        if (priority.getTitle() == null || priority.getTitle().trim().length() == 0)
            return new ResponseEntity("missed param: title", HttpStatus.NOT_ACCEPTABLE);
        if (priority.getColor() == null || priority.getColor().trim().length() == 0)
            return new ResponseEntity("missed param: color", HttpStatus.NOT_ACCEPTABLE);
        return ResponseEntity.ok(priorityService.add(priority));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Priority> delete(@RequestBody Long id) {
        MyLogger.logInfo("PriorityController:delete()----------------------------------------");
        if (id == null || id == 0)
            return new ResponseEntity("redundand param: id cant be null", HttpStatus.NOT_ACCEPTABLE);
        try {
            priorityService.delete(id);
        } catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
            return new ResponseEntity("id:" + id + "not found", HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/search")
    public ResponseEntity<List<Priority>> searchByTitle(@RequestBody PrioritySearchValues prioritySearchValues) {
        MyLogger.logInfo("search: title=" + prioritySearchValues.getTitle() + ", email=" + prioritySearchValues.getEmail());
        List<Priority> list = priorityService.search(prioritySearchValues.getTitle(), prioritySearchValues.getEmail());
        return ResponseEntity.ok(list);

    }

    @PostMapping("/id")
    public ResponseEntity findById(@RequestBody Long id){
        MyLogger.logInfo("findById: "+id);
        try{
            return new ResponseEntity(priorityService.findById(id), HttpStatus.OK);
        }
        catch (Exception e){
            System.out.println(e);
            return new ResponseEntity("id not found", HttpStatus.OK);
        }

    }

}
