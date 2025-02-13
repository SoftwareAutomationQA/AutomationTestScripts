package autoModularFramework;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentReporter;

import io.github.bonigarcia.wdm.WebDriverManager;

public class ReuseableTestFunctions {

	static WebDriver driver;
	public static ExtentReporter htmlReport;
	public static ExtentReports extentReport;
	public static ExtentTest logger;

	//Reading Data from Excel TestData
	public static String[][] readDataFromXl(String dataTablePath, String sheetName) throws IOException{
		//String cur_dir = System.getProperty("user.dir");
		FileInputStream fs = new FileInputStream (new File(dataTablePath));

		HSSFWorkbook wb= new HSSFWorkbook(fs);
		HSSFSheet sheet=wb.getSheet(sheetName);
		int trow= sheet.getLastRowNum()+1;
		int tcol=sheet.getRow(0).getLastCellNum();

		String[][] str=new String[trow][tcol];
		for(int i=0;i<trow;i++){
			for(int j=0;j<tcol;j++){
				str[i][j]=sheet.getRow(i).getCell(j).getStringCellValue();

			}
		}
		/*for(int i=0;i<str.length;i++){
				for(int j=0;j<str[0].length;j++){
					System.out.print(str[i][j]+" ");
				}
				System.out.println();

			}
			System.out.println();*/
		return str;
	}


	//Reading Data from Excel Data
	public static String[][] readData(String dataTablePath, String sheetName,String mName) throws IOException{
		//String cur_dir = System.getProperty("user.dir");
		FileInputStream fs = new FileInputStream (new File(dataTablePath));
		HSSFWorkbook wb= new HSSFWorkbook(fs);
		HSSFSheet sheet=wb.getSheet(sheetName);
		int trow= sheet.getLastRowNum()+1;
		int tcol=sheet.getRow(0).getLastCellNum();
		String[][] str=new String[1][4];
		int c=0;
		for(int i=1;i<trow;i++){
			/*	System.out.println(mName);
						System.out.println(sheet.getRow(i).getCell(0).getStringCellValue());*/
			if(sheet.getRow(i).getCell(0).getStringCellValue().equalsIgnoreCase(mName)) {

				for(int j=0;j<tcol;j++){
					//							System.out.println(sheet.getRow(i).getCell(j).getStringCellValue());
					str[c][j]=sheet.getRow(i).getCell(j).getStringCellValue();
					//System.out.println("Hello"+str[0][j]);

				}
				break;
			}
		}
		return str;
	}


	//Reading Organization Data from Excel Data
	public static String[][] readOrgData(String dataTablePath, String sheetName,String mName) throws IOException{
		//String cur_dir = System.getProperty("user.dir");
		FileInputStream fs = new FileInputStream (new File(dataTablePath));
		HSSFWorkbook wb= new HSSFWorkbook(fs);
		HSSFSheet sheet=wb.getSheet(sheetName);
		int trow= sheet.getLastRowNum()+1;
		int tcol=sheet.getRow(0).getLastCellNum();
		String[][] str=new String[1][2];
		int c=0;
		for(int i=1;i<trow;i++){
			/*	System.out.println(mName);
						System.out.println(sheet.getRow(i).getCell(0).getStringCellValue());*/
			if(sheet.getRow(i).getCell(1).getStringCellValue().equalsIgnoreCase(mName)) {

				for(int j=0;j<tcol;j++){
					//							System.out.println(sheet.getRow(i).getCell(j).getStringCellValue());
					str[c][j]=sheet.getRow(i).getCell(j).getStringCellValue();
					//System.out.println("Hello"+str[0][j]);

				}
				break;
			}
		}
		return str;
	}


	//Method to Launch different Browser 
	public static WebDriver launchBrowser(String browserName) {

		if(browserName.equalsIgnoreCase("firefoxBrowser")) {
			WebDriverManager.firefoxdriver().setup();
			driver= new FirefoxDriver();
		}
		else if(browserName.equalsIgnoreCase("chromeBrowser")) {
			WebDriverManager.chromedriver().setup();
			driver= new ChromeDriver();
			driver.manage().window().maximize();
		}
		else if(browserName.equalsIgnoreCase("ieBrowser")) {
			WebDriverManager.iedriver().setup();
			/*DesiredCapabilities caps = DesiredCapabilities.internetExplorer();
			caps.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS,true);*/
			driver = new InternetExplorerDriver(); 
			//driver= new InternetExplorerDriver();
		}
		//driver.manage().timeouts().implicitlyWait(80,TimeUnit.SECONDS);
		return driver;
	}


	//To Close the Browser
	public static void closeBrowser(WebDriver dr) {
		dr.quit();
	}


	//To Generate the ExtentReports

	public static ExtentReports generateReport() {
		String dateName = new SimpleDateFormat("MM-dd-yyyy-hh-mm-ss").format(new Date());
		//htmlReport = new ExtentHtmlReporter(System.getProperty("user.dir")+"/HtmlReports/"+dateName+"_Reports.html");
		//System.out.println(htmlReport);
		extentReport = new ExtentReports();
		extentReport.attachReporter();

		extentReport.setSystemInfo("Host Name", "Xero.Com");
		extentReport.setSystemInfo("Environment", "Automation Testing");
		extentReport.setSystemInfo("User Name", "User1");

		//htmlReport.config().setDocumentTitle("Testing Report");
		//htmlReport.config().setReportName("General Report");

		return extentReport;
	}

	//Method to Create log
	public static ExtentTest createLogger(String tcName,ExtentReports er) {
		logger = er.createTest(tcName);		
		return logger;
	}


	//To Flush or End the ExtentReport
	public static void endReport(ExtentReports er)
	{
		er.flush();
	}


	//To Enter Text in TextBox Element
	public static void enterText(WebElement element,String textValue,String objName) {
		if(element.isDisplayed()) {
			element.sendKeys(textValue);
			//	System.out.println("Pass: "+textValue+ " is entered in " +objName+ " field");
			TestScriptsMain.logger.log(Status.INFO, "Pass: "+textValue+ " is entered in " +objName+ " field");

		}else
		{
			//System.out.println("Fail: "+objName+ " filed does not exist. Please Check your application");
			TestScriptsMain.logger.log(Status.INFO, "Fail: "+objName+ " filed does not exist. Please Check your application");


		}
	}

	//To Click the Button Object
	public static void objectClick(WebElement element, String objName) {
		if(element.isDisplayed()) {
			element.click();
			//System.out.println("Pass: "+objName+" is clicked");
			TestScriptsMain.logger.log(Status.INFO, "Pass: "+objName+" is clicked");
		}else
		{
			//System.out.println("Fail : " + objName + " is not displayed, please check your application");
			TestScriptsMain.logger.log(Status.INFO, "Fail: "+objName+ " is not displayed,Please Check your application");

		}
	}



}
