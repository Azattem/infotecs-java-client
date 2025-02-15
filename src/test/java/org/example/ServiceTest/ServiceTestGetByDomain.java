package org.example.ServiceTest;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import org.example.ClientService;
import org.example.ConnectionController;
import org.testng.Assert;
import org.testng.annotations.*;

import java.io.IOException;

public class ServiceTestGetByDomain extends Assert {
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
    @DataProvider
    public Object[]input(){
        return new Object[][]{
                {"first.domain","192.168.0.1"},
                {"Wrong.domain","Данные не найдены"},
                {"wrongdata","Данные не найдены"}
        };
    }
    @Test(dataProvider = "input")
    public void getByDomainTest(String data,String expected){
        assertEquals(new ClientService(controller).getByDomain(data),expected);
    }
}
