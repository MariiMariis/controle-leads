package com.gpsit.controle_leads.selenium;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class LeadSeleniumTest {

    @LocalServerPort
    private int port;

    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeAll
    static void setupClass() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void setupTest() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new"); // Executar de forma silenciosa
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--disable-gpu");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--remote-allow-origins=*");
        
        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @AfterEach
    void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    void testarFluxoCompletoLeadE2E() {
        // 1. Acessar a página inicial
        driver.get("http://localhost:" + port + "/leads");
        assertEquals("Lista de Leads", driver.getTitle());

        // 2. Clicar em "Novo Lead"
        WebElement btnNovo = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("+ Adicionar Lead")));
        btnNovo.click();

        // 3. Preencher formulário de Cadastro (Cenário Válido)
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nome"))).sendKeys("Lead Selenium");
        driver.findElement(By.id("email")).sendKeys("selenium@teste.com");
        driver.findElement(By.id("telefone")).sendKeys("11999999999");
        
        Select selectStatus = new Select(driver.findElement(By.id("status")));
        selectStatus.selectByValue("NOVO");

        driver.findElement(By.id("observacoes")).sendKeys("Criado pelo Selenium WebDriver");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        // 4. Verificar se redirecionou para lista e se exibiu mensagem de sucesso
        WebElement alert = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".alert-success")));
        assertTrue(alert.getText().contains("Lead salvo com sucesso"));
        
        // 5. Editar o Lead recém criado
        WebElement btnEditar = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//tr[td[contains(text(), 'selenium@teste.com')]]//a[contains(text(), 'Editar')]")));
        btnEditar.click();

        // 6. Alterar um dado do formulário
        WebElement inputNome = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nome")));
        inputNome.clear();
        inputNome.sendKeys("Lead Editado Pelo Selenium");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        // 7. Verificar se o nome mudou na lista
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//td[contains(text(), 'Lead Editado Pelo Selenium')]")));

        // 8. Tentar cadastrar um lead com dados inválidos
        driver.get("http://localhost:" + port + "/leads/novo");
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[type='submit']"))).click();
        
        // Envia em branco e vai gerar mensagens de erro do Bean Validation
        WebElement erroNome = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".invalid-feedback")));
        assertTrue(erroNome.isDisplayed(), "Mensagem de erro de validação deve ser exibida");
        
        // 9. Excluir o lead editado
        driver.get("http://localhost:" + port + "/leads");
        WebElement btnExcluir = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//tr[td[contains(text(), 'selenium@teste.com')]]//a[contains(text(), 'Excluir')]")));
        btnExcluir.click();

        // 10. Lidar com o alerta de confirmação do Javascript
        Alert confirmAlert = wait.until(ExpectedConditions.alertIsPresent());
        confirmAlert.accept();

        // 11. Verificar se mensagem de sucesso de exclusão apareceu
        WebElement alertExclusao = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".alert-success")));
        assertTrue(alertExclusao.getText().contains("Lead removido com sucesso"));
    }
}
