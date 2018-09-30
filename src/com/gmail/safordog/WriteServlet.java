package com.gmail.safordog;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static java.lang.Class.forName;

@WebServlet(name = "WriteServlet", urlPatterns = "/edit")
public class WriteServlet extends HttpServlet {

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ArticleServlet as = new ArticleServlet();
        String content = req.getParameter("text");
        String iD = req.getParameter("ID");
        try {
            editText(content, iD);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        req.setAttribute("editMessage", "The changes were saved.");
        req.getRequestDispatcher("index_response.jsp").forward(req, resp);
    }

    public void editText(String content, String iD) throws ClassNotFoundException, SQLException {
        forName("org.postgresql.Driver");
        Connection con = DriverManager.getConnection("psql:postgresql://localhost/postgres", "postgres",
                "Destination6");
        try (PreparedStatement ps = con.prepareStatement("UPDATE articles SET content = ? WHERE id_article = ?")) {
            ps.setString(1, content);
            ps.setString(2, iD);
            ps.executeUpdate();
        }
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }
}
