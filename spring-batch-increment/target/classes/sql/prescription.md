getEdgePrescription
===
*查询user数据

select * from prescription

getEdgeIncrePrescription
===
* 查询user数据

select * from prescription
WHERE 1=1
@if(!isEmpty(lastUpdateTime)){
AND (sys_create_time >= #lastUpdateTime# OR sys_update_time >= #lastUpdateTime#)
@}

getTargetIncrePrescription
===
* 查询user数据

select * from prescription
WHERE 1=1
@if(!isEmpty(lastUpdateTime)){
AND (sys_create_time >= #lastUpdateTime# OR sys_update_time >= #lastUpdateTime#)
@}

insertEdgeIncrePrescription
===
* 插入数据

insert into prescription(id,prescriptionNumber,prescriptionName,prescriptionDescription,prescriberId,prescriberName
,sys_create_time,sys_update_time)
values (#id#,#prescriptionNumber#,#prescriptionName#
,#prescriptionDescription#,#prescriberId#,#prescriberName#
,#sysCreateTime#,#sysUpdateTime#)
ON DUPLICATE KEY UPDATE
id = VALUES(id),
prescriptionNumber = VALUES(prescriptionNumber),
prescriptionName = VALUES(prescriptionName),
prescriptionDescription = VALUES(prescriptionDescription),
prescriberId = VALUES(prescriberId),
prescriberName = VALUES(prescriberName),
sys_create_time = VALUES(sys_create_time),
sys_update_time = VALUES(sys_update_time)

insertTargetIncrePrescription
===
* 插入数据

insert into prescription(id,prescriptionNumber,prescriptionName,prescriptionDescription,prescriberId,prescriberName
,sys_create_time,sys_update_time)
values (#id#,#prescriptionNumber#,#prescriptionName#
,#prescriptionDescription#,#prescriberId#,#prescriberName#
,#sysCreateTime#,#sysUpdateTime#)
ON DUPLICATE KEY UPDATE
id = VALUES(id),
prescriptionNumber = VALUES(prescriptionNumber),
prescriptionName = VALUES(prescriptionName),
prescriptionDescription = VALUES(prescriptionDescription),
prescriberId = VALUES(prescriberId),
prescriberName = VALUES(prescriberName),
sys_create_time = VALUES(sys_create_time),
sys_update_time = VALUES(sys_update_time)

insertPrescription
===
* 插入数据

insert into prescription(id,prescriptionNumber,prescriptionName,prescriptionDescription,prescriberId,prescriberName
,sys_create_time,sys_update_time)
values (#id#,#prescriptionNumber#,#prescriptionName#
,#prescriptionDescription#,#prescriberId#,#prescriberName#
,#sysCreateTime#,#sysUpdateTime#)
