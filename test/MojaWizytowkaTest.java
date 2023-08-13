package com.kodilla.testcontainers;


import org.junit.Rule;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testcontainers.containers.BrowserWebDriverContainer;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.shaded.org.apache.commons.io.FileUtils;
import org.testcontainers.utility.DockerImageName;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.testcontainers.containers.BrowserWebDriverContainer.VncRecordingMode.RECORD_ALL;

public class MojaWizytowkaTest {

    @Rule
    public Network network = Network.newNetwork();

    @Rule
    public GenericContainer webServer =
            new GenericContainer(
                    DockerImageName.parse("kodilla-httpd:latest")
                            .asCompatibleSubstituteFor("selenium/standalone-chrome"))

                    .withNetwork(network)
                    .withNetworkAliases("moja-wizytowka")
                    .withExposedPorts(80);

    @Rule
    public BrowserWebDriverContainer chrome =
            new BrowserWebDriverContainer<>()
                    .withNetwork(network)
                    .withRecordingMode(RECORD_ALL, new File("./build/"))
                    .withCapabilities(new ChromeOptions());

    @Test
    public void customImageTest() throws InterruptedException, IOException {
        RemoteWebDriver driver = chrome.getWebDriver();
        driver.get("http://moja-wizytowka/");

                File screenshot = driver.getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(screenshot, new File("./build/screenshots/" + screenshot.getName()));

        String title = driver.findElement(By.id("wiz")).getText();
        assertEquals("Moja wizytówka Kama", title);
    }
}