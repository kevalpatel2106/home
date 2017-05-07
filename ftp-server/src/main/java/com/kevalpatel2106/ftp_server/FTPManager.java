package com.kevalpatel2106.ftp_server;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.text.format.Formatter;
import android.util.Log;

import org.apache.ftpserver.FtpServer;
import org.apache.ftpserver.FtpServerFactory;
import org.apache.ftpserver.ftplet.FtpException;
import org.apache.ftpserver.listener.ListenerFactory;
import org.apache.ftpserver.usermanager.PropertiesUserManagerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static android.content.Context.WIFI_SERVICE;

/**
 * Created by Keval Patel on 06/05/17.
 *
 * @author 'https://github.com/kevalpatel2106'
 */

public class FTPManager {

    private FtpServer mFtpServer;
    private int port = 20;// The port number
    private String ftpConfigDir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/ftpConfig/";

    public FTPManager(Context context) {
        File f = new File(ftpConfigDir);
        if (!f.exists()) f.mkdir();
        Log.d("File path", ftpConfigDir);
        copyResourceFile(context, R.raw.users, ftpConfigDir + "users.properties");
        copyResourceFile(context, R.raw.users, ftpConfigDir + "ftpserver.jks");
        Config1();
    }

    public String getLocalIpAddress(Context context) {
        WifiManager wm = (WifiManager) context.getSystemService(WIFI_SERVICE);
        return Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
    }

    private void copyResourceFile(Context context, int rid, String targetFile) {
        InputStream fin = context.getResources().openRawResource(rid);
        FileOutputStream fos = null;
        int length;
        try {
            fos = new FileOutputStream(targetFile);
            byte[] buffer = new byte[1024];
            while ((length = fin.read(buffer)) != -1) {
                fos.write(buffer, 0, length);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fin != null) {
                try {
                    fin.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void startServer(Context context) {
        File f = new File(ftpConfigDir);
        if (!f.exists()) f.mkdir();
        copyResourceFile(context, R.raw.users, ftpConfigDir + "users.properties");
        Config1();
    }

    void Config1() {
        FtpServerFactory serverFactory = new FtpServerFactory();
        ListenerFactory factory = new ListenerFactory();
        PropertiesUserManagerFactory userManagerFactory = new PropertiesUserManagerFactory();
        String[] str = {"mkdir", ftpConfigDir};
        try {
            Process ps = Runtime.getRuntime().exec(str);
            try {
                ps.waitFor();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        String filename = ftpConfigDir + "users.properties";
        File files = new File(filename);
        userManagerFactory.setFile(files);
        serverFactory.setUserManager(userManagerFactory.createUserManager());
        factory.setPort(port);
        try {
            serverFactory.addListener("default", factory.createListener());
            FtpServer server = serverFactory.createServer();
            this.mFtpServer = server;
            server.start();
        } catch (FtpException e) {
            e.printStackTrace();
        }
    }

    public void stopServer() {
        if (null != mFtpServer) {
            mFtpServer.stop();
            mFtpServer = null;
        }
    }
}
