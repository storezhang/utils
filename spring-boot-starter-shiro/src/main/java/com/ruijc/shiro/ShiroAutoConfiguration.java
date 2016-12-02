package com.ruijc.shiro;

import com.ruijc.shiro.annotation.EnableShiroWebSupport;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authc.credential.DefaultPasswordService;
import org.apache.shiro.authc.credential.PasswordService;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.crypto.CipherService;
import org.apache.shiro.crypto.hash.DefaultHashService;
import org.apache.shiro.io.Serializer;
import org.apache.shiro.mgt.RememberMeManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.AuthenticatingRealm;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.realm.jdbc.JdbcRealm;
import org.apache.shiro.session.SessionListener;
import org.apache.shiro.session.mgt.ExecutorServiceSessionValidationScheduler;
import org.apache.shiro.session.mgt.SessionValidationScheduler;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.session.mgt.eis.SessionIdGenerator;
import org.apache.shiro.session.mgt.quartz.QuartzSessionValidationScheduler;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.servlet.Cookie;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.shiro.web.session.mgt.WebSessionManager;
import org.quartz.Scheduler;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Import;

import javax.servlet.Filter;
import javax.sql.DataSource;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Shiro自动配置
 *
 * @author Storezhang
 */
@Configuration
@EnableShiroWebSupport
@ConditionalOnWebApplication
@Import(ShiroConfiguration.class)
@EnableConfigurationProperties({
        ShiroProperties.class, ShiroAuthFilterProperties.class,
        ShiroCookieProperties.class, ShiroSessionProperties.class,
        ShiroJdbcRealmProperties.class
})
public class ShiroAutoConfiguration {

    @Autowired
    private ShiroProperties shiroProperties;
    @Autowired
    private ShiroAuthFilterProperties authFilterProperties;
    @Autowired
    private ShiroCookieProperties shiroCookieProperties;
    @Autowired
    private ShiroSessionProperties shiroSessionProperties;
    @Autowired
    private ShiroJdbcRealmProperties shiroJdbcRealmProperties;
    @Autowired(required = false)
    private CipherService cipherService;
    @Autowired(required = false)
    private Serializer<PrincipalCollection> serializer;
    @Autowired(required = false)
    private Collection<SessionListener> listeners;
    @Autowired(required = false)
    private JdbcPermissionDefinitionsLoader jdbcPermissionDefinitionsLoader;
    @Autowired(required = false)
    private ShiroFilterCustomizer shiroFilterCustomizer;

    @Bean(name = "mainRealm")
    @ConditionalOnMissingBean(name = "mainRealm")
    @ConditionalOnProperty(prefix = "shiro.realm.jdbc", name = "enabled", havingValue = "true")
    @DependsOn(value = {"dataSource", "lifecycleBeanPostProcessor", "credentialsMatcher"})
    public Realm jdbcRealm(DataSource dataSource, CredentialsMatcher credentialsMatcher) {
        JdbcRealm realm = new JdbcRealm();

        if (shiroJdbcRealmProperties.getAuthenticationQuery() != null) {
            realm.setAuthenticationQuery(shiroJdbcRealmProperties.getAuthenticationQuery());
        }
        if (shiroJdbcRealmProperties.getUserRolesQuery() != null) {
            realm.setUserRolesQuery(shiroJdbcRealmProperties.getUserRolesQuery());
        }
        if (shiroJdbcRealmProperties.getPermissionsQuery() != null) {
            realm.setPermissionsQuery(shiroJdbcRealmProperties.getPermissionsQuery());
        }
        if (shiroJdbcRealmProperties.getSalt() != null) {
            realm.setSaltStyle(shiroJdbcRealmProperties.getSalt());
        }
        realm.setPermissionsLookupEnabled(shiroJdbcRealmProperties.isPermissionsLookupEnabled());
        realm.setDataSource(dataSource);
        realm.setCredentialsMatcher(credentialsMatcher);

        return realm;
    }

    @Bean(name = "mainRealm")
    @ConditionalOnMissingBean(name = "mainRealm")
    @DependsOn(value = {"lifecycleBeanPostProcessor", "credentialsMatcher"})
    public Realm realm(CredentialsMatcher credentialsMatcher) {
        Class<?> realmClass = shiroProperties.getRealmClass();
        Realm realm = (Realm) BeanUtils.instantiate(realmClass);
        if (realm instanceof AuthenticatingRealm) {
            ((AuthenticatingRealm) realm).setCredentialsMatcher(credentialsMatcher);
        }

        return realm;
    }

    @Bean(name = "cacheManager")
    @ConditionalOnClass(EhCacheManager.class)
    @ConditionalOnMissingBean(name = "cacheManager")
    public CacheManager ehcacheManager() {
        EhCacheManager ehCacheManager = new EhCacheManager();

        ShiroProperties.Ehcache ehcache = shiroProperties.getEhcache();
        if (ehcache.getConfigFile() != null) {
            ehCacheManager.setCacheManagerConfigFile(ehcache.getConfigFile());
        }

        return ehCacheManager;
    }

    @Bean
    @ConditionalOnMissingBean(Cookie.class)
    public Cookie rememberMeCookie() {
        SimpleCookie cookie = new SimpleCookie();

        cookie.setName(authFilterProperties.getRememberMeParamName());
        cookie.setMaxAge(shiroCookieProperties.getMaxAge());
        cookie.setValue(shiroCookieProperties.getValue());
        cookie.setVersion(shiroCookieProperties.getVersion());
        cookie.setHttpOnly(shiroCookieProperties.isHttpOnly());
        cookie.setSecure(shiroCookieProperties.isSecure());

        return cookie;
    }

    @Bean
    @ConditionalOnMissingBean(RememberMeManager.class)
    public RememberMeManager rememberMeManager(Cookie cookie) {
        CookieRememberMeManager manager = new CookieRememberMeManager();
        manager.setCookie(cookie);
        manager.setCipherService(cipherService);
        if (null != shiroCookieProperties.getCipherKey()) {
            manager.setCipherKey(shiroCookieProperties.getCipherKey().getBytes());
        } else {
            if (null != shiroCookieProperties.getEncryptionCipherKey()) {
                manager.setEncryptionCipherKey(shiroCookieProperties.getEncryptionCipherKey().getBytes());
            }
            if (null != shiroCookieProperties.getDecryptionCipherKey()) {
                manager.setDecryptionCipherKey(shiroCookieProperties.getDecryptionCipherKey().getBytes());
            }
        }
        manager.setSerializer(serializer);

        return manager;
    }

    @Bean
    @ConditionalOnMissingBean
    public SessionDAO sessionDAO(CacheManager cacheManager) {
        EnterpriseCacheSessionDAO dao = new EnterpriseCacheSessionDAO();
        dao.setActiveSessionsCacheName(shiroSessionProperties.getActiveSessionsCacheName());
        Class<? extends SessionIdGenerator> idGenerator = shiroSessionProperties.getIdGenerator();
        if (idGenerator != null) {
            SessionIdGenerator sessionIdGenerator = BeanUtils.instantiate(idGenerator);
            dao.setSessionIdGenerator(sessionIdGenerator);
        }
        dao.setCacheManager(cacheManager);

        return dao;
    }

    @Bean(name = "sessionValidationScheduler")
    @DependsOn(value = {"sessionManager"})
    @ConditionalOnClass(Scheduler.class)
    @ConditionalOnMissingBean(SessionValidationScheduler.class)
    public SessionValidationScheduler quartzSessionValidationScheduler(DefaultWebSessionManager sessionManager) {
        QuartzSessionValidationScheduler quartzSessionValidationScheduler = new QuartzSessionValidationScheduler(sessionManager);
        sessionManager.setDeleteInvalidSessions(shiroSessionProperties.isDeleteInvalidSessions());
        sessionManager.setSessionValidationInterval(shiroSessionProperties.getValidationInterval());
        sessionManager.setSessionValidationSchedulerEnabled(shiroSessionProperties.isValidationSchedulerEnabled());
        sessionManager.setSessionValidationScheduler(quartzSessionValidationScheduler);

        return quartzSessionValidationScheduler;
    }

    @Bean(name = "sessionValidationScheduler")
    @DependsOn(value = {"sessionManager"})
    @ConditionalOnMissingBean(SessionValidationScheduler.class)
    public SessionValidationScheduler sessionValidationScheduler(DefaultWebSessionManager sessionManager) {
        ExecutorServiceSessionValidationScheduler scheduler = new ExecutorServiceSessionValidationScheduler(sessionManager);
        sessionManager.setDeleteInvalidSessions(shiroSessionProperties.isDeleteInvalidSessions());
        sessionManager.setSessionValidationInterval(shiroSessionProperties.getValidationInterval());
        sessionManager.setSessionValidationSchedulerEnabled(shiroSessionProperties.isValidationSchedulerEnabled());
        sessionManager.setSessionValidationScheduler(scheduler);

        return scheduler;
    }

    @Bean
    @DependsOn(value = {"cacheManager", "sessionDAO"})
    public WebSessionManager sessionManager(CacheManager cacheManager, SessionDAO sessionDAO) {
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        sessionManager.setCacheManager(cacheManager);
        sessionManager.setGlobalSessionTimeout(shiroSessionProperties.getGlobalSessionTimeout());

        sessionManager.setSessionDAO(sessionDAO);
        sessionManager.setSessionListeners(listeners);

        return sessionManager;
    }

    @Bean(name = "credentialsMatcher")
    @ConditionalOnMissingBean
    @DependsOn("cacheManager")
    public CredentialsMatcher credentialsMatcher(CacheManager cacheManager) {
        RetryLimitMatcher credentialsMatcher = new RetryLimitMatcher(cacheManager);
        credentialsMatcher.setMaxRetry(shiroProperties.getMaxRetry());

        return credentialsMatcher;
    }

    public AuthFilter authFilter() {
        Class<?> authClass = shiroProperties.getAuthClass();
        AuthFilter filter = (AuthFilter) BeanUtils.instantiate(authClass);
        filter.setLoginUrl(shiroProperties.getLoginUrl());
        filter.setSuccessUrl(shiroProperties.getSuccessUrl());
        filter.setUsernameParam(authFilterProperties.getUserParamName());
        filter.setPasswordParam(authFilterProperties.getPasswordParamName());
        filter.setRememberMeParam(authFilterProperties.getRememberMeParamName());
        filter.setDataParamName(authFilterProperties.getDataParamName());

        return filter;
    }

    public ShiroFilterFactoryBean getShiroFilterFactoryBean(SecurityManager securityManager) throws Exception {
        ShiroFilterFactoryBean shiroFilter = new ShiroFilterFactoryBean();
        shiroFilter.setSecurityManager(securityManager);
        shiroFilter.setLoginUrl(shiroProperties.getLoginUrl());
        shiroFilter.setSuccessUrl(shiroProperties.getSuccessUrl());
        shiroFilter.setUnauthorizedUrl(shiroProperties.getUnauthorizedUrl());

        Map<String, Filter> filterMap = new LinkedHashMap<String, Filter>();
        filterMap.put("authm", authFilter());

        Map<String, Filter> filterClasses = instantiateFilterClasses(shiroProperties.getFilters());
        if (filterClasses != null) {
            filterMap.putAll(filterClasses);
        }

        if (shiroFilterCustomizer != null) {
            filterMap = shiroFilterCustomizer.customize(filterMap);
        }

        shiroFilter.setFilters(filterMap);

        Map<String, String> filterChains = new LinkedHashMap<String, String>();
        if (jdbcPermissionDefinitionsLoader != null) {
            Map<String, String> permissionUrlMap = jdbcPermissionDefinitionsLoader.getObject();
            filterChains.putAll(permissionUrlMap);
        }
        if (shiroProperties.getFilterChainDefinitions() != null) {
            filterChains.putAll(shiroProperties.getFilterChainDefinitions());
        }
        shiroFilter.setFilterChainDefinitionMap(filterChains);

        return shiroFilter;
    }

    private Map<String, Filter> instantiateFilterClasses(Map<String, Class<? extends Filter>> filters) {
        Map<String, Filter> filterMap = null;
        if (filters != null) {
            filterMap = new LinkedHashMap<String, Filter>();
            for (String name : filters.keySet()) {
                Class<? extends Filter> clazz = filters.get(name);
                Filter f = BeanUtils.instantiate(clazz);
                filterMap.put(name, f);
            }
        }

        return filterMap;
    }

    @Bean(name = "shiroFilter")
    @DependsOn("securityManager")
    @ConditionalOnMissingBean
    public FilterRegistrationBean filterRegistrationBean(SecurityManager securityManager) throws Exception {
        FilterRegistrationBean filterRegistration = new FilterRegistrationBean();
        //该值缺省为false,表示生命周期由SpringApplicationContext管理,设置为true则表示由ServletContainer管理
        filterRegistration.addInitParameter("targetFilterLifecycle", "true");
        filterRegistration.setFilter((Filter) getShiroFilterFactoryBean(securityManager).getObject());
        filterRegistration.setEnabled(true);
        filterRegistration.addUrlPatterns("/*");

        return filterRegistration;
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "shiro", name = "filterChainSql")
    public JdbcPermissionDefinitionsLoader jdbcFilterChainsLoader(DataSource dataSource) {
        JdbcPermissionDefinitionsLoader jdbcPermissionDefinitionsLoader = new JdbcPermissionDefinitionsLoader(dataSource);
        jdbcPermissionDefinitionsLoader.setSql(shiroProperties.getFilterChainSql());

        return jdbcPermissionDefinitionsLoader;
    }

    @Bean
    @ConditionalOnMissingBean
    public PasswordService passwordService() {
        DefaultPasswordService service = new DefaultPasswordService();

        DefaultHashService hashService = new DefaultHashService();
        hashService.setHashAlgorithmName(shiroProperties.getHashAlgorithmName());
        hashService.setHashIterations(shiroProperties.getHashIterations());
        service.setHashService(hashService);

        return service;
    }
}
