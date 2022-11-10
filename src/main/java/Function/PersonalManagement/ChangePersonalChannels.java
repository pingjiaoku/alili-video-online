package Function.PersonalManagement;

import Function.PublicFunction;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet(name = "ChangePersonalChannels", urlPatterns = "/ChangePersonalChannels")
public class ChangePersonalChannels extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("==更改用户频道========================================================");

        // channels类似于List<Map<String, Object>>
        String channels = request.getParameter("channels");
        String acc = request.getParameter("acc");
        String pwd = request.getParameter("pwd");

        if(InsertUserChannels(channels, acc, pwd)){
            System.out.println("更改排序成功");
        } else {
            System.out.println("更改排序失败");
        }
        System.out.println("======================================================================");

    }

    // 先查询账号密码对应的用户id，再删除对应用户的频道排序信息，最后再插入新的
    private static boolean InsertUserChannels(String channels, String acc, String pwd){
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = PublicFunction.getConnection();
            conn.setAutoCommit(false);
            String sql = "insert into users_channel (v_id,v_user_id,v_channel_id,v_ordinal,v_display) values (SEQ_USER_CHANNEL_ID.nextval,?,?,?,?)";
            stmt = conn.prepareStatement(sql);

            // 先查询账号密码对应的id
            String id = SelectUserId(conn, acc, pwd);
            if(id != null) {
                // 删除用户的channel序列
                if (DeleteUserChannel(conn, acc, pwd)) {
                    // 批量插入用户的channel信息
                    for (Object o : JSON.parseArray(channels)) {
                        // 需要先jsonarryA.get(0)根据索引获取map数组的字符串，再将map转为json对象，然后就可以根据Key获取Value
                        JSONObject jsonObject = JSONObject.parseObject(o.toString());

                        System.out.println(id+","+jsonObject.getString("channelid")+","+Integer.parseInt(jsonObject.getString("sortnum"))+","+("true".equals(jsonObject.getString("display")) ? 1 : 0));

                        stmt.setString(1, id);
                        stmt.setString(2, jsonObject.getString("channelid"));
                        stmt.setLong(3, Integer.parseInt(jsonObject.getString("sortnum")));
                        stmt.setLong(4, "true".equals(jsonObject.getString("display")) ? 1 : 0);

                        stmt.execute();

                    }
                    // 插入成功提交
                    conn.commit();
                    return true;
                }
            }
        } catch (SQLException e) {
            // 发生异常回滚
            try {
                assert conn != null;
                conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            PublicFunction.CloseAll(null,stmt,conn);
        }
        return false;

    }

    private static String SelectUserId(Connection conn, String acc, String pwd) throws SQLException {
        String sql = "select v_id from users where v_account=? and v_password=?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1,acc);
        stmt.setString(2,pwd);
        ResultSet rs = stmt.executeQuery();
        if(rs.next()){
            return rs.getString(1);
        }
        return null;
    }

    private static boolean DeleteUserChannel(Connection conn, String acc, String pwd) throws SQLException{

        String sql = "delete from users_channel where V_USER_ID=(select v_id from users where v_account=? and v_password=?)";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1,acc);
        stmt.setString(2,pwd);
        stmt.execute();
        conn.commit();

        return true;

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
