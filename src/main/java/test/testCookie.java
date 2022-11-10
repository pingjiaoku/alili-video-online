package test;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

@WebServlet("/testCookie")
public class testCookie extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Cookie cookie1 = new Cookie("alili_account","dongadong");
        Cookie cookie2 = new Cookie("alili_password","123456");
        // 设置cookie生命周期为7天
        cookie1.setMaxAge(60*60*24*7);
        cookie2.setMaxAge(60*60*24*7);
        response.addCookie(cookie1);
        response.addCookie(cookie2);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
