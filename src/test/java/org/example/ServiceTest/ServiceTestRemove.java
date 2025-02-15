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

public class ServiceTestRemove extends Assert {
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
                {"first.domain","Ok"},
                {"192.168.0.4","Ok"},
                {"first.domain","Несодержит домейн first.domain"},
                {"192.168.0.4","Несодержит Ip 192.168.0.4"},
        };
    }
    @Test(dataProvider = "input")
    public void RemoveTest(String input,String expected){
    ClientService clientService = new ClientService(controller);
    assertEquals(clientService.remove(input),expected);
    }

}
