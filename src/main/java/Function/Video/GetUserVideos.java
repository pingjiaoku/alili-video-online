package Function.Video;

import Classes.Video;
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
import java.util.concurrent.Flow;

@WebServlet(name = "GetUserVideos", urlPatterns = "/GetUserVideos")
public class GetUserVideos extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("==获取用户视频==========================================================");
        String id = request.getParameter("id");
        String rule = request.getParameter("rule");

        response.setContentType("application/json;charset=utf-8");
        ArrayList<Video> list = new ArrayList<>();

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = PublicFunction.getConnection();
            String sql = "select v_id,V_TITLE,to_char(V_PUBLISH_DATE,'yyyy-mm-dd') t_publish," +
                    " V_COVER_URL, V_TOTAL_TIME, V_VIEW_COUNT from videos where V_AUTHOR_ID=? ";
            if("new".equals(rule)){
                sql += "order by v_publish_date desc";
            } else if("view".equals(rule)){
                sql += "order by v_view_count desc";
            } else {
                sql += "order by v_collection desc";
            }
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, id);

            rs = stmt.executeQuery();
            while(rs.next()){
                Video video = new Video();
                video.setId(rs.getString("v_id"));
                video.setTitle(rs.getString("v_title"));
                video.setPublishDate(rs.getString("t_publish"));
                video.setCoverUrl(rs.getString("v_cover_url"));
                video.setTotalTime(rs.getString("v_total_time"));
                video.setViewCount(rs.getLong("v_view_count"));

                list.add(video);
            }
        } catch (SQLException e) {
            list = null;
            e.printStackTrace();
        } finally {
            PublicFunction.CloseAll(rs, stmt, conn);

        }
        String s = JSON.toJSONString(list);
        System.out.println(s);
        response.getWriter().write(s);
        System.out.println("======================================================================");

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
