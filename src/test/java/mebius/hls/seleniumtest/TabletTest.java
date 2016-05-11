package mebius.hls.seleniumtest;

import org.junit.*;
import ru.yandex.qatools.allure.annotations.Title;

import static mebius.hls.seleniumtest.YandexMarketConstants.*;

/**
 * @author Andrey Ogoltsov hellstorm@interzet.ru
 */

@Title("Тестирование веб-интерфейса раздела планшетов яндекс.маркета")
public class TabletTest {
	
	private YandexMarketTestSteps steps;
	
	@Before
	public void openBrowser()
	{
		steps = new YandexMarketTestSteps();
		steps.openStartingPage();
	}
	
	@Test
	@Title("Планшеты")
	public void testTablets()
	{
		steps.goToMarketSubDepartment("Компьютеры", "Планшеты");		
		steps.applySearchFilters("20000", "25000", VENDOR_ID_ACER, VENDOR_ID_DELL);
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