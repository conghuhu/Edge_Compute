//同步数据流向数据类型
export interface dataFlow {
  id: number;
  executionTime: string;
  fromData: string;
  toData: string;
  status: string;
}

//原始容器配置表单对象
export interface ContainerForm {
  serviceName: string;
  image: string;
  publishedPort: string;
  targetPort: string;
  netWork: string;
  constraints:string[];
}
//展示容器配置表单对象
export interface ShowContainerForm {
  serviceName: string;
  image: string;
  publishedPort: string;
  targetPort: string;
  netWork: string;
  constraints:string;
}
//容器显示Service信息
 interface ServiceName{
  createTime:string;
  tagetPort:number;
  publishedPort:number;
  constraints:string;
  publishMode:string;
}
//容器显示network信息
 interface NetWork{
  netName:string;
  scope:string
}
//容器原始信息对象
export interface ContainerInfo{
  image:string;
  service:ServiceName |null;
  targetPort:number;
  taskId:string;
  network:NetWork |null;
}
//容器展示信息对象
export interface  ContainerShow{
  image:string;
  createTime:string;
  targetPort:number;
  publishedPort:number;
  constraints:string;
  publishMode:string;
  taskId:string;
  netName:string;

}


//网关对象
export interface Gateway{
   id:string;
   path:string;
   url:string;
   enabled:boolean;
}
//节点列表
export interface NodeItem{
  architecture:string;
  createdTime:string;
  hostname:string;
  id:string;
  ip:string;
  label:string;
  os:string;
  role:string;
  updateTime:string;
}
//NetWork对象信息
export interface NetWorkItem{
  driver:string;
  id:string;
  netName:string;
  scope:string;
}

//获取网关变化记录
export interface NetWorkChange{
  fromservice:string;
  toservice:string;
  insertdate:string;
}