package ro.mindit.todo.dao;

import ro.mindit.todo.model.Todo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static ro.mindit.todo.util.Constants.*;

public class TodoDao {
    private int idSeq = 1;
    private List<Todo> todos;

    private Connection jdbcConnection;

    public TodoDao() {
        final Todo todo = new Todo() {{
            setId(idSeq++);
            setName("Todo 1");
            setOwner("Adi");
            setPriority("High");
        }};
        todos = new ArrayList<Todo>() {{
            add(todo);
        }};
    }

    public List<Todo> queryAll() {
        return todos;
    }

    public Todo getTodo(int id) {
        Todo result = null;
        for (Todo todo : todos) {
            if (todo.getId() == id) {
                result = todo;
                break;
            }
        }
        return result;
    }


    public void connect() throws SQLException {
        if (jdbcConnection == null || jdbcConnection.isClosed()) {
            try {
                Class.forName(jdbcDriver);
            } catch (ClassNotFoundException e) {
                throw new SQLException(e);
            }
            jdbcConnection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
        }
    }

    public void disconnect() throws SQLException {
        if (jdbcConnection != null && !jdbcConnection.isClosed()) {
            jdbcConnection.close();
        }
    }

    public Todo findOne(int id) throws SQLException {
        Todo todo = null;
        String sql = "SELECT * FROM todo WHERE id = ?";

        connect();

        PreparedStatement statement = jdbcConnection.prepareStatement(sql);
        statement.setInt(1, id);

        ResultSet resultSet = statement.executeQuery();

        if (resultSet.next()) {
            String title = resultSet.getString("name");
            String author = resultSet.getString("owner");
            String price = resultSet.getString("priority");

            todo = new Todo(id, title, author, price);
        }

        resultSet.close();
        statement.close();
        disconnect();

        return todo;
    }

    public List<Todo> findAll() throws SQLException {
        List<Todo> todoList = new ArrayList<Todo>();

        String sql = "SELECT * FROM todo";

        connect();

        Statement statement = jdbcConnection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);

        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String title = resultSet.getString("name");
            String author = resultSet.getString("owner");
            String price = resultSet.getString("priority");
            Todo todo = new Todo(id, title, author, price);
            todoList.add(todo);
        }

        resultSet.close();
        statement.close();

        disconnect();

        return todoList;
    }

    public void addTodo(Todo todo) throws SQLException {
        String sql = "INSERT INTO todo VALUES(?, ?, ?, ?)";
        connect();
        PreparedStatement statement = jdbcConnection.prepareStatement(sql);
        statement.setInt(1, todo.getId());
        statement.setString(2, todo.getName());
        statement.setString(3, todo.getOwner());
        statement.setString(4, todo.getPriority());
        statement.executeUpdate();
        statement.close();
        disconnect();
    }

    public void deleteTodo(int id) throws SQLException {
        String sql = "DELETE FROM todo WHERE id = ?";
        connect();
        PreparedStatement statement = jdbcConnection.prepareStatement(sql);
        statement.setInt(1, id);
        statement.executeUpdate();
        statement.close();
        disconnect();
    }

    public void updateTodo(String name, String owner, String priority, int id) throws SQLException {
        String sql = "UPDATE todo SET name = ?, owner = ?, priority = ? WHERE id = ?";
        connect();
        PreparedStatement statement = jdbcConnection.prepareStatement(sql);
        statement.setString(1, name);
        statement.setString(2, owner);
        statement.setString(3, priority);
        statement.setInt(4, id);
        statement.executeUpdate();
        statement.close();
        disconnect();
    }

}
