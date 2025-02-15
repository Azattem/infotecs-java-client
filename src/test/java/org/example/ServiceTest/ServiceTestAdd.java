package org.example.ServiceTest;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import org.example.ClientService;
import org.example.ConnectionController;
import org.example.DataBaseSlice;
import org.example.JsonWorker;
import org.testng.Assert;
import org.testng.annotations.*;

import java.io.IOException;

public class ServiceTestAdd extends Assert {
    public ConnectionController controller;
    private DataBaseSlice orginalData;
    @Parameters({"host","port","user","password","fileUrl"})
    @BeforeClass
    public void setup(String host,String port,String user,String password,String fileUrl) throws JSchException, SftpException {
        controller= new ConnectionController(host, port, user, password, fileUrl);
        orginalData = JsonWorker.getData(controller.getSftpChannel(),fileUrl);
    }
    @AfterClass
    public void cleanup() throws JSchException, SftpException, IOException {
        JsonWorker.writeJsonFile(orginalData,controller.getSftpChannel(),controller.getFileUrl());
        controller.disconnect();
    }
    @DataProvider
    public Object[]input(){
    return new Object[][]{
            {"1.Domain","192.168.0.10", "Ok",true},
            {"1.Domain","192.168.0.12", "Введите уникальный Домен и ip адресс ",false},
            {"2.Domain","192.168.0.10", "Введите уникальный Домен и ip адресс ",false},
            {"2.Domain","192.168.0","Введите коректный ip адрес",false},
    };
    }
    @Test(dataProvider = "input",priority = 0)
    public void addTestMessage(String domain,String ip,String expected,boolean result){
    ClientService clientService = new ClientService(controller);
    assertEquals(clientService.add(domain,ip),expected);

    }
    @Test(dataProvider = "input",priority = 1)
    public void addTestFunc(String domain,String ip,String expected,boolean result){
        ClientService clientService = new ClientService(controller);
        assertEquals(clientService.getByIp(ip).equals(domain)&&clientService.getByDomain(domain).equals(ip),result);
    }

}
