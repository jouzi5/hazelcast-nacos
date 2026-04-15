package cn.jouzi5.hazeclast.nacos;

import static com.hazelcast.config.properties.PropertyTypeConverter.STRING;

import com.hazelcast.config.properties.PropertyDefinition;
import com.hazelcast.config.properties.PropertyTypeConverter;
import com.hazelcast.config.properties.SimplePropertyDefinition;
import com.hazelcast.config.properties.ValueValidator;

public class NacosDiscoveryProperties {

  public static final PropertyDefinition NACOS_SERVERADDR = property("nacos_serveraddr", STRING);

  public static final PropertyDefinition NACOS_USERNAME = property("nacos_username", STRING);

  public static final PropertyDefinition NACOS_PASSWORD = property("nacos_password", STRING);

  public static final PropertyDefinition NACOS_NAMESPACE = property("nacos_namespace", STRING);

  public static final PropertyDefinition NACOS_GROUP = property("nacos_group", STRING);

  private NacosDiscoveryProperties() {}

  private static PropertyDefinition property(String key, PropertyTypeConverter typeConverter) {
    return property(key, typeConverter, null);
  }

  private static PropertyDefinition property(
      String key, PropertyTypeConverter typeConverter, ValueValidator valueValidator) {
    return new SimplePropertyDefinition(key, true, typeConverter, valueValidator);
  }
}
