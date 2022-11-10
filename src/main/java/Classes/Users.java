package Classes;

import java.sql.Date;

/**
 * @description:
 * @time: 9/10/2022 14:38
 */
public class Users {
    private String rowId;
    private String id;
    private String uname;      // 昵称
    private Integer dateCount; // 注册天数
    private String headshot;   // 头像文件名
    private String account;    // 账号
    private String password;   // 密码
    private Integer gender;    // 性别
    private String registerDate; // 注册日期
    private Integer status;    // 状态
    private Integer identity;  // 身份
    private Integer videoNum;  // 发布的视频数量
    private Long likes;        // 被点赞总数
    private Long history;      // 历史播放数
    private Long viewCount;    // 播放数
    private Integer concern;   // 关注人数
    private Long fans;         // 粉丝数
    private Long collection;   // 收藏的视频总数
    private Integer isConcern;  // 是否关注

    public String getRowId() {
        return rowId;
    }

    public void setRowId(String rowId) {
        this.rowId = rowId;
    }

    public Integer getDateCount() {
        return dateCount;
    }

    public void setDateCount(Integer dateCount) {
        this.dateCount = dateCount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getHeadshot() {
        return headshot;
    }

    public void setHeadshot(String headshot) {
        this.headshot = headshot;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public String getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(String registerDate) {
        this.registerDate = registerDate;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getIdentity() {
        return identity;
    }

    public void setIdentity(Integer identity) {
        this.identity = identity;
    }

    public Integer getVideoNum() {
        return videoNum;
    }

    public void setVideoNum(Integer videoNum) {
        this.videoNum = videoNum;
    }

    public Long getLikes() {
        return likes;
    }

    public void setLikes(Long likes) {
        this.likes = likes;
    }

    public Integer getConcern() {
        return concern;
    }

    public void setConcern(Integer concern) {
        this.concern = concern;
    }

    public Long getFans() {
        return fans;
    }

    public void setFans(Long fans) {
        this.fans = fans;
    }

    public Long getCollection() {
        return collection;
    }

    public void setCollection(Long collection) {
        this.collection = collection;
    }

    public Long getHistory() {
        return history;
    }

    public void setHistory(Long history) {
        this.history = history;
    }

    public Long getViewCount() {
        return viewCount;
    }

    public void setViewCount(Long viewCount) {
        this.viewCount = viewCount;
    }

    public Integer getIsConcern() {
        return isConcern;
    }

    public void setIsConcern(Integer isConcern) {
        this.isConcern = isConcern;
    }

    @Override
    public String toString() {
        return "Users{" +
                "rowId='" + rowId + '\'' +
                ", id='" + id + '\'' +
                ", uname='" + uname + '\'' +
                ", dateCount=" + dateCount +
                ", headshot='" + headshot + '\'' +
                ", account='" + account + '\'' +
                ", password='" + password + '\'' +
                ", gender=" + gender +
                ", registerDate=" + registerDate +
                ", status=" + status +
                ", identity=" + identity +
                ", videoNum=" + videoNum +
                ", likes=" + likes +
                ", concern=" + concern +
                ", fans=" + fans +
                ", collection=" + collection +
                '}';
    }
}
