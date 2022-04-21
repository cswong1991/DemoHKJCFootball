import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import redis.clients.jedis.Jedis;

public class ActiveMatches {
    private Jedis redis_driver;
    private JsonArray data_jsonarray;
    public Set<String> data_set = new HashSet<String>();

    public ActiveMatches(Jedis redis_driver) {
        this.redis_driver = redis_driver;
    }

    public void load(JsonArray data_jsonarray) {
        this.data_jsonarray = data_jsonarray;
    }

    public void cache() {
        data_set.clear();
        redis_driver.del("active_matches");
        for (JsonElement data_jsonelement : data_jsonarray) {
            String match_id = data_jsonelement.getAsJsonObject().get("matchID").getAsString();
            data_set.add(match_id);
            redis_driver.rpush("active_matches", match_id);
        }
    }

    public Set<String> filterClosedMatches() {
        Set<String> cached_matches = redis_driver.keys("*");
        return cached_matches.stream()
                .filter(e1 -> !e1.equals("active_matches") && !data_set.contains(e1)).collect(Collectors.toSet());
    }
}
