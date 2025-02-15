package org.example.FileWorker;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import org.example.ConnectionController;
import org.example.JsonWorker;
import org.testng.Assert;
import org.testng.annotations.*;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

public class JsonWorkerGetTest extends Assert {
private ConnectionController controller;
@Parameters({"host","port","user","password","fileUrl"})
@BeforeClass
public void setup(String host,String port,String user,String password,String fileUrl) throws JSchException {
controller= new ConnectionController(host, port, user, password, fileUrl);
}
@AfterTest
public void cleanup(){
controller.disconnect();
}
@DataProvider
Object[] inputDataStreams(){
return new Object[][]{
        {new ByteArrayInputStream(("{\n" +
                        "\t\"addresses\": [\n" +
                        "\t\t{\n" +
                        "\t\t\t\"domain\": \"first.domain\",\n" +
                        "\t\t\t\"ip\": \"192.168.0.1\"\n" +
                        "\t\t},\n" +
                        "\t\t{\n" +
                        "\t\t\t\"domain\": \"fourth.domain\",\n" +
                        "\t\t\t\"ip\": \"192.168.0.4\"\n" +
                        "\t\t},\n" +
                        "\t\t{\n" +
                        "\t\t\t\"domain\": \"second.domain\",\n" +
                        "\t\t\t\"ip\": \"192.168.0.2\"\n" +
                        "\t\t},\n" +
                        "\t\t{\n" +
                        "\t\t\t\"domain\": \"third.domain\",\n" +
                        "\t\t\t\"ip\": \"192.168.0.3\"\n" +
                        "\t\t}\n" +
                        "\t]\n" +
                        "}").getBytes(StandardCharsets.UTF_8)),new String[][]{new String[]{"first.domain","192.168.0.1"},new String[]{"fourth.domain","192.168.0.4"},new String[]{"second.domain","192.168.0.2"},new String[]{"third.domain","192.168.0.3"}
                        }},
        {new ByteArrayInputStream(("{\n" +
                "\t\"addresses\": [\n" +
                "\t]\n" +
                "}").getBytes(StandardCharsets.UTF_8)),new String[][]{}}};
}

@Test(dataProvider = "inputDataStreams")
public void getDataTestNoConnection(ByteArrayInputStream inputStream,String[][] expected){
assertEquals(JsonWorker.getData(inputStream).getAsSortedArray().toArray(),expected);
}
@Parameters({"fileUrl"})
@Test
public void getDataTestWithConnection(String fileUrl) throws JSchException, SftpException {
assertEquals(JsonWorker.getData(controller.getSftpChannel(),fileUrl).getAsSortedArray().toArray(),new String[][]{new String[]{"first.domain","192.168.0.1"},new String[]{"fourth.domain","192.168.0.4"},new String[]{"second.domain","192.168.0.2"},new String[]{"third.domain","192.168.0.3"}});
}
}
