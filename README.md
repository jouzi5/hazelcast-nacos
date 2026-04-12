1. start nacos 
```shell
docker run --name nacos-standalone-derby     -e MODE=standalone     -e NACOS_AUTH_TOKEN=VGhpc0lzTXlDdXN0b21TZWNyZXRLZXkwMTIzNDU2Nzg=     -e NACOS_AUTH_IDENTITY_KEY=nacos     -e NACOS_AUTH_IDENTITY_VALUE=nacos     -p 8080:8080     -p 8848:8848     -p 9848:9848     -d nacos/nacos-server:v3.2.0
```

2. configure nacos properties for hazelcast
```java
    private DiscoveryStrategyConfig createDiscoveryConfig() {
        DiscoveryStrategyConfig discoveryStrategyConfig = new DiscoveryStrategyConfig(new NacosDiscoveryStrategyFactory());
        discoveryStrategyConfig.addProperty(NacosDiscoveryProperties.NACOS_SERVERADDR.key(), "localhost:8848");
        discoveryStrategyConfig.addProperty(NacosDiscoveryProperties.NACOS_USERNAME.key(), "nacos");
        discoveryStrategyConfig.addProperty(NacosDiscoveryProperties.NACOS_PASSWORD.key(), "nacos");
        discoveryStrategyConfig.addProperty(NacosDiscoveryProperties.NACOS_NAMESPACE.key(), "public");
        discoveryStrategyConfig.addProperty(NacosDiscoveryProperties.NACOS_GROUP.key(), "test-hazelcast");
        return discoveryStrategyConfig;
    }
```