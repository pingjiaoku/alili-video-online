function AjaxGetVideoInfo(videoId){
    $.ajax({
        type:"post",
        url:'ViewVideo',
        data:{
            "videoId":videoId,
            acc: getCookie("alili_acc")+"",
            pwd: getCookie("alili_pwd")+""
        },
        success:function (data){
            if(data == null){
                NoVideoFound();
            } else {
                data = eval(data);
                ConfigVideoInfo(data);
                AjaxGetVideoComment(data["id"], "hot");
                AjaxGetVideoBarrage(data["id"]);
            }
        },
        error:function (){
            alert("视频信息获取失败");
            NoVideoFound();
        }
    })
}


// 找到视频后配置视频信息
function ConfigVideoInfo(data){
    // 设置视频
    let video = document.getElementById("video_player").children[0];
    video.innerHTML = "<source src='./web-resources/video/" + data["videoUrl"]+"'>";
    // 初始化视频控件
    InitVideoController();
    // 设置head里面的Title
    document.title = data["title"];
    // 设置标题
    let title = document.getElementById("main-video-title")
    title.innerText = data["title"];
    // 播放量、弹幕量、发布时间
    let tmp = title.nextElementSibling; // title下个兄弟
    tmp.children[0].children[1].innerText = data["viewCount"];
    tmp.children[1].children[1].innerText = data["barrageCount"];
    tmp.children[2].children[1].innerText = data["publishDate"];
    // 简介
    document.getElementById("video-describe-area").children[0].innerHTML = data["describe"];
    // 点赞收藏
    tmp = document.getElementById("main-video-like-coll")
    if(data["isThumbs"]!==0){
        tmp.children[0].children[0].src = "imgs/icon/dongtai-on.svg";
    }
    if(data["isCollection"]!==0){
        tmp.children[1].children[0].src = "imgs/icon/shoucang-on.svg";
    }
    tmp.children[0].children[1].innerText = data["likes"];
    tmp.children[1].children[1].innerText = data["collection"];
    tmp.children[0].onclick = function () {
        VideoThumbsUp(data["id"]);
    }
    tmp.children[1].onclick = function () {
        VideoCollection(data["id"]);
    }

    // 评论数量和评论排序按钮
    let comInfo = document.getElementById("video-comment-info");
    comInfo.children[1].innerText = data["commentCount"];
    comInfo.children[2].onclick = function (){
        AjaxGetVideoComment(data["id"], "new");
    };
    comInfo.children[4].onclick = function (){
        AjaxGetVideoComment(data["id"], "hot");
    }

    // 作者信息
    document.getElementById("author-main").innerHTML =
        "<div id='author-headshot'>\n" +
        "    <img onclick='ToHomePage(\"" + data["authorId"] + "\")' class=\"cursor-pointer\" src=\"web-resources/headshot/" + data["authorHeadshot"] + "\" alt=\"\">\n" +
        "</div>\n" +
        "<div id=\"author-main-info\">\n" +
        "    <h4  onclick='ToHomePage(\"" + data["authorId"] + "\")' class=\"font-cover-change-color\">" + data["authorName"] + "</h4>\n" +
        "    <p><span id='author-fans'>" + data["authorFans"] + "</span>粉丝 - " + data["authorVideo"] + "视频</p>\n" +
        "    <input id='is-concern' onclick='FollowAuthorChannel(\"" + data["authorId"] + "\")' type=\"button\" value=\"关注\">\n" +
        "</div>";

    if(data["isConcern"] == 1){
        let tmp = document.getElementById("author-main-info");
        tmp.children[2].value = "已关注";
        tmp.children[2].style.backgroundColor = "#a0a0a0";
    }
}

function VideoThumbsUp(id){
    let acc = getCookie("alili_acc");
    let pwd = getCookie("alili_pwd");
    if(acc == null || pwd == null){
        UserLoginTips();
    } else {
        $.ajax({
            type: "post",
            url: "VideoThumbsUp",
            data: {
                acc: acc,
                pwd: pwd,
                id: id
            },
            success: function (data) {
                let like = document.getElementById("main-video-like-coll").children[0];

                if(data==="1"){
                    like.children[1].innerText = parseInt(like.children[1].innerText) + 1;
                    like.children[0].src = "imgs/icon/dongtai-on.svg";
                } else if(data==="2") {
                    like.children[0].src = "imgs/icon/dongtai-off.svg";
                    like.children[1].innerText = parseInt(like.children[1].innerText) - 1;
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
function VideoCollection(id){
    let acc = getCookie("alili_acc");
    let pwd = getCookie("alili_pwd");

    if(acc == null || pwd == null){
        UserLoginTips();
    } else {
        $.ajax({
            type: "post",
            url: "VideoCollection",
            data: {
                acc: acc,
                pwd: pwd,
                videoId: id,
                folder: "默认收藏夹"
            },
            success: function (data){
                if(data === "Success"){
                    let icon = document.getElementById("main-video-like-coll").children[1].children[0];
                    if(icon.src.endsWith("shoucang-off.svg")){
                        icon.src = "imgs/icon/shoucang-on.svg";
                        icon.nextElementSibling.innerText = parseInt(icon.nextElementSibling.innerText)+1;
                    } else {
                        icon.src = "imgs/icon/shoucang-off.svg";
                        icon.nextElementSibling.innerText = parseInt(icon.nextElementSibling.innerText)-1;
                    }
                }
            },
            error: function (){
                alert("服务器连接失败");
            }
        })
    }
}

// 获取对应视频的弹幕
function AjaxGetVideoBarrage(id) {
    $.ajax({
        type:"post",
        url:"GetVideoBarrage",
        data:{
            "videoId":id
        },
        success:function (data){
            StartBarrages(eval(data))
        },
        error:function (){
            alert("弹幕数据获取失败");
        }
    })
}




