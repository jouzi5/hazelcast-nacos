package cn.jouzi5.hazeclast.nacos;

import com.hazelcast.cluster.Address;
import com.hazelcast.logging.ILogger;
import com.hazelcast.spi.discovery.DiscoveryNode;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.rnorth.ducttape.unreliables.Unreliables;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.DockerImageName;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class NacosGenericContainerTest {

//    @ClassRule
    public static GenericContainer<?> nacosContainer = new GenericContainer<>(DockerImageName.parse("nacos/nacos-server:v3.2.0"))
            .withEnv("MODE", "standalone")
            .withExposedPorts(8848, 9848, 8080)
            .withEnv("NACOS_AUTH_TOKEN", "VGhpc0lzTXlDdXN0b21TZWNyZXRLZXkwMTIzNDU2Nzg=")
            .withEnv("NACOS_AUTH_IDENTITY_KEY", "nacos")
            .withEnv("NACOS_AUTH_IDENTITY_VALUE", "nacos");

    @Before
    public void setUp() throws Exception {
//        nacosContainer.start();
    }

    @Test
    public void testConnectionToNacosContainer() throws Exception {
//        String nacosHost = nacosContainer.getHost();
//        int nacosPort = nacosContainer.getMappedPort(8848);
        String nacosHost = "localhost";
        int nacosPort = 8848;

        String serverAddr = nacosHost + ":" + nacosPort;
        
        System.out.println("Nacos running at: " + serverAddr);

        Map<String, Comparable> properties = new HashMap<>();
        properties.put("nacos_serveraddr", serverAddr);
        properties.put("nacos_username", "nacos");
        properties.put("nacos_password", "nacos");
        properties.put("nacos_group", "DEFAULT_GROUP");

        ILogger logger = mock(ILogger.class);

        NacosDiscoveryStrategy strategy = new NacosDiscoveryStrategy(null, logger, properties);
        strategy.start();

        try {
            Iterable<DiscoveryNode> nodes = strategy.discoverNodes();
            assertNotNull(nodes);
            
            int nodeCount = 0;
            for (DiscoveryNode node : nodes) {
                nodeCount++;
                Address addr = node.getPrivateAddress();
                System.out.println("Discovered node: " + addr.getHost() + ":" + addr.getPort());
            }
            
            System.out.println("Total nodes discovered: " + nodeCount);
        } finally {
            strategy.destroy();
        }
    }
}
