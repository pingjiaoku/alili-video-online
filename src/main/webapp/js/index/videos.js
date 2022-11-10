// 绘制视频列表
function VideoPages(data){
    let row = 4, col = 4;
    let htmlStr = "";
    let i = 0;
    htmlStr += "<ul>";
    for (let k = 0; k < row; k++) {
        htmlStr += "<li><ul class=\"row-video\">";
        for (let j = 0; j < col && i<data.length; j++, i++) {
            htmlStr +=
                "<li>\n" +
            "    <div class=\"intrinsic-aspect-ratio-container\">\n" +
            "        <div onclick='ToVideoPage(\"" + data[i]["id"] + "\")' class=\"cover cursor-pointer\">\n" +
            "            <img src=\"web-resources/cover/" + data[i]["coverUrl"] + "\" alt=\"\">\n" +
            "            <div>" +
            "                <img src='imgs/icon/bofang.svg' alt=''>" +
            "                <p>" + data[i]["viewCount"] + "</p>" +
            "                <span>" + data[i]["totalTime"] + "</span>" +
            "            </div>" +
            "        </div>\n" +
            "        <div class=\"video-message\">\n" +
            "            <img class='cursor-pointer' onclick='ToHomePage(\"" + data[i]["authorId"] + "\")' src=\"web-resources/headshot/" + data[i]["authorHeadshot"] + "\" alt=\"\">\n" +
            "            <div>\n" +
            "                <h3 onclick='ToVideoPage(\"" + data[i]["id"] + "\")' class=\"video-title cursor-pointer\">" + data[i]["title"] + "</h3>\n" +
            "                <p onclick='ToHomePage(\"" + data[i]["authorId"] + "\")' class=\"video-author cursor-pointer\">" + data[i]["authorName"] + " · " + data[i]["publishDate"] + "</p>\n" +
            "            </div>\n" +
            "        </div>\n" +
            "    </div>\n" +
            "</li>";

        }
        htmlStr += "</ul></li>";
    }
    htmlStr += "</ul>";
    document.getElementById("video-content").innerHTML = htmlStr;

    // 重新设置字体大小
    if(data.length !== 0){
        ReFontSize()
    }
}





/*function VideoPages(data){
    let htmlStr = "";
    htmlStr += "<ul>";
    for (let i = 0; i < data.length; i++) {
        htmlStr +=
            "<li>\n" +
            "    <div class=\"intrinsic-aspect-ratio-container\">\n" +
            "        <div onclick='ToVideoPage(\"" + data[i]["id"] + "\")' class=\"cover cursor-pointer\">\n" +
            "            <img src=\"web-resources/cover/" + data[i]["coverUrl"] + "\" alt=\"\">\n" +
            "            <div>" +
            "                <img src='imgs/icon/bofang.svg' alt=''>" +
            "                <p>" + data[i]["viewCount"] + "</p>" +
            "                <span>" + data[i]["totalTime"] + "</span>" +
            "            </div>" +
            "        </div>\n" +
            "        <div class=\"video-message\">\n" +
            "            <img class='cursor-pointer' onclick='ToHomePage(\"" + data[i]["authorId"] + "\")' src=\"web-resources/headshot/" + data[i]["authorHeadshot"] + "\" alt=\"\">\n" +
            "            <div>\n" +
            "                <h3 onclick='ToVideoPage(\"" + data[i]["id"] + "\")' class=\"video-title cursor-pointer\">" + data[i]["title"] + "</h3>\n" +
            "                <p onclick='ToHomePage(\"" + data[i]["authorId"] + "\")' class=\"video-author cursor-pointer\">" + data[i]["authorName"] + " · " + data[i]["publishDate"] + "</p>\n" +
            "            </div>\n" +
            "        </div>\n" +
            "    </div>\n" +
            "</li>";
    }
    htmlStr += "</ul>";
    document.getElementById("video-content").innerHTML = htmlStr;

    // 重新设置字体大小
    ReFontSize();
}*/
