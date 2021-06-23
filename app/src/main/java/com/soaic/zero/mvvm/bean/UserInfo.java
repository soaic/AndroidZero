package com.soaic.zero.mvvm.bean;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.soaic.zero.BR;

public class UserInfo extends BaseObservable {

    public String userName;
    public int age;
    public boolean isChecked;

    @Bindable
    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        if (checked != isChecked) {
            isChecked = checked;

            notifyPropertyChanged(BR.checked);
            System.out.println("checked="+checked);
        }
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "userName='" + userName + '\'' +
                ", age=" + age +
                '}';
    }
}
