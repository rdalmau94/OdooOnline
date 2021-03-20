package com.example.odooonline;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import de.timroes.axmlrpc.XMLRPCCallback;
import de.timroes.axmlrpc.XMLRPCException;
import de.timroes.axmlrpc.XMLRPCServerException;

public class MainActivity extends AppCompatActivity {
    private OdooUtility odoo;
    private long loginTaskId;
    EditText editUsername;
    EditText editPassword;
    EditText editDatabase;
    EditText editServerAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String database = SharedData.getKey(MainActivity.this, "database");
        String serverAddress = SharedData.getKey(MainActivity.this,
                "serverAddress");
        String username = SharedData.getKey(MainActivity.this, "username");
        String password = SharedData.getKey(MainActivity.this, "password");
        editServerAddress = (EditText)
                findViewById(R.id.editServerAddress);
        editDatabase = (EditText) findViewById(R.id.editDatabase);
        editUsername = (EditText) findViewById(R.id.editUsername);
        editPassword = (EditText) findViewById(R.id.editPassword);
        editServerAddress.setText(serverAddress);
        editDatabase.setText(database);
        editUsername.setText(username);
        editPassword.setText(password);
    }
    public void onClickLogin(View view){
        switch (view.getId()) {
            case R.id.buttonLogin:
                String password = editPassword.getText().toString();
                String username = editUsername.getText().toString();
                String database = editDatabase.getText().toString();
                String serverAddress = editServerAddress.getText().toString();
                odoo = new OdooUtility(serverAddress, "common");
                loginTaskId = odoo.login(listener,
                        database, username, password);
                SharedData.setKey(MainActivity.this, "password", password);
                SharedData.setKey(MainActivity.this, "username", username);
                SharedData.setKey(MainActivity.this, "database", database);
                SharedData.setKey(MainActivity.this, "serverAddress",
                        serverAddress);
                break;
        }
    }
    XMLRPCCallback listener = new XMLRPCCallback() {
        public void onResponse(long id, Object result) {
        /*
        looper so we can access the UI component from this thread
        */
            Looper.prepare();
            if (id == loginTaskId){
                if ( result instanceof Boolean && (Boolean)result == false)
                {
                    odoo.MessageDialog(MainActivity.this,
                            "Login Error. Please try again");
                }
                else
                {
                    String uid = result.toString();
                    SharedData.setKey(MainActivity.this, "uid", uid);
                    //odoo.MessageDialog(MainActivity.this,"Login Succeed. uid=" + uid);

                    Intent myIntent = new Intent(MainActivity.this, MenuActivity.class);
                    MainActivity.this.startActivity(myIntent);
                }
            }
            Looper.loop();
        }
        public void onError(long id, XMLRPCException error) {
            // Handling any error in the library
            Looper.prepare();
            Log.e("LOGIN", error.getMessage());
            odoo.MessageDialog(MainActivity.this,
                    "Login Error. " + error.getMessage());
            Looper.loop();
        }
        public void onServerError(long id, XMLRPCServerException error) {
            // Handling an error response from the server
            Looper.prepare();
            Log.e("LOGIN", error.getMessage());
            odoo.MessageDialog(MainActivity.this,
                    "Login Error. " + error.getMessage());
            Looper.loop();
        }
    };
}