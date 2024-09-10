package org.auto;

import org.openqa.selenium.*;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import io.github.bonigarcia.wdm.WebDriverManager;


import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String[] args) {
        try {
            //Launch Edge using the desktop shortcut with remote debugging enabled
            String desktopIconPath = "C:\\Users\\sushant.raj\\Desktop\\Profile 1 - Edge.lnk"; // Update with your path
            String command = String.format("cmd /c start \"\" \"%s\" --remote-debugging-port=9222", desktopIconPath);
            Runtime.getRuntime().exec(command);

            // Wait for Edge to fully launch
            Thread.sleep(5000); // Adjust the sleep time as needed

            //Set up Edge WebDriver to connect to the existing Edge instance
            WebDriverManager.edgedriver().setup();
            EdgeOptions options = new EdgeOptions();
            options.setExperimentalOption("debuggerAddress", "localhost:9222");

            WebDriver driver = new EdgeDriver(options);

            // Ensure WebDriver is connected to the existing session
            if (driver.getWindowHandles().isEmpty()) {
                throw new RuntimeException("No browser window found. Ensure the Edge browser is running with remote debugging enabled.");
            }

            // List of 30 countries
            List<String> countries = Arrays.asList(
                    "United States", "Canada", "Brazil", "United Kingdom", "France",
                    "Germany", "Italy", "Spain", "Mexico", "Argentina",
                    "China", "India", "Japan", "South Korea", "Australia",
                    "New Zealand", "Russia", "South Africa", "Egypt", "Nigeria",
                    "Turkey", "Saudi Arabia", "Indonesia", "Thailand", "Vietnam",
                    "Pakistan", "Bangladesh", "Philippines", "Malaysia", "Singapore"
            );

            // Base URL to search country names
            String baseUrl = "https://www.bing.com/search?q=";
            Random random = new Random();

            try {
                int count = 1;
                for (String country : countries) {
                    // Open a new tab and switch to it using JavaScriptExecutor
                    ((JavascriptExecutor) driver).executeScript("window.open();");
                    Set<String> handles = driver.getWindowHandles();
                    String newTabHandle = handles.toArray()[handles.size() - 1].toString();
                    driver.switchTo().window(newTabHandle);


                    WebElement searchBox = driver.findElement(By.name("q")); // the search box has the name "q"

                    // Type each character of the country name separately with added delay
                    for (char ch : country.toCharArray()) {
                        searchBox.sendKeys(String.valueOf(ch));
                        Thread.sleep(150);  // Adding a small delay to simulate real typing
                    }

                    // Simulate pressing Enter to search
                    searchBox.sendKeys(Keys.RETURN);

                    // Build the search URL for the country
                    String searchUrl = baseUrl + country.replace(" ", "+");

                    // Navigate to the search URL
                    driver.get(searchUrl);

                    // Wait for the page to load completely (you can adjust the timeout if needed)
                    driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);

                    // Optionally, print the title of the loaded page
                    System.out.println("Loaded: " + driver.getTitle() + " " + count++);

                    // Wait for a random period to mimic human behavior (between 2-5 seconds)
                    Thread.sleep(2000 + random.nextInt(3000));

                    // Close the current tab
//                    driver.close();

                    // Switch back to the first available window
                    driver.switchTo().window(driver.getWindowHandles().iterator().next());
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                // Quit the browser
                driver.quit();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
