package mebius.hls.seleniumtest;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

public class PageReadyStateCheck implements com.google.common.base.Function<WebDriver, Boolean>
{
	public Boolean apply(WebDriver drv)
	{
		return ((JavascriptExecutor)drv).executeScript("return document.readyState").toString().equals("complete");
	}
}
