package com.example.odooonline;

import java.util.Map;

public class SOLine {

        private Integer id;
        private String product;
        private Integer productId;
        private String name;
        private String unitOfMeasure;
        private Integer unitOfMeasureId;
        private Double productUomQty;
        private Double priceUnit;
        private Double priceSubtotal;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getProduct() {
            return product;
        }

        public void setProduct(String product) {
            this.product = product;
        }

        public Integer getProductId() {
            return productId;
        }

        public void setProductId(Integer productId) {
            this.productId = productId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Double getProductUomQty() {
            return productUomQty;
        }

        public void setProductUomQty(Double productUomQty) {
            this.productUomQty = productUomQty;
        }

        public Double getPriceUnit() {
            return priceUnit;
        }

        public void setPriceUnit(Double priceUnit) {
            this.priceUnit = priceUnit;
        }

        public Double getPriceSubtotal() {
            return priceSubtotal;
        }

        public void setPriceSubtotal(Double priceSubtotal) {
            this.priceSubtotal = priceSubtotal;
        }

        public String getUnitOfMeasure() {
            return unitOfMeasure;
        }

        public void setUnitOfMeasure(String unitOfMeasure) {
            this.unitOfMeasure = unitOfMeasure;
        }

        public Integer getUnitOfMeasureId() {
            return unitOfMeasureId;
        }

        public void setUnitOfMeasureId(Integer unitOfMeasureId) {
            this.unitOfMeasureId = unitOfMeasureId;
        }

        public void setData(Map<String, Object> classObj) {
            setId((Integer) classObj.get("id"));
            OdooUtility.M2OField product_id = OdooUtility.getMany2One(classObj,
                    "product_id");
            setProductId(product_id.id);
            setProduct(product_id.value);
            OdooUtility.M2OField product_uom_id = OdooUtility.getMany2One(classObj,
                    "product_uom_id");
            setUnitOfMeasureId(product_uom_id.id);
            setUnitOfMeasure(product_uom_id.value);
            setName(OdooUtility.getString(classObj, "name"));
            setProductUomQty(OdooUtility.getDouble(classObj,
                    "product_uom_qty"));
            setPriceUnit(OdooUtility.getDouble(classObj,
                    "price_unit"));
            setPriceSubtotal(OdooUtility.getDouble(classObj,
                    "price_subtotal"));
        }
}
