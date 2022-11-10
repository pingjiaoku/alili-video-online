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

@WebServlet(name = "GetUserHomeInfo", urlPatterns = "/GetUserHomeInfo")
public class GetUserHomeInfo extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("==获取用户主页信息========================================================");
        String id = request.getParameter("id");
        String acc = request.getParameter("acc");
        String pwd = request.getParameter("pwd");
        System.out.println(id + ":" + acc + ":" + pwd);

        response.setContentType("application/json;charset=utf-8");

        Users user = new Users();

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = PublicFunction.getConnection();
            String sql =
                    "select v_name,V_HEADSHOT,V_GENDER,V_VIDEO,V_LIKES,V_CONCERN,V_FANS,V_COLLECTION, " +
                            "(select count(*) from USERS_CONCERN " +
                            "where V_USER1=(select v_id from users where V_ACCOUNT=? and V_PASSWORD=?) " +
                            "and v_user2=u1.V_ID) is_concern " +
                    "from users u1 where v_id=?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, acc);
            stmt.setString(2, pwd);
            stmt.setString(3, id);

            rs = stmt.executeQuery();
            if(rs.next()){

                user.setUname(rs.getString("v_name"));
                user.setHeadshot(rs.getString("v_headshot"));
                user.setGender(rs.getInt("v_gender"));
                user.setVideoNum(rs.getInt("v_video"));
                user.setLikes(rs.getLong("v_likes"));
                user.setConcern(rs.getInt("v_concern"));
                user.setFans(rs.getLong("v_fans"));
                user.setCollection(rs.getLong("v_collection"));
                user.setIsConcern(rs.getInt("is_concern"));

            } else {
                user = null;
            }
        } catch (SQLException e) {
            user = null;
            e.printStackTrace();
        } finally {
            PublicFunction.CloseAll(rs, stmt, conn);
        }
        String s = JSON.toJSONString(user);
        System.out.println(s);
        response.getWriter().write(s);
        System.out.println("======================================================================");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
