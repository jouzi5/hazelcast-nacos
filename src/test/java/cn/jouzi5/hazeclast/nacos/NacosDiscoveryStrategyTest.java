package cn.jouzi5.hazeclast.nacos;

import com.hazelcast.logging.ILogger;
import com.hazelcast.spi.discovery.DiscoveryNode;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class NacosDiscoveryStrategyTest {

    @Test(expected = RuntimeException.class)
    public void testStartThrowsWhenServerAddrIsNull() {
        ILogger logger = mock(ILogger.class);
        Map<String, Comparable> properties = new HashMap<>();
        properties.put("nacos_username", "test");
        properties.put("nacos_password", "test");

        NacosDiscoveryStrategy strategy = new NacosDiscoveryStrategy(null, logger, properties);
        strategy.start();
    }

    @Test(expected = RuntimeException.class)
    public void testStartThrowsWhenUsernameIsNull() {
        ILogger logger = mock(ILogger.class);
        Map<String, Comparable> properties = new HashMap<>();
        properties.put("nacos_serveraddr", "127.0.0.1:8848");
        properties.put("nacos_password", "test");

        NacosDiscoveryStrategy strategy = new NacosDiscoveryStrategy(null, logger, properties);
        strategy.start();
    }

    @Test(expected = RuntimeException.class)
    public void testStartThrowsWhenPasswordIsNull() {
        ILogger logger = mock(ILogger.class);
        Map<String, Comparable> properties = new HashMap<>();
        properties.put("nacos_serveraddr", "127.0.0.1:8848");
        properties.put("nacos_username", "test");

        NacosDiscoveryStrategy strategy = new NacosDiscoveryStrategy(null, logger, properties);
        strategy.start();
    }

    @Test
    public void testStrategyCanBeInstantiatedWithValidProperties() {
        ILogger logger = mock(ILogger.class);
        Map<String, Comparable> properties = new HashMap<>();
        properties.put("nacos_serveraddr", "127.0.0.1:8848");
        properties.put("nacos_username", "test");
        properties.put("nacos_password", "test");

        NacosDiscoveryStrategy strategy = new NacosDiscoveryStrategy(null, logger, properties);
        assertNotNull(strategy);
    }

    @Test
    public void testStrategyWithNamespaceProperty() {
        ILogger logger = mock(ILogger.class);
        Map<String, Comparable> properties = new HashMap<>();
        properties.put("nacos_serveraddr", "127.0.0.1:8848");
        properties.put("nacos_username", "test");
        properties.put("nacos_password", "test");
        properties.put("nacos_namespace", "test-namespace");

        NacosDiscoveryStrategy strategy = new NacosDiscoveryStrategy(null, logger, properties);
        assertNotNull(strategy);
    }

    @Test
    public void testStrategyWithGroupProperty() {
        ILogger logger = mock(ILogger.class);
        Map<String, Comparable> properties = new HashMap<>();
        properties.put("nacos_serveraddr", "127.0.0.1:8848");
        properties.put("nacos_username", "test");
        properties.put("nacos_password", "test");
        properties.put("nacos_group", "TEST_GROUP");

        NacosDiscoveryStrategy strategy = new NacosDiscoveryStrategy(null, logger, properties);
        assertNotNull(strategy);
    }

    @Test
    public void testStrategyWithAllProperties() {
        ILogger logger = mock(ILogger.class);
        Map<String, Comparable> properties = new HashMap<>();
        properties.put("nacos_serveraddr", "127.0.0.1:8848");
        properties.put("nacos_username", "test");
        properties.put("nacos_password", "test");
        properties.put("nacos_namespace", "test-namespace");
        properties.put("nacos_group", "TEST_GROUP");

        NacosDiscoveryStrategy strategy = new NacosDiscoveryStrategy(null, logger, properties);
        assertNotNull(strategy);
    }

    @Test
    public void testStrategyWithNullDiscoveryNode() {
        ILogger logger = mock(ILogger.class);
        Map<String, Comparable> properties = new HashMap<>();
        properties.put("nacos_serveraddr", "127.0.0.1:8848");
        properties.put("nacos_username", "test");
        properties.put("nacos_password", "test");

        NacosDiscoveryStrategy strategy = new NacosDiscoveryStrategy(null, logger, properties);
        assertNotNull(strategy);
    }
}
