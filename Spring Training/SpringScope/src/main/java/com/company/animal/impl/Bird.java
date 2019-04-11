/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.company.animal.impl;

import com.company.animal.Animal;

/**
 *
 * @author totoland
 */
public class Bird implements Animal {

    private int legs;
    private String say;
    private boolean fly;
    
    public int getLegs() {
        return getLegs();
    }

    public String say() {
        return getSay();
    }

    public boolean canFly() {
        return fly;
    }

    /**
     * @param legs the legs to set
     */
    public void setLegs(int legs) {
        this.legs = legs;
    }

    /**
     * @return the say
     */
    public String getSay() {
        return say;
    }

    /**
     * @param say the say to set
     */
    public void setSay(String say) {
        this.say = say;
    }

    /**
     * @return the fly
     */
    public boolean isFly() {
        return fly;
    }

    /**
     * @param fly the fly to set
     */
    public void setFly(boolean fly) {
        this.fly = fly;
    }
}
