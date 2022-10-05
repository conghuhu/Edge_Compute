package cn.cislc.dockerservice.service;

import cn.cislc.dockerservice.entity.Network;
import cn.cislc.dockerservice.param.NetWorkParam;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author conghuhu
 * @since 2022-03-07
 */
public interface NetworkService extends IService<Network> {

    List<Network> updateNetWorkList(List<com.github.dockerjava.api.model.Network> networkList);

    void createNetWork(NetWorkParam netWorkParam);

    void removeNetWork(String networkId);
}
