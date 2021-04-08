package com.example.odooonline;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.timroes.axmlrpc.XMLRPCCallback;
import de.timroes.axmlrpc.XMLRPCException;
import de.timroes.axmlrpc.XMLRPCServerException;

public class CustomerFormActivity extends AppCompatActivity {

    private OdooUtility odoo;
    private String uid;
    private String password;
    private String serverAddress;
    private String database;
    private long searchTaskId;
    EditText editName;
    EditText editStreet;
    EditText editStreet2;
    EditText editCity;
    EditText editState;
    EditText editCountry;
    EditText editPhone;
    EditText editMobile;
    //EditText editFax;
    EditText editEmail;
    EditText editWebsite;
    CustomerListActivity.Partner partner;
    private long updatePartnerTaskId;
    private long createPartnerTaskId;
    private long deletePartnerTaskId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_form);

        initFields();

        uid = SharedData.getKey(CustomerFormActivity.this, "uid");
        password = SharedData.getKey(CustomerFormActivity.this, "password");
        serverAddress = SharedData.getKey(CustomerFormActivity.this,
                "serverAddress");
        database = SharedData.getKey(CustomerFormActivity.this, "database");
        odoo = new OdooUtility(serverAddress, "object");
        partner = new CustomerListActivity.Partner();
        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        if (name != null)
            searchPartnerByName(name);
    }

    private void initFields()
    {
        editName = (EditText) findViewById(R.id.editName);
        editStreet= (EditText) findViewById(R.id.editStreet);
        editStreet2= (EditText) findViewById(R.id.editStreet2);
        editCity= (EditText) findViewById(R.id.editCity);
        editState= (EditText) findViewById(R.id.editState);
        editCountry= (EditText) findViewById(R.id.editCountry);
        editPhone= (EditText) findViewById(R.id.editPhone);
        editMobile= (EditText) findViewById(R.id.editMobile);
        //editFax= (EditText) findViewById(R.id.editFax);
        editEmail= (EditText) findViewById(R.id.editEmail);
        editWebsite= (EditText) findViewById(R.id.editWebsite);
    }

    private void searchPartnerByName(String name) {
        List conditions = Arrays.asList(Arrays.asList(
                Arrays.asList("name", "=", name)));
        Map fields = new HashMap() {{
            put("fields", Arrays.asList(
                    "id",
                    "display_name",
                    "street",
                    "street2",
                    "city",
                    "state_id",
                    "country_id",
                    "phone",
                    "mobile",
                    "email",
                    "website"
            ));
        }};
        searchTaskId = odoo.search_read(listener, database, uid, password,
                "res.partner", conditions, fields);
    }

    XMLRPCCallback listener = new XMLRPCCallback() {
        public void onResponse(long id, Object result) {
            Looper.prepare();
            if (id == searchTaskId) {
                Object[] classObjs = (Object[]) result;
                int length = classObjs.length;
                if (length > 0) {
                    for (int i = 0; i < length; i++) {
                        @SuppressWarnings("unchecked")
                        Map<String, Object> classObj =
                                (Map<String, Object>) classObjs[i];
                        partner.setData(classObj);
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            fillPartnerForm();
                        }
                    });
                }else {
                    odoo.MessageDialog(CustomerFormActivity.this,
                            "Partner not found");
                }
            }else if (id==updatePartnerTaskId)
            {
                final Boolean updateResult = (Boolean) result;
                if(updateResult)
                {
                    Log.v("PARTNER UPDATE", "successfully");
                    odoo.MessageDialog(CustomerFormActivity.this,
                            "Update customer succeed.");
                }
                else{
                    odoo.MessageDialog(CustomerFormActivity.this,
                            "Update customer failed. Server return was false");
                }
            }else if (id == createPartnerTaskId){
                String createResult = result.toString();
                if(createResult != null)
                {
                    Log.v("PARTNER CREATE", "successfully");
                    odoo.MessageDialog(CustomerFormActivity.this,
                            "Create partner succeed. ID = " + createResult);
                }
                else
                {
                    odoo.MessageDialog(CustomerFormActivity.this,
                            "Create partner failed. Server return was false");
                }
            }else if (id == deletePartnerTaskId) {
                final Boolean deleteResult = (Boolean) result;
                if(deleteResult)
                {
                    Log.v("PARTNER DELETE", "successfully");
                    odoo.MessageDialog(CustomerFormActivity.this,
                            "Delete customer succeed.");
                }
                else{
                    odoo.MessageDialog(CustomerFormActivity.this,
                            "Delete customer failed. Server return was false");
                }
            }
            Looper.loop();
        }
        public void onError(long id, XMLRPCException error) {
            Looper.prepare();
            Log.e("SEARCH ****", error.getMessage());
            odoo.MessageDialog(CustomerFormActivity.this, error.getMessage());
            Looper.loop();
        }
        public void onServerError(long id, XMLRPCServerException error) {
            Looper.prepare();
            Log.e("SEARCH ****", error.getMessage());
            odoo.MessageDialog(CustomerFormActivity.this, error.getMessage());
            Looper.loop();
        }
    };

    private void fillPartnerForm() {
        editName.setText(partner.getName());
        editStreet.setText(partner.getStreet());
        editStreet2.setText(partner.getStreet2());
        editCity.setText(partner.getCity());
        editState.setText(partner.getState());
        editCountry.setText(partner.getCountry());
        editPhone.setText(partner.getPhone());
        editMobile.setText(partner.getMobile());
        //editFax.setText(partner.getFax());
        editEmail.setText(partner.getEmail());
        editWebsite.setText(partner.getWebsite());
    }

    private void updatePartnerModel(){
        String name = editName.getText().toString();
        String street = editStreet.getText().toString();
        String street2 = editStreet2.getText().toString();
        String city = editCity.getText().toString();
        String state = editState.getText().toString();
        String country = editCountry.getText().toString();
        String phone = editPhone.getText().toString();
        String mobile = editMobile.getText().toString();
        //String fax = editFax.getText().toString();
        String email = editEmail.getText().toString();
        String website = editWebsite.getText().toString();
        partner.setName(name);
        partner.setStreet(street);
        partner.setStreet2(street2);
        partner.setCity(city);
        //partner.setStateId( );
        partner.setState(state);
        //partner.setCountryId( );
        partner.setCountry(country);
        partner.setPhone(phone);
        partner.setMobile(mobile);
        //partner.setFax(fax);
        partner.setEmail(email);
        partner.setWebsite(website);
    }

    public void onSavePartner(View v){
        updatePartnerModel();
        if (partner.getId() != null)
            updatePartnerToOdoo();
        else
            createPartnerToOdoo();
    }

    private void createPartnerToOdoo(){
        List data = Arrays.asList(new HashMap() {{
            put("name", partner.getName());
            put("street", partner.getName());
            put("street2", partner.getStreet());
            put("city", partner.getStreet2());
            if (partner.getStateId() != null)
                put("state_id", partner.getStateId());
            if (partner.getCountryId() != null)
                put("country_id", partner.getCountryId());
            put("phone", partner.getPhone());
            put("mobile", partner.getMobile());
            //put("fax", partner.getFax());
            put("email", partner.getEmail());
            put("website", partner.getWebsite());
        }});
        createPartnerTaskId = odoo.create(listener, database, uid,
                password, "res.partner", data);
    }

    public void onDeletePartner(View view){
        AlertDialog.Builder alertDialogBuilder = new
                AlertDialog.Builder(CustomerFormActivity.this);
        String msg = "Are you sure to delete this?";
        alertDialogBuilder.setMessage(msg)
                .setPositiveButton(android.R.string.ok, new
                        DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                deletePartner();
                                dialog.dismiss();
                            }
                        }).create().show();
    }

    private void deletePartner(){
        List conditions = Arrays.asList(
                Arrays.asList( partner.getId() ));
        deletePartnerTaskId = odoo.delete(listener, database, uid,
                password, "res.partner", conditions);
    }

    private void updatePartnerToOdoo(){
        List data = Arrays.asList(
                Arrays.asList(partner.getId()),
                new HashMap() {{
                    put("name", partner.getName());
                    put("street", partner.getStreet());
                    put("street2", partner.getStreet2());
                    put("city", partner.getCity());
                    put("state_id", partner.getStateId());
                    put("country_id", partner.getCountryId());
                    put("phone", partner.getPhone());
                    put("mobile", partner.getMobile());
                    //put("fax", partner.getFax());
                    put("email", partner.getEmail());
                    put("website", partner.getWebsite());
                }}
        );
        updatePartnerTaskId = odoo.update(listener, database, uid,
                password, "res.partner", data);
    }

}