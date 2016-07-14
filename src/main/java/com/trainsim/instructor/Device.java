/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.trainsim.instructor;

import java.awt.Image;

/**
 *
 * @author patipat
 */
public class Device {

    private String id;

    /**
     * Get the value of id
     *
     * @return the value of id
     */
    public String getId() {
        return id;
    }

    /**
     * Set the value of id
     *
     * @param id new value of id
     */
    public void setId(String id) {
        this.id = id;
    }

    private String type;

    /**
     * Get the value of type
     *
     * @return the value of type
     */
    public String getType() {
        return type;
    }

    /**
     * Set the value of type
     *
     * @param type new value of type
     */
    public void setType(String type) {
        this.type = type;
    }

    private String name;

    /**
     * Get the value of name
     *
     * @return the value of name
     */
    public String getName() {
        return name;
    }

    /**
     * Set the value of name
     *
     * @param name new value of name
     */
    public void setName(String name) {
        this.name = name;
    }

    private int x;

    /**
     * Get the value of x
     *
     * @return the value of x
     */
    public int getX() {
        return x;
    }

    /**
     * Set the value of x
     *
     * @param x new value of x
     */
    public void setX(int x) {
        this.x = x;
    }

    private int y;

    /**
     * Get the value of y
     *
     * @return the value of y
     */
    public int getY() {
        return y;
    }

    /**
     * Set the value of y
     *
     * @param y new value of y
     */
    public void setY(int y) {
        this.y = y;
    }

    private int width;

    /**
     * Get the value of width
     *
     * @return the value of width
     */
    public int getWidth() {
        return width;
    }

    /**
     * Set the value of width
     *
     * @param width new value of width
     */
    public void setWidth(int width) {
        this.width = width;
    }

    private int height;

    /**
     * Get the value of height
     *
     * @return the value of height
     */
    public int getHeight() {
        return height;
    }

    /**
     * Set the value of height
     *
     * @param height new value of height
     */
    public void setHeight(int height) {
        this.height = height;
    }

    private Image imgOn;

    /**
     * Get the value of imgOn
     *
     * @return the value of imgOn
     */
    public Image getImgOn() {
        return imgOn;
    }

    /**
     * Set the value of imgOn
     *
     * @param imgOn new value of imgOn
     */
    public void setImgOn(Image imgOn) {
        this.imgOn = imgOn;
    }

    private Image imgOff;

    /**
     * Get the value of imgOff
     *
     * @return the value of imgOff
     */
    public Image getImgOff() {
        return imgOff;
    }

    /**
     * Set the value of imgOff
     *
     * @param imgOff new value of imgOff
     */
    public void setImgOff(Image imgOff) {
        this.imgOff = imgOff;
    }

    private Image imgCurr;

    /**
     * Get the value of imgCurr
     *
     * @return the value of imgCurr
     */
    public Image getImgCurr() {
        return imgCurr;
    }

    /**
     * Set the value of imgCurr
     *
     * @param imgCurr new value of imgCurr
     */
    public void setImgCurr(Image imgCurr) {
        this.imgCurr = imgCurr;
    }
    
    
    private String cmdOff;
    
    
    public void setCmdOff(String cmdOff) {
        this.cmdOff = cmdOff;
    }
    
    public String getCmdOff() {
        return this.cmdOff;
    }
    
    
    private String cmdOn;
    
    
    public void setCmdOn(String cmdOn) {
        this.cmdOn = cmdOn;
    }
    
    public String getCmdOn() {
        return this.cmdOn;
    }
    
    
    private String lampOn;
    
    
    public void setLampOn(String lampOn) {
        this.lampOn = lampOn;
    }
    
    public String getLampOn() {
        return this.lampOn;
    }
    
    
    private String lampOff;
    
    
    public void setLampOff(String lampOff) {
        this.lampOff = lampOff;
    }
    
    public String getLampOff() {
        return this.lampOff;
    }

}
