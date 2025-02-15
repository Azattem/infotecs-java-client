package org.example;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;

import java.io.IOException;

public class ClientService {
    private final ConnectionController controller;

    public ClientService(ConnectionController controller){
        this.controller = controller;
    }

    public String add(String domain,String ip){
        if(!isIpv4(ip)){
            return "Введите коректный ip адрес";
        }
        DataBaseSlice data = getData();
        if(data.containsDomain(domain)||data.containsIp(ip)){
            return "Введите уникальный Домен и ip адресс ";
        }
        data.add(domain,ip);

        return writeData(data);
    }

    public String getAll(){
        DataBaseSlice data = getData();
        StringBuilder s = new StringBuilder();
        for (String[] entry : data.getAsSortedArray()) {
                s.append(entry[0]).append(" ").append(entry[1]).append("\n");
        }
            return s.toString();
    }

    public String getByDomain(String Domain){
        String text = getData().getByDomain(Domain);
        if(text== null){
            return "Данные не найдены";
        }
        return text;
    }

    public String getByIp(String ip){
        String text = getData().getByIp(ip);
        if(text== null){
            return "Данные не найдены";
        }
        return text;

    }

    public String remove(String input){
        DataBaseSlice data = getData();
        if(isIpv4(input)) {
            if (!data.containsIp(input)) {
                return "Несодержит Ip " + input;
            }
            data.removeByIp(input);
            writeData(data);
            return "Ok";
        }else {
            if(!data.containsDomain(input)){
                return "Несодержит домейн "+input;
            }
            data.removeByDomain(input);
            writeData(data);
            return "Ok";
        }
    }

    public static boolean isIpv4(String address){
        if(address==null){
            return false;
        }
        String[] text = address.split("\\.");
        if(text.length != 4){
            return false;
        }
            for (String s : text) {
                try {
                    if (Integer.parseInt(s.trim()) < 0 || Integer.parseInt(s.trim()) > 255) {
                        return false;
                    }
                } catch (NumberFormatException e) {
                    return false;
                }
            }
        return true;
    }

    private DataBaseSlice getData(){
        try {
            return JsonWorker.getData(controller.getSftpChannel(),controller.getFileUrl());
        }catch (SftpException | JSchException e) {
            return new DataBaseSlice();
        }
    }

    private String writeData(DataBaseSlice data){
        try {
            JsonWorker.writeJsonFile(data,controller.getSftpChannel(),controller.getFileUrl());
            return "Ok";
        } catch (JSchException e) {
            return "Server Not Answering";
        } catch (SftpException e) {
            return "File not Found";
        } catch (IOException e) {
            return "Writing Error";
        }
    }
}

