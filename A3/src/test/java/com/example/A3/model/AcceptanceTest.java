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


    static WebDriver d1;
    static WebDriver d2;
    static WebDriver d3;
    static WebDriver d4;

    @BeforeAll
    public static void setup() throws InterruptedException {
        WebDriverManager.firefoxdriver().setup();

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
        Thread.sleep(500);
        d1.findElement(By.id("submit1")).click();
        Thread.sleep(500);
        d2.findElement(By.id("username")).sendKeys("player2");
        Thread.sleep(500);
        d2.findElement(By.id("submit1")).click();
        Thread.sleep(500);
        d3.findElement(By.id("username")).sendKeys("player3");
        Thread.sleep(500);
        d3.findElement(By.id("submit1")).click();
        Thread.sleep(500);
        d4.findElement(By.id("username")).sendKeys("player4");
        Thread.sleep(500);
        d4.findElement(By.id("submit1")).click();
        Thread.sleep(500);
    }

    @Test
    public void row41() throws InterruptedException {

        d4.findElement(By.id("message")).sendKeys("TEST_ROW_41");
		d4.findElement(By.id("submit2")).click();
        Thread.sleep(2000);

		d1.findElement(By.id("message")).sendKeys("3C");
		d1.findElement(By.id("submit2")).click();
        Thread.sleep(2000);

		assertThat(d2.findElement(By.xpath("//*[@id=\"chat\"]/div[6]/div[1]")).getText(),is("Current Card: 3C player2's turn"));

        d4.findElement(By.id("message")).sendKeys("RESET_SERVER");
        d4.findElement(By.id("submit2")).click();
        Thread.sleep(2000);

    }
    //p1 plays 1H assert next player is player 4 AND interface must show now playing in opposite direction (i.e., going right)
    //        player4 plays 7H and next player is player 3

    @Test
    void row42() throws InterruptedException {

        d4.findElement(By.id("message")).sendKeys("TEST_ROW_42");
        d4.findElement(By.id("submit2")).click();
        Thread.sleep(2000);

        d1.findElement(By.id("message")).sendKeys("AH");
        d1.findElement(By.id("submit2")).click();
        Thread.sleep(2000);

        assertThat(d4.findElement(By.xpath("/html/body/div/div[3]/div/div/div/div[2]/div/div[7]/div[1]")).getText(),is("Current Direction is Right"));
        assertThat(d4.findElement(By.xpath("/html/body/div/div[3]/div/div/div/div[2]/div/div[8]/div[1]")).getText(),is("Current Card: AH player4's turn"));

        d4.findElement(By.id("message")).sendKeys("7H");
        d4.findElement(By.id("submit2")).click();
        Thread.sleep(2000);

        assertThat(d3.findElement(By.xpath("/html/body/div/div[3]/div/div/div/div[2]/div/div[6]/div[1]")).getText(),is("Current Card: 7H player3's turn"));

        d4.findElement(By.id("message")).sendKeys("RESET_SERVER");
        d4.findElement(By.id("submit2")).click();
        Thread.sleep(2000);
    }


   @Test
    public void row45() throws InterruptedException {
        d4.findElement(By.id("message")).sendKeys("TEST_ROW_45");
        d4.findElement(By.id("submit2")).click();
        Thread.sleep(2000);

        d4.findElement(By.id("message")).sendKeys("3C");
        d4.findElement(By.id("submit2")).click();
        Thread.sleep(2000);

        assertThat(d1.findElement(By.xpath("/html/body/div/div[3]/div/div/div/div[2]/div/div[19]/div[1]")).getText(),is("Current Card: 3C player1's turn"));

        d4.findElement(By.id("message")).sendKeys("RESET_SERVER");
        d4.findElement(By.id("submit2")).click();
        Thread.sleep(2000);
    }

    @Test
    public void row46() throws InterruptedException {
        d4.findElement(By.id("message")).sendKeys("TEST_ROW_46");
        d4.findElement(By.id("submit2")).click();
        Thread.sleep(2000);

        d4.findElement(By.id("message")).sendKeys("AH");
        d4.findElement(By.id("submit2")).click();
        Thread.sleep(2000);
        assertThat(d3.findElement(By.xpath("/html/body/div/div[3]/div/div/div/div[2]/div/div[9]/div[1]")).getText(),is("Current Direction is Right"));
        assertThat(d3.findElement(By.xpath("/html/body/div/div[3]/div/div/div/div[2]/div/div[10]/div[1]")).getText(),is("Current Card: AH player3's turn"));

        d3.findElement(By.id("message")).sendKeys("7H");
        d3.findElement(By.id("submit2")).click();
        Thread.sleep(2000);

        assertThat(d2.findElement(By.xpath("/html/body/div/div[3]/div/div/div/div[2]/div/div[11]/div[1]")).getText(),is("Current Card: 7H player2's turn"));

        d4.findElement(By.id("message")).sendKeys("RESET_SERVER");
        d4.findElement(By.id("submit2")).click();
        Thread.sleep(2000);
    }

    @Test
    public void row48() throws InterruptedException {
        d4.findElement(By.id("message")).sendKeys("TEST_ROW_48");
        d4.findElement(By.id("submit2")).click();
        Thread.sleep(2000);


        d4.findElement(By.id("message")).sendKeys("QC");
        d4.findElement(By.id("submit2")).click();
        Thread.sleep(2000);

        assertThat(d2.findElement(By.xpath("/html/body/div/div[3]/div/div/div/div[2]/div/div[13]/div[1]")).getText(),is("a Queen was played, the next player's turn has been skipped"));
        assertThat(d2.findElement(By.xpath("/html/body/div/div[3]/div/div/div/div[2]/div/div[15]/div[1]")).getText(),is("Current Card: QC player2's turn"));

        d4.findElement(By.id("message")).sendKeys("RESET_SERVER");
        d4.findElement(By.id("submit2")).click();
        Thread.sleep(2000);
    }

    @Test
    public void row51() throws InterruptedException {
        d4.findElement(By.id("message")).sendKeys("TEST_ROW_51");
        d4.findElement(By.id("submit2")).click();
        Thread.sleep(2000);


        d1.findElement(By.id("message")).sendKeys("KH");
        d1.findElement(By.id("submit2")).click();
        Thread.sleep(2000);

        assertThat(d1.findElement(By.xpath("/html/body/div/div[3]/div/div/div/div[2]/div/div[26]/div[1]")).getText(),is("Player turn ended"));

        d4.findElement(By.id("message")).sendKeys("RESET_SERVER");
        d4.findElement(By.id("submit2")).click();
        Thread.sleep(2000);
    }
    @Test
    public void row52() throws InterruptedException {
        d4.findElement(By.id("message")).sendKeys("TEST_ROW_51");
        d4.findElement(By.id("submit2")).click();
        Thread.sleep(2000);


        d1.findElement(By.id("message")).sendKeys("7C");
        d1.findElement(By.id("submit2")).click();
        Thread.sleep(2000);

        assertThat(d1.findElement(By.xpath("/html/body/div/div[3]/div/div/div/div[2]/div/div[26]/div[1]")).getText(),is("Player turn ended"));

        d4.findElement(By.id("message")).sendKeys("RESET_SERVER");
        d4.findElement(By.id("submit2")).click();
        Thread.sleep(2000);
    }

    @Test
    public void row54() throws InterruptedException {
        d4.findElement(By.id("message")).sendKeys("TEST_ROW_54");
        d4.findElement(By.id("submit2")).click();
        Thread.sleep(2000);


        d1.findElement(By.id("message")).sendKeys("5S");
        d1.findElement(By.id("submit2")).click();
        Thread.sleep(2000);

        assertThat(d1.findElement(By.xpath("/html/body/div/div[3]/div/div/div/div[2]/div/div[34]/div[1]")).getText(),is("You Cannot Play That Card, Try Again"));

        d4.findElement(By.id("message")).sendKeys("RESET_SERVER");
        d4.findElement(By.id("submit2")).click();
        Thread.sleep(2000);
    }
//    @Test
//    void row77() throws InterruptedException {
//        d4.findElement(By.id("message")).sendKeys("TEST_ROW_77");
//        d4.findElement(By.id("submit2")).click();
//
//        Thread.sleep(500);
//
//        d2.findElement(By.id("message")).sendKeys("5S");
//        d2.findElement(By.id("submit2")).click();
//        assertThat(d2.findElement(By.xpath("/html/body/div/div[3]/div/div/div/div[2]/div/div[9]/div[1]\n")).getText(),is("Round has Ended, Player scores: player1 scored 1 Points, player2 scored 0 Points, player3 scored 86 Points, player4 scored 102 Points, Game has Ended, player2 is the Winner!"));
//
//    }
}
