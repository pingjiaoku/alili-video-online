// 获取当前项目地址
function getProjectPath() {
    //获取当前网址，如： http://localhost:8080/myproj/view/my.jsp
    let curWwwPath = window.document.location.href;

    //获取主机地址之后的目录，如： myproj/view/my.jsp
    let pathName = window.document.location.pathname;
    let pos = curWwwPath.indexOf(pathName);

    //获取主机地址，如： http://localhost:8080
    let localhostPaht = curWwwPath.substring(0, pos);

    //获取带"/"的项目名，如：/myproj
    let projectName = pathName.substring(0, pathName.substr(1).indexOf('/') + 1);

    //得到了 http://localhost:8080/myproj
    return localhostPaht + projectName;
}

// 获取cookie
function getCookie(name) {
    let cookies = document.cookie;
    let list = cookies.split("; ");     // 解析出名/值对列表

    for(let i = 0; i < list.length; i++) {
        let arr = list[i].split("=");   // 解析出名和值
        if(arr[0] === name)
            return decodeURIComponent(arr[1]);   // 对cookie值解码
    }
    return null;
}
// 设置cookie：survivalTime单位是天数
function setCookie(cookieName, cookieValue, survivalTime) {
    let d = new Date();
    d.setTime(d.getTime() + (survivalTime*24*60*60*1000));
    let expires = "expires="+d.toUTCString();
    document.cookie = cookieName + "=" + cookieValue + "; " + expires;
}

// 删除cookie
function clearCookie(name) {
    setCookie(name, "", -1);
}

// 获取url上的参数
function getQueryString(name) {
    let reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
    let r = window.location.search.substr(1).match(reg);
    if (r != null) {
        return decodeURIComponent(r[2]);
    }
    return null;
}

// 将秒数转为时分秒
function getTimeHMS(time) {
    let str = "";
    // 转换为式分秒
    let h = parseInt(time / 60 / 60 % 24)
    if(h != 0) {
        str = h < 10 ? '0' + h : h;
        str += ":";
    }
    let m = parseInt(time / 60 % 60);
    str += m < 10 ? '0' + m : m;
    str += ":";
    let s = parseInt(time % 60);
    str += s < 10 ? '0' + s : s;
    // 作为返回值返回
    return str;
}

// 获取当前时间
function GetCurrentTime() {
    let date = new Date();
    let Y = date.getFullYear() + '-';
    let M = date.getMonth() + 1;
    if(M<10){
        M = "0" + M;
    }
    M = M + '-';
    let D = date.getDate();
    if(D<10){
        D = "0" + D;
    }
    D = D + ' ';
    let h = date.getHours();
    if(h<10){
        h = "0" + h;
    }
    h = h + ':';
    let m = date.getMinutes();
    if(m<10){
        m = "0" + m;
    }
    m = m + ':';
    let s = date.getSeconds();
    if(s<10) {
        s = "0" + s;
    }
    return Y+M+D+h+m+s;
}

// 用定时器实现计时器
function Timer() {
    this.timeID = null
    this.func = null
}
Timer.prototype.repeat = function(func, ms) {
    if (this.func === null) {
        this.func = func
    }
    // 确保一个 Timer 实例只能重复一个 func
    if (this.func !== func) {
        return
    }
    this.timeID = setTimeout(() => {
        func()
        this.repeat(func, ms)
    }, ms)
}
Timer.prototype.clear = function() {
    clearTimeout(this.timeID)
}
/* Timer范例
timer = new Timer()
timer.repeat(function () {
}, 5)
timer.clear();
*/

function ToIndexPage(){
    location.href="index.html";
}
// 以新页面打开视频界面
function ToVideoPage(id) {
    window.open("video.html?VID="+id);
}
// 以新页面打开个人主页
function ToHomePage(id) {
    window.open("home.html?userId="+id);
}
function ToPublishPage() {
    if (getCookie("alili_acc") != null && getCookie("alili_pwd") != null){
        window.open("publish.html");
    } else {
        UserLoginTips();
    }
}
function UserLoginTips(){
    alert("请登录")
}


// 绘制顶部导航栏
function DrawTopNav(){
    let str =
        "<div style=\"left: 30%\" id=\"search-input\">\n" +
        "    <input type=\"text\" value=\"\" placeholder=\"你想要的这儿都没有\">\n" +
        "    <img class='cursor-pointer' src=\"imgs/icon/search.svg\" alt=\"\">\n" +
        "</div>\n" +
        "<div id=\"top-right-info\">\n" +
        "    <div id=\"headshot-box\">\n" +
        "        <img class='cursor-pointer' onclick=\"ShowLogin()\" id=\"headshot\" src=\"web-resources/headshot/default.svg\" alt=\"\">\n" +
        "        <div id=\"userinfo-add-area\"></div>\n" +
        "    </div>\n" +
        "    <ul id=\"top-right\">\n" +
        "        <li class='cursor-pointer' id=\"message-icon\">\n" +
        "            <img class=\"icon\" src=\"imgs/icon/xiaoxi.svg\" alt=\"\">\n" +
        "            <p>消息</p>\n" +
        "        </li>\n" +
        "        <li class='cursor-pointer'>\n" +
        "            <img class=\"icon\" src=\"imgs/icon/shoucang-off.svg\" alt=\"\">\n" +
        "            <p>收藏</p>\n" +
        "        </li>\n" +
        "        <li class='cursor-pointer'>\n" +
        "            <img class=\"icon\" src=\"imgs/icon/lishi-off.svg\" alt=\"\">\n" +
        "            <p>历史</p>\n" +
        "        </li>\n" +
        "    </ul>\n" +
        "    <div onclick='ToPublishPage()' class='cursor-pointer' id=\"publish\">\n" +
        "        <img src=\"imgs/icon/tougao.svg\" alt=\"\">\n" +
        "        <p>投稿</p>\n" +
        "    </div>\n" +
        "</div>";
    document.getElementById("top-nav").insertAdjacentHTML('beforeend', str);

    AddSearchListener();
}


function AddSearchListener() {
    const searchInput = document.getElementById("search-input");
    searchInput.children[0].addEventListener("focus", function () {
        searchInput.style.border = "2px solid #616161";
        searchInput.style.backgroundColor = "#FFFFFF";
    });
    searchInput.children[0].addEventListener("blur", function () {
        searchInput.style.border = "1px solid #cfcfcf";
        searchInput.style.backgroundColor = "#f9f9f9";
    });
}

// 绘制登录和注册页面
function DrawLoginPage(){
    document.getElementById("layer-page").innerHTML =
        "<!-- 注册界面 -->\n" +
        "<form class=\"form\" id=\"form-reg\" enctype=\"multipart/form-data\">\n" +
        "    <div class=\"log-reg-tips\">\n" +
        "        <h4>已经是我们自己人了吗?</h4>\n" +
        "        <p>那好兄弟，你直接点击登录按钮，进入到我们这优秀的系统里!</p>\n" +
        "        <input type=\"button\" value=\"登录\" onclick=\"ShowLogin();\">\n" +
        "    </div>\n" +
        "    <div class=\"acc-pwd-info\">\n" +
        "        <span class=\"box-title\">注册</span>\n" +
        "        <div>\n" +
        "            <p>头像：</p>\n" +
        "            <div id=\"headshot-img\">\n" +
        "                <input id=\"headshot-reg\" type=\"file\" value=\"\" name=\"headshot\" accept=\"image/png,image/jpg,image/jpeg\">\n" +
        "            </div>\n" +
        "            <span class=\"tips\">仅支持png、jpg、jpeg文件</span>\n" +
        "            <ul>\n" +
        "                <li>\n" +
        "                    <p>昵称：</p>\n" +
        "                    <input type=\"text\" name=\"uname\" id=\"uname-reg\" value=\"\" maxlength=\"10\">\n" +
        "                    <span class=\"tips\">昵称不能有空格</span>\n" +
        "                </li>\n" +
        "                <li>\n" +
        "                    <p>账号：</p>\n" +
        "                    <input type=\"text\" name=\"account\" id=\"account-reg\" value=\"\" maxlength=\"20\">\n" +
        "                    <span class=\"tips\">仅由大写、小写字母、数字和下划线_组成，至少6位</span>\n" +
        "                </li>\n" +
        "                <li>\n" +
        "                    <p>密码：</p>\n" +
        "                    <input type=\"password\" name=\"password\" id=\"password-reg\" value=\"\" maxlength=\"20\">\n" +
        "                    <span class=\"tips\">仅由大写、小写字母、数字和下划线_组成，至少6位</span>\n" +
        "                </li>\n" +
        "                <li id=\"gender\">\n" +
        "                    <p>性别：</p>\n" +
        "                    <span>\n" +
        "                        <span>\n" +
        "                            <input type=\"radio\" name=\"gender\" value=\"1\">\n" +
        "                            <span>男</span>\n" +
        "                        </span>\n" +
        "                        <span>\n" +
        "                            <input type=\"radio\" name=\"gender\" value=\"2\">\n" +
        "                            <span>女</span>\n" +
        "                        </span>\n" +
        "                        <span>\n" +
        "                            <input type=\"radio\" name=\"gender\" value=\"0\" checked>\n" +
        "                            <span>保密</span>\n" +
        "                        </span>\n" +
        "                    </span>\n" +
        "                </li>\n" +
        "            </ul>\n" +
        "        </div>\n" +
        "        <input onclick=\"Register()\" type=\"button\" value=\"注册\">\n" +
        "    </div>\n" +
        "</form>\n" +
        "\n" +
        "\n" +
        "<!-- 登录界面 -->\n" +
        "<form class=\"form\" id=\"form-login\">\n" +
        "    <div class=\"log-reg-tips\">\n" +
        "        <h4>新用户?</h4>\n" +
        "        <p>好兄弟你来了，我们网站就差你加入了，快快点击下方注册按钮加入我们吧！</p>\n" +
        "        <input type=\"button\" value=\"注册\" onclick=\"ShowRegister()\">\n" +
        "    </div>\n" +
        "    <div class=\"acc-pwd-info\">\n" +
        "        <span class=\"box-title\">登录</span>\n" +
        "        <div>\n" +
        "            <ul>\n" +
        "                <li>\n" +
        "                    <p>账号：</p>\n" +
        "                    <input id=\"account-login\" type=\"text\" value=\"\" maxlength=\"20\">\n" +
        "                </li>\n" +
        "                <li>\n" +
        "                    <p>密码：</p>\n" +
        "                    <input id=\"password-login\" type=\"password\" value=\"\" maxlength=\"20\">\n" +
        "                </li>\n" +
        "            </ul>\n" +
        "        </div>\n" +
        "        <input onclick=\"Login()\" type=\"button\" value=\"登录\">\n" +
        "    </div>\n" +
        "</form>\n" ;


    AddListenerRegister();
}



// 设置首页头像and空间小box;
function SetPersonalBox(data){
    document.getElementById("headshot").src = "./web-resources/headshot/" + data["headshot"];
    document.getElementById("userinfo-add-area").innerHTML =
        "<div id=\"userinfo-box\" class='cursor-pointer'>\n" +
        "    <div>\n" +
        "        <h4 title='" + data['id'] + "' id='userinfo-uname'>" + data['uname'] + "</h4>\n" +
        "        <p>阿哩哩已陪伴你" + data['dateCount'] + "天</p>\n" +
        "    </div>\n" +
        "    <hr style=\"top:40px\">\n" +
        "    <div style=\"top:50px;\">\n" +
        "        <ul id=\"count-info\">\n" +
        "            <li style=\"left: 30px;\">\n" +
        "                <div>" + data['concern'] + "</div>\n" +
        "                <p>关注</p>\n" +
        "            </li>\n" +
        "            <li style=\"left: calc(50% - 35px);\">\n" +
        "                <div>" + data['fans'] + "</div>\n" +
        "                <p>粉丝</p>\n" +
        "            </li>\n" +
        "            <li style=\"right: 30px;\">\n" +
        "                <div>" + data['videoNum'] + "</div>\n" +
        "                <p>视频</p>\n" +
        "            </li>\n" +
        "        </ul>\n" +
        "    </div>\n" +
        "    <div style=\"top:120px;\">\n" +
        "        <ul id=\"personal-other-info\">\n" +
        "            <li onclick='ToHomePage(\"" + data["id"] + "\")'>\n" +
        "                <img src=\"imgs/icon/personal.svg\" alt=\"\">\n" +
        "                <p>个人中心</p>\n" +
        "            </li>\n" +
        "            <li>\n" +
        "                <img src=\"imgs/icon/publish-manage.svg\" alt=\"\">\n" +
        "                <p>投稿管理</p>\n" +
        "            </li>\n" +
        "            <li onclick='Logout();' style=\"top:40px;\">\n" +
        "                <img src=\"imgs/icon/logout.svg\" alt=\"\">\n" +
        "                <p>退出登录</p>\n" +
        "            </li>\n" +
        "        </ul>\n" +
        "    </div>\n" +
        "    <hr style=\"top:90px\">" +
        "</div>";
}

function AddUserFunListener() {
    const layer = document.getElementById("layer-page");
    const massageBox = document.getElementById("message-box");
    // 消息图标
    document.getElementById("message-icon").addEventListener("click", function () {
        layer.style.display = "block";
        massageBox.style.display = "block";
    })
    // 图层
    layer.addEventListener("click", function() {
        layer.style.display = "none";
        massageBox.style.display = "none";
    })


    // 消息box
    massageBox.addEventListener("click", function (e) {
        e.stopPropagation();
    })

}

function ShowLogin(){
    const layer = document.getElementById("layer-page");
    const loginBox = document.getElementById("form-login");
    const registerBox = document.getElementById("form-reg");
    layer.style.display = "block";
    loginBox.style.display = "block";
    registerBox.style.display = "none";
}

function ShowRegister(){
    const layer = document.getElementById("layer-page");
    const loginBox = document.getElementById("form-login");
    const registerBox = document.getElementById("form-reg");
    layer.style.display = "block";
    loginBox.style.display = "none";
    registerBox.style.display = "block";
}

function SetMessageBox(){
    let str =
        "        <!-- 消息界面 -->\n" +
        "        <div id=\"message-box\"></div>";
    document.getElementById("layer-page").insertAdjacentHTML('beforeend', str);
}

function FollowAuthorChannel(authorId){
    let acc = getCookie("alili_acc");
    let pwd = getCookie("alili_pwd");
    if(acc == null || pwd == null){
        UserLoginTips();
    } else {
        $.ajax({
            type: "post",
            url: "FollowAuthorChannel",
            data: {
                acc: acc,
                pwd: pwd,
                authorId: authorId
            },
            success: function (data){
                if(data === "Success"){
                    if(document.getElementById("is-concern").value === "已关注"){
                        document.getElementById("is-concern").style.backgroundColor = "#00AEEC";
                        document.getElementById("is-concern").value = "关注";
                        document.getElementById("author-fans").innerText = parseInt(document.getElementById("author-fans").innerText) - 1;
                    } else {
                        document.getElementById("is-concern").style.backgroundColor = "#a0a0a0";
                        document.getElementById("is-concern").value = "已关注";
                        document.getElementById("author-fans").innerText = parseInt(document.getElementById("author-fans").innerText) + 1;
                    }
                } else {
                    alert(data);
                }
            },
            error: function (){
                alert("服务器连接失败");
            }
        })
    }
}

