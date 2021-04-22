package com.example.odooonline;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.timroes.axmlrpc.XMLRPCCallback;
import de.timroes.axmlrpc.XMLRPCException;
import de.timroes.axmlrpc.XMLRPCServerException;

public class SOListActivity extends AppCompatActivity {

    private OdooUtility odoo;
    private String uid;
    private String password;
    private String serverAddress;
    private String database;
    private long searchTaskId;
    ListView listViewSaleOrder;
    List arrayListSaleOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_so_list);

        uid = SharedData.getKey(SOListActivity.this, "uid");
        password = SharedData.getKey(SOListActivity.this, "password");
        serverAddress = SharedData.getKey(SOListActivity.this,
                "serverAddress");
        database = SharedData.getKey(SOListActivity.this, "database");
        odoo = new OdooUtility(serverAddress, "object");
        arrayListSaleOrder = new ArrayList();
        listViewSaleOrder = (ListView)
                findViewById(R.id.listViewSOLine);
    }

    public void onClickSearchSO (View v){
        EditText editKeyword = (EditText) findViewById(R.id.editKeyword);
        String keyword = editKeyword.getText().toString();
        List conditions = Arrays.asList(
                Arrays.asList(
                        Arrays.asList("name", "ilike", keyword)));
        Map fields = new HashMap() {{
            put("fields", Arrays.asList(
                    "id",
                    "name"
            ));
        }};
        searchTaskId = odoo.search_read(listener, database, uid, password,
                "sale.order", conditions, fields);
    }

    XMLRPCCallback listener = new XMLRPCCallback() {
        public void onResponse(long id, Object result) {
            Looper.prepare();
            if (id==searchTaskId)
            {
                Object[] classObjs=(Object[])result;
                int length=classObjs.length;
                if(length>0){
                    arrayListSaleOrder.clear();
                    for (int i=0; i < length; i++) {
                        @SuppressWarnings("unchecked")
                        Map<String,Object> classObj =
                                (Map<String,Object>)classObjs[i];
                        arrayListSaleOrder.add(classObj.get("name"));
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            fillListSaleOrder();
                        }
                    });
                }
                else
                {
                    odoo.MessageDialog(SOListActivity.this,
                            "Sale Order not found");
                }
            }
            Looper.loop();
        }
        public void onError(long id, XMLRPCException error) {
            Looper.prepare();
            Log.e("SEARCH", error.getMessage());
            odoo.MessageDialog(SOListActivity.this, error.getMessage());
            Looper.loop();
        }
        public void onServerError(long id, XMLRPCServerException error) {
            Looper.prepare();
            Log.e("SEARCH", error.getMessage());
            odoo.MessageDialog(SOListActivity.this, error.getMessage());
            Looper.loop();
        }
    };

    private void fillListSaleOrder(){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                android.R.id.text1, arrayListSaleOrder);
        listViewSaleOrder.setAdapter(adapter);
        listViewSaleOrder.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id)
                    {
                        // ListView Clicked item index
                        int itemPosition = position;
                        // ListView Clicked item value
                        String itemValue = (String)
                                listViewSaleOrder.getItemAtPosition(position);
                        // Show Alert
                        /*
                            Toast.makeText(getApplicationContext(),
                               "Position :" + itemPosition + " ListItem : " +
                                itemValue, Toast.LENGTH_LONG)
                                .show();
                         */
                        Intent myIntent = new Intent(SOListActivity.this,
                                SOFormActivity.class);
                        myIntent.putExtra("name", itemValue);
                        SOListActivity.this.startActivity(myIntent);

                    }
                });
    }
}