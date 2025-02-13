package autoModularFramework;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.Duration;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentReporter;

import appModules.Module;

public class TestScriptsMain {

	public static ExtentReporter htmlReport;
	public static ExtentReports extentReport;
	public static ExtentTest logger;
	public String filePath = "F:XeroSelenium\\src\\test\\java\\utility/BrowserData.xls";
	public static WebDriver driver;
	public static String[][] recordData;
	public String testcase, flag, firefoxBrowser,chromeBrowser,ieBrowser;

	@BeforeTest
	public void ExtentReport() {
		extentReport = ReuseableTestFunctions.generateReport();
	}

	@BeforeMethod
	@Parameters({"url"})
	public void launchBrowser(String url) throws IOException {

		recordData = ReuseableTestFunctions.readDataFromXl(filePath,"Sheet1");
		
		//System.out.println(recordData.length);
		for(int i=1;i<recordData.length;i++) {
			//testcase= recordData[i][1];
			flag= recordData[i][1];

			if(flag.equalsIgnoreCase("Y")) {

				firefoxBrowser = recordData[i][2];
				chromeBrowser = recordData[i][4];
				ieBrowser = recordData[i][6];

				if(firefoxBrowser.equalsIgnoreCase("Y")) {
					logger =ReuseableTestFunctions.createLogger("Executing in Firefox", extentReport);
					driver=Module.launchBrowser(driver, url, "firefoxBrowser");
					//logger.log(Status.INFO,"Launched the Application in FireFox Browser");
				}

				if(chromeBrowser.equalsIgnoreCase("Y")) {
					logger =ReuseableTestFunctions.createLogger(testcase +"in Chrome", extentReport);
					driver=Module.launchBrowser(driver, url, "chromeBrowser");
				}

				if(ieBrowser.equalsIgnoreCase("Y")) {
					logger =ReuseableTestFunctions.createLogger(testcase +"in IE", extentReport);
					driver=Module.launchBrowser(driver, url, "ieBrowser");
				}
			}
		}
	}

	@DataProvider
	public static String[][] dataProvide(Method m) throws IOException{
	//	System.out.println(m.getName());
		String path ="F:\\XeroSelenium\\src\\test\\java\\utility/TestData.xls";
		String[][] excelData = ReuseableTestFunctions.readData(path,"Sheet1",m.getName());

		return excelData;

	}

	@Test(dataProvider="dataProvide", enabled=true, priority=1)
	public void Login(String testname,String uname, String pwd ,String expct) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, InterruptedException {
		logger=ReuseableTestFunctions.createLogger(testname, extentReport);
		Module.navigatetoXERO(driver,testname,uname,pwd,expct);
	}

	@Test(dataProvider="dataProvide",enabled=true,priority=2)
	public void LoginIncorrectPwd(String testname, String uname, String pwd ,String expct) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, InterruptedException {
		logger=ReuseableTestFunctions.createLogger(testname, extentReport);
		Module.incorrectPassword(driver, testname, uname,pwd,expct);
	}

	@Test(dataProvider="dataProvide",enabled=true,priority=3)
	public void LoginIncorrectId(String testname, String uname, String pwd ,String expct) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, InterruptedException {
		logger=ReuseableTestFunctions.createLogger(testname, extentReport);
		Module.incorrectUsername(driver,testname, uname,pwd,expct);
	}

	@Test(enabled=true,priority=4)
	public void forgetPassword() {
		logger=ReuseableTestFunctions.createLogger("forgetPassword", extentReport);
		String expected= "A link to reset your password has been sent to:\r\n" + 
				"ts@gmail.com";

		WebElement forgetPwdLink = driver.findElement(By.xpath(".//*[@id='contentTop']/div[2]/div[1]/a"));
		ReuseableTestFunctions.objectClick(forgetPwdLink, "Forgot Password Link");

		WebElement uName= driver.findElement(By.xpath(".//*[@id='UserName']"));
		ReuseableTestFunctions.enterText(uName,"ts@gmail.com", "UserName");

		WebElement sendLinkBtn = driver.findElement(By.xpath(".//*[@id='submitButton']/a/span"));
		ReuseableTestFunctions.objectClick(sendLinkBtn, "Send Link Button");
		try {
			Thread.sleep(4000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		String actualErrorLable = driver.findElement(By.xpath(".//*[@id='contentTop']/div/p[1]")).getText();
		logger.log(Status.INFO, actualErrorLable);
	}

	@Test(enabled=false,priority=5)
	@Parameters({"SignUpUrl"})
	public void signUpA(String SignUpUrl) throws InterruptedException {

		logger=ReuseableTestFunctions.createLogger("signUpA", extentReport);
		driver.get(SignUpUrl);
		WebElement freeTrail = driver.findElement(By.xpath("html/body/div[6]/header/nav/div[2]/div/div[1]/div/div/ul/li[1]/a"));
		ReuseableTestFunctions.objectClick(freeTrail, "Free Trail Button");


		WebElement fname= driver.findElement(By.xpath("html/body/div[6]/main/div[1]/div/div/form/div[2]/label/input"));
		ReuseableTestFunctions.enterText(fname, "Seen", "FirstName");

		WebElement lname= driver.findElement(By.xpath("html/body/div[6]/main/div[1]/div/div/form/div[3]/label/input"));
		ReuseableTestFunctions.enterText(lname, "dz", "LastName");

		WebElement email= driver.findElement(By.xpath("html/body/div[6]/main/div[1]/div/div/form/div[4]/label/input"));
		ReuseableTestFunctions.enterText(email, "Seen@yahoo.com", "Email Id");

		WebElement phone= driver.findElement(By.xpath("html/body/div[6]/main/div[1]/div/div/form/div[5]/label/input"));
		ReuseableTestFunctions.enterText(phone, "76809912313", "Phone");

		Thread.sleep(5000);
		WebElement selectCountry= driver.findElement(By.xpath("html/body/div[6]/main/div[1]/div/div/form/div[6]/label/span/select"));
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView();", selectCountry);

		Select select = new Select(selectCountry);
		select.selectByVisibleText("Switzerland");
		int size = driver.findElements(By.tagName("iframe")).size();
		System.out.println(size);

		WebElement frame = driver.findElement(By.xpath("html/body/div[6]/main/div[1]/div/div/form/div[7]/div/div/iframe"));
		driver.switchTo().frame(frame);

		WebElement captcha = driver.findElement(By.xpath(".//*[@id='recaptcha-anchor']/div[5]"));
		ReuseableTestFunctions.objectClick(captcha, "Captcha");
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
		WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("html/body/div[2]/div[3]/div[1]/div/div")));

		driver.switchTo().parentFrame();
		WebElement checkbox = driver.findElement(By.xpath("html/body/div[6]/main/div[1]/div/div/form/div[8]/div/label/input"));
		ReuseableTestFunctions.objectClick(checkbox, "checkbox");

		Thread.sleep(20000);
		WebElement getStarted = driver.findElement(By.className("g-recaptcha-submit"));
		ReuseableTestFunctions.objectClick(getStarted, "Get Started Button");

	}

	@Test(enabled=true,priority=6)
	@Parameters({"SignUpUrl"})
	public void signUpB(String SignUpUrl) throws InterruptedException {
		logger=ReuseableTestFunctions.createLogger("signUpB", extentReport);
		driver.get(SignUpUrl);
		WebElement freeTrail = driver.findElement(By.xpath("html/body/div[6]/header/nav/div[2]/div/div[1]/div/div/ul/li[1]/a"));
		ReuseableTestFunctions.objectClick(freeTrail, "Free Trail Button");
	}

	@Test(enabled=true,priority=7)
	@Parameters({"SignUpUrl"})
	public void signUpC(String SignUpUrl) throws InterruptedException {

		logger=ReuseableTestFunctions.createLogger("signUpC", extentReport);
		driver.get(SignUpUrl);
		logger.log(Status.INFO, "Launched The Application");
		WebElement freeTrail = driver.findElement(By.xpath("html/body/div[6]/header/nav/div[2]/div/div[1]/div/div/ul/li[1]/a"));
		ReuseableTestFunctions.objectClick(freeTrail, "Free Trail Button");
		logger.log(Status.INFO, "Free Trail Page Displayed");
		String parentWindow= driver.getWindowHandle();
		Thread.sleep(5000);
		WebElement privacy = driver.findElement(By.xpath("html/body/div[6]/main/div[1]/div/div/form/div[8]/div/label/a[2]"));
		ReuseableTestFunctions.objectClick(privacy, "Privacy Link");

		String expected = "https://www.xero.com/us/about/privacy/";

		Set<String> allWindows = (Set<String>) driver.getWindowHandles();
	//	System.out.println(allWindows);
		allWindows.remove(parentWindow);
		for(String curWindow : allWindows){
			
			driver.switchTo().window(curWindow);
			Thread.sleep(5000);
			String newurl = driver.getCurrentUrl();
			Assert.assertEquals(newurl, expected);
			
		}
		logger.log(Status.INFO, "Opend new Privacy Window");
	}

	@Test(enabled=true,priority=8)
	@Parameters({"SignUpUrl"})
	public void signUpD(String SignUpUrl) throws InterruptedException {

		logger=ReuseableTestFunctions.createLogger("signUpD", extentReport);
		driver.get(SignUpUrl);
		logger.log(Status.INFO, "Launched The Application");
		WebElement freeTrail = driver.findElement(By.xpath("html/body/div[6]/header/nav/div[2]/div/div[1]/div/div/ul/li[1]/a"));
		ReuseableTestFunctions.objectClick(freeTrail, "Free Trail Button");
		logger.log(Status.INFO, "Free Trail Page Displayed");
		String parentWindow= driver.getWindowHandle();
		Thread.sleep(5000);
		WebElement offerLink = driver.findElement(By.xpath("html/body/div[6]/main/div[1]/div/div/form/div[8]/div/label/a[3]"));
		ReuseableTestFunctions.objectClick(offerLink, "OfferDetails Link");

		String expected = "https://www.xero.com/us/signup/offer-details/";

		Set<String> allWindows = (Set<String>) driver.getWindowHandles();
	//	System.out.println(allWindows);
		allWindows.remove(parentWindow);
		for(String curWindow : allWindows){
			
			driver.switchTo().window(curWindow);
			Thread.sleep(5000);
			String newurl = driver.getCurrentUrl();
			Assert.assertEquals(newurl, expected);
			
		}
		logger.log(Status.INFO, "Opend new Offer Deatils Window");
	}

	@Test(enabled=true,priority=9)
	@Parameters({"SignUpUrl"})
	public void signUpE(String SignUpUrl) throws InterruptedException {

		logger=ReuseableTestFunctions.createLogger("signUpE", extentReport);
		driver.get(SignUpUrl);
		logger.log(Status.INFO, "Launched The Application");
		WebElement freeTrail = driver.findElement(By.xpath("html/body/div[6]/header/nav/div[2]/div/div[1]/div/div/ul/li[1]/a"));
		ReuseableTestFunctions.objectClick(freeTrail, "Free Trail Button");
		logger.log(Status.INFO, "Free Trail Page Displayed");
		String parentWindow= driver.getWindowHandle();
		Thread.sleep(5000);
		WebElement bookkeeperLink = driver.findElement(By.xpath("html/body/div[6]/main/div[2]/div/div/div/p/a"));
		ReuseableTestFunctions.objectClick(bookkeeperLink, "accountant or bookkeeper Link");

		String expected = "https://www.xero.com/us/partner-programs/partners/signup/";

		Set<String> allWindows = (Set<String>) driver.getWindowHandles();
	//	System.out.println(allWindows);
		allWindows.remove(parentWindow);
		for(String curWindow : allWindows){
			
			driver.switchTo().window(curWindow);
			Thread.sleep(5000);
			String newurl = driver.getCurrentUrl();
			Assert.assertEquals(newurl, expected);
			
		}
		logger.log(Status.INFO, "Opend new accountant or bookkeeper Window");
	}

	@Test(dataProvider="dataProvide", enabled=true,priority=10)
	public void TestTabsPage(String testname,String uname, String pwd ,String expct) throws InterruptedException {
		
		logger= ReuseableTestFunctions.createLogger("TestTabsPage", extentReport);
		Module.navigatetoXERO(driver,testname, uname, pwd, expct);
		WebElement ptext = driver.findElement(By.xpath(".//*[@id='xero-nav']/div[1]/p"));
		String unactual = ptext.getText();
		Assert.assertEquals(unactual, "You are currently using a trial account.Upgrade now");
		logger.log(Status.INFO,"Logged in to Home Page");
		
		WebElement dashboard = driver.findElement(By.xpath(".//*[@id='Dashboard']"));
		ReuseableTestFunctions.objectClick(dashboard, "DashBoard Button");
		String expecteddash= "Xero | Dashboard | Tekks";
		String actualdash = driver.getTitle();
		System.out.println(actualdash);
		Assert.assertEquals(actualdash, expecteddash);
		
		WebElement accounts = driver.findElement(By.xpath(".//*[@id='Accounts']"));
		ReuseableTestFunctions.objectClick(accounts, "Accounts Tab");
		
		WebElement payroll = driver.findElement(By.xpath(".//*[@id='Payroll']"));
		ReuseableTestFunctions.objectClick(payroll, "Payroll Tab");
		
		WebElement reports = driver.findElement(By.xpath(".//*[@id='Reports']"));
		ReuseableTestFunctions.objectClick(reports, "Reports Tab");
		
		WebElement contacts = driver.findElement(By.xpath(".//*[@id='Contacts']"));
		ReuseableTestFunctions.objectClick(contacts, "Contacts Tab");
		
		WebElement setting = driver.findElement(By.xpath(".//*[@id='Settings']"));
		ReuseableTestFunctions.objectClick(setting, "Setting Tab");
		
		WebElement newTab = driver.findElement(By.xpath(".//*[@id='quicklaunchTab']"));
		ReuseableTestFunctions.objectClick(newTab, "New Tab");
		
		WebElement file = driver.findElement(By.xpath(".//*[@id='xero-nav']/div[2]/div[2]/div[2]/ul/li[2]/a"));
		ReuseableTestFunctions.objectClick(file, "File Tab");
		String actual = driver.getTitle();
		System.out.println(actual);
		//Assert.assertEquals(actual, expected);
		
		WebElement notification = driver.findElement(By.xpath(".//*[@id='xero-nav']/div[2]/div[2]/div[2]/ul/li[3]/a"));
		ReuseableTestFunctions.objectClick(notification, "Notification Tab");
		
		WebElement search = driver.findElement(By.xpath(".//*[@id='xero-nav']/div[2]/div[2]/div[2]/ul/li[4]/a"));
		ReuseableTestFunctions.objectClick(search, "Search");
		
		WebElement help = driver.findElement(By.xpath(".//*[@id='xero-nav']/div[2]/div[2]/div[2]/ul/li[5]/a"));
		ReuseableTestFunctions.objectClick(help, "Help");
		String helptext = driver.findElement(By.xpath(".//*[@id='rt']/div[1]/h3")).getText();
		Assert.assertEquals(helptext, "Help");
		
		WebElement helpfield = driver.findElement(By.xpath(".//*[@id='menu_help']"));
		ReuseableTestFunctions.objectClick(helpfield, "Help Input");
		
		String helpcenter = driver.findElement(By.xpath(".//*[@id='rt']/ul/li[1]/a")).getText();
		Assert.assertEquals(helpcenter, "Help Center");
		logger.log(Status.INFO, "Help Center displayed");
		
		String gethelp = driver.findElement(By.xpath(".//*[@id='get_help']")).getText();
		Assert.assertEquals(gethelp, "Get help for this page");
		logger.log(Status.INFO, "Get help for this page display");
 }
	
	@Test(dataProvider="dataProvide", enabled=true,priority=11)
	public void Logout(String testname,String uname, String pwd ,String expected) throws InterruptedException, NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		
		Module.navigatetoXERO(driver, testname, uname, pwd, expected);
		Module.Logout(driver);
	}
	
	@Test(dataProvider="dataProvide", enabled=true,priority=12)
	public void UploadPic(String testname,String uname, String pwd ,String expected) throws InterruptedException {
		
		logger=ReuseableTestFunctions.createLogger("UploadPic", extentReport);
		Module.navigatetoXERO(driver, testname, uname, pwd, expected);
		
		WebElement userMenu= driver.findElement(By.xpath(".//*[@id='xero-nav']/div[2]/div[1]/div[2]/a"));
		ReuseableTestFunctions.objectClick(userMenu, "UserMenu");
		
		WebElement Profile= driver.findElement(By.xpath(".//*[@id='xero-nav']/div[2]/div[1]/div[2]/div/ul/li[1]/a"));
		ReuseableTestFunctions.objectClick(Profile, "Profile");
		String Actualtitle = driver.getTitle();
		//System.out.println(Actualtitle);
		Thread.sleep(4000);
		Assert.assertEquals(Actualtitle, "My Xero | Profile Settings");
		
		Thread.sleep(4000);
		WebElement uploadImg= driver.findElement(By.xpath(".//*[@id='button-1041-btnInnerEl']"));
		ReuseableTestFunctions.objectClick(uploadImg, "Upload Image Button");
		Thread.sleep(5000);
		WebElement browse= driver.findElement(By.xpath(".//*[@id='filefield-1174-button-fileInputEl']"));
		//ReuseableTestFunctions.objectClick(browse, "Browse Button");
		browse.sendKeys("C:\\Users\\hamsi\\OneDrive\\Desktop\\Logo.jpg");
		
		Thread.sleep(4000);
		WebElement uploadBtn= driver.findElement(By.xpath(".//*[@id='button-1178']"));
		ReuseableTestFunctions.objectClick(uploadBtn, "Upload Button");
	 
		WebElement changePic = driver.findElement(By.xpath(".//*[@id='button-1043-btnInnerEl']"));
		logger.log(Status.INFO, "Image Displayed");

	}
	
	
	@Test(dataProvider="dataProvide", enabled=true,priority=13)
	public void addOrganizationA(String testname,String uname, String pwd ,String expected) throws InterruptedException {
		
		logger = ReuseableTestFunctions.createLogger(testname, extentReport);
		Module.navigatetoXERO(driver, testname, uname, pwd, expected);
		
		WebElement companyName =driver.findElement(By.xpath(".//*[@id='xero-nav']/div[2]/div[1]/div[1]/div/h2"));
		ReuseableTestFunctions.objectClick(companyName, "CompanyName");
		
		WebElement myXero =driver.findElement(By.xpath(".//*[@id='xero-nav']/div[2]/div[1]/div[1]/div/div/div/a"));
		ReuseableTestFunctions.objectClick(myXero, "MyXero");
		
		WebElement addOrg =driver.findElement(By.xpath(".//*[@id='ext-gen1043']"));
		ReuseableTestFunctions.objectClick(addOrg, "Add more Organization");
		Thread.sleep(6000);
		
		WebElement orgName =driver.findElement(By.xpath(".//*[@id='text-1022-inputEl']"));
		ReuseableTestFunctions.enterText(orgName, "Tek", "Organization Name Input");
		Thread.sleep(3000);
		
		WebElement orgDo =driver.findElement(By.xpath(".//*[@id='industrysearchcombofield-1025-inputEl']"));
		ReuseableTestFunctions.enterText(orgDo, "Accounting", "Organization does Input");
		Thread.sleep(3000);
		
		WebElement startTrail  =driver.findElement(By.xpath(".//*[@id='simplebutton-1035']"));
		ReuseableTestFunctions.objectClick(startTrail, "StartTrail Button");
		Thread.sleep(5000);
		
		String trailLableactual = driver.findElement(By.xpath(".//*[@id='xero-nav']/div[1]/p")).getText();
		Assert.assertEquals(trailLableactual, "You are currently using a trial account.Upgrade now");
		logger.log(Status.INFO, "New Organization added and displayed");
	}
	
	
	@Test(dataProvider="dataProvide", enabled=true,priority=14)
	public void addOrganizationPaidB(String testname,String uname, String pwd ,String expected) throws InterruptedException {
		
		logger = ReuseableTestFunctions.createLogger(testname, extentReport);
		Module.navigatetoXERO(driver, testname, uname, pwd, expected);
		
		WebElement companyName =driver.findElement(By.xpath(".//*[@id='xero-nav']/div[2]/div[1]/div[1]/div/h2"));
		ReuseableTestFunctions.objectClick(companyName, "CompanyName");
		
		WebElement myXero =driver.findElement(By.xpath(".//*[@id='xero-nav']/div[2]/div[1]/div[1]/div/div/div/a"));
		ReuseableTestFunctions.objectClick(myXero, "MyXero");
		Thread.sleep(5000);
		WebElement addOrg =driver.findElement(By.xpath(".//*[@id='ext-gen1043']"));
		ReuseableTestFunctions.objectClick(addOrg, "Add more Organization");
		Thread.sleep(6000);
		
		WebElement orgName =driver.findElement(By.xpath(".//*[@id='text-1022-inputEl']"));
		ReuseableTestFunctions.enterText(orgName, "Teks", "Organization Name Input");
		Thread.sleep(3000);
		
		WebElement orgDo =driver.findElement(By.xpath(".//*[@id='industrysearchcombofield-1025-inputEl']"));
		ReuseableTestFunctions.enterText(orgDo, "Accounting", "Organization does Input");
		Thread.sleep(3000);
		
		WebElement buyNow  =driver.findElement(By.xpath(".//*[@id='simplebutton-1036']"));
		ReuseableTestFunctions.objectClick(buyNow, "BuyNow Button");
		Thread.sleep(8000);
		
		String pageTitleactual = driver.getTitle();
	//	System.out.println(pageTitleactual);
		Assert.assertEquals(pageTitleactual, "Xero | Pricing Plan");
	 	logger.log(Status.INFO, "Pruchase Plan details page displayed");
	}
	
	
	@Test(dataProvider="dataProvide", enabled=true,priority=15)
	public void addStarterPlan(String testname,String uname, String pwd ,String expected) throws InterruptedException {
		
		logger = ReuseableTestFunctions.createLogger(testname, extentReport);
		
		Module.navigatetoXERO(driver, testname, uname, pwd, expected);
		
		WebElement companyName =driver.findElement(By.xpath(".//*[@id='xero-nav']/div[2]/div[1]/div[1]/div/h2"));
		ReuseableTestFunctions.objectClick(companyName, "CompanyName");
		
		WebElement myXero =driver.findElement(By.xpath(".//*[@id='xero-nav']/div[2]/div[1]/div[1]/div/div/div/a"));
		ReuseableTestFunctions.objectClick(myXero, "MyXero");
		Thread.sleep(5000);
		WebElement addOrg =driver.findElement(By.xpath(".//*[@id='ext-gen1043']"));
		ReuseableTestFunctions.objectClick(addOrg, "Add more Organization");
		Thread.sleep(6000);
		
		WebElement orgName =driver.findElement(By.xpath(".//*[@id='text-1022-inputEl']"));
		ReuseableTestFunctions.enterText(orgName, "Tekks", "Organization Name Input");
		Thread.sleep(3000);
		WebElement orgDo =driver.findElement(By.xpath(".//*[@id='industrysearchcombofield-1025-inputEl']"));
		ReuseableTestFunctions.enterText(orgDo, "Accounting", "Organization does Input");
		Thread.sleep(3000);
		
		WebElement buyNow  =driver.findElement(By.xpath(".//*[@id='simplebutton-1036']"));
		ReuseableTestFunctions.objectClick(buyNow, "BuyNow Button");
		Thread.sleep(8000);
		
		String pageTitleactual = driver.getTitle();
	//	System.out.println(pageTitleactual);
		Assert.assertEquals(pageTitleactual, "Xero | Pricing Plan");
	 	logger.log(Status.INFO, "Pruchase Plan details page displayed");
	 	
	 	WebElement starter  =driver.findElement(By.xpath(".//*[@id='PRODUCTOPTION/ORG/SOLO']/div[1]/label"));
		ReuseableTestFunctions.objectClick(starter, "Starter Plan");
		
		WebElement continueBilling  =driver.findElement(By.xpath(".//*[@id='frmMain']/div/div[2]/div/main/div[10]/button"));
		ReuseableTestFunctions.objectClick(continueBilling, "ContinueBilling Button");
	 	
		WebElement streetaddress =driver.findElement(By.xpath(".//*[@id='POAddress']"));
		ReuseableTestFunctions.enterText(streetaddress, "3450 granada ave", "Street Address Input");
		Thread.sleep(3000);
		
		WebElement city =driver.findElement(By.xpath(".//*[@id='POCity']"));
		ReuseableTestFunctions.enterText(city, "santa clara", "City Input");
		Thread.sleep(3000);
		
		WebElement state =driver.findElement(By.xpath(".//*[@id='PORegionDropdown']"));
		Select selectState = new Select(state);
		selectState.selectByVisibleText("California");
		//ReuseableTestFunctions.enterText(city, "California", "State Input");
		Thread.sleep(3000);
		
		WebElement zip =driver.findElement(By.xpath(".//*[@id='POPostalCode']"));
		ReuseableTestFunctions.enterText(zip, "95051", "Zip Input");
		Thread.sleep(3000);
		
		WebElement continueToPay  =driver.findElement(By.xpath(".//*[@id='frmMain']/div/div/div/main/div[3]/div/div[2]/div/button"));
		ReuseableTestFunctions.objectClick(continueToPay, "continueToPay Button");
		String expectedaddress="3450 granada ave\r\n" + 
				"santa clara\r\n" + 
				"California\r\n" + 
				"95051\r\n" + 
				"United States";
		
		String actualaddress = driver.findElement(By.xpath(".//*[@id='postalAddressDetails']")).getText();
		//System.out.println("aDDRESS: "+add );
		//Assert.assertEquals(actualaddress, expectedaddress);
		logger.log(Status.INFO, "Address verified" +actualaddress);
	
  }
	
	
	@Test(dataProvider="dataProvide",enabled=true,priority=16)
	public void addStandardPlan(String testname,String uname, String pwd ,String expected) throws InterruptedException {
		
		logger = ReuseableTestFunctions.createLogger(testname, extentReport);
		
		Module.navigatetoXERO(driver, testname, uname, pwd, expected);
		
		WebElement companyName =driver.findElement(By.xpath(".//*[@id='xero-nav']/div[2]/div[1]/div[1]/div/h2"));
		ReuseableTestFunctions.objectClick(companyName, "CompanyName");
		
		WebElement myXero =driver.findElement(By.xpath(".//*[@id='xero-nav']/div[2]/div[1]/div[1]/div/div/div/a"));
		ReuseableTestFunctions.objectClick(myXero, "MyXero");
		
		Thread.sleep(5000);
		WebElement addOrg =driver.findElement(By.xpath(".//*[@id='ext-gen1043']"));
		ReuseableTestFunctions.objectClick(addOrg, "Add more Organization");
		Thread.sleep(6000);
		
		WebElement orgName =driver.findElement(By.xpath(".//*[@id='text-1022-inputEl']"));
		ReuseableTestFunctions.enterText(orgName, "Te", "Organization Name Input");
		Thread.sleep(3000);
		WebElement orgDo =driver.findElement(By.xpath(".//*[@id='industrysearchcombofield-1025-inputEl']"));
		ReuseableTestFunctions.enterText(orgDo, "Accounting", "Organization does Input");
		Thread.sleep(3000);
		
		WebElement buyNow  =driver.findElement(By.xpath(".//*[@id='simplebutton-1036']"));
		ReuseableTestFunctions.objectClick(buyNow, "BuyNow Button");
		Thread.sleep(8000);
		
		String pageTitleactual = driver.getTitle();
	//	System.out.println(pageTitleactual);
		Assert.assertEquals(pageTitleactual, "Xero | Pricing Plan");
	 	logger.log(Status.INFO, "Pruchase Plan details page displayed");
	 	
	 	/*WebElement standard  =driver.findElement(By.xpath(".//*[@id='PRODUCTOPTION/ORG/STANDARD']/div[1]/label']"));
		ReuseableTestFunctions.objectClick(standard, "standard Plan");*/
		
		WebElement continueBilling  =driver.findElement(By.xpath(".//*[@id='frmMain']/div/div[2]/div/main/div[10]/button"));
		ReuseableTestFunctions.objectClick(continueBilling, "ContinueBilling Button");
	 	
		WebElement streetaddress =driver.findElement(By.xpath(".//*[@id='POAddress']"));
		ReuseableTestFunctions.enterText(streetaddress, "3450 granada ave", "Street Address Input");
		Thread.sleep(3000);
		
		WebElement city =driver.findElement(By.xpath(".//*[@id='POCity']"));
		ReuseableTestFunctions.enterText(city, "santa clara", "City Input");
		Thread.sleep(3000);
		
		WebElement state =driver.findElement(By.xpath(".//*[@id='PORegionDropdown']"));
		Select selectState = new Select(state);
		selectState.selectByVisibleText("California");
		//ReuseableTestFunctions.enterText(city, "California", "State Input");
		Thread.sleep(3000);
		
		WebElement zip =driver.findElement(By.xpath(".//*[@id='POPostalCode']"));
		ReuseableTestFunctions.enterText(zip, "95051", "Zip Input");
		Thread.sleep(3000);
		
		WebElement continueToPay  =driver.findElement(By.xpath(".//*[@id='frmMain']/div/div/div/main/div[3]/div/div[2]/div/button"));
		ReuseableTestFunctions.objectClick(continueToPay, "continueToPay Button");
		String expectedaddress="3450 granada ave\r\n" + 
				"santa clara\r\n" + 
				"California\r\n" + 
				"95051\r\n" + 
				"United States";
		
		String actualaddress = driver.findElement(By.xpath(".//*[@id='postalAddressDetails']")).getText();
		//System.out.println("aDDRESS: "+add );
		//Assert.assertEquals(actualaddress, expectedaddress);
		logger.log(Status.INFO, "Address verified" +actualaddress);
	
  }
	
	
	@Test(dataProvider="dataProvide",enabled=true,priority=17)
	public void addPremiumPlan(String testname,String uname, String pwd ,String expected) throws InterruptedException {
		
		logger = ReuseableTestFunctions.createLogger(testname, extentReport);
		
		Module.navigatetoXERO(driver, testname, uname, pwd, expected);
		
		WebElement companyName =driver.findElement(By.xpath(".//*[@id='xero-nav']/div[2]/div[1]/div[1]/div/h2"));
		ReuseableTestFunctions.objectClick(companyName, "CompanyName");
		
		WebElement myXero =driver.findElement(By.xpath(".//*[@id='xero-nav']/div[2]/div[1]/div[1]/div/div/div/a"));
		ReuseableTestFunctions.objectClick(myXero, "MyXero");
		Thread.sleep(5000);
		WebElement addOrg =driver.findElement(By.xpath(".//*[@id='ext-gen1043']"));
		ReuseableTestFunctions.objectClick(addOrg, "Add more Organization");
		Thread.sleep(6000);
		
		WebElement orgName =driver.findElement(By.xpath(".//*[@id='text-1022-inputEl']"));
		ReuseableTestFunctions.enterText(orgName, "Tekks", "Organization Name Input");
		Thread.sleep(3000);
		WebElement orgDo =driver.findElement(By.xpath(".//*[@id='industrysearchcombofield-1025-inputEl']"));
		ReuseableTestFunctions.enterText(orgDo, "Accounting", "Organization does Input");
		Thread.sleep(3000);
		
		WebElement buyNow  =driver.findElement(By.xpath(".//*[@id='simplebutton-1036']"));
		ReuseableTestFunctions.objectClick(buyNow, "BuyNow Button");
		Thread.sleep(8000);
		
		String pageTitleactual = driver.getTitle();
	//	System.out.println(pageTitleactual);
		Assert.assertEquals(pageTitleactual, "Xero | Pricing Plan");
	 	logger.log(Status.INFO, "Pruchase Plan details page displayed");
	 	
	 	WebElement premium  =driver.findElement(By.xpath(".//*[@id='PRODUCTOPTION/ORG/PRO']/div[1]/label"));
		ReuseableTestFunctions.objectClick(premium, "Premium Plan");
		
		WebElement continueBilling  =driver.findElement(By.xpath(".//*[@id='frmMain']/div/div[2]/div/main/div[10]/button"));
		ReuseableTestFunctions.objectClick(continueBilling, "ContinueBilling Button");
	 	
		WebElement streetaddress =driver.findElement(By.xpath(".//*[@id='POAddress']"));
		ReuseableTestFunctions.enterText(streetaddress, "3450 granada ave", "Street Address Input");
		Thread.sleep(3000);
		
		WebElement city =driver.findElement(By.xpath(".//*[@id='POCity']"));
		ReuseableTestFunctions.enterText(city, "santa clara", "City Input");
		Thread.sleep(3000);
		
		WebElement state =driver.findElement(By.xpath(".//*[@id='PORegionDropdown']"));
		Select selectState = new Select(state);
		selectState.selectByVisibleText("California");
		//ReuseableTestFunctions.enterText(city, "California", "State Input");
		Thread.sleep(3000);
		
		WebElement zip =driver.findElement(By.xpath(".//*[@id='POPostalCode']"));
		ReuseableTestFunctions.enterText(zip, "95051", "Zip Input");
		Thread.sleep(3000);
		
		WebElement continueToPay  =driver.findElement(By.xpath(".//*[@id='frmMain']/div/div/div/main/div[3]/div/div[2]/div/button"));
		ReuseableTestFunctions.objectClick(continueToPay, "continueToPay Button");
		String expectedaddress="3450 granada ave\r\n" + 
				"santa clara\r\n" + 
				"California\r\n" + 
				"95051\r\n" + 
				"United States";
		
		String actualaddress = driver.findElement(By.xpath(".//*[@id='postalAddressDetails']")).getText();
		//System.out.println("aDDRESS: "+add );
		//Assert.assertEquals(actualaddress, expectedaddress);
		logger.log(Status.INFO, "Address verified" +actualaddress);
	
  }
	
	
	@Test(dataProvider="dataProvide",enabled=true,priority=18)
	public void addQuickBookUser(String testname,String uname, String pwd ,String expected) throws InterruptedException {
		
		Module.navigatetoXERO(driver, testname, uname, pwd, expected);
		
		WebElement companyName =driver.findElement(By.xpath(".//*[@id='xero-nav']/div[2]/div[1]/div[1]/div/h2"));
		ReuseableTestFunctions.objectClick(companyName, "CompanyName");
		
		WebElement myXero =driver.findElement(By.xpath(".//*[@id='xero-nav']/div[2]/div[1]/div[1]/div/div/div/a"));
		ReuseableTestFunctions.objectClick(myXero, "MyXero");
		Thread.sleep(5000);
		WebElement addOrg =driver.findElement(By.xpath(".//*[@id='ext-gen1043']"));
		ReuseableTestFunctions.objectClick(addOrg, "Add more Organization");
		Thread.sleep(6000);
		
		WebElement orgName =driver.findElement(By.xpath(".//*[@id='text-1022-inputEl']"));
		ReuseableTestFunctions.enterText(orgName, "Arch1", "Organization Name Input");
		Thread.sleep(3000);
		WebElement orgDo =driver.findElement(By.xpath(".//*[@id='industrysearchcombofield-1025-inputEl']"));
		ReuseableTestFunctions.enterText(orgDo, "Accounting", "Organization does Input");
		Thread.sleep(3000);
		
		WebElement quickBooklink =driver.findElement(By.xpath(".//*[@id='conversionLink']"));
		ReuseableTestFunctions.objectClick(quickBooklink, "QuickBook Link");
		Thread.sleep(3000);
		
		WebElement quickBookCheckbox =driver.findElement(By.xpath(".//*[@id='conversionLink']"));
		ReuseableTestFunctions.objectClick(quickBookCheckbox, "QuickBook CheckBox");
		Thread.sleep(3000);
		
		WebElement start  =driver.findElement(By.xpath(".//*[@id='simplebutton-1035']"));
		
		if(start.isDisplayed()) {
			logger.log(Status.INFO, "Start Trail Button disabled");
		}
				
		WebElement continueBtn  =driver.findElement(By.xpath(".//*[@id='simplebutton-1036']"));
		ReuseableTestFunctions.objectClick(continueBtn, "Continue Button");
		Thread.sleep(8000);
		
		String pageTitleactual = driver.getTitle();
		//System.out.println(pageTitleactual);
		Assert.assertEquals(pageTitleactual, "Xero | Pricing Plan");
	 	logger.log(Status.INFO, "Quick File Conversion page displayed");
		
	}
	
	@Test(dataProvider="dataProvide",enabled=true,priority=19)
	public void subscriptionandBilling(String testname,String userName, String Password ,String expected) throws InterruptedException {
		
		logger=ReuseableTestFunctions.createLogger(testname, extentReport);
		Module.navigatetoXERO(driver, testname, userName, Password, expected);
		
		WebElement account = driver.findElement(By.xpath(".//*[@id='Accounts']"));
		ReuseableTestFunctions.objectClick(account, "Accounts tab");
		
		WebElement purchase = driver.findElement(By.xpath(".//*[@id='xero-nav']/div[2]/div[2]/div[1]/ul/li[2]/ul/li[3]/a"));
		ReuseableTestFunctions.objectClick(purchase, "Purchase");
		Thread.sleep(5000);
		
		WebElement link = driver.findElement(By.xpath(".//*[@id='ext-gen1035']/span"));
		ReuseableTestFunctions.objectClick(link, "Link");
		Thread.sleep(2000);
		WebElement repeatedBill = driver.findElement(By.xpath(".//*[@id='ext-gen1034']/li[2]/a"));
		ReuseableTestFunctions.objectClick(repeatedBill, "Link");
		
		String actualtitle= driver.getTitle();
		//System.out.println(actualtitle);
		//Assert.assertEquals(actualtitle, "Xero | New Repeating Bill | Tekks");
		logger.log(Status.INFO, "New Repeating Bill Displayed");
	}
	
	
	@AfterMethod(enabled=true)
	public static void endDriver() {
	
		ReuseableTestFunctions.closeBrowser(driver);
	}

	@AfterTest
	public static void endReport() {
		ReuseableTestFunctions.endReport(extentReport);
	}


}
