/*
	已修改的bug和添加的细节
	顶部弹幕和移动弹幕使用同一个弹道数组，导致顶部弹幕分布不规则 ——— 使用不同的弹道数组
	暂停视频后再拖动进度条，弹幕还是会移动 ——— 拖动进度条时没有清空画布，反而是初始化画布，导致弹幕移动的代码还能执行
	颜色选择器无作用 ——— 自己加
	弹幕道太少，数据量过多导致重叠 ——— 增加弹道
	拖动进度条时，自己发送到弹幕没出现 ——— 发送弹幕时，没有将新弹幕添加到备份数组中
	弹幕过于整齐/一秒内的弹幕同时出现 ——— 根据高度和宽度来影响速度，提前绘制弹幕(在不可视区域设置随机初始坐标)
	弹幕开关，弹幕筛选，弹幕大小、速度和透明度
*/
function StartBarrages(testArray) {
    const video = document.querySelector("video");
    const cav = document.querySelector("canvas");
    const inputEle = document.getElementById("barrage_input");
    const sendEle = document.getElementById("barrage_send_btn");
    const colorUl = document.getElementById("barrage_color");
    const sendBrgType = document.getElementById("send_brg_type");
    const brgSwitch = document.getElementById("brg_switch");
    const brgTopSwitch = document.getElementById("brg_top_switch");
    const brgMoveSwitch = document.getElementById("brg_move_switch");
    //设置常量canvas的高度以及宽度
    const cavWidth = 1560;
    const cavHeight = 960;
    cav.width = cavWidth;
    cav.height = cavHeight;
    const ctx = cav.getContext("2d");
    // 存储出现在画布上弹幕对象的数组
    let capObjs = [];

    // 弹幕在画布中高度可能值组成的数组，M为移动弹幕的，N为顶部弹幕的
    let topObjsM = [];
    let topObjsN = [];
    for (let i = 0; i < 30; i++) {
        let tObjM = {blank:true, value: 30+i*30, index:0};
        let tObjN = {blank:true, value: 30+i*30, index:0};
        topObjsM.push(tObjM);
        topObjsN.push(tObjN);
    }


    // 测试数据
    /*let testArray = [{content:"ABCDEFGHIJKLMNOPQRSTUVWXYZ",time:1,isMove:false,colorIndex:"#fff"},
        {content:"233333333333333",time:2,isMove:true,colorIndex:"#fff"},
        {content:"干杯，哈哈哈~~~~~~",time:2,isMove:true,colorIndex:"rgba(255,0,255,0.5)"},
        {content:"干杯，哈哈哈~~~~~~",time:2,isMove:true,colorIndex:"rgba(255,0,255,0.5)"},
        {content:"干杯，哈哈哈~~~~~~",time:2,isMove:true,colorIndex:"rgba(255,0,255,0.5)"},
        {content:"干杯，哈哈哈~~~~~~",time:2,isMove:true,colorIndex:"rgba(255,0,255,0.5)"},
        {content:"干杯，哈哈哈~~~~~~",time:2,isMove:true,colorIndex:"rgba(255,0,255,0.5)"},
        {content:"干杯，哈哈哈~~~~~~",time:2,isMove:true,colorIndex:"rgba(255,0,255,0.5)"},
        {content:"干杯，哈哈哈~~~~~~",time:2,isMove:true,colorIndex:"rgba(255,0,255,0.5)"},
        {content:"干杯，哈哈哈~~~~~~",time:2,isMove:true,colorIndex:"rgba(255,0,255,0.5)"},
        {content:"干杯，哈哈哈~~~~~~",time:2,isMove:true,colorIndex:"rgba(255,0,255,0.5)"},
        {content:"干杯，哈哈哈~~~~~~",time:2,isMove:true,colorIndex:"rgba(255,0,255,0.5)"},
        {content:"干杯，哈哈哈~~~~~~",time:2,isMove:true,colorIndex:"rgba(255,0,255,0.5)"},
        {content:"干杯，哈哈哈~~~~~~",time:2,isMove:true,colorIndex:"rgba(255,0,255,0.5)"},
        {content:"干杯，哈哈哈~~~~~~",time:2,isMove:true,colorIndex:"rgba(255,0,255,0.5)"},
        {content:"干杯，哈哈哈~~~~~~",time:2,isMove:true,colorIndex:"rgba(255,0,255,0.5)"},
        {content:"干杯，哈哈哈~~~~~~",time:2,isMove:true,colorIndex:"rgba(255,0,255,0.5)"},
        {content:"干杯，哈哈哈~~~~~~",time:2,isMove:true,colorIndex:"rgba(255,0,255,0.5)"},
        {content:"干杯，哈哈哈~~~~~~",time:2,isMove:true,colorIndex:"rgba(255,0,255,0.5)"},
        {content:"干杯，哈哈哈~~~~~~",time:2,isMove:true,colorIndex:"rgba(255,0,255,0.5)"},
        {content:"干杯，哈哈哈~~~~~~",time:2,isMove:false,colorIndex:"#0FF"},
        {content:"干杯，哈哈哈~~~~~~",time:2,isMove:true,colorIndex:"#0FF"},
        {content:"干杯，哈哈哈~~~~~~",time:2,isMove:true,colorIndex:"#fff"},
        {content:"干杯，哈哈哈~~~~~~",time:2,isMove:false,colorIndex:"#000"},
        {content:"233333333333333",time:3,isMove:true,colorIndex:"#fff"},
        {content:"233333333333333",time:3,isMove:false,colorIndex:"#fff"},
        {content:"233333333333333",time:3,isMove:true,colorIndex:"#000"},
        {content:"233333333333333",time:3,isMove:true,colorIndex:"#fff"},
        {content:"23333333345555555555",time:3,isMove:true,colorIndex:"#fff"},
        {content:"233333333333333",time:3,isMove:true,colorIndex:"#fff"},
        {content:"233333333333333",time:4,isMove:false,colorIndex:"#fff"},
        {content:"老师说的非常好，我要好好学习了》》》发》",time:4,isMove:false,colorIndex:"#fff"},
        {content:"老铁们，小礼物走一波了，小汽车小火箭刷起来吧=========",time:4,isMove:true,colorIndex:"#fff"},
        {content:"老铁们，小礼物走一波了，小汽车小火箭刷起来吧=========",time:4,isMove:true,colorIndex:"#fff"},
        {content:"老铁们，小礼物走一波了，小汽车小火箭刷起来吧=========",time:4,isMove:true,colorIndex:"#fff"},
        {content:"老铁们，小礼物走一波了，小汽车小火箭刷起来吧=========",time:4,isMove:true,colorIndex:"#fff"},
        {content:"老铁们，小礼物走一波了，小汽车小火箭刷起来吧=========",time:4,isMove:true,colorIndex:"#fff"},
        {content:"老铁们，小礼物走一波了，小汽车小火箭刷起来吧=========",time:4,isMove:true,colorIndex:"#fff"},
        {content:"老铁们，小礼物走一波了，小汽车小火箭刷起来吧=========",time:4,isMove:true,colorIndex:"#fff"},
        {content:"老铁们，小礼物走一波了，小汽车小火箭刷起来吧=========",time:4,isMove:true,colorIndex:"#fff"},
        {content:"老铁们，小礼物走一波了，小汽车小火箭刷起来吧=========",time:4,isMove:true,colorIndex:"#fff"},
        {content:"老铁们，小礼物走一波了，小汽车小火箭刷起来吧=========",time:4,isMove:true,colorIndex:"#fff"},
        {content:"老铁们，小礼物走一波了，小汽车小火箭刷起来吧=========",time:4,isMove:true,colorIndex:"#fff"},
        {content:"老铁们，小礼物走一波了，小汽车小火箭刷起来吧=========",time:4,isMove:true,colorIndex:"#fff"},
        {content:"老铁们，小礼物走一波了，小汽车小火箭刷起来吧=========",time:4,isMove:true,colorIndex:"#fff"},
        {content:"老铁们，小礼物走一波了，小汽车小火箭刷起来吧=========",time:4,isMove:true,colorIndex:"#fff"},
        {content:"老铁们，小礼物走一波了，小汽车小火箭刷起来吧=========",time:4,isMove:true,colorIndex:"#fff"},
        {content:"老铁们，小礼物走一波了，小汽车小火箭刷起来吧=========",time:4,isMove:true,colorIndex:"#fff"},
        {content:"老铁们，小礼物走一波了，小汽车小火箭刷起来吧=========",time:4,isMove:true,colorIndex:"#fff"},
        {content:"老铁们，小礼物走一波了，小汽车小火箭刷起来吧=========",time:4,isMove:true,colorIndex:"#fff"},
        {content:"老铁们，小礼物走一波了，小汽车小火箭刷起来吧=========",time:4,isMove:true,colorIndex:"#fff"},
        {content:"老铁们，小礼物走一波了，小汽车小火箭刷起来吧=========",time:4,isMove:true,colorIndex:"#fff"},
        {content:"老铁们，小礼物走一波了，小汽车小火箭刷起来吧=========",time:4,isMove:true,colorIndex:"#fff"},
        {content:"老铁们，小礼物走一波了，小汽车小火箭刷起来吧=========",time:4,isMove:true,colorIndex:"#fff"},
        {content:"老铁们，小礼物走一波了，小汽车小火箭刷起来吧=========",time:4,isMove:true,colorIndex:"#fff"},
        {content:"老铁们，小礼物走一波了，小汽车小火箭刷起来吧=========",time:4,isMove:true,colorIndex:"#fff"},
        {content:"老铁们，小礼物走一波了，小汽车小火箭刷起来吧=========",time:4,isMove:true,colorIndex:"#fff"},
        {content:"老铁们，小礼物走一波了，小汽车小火箭刷起来吧=========",time:4,isMove:true,colorIndex:"#fff"},
        {content:"老铁们，小礼物走一波了，小汽车小火箭刷起来吧=========",time:4,isMove:true,colorIndex:"#fff"},
        {content:"老铁们，小礼物走一波了，小汽车小火箭刷起来吧=========",time:4,isMove:true,colorIndex:"#fff"},
        {content:"老铁们，小礼物走一波了，小汽车小火箭刷起来吧=========",time:4,isMove:true,colorIndex:"#fff"},
        {content:"老铁们，小礼物走一波了，小汽车小火箭刷起来吧=========",time:4,isMove:true,colorIndex:"#fff"},
        {content:"老铁们，小礼物走一波了，小汽车小火箭刷起来吧=========",time:4,isMove:true,colorIndex:"#fff"},
        {content:"老铁们，小礼物走一波了，小汽车小火箭刷起来吧=========",time:4,isMove:true,colorIndex:"#fff"},
        {content:"老铁们，小礼物走一波了，小汽车小火箭刷起来吧=========",time:4,isMove:true,colorIndex:"#fff"},
        {content:"老铁们，小礼物走一波了，小汽车小火箭刷起来吧=========",time:4,isMove:true,colorIndex:"#fff"},
        {content:"老铁们，小礼物走一波了，小汽车小火箭刷起来吧=========",time:4,isMove:true,colorIndex:"#fff"},
        {content:"老铁们，小礼物走一波了，小汽车小火箭刷起来吧=========",time:4,isMove:true,colorIndex:"#fff"},
        {content:"老铁们，小礼物走一波了，小汽车小火箭刷起来吧=========",time:4,isMove:true,colorIndex:"#fff"},
        {content:"老铁们，小礼物走一波了，小汽车小火箭刷起来吧=========",time:4,isMove:false,colorIndex:"#000"},
        {content:"老铁们，小礼物走一波了，小汽车小火箭刷起来吧=========",time:4,isMove:false,colorIndex:"#000"},
        {content:"老铁们，小礼物走一波了，小汽车小火箭刷起来吧=========",time:4,isMove:false,colorIndex:"#000"},
        {content:"老铁们，小礼物走一波了，小汽车小火箭刷起来吧=========",time:4,isMove:false,colorIndex:"#000"},
        {content:"老铁们，小礼物走一波了，小汽车小火箭刷起来吧=========",time:4,isMove:false,colorIndex:"#000"},
        {content:"老铁们，小礼物走一波了，小汽车小火箭刷起来吧=========",time:4,isMove:false,colorIndex:"#000"},
        {content:"老铁们，小礼物走一波了，小汽车小火箭刷起来吧=========",time:4,isMove:false,colorIndex:"#000"},
        {content:"老铁们，小礼物走一波了，小汽车小火箭刷起来吧=========",time:4,isMove:false,colorIndex:"#000"},
        {content:"老铁们，小礼物走一波了，小汽车小火箭刷起来吧=========",time:4,isMove:false,colorIndex:"#000"},
        {content:"老铁们，小礼物走一波了，小汽起来吧=========",time:4,isMove:false,colorIndex:"#000"},
        {content:"老铁们，小礼物走一波了，小汽起来吧=========",time:4,isMove:false,colorIndex:"#000"},
        {content:"老铁们，小礼物走一波了，小汽起来吧=========",time:4,isMove:false,colorIndex:"#000"},
        {content:"老铁们，小礼物走一波了，小汽起来吧=========",time:4,isMove:false,colorIndex:"#000"},
        {content:"老铁们，小礼物走一波了，小汽起来吧=========",time:4,isMove:false,colorIndex:"#000"},
        {content:"老铁们，小礼物走一波了，小汽起来吧=========",time:4,isMove:false,colorIndex:"#000"},
        {content:"老铁们，小礼物走一波了，小汽起来吧=========",time:4,isMove:false,colorIndex:"#000"},
        {content:"老铁们，小礼物走一波了，小汽起来吧=========",time:4,isMove:false,colorIndex:"#000"},
        {content:"老铁们，小礼物走一波了，小汽车小火箭刷起来吧=========",time:4,isMove:false,colorIndex:"#000"},
        {content:"老铁们，小礼物走一波汽车小火箭刷起来吧=========",time:4,isMove:false,colorIndex:"#000"},
        {content:"老铁们，小礼物走一波汽车小火箭刷起来吧=========",time:4,isMove:false,colorIndex:"#000"},
        {content:"老铁们，小礼物走一波汽车小火箭刷起来吧=========",time:4,isMove:false,colorIndex:"#000"},
        {content:"老铁们，小礼物走一波汽车小火箭刷起来吧=========",time:4,isMove:false,colorIndex:"#000"},
        {content:"老铁们，小礼物走一波了，小汽车小火箭刷起来吧=========",time:4,isMove:false,colorIndex:"#000"},
        {content:"老铁们，小礼物走一波了，小汽车小火箭刷起来吧=========",time:4,isMove:false,colorIndex:"#000"},
        {content:"老铁们，小一波了，小汽车小火箭刷起来吧=========",time:4,isMove:false,colorIndex:"#000"},
        {content:"老铁们，小一波了，小汽车小火箭刷起来吧=========",time:4,isMove:false,colorIndex:"#000"},
        {content:"老铁们，小一波了，小汽车小火箭刷起来吧=========",time:4,isMove:false,colorIndex:"#000"},
        {content:"老铁们，小一波了，小汽车小火箭刷起来吧=========",time:4,isMove:false,colorIndex:"#000"},
        {content:"老铁们，小一波了，小汽车小火箭刷起来吧=========",time:4,isMove:false,colorIndex:"#000"},
        {content:"老铁们，小一波了，小汽车小火箭刷起来吧=========",time:4,isMove:false,colorIndex:"#000"},
        {content:"老铁们，小一波了，小汽车小火箭刷起来吧=========",time:4,isMove:false,colorIndex:"#000"},
        {content:"老铁们，小一波了，小汽车小火箭刷起来吧=========",time:4,isMove:false,colorIndex:"#000"},
        {content:"老铁们，小礼物走一波了，小汽车小火箭刷起来吧=========",time:4,isMove:false,colorIndex:"#000"},
        {content:"老铁们，小礼物走一波了，小汽车小火箭刷起来吧=========",time:4,isMove:false,colorIndex:"#000"},
        {content:"老铁们，小礼物走一波了，小汽车小火箭刷起来吧=========",time:4,isMove:false,colorIndex:"#000"},
        {content:"老铁们，小礼物走一波了，小汽车小火箭刷起来吧=========",time:4,isMove:false,colorIndex:"#000"},
        {content:"老铁们，小礼物走一波了，小汽车小火箭刷起来吧=========",time:4,isMove:true,colorIndex:"#fff"},
        {content:"老铁们，小礼物走一波了，小汽车小火箭刷起来吧=========",time:4,isMove:false,colorIndex:"#fff"},
        {content:"老铁们，小礼物走一波了，小汽车小火箭刷起来吧=========",time:5,isMove:true,colorIndex:"#FF0"},
        {content:"老铁们，小礼物走一波了，小汽车小火箭刷起来吧=========",time:5,isMove:true,colorIndex:"#fff"},
        {content:"2333333343333333",time:5,isMove:true,colorIndex:"#0FF"},
        {content:"233333333333333",time:6,isMove:true,colorIndex:"#00F"},
        {content:"233333333333333",time:7,isMove:true,colorIndex:"#00F"},
        {content:"233333333333333",time:7,isMove:true,colorIndex:"#00F"},
        {content:"233333333333333",time:7,isMove:true,colorIndex:"#00F"},
        {content:"老师说的非常好，我要好好学习了》》》发》",time:7,isMove:false,colorIndex:"#fff"},
        {content:"老师说的非常好，我要好好学习了》》》发》",time:7,isMove:false,colorIndex:"#fff"},
        {content:"233333333333333",time:7,isMove:true,colorIndex:"#00F"},
        {content:"233333333333333",time:7,isMove:true,colorIndex:"#00F"},
        {content:"233333333333333",time:7,isMove:false,colorIndex:"#00F"},
        {content:"233333333333333",time:8,isMove:true,colorIndex:"#fff"},
        {content:"233333333333333",time:9,isMove:true,colorIndex:"#fff"},
        {content:"233333333333333",time:10,isMove:true,colorIndex:"#000"},
        {content:"老师说的非常好，我要好好学习了》》》》",time:12,isMove:true,colorIndex:"#fff"},
        {content:"老师说的非常好，我要好好学习了》》》》",time:13,isMove:true,colorIndex:"#fff"},
        {content:"老师说的非常好，我要好好学习了》》》》",time:14,isMove:true,colorIndex:"#00F"},
        {content:"老师说的非常好，我要好好学习了》》》发》",time:15,isMove:false,colorIndex:"#fff"},
        {content:"老师说的非常好，我要好好学习了》》》》",time:16,isMove:true,colorIndex:"#00F"},
        {content:"老师说的非常好，我要好好学习了》》》》",time:17,isMove:true,colorIndex:"#FF0"},
        {content:"老师说的非常好，我要好好学习了》》》》",time:18,isMove:true,colorIndex:"#00F"},
        {content:"老师说的非常好，我要好好学习了》》》发》",time:18,isMove:false,colorIndex:"#fff"},
        {content:"老师说的非常好，我要好好学习了》》》》",time:19,isMove:true,colorIndex:"#fff"},
        {content:"老师说的非常好，我要好好学习了》》》》",time:20,isMove:true,colorIndex:"#FF0"},
        {content:"老师说的非常好，我要好好学习了》》》》",time:21,isMove:true,colorIndex:"#fff"},
        {content:"老师说的非常好，我要好好学习了》》》》",time:22,isMove:true,colorIndex:"#fff"},
        {content:"老铁们，小礼物走一波了，小汽车小火箭刷起来吧=========",time:23,isMove:true,colorIndex:"#fff"},
        {content:"老铁们，小礼物走一波了，小汽车小火箭刷起来吧=========",time:24,isMove:false,colorIndex:"#fff"},
        {content:"老铁们，小礼物走一波了，小汽车小火箭刷起来吧=========",time:25,isMove:true,colorIndex:"#FF0"},
        {content:"老铁们，小礼物走一波了，小汽车小火箭刷起来吧=========",time:26,isMove:true,colorIndex:"#fff"},
        {content:"老铁们，小礼物走一波了，小汽车小火箭刷起来吧=========",time:27,isMove:true,colorIndex:"#F0F"},
        {content:"老铁们，小礼物走一波了，小汽车小火箭刷起来吧=========",time:28,isMove:false,colorIndex:"#F0F"},
        {content:"老铁们，小礼物走一波了，小汽车小火箭刷起来吧=========",time:29,isMove:true,colorIndex:"#F0F"},
        {content:"老铁们，小礼物走一波了，小汽车小火箭刷起来吧=========",time:30,isMove:true,colorIndex:"#F0F"},
        {content:"马上就下课了，瓦罗蓝大陆走起了～～～",time:31,isMove:true,colorIndex:"#F0F"},
        {content:"马上就下课了，瓦罗蓝大陆走起了～～～",time:32,isMove:true,colorIndex:"#00F"},
        {content:"马上就下课了，瓦罗蓝大陆走起了～～～",time:33,isMove:false,colorIndex:"#00F"},
        {content:"马上就下课了，瓦罗蓝大陆走起了～～～",time:33,isMove:true,colorIndex:"#F0F"},
        {content:"马上就下课了，瓦罗蓝大陆走起了～～～",time:34,isMove:false,colorIndex:"#F0F"},
        {content:"马上就下课了，瓦罗蓝大陆走起了～～～",time:35,isMove:true,colorIndex:"#F0F"},
        {content:"马上就下课了，瓦罗蓝大陆走起了～～～",time:36,isMove:true,colorIndex:"#00F"},
        {content:"马上就下课了，瓦罗蓝大陆走起了～～～",time:37,isMove:true,colorIndex:"#00F"}];*/
    // 备份
    let testArrayCopy = [];
    // 将测试数据备份
    copyArray(testArray , testArrayCopy)
    /*弹幕对象的构造函数，参数分别是：1.isMove：弹幕是否是移动的弹幕，2.col：弹幕的颜色，3.text：弹幕的文本*/
    function Barrage( isMove, col, text ) {
        this.isMove = isMove;           // 标记是否为移动弹幕
        this.speed = 0;                 // 弹幕移动速度
        this.color = col;               // 弹幕颜色
        this.content = text;            // 弹幕文本
        this.latestTime = 0;            // 控制顶部弹幕的显示时间
        this.width = text.length * 20;  // 弹幕显示宽度
        this.top = 20;                  // 弹幕出现的高度
        this.topIndex = 0;              // 弹幕出现的高度编号
        this.occupyPos = true;          // 当弹幕完全出现的时候，会将其占用的高度释放，该字段用来控制只释放一次
        this.left = 0;                  // 控制移动的，是该弹幕左侧离视频左侧的距离
        this.setLeftValue();            // 设置横坐标
        this.setTopValue();             // 设置纵坐标，高度
        this.moving();                  // 完成坐标的改变
    }
    Barrage.prototype.setTopValue = function() {
        // 遍历弹幕高度，将空白的高度赋给this弹幕，并根据高度和宽度设置速度
        let findTop = false;
        if (this.isMove){
            for(let i = 0 ,len = topObjsM.length ; i < len ; i++){
                if (topObjsM[i].blank) {
                    this.top = topObjsM[i].value;
                    this.topIndex = i;
                    topObjsM[i].blank = false

                    findTop = true;
                    break;
                }
            }
            // 弹幕没有找到空白高度
            if(findTop === false){
                let randomTop = Math.floor(Math.random() * topObjsM.length)
                this.top = topObjsM[randomTop].value;
                this.topIndex = randomTop;
                topObjsM[randomTop].blank = false;
            }
            this.speed = (1 + this.top/600 + this.width/2000);
        } else {
            for(let i = 0 ,len = topObjsN.length ; i < len ; i++){
                if (topObjsN[i].blank) {
                    this.top = topObjsN[i].value;
                    this.topIndex = i;
                    topObjsN[i].blank = false;
                    findTop = true;
                    break;
                }
            }
            if(findTop === false){
                let randomTop = Math.floor(Math.random() * topObjsN.length)
                this.top = topObjsM[randomTop].value;
                this.topIndex = randomTop;
                topObjsN[randomTop].blank = false;
            }
        }

    }
    Barrage.prototype.setLeftValue = function() {
        if (this.isMove) {
            // 提前绘制弹幕(先在不可显示的地方移动一会)
            this.left = cavWidth +  + Math.random()*300;
        }
        else {
            // 弹幕字符数
            let contentLength = this.content.length;
            // 居中显示：画布分辨率-弹幕长度的一半
            this.left = 800 - contentLength * 9;
        }
    }
    Barrage.prototype.moving = function() {
        if (this.isMove) {
            // 更新弹幕显示位置
            this.left-=this.speed*(1 + brgSpeedCount.innerText / 100);
            // 当弹幕完全出现的时候，会将其占用的高度释放，occupyPos用来控制只释放一次
            if ( this.left + this.width + 100 < cavWidth  && this.occupyPos) {
                this.occupyPos = false;
                topObjsM[this.topIndex].blank = true;
            }
        }
        else{
            // 控制显示时间
            this.latestTime += 1;
            this.left = 800 - this.content.length * (0.2*brgSizeCount.innerText + 10)/2;
            if (this.latestTime > 200) {
                topObjsN[this.topIndex].blank = true;
            }
        }
    }


    // 弹幕速度（用户控制）
    const barrageSpeedBar = document.getElementById("barrage_speed");
    const barrageSizeBar = document.getElementById("barrage_size");
    const barrageTranBar = document.getElementById("barrage_transparency");
    const brgSpeedCount = document.getElementById("barrage_speed_count");
    const brgSizeCount = document.getElementById("barrage_size_count");
    const brgTranCount = document.getElementById("barrage_tran_count");
    // 点击进度条 跳转
    barrageSpeedBar.addEventListener("mousedown" , function(e){ progress(e, this, brgSpeedCount)});
    barrageSizeBar.addEventListener("mousedown" , function(e){ progress(e, this, brgSizeCount)});
    barrageTranBar.addEventListener("mousedown" , function(e){ progress(e, this, brgTranCount)});
    function progress(e, proObj, count){
        // 进度条总宽度
        let proWidth = proObj.offsetWidth;
        // 获取当前鼠标坐标
        let X = e.pageX;
        // 更改进度条长度
        proObj.children[0].style.width = e.offsetX + 'px';
        // 进度条比例
        let ratio = e.offsetX / proWidth;

        count.innerText = parseInt(ratio * 100) + "";
        // 当前进度条长度
        let proBar = proObj.children[0].offsetWidth;

        document.onmousemove = function (e) {
            // 拖动时保持状态栏出现
            document.getElementById("barrage_setting").style.display = "block";
            clearTimeout(timeOutB);
            // 计算移动距离
            let diff = e.pageX-X;
            // 计算进度条移动后的长度
            let proBarNew = proBar + diff;
            if(proBarNew > proWidth){
                proBarNew = proWidth;
            } else if(proBarNew < 0) {
                proBarNew = 0;
            }

            proObj.children[0].style.width = proBarNew + "px";
            ratio = proBarNew / proWidth;
            count.innerText = parseInt(ratio * 100) + "";

        }
        //当鼠标弹起的时候
        document.onmouseup = function() {
            // 取消移动事件
            document.onmousemove = null;
            // 若鼠标在音量进度条中
            if(barrageSetFlag) {
                return;
            }
            // 隐藏音量进度条
            document.getElementById("barrage_setting").style.display = "none";
        }
    }

    // 弹幕开关
    let brgSwitchC = true;
    let brgTopSwitchC = true;
    let brgMoveSwitchC = true;
    brgSwitch.parentElement.addEventListener("click", function (){
        if(brgSwitchC) {
            brgSwitchC = false;
            brgSwitch.src = "./imgs/video_controller/播放器-弹幕（关）_44.svg";
            // 初始化所有配置
            reinitCav()
        } else {
            brgSwitchC = true;
            brgSwitch.src = "./imgs/video_controller/播放器-弹幕（开）_44.svg";
            // 重启弹幕功能
            initCanvas();
        }
    })
    brgTopSwitch.parentElement.addEventListener("click", function () {
        if(brgTopSwitchC) {
            brgTopSwitchC = false;
            brgTopSwitch.src = "./imgs/video_controller/播放器-弹幕顶部（关）_44.svg";
        } else {
            brgTopSwitchC = true;
            brgTopSwitch.src = "./imgs/video_controller/播放器-弹幕顶部（开）_44.svg";
        }
    })
    brgMoveSwitch.parentElement.addEventListener("click", function () {
        if(brgMoveSwitchC) {
            brgMoveSwitchC = false;
            brgMoveSwitch.src = "./imgs/video_controller/播放器-弹幕滚动（关）_44.svg";
        } else {
            brgMoveSwitchC = true;
            brgMoveSwitch.src = "./imgs/video_controller/播放器-弹幕滚动（开）_44.svg";
        }
    })


    // 循环遍历数组，根据对象的属性绘制在画布上
    function drawAllText () {
        // 清空画布
        ctx.clearRect(0, 0, cavWidth, cavHeight);
        // ctx.beginPath();
        for(let i=0 , len = capObjs.length ; i < len ; i++ ){
            // 筛选弹幕
            if((capObjs[i].isMove && brgMoveSwitchC) || (!capObjs[i].isMove && brgTopSwitchC)) {
                // 弹幕描边
                ctx.strokeStyle = "black";
                // 弹幕阴影
                ctx.shadowColor = 'rgba(255,255,255,0.2)';
                ctx.shadowOffsetX = 1;
                ctx.shadowOffsetY = 1;
                // 弹幕颜色
                ctx.fillStyle = capObjs[i].color;
                // 弹幕透明度
                ctx.globalAlpha = brgTranCount.innerText/100;
                // 弹幕样式
                ctx.font = "bold " + (0.2*brgSizeCount.innerText + 10) + "px sans-serif";
                // 绘制
                ctx.strokeText(capObjs[i].content, capObjs[i].left, capObjs[i].top);
                ctx.fillText(capObjs[i].content, capObjs[i].left, capObjs[i].top);
                ctx.closePath();
            }
            capObjs[i].moving();
        }
    }

    // 更新数组，当对象已经超出范围的时候从数组删除这个对象
    function refreshObjs(objs) {
        for (let i = objs.length - 1; i >= 0; i--) {
            if (objs[i].left + objs[i].width < 0 || objs[i].latestTime > 200 ) {
                objs.splice(i , 1)
            }
        }
    }

    // 更新保存弹幕对象的数组
    function updateArray () {
        // 视频当前时间
        let now = parseInt(video.currentTime);
        for (let i = 0; i < testArray.length; i++) {
            let nowItemTime = parseInt(testArray[i].time);
            // (得到视频当前时间的弹幕群) && ((是移动弹幕&&被允许)||(是顶部弹幕&&被允许))
            if (( nowItemTime === now ) && ((testArray[i].isMove && brgMoveSwitchC) || (!testArray[i].isMove && brgTopSwitchC))) {
                let color = testArray[i].colorIndex;
                // 创建弹幕对象
                let temcap = new Barrage(testArray[i].isMove, color, testArray[i].content);
                // 添加到展示数组中
                capObjs.push(temcap);
                // 删除弹幕数组中对应的弹幕
                testArray.splice(i,1);
            }
        }
    }



    // 初始化，重新启动canvas，用在人为导致进度条时间的改变和关闭弹幕时
    function reinitCav() {
        // 还原被删除的弹幕
        copyArray(testArrayCopy , testArray);
        // 释放所有弹幕道
        for (let i = 0; i < topObjsM.length; i++){
            topObjsM[i].blank = true;
            topObjsN[i].blank = true;
        }
        // 清空就绪弹幕数组
        capObjs = [];
        // 删除定时器
        timer.clear();
        // 清除画布
        ctx.clearRect(0, 0, cavWidth, cavHeight);
    }


    // 定义计时器
    let timer = new Timer();

    //初始化canvas，用在开始播放时
    function initCanvas () {
        timer.clear();
        timer = new Timer()
        timer.repeat(function () {
            drawAllText(); // 绘制弹幕
            updateArray(); // 更新就绪弹幕
            refreshObjs(capObjs) // 删除已消失的弹幕
        }, 10)
    }

    //复制数组
    function copyArray (arr1, arr2) {
        for (let i =0; i < arr1.length; i++) {
            arr2[i] = arr1[i];
        }
    }

    video.addEventListener("playing" , function () {
        initCanvas();
    })

    let prevPlayTime = 0;
    //进度条改变执行代码
    video.addEventListener("timeupdate", function  () {
        let nowPlayTime = video.currentTime;
        let diffTime = Math.abs(nowPlayTime - prevPlayTime);
        prevPlayTime = nowPlayTime;
        if (diffTime > 1) {
            reinitCav();
        }
    }, false)

    //视频暂停执行代码
    video.addEventListener("pause" , function () {
        timer.clear();
    })

    let isMoveInputEle = true;
    sendBrgType.addEventListener("click", function () {
        if(isMoveInputEle) {
            isMoveInputEle = false;
            sendBrgType.src = "./imgs/video_controller/播放器-弹幕顶部（开）_44.svg";
        } else {
            isMoveInputEle = true;
            sendBrgType.src = "./imgs/video_controller/播放器-弹幕滚动（开）_44.svg";
        }
    });

    // 当用户点击send发送弹幕时获取数据
    function sendBarrage () {
        let acc = getCookie("alili_acc");
        let pwd = getCookie("alili_pwd");
        if(acc == null || pwd == null){
            UserLoginTips();
        }else{
            let inputEleTxt = inputEle.value;
            let now = parseInt(video.currentTime);
            let temObj = {content:inputEleTxt,time:now,isMove:isMoveInputEle,colorIndex:colorUl.value};

            $.ajax({
                type:"post",
                url:"SendVideoBarrage",
                data:{
                    acc:acc,
                    pwd:pwd,
                    "videoId":getQueryString("VID"),
                    content:inputEleTxt,
                    time:now,
                    "isMove":isMoveInputEle,
                    "colorIndex":colorUl.value
                },
                success: function (data){
                    if(data==="Success"){
                        testArray.push(temObj);
                        testArrayCopy.push(temObj);
                        inputEle.value = "";
                    } else {
                        alert("好像出问题了");
                    }
                },
                error:function (){
                    alert("服务器连接失败");
                }
            })
        }
    }

    //点击send的监听事件
    sendEle.addEventListener("click" , sendBarrage)

    //input的回车监听事件
    inputEle.addEventListener("keydown", function(e) {
        if (e.key === "Enter") {
            sendBarrage()
        }
    })
}

