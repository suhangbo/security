create table persistent_logins (username varchar(64) not null, series varchar(64) primary key, token varchar(64) not null, last_used timestamp not null);


-- varchar 变长
-- char 定长
create table sys_user(
    id int primary key,
    username char(20),
    password char(100)
) ENGINE = MyIsam;