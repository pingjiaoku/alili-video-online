package Function.PersonalManagement;

import Classes.Users;
import Function.PublicFunction;
import com.alibaba.fastjson.JSON;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static Function.PublicFunction.*;

@WebServlet(name = "Logging", urlPatterns = "/Logging")
public class Logging extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("==用户登录============================================================");

        String acc = request.getParameter("account");
        String pwd = request.getParameter("password");
        System.out.println("登录中：【acc："+acc+"，pwd："+pwd+"】");

        if(acc != null && pwd != null) {

            Connection conn = null;
            try {
                conn = getConnection();
                Users user = SelectUserByAccPwd(conn, acc, pwd);
                if (user != null) {
                    System.out.print("找到该用户：");
                    // 每次登录成功刷新cookie存活时间，不用user.getAccount()等，他妈的根本就没封装这账号密码到对象里面
                    AddCookie(response, acc, pwd);

                    String userStr = JSON.toJSONString(user);
                    System.out.println(userStr);
                    response.setContentType("application/json;charset=utf-8");
                    response.getWriter().write(userStr);

                    System.out.println("======================================================================");

                    return;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                CloseAll(null,null,conn);
            }
        }

        System.out.println("没有找到该用户");
        // 删除该错误的cookie
        DropAccPwdCookie(response);
        response.setContentType("text/html;charset=utf-8");
        response.getWriter().write("NoUserFound");

        System.out.println("======================================================================");

    }



    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
