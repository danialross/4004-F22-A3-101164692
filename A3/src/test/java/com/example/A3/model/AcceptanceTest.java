package com.example.A3.model;

import com.example.A3.A3Application;
import com.sun.tools.javac.Main;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class AcceptanceTest {


    WebDriver d1;
    WebDriver d2;
    WebDriver d3;
    WebDriver d4;

    @BeforeAll
    public static void setup(){
        WebDriverManager.firefoxdriver().setup();
    }

    @BeforeEach
    void init() throws Exception {
        d1 = new FirefoxDriver();
        d2 = new FirefoxDriver();
        d3 = new FirefoxDriver();
        d4 = new FirefoxDriver();

        d1.get("localhost:8090");
        d2.get("localhost:8090");
        d3.get("localhost:8090");
        d4.get("localhost:8090");

        Thread.sleep(500);

        d1.findElement(By.id("username")).sendKeys("player1");
		d1.findElement(By.id("submit1")).click();
        d2.findElement(By.id("username")).sendKeys("player2");
        d2.findElement(By.id("submit1")).click();
        d3.findElement(By.id("username")).sendKeys("player3");
        d3.findElement(By.id("submit1")).click();
        d4.findElement(By.id("username")).sendKeys("player4");
        d4.findElement(By.id("submit1")).click();

        Thread.sleep(500);

    }

    @AfterEach
    void stop(){

        d1.quit();
        d2.quit();
        d3.quit();
        d4.quit();


    }

    @Test
    public void row41() throws InterruptedException {
        d4.findElement(By.id("message")).sendKeys("TEST_ROW_41");
		d4.findElement(By.id("submit2")).click();

        Thread.sleep(500);

		d1.findElement(By.id("message")).sendKeys("3C");
		d1.findElement(By.id("submit2")).click();

		Thread.sleep(500);

		assertThat(d2.findElement(By.xpath("/html/body/div/div[3]/div/div/div/div[2]/div/div[6]/div[1]")).getText(),is("Current Card: 3C player2's turn"));
    }

    @Test
    void row77() throws InterruptedException {
        d4.findElement(By.id("message")).sendKeys("TEST_ROW_77");
        d4.findElement(By.id("submit2")).click();

        Thread.sleep(500);

        d2.findElement(By.id("message")).sendKeys("5S");
        d2.findElement(By.id("submit2")).click();
        assertThat(d2.findElement(By.xpath("/html/body/div/div[3]/div/div/div/div[2]/div/div[9]/div[1]\n")).getText(),is("Round has Ended, Player scores: player1 scored 1 Points, player2 scored 0 Points, player3 scored 86 Points, player4 scored 102 Points, Game has Ended, player2 is the Winner!"));

    }
}
