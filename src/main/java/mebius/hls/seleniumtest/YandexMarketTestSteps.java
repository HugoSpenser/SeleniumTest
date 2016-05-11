package mebius.hls.seleniumtest;

import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import ru.yandex.qatools.allure.annotations.Step;

import static mebius.hls.seleniumtest.YandexMarketConstants.*;
import static org.openqa.selenium.support.ui.ExpectedConditions.*;
import static org.junit.Assert.assertEquals;

public class YandexMarketTestSteps
{	
	private static WebDriver drv;
	private static Wait<WebDriver> wait;
	private static WebElement vendorListTop, vendorListOthers;
	private static PageReadyStateCheck readyStateCheckFunc;
	private static List<WebElement> searchResultsList;
	
	public YandexMarketTestSteps()
	{
		readyStateCheckFunc = new PageReadyStateCheck();
		drv = new FirefoxDriver();
		wait = new WebDriverWait(drv, 15, 1000);
	}
	
	@Step("Переход на главную страницу яндекса")
	public void openStartingPage()
	{
		drv.get("http://yandex.ru");
		drv.manage().window().maximize();
	}
	
	@Step("Переход в раздел маркета {0}, подраздел {1}")
	public void goToMarketSubDepartment(String departmentName, String subDepartmentName)
	{
		clickElWithCssSelector("[data-id=market]");
		clickElWithCssSelector("[data-department=" + departmentName + "]");
		drv.findElement(By.linkText(subDepartmentName)).click();
	}
	
	@Step("Установка значений поисковых фильтров")
	public void applySearchFilters(String lowerPrice, String higherPrice, String... vendorIds)
	{
		drv.findElement(By.partialLinkText("Расширенный поиск")).click();
		if (!isStrEmpty(lowerPrice))
			setFromPriceFilter(lowerPrice);
		if (!isStrEmpty(higherPrice))
			setToPriceFilter(higherPrice);
		setVendorLists();
		checkVendors(vendorIds);
		clickElWithCssSelector(".button_action_filter-apply");
		wait.until(presenceOfElementLocated(By.className("filter-applied-results_state_loading")));
		wait.until(presenceOfAllElementsLocatedBy(By.className("snippet-card")));
	}
	
	@Step("Проверка количества найденных товаров на странице. Должно быть {0}")
	public void checkNumberOfSearchResults(int resultsNumber)
	{
		searchResultsList = drv.findElement(By.className("filter-applied-results")).findElements(By.className("snippet-card"));
		assertEquals("After applying search filters there must be exactly 10 items on the page", searchResultsList.size(), resultsNumber);		
	}
	
	
	@Step("Запоминаем наименование первого товара")
	public String getFirstResultTitle()
	{
		return searchResultsList.get(0).findElement(By.className("snippet-card__header-text")).getText();
	}
	
	@Step("Поиск: {0}")
	public void searchFor(String searchQuery)
	{
		WebElement searchInp = drv.findElement(By.id("header-search"));
		String curUrl = drv.getCurrentUrl();
		searchInp.sendKeys(searchQuery);
		searchInp.submit();
		wait.until(not(urlToBe(curUrl)));
		checkPageIsReady();
	}
	
	@Step("Проверка сответствия имени товара значению {0}")
	public void checkProductTitleEqualityToString(String str)
	{
		assertEquals("Title of the object page must be equal to stored value",
				str, drv.findElement(By.className("title_changeable_yes")).getText());		
	}
	
	@Step("Установка нижнего значения цены: {0}")
	public void setFromPriceFilter(String priceValue)
	{
		drv.findElement(By.id(FROM_PRICE_INPUT_ID)).sendKeys(priceValue);
	}
	
	@Step("Установка верхнего значения цены: {0}")
	public void setToPriceFilter(String priceValue)
	{
		drv.findElement(By.id(TO_PRICE_INPUT_ID)).sendKeys(priceValue);
	}
	
	@Step("Выбор производителей")
	public void checkVendors(String ... vendorIds)
	{
		for (String vendorId : vendorIds)
			checkVendor(vendorId);
	}
	
	@Step("id = {0}")
	public void checkVendor(String vendorId)
	{
		try {
			vendorListTop.findElement(By.id(vendorId)).click();
		} catch (org.openqa.selenium.NoSuchElementException ex) {
			WebElement moreButton;
			if ((moreButton = drv.findElement(By.className("button-vendors__others"))) != null && !moreButton.getAttribute("style").contains("display: none"))
				moreButton.click();
			vendorListOthers.findElement(By.id(vendorId)).click();
		}
	}
	
	public void closeBrowser()
	{
		drv.quit();
	}
	
	//Вспомогательные методы

	private boolean isStrEmpty(String str)
	{
		return (str == null || str.isEmpty());
	}
	
	private void checkPageIsReady()
	{
		wait.until(readyStateCheckFunc);
	}
	
	private void clickElWithCssSelector(String selector)
	{
		drv.findElement(By.cssSelector(selector)).click();
	}
	
	private void setVendorLists()
	{
		vendorListTop = drv.findElement(By.className("vendors-list__top"));
		vendorListOthers = drv.findElement(By.className("vendors-list__others"));
	}
}
