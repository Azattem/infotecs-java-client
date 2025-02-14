package org.example;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class JsonWorker {
    public static DataBaseSlice getData(InputStream inputStream){
        Scanner scanner = new Scanner(inputStream);
        DataBaseSlice data = new DataBaseSlice();
        String domain = "Null";
        String ip = "Null";
        while (scanner.hasNext()){
        String line = scanner.nextLine().trim();
        if(line.contains("\"domain\":")){
            domain = line.split("\"domain\": \"")[1].split("\"")[0];
        }
        if(line.contains("\"ip\":")){
            ip = line.split("\"ip\": \"")[1].split("\"")[0];
            data.add(domain,ip);
        }
        }
        return data;
    }
    public static DataBaseSlice getData(ChannelSftp channelSftp,String filename) throws SftpException, JSchException {
        channelSftp.connect();
        DataBaseSlice dataBaseSlice = getData(channelSftp.get(filename));
        channelSftp.disconnect();
    return dataBaseSlice;

    }
    public static void writeJsonFile(DataBaseSlice dataBaseSlice, OutputStream outputStream) throws IOException {
        OutputStreamWriter out = new OutputStreamWriter(outputStream);
        String text = "{\n" +
                        "\t\"addresses\": [\n";
        List<String[]> arrayList = dataBaseSlice.getAsSortedArray();
        for (int i = 0; i < arrayList.size()-1; i++) {
         text = text+ String.format("\t\t{\n" +
                 "\t\t\t\"domain\": \"%s\",\n" +
                 "\t\t\t\"ip\": \"%s\"\n" +
                 "\t\t},\n",arrayList.get(i)[0],arrayList.get(i)[1]);
        }
        text = text+ String.format("\t\t{\n" +
                "\t\t\t\"domain\": \"%s\",\n" +
                "\t\t\t\"ip\": \"%s\"\n" +
                "\t\t}\n" +
                "\t]\n" +
                "}",arrayList.get(arrayList.size()-1)[0],arrayList.get(arrayList.size()-1)[1]);
        out.write(text);
        out.flush();
        out.close();
    }

    public static void writeJsonFile(DataBaseSlice dataBaseSlice,ChannelSftp channelSftp,String filename) throws JSchException, SftpException, IOException {
    channelSftp.connect();
    writeJsonFile(dataBaseSlice,channelSftp.put(filename));
    channelSftp.disconnect();
    }
}
