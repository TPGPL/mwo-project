package pl.edu.pw.mwoproj;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import pl.edu.pw.mwoproj.models.Publisher;
import pl.edu.pw.mwoproj.repositories.PublisherRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PublisherServiceIntegrationTest {
    @LocalServerPort
    private int port;
    private WebDriver driver;
    @Autowired
    private PublisherRepository repository;

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
    public void emptyListTest() {
        driver.get("http://localhost:"+port+"/publishers");

        assertThat(driver.findElement(By.className("no-content")).getText()).isEqualTo("No data available.");
    }

    @Test
    public void createPublisherSuccessTest() {
        driver.get("http://localhost:"+port+"/publishers");
        driver.findElement(By.className("publisher-create-button")).click();

        assertThat(driver.getTitle()).isEqualTo("MVC App - Create publisher");

        driver.findElement(By.className("name-input")).sendKeys("NameTest");

        driver.findElement(By.className("publisher-create-button")).click();

        assertThat(driver.getTitle()).isEqualTo("MVC App - Publishers");
        assertThat(driver.findElement(By.id("publisherId-1")).getText()).isEqualTo("1");
        assertThat(driver.findElement(By.id("publisherName-1")).getText()).isEqualTo("NameTest");
    }

    @Test
    public void createPublisherNotSuccessTest() {
        driver.get("http://localhost:"+port+"/publishers");
        driver.findElement(By.className("publisher-create-button")).click();

        assertThat(driver.getTitle()).isEqualTo("MVC App - Create publisher");

        driver.findElement(By.className("name-input")).sendKeys("a");

        driver.findElement(By.className("publisher-create-button")).click();

        assertThat(driver.getTitle()).isEqualTo("MVC App - Publishers");
        assertThat(driver.findElement(By.className("no-content")).getText()).isEqualTo("No data available.");
    }

    @Test
    public void updatePublisherSuccessTest() {
        repository.save(Publisher.builder().name("OldName").build());
        driver.get("http://localhost:"+port+"/publishers");

        assertThat(driver.findElement(By.id("publisherId-1")).getText()).isEqualTo("1");
        assertThat(driver.findElement(By.id("publisherName-1")).getText()).isEqualTo("OldName");

        driver.findElement(By.id("publisherUpdate-1")).click();

        assertThat(driver.getTitle()).isEqualTo("MVC App - Update publisher");

        driver.findElement(By.className("name-input")).clear();
        driver.findElement(By.className("name-input")).sendKeys("NewName");
        driver.findElement(By.className("publisher-update-button")).click();

        assertThat(driver.getTitle()).isEqualTo("MVC App - Publishers");
        assertThat(driver.findElement(By.id("publisherId-1")).getText()).isEqualTo("1");
        assertThat(driver.findElement(By.id("publisherName-1")).getText()).isEqualTo("NewName");
    }

    @Test
    public void updatePublisherNotSuccessTest() {
        repository.save(Publisher.builder().name("OldName").build());
        driver.get("http://localhost:"+port+"/publishers");

        assertThat(driver.findElement(By.id("publisherId-1")).getText()).isEqualTo("1");
        assertThat(driver.findElement(By.id("publisherName-1")).getText()).isEqualTo("OldName");

        driver.findElement(By.id("publisherUpdate-1")).click();

        assertThat(driver.getTitle()).isEqualTo("MVC App - Update publisher");

        driver.findElement(By.className("name-input")).clear();

        driver.findElement(By.className("name-input")).sendKeys("a");

        driver.findElement(By.className("publisher-update-button")).click();

        assertThat(driver.getTitle()).isEqualTo("MVC App - Publishers");
        assertThat(driver.findElement(By.id("publisherId-1")).getText()).isEqualTo("1");
        assertThat(driver.findElement(By.id("publisherName-1")).getText()).isEqualTo("OldName");
    }

    @Test
    public void deletePublisherSingleListTest() {
        repository.save(Publisher.builder().name("Name1").build());
        driver.get("http://localhost:"+port+"/publishers");

        driver.findElement(By.id("publisherDelete-1")).click();
        assertThat(driver.findElement(By.className("no-content")).getText()).isEqualTo("No data available.");
    }

    @Test
    public void deletePublisherMultipleListTest() {
        repository.save(Publisher.builder().name("Name1").build());
        repository.save(Publisher.builder().name("Name2").build());
        driver.get("http://localhost:"+port+"/publishers");

        driver.findElement(By.id("publisherDelete-1")).click();

        assertThatThrownBy(() -> driver.findElement(By.id("publisherId-1"))).isInstanceOf(NoSuchElementException.class);

        assertThat(driver.findElement(By.id("publisherId-2")).getText()).isEqualTo("2");
        assertThat(driver.findElement(By.id("publisherName-2")).getText()).isEqualTo("Name2");
    }

    @Test
    public void emptyUpdatePageTest() {
        driver.get("http://localhost:"+port+"/publishers/update/10000");
        assertThat(driver.findElement(By.className("no-content")).getText()).isEqualTo("Failed to get publisher with this ID.");
    }

    @Test
    public void readPublisherTest() {
        repository.save(Publisher.builder().name("Name").build());
        driver.get("http://localhost:"+port+"/publishers");

        assertThat(driver.findElement(By.id("publisherId-1")).getText()).isEqualTo("1");
        assertThat(driver.findElement(By.id("publisherName-1")).getText()).isEqualTo("Name");
    }
}
