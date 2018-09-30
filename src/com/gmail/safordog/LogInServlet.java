package com.gmail.safordog;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet(name = "LogInServlet", urlPatterns = "/login_user")
public class LogInServlet extends HttpServlet {

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        AddUserServlet aus = new AddUserServlet();
        String log = req.getParameter("logining");
        String psw = req.getParameter("passwording");
        String logPsw = log + psw;
        session.setAttribute("logpsw", logPsw);
        session.setAttribute("login", log);
        try {
            if (aus.getUsersFromDataBase().containsKey(log) && aus.getUsersFromDataBase().get(log).getPassword().equals(psw)) {
                req.setAttribute("acceptMessage", "Wellcome");
                req.getRequestDispatcher("index_response.jsp").forward(req, resp);
            } else {
                req.setAttribute("deniedMessage", "Wrong password / login or register");
                req.getRequestDispatcher("index_response.jsp").forward(req, resp);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
