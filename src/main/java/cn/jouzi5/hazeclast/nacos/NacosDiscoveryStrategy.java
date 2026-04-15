package cn.jouzi5.hazeclast.nacos;

import com.alibaba.nacos.api.PropertyKeyConst;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.alibaba.nacos.client.naming.NacosNamingService;
import com.hazelcast.cluster.Address;
import com.hazelcast.internal.util.StringUtil;
import com.hazelcast.logging.ILogger;
import com.hazelcast.spi.discovery.AbstractDiscoveryStrategy;
import com.hazelcast.spi.discovery.DiscoveryNode;
import com.hazelcast.spi.discovery.SimpleDiscoveryNode;
import java.net.UnknownHostException;
import java.util.*;

public class NacosDiscoveryStrategy extends AbstractDiscoveryStrategy {

  private final DiscoveryNode thisNode;
  private final ILogger logger;
  private NacosNamingService nacosNamingService;
  private String instanceName = DEFAULT_NAME;

  private static final String DEFAULT_NAME = "hazeclast";

  public NacosDiscoveryStrategy(
      DiscoveryNode discoveryNode, ILogger logger, Map<String, Comparable> properties) {
    super(logger, properties);
    this.thisNode = discoveryNode;
    this.logger = logger;
  }

  @Override
  public void start() {
    String serverAddr = getOrNull(NacosDiscoveryProperties.NACOS_SERVERADDR);
    String username = getOrNull(NacosDiscoveryProperties.NACOS_USERNAME);
    String password = getOrNull(NacosDiscoveryProperties.NACOS_PASSWORD);
    if (serverAddr == null || username == null || password == null) {
      throw new RuntimeException(
          "nacos properties [nacos_serverAddr, nacos_username, nacos_password] must be set");
    }
    Properties config = new Properties();
    config.setProperty(PropertyKeyConst.SERVER_ADDR, serverAddr);
    config.setProperty(PropertyKeyConst.USERNAME, username);
    config.setProperty(PropertyKeyConst.PASSWORD, password);

    String namespace = getOrNull(NacosDiscoveryProperties.NACOS_NAMESPACE);
    if (!StringUtil.isNullOrEmpty(namespace)) {
      config.setProperty(PropertyKeyConst.NAMESPACE, namespace);
    }
    try {
      nacosNamingService = createNamingService(config);
    } catch (NacosException e) {
      logger.fine("nacos namingService create err:", e);
      throw new RuntimeException(e);
    }

    if (this.thisNode != null) {
      String instanceName =
          Optional.ofNullable(thisNode.getProperties().get("nacosInstanceName"))
              .map(Object::toString)
              .orElse(null);
      if (!StringUtil.isNullOrEmpty(instanceName)) {
        this.instanceName = instanceName;
      }
      try {
        nacosNamingService.registerInstance(
            this.instanceName,
            getOrNull(NacosDiscoveryProperties.NACOS_GROUP),
            this.thisNode.getPrivateAddress().getHost(),
            thisNode.getPrivateAddress().getPort());
      } catch (NacosException e) {
        logger.fine("nacos serviceRegistry err:", e);
        throw new RuntimeException(e);
      }
    }
  }

  protected NacosNamingService createNamingService(Properties config) throws NacosException {
    return new NacosNamingService(config);
  }

  @Override
  public void destroy() {
    try {
      if (this.thisNode != null && this.nacosNamingService != null) {
        nacosNamingService.shutDown();
      }
    } catch (NacosException e) {
      logger.fine("nacos namingService shutdown err:", e);
      throw new RuntimeException(e);
    }
  }

  @Override
  public Iterable<DiscoveryNode> discoverNodes() {
    List<Instance> allInstances = null;
    try {
      String group = getOrNull(NacosDiscoveryProperties.NACOS_GROUP);
      allInstances = nacosNamingService.getAllInstances(this.instanceName, group);

      List<DiscoveryNode> nodes = new ArrayList<>(allInstances.size());
      for (Instance instance : allInstances) {
        nodes.add(new SimpleDiscoveryNode(new Address(instance.getIp(), instance.getPort())));
      }

      return nodes;
    } catch (NacosException | UnknownHostException e) {
      logger.fine("nacos namingService discoverNodes err:", e);
      throw new RuntimeException(e);
    }
  }
}
