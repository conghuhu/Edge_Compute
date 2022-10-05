package cn.cislc.dockerservice.utils;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.*;
import com.github.dockerjava.api.model.*;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientImpl;
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient;
import com.github.dockerjava.transport.DockerHttpClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.HttpHostConnectException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static com.github.dockerjava.api.model.HostConfig.newHostConfig;

/**
 * @author conghuhu
 * @create 2022-03-04 16:20
 */
@Slf4j
@Component
public class DockerUtils {

    @Value("${ca.path}")
    private String caPath;

    /**
     * 连接docker服务器
     *
     * @return
     */
    public DockerClient connectDocker(String tcpUrl) throws HttpHostConnectException {
        DefaultDockerClientConfig config = DefaultDockerClientConfig.createDefaultConfigBuilder()
                .withDockerHost(tcpUrl)
                .withDockerTlsVerify(true)
                .withDockerCertPath(caPath)
                .withDockerConfig(caPath)
                .withApiVersion("1.41")
                .withRegistryUsername("15841721425")
                .withRegistryPassword("@cong0917")
                .withRegistryEmail("15841721425@163.com")
                .withRegistryUrl("https://index.docker.io/v1/ ")
                .build();

        DockerHttpClient httpClient = new ApacheDockerHttpClient.Builder()
                .dockerHost(config.getDockerHost())
                .sslConfig(config.getSSLConfig())
                .maxConnections(150)
                .connectionTimeout(Duration.ofSeconds(10))
                .responseTimeout(Duration.ofSeconds(10))
                .build();
        DockerClient dockerClient = DockerClientImpl.getInstance(config, httpClient);
        try {
            dockerClient.pingCmd().exec();
        } catch (Exception e) {
            e.printStackTrace();
            throw new HttpHostConnectException("docker连接失败");
        }
        return dockerClient;
    }

    /**
     * 创建容器
     *
     * @param client
     * @return
     */
    public CreateContainerResponse createContainer(DockerClient client, String containerName,
                                                   String imageName, int externalPort, int exposedPort) throws IOException {
        //externalPort—>exposedPort
        ExposedPort tcp80 = ExposedPort.tcp(exposedPort);
        Ports portBindings = new Ports();
        portBindings.bind(tcp80, Ports.Binding.bindPort(externalPort));

        CreateContainerResponse container = client.createContainerCmd(imageName)
                .withName(containerName)
                .withHostConfig(newHostConfig().withPortBindings(portBindings))
                .withExposedPorts(tcp80).exec();
        return container;
    }

    /**
     * 启动容器
     *
     * @param client
     * @param containerId
     */
    public void startContainer(DockerClient client, String containerId) {
        client.startContainerCmd(containerId).exec();
    }

    /**
     * 重启容器
     *
     * @param client
     * @param containerId
     */
    public void restartContainer(DockerClient client, String containerId) {
        client.restartContainerCmd(containerId).exec();
    }

    /**
     * 停止容器
     *
     * @param client
     * @param containerId
     */
    public void stopContainer(DockerClient client, String containerId) {
        client.stopContainerCmd(containerId).exec();
    }

    /**
     * 删除容器
     *
     * @param client
     * @param containerId
     */
    public void removeContainer(DockerClient client, String containerId) {
        client.removeContainerCmd(containerId).exec();
    }


    /**
     * 创建service
     *
     * @param client
     * @param serviceName
     * @param imageName
     * @param publishPort
     * @param targetPort
     * @param netWork
     * @param constraints
     * @return
     */
    public CreateServiceResponse createService(DockerClient client, String serviceName,
                                               String imageName, int publishPort, int targetPort,
                                               String netWork, List<String> constraints
    ) throws IOException {
        ServiceSpec serviceSpec = initServiceSpec(serviceName, imageName, publishPort, targetPort, netWork, constraints);
        CreateServiceResponse response = client.createServiceCmd(serviceSpec).exec();
        return response;
    }

    /**
     * 获取所有的service
     *
     * @param client
     * @return
     * @throws IOException
     */
    public List<Service> getServiceListInfo(DockerClient client) throws IOException {
        List<Service> serviceList = client.listServicesCmd().exec();
        return serviceList;
    }

    /**
     * 检查某个service
     *
     * @param client
     * @param serviceId
     * @return
     * @throws IOException
     */
    public Service inspectService(DockerClient client, String serviceId) throws IOException {
        Service service = client.inspectServiceCmd(serviceId).exec();
        return service;
    }

    /**
     * 删除service
     *
     * @param client
     * @param serviceId
     * @throws IOException
     */
    public void deleteService(DockerClient client, String serviceId) throws IOException {
        client.removeServiceCmd(serviceId).exec();
    }

    /**
     * 更新一个service
     *
     * @param client
     * @param serviceName
     * @param imageName
     * @param publishPort
     * @param targetPort
     * @param netWork
     * @param constraints
     * @throws IOException
     */
    public void updateService(DockerClient client, String serviceName,
                              String imageName, int publishPort, int targetPort,
                              String netWork, List<String> constraints) throws IOException {
        ServiceSpec serviceSpec = initServiceSpec(serviceName, imageName, publishPort, targetPort, netWork, constraints);
        client.updateServiceCmd(serviceName, serviceSpec).exec();
    }

    /**
     * 初始化一个serviceSpec
     *
     * @param serviceName
     * @param imageName
     * @param publishPort
     * @param targetPort
     * @param netWork
     * @param constraints
     * @return
     */
    public ServiceSpec initServiceSpec(String serviceName, String imageName, int publishPort,
                                       int targetPort, String netWork, List<String> constraints) {
        List<PortConfig> ports = new ArrayList<>();
        ports.add(new PortConfig().withPublishedPort(publishPort).withTargetPort(targetPort));
        List<NetworkAttachmentConfig> networks = new ArrayList<>();
        networks.add(new NetworkAttachmentConfig().withTarget(netWork));
        ServiceSpec serviceSpec = new ServiceSpec().withName(serviceName)
                .withTaskTemplate(new TaskSpec()
                        .withForceUpdate(0)
                        .withContainerSpec(new ContainerSpec()
                                .withImage(imageName)
                        ).withPlacement(new ServicePlacement()
                                .withConstraints(constraints)
                        )
                )
                .withMode(new ServiceModeConfig().withReplicated(
                        new ServiceReplicatedModeOptions().withReplicas(1)
                ))
                .withNetworks(networks)
                .withEndpointSpec(new EndpointSpec()
                        .withMode(EndpointResolutionMode.VIP)
                        .withPorts(ports
                        ));
        return serviceSpec;
    }

    /**
     * 获取node列表
     *
     * @param client
     * @return
     * @throws IOException
     */
    public List<SwarmNode> getNodeList(DockerClient client) throws IOException {
        List<SwarmNode> swarmNodeList = client.listSwarmNodesCmd().exec();
        return swarmNodeList;
    }

    /**
     * 获取任务列表
     *
     * @param client
     * @return
     * @throws IOException
     */
    public List<Task> getTaskList(DockerClient client) throws IOException {
        List<Task> taskList = client.listTasksCmd().exec();
        return taskList;
    }

    /**
     * 获取网络列表
     *
     * @param client
     * @return
     * @throws IOException
     */
    public List<Network> getNetworkList(DockerClient client) throws IOException {
        List<Network> networkList = client.listNetworksCmd().exec();
        return networkList;
    }

    /**
     * 创建网络
     *
     * @param client
     * @param networkName
     * @param drive
     * @param checkDuplicate
     * @return
     * @throws IOException
     */
    public CreateNetworkResponse createNetwork(DockerClient client, String networkName, String drive
            , boolean checkDuplicate) throws IOException {
        CreateNetworkResponse response = client.createNetworkCmd()
                .withName(networkName)
                .withDriver(drive)
                .withCheckDuplicate(checkDuplicate)
                .exec();
        return response;
    }

    /**
     * 删除某个网络
     *
     * @param client
     * @param networkId
     * @throws IOException
     */
    public void deleteNetwork(DockerClient client, String networkId) throws IOException {
        client.removeNetworkCmd(networkId).exec();
    }
}
