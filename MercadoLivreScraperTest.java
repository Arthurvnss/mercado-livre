import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.util.List;

public class MercadoLivreScraperTest {

    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeEach
    public void iniciar() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");

        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    @Test
    public void buscarProduto() throws IOException {

        driver.get("https://www.mercadolivre.com.br/");

        WebElement campoBusca = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.cssSelector("input[name='as_word']"))
        );

        campoBusca.sendKeys("Xiaomi POCO");
        campoBusca.sendKeys(Keys.ENTER);

        List<WebElement> produtos = wait.until(
                ExpectedConditions.presenceOfAllElementsLocatedBy(
                        By.cssSelector("li.ui-search-layout__item, div.poly-card"))
        );

        WebElement primeiroProduto = produtos.get(0);

        String descricao = primeiroProduto.findElement(
                By.cssSelector("a.poly-component__title, h2")).getText();

        String preco = primeiroProduto.findElement(
                By.cssSelector(".andes-money-amount__fraction")).getText();

        String url = primeiroProduto.findElement(
                By.cssSelector("a")).getAttribute("href");

        salvarCSV(descricao, preco, url);
    }

    private void salvarCSV(String descricao, String preco, String url) throws IOException {
        FileWriter arquivo = new FileWriter("produto.csv");
        arquivo.write("Descrição,Preço,URL\n");
        arquivo.write("\"" + descricao + "\",\"" + preco + "\",\"" + url + "\"\n");
        arquivo.close();
    }

    @AfterEach
    public void finalizar() {
        if (driver != null) {
            driver.quit();
        }
    }
}
