package com.example.odooonline;

import java.util.Map;

public class SO {

    private Integer id;
    private String name;
    private String partner;
    private Integer partnerId;
    private String dateOrder;
    private String clientOrderRef;
    private String state;
    private String warehouse;
    private Integer warehouseId;
    private Double amountTotal;
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
    public String getPartner() {
        return partner;
    }
    public void setPartner(String partner) {
        this.partner = partner;
    }
    public Integer getPartnerId() {
        return partnerId;
    }
    public void setPartnerId(Integer partnerId) {
        this.partnerId = partnerId;
    }
    public String getDateOrder() {
        return dateOrder;
    }
    public void setDateOrder(String dateOrder) {
        this.dateOrder = dateOrder;
    }
    public String getClientOrderRef() {
        return clientOrderRef;
    }
    public void setClientOrderRef(String clientOrderRef) {
        this.clientOrderRef = clientOrderRef;
    }
    public String getState() {
        return state;
    }
    public void setState(String state) {
        this.state = state;
    }
    public String getWarehouse() {
        return warehouse;
    }
    public void setWarehouse(String warehouse) {
        this.warehouse = warehouse;
    }
    public Integer getWarehouseId() {
        return warehouseId;
    }
    public void setWarehouseId(Integer warehouseId) {
        this.warehouseId = warehouseId;
    }
    public Double getAmountTotal() {
        return amountTotal;
    }
    public void setAmountTotal(Double amountTotal) {
        this.amountTotal = amountTotal;
    }
    public void setData(Map<String,Object> classObj) {
        setId((Integer) classObj.get("id"));
        setName(OdooUtility.getString(classObj, "name"));
        OdooUtility.M2OField partner_id = OdooUtility.getMany2One(classObj,
                "partner_id");
        setPartnerId(partner_id.id);
        setPartner(partner_id.value);
        setDateOrder(OdooUtility.getString(classObj, "date_order"));
        setClientOrderRef(OdooUtility.getString(classObj,
                "client_order_ref"));
        setState(OdooUtility.getString(classObj, "state"));
        OdooUtility.M2OField warehouse_id = OdooUtility.getMany2One(classObj,
                "warehouse_id");
        setWarehouseId(warehouse_id.id);
        setWarehouse(warehouse_id.value);
        setAmountTotal(OdooUtility.getDouble(classObj, "amount_total"));
    }
}
