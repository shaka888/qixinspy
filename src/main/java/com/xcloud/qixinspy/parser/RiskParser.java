package com.xcloud.qixinspy.parser;

import java.util.NoSuchElementException;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class RiskParser {

    public String parse(String uuid, WebDriver driver) {
        try {
            WebElement element = driver.findElement(By.xpath("//*[@id=\"mainTab\"]/li[2]/a"));
            element.click();
        } catch (NoSuchElementException e) {
            e.printStackTrace();
        }
        return null;
    }

}