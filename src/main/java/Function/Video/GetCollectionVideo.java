package Function.Video;

import Classes.Video;
import Function.PublicFunction;
import com.alibaba.fastjson.JSON;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import javax.naming.ldap.PagedResultsControl;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

@WebServlet(name = "GetCollectionVideo", urlPatterns = "/GetCollectionVideo")
public class GetCollectionVideo extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("==获取收藏视频======================================================");
        String userId = request.getParameter("userId");
        String type = request.getParameter("type");

        response.setContentType("application/json;charset=utf-8");
        ArrayList<Video> list = new ArrayList<>();

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = PublicFunction.getConnection();
            String sql =
                    """
                    select c.v_id coll_id, to_char(V_COLL_DATE, 'yyyy-mm-dd') coll_date,
                        v.v_id video_id, V_TITLE, V_AUTHOR_ID, V_COVER_URL, V_TOTAL_TIME, V_VIEW_COUNT
                    from COLLECTION c, VIDEOS v
                    where V_USER_ID=? and V_FOLDER=?
                    and c.V_VIDEO_ID=v.v_id
                    order by c.V_COLL_DATE
                    """;
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, userId);
            stmt.setString(2, type);

            rs = stmt.executeQuery();

            while (rs.next()){
                Video video = new Video();
                video.setCollId(rs.getLong("coll_id"));
                video.setCollDate(rs.getString("coll_date"));
                video.setId(rs.getString("video_id"));
                video.setTitle(rs.getString("v_title"));
                video.setAuthorId(rs.getString("v_author_id"));
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
