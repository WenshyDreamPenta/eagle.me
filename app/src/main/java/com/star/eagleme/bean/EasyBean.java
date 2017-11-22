package com.star.eagleme.bean;

/**
 * Created by star on 2017/8/10.
 */

public class EasyBean
{
    private String text;
    private boolean isSelected;

    public EasyBean(String text, boolean isSelected) {
        this.text = text;
        setSelected(isSelected);
    }
    public EasyBean()
    {

    }
    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
