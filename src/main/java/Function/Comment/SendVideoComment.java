package Function.Comment;

import Classes.Users;
import Classes.Video;
import Function.PublicFunction;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import oracle.jdbc.OracleType;

import java.io.IOException;
import java.security.cert.TrustAnchor;
import java.sql.*;

import static Function.PublicFunction.*;

@WebServlet(name = "SendVideoComment", urlPatterns = "/SendVideoComment")
public class SendVideoComment extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("==发送评论============================================================");
        String account = request.getParameter("account");
        String password = request.getParameter("password");
        String videoId = request.getParameter("videoId");
        String content = request.getParameter("content");
        String toComment = request.getParameter("toComment");
        System.out.println("account：" + account + "，password：" + password + "，videoId：" + videoId + "，content：" + content + "，toComment：" + toComment);

        response.setContentType("text/html;charset=utf-8");

        Connection conn = null;
        try {
            conn = getConnection();
            conn.setAutoCommit(false);
            Long commentId = SendComment(conn, account, password, videoId, content, toComment);
            System.out.println("插入的comment的id为：" + commentId);
            response.getWriter().write(commentId+"");
        } catch (SQLException e) {
            e.printStackTrace();
            response.getWriter().write("null");
            // 当存储过程发生异常的时候老实用正常的sql
//            TmpSendComment(conn, account, password, videoId, content, toComment, response);
        } finally {
            CloseAll(null,null,conn);
        }

        System.out.println("======================================================================");

    }

    private static Long SendComment(Connection conn, String acc, String pwd, String videoId, String content, String toComment){
        // 用存储过程添加评论并获取
        CallableStatement stmt = null;
        try {
            stmt = conn.prepareCall("{call PRO_COMM_SEND(?,?,?,?,?,?)}");
            stmt.setString(1, acc);
            stmt.setString(2, pwd);
            stmt.setString(3, videoId);
            stmt.setString(4, content);
            if("null".equals(toComment) || toComment == null){
                stmt.setNull(5, Types.NULL);
            } else {
                stmt.setLong(5, Long.parseLong(toComment));
            }
            stmt.registerOutParameter(6, OracleType.LONG);
            stmt.execute();

            Long commentId = stmt.getLong(6);

            conn.commit();

            System.out.println(commentId);
            return commentId;
        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            PublicFunction.CloseAll(null, stmt, null);
        }
        return null;
    }

    private static void TmpSendComment(Connection conn, String account, String password, String videoId,
                                       String content, String toComment, HttpServletResponse response) throws IOException {
        String sql = """
                        insert into comments (V_ID, V_CONTENT, V_AUTHOR_ID, V_VIDEO_ID, V_TO_COMMENT) 
                        values (SEQ_COMMENTS_ID.nextval,?,(
                        select v_id from USERS where V_ACCOUNT=? and V_PASSWORD=?
                        ),?,?)
                        """;
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, content);
            stmt.setString(2, account);
            stmt.setString(3, password);
            stmt.setString(4, videoId);
            if("null".equals(toComment) || toComment == null) {
                stmt.setNull(5, Types.NULL);
            } else {
                stmt.setLong(5, Long.parseLong(toComment));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
