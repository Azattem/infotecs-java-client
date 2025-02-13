package org.example;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class ConnectionController {
    private final Session session;
    ConnectionController(String host,String port,String user,String password) throws JSchException {
        JSch jsch = new JSch();
        session = jsch.getSession(user, host,  Integer.parseInt(port));
        session.setPassword(password);
        java.util.Properties config = new java.util.Properties();
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config);
        session.connect();
    }

    public void Disconnect(){
    session.disconnect();
    }
    public ChannelSftp getSftpChannel() throws JSchException {
            return (ChannelSftp) session.openChannel("sftp");
    }
    public boolean isConnected(){
    return session.isConnected();
    }
}
