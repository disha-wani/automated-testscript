/**
 * 
 */
package com.uat.TestCases;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.uat.pages.LoginPage;
import com.uat.util.Xls_Reader;

/**
 * @author Admin
 *
 */
public class VerifyLoginFucntionality
{
		public Xls_Reader userXls;
	public VerifyLoginFucntionality()
	{
		if(System.getProperty("os.name").toUpperCase().startsWith("WINDOW"))
		{
			System.out.println("in Windows");
			userXls= new Xls_Reader(System.getProperty("user.dir")+"\\src\\com\\uat\\xls\\Crendiantial_User.xlsx");
		}
		else {
			System.out.println("in Ubuntu");
			userXls= new Xls_Reader(System.getProperty("user.dir")+"/src/com/uat/xls/Crendiantial_User.xlsx");
		}

	
	}
	public  WebDriver driver = null;
	
	@Test(dataProvider="getTestData")
	public void verifyValidLogin(String Username, String Password, String sText) throws InterruptedException
	{
		try{
//	System.setProperty("webdriver.firefox.driver", System.getProperty("user.dir")+"\\src\\com\\uat\\config\\firefox.exe");
//		driver = new InternetExplorerDriver(); 
		 driver = new FirefoxDriver();
		
		//driver.get("https://click2cloud.sharepoint.com/sites/UAT/TestV2.0/");
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		driver.get("http://uatvs-frontlayer-uatvs-frontlayer1.cloudapps.click2cloud.org/");
		
		
		LoginPage login = new LoginPage(driver);
		login.CloudLogin(Username, Password);
		
		driver.manage().window().maximize();	
		login.clicktestManagement();
		
		Thread.sleep(2000);
		
		By testManagement_Id= By.id("testMgnt");
		String testText = driver.findElement(testManagement_Id).getText();
		
		
		Assert.assertEquals(testText, sText);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		//driver.quit();
		
	}
	
	@AfterMethod
	public void CloseDriver()
	{
		driver.quit();
	}
	
	@DataProvider
	public Object[][] getTestData(){
		return getData(userXls, this.getClass().getSimpleName()) ;
	}
	
	public Object[][] getData(Xls_Reader xls , String testCaseName){
		// if the sheet is not present
		if(! xls.isSheetExist(testCaseName)){
			xls=null;
			return new Object[1][0];
		}
		
		
		int rows=xls.getRowCount(testCaseName);
		int cols=xls.getColumnCount(testCaseName);
		//System.out.println("Rows are -- "+ rows);
		//System.out.println("Cols are -- "+ cols);
		
	    Object[][] data =new Object[rows-1][cols];
		for(int rowNum=2;rowNum<=rows;rowNum++){
			for(int colNum=0;colNum<cols;colNum++){
				//System.out.print(xls.getCellData(testCaseName, colNum, rowNum) + " -- ");
				data[rowNum-2][colNum] = xls.getCellData(testCaseName, colNum, rowNum);
			}
			//System.out.println();
		}
		return data;
		
	}
}
