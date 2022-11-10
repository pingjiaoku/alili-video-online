import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

@MultipartConfig
@WebServlet(name = "testAjax", urlPatterns = "/testAjax")
public class testAjax extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Part video = request.getPart("video");
        Part cover = request.getPart("cover");
        String submittedFileName = video.getSubmittedFileName();
        System.out.println(submittedFileName);
        String submittedFileName1 = cover.getSubmittedFileName();
        System.out.println(submittedFileName1);
        String title = request.getParameter("title");
        System.out.println(title);
        String channel = request.getParameter("channel");
        System.out.println(channel);
        String[] tags = request.getParameterValues("tags");
        for (String tag : tags) {
            System.out.print(tag+":");
        }
        response.setContentType("text/html;charset=utf-8");
        response.getWriter().write("success");

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
