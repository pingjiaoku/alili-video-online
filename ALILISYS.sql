


--###########################################################
--###########################################################

-- �����û� alilisys�����и��ļǵ���jdbc.properties�и���
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
    v_name          varchar2(40) not null, -- �ǳ�
    v_headshot      varchar2(40) not null, -- ͷ���ļ�����
    v_account       varchar2(21) not null unique, -- �˺�
    v_password      varchar2(21) not null, -- ����
    v_gender        number(1)    not null, -- 1���У�2��Ů��0������
    v_register_date date         default sysdate not null, -- ע��ʱ��
    v_channel_sort  varchar2(50) default '1,2,3,4,5,6,7,8,9,a,b,c,d' not null, -- �û�Ƶ������
    v_status        number(1)    default 0, -- ״̬  0������
    v_identity      number(1)    default 0, -- ���  0:��ͨ�û�
    v_video         number(5)    default 0, -- ��Ƶ��Ŀ
    v_likes         number       default 0, -- ��������
    v_concern       number       default 0, -- ��ע��
    v_fans          number       default 0, -- ��˿��
    v_collection    number       default 0, -- �ղ���
    check (v_gender in (0,1,2))
);
create sequence seq_user;
create index index_user_acc_pwd on users(v_account, v_password) reverse;



create table follow(
    v_user1 varchar2(20),  -- ��ע��
    v_user2 varchar2(20),  -- ����ע��
    v_date  date not null, -- ��עʱ��
    check ( v_user1 != v_user2 ),
    primary key (v_user1, v_user2),
    foreign key (v_user1) references users(v_id),
    foreign key (v_user2) references users(v_id)
);
create index index_follow_user2 on follow(v_user2) reverse;




create table channel(
    v_id   char(1)       primary key,
    v_name varchar2(20)  not null unique, -- ������
    v_img  varchar2(500) not null         -- ͼ��
);
insert into channel values ('1', 'Ӱ��', 'dianshiju-off.svg');
insert into channel values ('2', '����', 'dongman-off.svg');
insert into channel values ('3', '��Ϸ', 'youxi-off.svg');
insert into channel values ('4', '����', 'yinyue-off.svg');
insert into channel values ('5', '��ʳ', 'meishi-off.svg');
insert into channel values ('6', '��Ц', 'gaoxiao-off.svg');
insert into channel values ('7', '�˶�', 'yundong-off.svg');
insert into channel values ('8', '�ճ�', 'richang-off.svg');
insert into channel values ('9', '�Ƽ�', 'keji-off.svg');
insert into channel values ('a', '����', 'dongwu-off.svg');
insert into channel values ('b', '����', 'guicu-off.svg');
insert into channel values ('c', '����', 'junshi-off.svg');
insert into channel values ('d', 'ʱ��', 'shishang-off.svg');
commit;



create table tags(
    v_id         number       primary key,
    v_name       varchar2(50) not null, -- ��ǩ��
    v_creator    varchar2(20) not null, -- ������
    v_channel    char(1)      not null, -- ����Ƶ��id
    v_count      number       not null, -- ʹ�ô���
    unique (v_channel, v_name),
    foreign key (v_creator) references users(v_id),
    foreign key (v_channel) references channel(v_id)
);
create sequence seq_tag;



create table videos(
    v_id            varchar2(20)   primary key,
    v_title         varchar2(160)  not null, -- ����
    v_describe      varchar2(1440) not null, -- ���
    v_author        varchar2(20)   not null, -- ����id
    v_channel       char(1)        not null, -- ��������
    v_video         varchar2(40)   not null, -- ��Ƶ�ļ���
    v_cover         varchar2(40)   not null, -- �����ļ���
    v_total_time    varchar2(10)   not null, -- ��Ƶ��ʱ��
    v_date          date           default sysdate not null, -- ����ʱ��
    v_view_count    number         default 0 not null, -- ������
    v_barrage_count number         default 0 not null, -- ��Ļ��
    v_comment_count number         default 0 not null, -- ������
    v_likes         number         default 0 not null, -- ������
    v_collection    number         default 0 not null, -- �ղ���
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
    v_content    varchar2(1440) not null, -- ��������
    v_author     varchar2(20)   not null, -- ������
    v_video      varchar2(20)   not null, -- ���۵���Ƶ
    v_date       date           default sysdate not null, -- ����ʱ��
    v_likes      number         default 0 not null, -- ������
    v_to_comment number, -- �ǲ��ǲ��в�����  null������  Ϊ���֣����ظ�������id
    constraint fk_comments_author foreign key (v_author) references users(v_id),
    constraint fk_comments_video foreign key (v_video) references videos(v_id)
);
create sequence seq_comment;
create index index_comment_author on comments(v_author);
create index index_comment_video on comments(v_video);



create table massages(
    v_id       number         primary key,
    v_receiver varchar2(20)   not null, -- ���շ�
    v_sender   varchar2(20)   not null, -- ���ͷ�
    v_content  varchar2(1440) not null, -- ����
    v_visited  number(1)      not null, -- �Ƿ񱻷��ʣ�0��δ���ʣ�1���ѷ���
    check (v_visited in(0,1)),
    foreign key (v_receiver) references users(v_id),
    foreign key (v_sender) references users(v_id)
);
create sequence seq_massage;
create index index_massage on massages(v_receiver, v_sender) reverse;



create table thumbs_up (
    v_user  varchar(20), -- ������
    v_cv_id varchar(20), -- �����޵����ۻ���Ƶ
    v_type  number(1), -- 0����Ƶ��1������
    check(v_type in (1,0)),
    primary key (v_user, v_cv_id, v_type),
    foreign key (v_user) references users(v_id)
);




create table barrages(
    v_id      number        primary key,
    v_content varchar2(320) not null, -- ����
    v_video   varchar2(20)  not null, -- ��Ƶid
    v_author  varchar2(20)  not null, -- ������id
    v_time    number        not null, -- ����ʱ��
    v_move    number(1)     not null, -- �Ƿ�Ϊ�ƶ���Ļ
    v_color   varchar2(40)  not null, -- ��ɫ
    v_date    date          default sysdate not null, -- ʱ��
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
    v_folder varchar2(20) default 'Ĭ���ղؼ�' not null,
    unique (v_user, v_folder, v_video),
    foreign key (v_user) references users(v_id),
    foreign key (v_video) references videos(v_id)
);
create sequence seq_collection;




--###########################################################
--###########################################################
-- ����
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
        dbms_output.put_line ('�����쳣�������룺' || SQLCODE || '; ������Ϣ��' || sqlerrm);
        comment_id := null;
end pro_comm_send;





--###########################################################
--###########################################################
-- ������
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




