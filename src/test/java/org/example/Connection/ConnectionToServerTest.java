package org.example.Connection;

import com.jcraft.jsch.JSchException;
import org.example.ConnectionController;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class ConnectionToServerTest extends Assert {

@Parameters({"host","port","user","password","fileUrl"})
@Test
public void testConnection(String host,String port,String user,String password,String fileUrl) throws JSchException {
ConnectionController controller= new ConnectionController(host, port, user, password, fileUrl);
assertTrue(controller.isConnected());
controller.disconnect();
}


}
