const channelBtn=document.getElementById("channel-setting");
const channel = document.getElementById("channel");
// const channelMain = document.getElementById("channel-main");
const channelBtnNone = document.getElementsByClassName("channel-icon-none");
const channelEyes = document.getElementsByClassName("eyes");

// 频道测试数据
let channelList =
    [{id:1,content:"影视",display:true,img:"dianshiju-off.svg",ordinal:1},
        {id:2,content:"动漫",display:true,img:"dongman-off.svg",ordinal:2},
        {id:3,content:"游戏",display:true,img:"youxi-off.svg",ordinal:3},
        {id:4,content:"音乐",display:true,img:"yinyue-off.svg",ordinal:4},
        {id:5,content:"美食",display:true,img:"meishi-off.svg",ordinal:5},
        {id:6,content:"搞笑",display:true,img:"gaoxiao-off.svg",ordinal:6},
        {id:7,content:"运动",display:true,img:"yundong-off.svg",ordinal:7},
        {id:8,content:"日常",display:true,img:"richang-off.svg",ordinal:8},
        {id:9,content:"科技",display:true,img:"keji-off.svg",ordinal:9},
        {id:10,content:"动物",display:true,img:"dongwu-off.svg",ordinal:10},
        {id:11,content:"鬼畜",display:true,img:"guicu-off.svg",ordinal:11},
        {id:12,content:"军事",display:true,img:"junshi-off.svg",ordinal:12},
        {id:13,content:"时尚",display:true,img:"shishang-off.svg",ordinal:13},
    ];



// 遍历频道数据，初始化频道列表
function ChannelInit(list) {
    list = list.sort(list["ordinal"])
    let str = "";
    for (let i = 0; i < list.length; i++) {
        let flag, eyeImg;
        if(list[i].display){
            flag = "block";
            eyeImg = "on";
        } else {
            flag = "none";
            eyeImg = "off";
        }
        str +=
            "<li style='display: " + flag + "' id='" + list[i].id + "' draggable=\"false\">\n" +
            "    <a>\n" +
            "        <img draggable=\"false\" src=\"imgs/icon/" + list[i].img + "\" alt=\"\">\n" +
            "        <span>" + list[i].content + "</span>\n" +
            "    </a>\n" +
            "    <div class=\"channel-icon-none\">\n" +
            "        <img draggable=\"false\" src=\"imgs/icon/sort.svg\" alt=\"\">\n" +
            "        <img class=\"eyes\" draggable=\"false\" src=\"imgs/icon/eye-" + eyeImg + ".svg\" alt=\"" + list[i].display + "\">\n" +
            "    </div>\n" +
            "</li>";
    }
    channel.innerHTML = str;
    // 由于channel是通过inner写的，删除channel后会删除对应的监听器，需要重新创建
    AddChannelEyesClickListener();
    AddChannelClickListener();
}


// 为每一个频道选项 后面的可见按钮 设置一个点击事件
function AddChannelEyesClickListener() {
    for (let i = 0; i < channelEyes.length; i++) {
        channelEyes[i].addEventListener("click", function () {
            if (this.alt === "false") {
                this.alt = "true";
                this.src = "imgs/icon/eye-on.svg";
            } else {
                this.alt = "false";
                this.src = "imgs/icon/eye-off.svg";
            }
        })
    }
}

// 频道设置（排序，可见）开关
channelBtn.addEventListener("click", function (){
    // 开始设置
    if(this.alt === "false") {
        this.alt = "true";
        this.src = "imgs/icon/right.svg";
        for (let i = 0; i < channel.children.length; i++) {
            // 将频道设置为动画元素
            channel.children[i].draggable = true;
            channel.children[i].style.backgroundColor = "#f5f5f5";
            // 使图标（排序，可见）设置为可见
            channelBtnNone[i].style.display = "block";
        }
        // 将已被隐藏的频道显示出来
        for (let i = 0; i < channelEyes.length; i++) {
            if(channelEyes[i].alt === "false"){
                channelEyes[i].parentElement.parentElement.style.display="block";
            }
        }
    }
    // 关闭设置
    else {
        this.alt = "false";
        this.src = "imgs/video_controller/settings-off.svg";
        for (let i = 0; i < channel.children.length; i++) {
            // 设置为非动画元素
            channel.children[i].draggable = false;
            channel.children[i].style.backgroundColor = "#ffffff";
            // 隐藏图标
            channelBtnNone[i].style.display = "none";
        }
        // 隐藏频道
        for (let i = 0; i < channelEyes.length; i++) {
            if(channelEyes[i].alt === "false"){
                channelEyes[i].parentElement.parentElement.style.display="none";
            }
        }
        AjaxChannel();
    }
})
// 上传修改后的频道顺序
function AjaxChannel(){
    let channels = [];
    // 获取频道排序信息
    for (let i = 0; i < channel.children.length; i++) {
        let obj = {
            sortnum: i + 1,
            channelid: channel.children[i].id,
            display: channel.children[i].children[1].children[1].alt
        };
        channels.push(obj);
    }
    console.log(channels)
    $.ajax({
        type: "post",
        url: "ChangePersonalChannels",
        data: {
            acc:getCookie("alili_acc"),
            pwd:getCookie("alili_pwd"),
            channels: JSON.stringify(channels)
        },
        success: function () {
            console.log("修改频道排序成功")
        }
    })
}

let nowChannel = document.getElementById("channel-main").children[0];
function AddChannelClickListener() {
    for (let i = 0; i < channel.children.length; i++) {
        channel.children[i].addEventListener("click", function () {
            if (channelBtn.alt === "false") {
                ToVideosPage(this.id);
                ChangeIcon(this);
            }
        })
    }
}
/*// 为所有频道设置点击事件
for (let i = 0; i < channelMain.children.length; i++) {
    channelMain.children[i].addEventListener("click", function () {
        ToVideosPage(this.id);
        ChangeIcon(this);
    })
}*/

function GetRankVideos(obj){
    ChangeIcon(obj);

}

function GetTrendVideos(obj){
    ChangeIcon(obj);

}

function GetHomeVideos(obj){
    ChangeIcon(obj);
    ToVideosPage(obj.id)
}

// 频道被点击后更改图标，并使上一个被点击的频道图标设为初始状态
function ChangeIcon(obj) {
    obj.children[0].children[0].src = obj.children[0].children[0].src.replace("-off","-on");
    if(nowChannel !== obj){
        nowChannel.children[0].children[0].src = nowChannel.children[0].children[0].src.replace("-on", "-off");
        nowChannel = obj;
    }
}

// 根据频道id刷新视频列表
function ToVideosPage(id){
    $.ajax({
        type: "post",
        url: "GetVideosByChannel",
        data: {
            id: id
        },
        success: function (data){
            if(data!=null){
                data=eval(data);
                VideoPages(data)
            }
        }
    })

}