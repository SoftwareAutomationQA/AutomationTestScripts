package appModules;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.Assert;

import com.aventstack.extentreports.Status;

import autoModularFramework.TestScriptsMain;
import io.github.bonigarcia.wdm.WebDriverManager;
import autoModularFramework.ReuseableTestFunctions;

public class Module {

	//Method to Launch different Browser 
	public static WebDriver launchBrowser(WebDriver driver,String url,String browserName) {
		
		if(browserName.equalsIgnoreCase("firefoxBrowser")) {
			
			WebDriverManager.firefoxdriver().setup();
			driver= new FirefoxDriver();
			driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
			driver.get(url);
		}
		else if(browserName.equalsIgnoreCase("chromeBrowser")) {
			WebDriverManager.chromedriver().setup();
			driver= new ChromeDriver();
			driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
			driver.get(url);
			driver.manage().window().maximize();
		}
		else if(browserName.equalsIgnoreCase("ieBrowser")) {
			WebDriverManager.iedriver().setup();
			/*DesiredCapabilities caps = DesiredCapabilities.internetExplorer();
			caps.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS,true);*/
			driver=new InternetExplorerDriver();
			driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
			driver.get(url);
			driver.manage().window().maximize();
		}
		return driver;
	}
	
	//Method to Login
	public static void navigatetoXERO(WebDriver driver,String testname,String userName, String Password,String expected) throws InterruptedException {

		//System.out.println(userName+" " +Password);
		//driver.get(url);
		WebElement uName= driver.findElement(By.xpath(".//*[@id='email']"));
		ReuseableTestFunctions.enterText(uName,userName, "UserName Input");

		WebElement pwd= driver.findElement(By.xpath(".//*[@id='password']"));
		ReuseableTestFunctions.enterText(pwd,Password, "Password Input");

		/*String unactual = uName.getText();
		Assert.assertEquals(unactual, userName);

		String pwdactual = pwd.getText();
		Assert.assertEquals(pwdactual, Password);*/

		WebElement login= driver.findElement(By.xpath(".//*[@id='submitButton']"));
		ReuseableTestFunctions.objectClick(login, "Login Button");
		Thread.sleep(10000);

		String actual = driver.getCurrentUrl();
		Assert.assertEquals(actual, expected);
		TestScriptsMain.logger.log(Status.INFO, "Inside Homepage");
		//driver.quit();
	}

	//Method to Incorrect Password Check
	public static void incorrectPassword(WebDriver driver,String testname, String userName, String Password,String expected) throws InterruptedException {

		WebElement uName= driver.findElement(By.xpath(".//*[@id='email']"));
		ReuseableTestFunctions.enterText(uName,userName, "UserName Input");

		WebElement pwd= driver.findElement(By.xpath(".//*[@id='password']"));
		ReuseableTestFunctions.enterText(pwd,Password, "Password Input");

		//		Assert.assertEquals(unactual, userName);
		Thread.sleep(5000);
		WebElement login= driver.findElement(By.xpath(".//*[@id='submitButton']"));
		ReuseableTestFunctions.objectClick(login, "Login Button");

		String errorMsgactual = driver.findElement(By.xpath(".//*[@id='contentTop']/div[2]/div[1]/div[2]/p")).getText();
		Assert.assertEquals(errorMsgactual, expected);

	}

	//Method to Incorrect Username Check
	public static void incorrectUsername(WebDriver driver,String testname, String userName, String Password,String expected) throws InterruptedException {

		WebElement uName= driver.findElement(By.xpath(".//*[@id='email']"));
		ReuseableTestFunctions.enterText(uName,userName, "UserName Input");

		WebElement pwd= driver.findElement(By.xpath(".//*[@id='password']"));
		ReuseableTestFunctions.enterText(pwd,Password, "Password Input");

		//		Assert.assertEquals(unactual, userName);
		Thread.sleep(5000);
		WebElement login= driver.findElement(By.xpath(".//*[@id='submitButton']"));
		ReuseableTestFunctions.objectClick(login, "Login Button");

		String errorMsgactual = driver.findElement(By.xpath(".//*[@id='contentTop']/div[2]/div[1]/div[2]/p")).getText();
		Assert.assertEquals(errorMsgactual, expected);

	}

	//Method to Logout From Application
	public static void Logout(WebDriver driver) {

		WebElement userMenu= driver.findElement(By.xpath(".//*[@id='xero-nav']/div[2]/div[1]/div[2]/a"));
		ReuseableTestFunctions.objectClick(userMenu, "UserMenu");

		WebElement logout= driver.findElement(By.xpath(".//*[@id='xero-nav']/div[2]/div[1]/div[2]/div/ul/li[3]/a"));
		ReuseableTestFunctions.objectClick(logout, "logout Button");
	}

	//Method to add Organization
	public static void addOrganization(WebDriver driver, String Organizationname, String Orgservice) throws InterruptedException {

		WebElement companyName =driver.findElement(By.xpath(".//*[@id='xero-nav']/div[2]/div[1]/div[1]/div/h2"));
		ReuseableTestFunctions.objectClick(companyName, "CompanyName");

		WebElement myXero =driver.findElement(By.xpath(".//*[@id='xero-nav']/div[2]/div[1]/div[1]/div/div/div/a"));
		ReuseableTestFunctions.objectClick(myXero, "MyXero");

		WebElement addOrg =driver.findElement(By.xpath(".//*[@id='ext-gen1043']"));
		ReuseableTestFunctions.objectClick(addOrg, "Add more Organization");
		Thread.sleep(6000);

		WebElement orgName =driver.findElement(By.xpath(".//*[@id='text-1022-inputEl']"));
		ReuseableTestFunctions.enterText(orgName, Organizationname, "Organization Name Input");
		Thread.sleep(3000);

		WebElement orgDo =driver.findElement(By.xpath(".//*[@id='industrysearchcombofield-1025-inputEl']"));
		ReuseableTestFunctions.enterText(orgDo, Orgservice, "Organization does Input");
		Thread.sleep(3000);

		WebElement buyNow  =driver.findElement(By.xpath(".//*[@id='simplebutton-1036']"));
		ReuseableTestFunctions.objectClick(buyNow, "BuyNow Button");
		Thread.sleep(8000);

		String pageTitleactual = driver.getTitle();
		//	System.out.println(pageTitleactual);
		Assert.assertEquals(pageTitleactual, "Xero | Pricing Plan");
		TestScriptsMain.logger.log(Status.INFO, "Pruchase Plan details page displayed");
	}

}
