package autoLoginReply;
import java.util.concurrent.TimeUnit;
import java.util.Calendar;
import java.util.regex.*;
import org.junit.*;
import static org.junit.Assert.*;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;

public class AutoLoginReply {
    private WebDriver driver;
    private String loginUrl;
    private boolean acceptNextAlert = true;
    private StringBuffer verificationErrors = new StringBuffer();

    @Before
    public void setUp() throws Exception {
        driver = new FirefoxDriver();
        loginUrl = "https://www.wacai.com/user/user.action?url=http%3A%2F%2Fbbs.wacai.com%2Fportal.php";
        driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
    }

    @Test
    public void testLogin() throws Exception {
        //open the login page
        driver.get(loginUrl);
        driver.findElement(By.id("account")).clear();
        driver.findElement(By.id("account")).sendKeys("***");
        driver.findElement(By.id("pwd")).clear();
        driver.findElement(By.id("pwd")).sendKeys("***");
        driver.findElement(By.id("login-btn")).click();

        //get the bbs portal page, find the specific forum href
        String a = driver.findElement(By.linkText("签到有礼")).getAttribute("href");
        driver.navigate().to(a);//get(a);
        //Thread title
        Calendar c = Calendar.getInstance();
        int m = c.get(Calendar.MONTH)+1;
        int d = c.get(Calendar.DAY_OF_MONTH);
        String dd = ""+d;
        if (d < 10)
            dd = "0"+dd;
        String threadTitle = "签到有礼"+m+"."+dd+"每天签到得铜钱";
        //System.out.println(threadTitle);
        //find today's check-in thread
        a =    driver.findElement(By.partialLinkText(threadTitle)).getAttribute("href");
        //System.out.println(a);
        driver.navigate().to(a);
        //using regrex to find the content we use to reply
        Pattern p = Pattern.compile("回帖内容必须为.+</font>非此内容将收回铜钱奖励");
        Matcher r = p.matcher(driver.getPageSource().toString());
        StringBuffer key = new StringBuffer();
        while(r.find()){
            key.append(r.group());
        }
        //using xpath locate the textarea and submit button
        driver.findElement(By.xpath("//textarea[@id='fastpostmessage']")).sendKeys(key.substring(key.indexOf(">")+1, key.indexOf("</")-1));
        //driver.manage().window().maximize();
        //System.out.println(driver.getPageSource());
        driver.findElement(By.xpath("//button[@id='fastpostsubmit']")).click();
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
