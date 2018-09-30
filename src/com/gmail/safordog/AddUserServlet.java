package com.gmail.safordog;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.*;
import java.sql.*;
import java.util.*;

import static java.lang.Class.forName;

@WebServlet(name = "AddUserServlet", urlPatterns = "/registration/adduser")
public class AddUserServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        String name = req.getParameter("name");
        String surname = req.getParameter("surname");
        String log = req.getParameter("login");
        String psw = req.getParameter("password");
        try {
            if (!getUsersFromDataBase().containsKey(log)) {
                try {
                    registerToDataBase(name, surname, log, psw);
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            } else {
                req.setAttribute("oldUserMessage", "User with this login is already registered.");
                req.getRequestDispatcher("/index_response.jsp").forward(req, resp);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        session.setAttribute("name", name);
        session.setAttribute("login", log);
        req.setAttribute("addUserMessage", "The registration process was successfully completed");
        req.getRequestDispatcher("/index_response.jsp").forward(req, resp);
    }

    public void registerToDataBase(String name, String surname, String login, String password) throws SQLException, ClassNotFoundException {
        forName("org.postgresql.Driver");
        Connection con = DriverManager.getConnection("jdbc:postgresql://localhost/postgres", "postgres",
                "Destination6");
        try (PreparedStatement ps = con.prepareStatement("INSERT INTO author VALUES(default, ?, ?, ?, ?)")) {
            ps.setString(1, name);
            ps.setString(2, surname);
            ps.setString(3, login);
            ps.setString(4, password);
            ps.executeUpdate();
        }
    }

    public Map<String, User> getUsersFromDataBase() throws ClassNotFoundException, SQLException {
        Map<String, User> users = new HashMap<>();
        forName("org.postgresql.Driver");
        Connection con = DriverManager.getConnection("psql:postgresql://localhost/postgres", "postgres",
                "Destination6");
        try (PreparedStatement ps = con.prepareStatement("SELECT * FROM author")) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    User user = new User();
                    user.setIdAuthor(rs.getInt("id_author"));
                    user.setName(rs.getString("name"));
                    user.setSurname(rs.getString("surname"));
                    user.setLogin(rs.getString("login"));
                    user.setPassword(rs.getString("password"));
                    users.put(rs.getString("login"), user);
                }
            }
        }
        return users;
    }

}
