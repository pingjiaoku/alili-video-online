package Classes;

/**
 * @description:
 * @time: 21/10/2022 9:02
 */
public class Barrages {
    private Long id;
    private String content;    // 弹幕内容
    private Integer time;      // 视频中出现时间
    private Boolean isMove;    // 是否为移动弹幕
    private String colorIndex; // 颜色
    private String videoId;    // 视频id
    private String userId;     // 发送弹幕用户id
    private String date;       // 发送时间

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public Boolean getIsMove() {
        return isMove;
    }

    public void setIsMove(Boolean move) {
        isMove = move;
    }

    public String getColorIndex() {
        return colorIndex;
    }

    public void setColorIndex(String colorIndex) {
        this.colorIndex = colorIndex;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Barrages{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", time=" + time +
                ", isMove=" + isMove +
                ", colorIndex='" + colorIndex + '\'' +
                ", videoId='" + videoId + '\'' +
                ", userId='" + userId + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
