package Function.Video;

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

@WebServlet(name = "GetRecommendTags", urlPatterns = "/GetRecommendTags")
public class GetRecommendTags extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("==获取推荐标签========================================================");
        String channelId = request.getParameter("channelId");
        System.out.println("channelId:" + channelId);

        response.setContentType("application/json;charset=utf-8");

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String sql = """
                    select v_name from (select v_name from TAGS where V_CHANNEL_ID=? order by V_COUNT desc) where rownum<=10
                    """;
        try {
            conn = PublicFunction.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, Integer.parseInt(channelId));

            rs = stmt.executeQuery();

            class tags{
                final String name;
                tags(String name) {
                    this.name = name;
                }
                public String getName(){
                    return this.name;
                }
            }
            ArrayList<tags> list = new ArrayList<>();
            while (rs.next()){
                String name = rs.getString("v_name");
                list.add(new tags(name));
            }
            String s = JSON.toJSONString(list);
            System.out.println(s);
            response.getWriter().write(s);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            PublicFunction.CloseAll(rs, stmt, conn);
        }

        System.out.println("======================================================================");
    }



    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}


