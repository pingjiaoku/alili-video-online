package Function.Video;

import Classes.Video;
import Function.PublicFunction;
import com.alibaba.fastjson.JSON;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import oracle.jdbc.OracleType;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.security.cert.TrustAnchor;
import java.sql.*;
import java.util.Arrays;
import java.util.regex.Pattern;

@MultipartConfig
@WebServlet(name = "UploadVideo", urlPatterns = "/UploadVideo")
public class UploadVideo extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("==上传视频============================================================");

        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");

        String videoName = null;
        String coverName = null;

        Connection conn = null;
        try {
            conn = PublicFunction.getConnection();
            conn.setAutoCommit(false);

            // 检查用户是否存在
            String userId = CheckUserExist(conn, request.getParameter("account"), request.getParameter("password"));

            if(userId != null) {
                // 获取视频信息，检查并保存文件
                Video video = VideoUploadCheck(request);
                if (video != null) {
                    videoName = video.getVideoUrl();
                    coverName = video.getCoverUrl();

                    video.setAuthorId(userId);
                    System.out.println(JSON.toJSONString(video));
                    // 向数据库中插入视频信息
                    String videoId = InsertVideo(request, conn, video);
                    System.out.println("videoId:" + videoId);
                    if(videoId!=null && InsertTags(conn, request, userId, video.getChannelId(), videoId)){
                        response.getWriter().write(videoId);
                        return;
                    }
                }
            }
            response.getWriter().write("Error");
        } catch (SQLException e) {
            DeleteFile(request, videoName, coverName);
            response.getWriter().write("Error");
            e.printStackTrace();
        } finally {
            PublicFunction.CloseAll(null, null, conn);
            System.out.println("======================================================================");
        }

    }

    private static void DeleteFile(HttpServletRequest request, String video, String cover){
        String projectUrl = request.getServletContext().getRealPath("/") + "\\web-resources\\";
        File videoFile1 = new File(projectUrl + "video\\" + video);
        File coverFile1 = new File(projectUrl + "cover\\" + cover);
        File videoFile2 = new File("D:\\javaweb\\ALL\\ALILI\\src\\main\\webapp\\web-resources\\video\\" + video);
        File coverFile2 = new File("D:\\javaweb\\ALL\\ALILI\\src\\main\\webapp\\web-resources\\cover\\" + cover);
        if(videoFile1.delete()&&videoFile2.delete()&&coverFile1.delete()&&coverFile2.delete()){
            System.out.println("文件删除成功");
        } else {
            System.out.println("文件删除失败");
        }
    }

    private static String CheckUserExist(Connection conn, String acc, String pwd) {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String sql = "select v_id from users where v_account=? and v_password=?";
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, acc);
            stmt.setString(2, pwd);

            rs = stmt.executeQuery();

            if(rs.next()){
                return rs.getString("v_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            PublicFunction.CloseAll(rs, stmt, null);
        }
        return null;
    }

    private static Video VideoUploadCheck(HttpServletRequest request) throws ServletException, IOException {
        Video video = new Video();
        String title = request.getParameter("title");
        String describe = request.getParameter("describe");
        if(CheckCount(title, 150) && CheckCount(describe, 1400)) {
            video.setTitle(title);
            video.setDescribe(describe);
            video.setChannelId(Integer.parseInt(request.getParameter("channel")));
            video.setTotalTime(request.getParameter("total-time"));
        } else {
            return null;
        }

        // 保存视频和封面，并获取文件名
        String videoName = PublicFunction.SaveFile(request, "video");
        String coverName = PublicFunction.SaveFile(request, "cover");
        if(videoName != null && coverName != null) {
            video.setVideoUrl(videoName);
            video.setCoverUrl(coverName);
        } else {
            return null;
        }

        return video;
    }
    private static boolean CheckCount(String str, int max){
        String[] t = str.split("");
        int chs = 0, other = 0;
        for (String s : t) {
            String regex = "[\u4e00-\u9fa5]"; // 中文
            if(Pattern.matches(regex, s)){
                chs++;
            } else {
                other++;
            }
        }
        return chs*4+other <= max;
    }



    private static String InsertVideo(HttpServletRequest request, Connection conn, Video video){
        CallableStatement stmt = null;
        try {
            stmt = conn.prepareCall("{call pro_video_upload(?,?,?,?,?,?,?,?)}");
            stmt.setString(1, video.getTitle());
            if("".equals(video.getDescribe())){
                stmt.setString(2, " ");
            } else {
                stmt.setString(2, video.getDescribe());
            }
            stmt.setString(3, video.getAuthorId());
            stmt.setInt(4, video.getChannelId());
            stmt.setString(5, video.getVideoUrl());
            stmt.setString(6, video.getCoverUrl());
            stmt.setString(7, video.getTotalTime());
            stmt.registerOutParameter(8, OracleType.VARCHAR2);

            stmt.execute();
            conn.commit();
            return stmt.getString(8);
        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            DeleteFile(request, video.getVideoUrl(), video.getCoverUrl());
            e.printStackTrace();
        } finally {
            PublicFunction.CloseAll(null, stmt, null);
        }
        return null;
    }

    private static boolean InsertTags(Connection conn, HttpServletRequest request, String userId, int channelId,@NotNull String videoId) {
        String tagCount = request.getParameter("tags-count");
        System.out.println("tag-count:" + tagCount);
        if(!"0".equals(tagCount)) {
            String[] tags = request.getParameterValues("tags");
            String sql = """
                    merge into TAGS
                    USING (select ? t_name, ? t_channel from dual) t2
                    on (TAGS.v_name = t2.t_name and tags.V_CHANNEL_ID=t2.t_channel)
                    when matched then
                        update set V_COUNT=v_count+1
                    when not matched then
                        insert (v_id, v_name, v_creator, v_count, v_channel_id)
                        VALUES (seq_tag_id.nextval, t2.t_name, ?, 0, t2.t_channel)
                    """;
            PreparedStatement stmt = null;

            try {
                stmt = conn.prepareStatement(sql);
                for (String tag : tags) {
                    if(CheckCount(tag, 40)){

                        stmt.setString(1, tag);
                        stmt.setInt(2, channelId);
                        stmt.setString(3, userId);

                        stmt.execute();
                        conn.commit();

                        InsertVideoTags(conn, tag, channelId, videoId);
                    }
                    System.out.print(tag + ":");
                }

                System.out.println();

                return true;
            } catch (SQLException e) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                e.printStackTrace();
            } finally {
                PublicFunction.CloseAll(null, stmt, null);
            }
        }
        return false;
    }

    private static void InsertVideoTags(Connection conn, String tag, int channelId, String videoId){
        String sql = "insert into video_tag values (SEQ_VIDEO_TAG_ID.nextval, ?, (select v_id from TAGS where v_name=? and V_CHANNEL_ID=?))";
        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, videoId);
            stmt.setString(2, tag);
            stmt.setInt(3, channelId);

            stmt.execute();

            conn.commit();

        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            PublicFunction.CloseAll(null, stmt, null);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
