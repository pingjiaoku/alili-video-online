package Function.Barrage;

import Function.PublicFunction;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@WebServlet(name = "SendVideoBarrage", urlPatterns = "/SendVideoBarrage")
public class SendVideoBarrage extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("==发送弹幕============================================================");
        String acc = request.getParameter("acc");
        String pwd = request.getParameter("pwd");
        String videoId = request.getParameter("videoId");
        String content = request.getParameter("content");
        String time = request.getParameter("time");
        String isMove = request.getParameter("isMove");
        String colorIndex = request.getParameter("colorIndex");
        System.out.println(acc+":"+pwd+":"+videoId+":"+content+":"+time+":"+isMove+":"+colorIndex);

        response.setContentType("text/html;charset=utf-8");

        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = PublicFunction.getConnection();
            conn.setAutoCommit(false);
            String sql = "insert into barrages (v_content,V_VIDEO_ID,V_AUTHOR_ID,V_TIME,v_move,V_COLOR)" +
                    " values (?,?,(select v_id from USERS where V_ACCOUNT=? and V_PASSWORD=?),?,?,?)";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, content);
            stmt.setString(2, videoId);
            stmt.setString(3, acc);
            stmt.setString(4, pwd);
            stmt.setInt(5, Integer.parseInt(time));
            stmt.setInt(6, "true".equals(isMove)?1:0);
            stmt.setString(7, colorIndex);

            stmt.execute();
            conn.commit();

            response.getWriter().write("Success");
        } catch (SQLException e) {
            try {
                if(conn != null) {
                    conn.rollback();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();

        }
        System.out.println("======================================================================");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
