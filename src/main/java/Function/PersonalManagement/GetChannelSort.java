package Function.PersonalManagement;

import Classes.Channel;
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

@WebServlet(name = "GetChannelSort", urlPatterns = "/GetChannelSort")
public class GetChannelSort extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("==获取用户频道========================================================");

        String userId = request.getParameter("userId");
        // 查询频道列表，没有则为"null"
        String s = JSON.toJSONString(GetChannelList(userId));
        System.out.println("用户频道排序："+s);
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(s);

        System.out.println("======================================================================");
    }
    private static ArrayList<Channel> GetChannelList(String id){
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        ArrayList<Channel> list = new ArrayList<>();
        boolean data = false;

        try {
            conn = PublicFunction.getConnection();
            String sql = "select CHANNEL.V_ID, CHANNEL.V_NAME, USERS_CHANNEL.V_DISPLAY, CHANNEL.v_img, USERS_CHANNEL.V_ORDINAL " +
                    "from USERS_CHANNEL, CHANNEL " +
                    "where CHANNEL.V_ID = USERS_CHANNEL.V_CHANNEL_ID and V_USER_ID = ? " +
                    "order by USERS_CHANNEL.V_ORDINAL";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, id);

            rs = stmt.executeQuery();
            while(rs.next()){
                data = true;
                Channel channel = new Channel();
                channel.setId(rs.getString("v_id"));
                channel.setContent(rs.getString("v_name"));
                channel.setDisplay(rs.getInt("v_display"));
                channel.setImg(rs.getString("v_img"));
                channel.setOrdinal(rs.getInt("v_ordinal"));
                list.add(channel);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            PublicFunction.CloseAll(rs,stmt,conn);
        }
        return data ? list : null;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
