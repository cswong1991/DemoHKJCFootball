import redis.clients.jedis.Jedis;

public class Redis {
    private static Redis instance;
    public Jedis driver;

    private Redis() {
    }

    public static Redis getInstance() {
        if (instance == null) {
            synchronized (Redis.class) {
                if (instance == null) {
                    instance = new Redis();
                }
            }
        }
        return instance;
    }

    public void initDriver(String host, String port) {
        driver = new Jedis(host, Integer.parseInt(port));
    }
}