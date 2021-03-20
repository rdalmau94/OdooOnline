package com.example.odooonline;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.timroes.axmlrpc.XMLRPCCallback;
import de.timroes.axmlrpc.XMLRPCClient;

public class OdooUtility {
    private URL url;
    private XMLRPCClient client;

    OdooUtility(String serverAddress, String path) {
        try{
            url = new URL(serverAddress + "/xmlrpc/2/" + path);
            client = new XMLRPCClient(url);
        } catch(Exception ex) {
            Log.e("ODOO UTILITY: ", ex.getMessage());
        }
    }
    static class M2OField {
        public Integer id;
        public String value;
    }
    public long login(XMLRPCCallback listener,
                      String db,
                      String username,
                      String password){
        Map<String,Object> emptyMap = new HashMap<String,Object>();
        long id = client.callAsync(listener, "authenticate", db,
                username, password, emptyMap);
        return id;
    }
    public long search_read(XMLRPCCallback listener,
                            String db,
                            String uid,
                            String password,
                            String object,
                            List conditions,
                            Map<String, List> fields ){
        long id = client.callAsync(listener, "execute_kw", db,
                Integer.parseInt(uid), password,
                object, "search_read", conditions, fields);
        return id;
    }
    public long create(XMLRPCCallback listener,
                       String db,
                       String uid,
                       String password,
                       String object,
                       List data){
        long id = client.callAsync(listener, "execute_kw", db,
                Integer.parseInt(uid), password,
                object, "create", data);
        return id;
    }
    public long exec(XMLRPCCallback listener,
                     String db,
                     String uid,
                     String password,
                     String object,
                     String method,
                     List data){
        long id = client.callAsync(listener, "execute_kw", db,
                Integer.parseInt(uid), password,
                object, method, data);
        return id;
    }
    public long update(XMLRPCCallback listener,
                       String db,
                       String uid,
                       String password,
                       String object,
                       List data){
        long id = client.callAsync(listener, "execute_kw", db,
                Integer.parseInt(uid), password,
                object, "write", data);
        return id;
    }
    public long delete(XMLRPCCallback listener,
                       String db,
                       String uid,
                       String password,
                       String object,
                       List data){
        long id = client.callAsync(listener, "execute_kw", db,
                Integer.parseInt(uid), password,
                object, "unlink", data);
        return id;
    }
    public static M2OField getMany2One(Map<String,Object> classObj, String
            fieldName){
        Integer fieldId=0;
        String fieldValue="";
        M2OField res = new M2OField();
        if(classObj.get(fieldName) instanceof Object[]){
            Object[] field = (Object[])classObj.get(fieldName);
            if(field.length >0 ){
                fieldId = (Integer)field[0];
                fieldValue = (String)field[1];
            }
        }
        res.id = fieldId;
        res.value = fieldValue;
        return res;
    }
    public static List getOne2Many(Map<String,Object> classObj, String
            fieldName)
    {
        List res = new ArrayList();
        Object[] field = (Object[])classObj.get(fieldName);
        for (int i=0; i<field.length; i++){
            res.add(i, field[i]);
        }
        return res;
    }
    public static String getString(Map<String,Object> classObj, String
            fieldName){
        String res = "";
        if (classObj.get(fieldName) instanceof String){
            res = (String)classObj.get(fieldName);
        }
        return res;
    }
    public static Double getDouble(Map<String,Object> classObj, String
            fieldName){
        Double res = 0.0;
        if (classObj.get(fieldName) instanceof Double){
            res = (Double)classObj.get(fieldName);
        }
        return res;
    }
    public static Integer getInteger(Map<String,Object> classObj, String
            fieldName){
        Integer res = 0;
        if (classObj.get(fieldName) instanceof Double){
            res = (Integer)classObj.get(fieldName);
        }
        return res;
    }
    public void MessageDialog(Context context, String msg){
        AlertDialog.Builder alertDialogBuilder = new
                AlertDialog.Builder(context);

        alertDialogBuilder.setMessage(msg)
                .setCancelable(false)
                .setPositiveButton(android.R.string.ok, new
                        DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).create().show();
    }
}
