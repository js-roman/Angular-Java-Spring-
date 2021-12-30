package ru.javabegins.springboot.business.contoller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.javabegins.springboot.business.entity.Task;
import ru.javabegins.springboot.business.search.TaskSearchValues;
import ru.javabegins.springboot.business.service.TaskService;
import ru.javabegins.springboot.util.MyLogger;

import java.util.Calendar;
import java.util.Date;
import java.util.List;



@RestController
@RequestMapping("/task")
public class TaskController {
    public static final String ID_COLUMN = "id"; //
    private TaskService taskService;

    @Autowired
    public TaskController(@RequestBody TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping("/all")
    public ResponseEntity<List<Task>> findAll(@RequestBody String email) {

        MyLogger.logInfo("task: findAll() ---------------------------------------------------------------- ");
List<Task> list=taskService.findAll(email);
        System.out.println(list.size()+" "+email);
        System.out.println("11111111111111111111111111111111111111");
        return ResponseEntity.ok(list); // поиск всех задач конкретного пользователя
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

    @PostMapping("/search")
    public ResponseEntity<Page<Task>> search(@RequestBody TaskSearchValues taskSearchValues)  {

        MyLogger.logInfo("task: search() ---------------------------------------------------------------- ");


        // исключить NullPointerException
        String title = taskSearchValues.getTitle() != null ? taskSearchValues.getTitle() : null;

        // конвертируем Boolean в Integer
        Integer completed = taskSearchValues.getCompleted() != null ? taskSearchValues.getCompleted() : null;

        Long priorityId = taskSearchValues.getPriorityId() != null ? taskSearchValues.getPriorityId() : null;
        Long categoryId = taskSearchValues.getCategoryId() != null ? taskSearchValues.getCategoryId() : null;

        String sortColumn = taskSearchValues.getSortColumn() != null ? taskSearchValues.getSortColumn() : null;
        String sortDirection = taskSearchValues.getSortDirection() != null ? taskSearchValues.getSortDirection() : null;

        Integer pageNumber = taskSearchValues.getPageNumber() != null ? taskSearchValues.getPageNumber() : 0;
        Integer pageSize = taskSearchValues.getPageSize() != null ? taskSearchValues.getPageSize() : 10;

        String email = taskSearchValues.getEmail() != null ? taskSearchValues.getEmail() : null; // для показа задач только этого пользователя


        // чтобы захватить в выборке все задачи по датам, независимо от времени - можно выставить время с 00:00 до 23:59

        Date dateFrom = null;
        Date dateTo = null;


        // выставить 00:00 для начальной даты (если она указана)
        if (taskSearchValues.getDateFrom() != null) {

            Calendar calendarFrom = Calendar.getInstance();
            calendarFrom.setTime(taskSearchValues.getDateFrom());
            calendarFrom.set(Calendar.HOUR_OF_DAY, 0);
            calendarFrom.set(Calendar.MINUTE, 0);
            calendarFrom.set(Calendar.SECOND, 0);
            calendarFrom.set(Calendar.MILLISECOND, 0);

            dateFrom = calendarFrom.getTime();

        }


        // выставить 23:59 для конечной даты (если она указана)
        if (taskSearchValues.getDateTo() != null) {

            Calendar calendarTo = Calendar.getInstance();
            calendarTo.setTime(taskSearchValues.getDateTo());
            calendarTo.set(Calendar.HOUR_OF_DAY, 23);
            calendarTo.set(Calendar.MINUTE, 59);
            calendarTo.set(Calendar.SECOND, 59);
            calendarTo.set(Calendar.MILLISECOND, 999);

            dateTo = calendarTo.getTime();

        }


        Sort.Direction direction = sortDirection == null || sortDirection.trim().length() == 0 || sortDirection.trim().equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;

        /* Вторым полем для сортировки добавляем id, чтобы всегда сохранялся строгий порядок.
            Например, если у 2-х задач одинаковое значение приоритета и мы сортируем по этому полю.
            Порядок следования этих 2-х записей после выполнения запроса может каждый раз меняться, т.к. не указано второе поле сортировки.
            Поэтому и используем ID - тогда все записи с одинаковым значением приоритета будут следовать в одном порядке по ID.
         */

        // поле (столбец) сортировки

        if (sortColumn == null){
            sortColumn = ID_COLUMN;
        }

        Sort sort = Sort.by(direction, sortColumn, ID_COLUMN);

        // объект постраничности
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, sort);

        // результат запроса с постраничным выводом
        Page<Task> result = taskService.find(title, completed, priorityId, categoryId, email, dateFrom, dateTo, pageRequest);

        // результат запроса
        return ResponseEntity.ok(result);

    }


}
