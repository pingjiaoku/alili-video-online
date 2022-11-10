package Function.PersonalManagement;

import Function.PublicFunction;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static Function.PublicFunction.*;

@WebServlet(name = "Login", urlPatterns = "/Login")
public class Login extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("==登录检测，添加Cookie=================================================");

        String account = request.getParameter("account");
        String password = request.getParameter("password");
        response.setContentType("text/html;charset=utf-8");

        Connection conn = null;
        try {
            conn = getConnection();
            if(SelectUserByAccPwd(conn, account, password) != null){
                System.out.println("用户登录：密码验证成功");
                // 刷新Cookie
                AddCookie(response,account,password);

                response.getWriter().write("true");
            } else {
                System.out.println("账号或密码错误");
                response.getWriter().write("账号或密码错误");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            CloseAll(null,null, conn);
        }

        System.out.println("============================================================================");
    }



    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
