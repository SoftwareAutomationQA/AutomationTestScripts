package autoModularFramework;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import org.testng.annotations.DataProvider;
import org.testng.collections.Lists;

public class DataProviderMain {

	
	@DataProvider
	public static String[][] dataProvide(Method m) throws IOException{
		System.out.println("DataFirst "+m.getName());
		String path ="XeroSelenium/src/test/java/utility/Data.xls";
		String[][] excelData = ReuseableTestFunctions.readData(path,"Sheet1",m.getName());

		return excelData;
	}
	
	@DataProvider
	public static String[][] getData(Method m) throws IOException{
		System.out.println("Datasecond "+m.getName());
		String path ="XeroSelenium/src/test/java/utility/OrgData.xls";
		String[][] excelData = ReuseableTestFunctions.readOrgData(path,"Sheet1",m.getName());

		return excelData;

	}
	
	@DataProvider
	public String[][] data(Method m) throws IOException {
		  String[][] data1= dataProvide(m);
		String[][] data2 = getData(m);
	    
		List<String[]> result = Lists.newArrayList();
	  result.addAll(Arrays.asList(data1));
	 result.addAll(Arrays.asList(data2));
	  return result.toArray(new String[result.size()][]);
	}
}
