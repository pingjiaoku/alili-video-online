package Function.PersonalManagement;

import Classes.Users;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Pattern;

import Function.*;
import org.jetbrains.annotations.NotNull;

import static Function.PublicFunction.AddCookie;
import static Function.PublicFunction.CloseAll;

@MultipartConfig
@WebServlet(name = "Register", urlPatterns = "/Register")
public class Register extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("==用户注册============================================================");

        request.setCharacterEncoding("utf-8");
        // 注册验证，并保存头像
        Users user = RegisterCheck(request);
        System.out.println("user:"+user);

        response.setContentType("text/html;charset=utf-8");

        if(user == null){
            System.out.println("注册信息检测未过");
            response.getWriter().write("RequestDataException");
        } else {
            System.out.println("注册信息检测已通过");

            Connection conn = null;
            try {
                conn = PublicFunction.getConnection();
                conn.setAutoCommit(false);

                // 检查账号唯一
                Integer count = CheckAccUnique(conn, user.getAccount());

                if (count != null) {
                    if (count > 0) {
                        System.out.println("注册失败，账号已存在");
                        response.getWriter().write("AccountExist");
                        return;
                    }
                    System.out.println("账号唯一检测已通过");

                    // 插入到数据库
                    // 插入成功
                    if (InsertUsers(conn, user)) {
                        System.out.println("添加用户信息到数据库中成功");
                        // 添加cookie
                        AddCookie(response, user.getAccount(), user.getPassword());
                        System.out.println("添加用户cookie成功");

                        response.getWriter().write("True");
                    } else {
                        System.out.println("用户注册信息添加失败，数据库连接异常");
                        response.getWriter().write("DatabaseConnectionException");
                    }

                } else {
                    System.out.println("账号检测是否唯一失败，数据库连接异常");
                    response.getWriter().write("DatabaseConnectionException");
                    return;
                }
            } catch (SQLException e) {
                try {
                    assert conn != null;
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                e.printStackTrace();
            }

        }

        System.out.println("======================================================================");
    }

    private Users RegisterCheck(HttpServletRequest request) throws ServletException, IOException {
        Users user = new Users();
        String uname = request.getParameter("uname");
        String account = request.getParameter("account");
        String password = request.getParameter("password");
        String gender = request.getParameter("gender");

        // 数据格式验证
        String regex = "[A-Za-z0-9_]{6,20}";
        if(Pattern.matches("[^\s]{1,10}",uname) && Pattern.matches(regex, account) && Pattern.matches(regex, password) && Pattern.matches("[012]", gender)){
            user.setUname(uname);
            user.setAccount(account);
            user.setPassword(password);
            user.setGender(Integer.parseInt(gender));
            System.out.println(user);
        } else {
            return null;
        }
        // 保存文件并获取文件名
        String fileName = PublicFunction.SaveFile(request, "headshot");
        if(fileName != null){
            user.setHeadshot(fileName);
            return user;
        }
        return null;
    }

    // 检查账号唯一，0：没有相同的数据，>0：有相同的数据，null：数据库连接异常
    private static Integer CheckAccUnique(Connection conn, String acc){
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            String sql = "select count(*) from users where V_ACCOUNT = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1,acc);

            rs = stmt.executeQuery();
            if(rs.next()){
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // 不要删除connection，还要用
            PublicFunction.CloseAll(rs, stmt, null);
        }
        return null;
    }

    // 添加用户数据
    private static boolean InsertUsers(Connection conn, @NotNull Users user){
        PreparedStatement stmt = null;
        try {
            String sql = "insert into users (v_headshot,v_name,v_account,v_password, v_gender) values (?,?,?,?,?)";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1,user.getHeadshot());
            stmt.setString(2,user.getUname());
            stmt.setString(3,user.getAccount());
            stmt.setString(4,user.getPassword());
            stmt.setInt(5,user.getGender());

            stmt.execute();
            conn.commit();

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            PublicFunction.CloseAll(null, stmt, conn);
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

}
