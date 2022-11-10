// 在视频中禁止右键菜单
document.getElementById("video_player").oncontextmenu = function(){
    return false;
}

let videoSpeed = document.getElementById("video_speed");
let videoSpeeds = document.getElementById("video_speeds");
// 更改视频倍数
function VideoSpeedChange(speed) {
    video.playbackRate = speed;
    videoSpeed.innerHTML = speed + "x";
    videoSpeeds.style.display = "none";
}

let timeOutB;
let barrageSetFlag = false;
function InitVideoController() {
    let video = document.querySelector("video");
    let playBtn = document.getElementById("play_btn");
    let thisVideoTime = document.getElementById("this_video_time");
    let progressBar = document.getElementById("progress_var");
    let barrageSet = document.getElementById("barrage_set");
    let barrageSetting = document.getElementById("barrage_setting");
    let barrageInput = document.getElementById("barrage_input");
    // let videoSpeed = document.getElementById("video_speed");
    // let videoSpeeds = document.getElementById("video_speeds");
    let voiceBtn = document.getElementById("voice_btn");
    let voiceProgress = document.getElementById("voice_progress");
    let voiceBtnElse = document.getElementById("voice_btn_else");
    let picInPic = document.getElementById("pic_in_pic");
    let fullscreenBtn = document.getElementById("fullscreen_btn");
    let ratio = 1;


// 倍数播放抽屉, 不用hover，因为需要点击倍数后用js隐藏倍数抽屉，导致之后的hover失效，而hover设置为!important会使js失效
    videoSpeeds.addEventListener("mouseover", function () {
        videoSpeeds.style.display = "block";
    })
    videoSpeed.addEventListener("mouseover", function () {
        videoSpeeds.style.display = "block";
    })

    videoSpeeds.addEventListener("mouseout", function () {
        videoSpeeds.style.display = "none";
    })
    videoSpeed.addEventListener("mouseout", function () {
        videoSpeeds.style.display = "none";
    })


    let voiceMousePosFlag = false;
    voiceBtn.addEventListener("mouseover", function () {
        voiceMousePosFlag = true;
        voiceProgress.style.display = "block";
    })
    voiceProgress.addEventListener("mouseover", function () {
        voiceMousePosFlag = true;
        voiceProgress.style.display = "block";
    })

    voiceBtn.addEventListener("mouseout", function () {
        voiceMousePosFlag = false;
        voiceProgress.style.display = "none";
    })
    voiceProgress.addEventListener("mouseout", function () {
        voiceMousePosFlag = false;
        voiceProgress.style.display = "none";
    })




    barrageSet.addEventListener("mouseover", function () {
        barrageSetting.style.display = "block";
        clearTimeout(timeOutB);
        barrageSetFlag = true;
    })
    barrageSetting.addEventListener("mouseover", function () {
        barrageSetting.style.display = "block";
        clearTimeout(timeOutB);
        barrageSetFlag = true;
    })

    barrageSet.addEventListener("mouseleave", function () {
        timeOutB = setTimeout(function () {
            barrageSetting.style.display = "none";
        }, 200);
        barrageSetFlag = false;
    })
    barrageSetting.addEventListener("mouseleave", function () {
        timeOutB = setTimeout(function () {
            barrageSetting.style.display = "none";
        }, 200);
        barrageSetFlag = false;
    })

// 定时器
    let clickStore;
// 计数器，记录点击的次数，1：创建定时器，300毫秒后将计数器归零并执行单击操作。2：定时器还没有触发，删除定时器、将计数器归零并执行双击操作
    let clickCount = 0;
// 视频播放
    playBtn.parentElement.addEventListener("click", playCon);
    video.addEventListener("click", function () {
        clickCount++;
        // 单击播放暂停
        if (clickCount === 1) {
            clickStore = setTimeout(function () {
                clickCount = 0;
                playCon();
            }, 300);
        }
        // 双击全屏
        else if (clickCount === 2) {
            // 删除上一次单击事件
            clearTimeout(clickStore);
            fullScreenEln();
            clickCount = 0;
        }
    })

    function playCon() {
        if (video.paused) {
            video.play();
            playBtn.src = "./imgs/video_controller/pause.svg";
        } else {
            video.pause();
            playBtn.src = "./imgs/video_controller/play.svg";
        }
    }

// 定时器，更新视频当前时间
    let videoTimer = new Timer();
    videoTimer.repeat(function () {
        // 获取视频总时长 现在播放的时间
        let total = video.duration;
        let nowTime = video.currentTime;
        let totalStr = getTimeHMS(total);
        let nowTimeStr = getTimeHMS(nowTime);
        // 将时间设置到页面
        thisVideoTime.innerHTML = nowTimeStr + "/" + totalStr;

        // 计算进度条比例
        let progress = nowTime / total * progressBar.clientWidth;
        progressBar.children[0].style.width = progress + "px";
        progressBar.children[1].style.left = progress + "px";
    }, 5);


// 点击进度条 跳转
    progressBar.addEventListener("mousedown", function (e) {
        // 进度条总宽度
        let videoProWidth = progressBar.offsetWidth;
        // 视频总长度
        let total = video.duration;
        // 视频当前时间
        let nowTime = video.currentTime;
        // 获取播放状态
        let status = video.paused;
        // 获取当前鼠标坐标
        let X = e.pageX;
        // 更改进度条长度
        progressBar.children[0].style.width = e.offsetX + 'px';
        // 更改视频当前时间， 当前点击位置 / 进度条的总宽度 * 总时长
        video.currentTime = e.offsetX / progressBar.clientWidth * total;
        // 计算出来的时间 渲染到页面
        thisVideoTime.innerHTML = getTimeHMS(nowTime) + "/" + getTimeHMS(total);
        // 当前进度条长度
        let proBar = progressBar.children[0].offsetWidth;

        document.onmousemove = function (e) {
            // 计算移动距离
            let diff = e.pageX - X;
            // 计算进度条移动后的长度
            let proBarNew = proBar + diff;
            if (proBarNew > videoProWidth) {
                proBarNew = videoProWidth;
            } else if (proBarNew < 0) {
                proBarNew = 0;
            }

            progressBar.children[0].style.width = proBarNew + "px";

            video.currentTime = proBarNew / videoProWidth * total;

            thisVideoTime.innerHTML = getTimeHMS(video.currentTime) + "/" + getTimeHMS(total);

            // 播放状态保持不变
            if (status) {
                video.pause();
                playBtn.src = "./imgs/video_controller/play.svg";
            } else {
                video.play();
                playBtn.src = "./imgs/video_controller/pause.svg";
            }
        }
    })

    const voiceTotal = document.getElementById("voice_total");
    const ptxt = document.getElementById("voice_count");
    const voicePro = document.getElementById("voice_pro");
// 鼠标按下音量条
    voiceTotal.addEventListener("mousedown", function (event) {

        // 获取按下时鼠标初始位置
        let y = event.pageY;
        // 按下时重新设置进度条
        voicePro.style.height = (140 - event.offsetY) + "px";
        // 获取进度条的初始height
        let voiceProLen = voicePro.offsetHeight
        // 计算当前进度条比例
        ratio = voiceProLen / 140;

        ptxt.innerHTML = "" + parseInt(ratio * 100);
        // 更改音量
        video.volume = ratio;
        // 更改图标，作用于静音时
        voiceBtn.children[0].src = "./imgs/video_controller/sound-on.svg";


        // 拖动需要写到down里面
        document.onmousemove = function (event) {
            // 拖动时保持状态栏出现
            voiceProgress.style.display = "block";
            controllerMenu.style.top = "calc(100% - 49px)";
            // 获取移动的距离
            let diff = y - event.pageY;
            // 计算当前进度条的height
            let voiceProLenNew = voiceProLen + diff;
            // 当超出进度条范围，控制
            if (voiceProLenNew < 0) {
                voiceProLenNew = 0;
            } else if (voiceProLenNew > 140) {
                voiceProLenNew = 140;
            }

            // 更改进度条height
            voicePro.style.height = voiceProLenNew + "px";

            // 计算当前进度条比例
            ratio = voiceProLenNew / 140;

            ptxt.innerHTML = parseInt(ratio * 100) + "";
            // 更改音量
            video.volume = ratio;
        }
        //当鼠标弹起的时候
        document.onmouseup = function () {
            // 取消移动事件
            document.onmousemove = null;
            // 若鼠标在音量进度条中
            if (voiceMousePosFlag) {
                return;
            }
            // 隐藏音量进度条
            voiceProgress.style.display = "none";
        }
    });

    document.onmouseup = function () {
        document.onmousemove = null;
    }

    let voiceBtnFlag = false;
// 音量开关
    voiceBtnElse.addEventListener("click", function () {
        if (voiceBtnFlag === false) {
            video.volume = 0;
            voiceBtn.children[0].src = "./imgs/video_controller/sound-off.svg";
            voiceBtnFlag = true;
        } else {
            video.volume = ratio;
            voiceBtn.children[0].src = "./imgs/video_controller/sound-on.svg";
            voiceBtnFlag = false;
        }

    })


// 画中画开关
    picInPic.parentElement.addEventListener("click", function () {
        video.requestPictureInPicture();
    })

// 全屏开关
    fullscreenBtn.parentElement.addEventListener("click", fullScreenEln);

    function fullScreenEln() {
        if (document.fullscreenElement) {
            document.exitFullscreen();
            fullscreenBtn.src = "./imgs/video_controller/fullscreen-enter.svg";
        } else {
            document.getElementById("video_player").requestFullscreen();
            fullscreenBtn.src = "./imgs/video_controller/fullscreen-exit.svg";
        }
    }


// 隐藏鼠标
    let mouseTimer; // 获取定时器对象
    let hidding = false;
    let inputFocus = false; // 当输入弹幕时，不隐藏鼠标和控制栏
    let mouseOnController = false; // 当鼠标悬停在控制栏上，不隐藏鼠标和控制栏
    let controllerMenu = document.getElementById("controller_menu")
    let videoPlayer = document.getElementById("video_player")
    videoPlayer.addEventListener("mousemove", function () {
        // 删除定时器
        clearTimeout(mouseTimer);
        // 若鼠标在控制栏上或输入弹幕，返回
        if (mouseOnController || inputFocus) {
            controllerMenu.style.top = "calc(100% - 49px)";
            return;
        }
        if (hidding) {
            hidding = false;
            return;
        }
        videoPlayer.style.cursor = "";
        controllerMenu.style.top = "calc(100% - 49px)";
        // 定时器，两秒后隐藏鼠标和状态栏
        mouseTimer = setTimeout(function () {
            hidding = true;
            videoPlayer.style.cursor = "none";
            controllerMenu.style.top = "100%";
        }, 2000);
    })
    videoPlayer.addEventListener("mouseleave", function () {
        if (inputFocus) {
            return;
        }
        mouseTimer = setTimeout(function () {
            controllerMenu.style.top = "100%";
        }, 2000);
    })
    controllerMenu.addEventListener("mouseover", function () {
        mouseOnController = true;
    })
    controllerMenu.addEventListener("mouseleave", function () {
        mouseOnController = false;
    })
    barrageInput.addEventListener("focus", function () {
        inputFocus = true;
    })
    barrageInput.addEventListener("blur", function () {
        inputFocus = false;
    })

}