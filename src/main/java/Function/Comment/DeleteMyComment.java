package Function.Comment;

import Function.PublicFunction;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@WebServlet(name = "DeleteMyComment", urlPatterns = "/DeleteMyComment")
public class DeleteMyComment extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("==删除评论============================================================");
        String acc = request.getParameter("acc");
        String pwd = request.getParameter("pwd");
        String commentId = request.getParameter("commentId");
        System.out.println("acc:" + acc + ",pwd:" + pwd + ",commentId:" + commentId);

        response.setContentType("text/html;charset=utf-8");

        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = PublicFunction.getConnection();
            conn.setAutoCommit(false);
            String sql = """
                         delete from COMMENTS
                         where V_ID=?
                         and V_AUTHOR_ID=(
                            select v_id from USERS
                            where V_ACCOUNT=? and V_PASSWORD=?
                            )
                         """;
            stmt = conn.prepareStatement(sql);
            stmt.setLong(1, Long.parseLong(commentId));
            stmt.setString(2, acc);
            stmt.setString(3, pwd);

            stmt.execute();
            conn.commit();

            response.getWriter().write("Success");

            System.out.println("删除成功");
        } catch (SQLException e) {
            response.getWriter().write("Error");

            System.out.println("发生异常");
            try {
                assert conn != null;
                conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
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
