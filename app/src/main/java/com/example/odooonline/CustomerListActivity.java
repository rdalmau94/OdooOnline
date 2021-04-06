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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.timroes.axmlrpc.XMLRPCCallback;
import de.timroes.axmlrpc.XMLRPCException;
import de.timroes.axmlrpc.XMLRPCServerException;

public class CustomerListActivity extends AppCompatActivity {

    private OdooUtility odoo;
    private String uid;
    private String password;
    private String serverAddress;
    private String database;
    private long searchTaskId;
    ListView listViewPartner;
    List arrayListPartner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_list);

        uid = SharedData.getKey(CustomerListActivity.this, "uid");
        password = SharedData.getKey(CustomerListActivity.this, "password");
        serverAddress = SharedData.getKey(CustomerListActivity.this,
                "serverAddress");
        database = SharedData.getKey(CustomerListActivity.this, "database");
        odoo = new OdooUtility(serverAddress, "object");
        arrayListPartner = new ArrayList();
        listViewPartner = (ListView) findViewById(R.id.listPartner);
    }

    public void onClickSearchPartner(View v){
        EditText editKeyword = (EditText) findViewById(R.id.editKeyword);
        String keyword = editKeyword.getText().toString();
        List conditions = Arrays.asList(
                Arrays.asList(
                        Arrays.asList("name", "ilike", keyword)));
        //pyhthon equivalent ==> [ [ ["name", "ilike", keyword] ] ]
        Map fields = new HashMap() {{
            put("fields", Arrays.asList(
                    "id",
                    "display_name"
            ));
        }};
        //python equivalent ==> { "fields" : ["id","display_name"] }
        searchTaskId = odoo.search_read(listener, database, uid, password,
                "res.partner", conditions, fields);
    }

    XMLRPCCallback listener = new XMLRPCCallback() {
        public void onResponse(long id, Object result) {
            Looper.prepare();
            if (id==searchTaskId)
            {
                Object[] classObjs=(Object[])result;
                int length=classObjs.length;
                if(length>0){
                    arrayListPartner.clear();
                    for (int i=0; i < length; i++) {
                        @SuppressWarnings("unchecked")
                        Map<String,Object> classObj =
                                (Map<String,Object>)classObjs[i];
                        arrayListPartner.add(classObj.get("display_name"));
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            fillListPartner();
                        }
                    });
                }
                else
                {
                    odoo.MessageDialog(CustomerListActivity.this,
                            "Partner not found");
                }
            }
            Looper.loop();
        }

        public void onError(long id, XMLRPCException error) {
            Log.e("SEARCH", error.getMessage());
            odoo.MessageDialog(CustomerListActivity.this, error.getMessage());
        }
        public void onServerError(long id, XMLRPCServerException error) {
            Log.e("SEARCH", error.getMessage());
            odoo.MessageDialog(CustomerListActivity.this, error.getMessage());
        }
    };

    private void fillListPartner(){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                android.R.id.text1, arrayListPartner);
        listViewPartner.setAdapter(adapter);
        listViewPartner.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id)
                    {
                        // ListView Clicked item index
                        int itemPosition = position;
                        // ListView Clicked item value
                        String itemValue = (String)
                                listViewPartner.getItemAtPosition(position);
                        // Show Alert
                        /*Toast.makeText(getApplicationContext(),
                                "Position :" + itemPosition + " ListItem : " +
                                        itemValue, Toast.LENGTH_LONG).show();*/
                        Intent myIntent = new Intent(CustomerListActivity.this,
                                CustomerFormActivity.class);
                        myIntent.putExtra("name", itemValue);
                        CustomerListActivity.this.startActivity(myIntent);
                    }
                });
    }

    public static class Partner {
        private Integer id;
        private String name;
        private String street;
        private String street2;
        private String city;
        private String state;
        private Integer stateId;
        private String country;
        private Integer countryId;
        private String phone;
        private String mobile;
        private String fax;
        private String email;
        private String website;
        public Integer getId() {
            return id;
        }
        public void setId(Integer id) {
            this.id = id;
        }
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
        public String getStreet() {
            return street;
        }
        public void setStreet(String street) {
            this.street = street;
        }
        public String getStreet2() {
            return street2;
        }
        public void setStreet2(String street2) {
            this.street2 = street2;
        }
        public String getCity() {
            return city;
        }
        public void setCity(String city) {
            this.city = city;
        }
        public String getState() {
            return state;
        }
        public void setState(String state) {
            this.state = state;
        }
        public Integer getStateId() {
            return stateId;
        }
        public void setStateId(Integer stateId) {
            this.stateId = stateId;
        }
        public String getCountry() {
            return country;
        }
        public void setCountry(String country) {
            this.country = country;
        }
        public Integer getCountryId() {
            return countryId;
        }
        public void setCountryId(Integer countryId) {
            this.countryId = countryId;
        }
        public String getPhone() {
            return phone;
        }
        public void setPhone(String phone) {
            this.phone = phone;
        }
        public String getMobile() {
            return mobile;
        }
        public void setMobile(String mobile) {
            this.mobile = mobile;
        }
        public String getFax() {
            return fax;
        }
        public void setFax(String fax) {
            this.fax = fax;
        }
        public String getEmail() {
            return email;
        }
        public void setEmail(String email) {
            this.email = email;
        }
        public String getWebsite() {
            return website;
        }
        public void setWebsite(String website) {
            this.website = website;
        }
        public void setData(Map<String,Object> classObj){
            setId((Integer) classObj.get("id"));
            setName(OdooUtility.getString(classObj, "display_name"));
            setStreet(OdooUtility.getString(classObj, "street"));
            setStreet2(OdooUtility.getString(classObj, "street2"));
            setCity(OdooUtility.getString(classObj, "city"));
            OdooUtility.M2OField state_id = OdooUtility.getMany2One(classObj, "state_id");
            setStateId(state_id.id);
            setState(state_id.value);
            OdooUtility.M2OField country_id = OdooUtility.getMany2One(classObj,
                    "country_id");
            setCountryId(country_id.id);
            setCountry(country_id.value);
            setPhone(OdooUtility.getString(classObj, "phone"));
            setMobile(OdooUtility.getString(classObj, "mobile"));
            setFax(OdooUtility.getString(classObj, "fax"));
            setEmail(OdooUtility.getString(classObj, "email"));
            setWebsite(OdooUtility.getString(classObj, "website"));
        }
    }
}