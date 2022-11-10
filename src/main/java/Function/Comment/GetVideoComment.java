package Function.Comment;

import Classes.Comment;
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
import java.util.ArrayList;

@WebServlet(name = "GetVideoComment", urlPatterns = "/GetVideoComment")
public class GetVideoComment extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("==获取视频评论========================================================");
        String id = request.getParameter("id");
        String rule = request.getParameter("rule");
        String acc = request.getParameter("acc");
        String pwd = request.getParameter("pwd");

        System.out.println("视频id：" + id + "，规则：" + rule + "，acc：" + acc + "，pwd：" + pwd);

        ArrayList<Comment> comments = GetVideoComments(id, rule, acc, pwd);

        String s = JSON.toJSONString(comments);
        System.out.println("获取到的评论：" + s);

        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(s);

        System.out.println("======================================================================");
    }


    private static ArrayList<Comment> GetVideoComments(String id, String rule, String acc, String pwd){
        ArrayList<Comment> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = PublicFunction.getConnection();
            // 使用临时表，减少该查询次数
            String sql ="""
                        with tmp as (
                        select v_id from users where V_ACCOUNT=? and V_PASSWORD=?
                        )
                        select u.V_ID user_id, u.V_HEADSHOT, u.V_NAME, c.V_ID comment_id,
                               c.V_CONTENT, c.V_TO_COMMENT, c.V_LIKES,
                               TO_CHAR(c.V_DATE, 'yyyy-mm-dd hh24:mi:ss') s_date,
                               (select count(*) from USER_COMMENT_THUMBS_UP u, tmp
                                where u.V_USER_ID=tmp.v_id
                                and V_COMMENT_ID=c.v_id
                               ) is_thumbs
                        from COMMENTS c, USERS u
                        where c.V_AUTHOR_ID=u.V_ID
                        and c.V_VIDEO_ID=?
                        """;

            if("hot".equals(rule)){
                sql += "order by c.V_LIKES desc";
            } else if("new".equals(rule)) {
                sql += "order by c.v_date desc";
            }

            stmt = conn.prepareStatement(sql);
            stmt.setString(1, acc);
            stmt.setString(2, pwd);
            stmt.setString(3, id);

            rs = stmt.executeQuery();

            while (rs.next()){
                Comment comment = new Comment();
                comment.setUserId(rs.getString("user_id"));
                comment.setUserName(rs.getString("v_name"));
                comment.setHeadshot(rs.getString("v_headshot"));
                comment.setId(rs.getLong("comment_id"));
                comment.setContent(rs.getString("v_content"));
                comment.setToComment(rs.getLong("v_to_comment"));
                comment.setLikes(rs.getLong("v_likes"));
                comment.setDate(rs.getString("s_date"));
                comment.setIsThumbs(rs.getInt("is_thumbs"));
                list.add(comment);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            PublicFunction.CloseAll(rs, stmt, conn);
        }
        return list;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
