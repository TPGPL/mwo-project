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
import org.openqa.selenium.firefox.FirefoxOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import pl.edu.pw.mwoproj.models.Author;
import pl.edu.pw.mwoproj.repositories.AuthorRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AuthorServiceIntegrationTest {
    @LocalServerPort
    private int port;
    private WebDriver driver;
    @Autowired
    private AuthorRepository repository;

    @BeforeAll
    public void setup() {
        ChromeOptions opt = new ChromeOptions();
        opt.addArguments("--no-sandbox");
        opt.addArguments("--disable-dev-shm-usage");
        opt.addArguments("--headless");
        driver = new ChromeDriver(opt);
    }

    @AfterAll
    public void cleanup() {
        driver.close();
    }
    @Test
    public void emptyListTest() {
        driver.get("http://localhost:"+port+"/authors");

        assertThat(driver.findElement(By.className("no-content")).getText()).isEqualTo("No data available.");
    }

    @Test
    public void createAuthorSuccessTest() {
        driver.get("http://localhost:"+port+"/authors");
        driver.findElement(By.className("author-create-button")).click();

        assertThat(driver.getTitle()).isEqualTo("MVC App - Create author");

        driver.findElement(By.className("name-input")).sendKeys("NameTest");
        driver.findElement(By.className("surname-input")).sendKeys("SurnameTest");
        driver.findElement(By.className("email-input")).sendKeys("email@test.com");

        driver.findElement(By.className("author-create-button")).click();

        assertThat(driver.getTitle()).isEqualTo("MVC App - Authors");
        assertThat(driver.findElement(By.id("authorId-1")).getText()).isEqualTo("1");
        assertThat(driver.findElement(By.id("authorName-1")).getText()).isEqualTo("NameTest");
        assertThat(driver.findElement(By.id("authorSurname-1")).getText()).isEqualTo("SurnameTest");
        assertThat(driver.findElement(By.id("authorMail-1")).getText()).isEqualTo("email@test.com");
    }

    @Test
    public void createAuthorNotSuccessTest() {
        driver.get("http://localhost:"+port+"/authors");
        driver.findElement(By.className("author-create-button")).click();

        assertThat(driver.getTitle()).isEqualTo("MVC App - Create author");

        driver.findElement(By.className("name-input")).sendKeys("");
        driver.findElement(By.className("surname-input")).sendKeys("SurnameTest");
        driver.findElement(By.className("email-input")).sendKeys("email@test.com");

        driver.findElement(By.className("author-create-button")).click();

        assertThat(driver.getTitle()).isEqualTo("MVC App - Authors");
        assertThat(driver.findElement(By.className("no-content")).getText()).isEqualTo("No data available.");
    }

    @Test
    public void updateAuthorSuccessTest() {
        repository.save(Author.builder().name("OldName").surname("OldSurname").email("old@mail.com").build());
        driver.get("http://localhost:"+port+"/authors");

        assertThat(driver.findElement(By.id("authorId-1")).getText()).isEqualTo("1");
        assertThat(driver.findElement(By.id("authorName-1")).getText()).isEqualTo("OldName");
        assertThat(driver.findElement(By.id("authorSurname-1")).getText()).isEqualTo("OldSurname");
        assertThat(driver.findElement(By.id("authorMail-1")).getText()).isEqualTo("old@mail.com");

        driver.findElement(By.id("authorUpdate-1")).click();

        assertThat(driver.getTitle()).isEqualTo("MVC App - Update author");

        driver.findElement(By.className("name-input")).clear();
        driver.findElement(By.className("surname-input")).clear();
        driver.findElement(By.className("email-input")).clear();

        driver.findElement(By.className("name-input")).sendKeys("NewName");
        driver.findElement(By.className("surname-input")).sendKeys("NewSurname");
        driver.findElement(By.className("email-input")).sendKeys("new@mail.com");

        driver.findElement(By.className("author-update-button")).click();

        assertThat(driver.getTitle()).isEqualTo("MVC App - Authors");
        assertThat(driver.findElement(By.id("authorId-1")).getText()).isEqualTo("1");
        assertThat(driver.findElement(By.id("authorName-1")).getText()).isEqualTo("NewName");
        assertThat(driver.findElement(By.id("authorSurname-1")).getText()).isEqualTo("NewSurname");
        assertThat(driver.findElement(By.id("authorMail-1")).getText()).isEqualTo("new@mail.com");
    }

    @Test
    public void updateAuthorNotSuccessTest() {
        repository.save(Author.builder().name("OldName").surname("OldSurname").email("old@mail.com").build());
        driver.get("http://localhost:"+port+"/authors");

        assertThat(driver.findElement(By.id("authorId-1")).getText()).isEqualTo("1");
        assertThat(driver.findElement(By.id("authorName-1")).getText()).isEqualTo("OldName");
        assertThat(driver.findElement(By.id("authorSurname-1")).getText()).isEqualTo("OldSurname");
        assertThat(driver.findElement(By.id("authorMail-1")).getText()).isEqualTo("old@mail.com");

        driver.findElement(By.id("authorUpdate-1")).click();

        assertThat(driver.getTitle()).isEqualTo("MVC App - Update author");

        driver.findElement(By.className("name-input")).clear();
        driver.findElement(By.className("surname-input")).clear();
        driver.findElement(By.className("email-input")).clear();

        driver.findElement(By.className("name-input")).sendKeys("a");
        driver.findElement(By.className("surname-input")).sendKeys("NewSurname");
        driver.findElement(By.className("email-input")).sendKeys("new@mail.com");

        driver.findElement(By.className("author-update-button")).click();

        assertThat(driver.getTitle()).isEqualTo("MVC App - Authors");
        assertThat(driver.findElement(By.id("authorId-1")).getText()).isEqualTo("1");
        assertThat(driver.findElement(By.id("authorName-1")).getText()).isEqualTo("OldName");
        assertThat(driver.findElement(By.id("authorSurname-1")).getText()).isEqualTo("OldSurname");
        assertThat(driver.findElement(By.id("authorMail-1")).getText()).isEqualTo("old@mail.com");
    }

    @Test
    public void deleteAuthorSingleListTest() {
        repository.save(Author.builder().name("Name1").surname("Surname").email("aut@email.com").build());
        driver.get("http://localhost:"+port+"/authors");

        driver.findElement(By.id("authorDelete-1")).click();
        assertThat(driver.findElement(By.className("no-content")).getText()).isEqualTo("No data available.");
    }

    @Test
    public void deleteAuthorMultipleListTest() {
        repository.save(Author.builder().name("Name1").surname("Surname").email("aut@email.com").build());
        repository.save(Author.builder().name("Name2").surname("Surname2").email("aut2@email.com").build());
        driver.get("http://localhost:"+port+"/authors");

        driver.findElement(By.id("authorDelete-1")).click();

        assertThatThrownBy(() -> driver.findElement(By.id("authorId-1"))).isInstanceOf(NoSuchElementException.class);

        assertThat(driver.findElement(By.id("authorId-2")).getText()).isEqualTo("2");
        assertThat(driver.findElement(By.id("authorName-2")).getText()).isEqualTo("Name2");
        assertThat(driver.findElement(By.id("authorSurname-2")).getText()).isEqualTo("Surname2");
        assertThat(driver.findElement(By.id("authorMail-2")).getText()).isEqualTo("aut2@email.com");
    }

    @Test
    public void emptyUpdatePageTest() {
        driver.get("http://localhost:"+port+"/authors/update/10000");
        assertThat(driver.findElement(By.className("no-content")).getText()).isEqualTo("Failed to get author with this ID.");
    }

    @Test
    public void readAuthorTest() {
        repository.save(Author.builder().name("Name").surname("Surname").email("author@mail.com").build());
        driver.get("http://localhost:"+port+"/authors");

        assertThat(driver.findElement(By.id("authorId-1")).getText()).isEqualTo("1");
        assertThat(driver.findElement(By.id("authorName-1")).getText()).isEqualTo("Name");
        assertThat(driver.findElement(By.id("authorSurname-1")).getText()).isEqualTo("Surname");
        assertThat(driver.findElement(By.id("authorMail-1")).getText()).isEqualTo("author@mail.com");
    }
}
