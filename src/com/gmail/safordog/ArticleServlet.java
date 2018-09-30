package com.gmail.safordog;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static java.lang.Class.forName;


@WebServlet(name = "ArticleServlet", urlPatterns = "/write")
public class ArticleServlet extends HttpServlet {

    static final String TEXT = "<html><head><link rel=\"stylesheet\" type=\"text/css\" href=\"/web../../../../styles/reset.css\">"
            + "<link rel=\"stylesheet\" type=\"text/css\" href=\"/web../../../../styles/styles.css\">"
            + "<title>ProgBlog - Title</title></head><body><div id=\"header\" class=\"title\">"
            + "<div id=\"problog_title\"><a href=\"index.jsp\"><p>ProJava-BLOG</p></a>"
            + "<p><my>public static void main(String[] args) {&#160&#160&#160&#160&#160...&#160&#160&#160..1001&#160&#160&#160...110110011</my></p>"
            + "<p><my1>System.out.println(\"ProJava-BLOG\");&#160&#160&#160&#160...100011010...</my1></p>"
            + "<p><my2>}&#160&#160&#160&#160&#160...1011101110</my2></p></div><div id=\"image_title\">"
            + "<img src=\"/images/java.png\" alt=\"java\"></div></div><div id=\"navi\" class=\"navi\">"
            + "<div id=\"my_cabinet\"><a href=\"/my_cabinet\"><p>my cabinet</p></a></div>"
            + "<div id=\"search\"><a href=\"/search\"><p>searching articles</p></a>"
            + "</div><div id=\"sort\"><a href=\"/sort\"><p>sorting articles</p></a></div><div id=\"registration\">"
            + "<a href=\"/registration\"><p>login / registration</p></a></div></div><fieldset>"
            + "<div id=\"content\"><div id=\"article\"><p><form action=\"/write\" method=\"post\">\n" +
            "    <p><b>Input an article:</b></p>\n" +
            "    <p><textarea rows=\"30\" cols=\"70\" name=\"text\"></textarea></p>\n" +
            "    <p><input type=\"submit\" value=\"save\" id=\"save\" name=\"save\"></p>\n" +
            "  </form></p></div></div></fieldset>$END$</body></html>";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        Article art = new Article();
        String a = req.getParameter("text");
        art.setContent(a);
        Date d = new Date();
        art.setDate(d.toString());
        art.setUserName((String) session.getAttribute("login"));
        art.setName((String) session.getAttribute("name"));
        int id = (int) (Math.random() * 100000000);
        art.setiD(Integer.toString(id));
        try {
            saveToDataBase(art);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        req.setAttribute("savedMessage", "Your article is saved");
        req.getRequestDispatcher("index_response.jsp").forward(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.getWriter().println(TEXT);
    }

    public void saveToDataBase(Article art) throws SQLException, ClassNotFoundException {
        forName("org.postgresql.Driver");
        try (Connection con = DriverManager.getConnection("jdbc:postgresql://localhost/postgres", "postgres", "Destination6")) {
            try (PreparedStatement ps = con.prepareStatement("INSERT INTO articles VALUES(default, ?, ?, ?, ?, ?, " +
                    "(SELECT author.id_author FROM author WHERE login = ?))")) {
                ps.setString(1, art.getName());
                ps.setString(2, art.getContent());
                ps.setString(3, art.getDate());
                ps.setString(4, art.getUserName());
                ps.setString(5, art.getiD());
                ps.setString(6, art.getUserName());
                ps.executeUpdate();
            }
        }
    }

    public List<Article> getArticlesFromDataBase() throws ClassNotFoundException, SQLException {
        forName("org.postgresql.Driver");
        Connection con = DriverManager.getConnection("jdbc:postgresql://localhost/postgres",
                "postgres", "Destination6");
        List<Article> articles = new ArrayList<>();
        try (PreparedStatement ps = con.prepareStatement("SELECT * FROM articles ORDER BY date DESC")) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Article art = new Article();
                    art.setName(rs.getString("name"));
                    art.setContent(rs.getString("content"));
                    art.setDate(rs.getString("date"));
                    art.setUserName(rs.getString("username"));
                    art.setiD(rs.getString("id_article"));
                    articles.add(art);
                }
            }
        }
        return articles;
    }

}
