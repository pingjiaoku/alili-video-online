


function Login(){
    let acc = document.getElementById("account-login");
    let pwd = document.getElementById("password-login");
    if(LoginCheck(acc, pwd)){
        $.ajax({
            type:"post",
            url:"Login",
            data:{
                'account':acc.value,
                'password':pwd.value
            },
            success:function (data){
                if(data === "true"){
                    location.reload()
                } else {
                    alert(data)
                }
            },
            error:function (data){
                alert("服务器连接失败")
            }
        })
    }
}

function LoginCheck(acc, pwd){
    if(/^[A-Za-z1-9_]{6,20}$/.test(acc.value)){
    }else{
        alert("账号格式有误");
        return false;
    }
    if(/^[A-Za-z1-9_]{6,20}$/.test(pwd.value)){
    }else{
        alert("密码格式有误");
        return false;
    }
    return true;
}

function Register(){
    if(RegisterCheck()){
        $.ajax({
            type:"post",
            url:"Register",
            data:new FormData(document.getElementById("form-reg")),
            //是否缓存
            cache: false,
            //当设置为false的时候,jquery 的ajax 提交的时候会序列化 data
            processData: false,
            /* contentType默认值：application/x-www-form-urlencoded；
             * 表单中设置的contentType为"multipart/form-data"；
             * ajax 中 contentType 设置为 false ，是为了避免 JQuery对要提交的表单中的enctype值修改*/
            contentType: false,
            success:function (data) {
                /*
                 * RequestDataException：请求数据异常，账号密码不合规
                 * AccountExist：账号已存在
                 * DatabaseConnectionException：数据库连接异常
                 * True：注册成功
                 *
                 */
                if(data === "True"){
                    location.reload()
                } else {
                    alert(data)
                }
            },
            error:function (){
                alert("服务器连接失败");
            }
        })
    }
}
function RegisterCheck(){
    return CheckUname()&&CheckAcc()&&CheckPwd();
}

function AddListenerRegister() {
    document.getElementById("uname-reg").addEventListener("blur", CheckUname);
    document.getElementById("account-reg").addEventListener("blur", CheckAcc);
    document.getElementById("password-reg").addEventListener("blur", CheckPwd);

    // 注册设置头像时回显
    document.getElementById("headshot-reg").addEventListener("click", function (){
        this.addEventListener("change", function (){
            let file = this.files[0];
            let fr = new FileReader();
            // 当图片加载好后设为背景
            fr.addEventListener("load", function (){
                document.getElementById("headshot-img").style.backgroundImage = "url("+this.result+")";
            })
            fr.readAsDataURL(file);
        })
    })

    const layer = document.getElementById("layer-page");
    const loginBox = document.getElementById("form-login");
    const registerBox = document.getElementById("form-reg");
    // 图层
    layer.addEventListener("click", function() {
        layer.style.display = "none";
        loginBox.style.display = "none";
        registerBox.style.display = "none";
    })
    // 登录
    loginBox.addEventListener("click", function (e) {
        // 点击子元素不触发父元素的事件
        e.stopPropagation();
    })
    // 注册
    registerBox.addEventListener("click", function (e) {
        e.stopPropagation();
    })
}


function CheckUname(){
    let uname = document.getElementById("uname-reg");
    let tip = uname.nextElementSibling;
    if(!/(^\s+)|(\s+$)|\s+/g.test(uname.value)){
        tip.style.display = "none";
        return true;
    }else{
        tip.style.display = "block";
        tip.style.color = "red";
        return false;
    }
}
function CheckAcc(){
    let account = document.getElementById("account-reg");
    let tip = account.nextElementSibling;
    if(/^[A-Za-z1-9_]{6,20}$/.test(account.value)){
        tip.style.display = "none";
        return true;
    }else{
        tip.style.display = "block";
        tip.style.color = "red";
        return false;
    }
}
function CheckPwd(){
    let password = document.getElementById("password-reg");
    let tip = password.nextElementSibling;
    if(/^[A-Za-z1-9_]{6,20}$/.test(password.value)){
        tip.style.display = "none";
        return true;
    }else{
        tip.style.display = "block";
        tip.style.color = "red";
        return false;
    }
}

function Logout(){
    if(confirm("确定登出吗？下次登录需要重新输入密码")){
        $.ajax({
            type:'post',
            url:'Logout',
            success:function (data){
                LogoutLocal();
            },
            error:function (data){
                LogoutLocal();
            }
        })
    }
}
function LogoutLocal(){
    clearCookie("alili_acc");
    clearCookie("alili_pwd");
    location.reload();
}