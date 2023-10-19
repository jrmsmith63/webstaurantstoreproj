package SelemMavenPractice.webstaruant.copy;

import static org.testng.Assert.assertEquals;
import java.util.ArrayList;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class webstaruant {	
	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		
		System.setProperty("webdriver.chrome.driver", "C:\\Users\\jrmsm\\Downloads\\chromedriver-win329\\chromedriver-win32\\chromedriver.exe");

		WebDriver driver = new ChromeDriver();
		JavascriptExecutor js = (JavascriptExecutor)driver;
		
		WebDriverWait wait = new WebDriverWait(driver,30);
		
		//Open Webstaurantstore website
		driver.get("https://www.webstaurantstore.com/");
		driver.manage().window().maximize();
		Thread.sleep(2000);
		 
		 // Search website for  term "stainless work table"
		driver.findElement(By.id("searchval")).sendKeys("stainless work table");
		driver.findElement(By.cssSelector("[value=\"Search\"]")).click();
		
		// Loop through each page of search results to check if all results include the word "Table" in their title.
		WebElement btnNextPage = driver.findElement(By.cssSelector("[class=\"inline-block leading-4 align-top rounded-r-md\"]"));  
		List<WebElement> foundElements = new ArrayList<WebElement>();
		System.out.println(foundElements.size());
		int pageCounter = 1;
		int itemMissing = 0;
		String pageCounterString = "";
		String pageFlag = "false";	
			
			while(pageFlag != "true")
			{
					pageCounterString = Integer.toString(pageCounter);
					System.out.println("Page Number : " + driver.findElement(By.linkText(pageCounterString)).getText());
				
					wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"ProductBoxContainer\"]"))); 
	
					List<WebElement> actualList = driver.findElements(By.xpath("//*[@id=\"ProductBoxContainer\"]/div[1]/a/span")); //full list of results
					List<WebElement> expectedList = driver.findElements(By.xpath("//*[@id=\"ProductBoxContainer\"]/div[1]/a/span[contains(text(), 'Table')]")); //results with the word "Table" in title
					itemMissing = actualList.size() - expectedList.size();
					
					System.out.println(actualList.size() + " items found on page " + pageCounterString);
				
					foundElements.addAll(actualList);
					System.out.println(foundElements.size() + " Total elements found");	
					
					try {
					    assertEquals(expectedList, actualList);
					    System.out.println("All searched items on page " + pageCounterString + " include the word Table in their title");
					} 
					catch(Throwable e){
					    System.err.println(itemMissing + " item(s) detected on page " + pageCounterString + " that dosen't include the word Table. ");
					}
					
					btnNextPage = driver.findElement(By.cssSelector("[class=\"inline-block leading-4 align-top rounded-r-md\"]"));
					btnNextPage.click();
					pageCounter++;
					pageCounterString = Integer.toString(pageCounter);
					
					try
					{
						driver.findElement(By.linkText(pageCounterString));
					}
					catch( NoSuchElementException ex)
					{
						System.out.println( "Last page reached"); //ex.printStackTrace();
					    pageFlag = "true";   
					}
		
			}
						 
		System.out.println("Finished checking search results");
		Thread.sleep(4000);
		 
		//Choose last found search result
		js.executeScript("window.scrollTo(0,document.body.scrollHeight)");
		 
		int totalPageSearchResults = driver.findElements(By.xpath("//*[@id=\"ProductBoxContainer\"]/div[1]/a/span")).size();
		driver.findElements(By.xpath("//*[@id=\"ProductBoxContainer\"]/div[1]/a/span")).get(totalPageSearchResults-1).click(); 
		Thread.sleep(2000);
		
		//Add last found search result to cart
		driver.findElement(By.id("buyButton")).click();
		Thread.sleep(6000);
		driver.findElement(By.linkText("View Cart")).click();
		System.out.println("Added last item to cart");
		Thread.sleep(3000);
	
		 //Empty cart
		driver.findElement(By.xpath("//*[@id=\"main\"]/div[1]/div/div[1]/div/button")).click();
		driver.findElement(By.xpath("//div/footer/button[1]")).click();
		Thread.sleep(4000);
		System.out.println("Emptied Cart");
		
		//Close Browser
		System.out.println("Testcase Completed, Closing Browser");
		driver.close();

	}

}
