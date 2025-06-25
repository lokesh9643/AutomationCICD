package rahulshettyacademy.tests;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import io.github.bonigarcia.wdm.WebDriverManager;
import rahulshettyacademy.pageobjects.LandingPage;

public class StandAloneTest {

	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub

		// Opening browser
		String productName = "ZARA COAT 3";
		WebDriverManager.chromedriver().setup();
		WebDriver driver = new ChromeDriver();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		driver.manage().window().maximize();
		
		//Login
		driver.get("https://rahulshettyacademy.com/client");
		LandingPage landingPage = new LandingPage(driver);
		driver.findElement(By.id("userEmail")).sendKeys("lokeshv39@gmail.com");
		driver.findElement(By.id("userPassword")).sendKeys("Thakur@2000");
		driver.findElement(By.id("login")).click();
		
		//Add item to cart
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".mb-3")));
		List<WebElement> products = driver.findElements(By.cssSelector(".mb-3"));
		WebElement prod = products.stream()
				.filter(product -> product.findElement(By.cssSelector("b")).getText().equals(productName)).findFirst()
				.orElse(null);
		prod.findElement(By.cssSelector(".card-body button:last-of-type")).click();
		
		// below lines show how to use optional
		
//		Optional<WebElement> prod2= products.stream()
//				.filter(product -> product.findElement(By.cssSelector("b")).getText().equals(productName)).findAny();
//		
//		// Check if prod2 is present
//		if (prod2.isPresent()) {
//		    // Extract the WebElement from the Optional
//		    WebElement product = prod2.get();
//		    
//		    // Perform actions on the found WebElement
//		    WebElement button = product.findElement(By.cssSelector(".card-body button:last-of-type"));
//		    button.click();
//		} else {
//		    // Handle the case where no product was found
//		    System.out.println("Product with the name '" + productName + "' not found.");
//		}
		
		
		//============== OR
		
//		prod2.ifPresent(product -> {
//		    WebElement button = product.findElement(By.cssSelector(".card-body button:last-of-type"));
//		    button.click();
//		});
//		
		//getting into cart
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#toast-container")));
		// ng-animating
		wait.until(ExpectedConditions.invisibilityOf(driver.findElement(By.cssSelector(".ng-animating"))));
		driver.findElement(By.cssSelector("[routerlink*='cart']")).click();

		//verifying item in cart
		List<WebElement> cartProducts = driver.findElements(By.cssSelector(".cartSection h3"));
		Boolean match = cartProducts.stream()
				.anyMatch(cartProduct -> cartProduct.getText().equalsIgnoreCase(productName));
		Assert.assertTrue(match);
		
		//Doing checkout
		wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".totalRow button")));
		Thread.sleep(3000);
		JavascriptExecutor js=(JavascriptExecutor)driver;
		js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
		//js.executeScript("arguments[0].scrollIntoView(true)", driver.findElement(By.cssSelector(".totalRow button")));
		Thread.sleep(3000);
		driver.findElement(By.cssSelector(".totalRow button")).click();
		
		//Giving shipping information
		Actions a = new Actions(driver);
		a.sendKeys(driver.findElement(By.cssSelector("[placeholder='Select Country']")), "india").build().perform();

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".ta-results")));
		Thread.sleep(3000);
		driver.findElement(By.xpath("(//button[contains(@class,'ta-item')])[2]")).click();
		Thread.sleep(3000);
		driver.findElement(By.cssSelector(".action__submit")).click();

		//Verifying the order details after payment
		Thread.sleep(3000);
		String confirmMessage = driver.findElement(By.cssSelector(".hero-primary")).getText();
		Assert.assertTrue(confirmMessage.equalsIgnoreCase("THANKYOU FOR THE ORDER."));
		driver.close();
		System.out.println("Driver closed successfully");
	}
}
