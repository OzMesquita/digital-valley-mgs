
/*
package util;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;


public class TesteLogin {

	private WebDriver driver = null;
	@Before
	public void inicializa() {
		System.setProperty("webdriver.chrome.driver","/Users/Gilberto Lima/Desktop/chromedriver_win32/chromedriver.exe");
		this.driver = new ChromeDriver();
		
	}
	@Test
	public void teste() {
		driver.get("localhost:8080/Controle_de_Acesso");
		WebElement login = driver.findElement(By.name("login"));
		WebElement senha = driver.findElement(By.name("senha"));
		WebElement botaoLogar = driver.findElement(By.id("btnsalvar"));
		login.sendKeys("FernandoA");
		senha.sendKeys("123456");
		botaoLogar.submit();
		
		boolean encontrou = driver.getPageSource().contains("Módulos");
		assertTrue(encontrou);
		
		acessaDarwin();
		acessarMinhasSelecoes();
		cadastrarEtapaDeInscricao();
		//removerEtapaDeInscricao();
	}
	
	public void acessaDarwin() {
		WebElement botaoAcessar = driver.findElement(By.id("Darwin2018.1"));
		botaoAcessar.submit();
	}
	
	public void acessarMinhasSelecoes() {
		WebElement minhasSelecoes =  driver.findElement(By.linkText("Minhas seleções"));
		minhasSelecoes.click();
		List<WebElement> elementos = driver.findElements(By.className("card-body"));
		
		for(WebElement we : elementos) {
			if (we.findElement(By.className("card-title")).getText().equals("BOLSA DE EXTENSÃO")) {
				we.findElement(By.linkText("Mais informações")).click();
			}
		}
	}
	
	public void removerEtapaDeInscricao() {
		driver.findElement(By.linkText("Remover etapa")).click();
	}
	
	
	public void cadastrarEtapaDeInscricao() {
		 WebElement linkCadInscri = driver.findElement(By.linkText("Cadastrar etapa de inscrição"));
		 linkCadInscri.click();
		 
		 WebElement descricao = driver.findElement(By.name("descricao"));
		 WebElement dataInicio = driver.findElement(By.name("dataInicio"));
		 WebElement dataTermino = driver.findElement(By.name("dataTermino"));
		 Select criterioDeAvaliacao = new Select(driver.findElement(By.name("criterioDeAvaliacao")));
		 
				 
		 
		 descricao.sendKeys("Etapa para entrega da documentação.");
		 dataInicio.clear();
		 dataInicio.sendKeys("02/09/2018");
		// dataTermino.clear();
		// dataTermino.sendKeys("05/09/2018");
		 criterioDeAvaliacao.selectByVisibleText("Deferimento");
		
		 WebElement botaoAcessar = driver.findElement(By.className("btn-primary"));
		 botaoAcessar.submit();
	}
	
	
	
	//@After
	//public void finaliza() {
	//	driver.quit();
	//}
	
}
*/
