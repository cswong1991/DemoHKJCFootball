import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import redis.clients.jedis.Jedis;

public class MatchDetail {
    private final Set<String> include_odds = new HashSet<String>(
            Arrays.asList("hadodds", "fhaodds", "hhaodds", "hdcodds", "hilodds", "fhlodds", "chlodds", "ftsodds",
                    "ttgodds", "ooeodds", "hftodds"));
    private Jedis redis_driver;
    private String match_id;
    private JsonArray data_jsonarray;

    public MatchDetail(Jedis redis_driver, String match_id) {
        this.redis_driver = redis_driver;
        this.match_id = match_id;
    }

    public void load(JsonArray data_jsonarray) {
        this.data_jsonarray = data_jsonarray;
    }

    public void cache() {
        for (JsonElement data_jsonelement : data_jsonarray) {
            // data_jsonelement = [{matchdata}...], element_jsonobject = matchdata
            JsonObject matchdata_jsonobject = data_jsonelement.getAsJsonObject();
            // filter unwanted data, matchID != required match_id
            if (!match_id.equals(matchdata_jsonobject.get("matchID").getAsString())) {
                continue;
            }

            Map<String, String> matchdata_map = new HashMap<String, String>();
            Integer new_oddsdata_id;
            try {
                new_oddsdata_id = redis_driver.hkeys(match_id).stream()
                        .filter(e1 -> e1.contains("oddsData"))
                        .map(e1 -> Integer.parseInt(e1.replace("oddsData", ""))).max(Integer::compare).get() + 1;
            } catch (Exception e) {
                new_oddsdata_id = 1;
            }
            Map<Object, Object> odds_data = matchdata_jsonobject.entrySet().stream()
                    .filter(e1 -> include_odds.contains(e1.getKey()))
                    .collect(Collectors.toMap(Entry::getKey, Entry::getValue));
            odds_data.put("id", new_oddsdata_id);
            odds_data.put("updated_at", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            odds_data.put("match_status", matchdata_jsonobject.get("matchStatus").getAsString());

            Gson gson = new Gson();
            matchdata_map.put("matchInfo",
                    gson.toJson(matchdata_jsonobject.entrySet().stream()
                            .filter(e1 -> !e1.getKey().contains("odds"))
                            .collect(Collectors.toMap(Entry::getKey, Entry::getValue))));
            matchdata_map.put("oddsData" + new_oddsdata_id.toString(), gson.toJson(odds_data));
            redis_driver.hset(match_id, matchdata_map);
            break;
        }
    }
}
