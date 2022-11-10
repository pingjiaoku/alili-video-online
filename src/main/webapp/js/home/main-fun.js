function DrawHomeUserInfo(data){
    let videoInfo = document.getElementById("user-video-info");
    videoInfo.children[0].children[2].innerText = data["videoNum"];
    videoInfo.children[1].children[2].innerText = data["collection"];
    // videoInfo.children[2].children[2].innerText = data["history"];

    let interactiveInfo = document.getElementById("user-interactive-info");
    interactiveInfo.children[0].children[0].innerText = data["concern"];
    interactiveInfo.children[1].children[0].innerText = data["fans"];
    interactiveInfo.children[2].children[0].innerText = data["likes"];
    // interactiveInfo.children[3].children[0].innerText = data["views"];

    document.getElementById("home-headshot").src = "./web-resources/headshot/" + data["headshot"];
}

function GetUserHomeInfo(userId){
    if(userId != null){
        $.ajax({
            type:"post",
            url:"GetUserHomeInfo",
            data:{
                id:userId,
                acc: getCookie("alili_acc")==null?"null":getCookie("alili_acc"),
                pwd: getCookie("alili_pwd")==null?"null":getCookie("alili_pwd")
            },
            success: function (data){
                if(data != null){
                    document.title = data["uname"] + "的个人空间";

                    DrawHomeUserInfo(data);

                    let str = "";
                    if(data["isConcern"] == 0){
                        str =
                            "<input type='button' value='关注' id='is-concern' class=\"other-btm cursor-pointer\" onClick=\"FollowAuthorChannel('" + userId + "')\">";
                    } else {
                        str =
                            "<input type='button' value='已关注' id='is-concern' style='background-color: #a0a0a0' class=\"other-btm cursor-pointer\" onClick=\"FollowAuthorChannel('" + userId + "')\">";
                    }

                    document.getElementById("home-user-info").insertAdjacentHTML("beforeend", str);
                }
            },
            error: function (){
                alert("服务器连接失败");
            }
        })
    }
}

function ShowUserVideos(obj){
    ChangeVideoInfoIcon(obj);

    document.getElementById("home-video-content").innerHTML =
        "<div>\n" +
        "    <h4>我的视频</h4>\n" +
        "    <ul id=\"video-sort-rule\">\n" +
        "        <li class=\"cursor-pointer\" onclick=\"HomeUserVideoSort('new',this)\"><span>最新发布</span></li>\n" +
        "        <li class=\"cursor-pointer\" onclick=\"HomeUserVideoSort('view',this)\"><span>最多播放</span></li>\n" +
        "        <li class=\"cursor-pointer\" onclick=\"HomeUserVideoSort('collection',this)\"><span>最多收藏</span></li>\n" +
        "    </ul>\n" +
        "</div>\n" +
        "<ul id=\"my-video-list\"></ul>";

    HomeUserVideoSort("new", document.getElementById("video-sort-rule").children[0]);
}

function ShowUserColl(obj){
    ChangeVideoInfoIcon(obj)

    document.getElementById("home-video-content").innerHTML =
        "<div>\n" +
        "    <h4>我的收藏</h4>\n" +
        "    <ul id=\"video-sort-rule\">\n" +
        "        <li class=\"cursor-pointer\" onclick=\"HomeUserVideoColl('默认收藏夹',this)\"><span>默认收藏夹</span></li>\n" +
        "        <li class=\"cursor-pointer\" onclick=\"HomeUserVideoColl('mycoll1',this)\"><span>收藏夹1</span></li>\n" +
        "        <li class=\"cursor-pointer\" onclick=\"HomeUserVideoColl('mycoll2',this)\"><span>收藏夹2</span></li>\n" +
        "    </ul>\n" +
        "</div>\n" +
        "<ul id=\"my-video-list\"></ul>";

    HomeUserVideoColl('默认收藏夹',document.getElementById("video-sort-rule").children[0]);
}

function ChangeVideoInfoIcon(obj){
    let list = document.getElementById("user-video-info");
    for (let i = 0; i < list.childElementCount; i++) {
        if(list.children[i] === obj){
            list.children[i].children[3].style.display = "block";
            list.children[i].children[1].style.color = "#00AEEC";
            list.children[i].children[2].style.color = "#00AEEC";
            let icon = list.children[i].children[0];
            for (let j = 0; j < icon.childElementCount; j++) {
                icon.children[j].style.fill = "#00AEEC";
            }
        } else {
            list.children[i].children[3].style.display = "none";
            list.children[i].children[1].style.color = "#424242";
            list.children[i].children[2].style.color = "#505050";
            let icon = list.children[i].children[0];
            for (let j = 0; j < icon.childElementCount; j++) {
                icon.children[j].style.fill = "rgba(254, 51, 85, 0.5)";
            }
        }
    }
}
function ChangeVideoSortIcon(obj){
    let ch = document.getElementById("video-sort-rule");
    for (let i = 0; i < ch.childElementCount; i++) {
        if(ch.children[i] === obj){
            ch.children[i].style.backgroundColor = "#00AEEC";
            ch.children[i].style.color = "#F0F0F0";
        } else {
            ch.children[i].style.backgroundColor = "#ffffff";
            ch.children[i].style.color = "#000000";
        }
    }
}

function HomeUserVideoColl(type, obj){
    let userId = getQueryString("userId");

    ChangeVideoSortIcon(obj);

    if(type != null && userId != null) {
        $.ajax({
            type: "post",
            url: "GetCollectionVideo",
            data: {
                userId: userId,
                type: type
            },
            success: function (data){
                if(data != null){
                    data = eval(data);
                    let str = "";
                    for (let i = 0; i < data.length; i++) {
                        str +=
                            "<li>\n" +
                            "    <div class=\"intrinsic-aspect-ratio-container\">\n" +
                            "        <div onclick='ToVideoPage(\"" + data[i]["id"] + "\")' class=\"cover cursor-pointer\">\n" +
                            "            <img src=\"web-resources/cover/" + data[i]["coverUrl"] + "\" alt=\"\">\n" +
                            "            <div>\n" +
                            "                <img src='imgs/icon/bofang.svg' alt=''>\n" +
                            "                <p>" + data[i]["viewCount"] + "</p>\n" +
                            "                <span>" + data[i]["totalTime"] + "</span>\n" +
                            "            </div>\n" +
                            "        </div>\n" +
                            "        <div class=\"video-message\">\n" +
                            "            <div>\n" +
                            "                <h3 onclick='ToVideoPage(\"" + data[i]["id"] + "\")' class=\"video-title cursor-pointer\">" + data[i]["title"] + "</h3>\n" +
                            "                <p onclick='ToVideoPage(\"" + data[i]["id"] + "\")' class=\"video-upload cursor-pointer\">收藏于-" + data[i]["collDate"] + "</p>\n" +
                            "            </div>\n" +
                            "        </div>\n" +
                            "    </div>\n" +
                            "</li>";
                    }
                    document.getElementById("my-video-list").innerHTML = str;
                }
            },
            error: function (){
                alert("服务器连接失败");
            }
        })
    }
}