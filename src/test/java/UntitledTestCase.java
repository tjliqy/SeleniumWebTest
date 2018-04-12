
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;

import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class UntitledTestCase {
    private WebDriver driver;
    private boolean acceptNextAlert = true;
    private StringBuffer verificationErrors = new StringBuffer();

    @Before
    public void setUp() throws Exception {
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
    }

    @Test
    public void testUntitledTestCase() throws Exception {

        XSSFWorkbook xwb = new XSSFWorkbook("/Users/tjliqy/Downloads/input.xlsx");
        XSSFSheet sheet = xwb.getSheetAt(0);
        XSSFRow row;
        String userName;
        String passWord;
        String gitHubUrl;
        String urlOnWeb;

        for (int i = sheet.getFirstRowNum(); i < sheet.getPhysicalNumberOfRows(); i++) {
            row = sheet.getRow(i);
            int cellNum = row.getFirstCellNum();

            if (row.getCell(cellNum).getCellTypeEnum().equals(CellType.NUMERIC)){
                userName = new BigDecimal(row.getCell(cellNum).getNumericCellValue()).toString();
            }else {
                userName = row.getCell(cellNum).getStringCellValue();
            }
            passWord = userName.substring(userName.length()-6,userName.length());
            gitHubUrl = row.getCell(cellNum+1).getStringCellValue();

            if (gitHubUrl != null && !gitHubUrl.equals("")) {
                driver.get("https://psych.liebes.top/st");
                driver.findElement(By.id("username")).click();
                driver.findElement(By.id("username")).clear();
                driver.findElement(By.id("username")).sendKeys(userName);
                driver.findElement(By.id("password")).click();
                driver.findElement(By.id("password")).clear();
                driver.findElement(By.id("password")).sendKeys(passWord);
                driver.findElement(By.id("submitButton")).click();
                urlOnWeb = driver.findElement(By.xpath("//p")).getText();
                System.out.println(userName + ": "+ urlOnWeb);
                assertEquals(formatUrl(gitHubUrl), formatUrl(urlOnWeb));
            }else {
                System.out.println(userName + ": Does not have gitHub Url");
            }
        }

    }

    @After
    public void tearDown() throws Exception {
        driver.quit();
        String verificationErrorString = verificationErrors.toString();
        if (!"".equals(verificationErrorString)) {
            fail(verificationErrorString);
        }
    }

    private String formatUrl(String url){
        if (url.endsWith("/")){
            return url.substring(0,url.length()-1).trim();
        }
        return url.trim();
    }
    private boolean isElementPresent(By by) {
        try {
            driver.findElement(by);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    private boolean isAlertPresent() {
        try {
            driver.switchTo().alert();
            return true;
        } catch (NoAlertPresentException e) {
            return false;
        }
    }

    private String closeAlertAndGetItsText() {
        try {
            Alert alert = driver.switchTo().alert();
            String alertText = alert.getText();
            if (acceptNextAlert) {
                alert.accept();
            } else {
                alert.dismiss();
            }
            return alertText;
        } finally {
            acceptNextAlert = true;
        }
    }
}
