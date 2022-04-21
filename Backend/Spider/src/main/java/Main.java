import java.net.MalformedURLException;
import java.text.ParseException;
import java.time.Duration;
import java.util.Map;
import java.util.Set;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Main {
    public static void main(String[] args) throws InterruptedException, ParseException, MalformedURLException {
        // args
        // 0: REDIS_HOST, 1: REDIS_PORT
        // 2: MYSQL_HOST, 3: MYSQL_PORT,
        // 4: MYSQL_DATABASE, 5: MYSQL_USER, 6: MYSQL_PASSWORD
        System.out.println("Spider start");

        // read env variables
        Map<String, String> env = System.getenv();

        // init selenium
        Selenium selenium = Selenium.getInstance();
        selenium.options.setHeadless(true);
        selenium.initDriver(env.getOrDefault("WEBDRIVER_REMOTEADDRESS", "http://localhost:4444"));

        // init redis
        Redis redis = Redis.getInstance();
        redis.initDriver(args.length > 0 ? args[0] : env.get("REDIS_HOST"),
                args.length > 1 ? args[1] : env.get("REDIS_PORT"));

        // get active matches
        ActiveMatches active_matches = new ActiveMatches(redis.driver);
        active_matches.load(getJsonArrayContent(selenium.driver,
                "https://bet.hkjc.com/football/getJSON.aspx?jsontype=odds_allodds.aspx&matchid=default"));
        active_matches.cache();

        // get match detail
        for (String match_id : active_matches.data_set) {
            // sleep for 500ms each loop
            Thread.sleep(500);

            MatchDetail match_detail = new MatchDetail(redis.driver, match_id);
            match_detail.load(getJsonArrayContent(selenium.driver,
                    "https://bet.hkjc.com/football/getJSON.aspx?jsontype=odds_allodds.aspx&matchid=" + match_id));
            match_detail.cache();
        }

        // filter expired matches and archive to database
        Set<String> closed_matches = active_matches.filterClosedMatches();
        if (closed_matches.size() > 0) {
            // init hibernate
            Hibernate hibernate = Hibernate.getInstance();
            hibernate.initSession(
                    args.length > 2 ? args[2] : env.get("MYSQL_HOST"),
                    args.length > 3 ? args[3] : env.get("MYSQL_PORT"),
                    args.length > 4 ? args[4] : env.get("MYSQL_DATABASE"),
                    args.length > 5 ? args[5] : env.get("MYSQL_USER"),
                    args.length > 6 ? args[6] : env.get("MYSQL_PASSWORD"));
            // save to mysql
            for (String match_id : closed_matches) {
                ArchiveMatch archive_match = new ArchiveMatch(hibernate, redis.driver, match_id);
                archive_match.load();
                archive_match.save();
            }
            hibernate.endSession();
            // remove from redis
            for (String match_id : closed_matches) {
                redis.driver.del(match_id);
            }
        }
        redis.driver.close();
        selenium.driver.quit();
        System.out.println("Spider end");
    }

    private static JsonArray getJsonArrayContent(WebDriver driver, String target_url) {
        driver.get(target_url);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("pre")));
        String text = element.getText();
        return JsonParser.parseString(text).getAsJsonArray();
    }
}
