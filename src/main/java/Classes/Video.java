package Classes;

import java.util.Collection;

/**
 * @description:
 * @time: 14/10/2022 14:42
 */
public class Video {
    private String id;
    private String title;         // 视频标题
    private String authorId;      // 作者id
    private String videoUrl;      // 视频地址
    private String coverUrl;      // 封面地址
    private String describe;      // 简介
    private String totalTime;     // 总时长 “hh:mm:ss”
    private String publishDate;   // 发布时间
    private Integer channelId;    // 所属频道id
    private Long viewCount;       // 播放数
    private Long barrageCount;    // 弹幕数
    private Long commentCount;    // 评论数
    private Long likes;           // 点赞数
    private Long collection;      // 收藏数
    private String collDate;      // 收藏日期，用于获取某用户的收藏列表
    private Long collId;          // 收藏表对应的id
    private String authorName;    // 作者名称
    private String authorHeadshot;// 作者头像地址
    private Long authorFans;      // 作者粉丝数
    private Integer authorVideo;  // 作者视频数
    private Integer isThumbs;     // 是否点赞视频
    private Integer isCollection; // 是否收藏该视频
    private Integer isConcern;    // 是否关注作者



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public String getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(String totalTime) {
        this.totalTime = totalTime;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
    }

    public Integer getChannelId() {
        return channelId;
    }

    public void setChannelId(Integer channelId) {
        this.channelId = channelId;
    }

    public Long getViewCount() {
        return viewCount;
    }

    public void setViewCount(Long viewCount) {
        this.viewCount = viewCount;
    }

    public Long getBarrageCount() {
        return barrageCount;
    }

    public void setBarrageCount(Long barrageCount) {
        this.barrageCount = barrageCount;
    }

    public Long getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(Long commentCount) {
        this.commentCount = commentCount;
    }

    public Long getLikes() {
        return likes;
    }

    public void setLikes(Long likes) {
        this.likes = likes;
    }

    public Long getCollection() {
        return collection;
    }

    public void setCollection(Long collection) {
        this.collection = collection;
    }

    public String getCollDate() {
        return collDate;
    }

    public void setCollDate(String collDate) {
        this.collDate = collDate;
    }

    public Long getCollId() {
        return collId;
    }

    public void setCollId(Long collId) {
        this.collId = collId;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getAuthorHeadshot() {
        return authorHeadshot;
    }

    public void setAuthorHeadshot(String authorHeadshot) {
        this.authorHeadshot = authorHeadshot;
    }

    public Long getAuthorFans() {
        return authorFans;
    }

    public void setAuthorFans(Long authorFans) {
        this.authorFans = authorFans;
    }

    public Integer getAuthorVideo() {
        return authorVideo;
    }

    public void setAuthorVideo(Integer authorVideo) {
        this.authorVideo = authorVideo;
    }

    public Integer getIsThumbs() {
        return isThumbs;
    }

    public void setIsThumbs(Integer isThumbs) {
        this.isThumbs = isThumbs;
    }

    public Integer getIsCollection() {
        return isCollection;
    }

    public void setIsCollection(Integer isCollection) {
        this.isCollection = isCollection;
    }

    public Integer getIsConcern() {
        return isConcern;
    }

    public void setIsConcern(Integer isConcern) {
        this.isConcern = isConcern;
    }

    @Override
    public String toString() {
        return "Video{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", authorId='" + authorId + '\'' +
                ", videoUrl='" + videoUrl + '\'' +
                ", coverUrl='" + coverUrl + '\'' +
                ", describe='" + describe + '\'' +
                ", totalTime='" + totalTime + '\'' +
                ", publishDate='" + publishDate + '\'' +
                ", channelId=" + channelId +
                ", viewCount=" + viewCount +
                ", barrageCount=" + barrageCount +
                ", commentCount=" + commentCount +
                ", likes=" + likes +
                ", Collection=" + collection +
                ", authorName='" + authorName + '\'' +
                ", authorHeadshot='" + authorHeadshot + '\'' +
                ", authorFans=" + authorFans +
                ", authorVideo=" + authorVideo +
                '}';
    }
}
