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
import java.util.Collections;

@WebServlet(urlPatterns = "/GetVideosByChannel", name = "GetVideosByChannel")
public class GetVideosByChannel extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("==根据频道获取视频========================================================");
        String id = request.getParameter("id");
        ArrayList<Video> list = new ArrayList<>();

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = PublicFunction.getConnection();
            String sql =
                    """
                    select VIDEOS.v_id,V_TITLE,V_AUTHOR_ID,v_headshot, V_NAME author_name,
                        to_char(V_PUBLISH_DATE,'yyyy-mm-dd') t_publish,
                        V_COVER_URL, V_TOTAL_TIME, V_VIEW_COUNT
                    from videos, USERS
                    where videos.V_AUTHOR_ID=users.V_ID
                    """;
            if(!"homepage".equals(id)){
                sql += " and v_channel_id=?";
            }
            stmt = conn.prepareStatement(sql);
            if(!"homepage".equals(id)){
                stmt.setString(1, id);
            }
            rs = stmt.executeQuery();

            while (rs.next()) {
                Video video = new Video();
                video.setId(rs.getString("v_id"));
                video.setTitle(rs.getString("v_title"));
                video.setAuthorId(rs.getString("V_AUTHOR_ID"));
                video.setAuthorHeadshot(rs.getString("v_headshot"));
                video.setAuthorName(rs.getString("author_name"));
                video.setPublishDate(rs.getString("t_publish"));
                video.setCoverUrl(rs.getString("v_cover_url"));
                video.setTotalTime(rs.getString("v_total_time"));
                video.setViewCount(rs.getLong("v_view_count"));

                list.add(video);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            PublicFunction.CloseAll(rs, stmt, conn);
        }
        response.setContentType("application/json;charset=utf-8");
        Collections.shuffle(list);
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
