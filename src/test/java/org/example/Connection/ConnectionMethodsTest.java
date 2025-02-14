package org.example.Connection;

import com.jcraft.jsch.JSchException;
import org.example.ConnectionController;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class ConnectionMethodsTest extends Assert {
private ConnectionController controller;
private final String host ="127.0.0.1";
private final String port ="22";
private final String user ="azatt";
private final String password = "2002";
private final String fileUrl = "serverdataTest.json";
@Parameters({"host","port","user","password","fileUrl"})
@BeforeTest
public void setup(String host,String port,String user,String password,String fileUrl) throws JSchException {
    controller = new ConnectionController(host,port,user,password,fileUrl);
}
@AfterTest
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
