import Classes.Channel;
import Classes.Comment;
import Classes.Users;
import Classes.Video;
import Function.PublicFunction;
import com.alibaba.fastjson.JSON;
import oracle.jdbc.OracleType;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

/**
 * @description:
 * @ 20/9/2022 21:11
 */
public class test {
    public static void main(String[] args) {
        class barrages{
            String content; int time; boolean ismove; String colorIndex;
            barrages(String content, int time, boolean ismove, String colorIndex){
                this.content=content;
                this.time=time;
                this.ismove=ismove;
                this.colorIndex=colorIndex;
            }
        }
        barrages[] json = new barrages[]{
        new barrages("ABCDEFGHIJKLMNOPQRSTUVWXYZ",1,false,"#fff"),
        new barrages("233333333333333",2,true,"#fff"),
        new barrages("干杯，哈哈哈~~~~~~",2,true,"rgba(255,0,255,0.5)"),
        new barrages("干杯，哈哈哈~~~~~~",2,true,"rgba(255,0,255,0.5)"),
        new barrages("干杯，哈哈哈~~~~~~",2,true,"rgba(255,0,255,0.5)"),
        new barrages("干杯，哈哈哈~~~~~~",2,true,"rgba(255,0,255,0.5)"),
        new barrages("干杯，哈哈哈~~~~~~",2,true,"rgba(255,0,255,0.5)"),
        new barrages("干杯，哈哈哈~~~~~~",2,true,"rgba(255,0,255,0.5)"),
        new barrages("干杯，哈哈哈~~~~~~",2,true,"rgba(255,0,255,0.5)"),
        new barrages("干杯，哈哈哈~~~~~~",2,true,"rgba(255,0,255,0.5)"),
        new barrages("干杯，哈哈哈~~~~~~",2,true,"rgba(255,0,255,0.5)"),
        new barrages("干杯，哈哈哈~~~~~~",2,true,"rgba(255,0,255,0.5)"),
        new barrages("干杯，哈哈哈~~~~~~",2,true,"rgba(255,0,255,0.5)"),
        new barrages("干杯，哈哈哈~~~~~~",2,true,"rgba(255,0,255,0.5)"),
        new barrages("干杯，哈哈哈~~~~~~",2,true,"rgba(255,0,255,0.5)"),
        new barrages("干杯，哈哈哈~~~~~~",2,true,"rgba(255,0,255,0.5)"),
        new barrages("干杯，哈哈哈~~~~~~",2,true,"rgba(255,0,255,0.5)"),
        new barrages("干杯，哈哈哈~~~~~~",2,true,"rgba(255,0,255,0.5)"),
        new barrages("干杯，哈哈哈~~~~~~",2,true,"rgba(255,0,255,0.5)"),
        new barrages("干杯，哈哈哈~~~~~~",2,true,"rgba(255,0,255,0.5)"),
        new barrages("干杯，哈哈哈~~~~~~",2,false,"#0FF"),
        new barrages("干杯，哈哈哈~~~~~~",2,true,"#0FF"),
        new barrages("干杯，哈哈哈~~~~~~",2,true,"#fff"),
        new barrages("干杯，哈哈哈~~~~~~",2,false,"#000"),
        new barrages("233333333333333",3,true,"#fff"),
        new barrages("233333333333333",3,false,"#fff"),
        new barrages("233333333333333",3,true,"#000"),
        new barrages("233333333333333",3,true,"#fff"),
        new barrages("23333333345555555555",3,true,"#fff"),
        new barrages("233333333333333",3,true,"#fff"),
        new barrages("233333333333333",4,false,"#fff"),
        new barrages("老师说的非常好，我要好好学习了》》》发》",4,false,"#fff"),
        new barrages("老铁们，小礼物走一波了，小汽车小火箭刷起来吧=========",4,true,"#fff"),
        new barrages("老铁们，小礼物走一波了，小汽车小火箭刷起来吧=========",4,true,"#fff"),
        new barrages("老铁们，小礼物走一波了，小汽车小火箭刷起来吧=========",4,true,"#fff"),
        new barrages("老铁们，小礼物走一波了，小汽车小火箭刷起来吧=========",4,true,"#fff"),
        new barrages("老铁们，小礼物走一波了，小汽车小火箭刷起来吧=========",4,true,"#fff"),
        new barrages("老铁们，小礼物走一波了，小汽车小火箭刷起来吧=========",4,true,"#fff"),
        new barrages("老铁们，小礼物走一波了，小汽车小火箭刷起来吧=========",4,true,"#fff"),
        new barrages("老铁们，小礼物走一波了，小汽车小火箭刷起来吧=========",4,true,"#fff"),
        new barrages("老铁们，小礼物走一波了，小汽车小火箭刷起来吧=========",4,true,"#fff"),
        new barrages("老铁们，小礼物走一波了，小汽车小火箭刷起来吧=========",4,true,"#fff"),
        new barrages("老铁们，小礼物走一波了，小汽车小火箭刷起来吧=========",4,true,"#fff"),
        new barrages("老铁们，小礼物走一波了，小汽车小火箭刷起来吧=========",4,true,"#fff"),
        new barrages("老铁们，小礼物走一波了，小汽车小火箭刷起来吧=========",4,true,"#fff"),
        new barrages("老铁们，小礼物走一波了，小汽车小火箭刷起来吧=========",4,true,"#fff"),
        new barrages("老铁们，小礼物走一波了，小汽车小火箭刷起来吧=========",4,true,"#fff"),
        new barrages("老铁们，小礼物走一波了，小汽车小火箭刷起来吧=========",4,true,"#fff"),
        new barrages("老铁们，小礼物走一波了，小汽车小火箭刷起来吧=========",4,true,"#fff"),
        new barrages("老铁们，小礼物走一波了，小汽车小火箭刷起来吧=========",4,true,"#fff"),
        new barrages("老铁们，小礼物走一波了，小汽车小火箭刷起来吧=========",4,true,"#fff"),
        new barrages("老铁们，小礼物走一波了，小汽车小火箭刷起来吧=========",4,true,"#fff"),
        new barrages("老铁们，小礼物走一波了，小汽车小火箭刷起来吧=========",4,true,"#fff"),
        new barrages("老铁们，小礼物走一波了，小汽车小火箭刷起来吧=========",4,true,"#fff"),
        new barrages("老铁们，小礼物走一波了，小汽车小火箭刷起来吧=========",4,true,"#fff"),
        new barrages("老铁们，小礼物走一波了，小汽车小火箭刷起来吧=========",4,true,"#fff"),
        new barrages("老铁们，小礼物走一波了，小汽车小火箭刷起来吧=========",4,true,"#fff"),
        new barrages("老铁们，小礼物走一波了，小汽车小火箭刷起来吧=========",4,true,"#fff"),
        new barrages("老铁们，小礼物走一波了，小汽车小火箭刷起来吧=========",4,true,"#fff"),
        new barrages("老铁们，小礼物走一波了，小汽车小火箭刷起来吧=========",4,true,"#fff"),
        new barrages("老铁们，小礼物走一波了，小汽车小火箭刷起来吧=========",4,true,"#fff"),
        new barrages("老铁们，小礼物走一波了，小汽车小火箭刷起来吧=========",4,true,"#fff"),
        new barrages("老铁们，小礼物走一波了，小汽车小火箭刷起来吧=========",4,true,"#fff"),
        new barrages("老铁们，小礼物走一波了，小汽车小火箭刷起来吧=========",4,true,"#fff"),
        new barrages("老铁们，小礼物走一波了，小汽车小火箭刷起来吧=========",4,true,"#fff"),
        new barrages("老铁们，小礼物走一波了，小汽车小火箭刷起来吧=========",4,true,"#fff"),
        new barrages("老铁们，小礼物走一波了，小汽车小火箭刷起来吧=========",4,true,"#fff"),
        new barrages("老铁们，小礼物走一波了，小汽车小火箭刷起来吧=========",4,true,"#fff"),
        new barrages("老铁们，小礼物走一波了，小汽车小火箭刷起来吧=========",4,true,"#fff"),
        new barrages("老铁们，小礼物走一波了，小汽车小火箭刷起来吧=========",4,false,"#000"),
        new barrages("老铁们，小礼物走一波了，小汽车小火箭刷起来吧=========",4,false,"#000"),
        new barrages("老铁们，小礼物走一波了，小汽车小火箭刷起来吧=========",4,false,"#000"),
        new barrages("老铁们，小礼物走一波了，小汽车小火箭刷起来吧=========",4,false,"#000"),
        new barrages("老铁们，小礼物走一波了，小汽车小火箭刷起来吧=========",4,false,"#000"),
        new barrages("老铁们，小礼物走一波了，小汽车小火箭刷起来吧=========",4,false,"#000"),
        new barrages("老铁们，小礼物走一波了，小汽车小火箭刷起来吧=========",4,false,"#000"),
        new barrages("老铁们，小礼物走一波了，小汽车小火箭刷起来吧=========",4,false,"#000"),
        new barrages("老铁们，小礼物走一波了，小汽车小火箭刷起来吧=========",4,false,"#000"),
        new barrages("老铁们，小礼物走一波了，小汽起来吧=========",4,false,"#000"),
        new barrages("老铁们，小礼物走一波了，小汽起来吧=========",4,false,"#000"),
        new barrages("老铁们，小礼物走一波了，小汽起来吧=========",4,false,"#000"),
        new barrages("老铁们，小礼物走一波了，小汽起来吧=========",4,false,"#000"),
        new barrages("老铁们，小礼物走一波了，小汽起来吧=========",4,false,"#000"),
        new barrages("老铁们，小礼物走一波了，小汽起来吧=========",4,false,"#000"),
        new barrages("老铁们，小礼物走一波了，小汽起来吧=========",4,false,"#000"),
        new barrages("老铁们，小礼物走一波了，小汽起来吧=========",4,false,"#000"),
        new barrages("老铁们，小礼物走一波了，小汽车小火箭刷起来吧=========",4,false,"#000"),
        new barrages("老铁们，小礼物走一波汽车小火箭刷起来吧=========",4,false,"#000"),
        new barrages("老铁们，小礼物走一波汽车小火箭刷起来吧=========",4,false,"#000"),
        new barrages("老铁们，小礼物走一波汽车小火箭刷起来吧=========",4,false,"#000"),
        new barrages("老铁们，小礼物走一波汽车小火箭刷起来吧=========",4,false,"#000"),
        new barrages("老铁们，小礼物走一波了，小汽车小火箭刷起来吧=========",4,false,"#000"),
        new barrages("老铁们，小礼物走一波了，小汽车小火箭刷起来吧=========",4,false,"#000"),
        new barrages("老铁们，小一波了，小汽车小火箭刷起来吧=========",4,false,"#000"),
        new barrages("老铁们，小一波了，小汽车小火箭刷起来吧=========",4,false,"#000"),
        new barrages("老铁们，小一波了，小汽车小火箭刷起来吧=========",4,false,"#000"),
        new barrages("老铁们，小一波了，小汽车小火箭刷起来吧=========",4,false,"#000"),
        new barrages("老铁们，小一波了，小汽车小火箭刷起来吧=========",4,false,"#000"),
        new barrages("老铁们，小一波了，小汽车小火箭刷起来吧=========",4,false,"#000"),
        new barrages("老铁们，小一波了，小汽车小火箭刷起来吧=========",4,false,"#000"),
        new barrages("老铁们，小一波了，小汽车小火箭刷起来吧=========",4,false,"#000"),
        new barrages("老铁们，小礼物走一波了，小汽车小火箭刷起来吧=========",4,false,"#000"),
        new barrages("老铁们，小礼物走一波了，小汽车小火箭刷起来吧=========",4,false,"#000"),
        new barrages("老铁们，小礼物走一波了，小汽车小火箭刷起来吧=========",4,false,"#000"),
        new barrages("老铁们，小礼物走一波了，小汽车小火箭刷起来吧=========",4,false,"#000"),
        new barrages("老铁们，小礼物走一波了，小汽车小火箭刷起来吧=========",4,true,"#fff"),
        new barrages("老铁们，小礼物走一波了，小汽车小火箭刷起来吧=========",4,false,"#fff"),
        new barrages("老铁们，小礼物走一波了，小汽车小火箭刷起来吧=========",5,true,"#FF0"),
        new barrages("老铁们，小礼物走一波了，小汽车小火箭刷起来吧=========",5,true,"#fff"),
        new barrages("2333333343333333",5,true,"#0FF"),
        new barrages("233333333333333",6,true,"#00F"),
        new barrages("233333333333333",7,true,"#00F"),
        new barrages("233333333333333",7,true,"#00F"),
        new barrages("233333333333333",7,true,"#00F"),
        new barrages("老师说的非常好，我要好好学习了》》》发》",7,false,"#fff"),
        new barrages("老师说的非常好，我要好好学习了》》》发》",7,false,"#fff"),
        new barrages("233333333333333",7,true,"#00F"),
        new barrages("233333333333333",7,true,"#00F"),
        new barrages("233333333333333",7,false,"#00F"),
        new barrages("233333333333333",8,true,"#fff"),
        new barrages("233333333333333",9,true,"#fff"),
        new barrages("233333333333333",10,true,"#000"),
        new barrages("老师说的非常好，我要好好学习了》》》》",12,true,"#fff"),
        new barrages("老师说的非常好，我要好好学习了》》》》",13,true,"#fff"),
        new barrages("老师说的非常好，我要好好学习了》》》》",14,true,"#00F"),
        new barrages("老师说的非常好，我要好好学习了》》》发》",15,false,"#fff"),
        new barrages("老师说的非常好，我要好好学习了》》》》",16,true,"#00F"),
        new barrages("老师说的非常好，我要好好学习了》》》》",17,true,"#FF0"),
        new barrages("老师说的非常好，我要好好学习了》》》》",18,true,"#00F"),
        new barrages("老师说的非常好，我要好好学习了》》》发》",18,false,"#fff"),
        new barrages("老师说的非常好，我要好好学习了》》》》",19,true,"#fff"),
        new barrages("老师说的非常好，我要好好学习了》》》》",20,true,"#FF0"),
        new barrages("老师说的非常好，我要好好学习了》》》》",21,true,"#fff"),
        new barrages("老师说的非常好，我要好好学习了》》》》",22,true,"#fff"),
        new barrages("老铁们，小礼物走一波了，小汽车小火箭刷起来吧=========",23,true,"#fff"),
        new barrages("老铁们，小礼物走一波了，小汽车小火箭刷起来吧=========",24,false,"#fff"),
        new barrages("老铁们，小礼物走一波了，小汽车小火箭刷起来吧=========",25,true,"#FF0"),
        new barrages("老铁们，小礼物走一波了，小汽车小火箭刷起来吧=========",26,true,"#fff"),
        new barrages("老铁们，小礼物走一波了，小汽车小火箭刷起来吧=========",27,true,"#F0F"),
        new barrages("老铁们，小礼物走一波了，小汽车小火箭刷起来吧=========",28,false,"#F0F"),
        new barrages("老铁们，小礼物走一波了，小汽车小火箭刷起来吧=========",29,true,"#F0F"),
        new barrages("老铁们，小礼物走一波了，小汽车小火箭刷起来吧=========",30,true,"#F0F"),
        new barrages("马上就下课了，瓦罗蓝大陆走起了～～～",31,true,"#F0F"),
        new barrages("马上就下课了，瓦罗蓝大陆走起了～～～",32,true,"#00F"),
        new barrages("马上就下课了，瓦罗蓝大陆走起了～～～",33,false,"#00F"),
        new barrages("马上就下课了，瓦罗蓝大陆走起了～～～",33,true,"#F0F"),
        new barrages("马上就下课了，瓦罗蓝大陆走起了～～～",34,false,"#F0F"),
        new barrages("马上就下课了，瓦罗蓝大陆走起了～～～",35,true,"#F0F"),
        new barrages("马上就下课了，瓦罗蓝大陆走起了～～～",36,true,"#00F"),
        new barrages("马上就下课了，瓦罗蓝大陆走起了～～～",37,true,"#00F")};

        try {
            Connection conn = PublicFunction.getConnection();
            conn.setAutoCommit(false);
            for (barrages barrages : json) {
                String sql = "insert into BARRAGES values (SEQ_BARRAGE_ID.nextval,?,'vid1','alili_3D',sysdate,?,?,?)";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, barrages.content);
                stmt.setInt(2, barrages.time);
                stmt.setInt(3, barrages.ismove ? 1 : 0);
                stmt.setString(4, barrages.colorIndex);
                stmt.execute();

                conn.commit();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



}


