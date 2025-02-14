import org.example.Main;
import org.testng.Assert;
import org.testng.annotations.*;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

public class MainTest extends Assert {
private final PrintStream orig = System.out;
private ByteArrayOutputStream console;
@BeforeTest
public void setUp(){
console = new ByteArrayOutputStream();
System.setOut(new PrintStream(console));
}
@AfterTest
public void tearDown() throws UnsupportedEncodingException {
System.out.close();
System.setOut(orig);
}
@AfterMethod
public void clear(){
console.reset();
}
@DataProvider
public Object[] testArgs(){
return new Object[][]{
        {new String[]{"127.0.0.1","22","random","random"},"Connection Error"},
        {new String[]{"127.0.0.1","22","random",},"Wrong Arguments"}
};
}
@Test(dataProvider = "testArgs")
public void TestWrongArgsMain(String[] args,String expected) throws UnsupportedEncodingException {
Main.main(args);
assertEquals (console.toString("UTF-8").trim(),expected);
}
}
