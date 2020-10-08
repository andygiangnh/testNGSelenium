package com.javacodegeeks.testng.selenium;

import java.net.URL;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class TestNGSeleniumBrowserCfgExample {
	private WebDriver driver;	

	@Parameters({"browser", "driverPath"})
	@BeforeTest
	public void initDriver(String browser, @Optional("") String driverPath) throws Exception {
		System.out.println("You are testing on browser " + browser);
		browser = browser.toLowerCase();
		if (!driverPath.equals("")) {
			System.setProperty("webdriver.chrome.driver", driverPath);
		}
		if (browser.equals("chrome")) {			
			//driver = new ChromeDriver();
			ChromeOptions chromeOptions = new ChromeOptions();
			chromeOptions.setCapability("browserVersion", "67");
			chromeOptions.setCapability("platformName", "Windows XP");
			driver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), chromeOptions);
		} else if (browser.equals("firefox")) {
			driver = new FirefoxDriver();
		} else {
			throw new RuntimeException("Please create a driver for " + browser);
		}
	}

	@Test(dataProvider = "searchStrings")
	public void searchGoogle(final String searchKey) {
		System.out.println("Search " + searchKey + " in google");
		driver.navigate().to("http://www.google.com");		
		WebElement element = driver.findElement(By.name("q"));
		System.out.println("Enter " + searchKey);
		element.sendKeys(searchKey);
		System.out.println("submit");
		element.submit();
		 (new WebDriverWait(driver, 10)).until(new ExpectedCondition<Boolean>() {
	            public Boolean apply(WebDriver d) {
	                return d.getTitle().toLowerCase().startsWith(searchKey.toLowerCase());
	            }
	        });
		System.out.println("Yay! We've got " + searchKey + " results");
	}

	@DataProvider
	private Object[][] searchStrings() {
		return new Object[][] { { "TestNG" }, { "Selenium" } };
	}

	@AfterTest
	public void quitDriver() throws Exception {
		driver.quit();
	}	
}
