package com.example.odooonline;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.timroes.axmlrpc.XMLRPCCallback;
import de.timroes.axmlrpc.XMLRPCException;
import de.timroes.axmlrpc.XMLRPCServerException;

public class SOLineListActivity extends AppCompatActivity {

    private OdooUtility odoo;
    private String uid;
    private String password;
    private String serverAddress;
    private String database;
    private long searchTaskId;
    Integer saleOrderId;
    ArrayList<SOLine> soLineArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_s_o_line_list);

        uid = SharedData.getKey(SOLineListActivity.this, "uid");
        password = SharedData.getKey(SOLineListActivity.this, "password");
        serverAddress = SharedData.getKey(SOLineListActivity.this,
                "serverAddress");
        database = SharedData.getKey(SOLineListActivity.this, "database");
        odoo = new OdooUtility(serverAddress, "object");
        soLineArrayList = new ArrayList<SOLine>();
        saleOrderId = Integer.parseInt( SharedData.getKey(
                SOLineListActivity.this, "saleOrderId"));

        searchSaleOrderLine(saleOrderId);
    }

    private void searchSaleOrderLine(Integer saleOrderId){
        List conditions = Arrays.asList(
                Arrays.asList(
                        Arrays.asList("order_id", "=", saleOrderId)));
        Map fields = new HashMap() {{
            put("fields", Arrays.asList(
                    "id",
                    "name",
                    "product_id",
                    "product_uom_qty",
                   // "product_uom_id",
                    "price_unit",
                    "price_subtotal"
            ));
        }};
        searchTaskId = odoo.search_read(listener, database, uid, password,
                "sale.order.line", conditions, fields);
    }

    XMLRPCCallback listener = new XMLRPCCallback() {
        public void onResponse(long id, Object result) {
            Looper.prepare();
            if (id==searchTaskId)
            {
                Object[] classObjs=(Object[])result;
                int length=classObjs.length;
                if(length>0){
                    soLineArrayList.clear();
                    for (int i=0; i < length; i++) {
                        @SuppressWarnings("unchecked")
                        Map<String,Object> classObj =
                                (Map<String,Object>)classObjs[i];
                        SOLine soLine = new SOLine();
                        soLine.setData(classObj);
                        soLineArrayList.add(soLine);
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            fillListSaleOrderLine();
                        }
                    });
                }
                else
                {
                    odoo.MessageDialog(SOLineListActivity.this,
                            "Sale Order Line not found");
                }
            }
            Looper.loop();
        }
        public void onError(long id, XMLRPCException error) {
            Looper.prepare();
            Log.e("SEARCH", error.getMessage());
            odoo.MessageDialog(SOLineListActivity.this, error.getMessage());
            Looper.loop();
        }
        public void onServerError(long id, XMLRPCServerException error) {
            Looper.prepare();
            Log.e("SEARCH", error.getMessage());
            odoo.MessageDialog(SOLineListActivity.this, error.getMessage());
            Looper.loop();
        }
    };

    private void fillListSaleOrderLine(){
        final ListView lv = (ListView)
                findViewById(R.id.listViewSOLine);
        lv.setAdapter(new SOLineCustomAdapter(this, soLineArrayList));
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a,
                                    View v, int position, long id) {
                Object o = lv.getItemAtPosition(position);
                SOLine fullObject = (SOLine) o;
                Toast.makeText(SOLineListActivity.this,
                        "You have chosen: " + " " +
                                fullObject.getName(), Toast.LENGTH_LONG).show();
            }
        });
    }
}