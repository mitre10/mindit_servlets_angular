/**
 *
 */
package ro.mindit.todo.resource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ro.mindit.todo.dao.TodoDao;
import ro.mindit.todo.model.Todo;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class TodoResource extends HttpServlet {

    private TodoDao todoDao;

    @Override
    public void init() throws ServletException {
        todoDao = new TodoDao();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String json = "";
        ObjectMapper obj = new ObjectMapper();
        ServletInputStream reqInputStream = req.getInputStream();
        Scanner sc = new Scanner(reqInputStream, "UTF-8").useDelimiter("\\A");
        json = sc.next();
        Todo t = obj.readValue(json, Todo.class);
        try {
            todoDao.connect();
            todoDao.addTodo(t);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String json = "";
        ObjectMapper obj = new ObjectMapper();
        ServletInputStream reqInputStream = req.getInputStream();
        Scanner sc = new Scanner(reqInputStream, "UTF-8").useDelimiter("\\A");
        json = sc.next();
        Todo t = obj.readValue(json, Todo.class);
        try {
            todoDao.connect();
            todoDao.updateTodo(t.getName(), t.getOwner(), t.getPriority(), t.getId());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("id");
        try {
            todoDao.connect();
            if (id != null) {
                todoDao.deleteTodo(Integer.parseInt(id));
            }
            todoDao.disconnect();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        // set response content type
        response.setContentType("application/json");


		String json = getTodoFromDb(request);

        PrintWriter out = response.getWriter();
        out.print(json);
        out.flush();
    }

    @Override
    public void destroy() {
    }

    private String getTodoFromDb(HttpServletRequest request) throws JsonProcessingException {
        String json = "";
        ObjectMapper objectMapper = new ObjectMapper();

        String id = request.getParameter("id");
        try {
            // Connect to the database
            todoDao.connect();

            if (id != null) {
                Todo todo = todoDao.findOne(Integer.parseInt(id));
                json = objectMapper.writeValueAsString(todo);
            } else {
                List<Todo> todos = todoDao.findAll();
                json = objectMapper.writeValueAsString(todos);
            }

            // Disconnect from the database
            todoDao.disconnect();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return json;
    }
}