package Function.Barrage;

import Classes.Barrages;
import Function.PublicFunction;
import com.alibaba.fastjson2.JSON;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

@WebServlet(name = "GetVideoBarrage", urlPatterns = "/GetVideoBarrage")
public class GetVideoBarrage extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("==获取弹幕============================================================");
        String videoId = request.getParameter("videoId");

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = PublicFunction.getConnection();
            ArrayList<Barrages> list = new ArrayList<>();

            stmt = conn.prepareStatement("select v_id, V_CONTENT, to_char(v_date,'yyyy-mm-dd hh24:mi:ss') t_date, v_time, v_move,v_color from BARRAGES where V_VIDEO_ID=?");
            stmt.setString(1, videoId);

            rs = stmt.executeQuery();
            while(rs.next()){
                Barrages brg = new Barrages();
                brg.setId(rs.getLong("v_id"));
                brg.setContent(rs.getString("v_content"));
                brg.setDate(rs.getString("t_date"));
                brg.setTime(rs.getInt("v_time"));
                brg.setIsMove(rs.getInt("v_move") == 1);
                brg.setColorIndex(rs.getString("v_color"));

                System.out.println(brg);
                list.add(brg);
            }


            String s = JSON.toJSONString(list);
            System.out.println(s);
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().write(s);
        } catch (SQLException e) {
            response.setContentType("text/html;charset=utf-8");
            response.getWriter().write("Error");
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
