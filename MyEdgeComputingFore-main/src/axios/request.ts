import axios, { AxiosRequestConfig, AxiosResponse } from "axios";
import router from "~/router";
import store from "~/store";
const instance = axios.create({
  //baseURL: "/api",
  //baseURL: "http://localhost:8080/example/increment",
  baseURL:"http://124.222.66.170:10010",
  //baseURL:"http://124.222.224.173:9528",
  timeout:10000
});

//post请求头
instance.defaults.headers.post["Content-Type"] =
  "application/x-www-form-urlencoded;charset=UTF-8";

  instance.interceptors.request.use(
    function (config) {
      // Message.loading({ content: "加载中" });
      //let token = localStorage.getItem("token");
      //token && (config.headers!.token = token);
      return config;
      // 在发送请求之前做些什么
    },
    function (error) {
      // 对请求错误做些什么
      return Promise.reject(error);
    }
  );

  // 添加响应拦截器
instance.interceptors.response.use(
  function (res) {
    const { code, data, message, success } = res.data;
    // Message.clear();
    // 对响应数据做点什么
    if (code == 200) {
      return data;
    } 
    if (!success) {
      //Message.error({ content: `${message}` });
      console.error(res);
      return Promise.reject(message);
    }
  },
  function (error) {
    //Message.error({ content: `${error}` });
    return Promise.reject(error);
  }
);
export default instance;