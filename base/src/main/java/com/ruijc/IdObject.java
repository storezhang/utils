/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ruijc;

/**
 * 具有序列号的基类
 *
 * @author Storezhang
 */
public class IdObject extends BaseObject {

    private Long id;//序列号

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setId(Integer id) {
        this.id = (long) id;
    }

    public String getKey() {
        String className = getClass().getSimpleName();
        int index = className.indexOf("_$$_");
        if (-1 != index) {
            className = className.substring(0, index);
        }
        return className + "-" + getId();
    }

    @Override
    public int hashCode() {
        int hash = 7;
        if (null == id) {
            hash = Integer.MAX_VALUE;
        } else {
            hash = 37 * hash + (int) (this.id ^ (this.id >>> 32));
        }
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (null == this.id) {
            return false;
        }
        String thisClass = getClass().getCanonicalName();
        String thatClass = obj.getClass().getCanonicalName();
        if (!(thisClass.contains(thatClass) || thatClass.contains(thisClass))) {
            return false;
        }
        if (!(obj instanceof IdObject)) {
            return false;
        }
        final IdObject other = (IdObject) obj;

        return this.id.intValue() == other.id.intValue();
    }
}
