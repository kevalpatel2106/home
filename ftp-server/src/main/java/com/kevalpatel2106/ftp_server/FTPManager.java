package com.kevalpatel2106.ftp_server;

import android.content.Context;
import android.util.Log;

import com.stericson.RootShell.exceptions.RootDeniedException;
import com.stericson.RootShell.execution.Command;
import com.stericson.RootTools.RootTools;

import org.apache.ftpserver.FtpServer;
import org.apache.ftpserver.FtpServerFactory;
import org.apache.ftpserver.ftplet.FtpException;
import org.apache.ftpserver.listener.ListenerFactory;
import org.apache.ftpserver.usermanager.PropertiesUserManagerFactory;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.concurrent.TimeoutException;

/**
 * Created by Keval Patel on 06/05/17.
 *
 * @author 'https://github.com/kevalpatel2106'
 */

public class FTPManager {
    private static final int PORT_NUMBER = 53705;// The PORT_NUMBER number
    private static final String TAG = FTPManager.class.getSimpleName();

    private FtpServer mFtpServer;

    /**
     * Initialize the FTP server
     *
     * @param context instance of the caller.
     * @see 'http://stackoverflow.com/a/42474815/4690731'
     */
    public FTPManager(Context context) {
        try {
            mountDrive();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        } catch (RootDeniedException | TimeoutException e) {
            e.printStackTrace();
            return;
        }

        //Copy the resources.
        copyResourceFile(context, R.raw.users, context.getExternalCacheDir().getAbsolutePath() + "/users.properties");
        copyResourceFile(context, R.raw.ftpserver, context.getExternalCacheDir().getAbsolutePath() + "/ftpserver.jks");
    }


    public String getLocalIpAddress() {
        try {
            Enumeration<NetworkInterface> enumNetworkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (enumNetworkInterfaces.hasMoreElements()) {
                Enumeration<InetAddress> enumInetAddress = enumNetworkInterfaces.nextElement().getInetAddresses();
                while (enumInetAddress.hasMoreElements()) {
                    InetAddress inetAddress = enumInetAddress.nextElement();
                    if (inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Copy all the resources from assets to local file storage.
     *
     * @param context    instance of the caller.
     * @param rid        Assets id.
     * @param targetFile target file to copy.
     */
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

    /**
     * Start the FTP server.
     */
    public void startServer(Context context) {
        //Set the user factory
        PropertiesUserManagerFactory userManagerFactory = new PropertiesUserManagerFactory();
        String filename = context.getExternalCacheDir().getAbsolutePath() + "/users.properties";
        File files = new File(filename);
        userManagerFactory.setFile(files);

        //Set the server factory
        FtpServerFactory serverFactory = new FtpServerFactory();
        serverFactory.setUserManager(userManagerFactory.createUserManager());

        //Set the port number
        ListenerFactory factory = new ListenerFactory();
        factory.setPort(PORT_NUMBER);
        try {
            serverFactory.addListener("default", factory.createListener());
            FtpServer server = serverFactory.createServer();
            mFtpServer = server;

            //Start the server
            server.start();
        } catch (FtpException e) {
            e.printStackTrace();
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "onCreate: FTP server started. IP address: " + getLocalIpAddress() + ":" + PORT_NUMBER);
            }
        }).start();
    }

    /**
     * Stop the FTP server.
     */
    public void stopServer() {
        if (null != mFtpServer) {
            mFtpServer.stop();
            mFtpServer = null;
        }
    }

    private void mountDrive() throws IOException, TimeoutException, RootDeniedException {
        RootTools.debugMode = true; //ON
        RootTools.log("Starting mount process...");

        if (RootTools.isRootAvailable()) {
            if (RootTools.isAccessGiven()) {

                Command command = new Command(0, "whoami",
                        "mkdir /mnt/usb",
                        "mount -t vfat -o rw /dev/block/sda1 /mnt/usb\n") {
                    @Override
                    public void commandOutput(int id, String line) {
                        super.commandOutput(id, line);
                        RootTools.log(line);
                    }

                    @Override
                    public void commandTerminated(int id, String reason) {
                        super.commandTerminated(id, reason);
                    }

                    @Override
                    public void commandCompleted(int id, int exitcode) {
                        super.commandCompleted(id, exitcode);
                    }
                };
                RootTools.getShell(true).add(command);
            }else {
                RootTools.log("This application is not having root access...");
            }
        } else {
            RootTools.log("Root access not available...");
        }
    }
}