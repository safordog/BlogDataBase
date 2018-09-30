package com.gmail.safordog;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;

import static java.lang.Class.forName;

@WebServlet(name = "SearchingServlet", urlPatterns = "/search")
public class SearchingServlet extends HttpServlet {

    ArticleServlet as = new ArticleServlet();
    RegistrationServlet rs = new RegistrationServlet();
    private String template = rs.getTemplate() + "%s";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String author = req.getParameter("author");
        try {
            resp.getWriter().print(String.format(template, searchByAuthor(author)));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String print = rs.getTemplate() + "<form action=\"/search\" method=\"post\" class=\"search\">"
                + "<br>Enter the name of the author:<input type=\"text\" name=\"author\"/>"
                + "<input type=\"submit\" value=\"search\"/></form></div></div>"
                + "</fieldset>ProJAVA Blog</body></html>";
        resp.getWriter().print(print);
    }

    public String searchByAuthor(String author) throws ClassNotFoundException, SQLException {
        String message = "";
        forName("org.postgresql.Driver");
        Connection con = DriverManager.getConnection("jdbc:postgresql://localhost/postgres", "postgres",
                "Destination6");
        try (PreparedStatement ps = con.prepareStatement("SELECT * FROM articles WHERE username = ?")) {
            ps.setString(1, author);
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
