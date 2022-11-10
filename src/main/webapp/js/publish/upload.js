// 获取推荐tags
function AjaxGetRecommendedTags(channelId){
    $.ajax({
        type: "post",
        url: "GetRecommendTags",
        data: {
            "channelId": channelId
        },
        success: function (data) {
            if(data === "Error"){

            }else {
                let str = "";
                data = eval(data);
                for (let i = 0; i < data.length; i++) {
                    str += "<span onclick=\"AddTagOfRecommend(this)\">&nbsp;&nbsp;#<span>" + data[i]["name"] + "</span>&nbsp;&nbsp;</span>";
                }
                document.getElementById("recommended-tags").innerHTML = str;
            }
        }
    })
}

// 投稿
function AjaxVideoPublish(){
    if(CheckData()){
        ReplaceDescribe();
        SetHideInfo();
        $.ajax({
            type:"post",
            url:"UploadVideo",
            data: new FormData(document.getElementById("publish-page")),
            //是否缓存
            cache: false,
            //当设置为false的时候,jquery 的ajax 提交的时候会序列化 data
            processData: false,
            /* contentType默认值：application/x-www-form-urlencoded；
             * 表单中设置的contentType为"multipart/form-data"；
             * ajax 中 contentType 设置为 false ，是为了避免 JQuery对要提交的表单中的enctype值修改*/
            contentType: false,
            success:function (data){
                if(data !== "Error"){
                    UploadVideoSuccess(data);
                }
            },
            error:function (){
                alert("服务器连接失败");
            }
        })
    }
}

function UploadVideoSuccess(id){
    document.getElementById("main-page").innerHTML =
        "<div id=\"success-page\">\n" +
        "    <div>\n" +
        "        <img src=\"imgs/icon/publish/upload-success.svg\" onload=\"SVGInject(this)\" alt=\"\">\n" +
        "        <p>发布成功</p>\n" +
        "    </div>\n" +
        "    <input type=\"button\" onclick=\"ToVideoPage('" + id + "')\" value=\"点击跳转到视频页面\">\n" +
        "</div>";
}
// 将简介中的空格、回车替换了
function ReplaceDescribe(){
    // 获取评论数据
    let describe = document.getElementById("video-describe");
    let content = describe.value;
    content = content.replace(/\r\n/g, '<br/>'); //IE9、FF、chrome
    content = content.replace(/\n/g, '<br/>'); //IE7-8
    content = content.replace(/\s/g, '&nbsp;'); //空格处理
    describe.value = content;
}
// 设置其他信息
function SetHideInfo() {
    let hideInfo = document.getElementById("hide-info");
    let video = document.getElementById("publish-video").children[2].children[0];
    let tags = document.getElementById("tag").children[2];
    hideInfo.children[0].value=getCookie("alili_acc");
    hideInfo.children[1].value=getCookie("alili_pwd");
    hideInfo.children[2].value=getTimeHMS(video.duration);
    hideInfo.children[3].value=tags.childElementCount;

}


// 检测视频和封面和标题是否设置
function CheckData(){
    let video = document.getElementById("video").value;
    let cover =  document.getElementById("cover").children[1].value;
    let title = document.getElementById("video-title");
    if(video === ""){
        document.getElementById("publish-video").children[2].style.border = "2px solid red";
    }
    if(cover === "") {
        document.getElementById("cover").style.border = "2px solid red";

    }
    if(title.value.length === 0){
        title.style.border = "2px solid red";
    }

    return cover!=="" && video!=="" && title.value.length>0;
}




function AddPublishListener() {
    // 设置视频回显
    let video = document.getElementById("publish-video").children[2];
    video.children[1].addEventListener("click", function () {
        video.children[0].src = "";
        this.addEventListener("change", function () {
            let videoUrl = getObjectURL(this.files[0])
            console.log(videoUrl);
            if (videoUrl) {
                video.children[0].src = videoUrl;
                video.style.border = "none";

            }

        })
    })

    document.getElementById("video-title").addEventListener("focus", function (){
        this.style.border = "1px solid #909090";
    })

    // 设置封面时回显
    let cover = document.getElementById("cover");
    cover.children[1].addEventListener("click", function () {
        cover.style.backgroundImage = "none";
        this.addEventListener("change", function () {
            cover.children[0].style.display = "none";
            let file = this.files[0];
            let fr = new FileReader();
            // 当图片加载好后设为背景
            fr.addEventListener("load", function () {
                cover.style.backgroundImage = "url(" + this.result + ")";
                cover.style.border = "1px solid #f0f0f0";
            })
            fr.readAsDataURL(file);
        })
    })

    // 频道更改时，获取推荐标签
    document.getElementById("publish-channels").addEventListener("change", function (){
        AjaxGetRecommendedTags(this.value);
    })


    document.getElementById("create-tag").addEventListener("keydown", function (e) {
        if (e.key === "Enter") {
            let tags = document.getElementById("tag").children[2];
            if (tags.childElementCount >= 10) {
                alert("最多只能添加十个标签哦");
            } else {
                let val = this.value.trim();
                if (val !== "") {
                    let str =
                        "<label onclick=\"this.parentNode.removeChild(this)\">\n" +
                        "    <input type=\"checkbox\" name=\"tags\" value=\"" + val + "\" checked>\n" +
                        "    <span>&nbsp;&nbsp;#" + val + "</span>\n" +
                        "    <img class=\"delete-tag-icon\" src=\"imgs/icon/publish/close.svg\" onload=\"SVGInject(this)\" alt=\"\">\n" +
                        "</label>";
                    tags.insertAdjacentHTML("beforeend", str);
                }
                this.value = "";
            }
        }
    })
}



// 建立一个可存取到该file的url
function getObjectURL(file) {
    let url = null;
    if (window.createObjectURL !== undefined) { // basic
        url = window.createObjectURL(file);
    } else if (window.URL !== undefined) { // mozilla(firefox)
        url = window.URL.createObjectURL(file);
    } else if (window.webkitURL !== undefined) { // webkit or chrome
        url = window.webkitURL.createObjectURL(file);
    }
    return url;
}
// 点击推荐的标签设置
function AddTagOfRecommend(obj){
    let tags = document.getElementById("tag").children[2];
    if(tags.childElementCount>=10){
        alert("最多只能添加十个标签哦");
    }else{
        let content = obj.children[0].innerText;
        let flag = true;
        for (let i=0; i<tags.childElementCount; i++) {
            if(content === tags.children[i].children[0].value){
                tags.children[i].parentNode.removeChild(tags.children[i])
                flag = false;
                break;
            }
        }
        if(flag){
            let str =
                "<label onclick=\"this.parentNode.removeChild(this)\">\n" +
                "    <input type=\"checkbox\" name=\"tags\" value=\"" + content + "\" checked>\n" +
                "    <span>&nbsp;&nbsp;#" + content + "</span>\n" +
                "    <img class=\"delete-tag-icon\" src=\"imgs/icon/publish/close.svg\" onload=\"SVGInject(this)\" alt=\"\">\n" +
                "</label>";
            tags.insertAdjacentHTML("beforeend", str);
        }

    }
}