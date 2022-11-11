


--###########################################################
--###########################################################

-- 所属用户 alilisys，若有更改记得在jdbc.properties中更改
-- create tablespace MyTablespace
-- datafile 'D:\Program\Database\Oracle\oradata\ORCL\MyTablespace.DBF'
-- size 100m
-- autoextend on
-- next 10m;
-- create user ALILISYS
-- identified by 123456
-- default tablespace MyTablespace;
-- grant dba to ALILISYS;

--###########################################################
--###########################################################

create table users(
    v_id            varchar2(20) primary key,
    v_name          varchar2(40) not null, -- 昵称
    v_headshot      varchar2(40) not null, -- 头像文件名称
    v_account       varchar2(21) not null unique, -- 账号
    v_password      varchar2(21) not null, -- 密码
    v_gender        number(1)    not null, -- 1：男，2：女，0：保密
    v_register_date date         default sysdate not null, -- 注册时间
    v_channel_sort  varchar2(50) default '1,2,3,4,5,6,7,8,9,a,b,c,d' not null, -- 用户频道排序
    v_status        number(1)    default 0, -- 状态  0：正常
    v_identity      number(1)    default 0, -- 身份  0:普通用户
    v_video         number(5)    default 0, -- 视频数目
    v_likes         number       default 0, -- 被点赞数
    v_concern       number       default 0, -- 关注数
    v_fans          number       default 0, -- 粉丝数
    v_collection    number       default 0, -- 收藏数
    check (v_gender in (0,1,2))
);
create sequence seq_user;
create index index_user_acc_pwd on users(v_account, v_password) reverse;



create table follow(
    v_user1 varchar2(20),  -- 关注者
    v_user2 varchar2(20),  -- 被关注者
    v_date  date not null, -- 关注时间
    check ( v_user1 != v_user2 ),
    primary key (v_user1, v_user2),
    foreign key (v_user1) references users(v_id),
    foreign key (v_user2) references users(v_id)
);
create index index_follow_user2 on follow(v_user2) reverse;




create table channel(
    v_id   char(1)       primary key,
    v_name varchar2(20)  not null unique, -- 分区名
    v_img  varchar2(500) not null         -- 图标
);
insert into channel values ('1', '影视', 'dianshiju-off.svg');
insert into channel values ('2', '动漫', 'dongman-off.svg');
insert into channel values ('3', '游戏', 'youxi-off.svg');
insert into channel values ('4', '音乐', 'yinyue-off.svg');
insert into channel values ('5', '美食', 'meishi-off.svg');
insert into channel values ('6', '搞笑', 'gaoxiao-off.svg');
insert into channel values ('7', '运动', 'yundong-off.svg');
insert into channel values ('8', '日常', 'richang-off.svg');
insert into channel values ('9', '科技', 'keji-off.svg');
insert into channel values ('a', '动物', 'dongwu-off.svg');
insert into channel values ('b', '鬼畜', 'guicu-off.svg');
insert into channel values ('c', '军事', 'junshi-off.svg');
insert into channel values ('d', '时尚', 'shishang-off.svg');
commit;



create table tags(
    v_id         number       primary key,
    v_name       varchar2(50) not null, -- 标签名
    v_creator    varchar2(20) not null, -- 创建者
    v_channel    char(1)      not null, -- 所属频道id
    v_count      number       not null, -- 使用次数
    unique (v_channel, v_name),
    foreign key (v_creator) references users(v_id),
    foreign key (v_channel) references channel(v_id)
);
create sequence seq_tag;



create table videos(
    v_id            varchar2(20)   primary key,
    v_title         varchar2(160)  not null, -- 标题
    v_describe      varchar2(1440) not null, -- 简介
    v_author        varchar2(20)   not null, -- 作者id
    v_channel       char(1)        not null, -- 所属分区
    v_video         varchar2(40)   not null, -- 视频文件名
    v_cover         varchar2(40)   not null, -- 封面文件名
    v_total_time    varchar2(10)   not null, -- 视频总时长
    v_date          date           default sysdate not null, -- 发布时间
    v_view_count    number         default 0 not null, -- 播放数
    v_barrage_count number         default 0 not null, -- 弹幕数
    v_comment_count number         default 0 not null, -- 评论数
    v_likes         number         default 0 not null, -- 点赞数
    v_collection    number         default 0 not null, -- 收藏数
    constraint fk_videos_author foreign key (v_author) references users(v_id),
    constraint fk_videos_channel foreign key (v_channel) references channel(v_id)
);
create sequence seq_video;
create bitmap index index_video_channel on videos(v_channel);
create index index_video_author on videos(v_author);




create table video_tag(
    v_video varchar2(20) not null,
    v_tag   number       not null,
    primary key (v_video, v_tag),
    foreign key (v_video) references videos(v_id),
    foreign key (v_tag) references tags(v_id)
);
create index index_video_tag on video_tag(v_tag);



create table comments(
    v_id         number         primary key,
    v_content    varchar2(1440) not null, -- 评论内容
    v_author     varchar2(20)   not null, -- 评论者
    v_video      varchar2(20)   not null, -- 评论的视频
    v_date       date           default sysdate not null, -- 评论时间
    v_likes      number         default 0 not null, -- 点赞数
    v_to_comment number, -- 是不是层中层评论  null：不是  为数字：被回复的评论id
    constraint fk_comments_author foreign key (v_author) references users(v_id),
    constraint fk_comments_video foreign key (v_video) references videos(v_id)
);
create sequence seq_comment;
create index index_comment_author on comments(v_author);
create index index_comment_video on comments(v_video);



create table massages(
    v_id       number         primary key,
    v_receiver varchar2(20)   not null, -- 接收方
    v_sender   varchar2(20)   not null, -- 发送方
    v_content  varchar2(1440) not null, -- 内容
    v_visited  number(1)      not null, -- 是否被访问，0：未访问，1：已访问
    check (v_visited in(0,1)),
    foreign key (v_receiver) references users(v_id),
    foreign key (v_sender) references users(v_id)
);
create sequence seq_massage;
create index index_massage on massages(v_receiver, v_sender) reverse;



create table thumbs_up (
    v_user  varchar(20), -- 点赞者
    v_cv_id varchar(20), -- 被点赞的评论或视频
    v_type  number(1), -- 0：视频，1：评论
    check(v_type in (1,0)),
    primary key (v_user, v_cv_id, v_type),
    foreign key (v_user) references users(v_id)
);




create table barrages(
    v_id      number        primary key,
    v_content varchar2(320) not null, -- 内容
    v_video   varchar2(20)  not null, -- 视频id
    v_author  varchar2(20)  not null, -- 发布者id
    v_time    number        not null, -- 出现时间
    v_move    number(1)     not null, -- 是否为移动弹幕
    v_color   varchar2(40)  not null, -- 颜色
    v_date    date          default sysdate not null, -- 时期
    foreign key (v_video) references videos(v_id),
    foreign key (v_author) references users(v_id)
);
create sequence seq_barrage;
create index index_barrage_author on barrages(v_author) reverse;
create index index_barrage_video on barrages(v_video) reverse;




create table collection(
    v_id     number       primary key,
    v_user   varchar2(20)                    not null,
    v_video  varchar2(20)                    not null,
    v_date   date         default sysdate    not null,
    v_folder varchar2(20) default '默认收藏夹' not null,
    unique (v_user, v_folder, v_video),
    foreign key (v_user) references users(v_id),
    foreign key (v_video) references videos(v_id)
);
create sequence seq_collection;




--###########################################################
--###########################################################
-- 例程
--###########################################################
--###########################################################

create function f_dec2hex(p_int in varchar2) return varchar2 is
begin
    return upper(trim(to_char(p_int, 'xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx')));
exception
    when others then
        return null;
end f_dec2hex;



create procedure pro_video_upload(
    t_title      in  videos.v_title%type,
    t_describe   in  videos.v_describe%type,
    t_author     in  videos.v_author%type,
    t_channel    in  videos.v_channel%type,
    t_video      in  videos.v_video%type,
    t_cover      in  videos.v_cover%type,
    t_total_time in  videos.v_total_time%type,
    t_video_id   out videos.v_id%type
) as
begin
    t_video_id := f_dec2hex(seq_video.nextval);
    insert into videos
    (v_id,v_title,v_describe,v_author,v_channel,v_video,v_cover,v_total_time)
    values (t_video_id,t_title,t_describe,t_author,t_channel,t_video,t_cover,t_total_time);
exception
    when others then
        rollback;
        t_video_id := null;
end pro_video_upload;



create procedure pro_comm_send(
    acc        in  users.v_account%type,
    pwd        in  users.v_password%type,
    video      in  comments.v_video%type,
    content    in  comments.v_content%type,
    to_comment in  comments.v_to_comment%type,
    comment_id out comments.v_id%type
) AS
begin
    comment_id := f_dec2hex(seq_comment.nextval);
    insert into comments (v_id,v_content,v_video,v_to_comment,v_author)
    values (comment_id,content,video,to_comment,
            (select v_id from users where V_ACCOUNT=acc and V_PASSWORD=pwd));
    commit;
exception
    when others then
        rollback ;
        dbms_output.put_line ('发生异常，错误码：' || SQLCODE || '; 错误信息：' || sqlerrm);
        comment_id := null;
end pro_comm_send;





--###########################################################
--###########################################################
-- 触发器
--###########################################################
--###########################################################

create trigger tri_user_insert before insert on users for each row
begin
    :new.v_id:=f_dec2hex(seq_user.nextval);
end;
create trigger tri_user_delete before delete on users for each row
begin
    delete from massages where v_receiver=:old.v_id or v_sender=:old.v_id;
    delete from videos where v_author=:old.v_id;
    delete from follow where v_user1=:old.v_id or v_user2=:old.v_id;
    delete from barrages where v_author=:old.v_id;
    delete from comments where v_author=:old.v_id;
end;



create trigger tri_follow_insert before insert on follow for each row
begin
    update users set v_fans=v_fans+1 where v_id=:new.v_user2;
    update users set v_concern=v_concern+1 where v_id=:new.v_user1;
end;
create trigger tri_follow_delete before delete on follow for each row
begin
    update users set v_fans=v_fans-1 where v_id=:old.v_user2;
    update users set v_concern=v_concern-1 where v_id=:old.v_user1;
end;



create trigger tri_video_insert before insert on videos for each row
begin
    update users set v_video=v_video+1 where v_id=:new.v_author;
end;
create trigger tri_video_delete before delete on videos for each row
begin
    delete from comments where v_video=:old.v_id;
    delete from barrages where v_video=:old.v_id;
    delete from video_tag where v_video=:old.v_id;
    update users set v_video=v_video-1 where v_id=:old.v_author;
end;



create trigger tri_comment_insert after insert on comments for each row
begin
    update videos set v_comment_count=v_comment_count+1 where v_id=:new.v_video;
end;
create trigger tri_comment_delete before delete on comments for each row
begin
    update videos set v_comment_count=v_comment_count-1 where v_id=:old.v_video;
end;



create trigger tri_thumbs_insert before insert on thumbs_up for each row
begin
    if :new.v_type = 0 then
        update videos set v_likes=v_likes+1 where v_id=:new.v_cv_id;
    else
        update comments set v_likes=v_likes+1 where v_id=:new.v_cv_id;
    end if;
end;
create trigger tri_thumbs_delete after delete on thumbs_up for each row
begin
    if :old.v_type = 0 then
        update videos set v_likes=v_likes-1 where v_id=:old.v_cv_id;
    else
        update comments set v_likes=v_likes-1 where v_id=:old.v_cv_id;
    end if;
end;



create trigger tri_brg_insert before insert on barrages for each row
begin
    :new.v_id:=seq_barrage.nextval;
    update videos set v_barrage_count=v_barrage_count+1 where v_id=:new.v_video;
end;
create trigger tri_brg_delete after delete on barrages for each row
begin
    update videos set v_barrage_count=v_barrage_count-1 where v_id=:old.v_video;
end;



create trigger tri_coll_insert after insert on collection for each row
begin
    update videos set v_collection=v_collection+1 where v_id=:new.v_video;
    update users set v_collection=v_collection+1 where v_id=:new.v_user;
end;
create trigger tri_coll_delete after delete on collection for each row
begin
    update videos set v_collection=v_collection-1 where v_id=:old.v_video;
    update users set v_collection=v_collection-1 where v_id=:old.v_user;
end;




