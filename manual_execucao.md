


# Manual de Execução — Sistema de Controle de Leads

  ## Requisitos do Sistema

-  **Java**: 17 ou superior (homologado com Java 25 + Gradle 9.3 + Spring Boot 3.4.3)

-  **Sistema Operacional**: Windows / Linux / macOS

-  **Navegador (apenas para testes E2E)**: Google Chrome — o WebDriverManager baixa o driver automaticamente

  

---

  

## 1. Como Iniciar a Aplicação?

  

### Via Linha de Comando

```powershell

# Windows:

.\gradlew.bat bootRun

  

# Linux/macOS:

./gradlew bootRun

```

Aguarde a mensagem `Started ControleLeadsApplication in X seconds` e acesse:

**http://localhost:8080/leads**

  

### Via IntelliJ IDEA

1.  `File > Open` → selecione a pasta `controle_leads` ou o arquivo `build.gradle`

2. Escolha **Open as Project** e aguarde a sincronização do Gradle

3. Abra `ControleLeadsApplication.java` → clique no ícone ▶ verde → **Run**

4. Acesse **http://localhost:8080/leads** no navegador

  

---

  ## 2.Onde estão os testes? 
  
  Os arquivos de teste se encontram em `src/test/java/com/gpsit/controle_leads/`

| Arquivo | Tipo | Localização |

|---|---|---|

| `LeadServicoTest.java` | Unitário (Mockito) | `test/.../servico/` |

| `LeadControladorTest.java` | Integração (MockMvc) | `test/.../controlador/` |

| `LeadFuzzTest.java` | Fuzz / Property-based (jqwik) | `test/.../fuzz/` |

| `LeadSeleniumTest.java` | E2E Headless (Selenium) | `test/.../selenium/` |

| `ControleLeadsApplicationTests.java` | Smoke test | `test/` raiz |


  

## 3. Como executar os Testes e Gerar Relatório de Cobertura?

  

```powershell

# Todos os testes + relatório JaCoCo (exclui Selenium):

.\gradlew.bat test jacocoTestReport

  

# Apenas testes E2E Selenium (requer Chrome instalado):

.\gradlew.bat test --tests "*.LeadSeleniumTest"

  

# Apenas um arquivo específico:

.\gradlew.bat test --tests "*.LeadFuzzTest"

.\gradlew.bat test --tests "*.LeadServicoTest"

.\gradlew.bat test --tests "*.LeadControladorTest"

```

  

### Visualizar Relatórios

```powershell

# Relatório de cobertura JaCoCo (HTML interativo):

start build\reports\jacoco\test\html\index.html

  

# Relatório de execução dos testes (aprovados/falhas):

start build\reports\tests\test\index.html

```


---

  
  

### Relatório de Cobertura JaCoCo (`build/reports/jacoco/test/html/index.html`)

-  **Verde** = linha/branch executada pelo teste

-  **Amarelo** = branch parcialmente executado 

-  **Vermelho** = código nunca executado nos testes


 