package cn.jouzi5.hazeclast.nacos;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.config.Config;
import com.hazelcast.config.DiscoveryStrategyConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.test.HazelcastSerialClassRunner;
import com.hazelcast.test.HazelcastTestSupport;
import java.io.IOException;
import java.util.ArrayList;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.testcontainers.containers.GenericContainer;

@RunWith(HazelcastSerialClassRunner.class)
public class ClientSmokeTest extends HazelcastTestSupport {

  // use an unusual port so clients won't guess it without ZooKeeper
  private static final int HAZELCAST_BASE_PORT = 9999;
  private static final int CLUSTER_SIZE = 2;

  private GenericContainer nacosContainer;
  private final HazelcastInstance[] instances = new HazelcastInstance[CLUSTER_SIZE];

  @Before
  public void setUp() throws Exception {
    ArrayList<String> ports = new ArrayList<>();
    ports.add("8848:8848");
    ports.add("9848:9848");
    ports.add("18080:8080");

    nacosContainer =
        new GenericContainer("nacos/nacos-server:v3.2.0")
            .withEnv("NACOS_AUTH_TOKEN", "VGhpc0lzTXlDdXN0b21TZWNyZXRLZXkwMTIzNDU2Nzg=")
            .withEnv("NACOS_AUTH_IDENTITY_KEY", "nacos")
            .withEnv("NACOS_AUTH_IDENTITY_VALUE", "nacos")
            .withEnv("MODE", "standalone");
    nacosContainer.setPortBindings(ports);
    nacosContainer.start();

    Assert.assertEquals(8848, (int) nacosContainer.getMappedPort(8848));
    Assert.assertEquals(9848, (int) nacosContainer.getMappedPort(9848));

    Thread.sleep(100000);
  }

  @After
  public void tearDown() throws IOException {
    try {
      HazelcastClient.shutdownAll();
      Hazelcast.shutdownAll();
    } finally {
      nacosContainer.close();
    }
  }

  @Test
  public void testClientCanConnectionToCluster() {
    startCluster();
    ClientConfig clientConfig = createClientConfig();

    // throws an exception when it cannot connect to a cluster
    HazelcastClient.newHazelcastClient(clientConfig);
  }

  private ClientConfig createClientConfig() {
    DiscoveryStrategyConfig discoveryStrategyConfig = createDiscoveryConfig();
    ClientConfig clientConfig = new ClientConfig();
    clientConfig.setProperty("hazelcast.discovery.enabled", "true");
    clientConfig
        .getNetworkConfig()
        .getDiscoveryConfig()
        .addDiscoveryStrategyConfig(discoveryStrategyConfig);
    return clientConfig;
  }

  private void startCluster() {
    Config config = createMemberConfig();
    for (int i = 0; i < CLUSTER_SIZE; i++) {
      instances[i] = Hazelcast.newHazelcastInstance(config);
    }
  }

  private Config createMemberConfig() {
    Config config = new Config();
    config.getNetworkConfig().setPort(HAZELCAST_BASE_PORT);
    config.getNetworkConfig().getJoin().getMulticastConfig().setEnabled(false);
    config.setProperty("hazelcast.discovery.enabled", "true");

    DiscoveryStrategyConfig discoveryStrategyConfig = createDiscoveryConfig();
    config
        .getNetworkConfig()
        .getJoin()
        .getDiscoveryConfig()
        .addDiscoveryStrategyConfig(discoveryStrategyConfig);
    return config;
  }

  private DiscoveryStrategyConfig createDiscoveryConfig() {
    DiscoveryStrategyConfig discoveryStrategyConfig =
        new DiscoveryStrategyConfig(new NacosDiscoveryStrategyFactory());
    discoveryStrategyConfig.addProperty(
        NacosDiscoveryProperties.NACOS_SERVERADDR.key(), "localhost:8848");
    discoveryStrategyConfig.addProperty(NacosDiscoveryProperties.NACOS_USERNAME.key(), "nacos");
    discoveryStrategyConfig.addProperty(NacosDiscoveryProperties.NACOS_PASSWORD.key(), "nacos");
    discoveryStrategyConfig.addProperty(NacosDiscoveryProperties.NACOS_NAMESPACE.key(), "public");
    discoveryStrategyConfig.addProperty(
        NacosDiscoveryProperties.NACOS_GROUP.key(), "test-hazelcast");
    return discoveryStrategyConfig;
  }
}
