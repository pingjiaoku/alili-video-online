package Function.Comment;

import Classes.Users;
import Function.PublicFunction;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import oracle.jdbc.OracleType;

import java.io.IOException;
import java.sql.*;

import static Function.PublicFunction.SelectUserByAccPwd;

@WebServlet(name = "CommentThumbsUp", urlPatterns = "/CommentThumbsUp")
public class CommentThumbsUp extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("==用户点赞评论========================================================");
        String acc = request.getParameter("acc");
        String pwd = request.getParameter("pwd");
        String id = request.getParameter("id");
        System.out.println("acc:"+acc+",pwd:"+pwd+",commentId:"+id);

        Connection conn = null;
        CallableStatement stmt = null;
        try {
            conn = PublicFunction.getConnection();
            // 调用存储过程
            stmt = conn.prepareCall("{call PRO_COMM_THUMBS(?,?,?,?)}");
            stmt.setString(1, acc);
            stmt.setString(2,pwd);
            stmt.setLong(3,Long.parseLong(id));
            // 传出参数设置，（1：用户已点赞该评论，2：用户已取消点赞该评论，3：发生异常）
            stmt.registerOutParameter(4, OracleType.NUMBER);
            stmt.execute();

            // 取出传出参数
            int flag = stmt.getInt(4);
            if(flag==1){
                System.out.println("用户已点赞该评论");
            } else if (flag==2) {
                System.out.println("用户已取消点赞该评论");
            } else {
                System.out.println("发生异常");
            }
            response.setContentType("text/html;charset=utf-8");
            response.getWriter().write(flag + "");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            PublicFunction.CloseAll(null, stmt, conn);
        }
        System.out.println("======================================================================");
    }


 /*   private static boolean SelectCommentThumbs(Connection conn, String userId, String commentId){
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String sql = "select * from user_comment_thumbs_up where v_user_id=? and v_comment_id=?";
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, userId);
            stmt.setLong(2, Long.parseLong(commentId));

            rs = stmt.executeQuery();

            if(rs.next()){
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            PublicFunction.CloseAll(rs, stmt, null);
        }
        return true;
    }


    private static void DeleteCommentThumbs(Connection conn, String userId, String commentId){

    }*/

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
