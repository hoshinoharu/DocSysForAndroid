package com.rehoshi.docsys.ui;

import android.os.Bundle;

import com.rehoshi.docsys.R;
import com.rehoshi.docsys.base.SimpleToolbarActivity;
import com.rehoshi.docsys.domain.User;
import com.rehoshi.docsys.service.UserApi;
import com.rehoshi.simple.form.FormBinder;
import com.rehoshi.simple.form.FormField;
import com.rehoshi.simple.form.value.MapInfo;
import com.rehoshi.simple.utils.ToastUtil;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterActivity extends SimpleToolbarActivity {

    @FormField(
            value = R.id.edTxt_userName,
            fieldName = "用户账号", minLength = 5, maxLength = 20,
            order = 1
    )
    private String account;

    @FormField(
            value = R.id.edTxt_password,
            fieldName = "密码", minLength = 6, maxLength = 10,trim = true,
            order = 2
    )
    private String pass;

    @FormField(
            value = R.id.edTxt_confirmPassword,
            fieldName = "密码", minLength = 6, maxLength = 10,trim = true,
            order = 3
    )
    private String confirmPass;

    private FormBinder formBinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        formBinder = new FormBinder(this);
        formBinder.adjustViewInput();
        setSPToolbarTitle("用户注册");
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public boolean verifyRegister(StringBuffer error) {
        MapInfo bind = formBinder.bind();
        boolean tag = bind.success();
        if (tag && !pass.equals(confirmPass)) {
            error.append("两次密码输入不一致");
            tag = false;
        } else {
            error.append(bind.getMessage());
        }
        return tag;
    }

    @OnClick(R.id.btn_register)
    public void register() {
        StringBuffer stringBuffer = new StringBuffer();
        if (verifyRegister(stringBuffer)) {
            User user = new User();
            user.setAccount(account);
            user.setPassword(pass);
            $(UserApi.class)
                    .register(user)
                    .onCallSuccess((data, msg, result) -> {
                        User.change(data, getSelf());
                        finish();
                    }).linkStart(R.id.btn_register, this);
        } else {
            ToastUtil.showLong(this, stringBuffer);
        }
    }
}
