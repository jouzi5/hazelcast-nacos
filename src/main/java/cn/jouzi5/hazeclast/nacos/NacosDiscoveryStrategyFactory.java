package cn.jouzi5.hazeclast.nacos;

import com.hazelcast.config.properties.PropertyDefinition;
import com.hazelcast.logging.ILogger;
import com.hazelcast.spi.discovery.DiscoveryNode;
import com.hazelcast.spi.discovery.DiscoveryStrategy;
import com.hazelcast.spi.discovery.DiscoveryStrategyFactory;

import java.util.*;

public class NacosDiscoveryStrategyFactory implements DiscoveryStrategyFactory {

    private static final Collection<PropertyDefinition> PROPERTY_DEFINITIONS;

    static {
        List<PropertyDefinition> propertyDefinitions = new ArrayList<>();
        propertyDefinitions.add(NacosDiscoveryProperties.NACOS_SERVERADDR);
        propertyDefinitions.add(NacosDiscoveryProperties.NACOS_USERNAME);
        propertyDefinitions.add(NacosDiscoveryProperties.NACOS_PASSWORD);
        propertyDefinitions.add(NacosDiscoveryProperties.NACOS_NAMESPACE);
        propertyDefinitions.add(NacosDiscoveryProperties.NACOS_GROUP);
        PROPERTY_DEFINITIONS = Collections.unmodifiableCollection(propertyDefinitions);
    }

    @Override
    public Class<? extends DiscoveryStrategy> getDiscoveryStrategyType() {
        return NacosDiscoveryStrategy.class;
    }

    @Override
    public DiscoveryStrategy newDiscoveryStrategy(DiscoveryNode discoveryNode, ILogger logger, Map<String, Comparable> properties) {
        return new NacosDiscoveryStrategy(discoveryNode, logger, properties);
    }

    @Override
    public Collection<PropertyDefinition> getConfigurationProperties() {
        return PROPERTY_DEFINITIONS;
    }
}
