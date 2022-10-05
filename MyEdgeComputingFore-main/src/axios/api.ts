import instance from "./request";
import{
    dataFlow, Gateway,NodeItem,ContainerInfo,ContainerForm,NetWorkItem,NetWorkChange,
}from "~/axios/globalInterface"
/**
 * 获取数据流向
 * @returns 
 */
export function getUserInfo(): Promise<dataFlow[]> {
    return instance.get(`/getDataFlow`);
  }
/**
 * 西药库到云端数据同步
 * @returns 
 */
export function westToYun():Promise<any>{
    return instance.get('/run_medical');
}
/**
 * 云端到西药库数据同步
 * @returns 
 */
export function yunTowest():Promise<any>{
    return instance.get('/reverse_medical');
}
/**
 * 处方到云端数据同步
 * @returns 
 */
 export function prescriptionToYun():Promise<any>{
    return instance.get('/run_prescription');
}
/**
 * 云端到处方数据同步
 * @returns 
 */
 export function yunToPrescription():Promise<any>{
    return instance.get('/reverse_prescription');
}
/**
 * 西药库崩溃数据同步
 * @returns 
 */
 export function hangDeadWest():Promise<any>{
    return instance.get('/hangDead_medical');
}
/**
 * 处方崩溃数据同步
 * @returns 
 */
 export function hangDeadPrescription():Promise<any>{
    return instance.get('/hangDead_prescription');
}
/**
 * 获取节点列表
 * @returns 
 */
 export function getNodeList():Promise<NodeItem[]>{
    return instance.get('/docker/node/getNodeList');
}
/**
 * 获取网关信息
 * @returns 
 */
 export function getGateWay():Promise<any>{
    return instance.get('/exchange/getList');
}
/**
 * 更新网关信息
 * @returns 
 *
 */
 export function updateGateWay(form:Gateway):Promise<any>{
    return instance.post('/exchange/updateGate',form);
}
/**
 * 删除网关信息
 * @returns 
 */
 export function deleteeGateWay(pathId:string):Promise<any>{
    return instance.delete(`/exchange/delete/?id=${pathId}`);
}
/**
 * 新增网关
 * @returns 
 *
 */
 export function createGateWay(form:Gateway):Promise<any>{
    return instance.post('/exchange/create',form);
}
/**
 * 查询容器信息
 * @returns 
 *
 */
 export function getContainerInfo(vaule:string):Promise<ContainerInfo[]>{
    return instance.get(`/docker/container/getContainerListByNodeId/${vaule}`);
}

/**
 * 指定服务器创建容器
 * @returns 
 *
 */
 export function createContainer(vaule:ContainerForm):Promise<any>{
    return instance.post('/docker/service/createService',vaule);
}

/**
 * 获取NetWork信息
 * @returns 
 *
 */
 export function getNetWork():Promise<NetWorkItem[]>{
    return instance.get('/docker/network/getNetworkList');
}

/**
 * 获取网关变化记录
 * @returns 
 *
 */
 export function getUpdateInfo():Promise<NetWorkChange[]>{
    return instance.get('/exchange/getupdateinfo');
}