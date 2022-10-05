getOriginUser
===
* 查询user数据

select * from test_user

all
===
* 查询user数据

select * from data_flow

insertUser
===
* 插入数据

insert into test_user(id,name,phone,title,email,gender,date_of_birth
    ,sys_create_time,sys_create_user,sys_update_time,sys_update_user)
values (#id#,#name#,#phone#,#title#,#email#,#gender#,#dateOfBirth#
    ,#sysCreateTime#,#sysCreateUser#,#sysUpdateTime#,#sysUpdateUser#)


getOriginIncreUser
===
* 查询user数据

select * from test_user
WHERE 1=1
@if(!isEmpty(lastUpdateTime)){
AND (sys_create_time >= #lastUpdateTime# OR sys_update_time >= #lastUpdateTime#)
@}

getTargetIncreUser
===
* 查询user数据

select * from test_user
WHERE 1=1
@if(!isEmpty(lastUpdateTime)){
AND (sys_create_time >= #lastUpdateTime# OR sys_update_time >= #lastUpdateTime#)
@}

insertIncreUser
===
* 插入数据

insert into test_user(id,name,phone,
@if(!isEmpty(title)){
title,
@}
email,gender,date_of_birth
    ,sys_create_time,sys_create_user,sys_update_time,sys_update_user)
values (#id#,#name#,#phone#
@if(!isEmpty(title)){
,#title#
@}
,#email#,#gender#,#dateOfBirth#
    ,#sysCreateTime#,#sysCreateUser#,#sysUpdateTime#,#sysUpdateUser#)
ON DUPLICATE KEY UPDATE 
id = VALUES(id),
name = VALUES(name),
phone = VALUES(phone),
@if(!isEmpty(title)){
title = VALUES(title),
@}
email = VALUES(email),
gender = VALUES(gender),
date_of_birth = VALUES(date_of_birth),
sys_create_time = VALUES(sys_create_time),
sys_create_user = VALUES(sys_create_user),
sys_update_time = VALUES(sys_update_time),
sys_update_user = VALUES(sys_update_user)

insertTargetIncreUser
===
* 插入数据

insert into test_user(id,name,phone,
@if(!isEmpty(title)){
title,
@}
email,gender,date_of_birth
,sys_create_time,sys_create_user,sys_update_time,sys_update_user)
values (#id#,#name#,#phone#
@if(!isEmpty(title)){
,#title#
@}
,#email#,#gender#,#dateOfBirth#
,#sysCreateTime#,#sysCreateUser#,#sysUpdateTime#,#sysUpdateUser#)
ON DUPLICATE KEY UPDATE
id = VALUES(id),
name = VALUES(name),
phone = VALUES(phone),
@if(!isEmpty(title)){
title = VALUES(title),
@}
email = VALUES(email),
gender = VALUES(gender),
date_of_birth = VALUES(date_of_birth),
sys_create_time = VALUES(sys_create_time),
sys_create_user = VALUES(sys_create_user),
sys_update_time = VALUES(sys_update_time),
sys_update_user = VALUES(sys_update_user)

insertDataFlow
===
* 插入数据

insert into data_flow(id,executionTime,fromData,toData,status)
values (#id#,#executionTime#,#fromData#,#toData#,#status#)
ON DUPLICATE KEY UPDATE
id = VALUES(id),
executionTime = VALUES(executionTime),
fromData = VALUES(fromData),
toData = VALUES(toData),
status = VALUES(status)

