package cn.jouzi5.hazeclast.nacos;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.config.Config;
import com.hazelcast.config.DiscoveryStrategyConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.test.HazelcastSerialClassRunner;
import com.hazelcast.test.HazelcastTestSupport;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;

import java.io.IOException;
import java.time.Duration;

@RunWith(HazelcastSerialClassRunner.class)
public class ClientSmokeTest extends HazelcastTestSupport {

    //use an unusual port so clients won't guess it without ZooKeeper
    private static final int HAZELCAST_BASE_PORT = 9999;
    private static final int CLUSTER_SIZE = 2;

    private GenericContainer nacosContainer;
    private final HazelcastInstance[] instances = new HazelcastInstance[CLUSTER_SIZE];

    @Before
    public void setUp() throws Exception {
//        nacosContainer = new GenericContainer("nacos/nacos-server:v3.2.0")
//                .withExposedPorts(8848, 9848, 8080)
//                .withEnv("NACOS_AUTH_TOKEN", "VGhpc0lzTXlDdXN0b21TZWNyZXRLZXkwMTIzNDU2Nzg=")
//                .withEnv("NACOS_AUTH_IDENTITY_KEY", "nacos")
//                .withEnv("NACOS_AUTH_IDENTITY_VALUE", "nacos")
//                .withEnv("MODE", "standalone")
//                .waitingFor(
//                        Wait.forHttp("/nacos/v1/console/health/readiness")
//                                .forPort(8848)
//                                .forStatusCode(200)
//                                .withStartupTimeout(Duration.ofMinutes(3))
//                );
//        nacosContainer.start();
    }

    @After
    public void tearDown() throws IOException {
        try {
            HazelcastClient.shutdownAll();
            Hazelcast.shutdownAll();
        } finally {
//            nacosContainer.close();
        }
    }

    @Test
    public void testClientCanConnectionToCluster() {
        startCluster();
        ClientConfig clientConfig = createClientConfig();

        //throws an exception when it cannot connect to a cluster
        HazelcastClient.newHazelcastClient(clientConfig);
    }

    private ClientConfig createClientConfig() {
        DiscoveryStrategyConfig discoveryStrategyConfig = createDiscoveryConfig();
        ClientConfig clientConfig = new ClientConfig();
        clientConfig.setProperty("hazelcast.discovery.enabled", "true");
        clientConfig.getNetworkConfig().getDiscoveryConfig().addDiscoveryStrategyConfig(discoveryStrategyConfig);
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
        config.getNetworkConfig().getJoin().getDiscoveryConfig().addDiscoveryStrategyConfig(discoveryStrategyConfig);
        return config;
    }

    private DiscoveryStrategyConfig createDiscoveryConfig() {
        DiscoveryStrategyConfig discoveryStrategyConfig = new DiscoveryStrategyConfig(new NacosDiscoveryStrategyFactory());
        discoveryStrategyConfig.addProperty(NacosDiscoveryProperties.NACOS_SERVERADDR.key(), "localhost:8848");
        discoveryStrategyConfig.addProperty(NacosDiscoveryProperties.NACOS_USERNAME.key(), "nacos");
        discoveryStrategyConfig.addProperty(NacosDiscoveryProperties.NACOS_PASSWORD.key(), "nacos");
        discoveryStrategyConfig.addProperty(NacosDiscoveryProperties.NACOS_NAMESPACE.key(), "public");
        discoveryStrategyConfig.addProperty(NacosDiscoveryProperties.NACOS_GROUP.key(), "test-hazelcast");
        return discoveryStrategyConfig;
    }
}
