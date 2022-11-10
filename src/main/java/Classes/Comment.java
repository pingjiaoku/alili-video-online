package Classes;

/**
 * @description:
 * @time: 16/10/2022 14:38
 */
public class Comment {
    private Long id;
    private String content;  // 评论内容
    private String date;     // 评论日期
    private String userId;   // 评论者id
    private String userName; // 评论者昵称
    private String headshot; // 评论者头像地址
    private String videoId;  // 视频id
    private Long likes;      // 点赞数
    private Long toComment;  // 是否为回复评论的评论
    private Long massageId;  // 对应消息的id
    private Integer isThumbs;// 是否点赞

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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getHeadshot() {
        return headshot;
    }

    public void setHeadshot(String headshot) {
        this.headshot = headshot;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public Long getLikes() {
        return likes;
    }

    public void setLikes(Long likes) {
        this.likes = likes;
    }

    public Long getToComment() {
        return toComment;
    }

    public void setToComment(Long toComment) {
        this.toComment = toComment;
    }

    public Long getMassageId() {
        return massageId;
    }

    public void setMassageId(Long massageId) {
        this.massageId = massageId;
    }

    public Integer getIsThumbs() {
        return isThumbs;
    }

    public void setIsThumbs(Integer isThumbs) {
        this.isThumbs = isThumbs;
    }
}
