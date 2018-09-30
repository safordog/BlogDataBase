package com.gmail.safordog;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.stream.Collectors;

import static java.lang.Class.forName;

@WebServlet(name = "SortingServlet", urlPatterns = "/sort")
public class SortingServlet extends HttpServlet {

    RegistrationServlet rs = new RegistrationServlet();
    private String template = rs.getTemplate() + "%s";

    ArticleServlet as = new ArticleServlet();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getParameter("sort").equals("author")) {
            try {
                resp.getWriter().print(String.format(template, sortByAuthor()));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else if (req.getParameter("sort").equals("date")) {
            try {
                resp.getWriter().print(String.format(template, sortByDate()));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String print = rs.getTemplate() + "<form action=\"/sort\" method=\"post\" class=\"search\">"
                + "<br><br>sorting by author:<input type=\"radio\" name=\"sort\" value=\"author\"/>"
                + "<br>sorting by date:<input type=\"radio\" name=\"sort\" value=\"date\"/>"
                + "<br><input type=\"submit\" value=\"sort\"/></form></div></div>"
                + "</fieldset>ProJAVA Blog</body></html>";
        resp.getWriter().print(print);
    }

    public String sortByAuthor() throws ClassNotFoundException, SQLException {
        String message = "";
        forName("org.postgresql.Driver");
        Connection con = DriverManager.getConnection("jdbc:postgresql://localhost/postgres", "postgres",
                "Destination6");
        try (PreparedStatement ps = con.prepareStatement("SELECT * FROM articles ORDER BY username DESC")) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    message += "<br><h3 class=\"word\">" + rs.getString("content") + "</h3><br>"
                            + rs.getString("username") + "<br>" + rs.getString("date") + "<br><br><br>";
                }
            }
        }
        return message;
    }

    public String sortByDate() throws ClassNotFoundException, SQLException {
        String message = "";
        forName("org.postgresql.Driver");
        Connection con = DriverManager.getConnection("jdbc:postgresql://localhost/postgres", "postgres",
                "Destination6");
        try (PreparedStatement ps = con.prepareStatement("SELECT * FROM articles ORDER BY date DESC")) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    message += "<br><h3 class=\"word\">" + rs.getString("content") + "</h3><br>"
                            + rs.getString("username") + "<br>" + rs.getString("date") + "<br><br><br>";
                }
            }
        }
        return message;
    }

}
