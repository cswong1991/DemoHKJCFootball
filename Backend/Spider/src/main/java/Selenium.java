import java.net.MalformedURLException;
import java.net.URL;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import io.github.bonigarcia.wdm.WebDriverManager;

public class Selenium {
    private static Selenium instance;
    public ChromeOptions options;
    public WebDriver driver;

    private Selenium() {
        options = new ChromeOptions();
    }

    public static Selenium getInstance() {
        if (instance == null) {
            synchronized (Selenium.class) {
                if (instance == null) {
                    instance = new Selenium();
                }
            }
        }
        return instance;
    }

    public void initDriver(String remote_address) throws MalformedURLException {
        WebDriverManager.chromedriver().setup();
        driver = new RemoteWebDriver(new URL(remote_address), options);
    }
}
