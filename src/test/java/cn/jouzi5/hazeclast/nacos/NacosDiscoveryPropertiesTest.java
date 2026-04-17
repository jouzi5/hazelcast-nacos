package cn.jouzi5.hazeclast.nacos;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import com.hazelcast.config.properties.PropertyDefinition;
import com.hazelcast.config.properties.PropertyTypeConverter;
import com.hazelcast.core.TypeConverter;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import org.junit.Test;

public class NacosDiscoveryPropertiesTest {

  @Test
  public void testNacosServerAddrProperty() {
    assertNotNull(NacosDiscoveryProperties.NACOS_SERVERADDR);
    assertEquals("nacos_serveraddr", NacosDiscoveryProperties.NACOS_SERVERADDR.key());
  }

  @Test
  public void testNacosUsernameProperty() {
    assertNotNull(NacosDiscoveryProperties.NACOS_USERNAME);
    assertEquals("nacos_username", NacosDiscoveryProperties.NACOS_USERNAME.key());
  }

  @Test
  public void testNacosPasswordProperty() {
    assertNotNull(NacosDiscoveryProperties.NACOS_PASSWORD);
    assertEquals("nacos_password", NacosDiscoveryProperties.NACOS_PASSWORD.key());
  }

  @Test
  public void testNacosNamespaceProperty() {
    assertNotNull(NacosDiscoveryProperties.NACOS_NAMESPACE);
    assertEquals("nacos_namespace", NacosDiscoveryProperties.NACOS_NAMESPACE.key());
  }

  @Test
  public void testNacosGroupProperty() {
    assertNotNull(NacosDiscoveryProperties.NACOS_GROUP);
    assertEquals("nacos_group", NacosDiscoveryProperties.NACOS_GROUP.key());
  }

  @Test
  public void testPropertyTypeConverterIsString() {
    PropertyDefinition serverAddr = NacosDiscoveryProperties.NACOS_SERVERADDR;
    TypeConverter typeConverter = serverAddr.typeConverter();
    assertEquals(PropertyTypeConverter.STRING, typeConverter);
  }

  @Test
  public void testAllPropertiesHaveStringTypeConverter() {
    assertEquals(PropertyTypeConverter.STRING, NacosDiscoveryProperties.NACOS_SERVERADDR.typeConverter());
    assertEquals(PropertyTypeConverter.STRING, NacosDiscoveryProperties.NACOS_USERNAME.typeConverter());
    assertEquals(PropertyTypeConverter.STRING, NacosDiscoveryProperties.NACOS_PASSWORD.typeConverter());
    assertEquals(PropertyTypeConverter.STRING, NacosDiscoveryProperties.NACOS_NAMESPACE.typeConverter());
    assertEquals(PropertyTypeConverter.STRING, NacosDiscoveryProperties.NACOS_GROUP.typeConverter());
  }

  @Test
  public void testConstructorIsPrivate() throws Exception {
    Constructor<NacosDiscoveryProperties> constructor = NacosDiscoveryProperties.class.getDeclaredConstructor();
    assertTrue(Modifier.isPrivate(constructor.getModifiers()));
  }

  @Test
  public void testValidatorIsNull() {
    assertEquals(null, NacosDiscoveryProperties.NACOS_SERVERADDR.validator());
  }

  @Test
  public void testPropertyDefinitionsAreUnique() {
    assertTrue(NacosDiscoveryProperties.NACOS_SERVERADDR != NacosDiscoveryProperties.NACOS_USERNAME);
    assertTrue(NacosDiscoveryProperties.NACOS_SERVERADDR != NacosDiscoveryProperties.NACOS_PASSWORD);
    assertTrue(NacosDiscoveryProperties.NACOS_SERVERADDR != NacosDiscoveryProperties.NACOS_NAMESPACE);
    assertTrue(NacosDiscoveryProperties.NACOS_SERVERADDR != NacosDiscoveryProperties.NACOS_GROUP);
  }

  @Test
  public void testPropertyKeysAreDistinct() {
    assertTrue(!NacosDiscoveryProperties.NACOS_SERVERADDR.key().equals(NacosDiscoveryProperties.NACOS_USERNAME.key()));
    assertTrue(!NacosDiscoveryProperties.NACOS_SERVERADDR.key().equals(NacosDiscoveryProperties.NACOS_PASSWORD.key()));
    assertTrue(!NacosDiscoveryProperties.NACOS_USERNAME.key().equals(NacosDiscoveryProperties.NACOS_PASSWORD.key()));
  }
}
