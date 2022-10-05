package cn.cislc.dockerservice.service.impl;

import cn.cislc.dockerservice.entity.Network;
import cn.cislc.dockerservice.mapper.NetworkMapper;
import cn.cislc.dockerservice.param.NetWorkParam;
import cn.cislc.dockerservice.service.NetworkService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author conghuhu
 * @since 2022-03-07
 */
@Service
public class NetworkServiceImpl extends ServiceImpl<NetworkMapper, Network> implements NetworkService {
    private final NetworkMapper networkMapper;

    public NetworkServiceImpl(NetworkMapper networkMapper) {
        this.networkMapper = networkMapper;
    }

    @Override
    public List<Network> updateNetWorkList(List<com.github.dockerjava.api.model.Network> networkList) {
        List<Network> resList = new ArrayList<>();
        networkList.forEach(item -> {
            Integer count = networkMapper.selectCount(new LambdaQueryWrapper<Network>().eq(Network::getId, item.getId()));
            Network network = new Network();
            network.setId(item.getId());
            network.setNetName(item.getName());
            network.setScope(item.getScope());
            network.setDriver(item.getDriver());
            resList.add(network);
            if (count > 0) {
                networkMapper.updateById(network);
            } else {
                networkMapper.insert(network);
            }
        });
        return resList;
    }

    @Override
    public void createNetWork(NetWorkParam netWorkParam) {
        Network network = new Network();
        BeanUtils.copyProperties(netWorkParam, network);
        network.setNetName(netWorkParam.getNetWorkName());
        networkMapper.insert(network);
    }

    @Override
    public void removeNetWork(String networkId) {
        networkMapper.deleteById(networkId);
    }
}
