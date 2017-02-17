package com.ruijc.mybatis;

import com.github.pagehelper.PageHelper;
import com.ruijc.mybatis.cache.SerializerUtils;
import com.ruijc.mybatis.cache.redis.RedisUtils;
import com.ruijc.util.serialize.FSTSerializerImpl;
import com.ruijc.util.serialize.FastjsonSerializerImpl;
import com.ruijc.util.serialize.ISerializer;
import com.ruijc.util.serialize.KryoSerializerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import tk.mybatis.spring.mapper.MapperScannerConfigurer;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * Mybatis自动化配置
 *
 * @author Storezhang
 */
@Configuration
@AutoConfigureAfter(org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration.class)
@EnableConfigurationProperties({PageHelperProperties.class, MapperProperties.class})
@ComponentScan(basePackageClasses = {RedisUtils.class, SerializerUtils.class})
public class MybatisAutoConfiguration {

    @Autowired
    private PageHelperProperties helperProperties;
    @Autowired
    private MapperProperties mapperProperties;

    @Bean
    @ConditionalOnMissingBean(PlatformTransactionManager.class)
    @ConditionalOnBean(DataSource.class)
    public PlatformTransactionManager transactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean
    @ConditionalOnClass(name = "com.github.pagehelper.PageHelper")
    public PageHelper pageHelper(DataSource dataSource) {
        PageHelper pageHelper = new PageHelper();

        Properties properties = new Properties();
        if (null != helperProperties) {
            properties.setProperty("dialect", helperProperties.getDialect());
            properties.setProperty("offsetAsPageNum", helperProperties.getOffsetAsPageNum());
            properties.setProperty("rowBoundsWithCount", helperProperties.getRowBoundsWithCount());
            properties.setProperty("pageSizeZero", helperProperties.getPageSizeZero());
            properties.setProperty("reasonable", helperProperties.getReasonable());
            properties.setProperty("params", helperProperties.getParams());
            properties.setProperty("supportMethodsArguments", helperProperties.getSupportMethodsArguments());
            properties.setProperty("returnPageInfo", helperProperties.getReturnPageInfo());
            properties.setProperty("closeConn", helperProperties.getCloseConn());
        }
        pageHelper.setProperties(properties);

        return pageHelper;
    }

    @Bean
    @ConditionalOnClass(name = "tk.mybatis.spring.mapper.MapperScannerConfigurer")
    public MapperScannerConfigurer mapperScannerConfigurer() {
        MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();

        mapperScannerConfigurer.setSqlSessionFactoryBeanName("sqlSessionFactory");
        mapperScannerConfigurer.setBasePackage("com.isea533.mybatis.mapper");
        Properties properties = new Properties();
        if (null != mapperProperties) {
            properties.setProperty("mappers", mapperProperties.getMappers());
            properties.setProperty("notEmpty", mapperProperties.getNotEmpty());
            properties.setProperty("IDENTITY", mapperProperties.getIdentity());
            properties.setProperty("ORDER", mapperProperties.getOrder());
            properties.setProperty("catalog", mapperProperties.getCatalog());
            properties.setProperty("schema", mapperProperties.getSchema());
            properties.setProperty("seqFormat", mapperProperties.getSeqFormat());
            properties.setProperty("style", mapperProperties.getStyle());
            properties.setProperty("enableMethodAnnotation", mapperProperties.getEnableMethodAnnotation());
        }
        mapperScannerConfigurer.setProperties(properties);

        return mapperScannerConfigurer;
    }

    @Bean
    @ConditionalOnClass(name = "com.alibaba.fastjson.JSON")
    @ConditionalOnProperty(name = {"mybatis.cache.serializer.type"}, havingValue = "fastjson", matchIfMissing = true)
    public ISerializer<Object> fastjsonSerializer() {
        return new FastjsonSerializerImpl<Object>();
    }

    @Bean
    @ConditionalOnClass(name = "com.esotericsoftware.kryo.Kryo")
    @ConditionalOnProperty(name = {"mybatis.cache.serializer.type"}, havingValue = "kryo", matchIfMissing = true)
    public ISerializer<Object> kryoSerializer() {
        return new KryoSerializerImpl<Object>();
    }

    @Bean
    @ConditionalOnClass(name = "org.nustaq.serialization.FSTConfiguration")
    @ConditionalOnProperty(name = {"mybatis.cache.serializer.type"}, havingValue = "fst", matchIfMissing = true)
    public ISerializer<Object> fstSerializer() {
        return new FSTSerializerImpl<Object>();
    }

    @Bean
    @ConditionalOnClass(name = "org.springframework.data.redis.core.RedisTemplate")
    @ConditionalOnProperty(name = {"mybatis.cache.type"}, havingValue = "redis", matchIfMissing = true)
    public RedisTemplate<byte[], byte[]> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<byte[], byte[]> template = new RedisTemplate<byte[], byte[]>();
        template.setConnectionFactory(factory);

        return template;
    }
}
