package ro.pub.cs.systems.pdsd.practicaltest02;

import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.ResponseHandler;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.impl.client.BasicResponseHandler;
import cz.msebera.android.httpclient.impl.client.HttpClientBuilder;

public class CommunicationThread extends Thread {

    private Socket socket;
    private EditText query;

    public CommunicationThread(Socket socket, EditText query) {
        this.socket = socket;
        this.query = query;
    }

    @Override
    public void run() {
        try {
            Log.v(Constants.TAG, "Connection opened to " + socket.getLocalAddress() + ":" + socket.getLocalPort()+ " from " + socket.getInetAddress());

            String word = query.getText().toString();
            String address = Constants.API_ADDRESS + word;

            HttpClient httpClient = HttpClientBuilder.create().build();
            HttpGet httpGet = new HttpGet(Constants.API_ADDRESS + query.getText().toString());
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            String result = httpClient.execute(httpGet, responseHandler);
            Log.d("RESULT TAG", result);

            PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
            printWriter.println(query.getText().toString());
            socket.close();
            Log.v(Constants.TAG, "Connection closed");
        } catch (IOException ioException) {
            Log.e(Constants.TAG, "An exception has occurred: " + ioException.getMessage());
            if (Constants.DEBUG) {
                ioException.printStackTrace();
            }
        }
    }

}
