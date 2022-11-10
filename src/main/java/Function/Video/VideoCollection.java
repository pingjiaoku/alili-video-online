package Function.Video;

import Function.PublicFunction;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet(name = "VideoCollection", urlPatterns = "/VideoCollection")
public class VideoCollection extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("==收藏视频=============================================================");
        String acc = request.getParameter("acc");
        String pwd = request.getParameter("pwd");
        String videoId = request.getParameter("videoId");
        String folder = request.getParameter("folder");
        System.out.println(acc + ":" + pwd + ":" + videoId + ":" + folder);

        response.setContentType("text/html;charset=utf-8");

        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = PublicFunction.getConnection();
            String sql =
                    """
                    merge into COLLECTION
                    USING (select
                            (select v_id from users
                             where V_ACCOUNT=? and V_PASSWORD=?
                            ) t_user_id, ? t_video_id, ? t_folder
                           from dual) t2
                    on (COLLECTION.V_USER_ID = t2.t_user_id and COLLECTION.V_VIDEO_ID=t2.t_video_id and COLLECTION.V_FOLDER=t2.t_folder)
                    when matched then
                        update set v_status=0
                        delete where v_status=0
                    when not matched then
                        insert (v_id, V_USER_ID, V_VIDEO_ID, V_FOLDER)
                        VALUES (seq_collection_id.nextval, t2.t_user_id, t2.t_video_id, t2.t_folder)
                    """;
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, acc);
            stmt.setString(2, pwd);
            stmt.setString(3, videoId);
            stmt.setString(4, folder);

            stmt.execute();

            System.out.println("删除或者收藏成功");
            response.getWriter().write("Success");
        } catch (SQLException e) {
            response.getWriter().write("Error");
            e.printStackTrace();
        } finally {
            PublicFunction.CloseAll(null, stmt, conn);
        }

        System.out.println("======================================================================");

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
