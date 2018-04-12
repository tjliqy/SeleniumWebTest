# Selenium TEST

## 关卡说明

1. 安装SeleniumIDE插件
2. 学会使用SeleniumIDE录制脚本和导出脚本
3. 访问https://psych.liebes.top/st使用学号登录系统（账户名为学号，密码为学号后6位），进入系统后可以看到该同学的git地址。
4. 编写Selenium JavaWebDriver程序，测试input.xlsx表格中的学号和git地址的对应关系是否正确。
5. 将测试代码提交到github上（4月15日23:59:59前）。

## 流程攻略

### 一、安装SeleniumIDE插件并录制导出脚本

1. 从Chrome应用市场安装Katalon（SeleniumIDE For Chrome）

2. 打开Katalon，点击Record按钮。使用chrome访问https://psych.liebes.top/st使用学号登录系统

3. 回到Katalon，点击Stop按钮，再点击Export按钮。在弹出框中将format选择为`Java(WebDriver + Junit)`，可以得到脚本代码

   ```java
   package com.example.tests;

   import java.util.regex.Pattern;
   import java.util.concurrent.TimeUnit;
   import org.junit.*;
   import static org.junit.Assert.*;
   import static org.hamcrest.CoreMatchers.*;
   import org.openqa.selenium.*;
   import org.openqa.selenium.firefox.FirefoxDriver;
   import org.openqa.selenium.support.ui.Select;

   public class UntitledTestCase {
     private WebDriver driver;
     private String baseUrl;
     private boolean acceptNextAlert = true;
     private StringBuffer verificationErrors = new StringBuffer();

     @Before
     public void setUp() throws Exception {
       driver = new FirefoxDriver();
       baseUrl = "https://www.katalon.com/";
       driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
     }

     @Test
     public void testUntitledTestCase() throws Exception {
       driver.get("https://psych.liebes.top/st");
       driver.findElement(By.id("username")).click();
       driver.findElement(By.id("username")).clear();
       driver.findElement(By.id("username")).sendKeys("xxxxxxxxx");//xxxx代表用户名
       driver.findElement(By.id("password")).clear();
       driver.findElement(By.id("password")).sendKeys("xxxxxx");//xxx代表密码
       driver.findElement(By.id("submitButton")).click();
       driver.findElement(By.xpath("//p")).click();
     }

     @After
     public void tearDown() throws Exception {
       driver.quit();
       String verificationErrorString = verificationErrors.toString();
       if (!"".equals(verificationErrorString)) {
         fail(verificationErrorString);
       }
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
   ```



### 二、安装ChromeWebDriver并构建项目

1. 安装ChromeWebDriver：[MacOs](https://www.jianshu.com/p/e137031bc7db)/[Windows](https://blog.csdn.net/u013360850/article/details/54962248)(因为这两篇博客中已经讲得很清楚，所以不再赘述)

2. 使用IDEA新建Maven项目

3. 在pom.xml中引入selenium、Junit、POI的依赖

   ```Xml
       <dependencies>
           <dependency>
               <groupId>org.seleniumhq.selenium</groupId>
               <artifactId>selenium-java</artifactId>
               <version>2.28.0</version>
           </dependency>
           <dependency>
               <groupId>com.opera</groupId>
               <artifactId>operadriver</artifactId>
           </dependency>
           <!-- https://mvnrepository.com/artifact/junit/junit -->
           <dependency>
               <groupId>junit</groupId>
               <artifactId>junit</artifactId>
               <version>4.11</version>
               <scope>test</scope>
           </dependency>
           <!-- https://mvnrepository.com/artifact/org.apache.poi/poi-ooxml -->
           <dependency>
               <groupId>org.apache.poi</groupId>
               <artifactId>poi-ooxml</artifactId>
               <version>3.17</version>
           </dependency>


       </dependencies>
       <dependencyManagement>
           <dependencies>
               <dependency>
                   <groupId>com.opera</groupId>
                   <artifactId>operadriver</artifactId>
                   <version>1.1</version>
                   <exclusions>
                       <exclusion>
                           <groupId>org.seleniumhq.selenium</groupId>
                           <artifactId>selenium-remote-driver</artifactId>
                       </exclusion>
                   </exclusions>
               </dependency>
           </dependencies>
       </dependencyManagement>
   ```

4. 在*项目路径/src/test/java/*中新建测试类UntitledTestCase，将从Katalon中导出的代码复制到该类中



### 三、编写测试逻辑

修改`testUntitledTestCase()`方法中代码。（在这里不具体贴出来了）

#### 思路

1. 使用POI从xlsx中读取信息
2. 对于每一条信息，使用selenium得到网站上Github Url并与从xlsx得到的做比较（`assertEqual()`)



## 坑！！！！！

1. #### xlsx中学号的数据，数据类型不统一！

   有的是普通字符串，有的是数字。如果是数字的会用科学计数法表示，但这明显不是我们想要的。

   **解决办法**：在读取的时候先判断一下这个格子中数据的类型，根据类型做不同的处理

   ```Java
   if(row.getCell(cellNum).getCellTypeEnum().equals(CellType.NUMERIC)){
   	userName = new BigDecimal(row.getCell(cellNum).getNumericCellValue()).toString();
   }else {
   	userName = row.getCell(cellNum).getStringCellValue();
   }
   ```

2. #### xlsx中url与网上的url格式不统一

   比如xlsx中是https://github.com/xxx/但是网上是https://github.com/xxx（结尾少个**/**)或者结尾多个空格之类的

   **解决办法**：对从两个来源获取的url做统一的格式化处理

   ```Java
       private String formatUrl(String url){
           if (url.endsWith("/")){
               return url.substring(0,url.length()-1).trim();
           }
           return url.trim();
       }
   ```

3. #### xlsx中有一个哥们他喵的没有githubURL！！

   **解决办法**：很简单，判断一下URL是否为空，如果是空的跳过丫的就好了。



## 通关画面

![](/Users/tjliqy/AndroidStudioProjects/SeleniumWebDriver/result.png)