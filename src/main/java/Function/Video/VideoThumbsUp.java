package Function.Video;

import Function.PublicFunction;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import oracle.jdbc.OracleType;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

@WebServlet(name = "VideoThumbsUp", urlPatterns = "/VideoThumbsUp")
public class VideoThumbsUp extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("==用户点赞视频========================================================");
        String acc = request.getParameter("acc");
        String pwd = request.getParameter("pwd");
        String id = request.getParameter("id");
        System.out.println("acc:"+acc+",pwd:"+pwd+",videoId:"+id);

        Connection conn = null;
        CallableStatement stmt = null;
        try {
            conn = PublicFunction.getConnection();
            // 调用存储过程
            stmt = conn.prepareCall("{call PRO_VIDEO_THUMBS(?,?,?,?)}");
            stmt.setString(1, acc);
            stmt.setString(2,pwd);
            stmt.setString(3,id);
            // 传出参数设置，（1：用户已点赞，2：用户已取消点赞，3：发生异常）
            stmt.registerOutParameter(4, OracleType.NUMBER);
            stmt.execute();

            // 取出传出参数
            int flag = stmt.getInt(4);
            if(flag==1){
                System.out.println("用户已点赞该视频");
            } else if (flag==2) {
                System.out.println("用户已取消点赞该视频");
            } else {
                System.out.println("发生异常");
            }
            response.setContentType("text/html;charset=utf-8");
            response.getWriter().write(flag + "");
        } catch (SQLException e) {
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
