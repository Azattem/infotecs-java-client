package org.example.FileWorker;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import org.example.ConnectionController;
import org.example.DataBaseSlice;
import org.example.JsonWorker;
import org.testng.Assert;
import org.testng.annotations.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class JsonWorkerWriteTest extends Assert {
    private ConnectionController controller;
    private DataBaseSlice example;
    private DataBaseSlice original;
    @DataProvider
    Object[] outputDataStreams() {
        return new Object[][]{
                {"{\n" +
                        "\t\"addresses\": [\n" +
                        "\t\t{\n" +
                        "\t\t\t\"domain\": \"23.domain\",\n" +
                        "\t\t\t\"ip\": \"192.168.0.20\"\n" +
                        "\t\t},\n" +
                        "\t\t{\n" +
                        "\t\t\t\"domain\": \"24.domain\",\n" +
                        "\t\t\t\"ip\": \"192.168.0.22\"\n" +
                        "\t\t},\n" +
                        "\t\t{\n" +
                        "\t\t\t\"domain\": \"first.domain\",\n" +
                        "\t\t\t\"ip\": \"192.168.0.1\"\n" +
                        "\t\t}\n" +
                        "\t]\n" +
                        "}"}
        };
    }

    @Parameters({"host","port","user","password","fileUrl"})
    @BeforeClass
    public void setup(String host,String port,String user,String password,String fileUrl) throws JSchException, SftpException {
        controller= new ConnectionController(host, port, user, password, fileUrl);
        original = JsonWorker.getData(controller.getSftpChannel(),fileUrl);
        example = new DataBaseSlice();
        example.add("23.domain","192.168.0.20");
        example.add("24.domain","192.168.0.22");
        example.add("first.domain","192.168.0.1");
    }
    @AfterClass
    public void cleanup() throws JSchException, SftpException, IOException {
        JsonWorker.writeJsonFile(original,controller.getSftpChannel(), controller.getFileUrl());
        controller.disconnect();
    }
    @Test(dataProvider = "outputDataStreams")
    public void JsonWorkerWriteNoConnection(String expected) throws IOException {
    ByteArrayOutputStream array =  new ByteArrayOutputStream();
    JsonWorker.writeJsonFile(example,array);
    assertEquals(array.toString(),expected);
    }
    @Test
    public void JsonWorkerWriteConnection() throws JSchException, SftpException, IOException {
    JsonWorker.writeJsonFile(example,controller.getSftpChannel(),controller.getFileUrl());
    assertEquals(JsonWorker.getData(controller.getSftpChannel(),controller.getFileUrl()).getAsSortedArray().toArray(),new String[][]{new String[]{"23.domain","192.168.0.20"},new String[]{"24.domain","192.168.0.22"},new String[]{"first.domain","192.168.0.1"}});
    }




}
