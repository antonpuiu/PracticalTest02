package ro.pub.cs.systems.pdsd.practicaltest02;

import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.InetAddress;
import java.net.Socket;

public class ServerThread extends Thread {
    public static boolean isRunning;
    private ServerSocket serverSocket;
    private EditText query;
    private int port;

    static {
        isRunning = false;
    }

    public ServerThread(EditText query, int port) {
        this.query = query;
        this.port = port;
    }

    public void startServer() {
        isRunning = true;
        start();
        Log.v(Constants.TAG, "startServer() method was invoked");
    }

    public void stopServer() {
        isRunning = false;
        try {
            serverSocket.close();
        } catch (IOException ioException) {
            if (Constants.DEBUG) {
                ioException.printStackTrace();
            }
        }
        Log.v(Constants.TAG, "stopServer() method was invoked");
    }

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(port,50, InetAddress.getByName("0.0.0.0"));

            while (isRunning) {
                Socket socket = serverSocket.accept();
                Log.v(Constants.TAG, "accept()-ed: " + socket.getInetAddress());
                if (socket != null) {
                    CommunicationThread communicationThread = new CommunicationThread(socket, query);
                    communicationThread.start();
                }
            }
        } catch (IOException ioException) {
            Log.e(Constants.TAG, "An exception has occurred: " + ioException.getMessage());
            if (Constants.DEBUG) {
                ioException.printStackTrace();
            }
        }
    }
}
