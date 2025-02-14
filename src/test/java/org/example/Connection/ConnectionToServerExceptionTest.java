package org.example.Connection;

import com.jcraft.jsch.JSchException;
import org.example.ConnectionController;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class ConnectionToServerExceptionTest {
    @Parameters({"host","port","user","password","fileUrl"})
    @Test(expectedExceptions = JSchException.class)
    public void testConnectionWrongHost(String host,String port,String user,String password,String fileUrl) throws JSchException {
        ConnectionController controller= new ConnectionController("150.0.0.123", port, user, password, fileUrl);
        controller.disconnect();
    }
    @Parameters({"host","port","user","password","fileUrl"})
    @Test(expectedExceptions = JSchException.class)
    public void testConnectionWrongPort(String host,String port,String user,String password,String fileUrl) throws JSchException {
        ConnectionController controller= new ConnectionController(host, "56", user, password, fileUrl);
        controller.disconnect();
    }
    @Parameters({"host","port","user","password","fileUrl"})
    @Test(expectedExceptions = JSchException.class)
    public void testConnectionWrongName(String host,String port,String user,String password,String fileUrl) throws JSchException {
        ConnectionController controller= new ConnectionController(host, port, "Dulac", password, fileUrl);
        controller.disconnect();
    }
    @Parameters({"host","port","user","password","fileUrl"})
    @Test(expectedExceptions = JSchException.class)
    public void testConnectionWrongPass(String host,String port,String user,String password,String fileUrl) throws JSchException {
        ConnectionController controller= new ConnectionController(host, port, user, "213183", fileUrl);
        controller.disconnect();
    }



}