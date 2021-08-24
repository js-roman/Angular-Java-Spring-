package ru.javabegins.springboot.business.contoller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.javabegins.springboot.business.entity.Task;
import ru.javabegins.springboot.service.TaskService;
import ru.javabegins.springboot.util.MyLogger;

import java.util.List;

@RestController
@RequestMapping("/task")
public class TaskController {
    private TaskService taskService;

    @Autowired
    public TaskController(@RequestBody TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping("/all")
    public List<Task> findAll(@RequestBody String email) {
        MyLogger.logInfo("findAll(" + email + ")");
        return taskService.findAll(email);
    }

    @PutMapping("/add")
    public ResponseEntity<Task> add(@RequestBody Task task) {
        MyLogger.logInfo("PriorityController:add()----------------------------------------");
        if (task.getId() != null && task.getId() != 0)
            return new ResponseEntity("redundand param: id must be null", HttpStatus.NOT_ACCEPTABLE);
        if (task.getTitle() == null || task.getTitle().trim().length() == 0)
            return new ResponseEntity("missed param: title", HttpStatus.NOT_ACCEPTABLE);

        return ResponseEntity.ok(taskService.add(task));
    }

    @PatchMapping("/update")
    public ResponseEntity<Task> update(@RequestBody Task task) {

        if (task.getId() == null || task.getId() == 0) {
            return new ResponseEntity("missed param: id", HttpStatus.NOT_ACCEPTABLE);
        }

        if (task.getTitle() == null || task.getTitle().trim().length() == 0) {
            return new ResponseEntity("missed param: title", HttpStatus.NOT_ACCEPTABLE);
        }


       taskService.update(task);
        return new ResponseEntity(HttpStatus.OK);

    }


    @DeleteMapping("delete")
    public ResponseEntity<Task> delete(@RequestBody Long id) {
        try {
            taskService.delete(id);
        } catch (EmptyResultDataAccessException e) {
            return new ResponseEntity("id not found", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    public Task findById(@RequestBody Long id) {
        return taskService.findById(id);
    }

}
