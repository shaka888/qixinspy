package com.xcloud.qixinspy;

import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SpyController {

    @Autowired
    private BasicInfoParser basicInfoParser;

    @RequestMapping(value = "/spy.do/{company}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String spy(@PathVariable String company, @RequestBody String json) {
        System.setProperty("webdriver.chrome.driver", "/Users/xienan/Desktop/chromedriver");
        WebDriver driver = null;
        try {
            driver = new ChromeDriver();
            driver.get("http://www.qixin.com/");
            driver.manage().window().maximize();
            List<Cookie> list = CookieParser.parse(json);
            for (Cookie e : list) {
                driver.manage().addCookie(e);
            }
            WebElement element = driver.findElement(By.id("searchBar"));
            element.sendKeys(company);
            element.submit();
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            element = driver.findElement(By.linkText(company));
            element.click();
            Set<String> allWindowsId = driver.getWindowHandles();
            for (String windowId : allWindowsId) {
                if (driver.switchTo().window(windowId).getTitle().contains("联系方式")) {
                    driver.switchTo().window(windowId);
                    break;
                }
            }
            String uuid = driver.findElement(By.xpath("//*[@id=\"eidHidden\"]")).getAttribute("value");
            basicInfoParser.parse(uuid, company, driver);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (driver != null) {
                driver.quit();
            }
        }
        return "ok";
    }

}