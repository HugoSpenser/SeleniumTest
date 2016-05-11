package mebius.hls.seleniumtest;

import org.junit.*;
import ru.yandex.qatools.allure.annotations.Title;

import static mebius.hls.seleniumtest.YandexMarketConstants.*;

/**
 * @author Andrey Ogoltsov hellstorm@interzet.ru
 */

@Title("Тестирование веб-интерфейса раздела ноутбуков яндекс.маркета")
public class LaptopTest {
	
	private YandexMarketTestSteps steps;
	
	@Before
	public void openBrowser()
	{
		steps = new YandexMarketTestSteps();
		steps.openStartingPage();
	}
	
	@Test
	@Title("Ноутбуки")
	public void testLaptops()
	{
		steps.goToMarketSubDepartment("Компьютеры", "Ноутбуки");		
		steps.applySearchFilters("", "30000", VENDOR_ID_HP, VENDOR_ID_LENOVO);
		steps.checkNumberOfSearchResults(10);		
		String firstElName = steps.getFirstResultTitle();		
		steps.searchFor(firstElName);
		steps.checkProductTitleEqualityToString(firstElName);
	}
	
	@After
	public void closeBrowser()
	{
		steps.closeBrowser();
	}
	
}