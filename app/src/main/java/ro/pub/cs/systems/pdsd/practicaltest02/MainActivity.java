package ro.pub.cs.systems.pdsd.practicaltest02;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private EditText serverPortEditText;
    private EditText clientPortEditText;
    private EditText clientAddressEditText;
    private EditText query;
    private TextView response;
    private ServerThread serverThread;

    private StartServerClickListener startServerClickListener = new StartServerClickListener();
    private class StartServerClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            int port = Integer.parseInt(serverPortEditText.getText().toString());

            serverThread = new ServerThread(query, port);
            serverThread.startServer();
        }
    }

    private StopServerClickListener stopServerClickListener = new StopServerClickListener();
    private class StopServerClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            serverThread.stopServer();
        }
    }

    private ClientExecuteQuery clientExecuteQuery = new ClientExecuteQuery();
    private class ClientExecuteQuery implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            ClientAsyncTask clientAsyncTask = new ClientAsyncTask(response);
            clientAsyncTask.execute(clientAddressEditText.getText().toString(), clientPortEditText.getText().toString());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // SERVER
        serverPortEditText = findViewById(R.id.port);
        Button serverStartButton = findViewById(R.id.start_server);
        Button serverStopButton = findViewById(R.id.stop_server);

        serverStartButton.setOnClickListener(startServerClickListener);
        serverStopButton.setOnClickListener(stopServerClickListener);


        //CLIENT
        Button sendButton = findViewById(R.id.send);
        clientAddressEditText = findViewById(R.id.client_address);
        clientPortEditText = findViewById(R.id.client_port);
        query = findViewById(R.id.query);
        response = findViewById(R.id.response);

        sendButton.setOnClickListener(clientExecuteQuery);
    }

    @Override
    public void onDestroy() {
        if (serverThread != null) {
            serverThread.stopServer();
        }
        super.onDestroy();
    }
}
