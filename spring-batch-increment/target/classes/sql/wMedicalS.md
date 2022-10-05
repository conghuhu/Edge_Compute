getOriginWMedicalS
===
* 查询user数据

select * from test_user



insertWMedicalS
===
* 插入数据

insert into west_medical_storehouse(id,warehouseNumberName,drugNumber,drugName,description,manufacturerId,manufacturer
    ,batchNo,packingUnit,supplier,supplierName,sys_create_time,sys_update_time)
values (#id#,#warehouseNumberName#,#drugNumber#,#drugName#,#description#,#manufacturerId#,#manufacturer#
    ,#batchNo#,#packingUnit#,#supplier#,#supplierName#,#sysCreateTime#,#sysUpdateTime#)


getOriginIncreWMedicalS
===
* 查询user数据

select * from west_medical_storehouse
WHERE 1=1
@if(!isEmpty(lastUpdateTime)){
AND (sys_create_time >= #lastUpdateTime# OR sys_update_time >= #lastUpdateTime#)
@}

getTargetIncreWMedicalS
===
* 查询user数据

select * from west_medical_storehouse
WHERE 1=1
@if(!isEmpty(lastUpdateTime)){
AND (sys_create_time >= #lastUpdateTime# OR sys_update_time >= #lastUpdateTime#)
@}

insertIncreWMedicalS
===
* 插入数据

insert into west_medical_storehouse(id,warehouseNumberName,drugNumber,drugName,description,manufacturerId,manufacturer
    ,batchNo,packingUnit,supplier,supplierName,sys_create_time,sys_update_time)
values (#id#,#warehouseNumberName#,#drugNumber#,#drugName#,#description#,#manufacturerId#,#manufacturer#
    ,#batchNo#,#packingUnit#,#supplier#,#supplierName#,#sysCreateTime#,#sysUpdateTime#)
ON DUPLICATE KEY UPDATE 
id = VALUES(id),
warehouseNumberName = VALUES(warehouseNumberName),
drugNumber = VALUES(drugNumber),
drugName = VALUES(drugName),
description = VALUES(description),
manufacturerId = VALUES(manufacturerId),
manufacturer = VALUES(manufacturer),
batchNo = VALUES(batchNo),
packingUnit = VALUES(packingUnit),
supplier = VALUES(supplier),
supplierName = VALUES(supplierName),
sys_create_time = VALUES(sys_create_time),
sys_update_time = VALUES(sys_update_time)

insertTargetIncreWMedicalS
===
* 插入数据

insert into west_medical_storehouse(id,warehouseNumberName,drugNumber,drugName,description,manufacturerId,manufacturer
    ,batchNo,packingUnit,supplier,supplierName,sys_create_time,sys_update_time)
values (#id#,#warehouseNumberName#,#drugNumber#,#drugName#,#description#,#manufacturerId#,#manufacturer#
    ,#batchNo#,#packingUnit#,#supplier#,#supplierName#,#sysCreateTime#,#sysUpdateTime#)
ON DUPLICATE KEY UPDATE 
id = VALUES(id),
warehouseNumberName = VALUES(warehouseNumberName),
drugNumber = VALUES(drugNumber),
drugName = VALUES(drugName),
description = VALUES(description),
manufacturerId = VALUES(manufacturerId),
manufacturer = VALUES(manufacturer),
batchNo = VALUES(batchNo),
packingUnit = VALUES(packingUnit),
supplier = VALUES(supplier),
supplierName = VALUES(supplierName),
sys_create_time = VALUES(sys_create_time),
sys_update_time = VALUES(sys_update_time)



