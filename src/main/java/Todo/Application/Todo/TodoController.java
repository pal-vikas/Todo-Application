package Todo.Application.Todo;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import jakarta.validation.Valid;

// @Controller
@SessionAttributes("name")
public class TodoController {
    
    private TodoService todoService;

    public TodoController(TodoService todoService) {
        super();
        this.todoService = todoService;
    }

    @RequestMapping("list-todos")
    public String listAllTodos(ModelMap model)
    {
        String username = getLoggedInUsername(model);
        List<Todo>todos = todoService.findByUsername(username);
        model.put("todos",todos);

        return "listTodos";

    }

    @RequestMapping(value = "add-todo", method = RequestMethod.GET)
    public String showNewTodo(ModelMap model,@Valid Todo todo1, BindingResult result)
    {
        String username = getLoggedInUsername(model);
        Todo todo = new Todo(0, username, "", todo1.getTargetDate(), false);
        model.put("todo", todo);
        return "addTodos";
    }

    @RequestMapping(value = "add-todo", method = RequestMethod.POST)
    public String addNewTodos( ModelMap model,  @Valid Todo todo, BindingResult result)
    {
        if (result.hasErrors()) {
                
            return"addTodos";
        }
        String username = getLoggedInUsername(model);
        todoService.addTodo(username, todo.getDescription(), todo.getTargetDate(), false);
        return "redirect:list-todos";
    }

    @RequestMapping("delete-todo")
    public String deleteTodo(@RequestParam int id)
    {
        todoService.deleteById(id);
        return"redirect:list-todos";
    }

    @RequestMapping(value = "update-todo", method = RequestMethod.GET)
    public String showUpdateTodo(@RequestParam int id, ModelMap model)
    {
       Todo todo = todoService.findById(id);
       model.addAttribute("todo",todo);
       return "addTodos";
    }


    @RequestMapping(value = "update-todo", method = RequestMethod.POST)
    public String updateNewTodos( ModelMap model,  @Valid Todo todo, BindingResult result)
    {
        if (result.hasErrors()) {
                
            return"addTodos";
        }
        todoService.updateTodo(todo);
        return "redirect:list-todos";
    }


    private String getLoggedInUsername(ModelMap model) {

      Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();    }

}
