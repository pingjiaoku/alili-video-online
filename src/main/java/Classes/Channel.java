package Classes;

import java.awt.*;

/**
 * @description:
 * @time: 11/10/2022 14:12
 */
public class Channel {
    private String id;
    private String content;
    private Boolean display;
    private String img;
    private Integer ordinal;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Boolean getDisplay() {
        return display;
    }

    public void setDisplay(int display) {
        this.display = display == 1;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public Integer getOrdinal() {
        return ordinal;
    }

    public void setOrdinal(Integer ordinal) {
        this.ordinal = ordinal;
    }

    @Override
    public String toString() {
        return "Channel{" +
                "id='" + id + '\'' +
                ", content='" + content + '\'' +
                ", display=" + display +
                ", img='" + img + '\'' +
                ", ordinal=" + ordinal +
                '}';
    }
}
