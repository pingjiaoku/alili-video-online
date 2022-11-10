package Function;

import Classes.Users;
import Classes.Video;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.file.Files;
import java.sql.*;
import java.util.Properties;
import java.util.UUID;

/**
 * description: 各种功能的集合类
 * time: 9/10/2022 15:33
 */
public class PublicFunction {
    // 加载驱动
    static {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");//加载数据驱动
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    // 获取数据库连接
    public static Connection getConnection() throws SQLException {
        String url = "jdbc:oracle:thin:@localhost:1521:orcl";

        Properties props = new Properties() ;
        props.put( "user" , "ALILISYS") ;
        props.put( "password" , "123456") ;
        props.put( "oracle.net.CONNECT_TIMEOUT" , "10000") ;
        props.put( "oracle.jdbc.ReadTimeout" , "2000" ) ;

        return DriverManager.getConnection(url, props);
    }

    // 关闭连接
    public static void CloseAll(ResultSet rs, PreparedStatement stmt, Connection conn){
        if(rs!=null){
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if(stmt!=null){
            try {
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if(conn!=null){
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * description: 保存各种文件，并返回文件名
     * @param request：
     * @param type： 文件类型，只能是headshot、cover和video
     * return: 文件名，
     * time: 9/10/2022 15:41
     */
    public static String SaveFile(@NotNull HttpServletRequest request,@NotNull String type) throws ServletException, IOException {
        String fileName = null;
        if("cover".equals(type) || "video".equals(type) || "headshot".equals(type)) {
            // 获取文件
            Part file = request.getPart(type);
            if (file.getSize() == 0) {
                System.out.println("没有文件");
                return "headshot".equals(type) ? "default.svg" : null;
            } else {
                // 获取文件名
                fileName = file.getSubmittedFileName();
                // 获取文件名后缀
                fileName = fileName.substring(fileName.lastIndexOf("."));

                // 文件不合规
                if (!CheckFile(fileName, type))
                    return null;

                // 制定唯一的文件名
                fileName = UUID.randomUUID() + fileName;
                try {
                    // 保存到服务器
                    String sourceUrl = request.getServletContext().getRealPath("/") + "\\web-resources\\" + type + "\\" + fileName;
                    System.out.println("服务器文件保存位置："+sourceUrl);
                    file.write(sourceUrl);

                    // 复制到项目里
                    File sourceFile = new File(sourceUrl);
                    String destUrl = "D:\\javaweb\\ALL\\ALILI\\src\\main\\webapp\\web-resources\\" + type + "\\" + fileName;
                    File destFile = new File(destUrl);
                    System.out.println("备份文件位置：" + destUrl);
                    Files.copy(sourceFile.toPath(), destFile.toPath());
                } catch (IOException e) {
                    System.out.println("文件保存失败，请检查文件和路径是否正确--" + type + "：" + fileName);
                    fileName = null;
                    e.printStackTrace();
                }
            }
        }
        return fileName;
    }
    private static boolean CheckFile(String name,@NotNull String type){
        String[] img = {".png", ".jpg", ".jpeg", ".webp"};
        String[] video = {".avi",".mp4",".mpeg",".mov",".webm",".m4v"};

        if(type.equals("video")){
            for (String s:video) {
                if(s.equals(name))
                    return true;
            }
        } else {
            for (String s:img){
                if(s.equals(name))
                    return true;
            }
        }
        return false;
    }

    // 每次登录成功或者注册成功刷新cookie存活时间
    public static void AddCookie(HttpServletResponse response, String acc, String pwd){

        Cookie account = new Cookie("alili_acc", acc);
        Cookie password = new Cookie("alili_pwd", pwd);
        account.setMaxAge(60*60*24*7);
        password.setMaxAge(60*60*24*7);
        account.setPath("/");
        password.setPath("/");
        response.addCookie(account);
        response.addCookie(password);
    }



    // 删除cookie，用于登出和登录时传入错误的账号密码
    public static void DropAccPwdCookie(HttpServletResponse response){
        Cookie cookie1 = new Cookie("alili_acc", null);
        Cookie cookie2 = new Cookie("alili_pwd", null);
        cookie1.setMaxAge(0);
        cookie2.setMaxAge(0);
        cookie1.setPath("/");
        cookie2.setPath("/");
        response.addCookie(cookie1);
        response.addCookie(cookie2);
    }


    public static Users SelectUserByAccPwd(Connection conn, String acc, String pwd){
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            String sql = "select V_ID, V_NAME, V_HEADSHOT, v_register_date, floor(TO_NUMBER(sysdate-V_REGISTER_DATE)) date_count, " +
                    "V_GENDER, V_LIKES, V_CONCERN, V_FANS, v_video, v_collection from USERS where v_account = ? and v_password = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1,acc);
            stmt.setString(2,pwd);

            rs = stmt.executeQuery();
            if(rs.next()){
                Users user = new Users();
                user.setId(rs.getString("v_id"));
                user.setUname(rs.getString("v_name"));
                user.setDateCount(rs.getInt("date_count"));
                user.setHeadshot(rs.getString("v_headshot"));
                user.setGender(rs.getInt("v_gender"));
                user.setRegisterDate(rs.getString("v_register_date"));
                user.setLikes(rs.getLong("v_likes"));
                user.setConcern(rs.getInt("v_concern"));
                user.setFans(rs.getLong("v_fans"));
                user.setVideoNum(rs.getInt("v_video"));
                user.setCollection(rs.getLong("v_collection"));
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            PublicFunction.CloseAll(rs,stmt,null);
        }
        return null;
    }




}
