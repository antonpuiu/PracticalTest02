package ro.pub.cs.systems.pdsd.practicaltest02;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClientAsyncTask extends AsyncTask<String, String, Void> {

    private TextView serverMessageTextView;

    public ClientAsyncTask(TextView serverMessageTextView) {
        this.serverMessageTextView = serverMessageTextView;
    }

    @Override
    protected Void doInBackground(String... params) {
        Socket socket = null;
        try {
            String serverAddress = params[0];
            int serverPort = Integer.parseInt(params[1]);
            socket = new Socket(serverAddress, serverPort);
            if (socket == null) {
                return null;
            }
            Log.v(Constants.TAG, "Connection opened with " + socket.getInetAddress() + ":" + socket.getLocalPort());
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));;
            String currentLine;
            while ((currentLine = bufferedReader.readLine()) != null) {
                publishProgress(currentLine);
            }
        } catch (IOException ioException) {
            Log.e(Constants.TAG, "An exception has occurred: " + ioException.getMessage());
            if (Constants.DEBUG) {
                ioException.printStackTrace();
            }
        } finally {
            try {
                if (socket != null) {
                    socket.close();
                }
                Log.v(Constants.TAG, "Connection closed");
            } catch (IOException ioException) {
                Log.e(Constants.TAG, "An exception has occurred: " + ioException.getMessage());
                if (Constants.DEBUG) {
                    ioException.printStackTrace();
                }
            }
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        serverMessageTextView.setText("");
    }

    @Override
    protected void onProgressUpdate(String... progress) {
        serverMessageTextView.append(progress[0] + "\n");
    }

    @Override
    protected void onPostExecute(Void result) {}

}
