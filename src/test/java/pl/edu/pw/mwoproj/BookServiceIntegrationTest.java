package pl.edu.pw.mwoproj;

import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import pl.edu.pw.mwoproj.models.Author;
import pl.edu.pw.mwoproj.models.Book;
import pl.edu.pw.mwoproj.models.Publisher;
import pl.edu.pw.mwoproj.repositories.AuthorRepository;
import pl.edu.pw.mwoproj.repositories.BookRepository;
import pl.edu.pw.mwoproj.repositories.PublisherRepository;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BookServiceIntegrationTest {
    @LocalServerPort
    private int port;
    private WebDriver driver;
    @Autowired
    private BookRepository repository;
    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private PublisherRepository publisherRepository;
    private Author savedAuthor;
    private Publisher savedPublisher;

    @BeforeAll
    public void setup() {
        ChromeOptions opt = new ChromeOptions();
        opt.addArguments("--no-sandbox");
        opt.addArguments("--disable-dev-shm-usage");
        opt.addArguments("--headless");
        driver = new ChromeDriver(opt);
        driver.manage().window().maximize();
    }

    @BeforeEach
    public void setupDate() {
        savedAuthor = authorRepository.save(Author.builder().name("AuthorName").surname("AuthorSurname").email("author@mail.com").build());
        savedPublisher = publisherRepository.save(Publisher.builder().name("PublisherName").build());
    }

    @AfterAll
    public void cleanup() {
        driver.close();
    }

    @Test
    public void emptyListTest() {
        driver.get("http://localhost:" + port + "/books");

        assertThat(driver.findElement(By.className("no-content")).getText()).isEqualTo("No data available.");
    }

    @Test
    public void createBookSuccessTest() {
        driver.get("http://localhost:" + port + "/books");
        driver.findElement(By.className("book-create-button")).click();

        assertThat(driver.getTitle()).isEqualTo("MVC App - Create book");

        driver.findElement(By.className("author-input")).clear();
        driver.findElement(By.className("publisher-input")).clear();
        driver.findElement(By.className("count-input")).clear();

        driver.findElement(By.className("title-input")).sendKeys("BookTitle");
        driver.findElement(By.className("author-input")).sendKeys("1");
        driver.findElement(By.className("publisher-input")).sendKeys("1");
        driver.findElement(By.className("count-input")).sendKeys("123");
        driver.findElement(By.className("date-input")).sendKeys("2019-11-11");
        driver.findElement(By.className("isbn-input")).sendKeys("1112223334441");

        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollBy(0,100)", "");
        new Actions(driver).moveToElement(driver.findElement(By.className("book-create-button"))).click().build().perform();

        assertThat(driver.getTitle()).isEqualTo("MVC App - Books");
        assertThat(driver.findElement(By.id("bookId-1")).getText()).isEqualTo("1");
        assertThat(driver.findElement(By.id("bookTitle-1")).getText()).isEqualTo("BookTitle");
        assertThat(driver.findElement(By.id("bookAut-1")).getText()).isEqualTo("AuthorName AuthorSurname");
        assertThat(driver.findElement(By.id("bookPub-1")).getText()).isEqualTo("PublisherName");
        assertThat(driver.findElement(By.id("bookCount-1")).getText()).isEqualTo("123");
        assertThat(driver.findElement(By.id("bookDate-1")).getText()).isEqualTo("2019-11-11");
        assertThat(driver.findElement(By.id("bookIsbn-1")).getText()).isEqualTo("1112223334441");
    }

    @Test
    public void createBookNotSuccessTest() {
        driver.get("http://localhost:" + port + "/books");
        driver.findElement(By.className("book-create-button")).click();

        assertThat(driver.getTitle()).isEqualTo("MVC App - Create book");

        driver.findElement(By.className("author-input")).clear();
        driver.findElement(By.className("publisher-input")).clear();
        driver.findElement(By.className("count-input")).clear();

        driver.findElement(By.className("title-input")).sendKeys("a");
        driver.findElement(By.className("author-input")).sendKeys("1");
        driver.findElement(By.className("publisher-input")).sendKeys("1");
        driver.findElement(By.className("count-input")).sendKeys("123");
        driver.findElement(By.className("date-input")).sendKeys("2019-11-11");
        driver.findElement(By.className("isbn-input")).sendKeys("1112223334441");

        new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.elementToBeClickable(By.className("book-create-button"))).click();

        assertThat(driver.getTitle()).isEqualTo("MVC App - Books");
        assertThat(driver.findElement(By.className("no-content")).getText()).isEqualTo("No data available.");
    }

    @Test
    public void updateBookSuccessTest() throws ParseException {
        repository.save(Book.builder()
                .author(savedAuthor)
                .pageCount(123)
                .publisher(savedPublisher)
                .title("BookTitle")
                .releaseDate(new Date(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-11").getTime()))
                .isbn("1112223334441").build());
        driver.get("http://localhost:" + port + "/books");

        assertThat(driver.getTitle()).isEqualTo("MVC App - Books");
        assertThat(driver.findElement(By.id("bookId-1")).getText()).isEqualTo("1");
        assertThat(driver.findElement(By.id("bookTitle-1")).getText()).isEqualTo("BookTitle");
        assertThat(driver.findElement(By.id("bookAut-1")).getText()).isEqualTo("AuthorName AuthorSurname");
        assertThat(driver.findElement(By.id("bookPub-1")).getText()).isEqualTo("PublisherName");
        assertThat(driver.findElement(By.id("bookCount-1")).getText()).isEqualTo("123");
        assertThat(driver.findElement(By.id("bookDate-1")).getText()).isEqualTo("2019-11-11");
        assertThat(driver.findElement(By.id("bookIsbn-1")).getText()).isEqualTo("1112223334441");

        driver.findElement(By.id("bookUpdate-1")).click();

        assertThat(driver.getTitle()).isEqualTo("MVC App - Update book");

        driver.findElement(By.className("title-input")).clear();
        driver.findElement(By.className("author-input")).clear();
        driver.findElement(By.className("publisher-input")).clear();
        driver.findElement(By.className("count-input")).clear();
        driver.findElement(By.className("date-input")).clear();
        driver.findElement(By.className("isbn-input")).clear();

        driver.findElement(By.className("title-input")).sendKeys("NewTitle");
        driver.findElement(By.className("author-input")).sendKeys("1");
        driver.findElement(By.className("publisher-input")).sendKeys("1");
        driver.findElement(By.className("count-input")).sendKeys("321");
        driver.findElement(By.className("date-input")).sendKeys("2016-11-11");
        driver.findElement(By.className("isbn-input")).sendKeys("1112223334442");

        new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.elementToBeClickable(By.className("book-update-button"))).click();

        assertThat(driver.getTitle()).isEqualTo("MVC App - Books");
        assertThat(driver.findElement(By.id("bookId-1")).getText()).isEqualTo("1");
        assertThat(driver.findElement(By.id("bookTitle-1")).getText()).isEqualTo("NewTitle");
        assertThat(driver.findElement(By.id("bookAut-1")).getText()).isEqualTo("AuthorName AuthorSurname");
        assertThat(driver.findElement(By.id("bookPub-1")).getText()).isEqualTo("PublisherName");
        assertThat(driver.findElement(By.id("bookCount-1")).getText()).isEqualTo("321");
        assertThat(driver.findElement(By.id("bookDate-1")).getText()).isEqualTo("2016-11-11");
        assertThat(driver.findElement(By.id("bookIsbn-1")).getText()).isEqualTo("1112223334442");
    }

    @Test
    public void updateBookNotSuccessTest() throws ParseException {
        repository.save(Book.builder()
                .author(savedAuthor)
                .pageCount(123)
                .publisher(savedPublisher)
                .title("BookTitle")
                .releaseDate(new Date(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-11").getTime()))
                .isbn("1112223334441").build());
        driver.get("http://localhost:" + port + "/books");

        assertThat(driver.getTitle()).isEqualTo("MVC App - Books");
        assertThat(driver.findElement(By.id("bookId-1")).getText()).isEqualTo("1");
        assertThat(driver.findElement(By.id("bookTitle-1")).getText()).isEqualTo("BookTitle");
        assertThat(driver.findElement(By.id("bookAut-1")).getText()).isEqualTo("AuthorName AuthorSurname");
        assertThat(driver.findElement(By.id("bookPub-1")).getText()).isEqualTo("PublisherName");
        assertThat(driver.findElement(By.id("bookCount-1")).getText()).isEqualTo("123");
        assertThat(driver.findElement(By.id("bookDate-1")).getText()).isEqualTo("2019-11-11");
        assertThat(driver.findElement(By.id("bookIsbn-1")).getText()).isEqualTo("1112223334441");

        driver.findElement(By.id("bookUpdate-1")).click();

        assertThat(driver.getTitle()).isEqualTo("MVC App - Update book");

        driver.findElement(By.className("title-input")).clear();
        driver.findElement(By.className("author-input")).clear();
        driver.findElement(By.className("publisher-input")).clear();
        driver.findElement(By.className("count-input")).clear();
        driver.findElement(By.className("date-input")).clear();
        driver.findElement(By.className("isbn-input")).clear();

        driver.findElement(By.className("title-input")).sendKeys("a");
        driver.findElement(By.className("author-input")).sendKeys("1");
        driver.findElement(By.className("publisher-input")).sendKeys("1");
        driver.findElement(By.className("count-input")).sendKeys("321");
        driver.findElement(By.className("date-input")).sendKeys("2016-11-11");
        driver.findElement(By.className("isbn-input")).sendKeys("1112223334442");

        new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.elementToBeClickable(By.className("book-update-button"))).click();

        assertThat(driver.getTitle()).isEqualTo("MVC App - Books");
        assertThat(driver.findElement(By.id("bookId-1")).getText()).isEqualTo("1");
        assertThat(driver.findElement(By.id("bookTitle-1")).getText()).isEqualTo("BookTitle");
        assertThat(driver.findElement(By.id("bookAut-1")).getText()).isEqualTo("AuthorName AuthorSurname");
        assertThat(driver.findElement(By.id("bookPub-1")).getText()).isEqualTo("PublisherName");
        assertThat(driver.findElement(By.id("bookCount-1")).getText()).isEqualTo("123");
        assertThat(driver.findElement(By.id("bookDate-1")).getText()).isEqualTo("2019-11-11");
        assertThat(driver.findElement(By.id("bookIsbn-1")).getText()).isEqualTo("1112223334441");
    }

    @Test
    public void deleteBookSingleListTest() throws ParseException {
        repository.save(Book.builder()
                .author(savedAuthor)
                .pageCount(123)
                .publisher(savedPublisher)
                .title("BookTitle")
                .releaseDate(new Date(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-11").getTime()))
                .isbn("1112223334441").build());
        driver.get("http://localhost:" + port + "/books");

        driver.findElement(By.id("bookDelete-1")).click();
        assertThat(driver.findElement(By.className("no-content")).getText()).isEqualTo("No data available.");
    }

    @Test
    public void deleteBookMultipleListTest() throws ParseException {
        repository.save(Book.builder()
                .author(savedAuthor)
                .pageCount(123)
                .publisher(savedPublisher)
                .title("BookTitle")
                .releaseDate(new Date(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-11").getTime()))
                .isbn("1112223334441").build());
        repository.save(Book.builder()
                .author(savedAuthor)
                .pageCount(321)
                .publisher(savedPublisher)
                .title("SecondBook")
                .releaseDate(new Date(new SimpleDateFormat("yyyy-MM-dd").parse("2015-11-11").getTime()))
                .isbn("1112223334443").build());

        driver.get("http://localhost:" + port + "/books");

        driver.findElement(By.id("bookDelete-1")).click();

        assertThatThrownBy(() -> driver.findElement(By.id("bookId-1"))).isInstanceOf(NoSuchElementException.class);

        assertThat(driver.findElement(By.id("bookId-2")).getText()).isEqualTo("2");
        assertThat(driver.findElement(By.id("bookTitle-2")).getText()).isEqualTo("SecondBook");
        assertThat(driver.findElement(By.id("bookAut-2")).getText()).isEqualTo("AuthorName AuthorSurname");
        assertThat(driver.findElement(By.id("bookPub-2")).getText()).isEqualTo("PublisherName");
        assertThat(driver.findElement(By.id("bookCount-2")).getText()).isEqualTo("321");
        assertThat(driver.findElement(By.id("bookDate-2")).getText()).isEqualTo("2015-11-11");
        assertThat(driver.findElement(By.id("bookIsbn-2")).getText()).isEqualTo("1112223334443");
    }

    @Test
    public void emptyUpdatePageTest() {
        driver.get("http://localhost:" + port + "/books/update/10000");
        assertThat(driver.findElement(By.className("no-content")).getText()).isEqualTo("Failed to get book with this ID.");
    }

    @Test
    public void readBookTest() throws ParseException {
        repository.save(Book.builder()
                .author(savedAuthor)
                .pageCount(123)
                .publisher(savedPublisher)
                .title("BookTitle")
                .releaseDate(new Date(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-11").getTime()))
                .isbn("1112223334441").build());
        driver.get("http://localhost:" + port + "/books");

        assertThat(driver.getTitle()).isEqualTo("MVC App - Books");
        assertThat(driver.findElement(By.id("bookId-1")).getText()).isEqualTo("1");
        assertThat(driver.findElement(By.id("bookTitle-1")).getText()).isEqualTo("BookTitle");
        assertThat(driver.findElement(By.id("bookAut-1")).getText()).isEqualTo("AuthorName AuthorSurname");
        assertThat(driver.findElement(By.id("bookPub-1")).getText()).isEqualTo("PublisherName");
        assertThat(driver.findElement(By.id("bookCount-1")).getText()).isEqualTo("123");
        assertThat(driver.findElement(By.id("bookDate-1")).getText()).isEqualTo("2019-11-11");
        assertThat(driver.findElement(By.id("bookIsbn-1")).getText()).isEqualTo("1112223334441");
    }
}
