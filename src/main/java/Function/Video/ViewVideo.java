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

import static Function.PublicFunction.*;

@WebServlet(name="ViewVideo", urlPatterns = "/ViewVideo")
public class ViewVideo extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("==获取视频============================================================");

        String videoId = request.getParameter("videoId");
        String acc = request.getParameter("acc");
        String pwd = request.getParameter("pwd");
        System.out.println("videoId:"+videoId+",acc:"+acc+",pwd:"+pwd);

        response.setContentType("application/json;charset=utf-8");

        if(videoId != null) {
            Connection conn = null;
            try {
                conn = getConnection();
                conn.setAutoCommit(false);
                Video video = SelectVideoById(conn, videoId, acc, pwd);
                if(video != null) {
                    String s = JSON.toJSONString(video);
                    System.out.println("得到视频信息："+s);
                    response.getWriter().write(s);

                    System.out.println("======================================================================");
                    return;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                CloseAll(null,null,conn);
            }

        }
        System.out.println("视频获取失败");
        response.getWriter().write("null");

        System.out.println("======================================================================");

    }


    public static Video SelectVideoById(Connection conn, String videoId, String acc, String pwd){
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            String sql = """
                        with tmp as (select v_id from users where V_ACCOUNT=? and V_PASSWORD=?)
                        select v.V_ID,v.V_TITLE,v.V_DESCRIBE,v.V_AUTHOR_ID,
                               to_char(v.V_PUBLISH_DATE, 'yyyy-mm-dd hh24:mi:ss') publish_date,
                               v.v_total_time, v.V_CHANNEL_ID,v.V_VIDEO_URL,v.V_COVER_URL,
                               v.V_VIEW_COUNT,v.V_BARRAGE_COUNT,v.V_COMMENT_COUNT,v.V_LIKES,
                               v.V_COLLECTION,u.V_NAME,u.V_HEADSHOT,u.V_FANS,u.V_VIDEO,
                               (select count(*) from USER_VIDEO_THUMBS_UP, tmp
                               where V_USER_ID=tmp.V_ID
                               and V_VIDEO_ID=v.V_ID) is_thumbs,
                               (select count(*) from COLLECTION, tmp
                               where V_USER_ID=tmp.V_ID and V_VIDEO_ID=v.V_ID) is_coll,
                               (select count(*) from USERS_CONCERN uc, tmp
                               where uc.V_USER1=tmp.v_id and V_USER2=v.V_AUTHOR_ID) is_concern
                        from videos v,users u
                        where v.V_AUTHOR_ID=u.V_ID
                        and v.V_ID=?
                        """;
            stmt = conn.prepareStatement(sql);
            stmt.setString(1,acc);
            stmt.setString(2,pwd);
            stmt.setString(3,videoId);

            rs = stmt.executeQuery();
            if(rs.next()){
                // 查询到了视频，增加一次播放数
                if(AddVideoViewCount(conn, videoId)){
                    System.out.println("视频播放数+1");
                } else {
                    System.out.println("增加视频播放数失败");
                }
                Video video = new Video();
                video.setId(rs.getString("v_id"));
                video.setTitle(rs.getString("v_title"));
                video.setDescribe(rs.getString("v_describe"));
                video.setAuthorId(rs.getString("v_author_id"));
                video.setTotalTime(rs.getString("v_total_time"));
                // jdbc获取oracle的date时，使用getTimestamp才能得到时分秒
                video.setPublishDate(rs.getString("publish_date"));
                video.setChannelId(rs.getInt("v_channel_id"));
                video.setVideoUrl(rs.getString("v_video_url"));
                video.setCoverUrl(rs.getString("v_cover_url"));
                video.setViewCount(rs.getLong("v_view_count"));
                video.setAuthorName(rs.getString("v_name"));
                video.setAuthorHeadshot(rs.getString("v_headshot"));
                video.setBarrageCount(rs.getLong("v_barrage_count"));
                video.setCommentCount(rs.getLong("v_comment_count"));
                video.setLikes(rs.getLong("v_likes"));
                video.setCollection(rs.getLong("V_COLLECTION"));
                video.setAuthorFans(rs.getLong("v_fans"));
                video.setAuthorVideo(rs.getInt("v_video"));
                video.setIsThumbs(rs.getInt("is_thumbs"));
                video.setIsCollection(rs.getInt("is_coll"));
                video.setIsConcern(rs.getInt("is_concern"));
                return video;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            CloseAll(rs,stmt,null);
        }
        return null;
    }


    private static boolean AddVideoViewCount(Connection conn, String videoId) {
        PreparedStatement stmt = null;
        String sql = "update videos set v_view_count=v_view_count+1 where v_id=?";
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, videoId);

            if(stmt.execute()){
                conn.commit();
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } finally {
            PublicFunction.CloseAll(null, stmt, null);
        }
        return false;
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
