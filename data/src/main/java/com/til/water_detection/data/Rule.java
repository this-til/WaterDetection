package com.til.water_detection.data;

import java.util.Objects;

public class Rule {

    private int id;
    private int datatypeId;
    private int equipmentId;

    private float exceptionUpper;
    private float warnUpper;
    private float warnLower;
    private float exceptionLower;

    private boolean warnSendMessage;
    private boolean exceptionSendMessage;

    public Rule(int id, int datatypeId, int equipmentId, float exceptionUpper, float warnUpper, float warnLower, float exceptionLower, boolean warnSendMessage, boolean exceptionSendMessage) {
        this.id = id;
        this.datatypeId = datatypeId;
        this.equipmentId = equipmentId;
        this.exceptionUpper = exceptionUpper;
        this.warnUpper = warnUpper;
        this.warnLower = warnLower;
        this.exceptionLower = exceptionLower;
        this.warnSendMessage = warnSendMessage;
        this.exceptionSendMessage = exceptionSendMessage;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDatatypeId() {
        return datatypeId;
    }

    public void setDatatypeId(int datatypeId) {
        this.datatypeId = datatypeId;
    }

    public int getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(int equipmentId) {
        this.equipmentId = equipmentId;
    }

    public float getExceptionUpper() {
        return exceptionUpper;
    }

    public void setExceptionUpper(float exceptionUpper) {
        this.exceptionUpper = exceptionUpper;
    }

    public float getWarnUpper() {
        return warnUpper;
    }

    public void setWarnUpper(float warnUpper) {
        this.warnUpper = warnUpper;
    }

    public float getWarnLower() {
        return warnLower;
    }

    public void setWarnLower(float warnLower) {
        this.warnLower = warnLower;
    }

    public float getExceptionLower() {
        return exceptionLower;
    }

    public void setExceptionLower(float exceptionLower) {
        this.exceptionLower = exceptionLower;
    }

    public boolean isWarnSendMessage() {
        return warnSendMessage;
    }

    public void setWarnSendMessage(boolean warnSendMessage) {
        this.warnSendMessage = warnSendMessage;
    }

    public boolean isExceptionSendMessage() {
        return exceptionSendMessage;
    }

    public void setExceptionSendMessage(boolean exceptionSendMessage) {
        this.exceptionSendMessage = exceptionSendMessage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rule rule = (Rule) o;
        return id == rule.id && datatypeId == rule.datatypeId && equipmentId == rule.equipmentId && Float.compare(exceptionUpper, rule.exceptionUpper) == 0 && Float.compare(warnUpper, rule.warnUpper) == 0 && Float.compare(warnLower, rule.warnLower) == 0 && Float.compare(exceptionLower, rule.exceptionLower) == 0 && warnSendMessage == rule.warnSendMessage && exceptionSendMessage == rule.exceptionSendMessage ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, datatypeId, equipmentId, exceptionUpper, warnUpper, warnLower, exceptionLower, warnSendMessage, exceptionSendMessage);
    }

    @Override
    public String toString() {
        return "Rule{" +
                "id=" + id +
                ", datatypeId=" + datatypeId +
                ", equipmentId=" + equipmentId +
                ", exceptionUpper=" + exceptionUpper +
                ", warnUpper=" + warnUpper +
                ", warnLower=" + warnLower +
                ", exceptionLower=" + exceptionLower +
                ", warnSendMessage=" + warnSendMessage +
                ", exceptionSendMessage=" + exceptionSendMessage +
                '}';
    }
}
