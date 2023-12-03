package pl.edu.pw.mwoproj;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.TestInstance;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class HomeAndNavigationTest {
    @LocalServerPort
    private int port;
    private WebDriver driver;

    @BeforeAll
    public void setup() {
        ChromeOptions opt = new ChromeOptions();
        opt.addArguments("--no-sandbox");
        opt.addArguments("--disable-dev-shm-usage");
        opt.addArguments("--headless");
        opt.addArguments("start-maximized");
        driver = new ChromeDriver(opt);
    }

    @AfterAll
    public void cleanup() {
        driver.close();
    }

    @Test
    public void HomeLinkTest() {
        driver.get("http://localhost:"+port+"/");

        Assertions.assertThat(driver.getTitle()).isEqualTo("MVC App");

        driver.findElement(By.className("home-link")).click();

        Assertions.assertThat(driver.getTitle()).isEqualTo("MVC App");
    }

    @Test
    public void BookLinkTest() {
        driver.get("http://localhost:"+port+"/");

        Assertions.assertThat(driver.getTitle()).isEqualTo("MVC App");

        driver.findElement(By.className("book-link")).click();

        Assertions.assertThat(driver.getTitle()).isEqualTo("MVC App - Books");
    }

    @Test
    public void AuthorLinkTest() {
        driver.get("http://localhost:"+port+"/");

        Assertions.assertThat(driver.getTitle()).isEqualTo("MVC App");

        driver.findElement(By.className("author-link")).click();

        Assertions.assertThat(driver.getTitle()).isEqualTo("MVC App - Authors");
    }

    @Test
    public void PublisherLinkTest() {
        driver.get("http://localhost:"+port+"/");

        Assertions.assertThat(driver.getTitle()).isEqualTo("MVC App");

        driver.findElement(By.className("publisher-link")).click();

        Assertions.assertThat(driver.getTitle()).isEqualTo("MVC App - Publishers");
    }

    @Test
    public void HomeDescriptionTest() {
        driver.get("http://localhost:"+port+"/");

        Assertions.assertThat(driver.getTitle()).isEqualTo("MVC App");

        var desc = driver.findElement(By.className("home-description")).getText();

        Assertions.assertThat(desc).isEqualTo("Home page of the MVC app");
    }
}
