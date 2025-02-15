package org.example.ServiceTest;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import org.example.ClientService;
import org.example.ConnectionController;
import org.example.JsonWorker;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.io.IOException;

public class ServiceTestGetAll extends Assert {
private ConnectionController controller;
@Parameters({"host","port","user","password","fileUrl"})
@BeforeClass
public void setup(String host,String port,String user,String password,String fileUrl) throws JSchException {
    controller= new ConnectionController(host, port, user, password, fileUrl);
}
@AfterClass
public void cleanup() throws JSchException, SftpException, IOException {
    controller.disconnect();
}
@Test
public void getAllTest(){
assertEquals(new ClientService(controller).getAll(),"first.domain 192.168.0.1\n" +
        "fourth.domain 192.168.0.4\n" +
        "second.domain 192.168.0.2\n" +
        "third.domain 192.168.0.3\n");
}
}
