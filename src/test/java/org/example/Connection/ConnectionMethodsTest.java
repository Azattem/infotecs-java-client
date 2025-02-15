package org.example.Connection;

import com.jcraft.jsch.JSchException;
import org.example.ConnectionController;
import org.testng.Assert;
import org.testng.annotations.*;

public class ConnectionMethodsTest extends Assert {
private ConnectionController controller;
private String fileUrl;
@Parameters({"host","port","user","password","fileUrl"})
@BeforeClass
public void setup(String host,String port,String user,String password,String fileUrl) throws JSchException {
    controller = new ConnectionController(host,port,user,password,fileUrl);
    this.fileUrl = fileUrl;
}
@AfterClass
public void cleanup(){
    if(controller.isConnected()) {
        controller.disconnect();
    }
}
@Test(priority = 0)
public void isConnectedTest(){
    assertTrue(controller.isConnected());
}
@Test(priority = 1)
public void getUrlTest(){
    assertEquals(controller.getFileUrl(),fileUrl);
}
@Test(priority = 2)
public void getSftpChannelTest() throws JSchException {
    assertNotNull(controller.getSftpChannel());
}
@Test(priority = 3)
public void disconnectTest(){
    controller.disconnect();
    assertFalse(controller.isConnected());
}





}
