package com.piseth.anemi.retrofit.apiservices;

import com.piseth.anemi.utils.model.User;

import java.util.List;

public interface UserCallBack {
    void onSuccess(User user);
    void onFailure();
}
