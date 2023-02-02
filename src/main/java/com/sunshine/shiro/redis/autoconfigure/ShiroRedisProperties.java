package com.sunshine.shiro.redis.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;
import java.util.List;

/**
 * @author Teamo
 * @since 2021/05/24
 */
@ConfigurationProperties(prefix = "shiro.redis")
public class ShiroRedisProperties {
    /**
     * 是否启用Shiro Redis
     */
    private Boolean enable = true;

    /**
     * Redis服务主机
     */
    private String host = "localhost";

    /**
     * Redis数据库
     */
    private int database = 0;

    /**
     * redis服务器的登录密码
     */
    private String password;

    /**
     * Redis 服务器端口
     */
    private int port = 6379;

    /**
     * 读取超时时间
     */
    private Duration timeout;

    /**
     * 连接超时时间
     */
    private Duration connectTimeout;

    /**
     * 客户端类型
     */
    private ClientType clientType;

    /**
     * Shiro Redis会话配置
     */
    private Session session = new Session();

    /**
     * Shiro Redis缓存配置
     */
    private Cache cache = new Cache();

    /**
     * 每次迭代返回的元素数
     */
    private int count = 100;

    /**
     * 哨兵模式
     */
    private Sentinel sentinel;

    /**
     * 主从模式
     */
    private Cluster cluster;

    /**
     * Lettuce配置
     */
    private Lettuce lettuce = new Lettuce();

    /**
     * Jedis配置
     */
    private Jedis jedis = new Jedis();

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getDatabase() {
        return database;
    }

    public void setDatabase(int database) {
        this.database = database;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public Duration getTimeout() {
        return timeout;
    }

    public void setTimeout(Duration timeout) {
        this.timeout = timeout;
    }

    public Duration getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(Duration connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public ClientType getClientType() {
        return clientType;
    }

    public void setClientType(ClientType clientType) {
        this.clientType = clientType;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public Cache getCache() {
        return cache;
    }

    public void setCache(Cache cache) {
        this.cache = cache;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Sentinel getSentinel() {
        return sentinel;
    }

    public void setSentinel(Sentinel sentinel) {
        this.sentinel = sentinel;
    }

    public Cluster getCluster() {
        return cluster;
    }

    public void setCluster(Cluster cluster) {
        this.cluster = cluster;
    }

    public Lettuce getLettuce() {
        return lettuce;
    }

    public void setLettuce(Lettuce lettuce) {
        this.lettuce = lettuce;
    }

    public Jedis getJedis() {
        return jedis;
    }

    public void setJedis(Jedis jedis) {
        this.jedis = jedis;
    }

    /**
     * Redis客户端类型
     */
    public enum ClientType {

        /**
         * 使用Lettuce客户端
         */
        LETTUCE,

        /**
         * 使用Jedis客户端
         */
        JEDIS

    }

    /**
     * 主从属性
     */
    public static class Cluster {

        /**
         * 节点列表，格式为"host:port"，多个以逗号分隔
         */
        private List<String> nodes;

        /**
         * 最大集群重定向的个数
         */
        private Integer maxRedirects;

        public List<String> getNodes() {
            return this.nodes;
        }

        public void setNodes(List<String> nodes) {
            this.nodes = nodes;
        }

        public Integer getMaxRedirects() {
            return maxRedirects;
        }

        public void setMaxRedirects(Integer maxRedirects) {
            this.maxRedirects = maxRedirects;
        }
    }

    /**
     * 哨兵属性
     */
    public static class Sentinel {

        /**
         * 主名称
         */
        private String master;

        /**
         * “host:port”，逗号分隔列表。
         */
        private List<String> nodes;

        /**
         * 用于与哨兵进行身份验证的密码。
         */
        private String password;

        /**
         * Lettuce读取模式
         */
        private ReadFromType readFromType = ReadFromType.REPLICA_PREFERRED;

        /**
         * @see io.lettuce.core.ReadFrom
         */
        public enum ReadFromType {
            MASTER,
            MASTER_PREFERRED,
            UPSTREAM,
            UPSTREAM_PREFERRED,
            REPLICA_PREFERRED,
            REPLICA,
            LOWEST_LATENCY,
            ANY,
            ANY_REPLICA
        }

        public String getMaster() {
            return this.master;
        }

        public void setMaster(String master) {
            this.master = master;
        }

        public List<String> getNodes() {
            return this.nodes;
        }

        public void setNodes(List<String> nodes) {
            this.nodes = nodes;
        }

        public String getPassword() {
            return this.password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public ReadFromType getReadFromType() {
            return readFromType;
        }

        public void setReadFromType(ReadFromType readFromType) {
            this.readFromType = readFromType;
        }
    }

    /**
     * Lettuce属性
     */
    public static class Lettuce {

        /**
         * 是否开启异步，默认开启
         */
        private Boolean async = Boolean.TRUE;

        /**
         * Lettuce连接池配置
         */
        private final Pool pool = new Pool();

        public Boolean getAsync() {
            return async;
        }

        public void setAsync(Boolean async) {
            this.async = async;
        }

        public Pool getPool() {
            return pool;
        }
    }

    /**
     * Jedis属性
     */
    public static class Jedis {

        /**
         * Jedis连接池配置
         */
        private final Pool pool = new Pool();

        public Pool getPool() {
            return this.pool;
        }
    }

    /**
     * 连接池，使用commons-pool2
     */
    public static class Pool {
        /**
         * 最大空闲连接数
         */
        private int maxIdle = 8;

        /**
         * 最小空闲连接数
         */
        private int minIdle = 0;

        /**
         * 最大连接数
         */
        private int maxActive = 8;

        /**
         * 最大等待时间，单位毫秒
         */
        private Duration maxWait = Duration.ofMillis(-1);

        /**
         * 空闲对象驱逐时间
         */
        private Duration timeBetweenEvictionRuns;

        public int getMaxIdle() {
            return this.maxIdle;
        }

        public void setMaxIdle(int maxIdle) {
            this.maxIdle = maxIdle;
        }

        public int getMinIdle() {
            return this.minIdle;
        }

        public void setMinIdle(int minIdle) {
            this.minIdle = minIdle;
        }

        public int getMaxActive() {
            return this.maxActive;
        }

        public void setMaxActive(int maxActive) {
            this.maxActive = maxActive;
        }

        public Duration getMaxWait() {
            return this.maxWait;
        }

        public void setMaxWait(Duration maxWait) {
            this.maxWait = maxWait;
        }

        public Duration getTimeBetweenEvictionRuns() {
            return this.timeBetweenEvictionRuns;
        }

        public void setTimeBetweenEvictionRuns(Duration timeBetweenEvictionRuns) {
            this.timeBetweenEvictionRuns = timeBetweenEvictionRuns;
        }
    }

    public static class Session {
        /**
         * 是否启用Redis Session
         */
        private Boolean enable = Boolean.TRUE;

        /**
         * 会话缓存前缀
         */
        private String keyPrefix;

        /**
         * Redis缓存键/值过期时间。过期时间以秒为单位。特殊值：1：没有到期，-2：与session相同的超时时间。注意：请确保过期时间大于会话超时时间。
         */
        private Duration expire = Duration.ofSeconds(-2L);

        /**
         * 是否在ThreadLocal中启用临时session
         */
        private Boolean sessionInMemoryEnabled = Boolean.TRUE;

        /**
         * session在ThreadLocal中的过期时间
         */
        private Duration sessionInMemoryTimeout = Duration.ofMillis(1000L);

        public Boolean getEnable() {
            return enable;
        }

        public void setEnable(Boolean enable) {
            this.enable = enable;
        }

        public String getKeyPrefix() {
            return keyPrefix;
        }

        public void setKeyPrefix(String keyPrefix) {
            this.keyPrefix = keyPrefix;
        }

        public Duration getExpire() {
            return expire;
        }

        public void setExpire(Duration expire) {
            this.expire = expire;
        }

        public Boolean getSessionInMemoryEnabled() {
            return sessionInMemoryEnabled;
        }

        public void setSessionInMemoryEnabled(Boolean sessionInMemoryEnabled) {
            this.sessionInMemoryEnabled = sessionInMemoryEnabled;
        }

        public Duration getSessionInMemoryTimeout() {
            return sessionInMemoryTimeout;
        }

        public void setSessionInMemoryTimeout(Duration sessionInMemoryTimeout) {
            this.sessionInMemoryTimeout = sessionInMemoryTimeout;
        }
    }

    public static class Cache {
        /**
         * 主体id字段名称
         */
        private String principalIdFieldName = "id";

        /**
         * 缓存前缀
         */
        private String keyPrefix;

        /**
         * 过期时间
         */
        private Duration expire = Duration.ofSeconds(1800L);

        public String getPrincipalIdFieldName() {
            return principalIdFieldName;
        }

        public void setPrincipalIdFieldName(String principalIdFieldName) {
            this.principalIdFieldName = principalIdFieldName;
        }

        public String getKeyPrefix() {
            return keyPrefix;
        }

        public void setKeyPrefix(String keyPrefix) {
            this.keyPrefix = keyPrefix;
        }

        public Duration getExpire() {
            return expire;
        }

        public void setExpire(Duration expire) {
            this.expire = expire;
        }
    }
}
