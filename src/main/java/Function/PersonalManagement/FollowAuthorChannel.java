package Function.PersonalManagement;

import Function.PublicFunction;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@WebServlet(name = "FollowAuthorChannel", urlPatterns = "/FollowAuthorChannel")
public class FollowAuthorChannel extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("==关注作者==============================================================");
        String acc = request.getParameter("acc");
        String pwd = request.getParameter("pwd");
        String authorId = request.getParameter("authorId");
        System.out.println(acc + ":" + pwd + ":" + authorId);

        response.setContentType("text/html;charset=utf-8");

        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = PublicFunction.getConnection();
            conn.setAutoCommit(false);
//            String sql = "insert into USERS_CONCERN (v_user1, v_user2) values ((select v_id from users where V_ACCOUNT=? and V_PASSWORD=?), ?)";
            String sql =
                    """
                    merge into USERS_CONCERN uc
                    using (select (select v_id from users where V_ACCOUNT=? and V_PASSWORD=?) t_user1,
                           ? t_user2 from dual) tmp
                    on(uc.V_USER1=tmp.t_user1 and uc.V_USER2=tmp.t_user2)
                    when matched then
                        update set status=0
                        delete where status=0 or (v_user1=tmp.t_user1 and v_user2=tmp.t_user2)
                    when not matched then
                        insert (v_user1, v_user2) values ( t_user1, t_user2 )
                    """;
            stmt = conn.prepareStatement(sql);

            stmt.setString(1, acc);
            stmt.setString(2, pwd);
            stmt.setString(3, authorId);
            stmt.execute();
            conn.commit();

            response.getWriter().write("Success");
        } catch (SQLException e) {
            try {
                if(conn != null)
                    conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            response.getWriter().write("Error");
        } finally {
            PublicFunction.CloseAll(null, stmt, conn);
        }

        System.out.println("======================================================================");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
