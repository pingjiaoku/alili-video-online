// 获取视频评论
function AjaxGetVideoComment(id, rule) {
    // 用于判断用户是否点赞该评论
    let acc = getCookie("alili_acc");
    let pwd = getCookie("alili_pwd");

    $.ajax({
        type:"post",
        url:"GetVideoComment",
        data:{
            id:id,
            rule: rule,
            acc: acc + "",
            pwd: pwd + "",
        },
        success:function (data){
            data = eval(data);
            // 绘制评论
            DrawVideoComments(data);
        },
        error:function (){
            alert("评论数据获取失败");
        }
    })
}

// 绘制视频评论
function DrawVideoComments(data){

    let comArea = document.getElementById("video-comments");
    let str = "";
    for (let i = 0; i < data.length; i++) {
        let icon = "thumbs-up-off.svg"
        if(data[i]["isThumbs"] === 1) {
            icon = "thumbs-up-on.svg";
        }
        str +=
            "<li>\n" +
            "    <!-- 头像 -->\n" +
            "    <div>\n" +
            "        <img onclick=\"ToHomePage('" + data[i]["userId"] + "')\" class=\"comment-headshot cursor-pointer\" src=\"web-resources/headshot/" + data[i]["headshot"] + "\" alt=\"\">\n" +
            "    </div>\n" +
            "    <div class=\"comment-content\">\n" +
            "        <h6 onclick=\"ToHomePage('" + data[i]["userId"] + "')\" class=\"font-cover-change-color\">" + data[i]["userName"] + "</h6>\n" +
            "        <p>" + data[i]["content"] + "</p>\n" +
            "    </div>\n" +
            "    <ul class=\"comment-message\">\n" +
            "        <li>\n" +
            "            <p>" + data[i]["date"] + "</p>\n" +
            "        </li>\n" +
            "        <li id='video-comment-like-count-" + data[i]["id"] + "' onclick='CommentThumbsUp(" + data[i]["id"] + ")' class=\"comment-like cursor-pointer\">\n" +
            "            <img style='height: 20px; width: 20px;' src=\"imgs/icon/" + icon + "\" alt=\"\">\n" +
            "            <p>" + data[i]["likes"] + "</p>\n" +
            "        </li>\n" +
            "        <li>\n" +
            // "            <p class=\"font-cover-change-color\">回复</p>\n" +
            "        </li>\n" +
            "    </ul>\n" +
            "</li>\n";
    }

    comArea.innerHTML = str;

}

// 发送评论
function AjaxSendComment(comment, content, videoId) {
    let acc = getCookie("alili_acc");
    let pwd = getCookie("alili_pwd");
    if(acc == null || pwd == null){
        // 用户未登录
        UserLoginTips();
        return;
    }
    $.ajax({
        type:"post",
        url:"SendVideoComment",
        data:{
            account: acc,
            password: pwd,
            "videoId": videoId,
            content: content,
            "toComment": "null"
        },
        success:function (data){
            if(data == null){
                alert("AjaxSendComment发生异常");
            }
            else{
                ShowMyComment(data, comment, content)
            }
        },
        error:function (){
            alert("服务器连接失败");
        }
    })
}

// 评论成功插入到数据库中后回显评论
function ShowMyComment(data, comment, content){
    let uname = document.getElementById("userinfo-uname").innerText;
    let headshot = document.getElementById("headshot").src;
    let userId = document.getElementById("userinfo-uname").title;
    let str =
        "<li id='comment-" + data + "'>\n" +
        "    <div>\n" +
        "        <img onclick=\"ToHomePage('" + userId + "')\" class=\"comment-headshot cursor-pointer\" src=\"" + headshot + "\" alt=\"\">\n" +
        "    </div>\n" +
        "    <div class=\"comment-content\">\n" +
        "        <h6 onclick=\"ToHomePage('" + userId + "')\" class=\"font-cover-change-color\">" + uname + "</h6>\n" +
        "        <p>" + content + "</p>\n" +
        "    </div>\n" +
        "    <ul class=\"comment-message\">\n" +
        "        <li>\n" +
        "            <p>" + GetCurrentTime() + "</p>\n" +
        "        </li>\n" +
        "        <li id='video-comment-like-count-" + data + "' onclick='CommentThumbsUp(" + data + ")' class=\"comment-like cursor-pointer\">\n" +
        "            <img style='height: 20px; width: 20px;' src=\"imgs/icon/thumbs-up-off.svg\" alt=\"\">\n" +
        "            <p>0</p>\n" +
        "        </li>\n" +
        "        <li>\n" +
        // "            <p class=\"font-cover-change-color\">回复</p>\n" +
        "            <p onclick='DeleteMyComment(\"" + data + "\")' class=\"font-cover-change-color\">删除</p>\n" +
        "        </li>\n" +
        "    </ul>\n" +
        "</li>";
    document.getElementById("video-comments").insertAdjacentHTML('afterbegin', str);

    comment.value = "";
}

function DeleteMyComment(id) {
    let acc = getCookie("alili_acc");
    let pwd = getCookie("alili_pwd");
    if(acc == null || pwd == null) {
        UserLoginTips();
    } else {
        $.ajax({
            type: "post",
            url: "DeleteMyComment",
            data: {
                acc: acc,
                pwd: pwd,
                "commentId": id
            },
            success: function (data){
                if(data === "Success"){
                    // 删除评论
                    let myComment = document.getElementById("comment-"+id);
                    myComment.parentNode.removeChild(myComment);
                } else {
                    alert("服务器连接失败");
                }
            },
            error: function (){
                alert("服务器连接失败");
            }
        })
    }
}


// 评论点赞功能
function CommentThumbsUp(id){
    // alert("已点赞");
    let acc = getCookie("alili_acc");
    let pwd = getCookie("alili_pwd");
    if(acc == null || pwd == null){
        UserLoginTips();
    } else {
        let likeCount = document.getElementById("video-comment-like-count-"+id);
        $.ajax({
            type:"post",
            url:"CommentThumbsUp",
            data:{
                acc: acc,
                pwd: pwd,
                id: id
            },
            success: function (data){
                if(data==="1"){
                    likeCount.children[0].src = "imgs/icon/thumbs-up-on.svg";
                    likeCount.children[1].innerText = parseInt(likeCount.children[1].innerText) + 1;
                } else if(data==="2") {
                    likeCount.children[0].src = "imgs/icon/thumbs-up-off.svg";
                    likeCount.children[1].innerText = parseInt(likeCount.children[1].innerText) - 1;
                } else {
                    alert("数据异常");
                }
            },
            error: function (){
                alert("服务器连接失败");
            }
        })
    }
}