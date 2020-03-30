package com.rehoshi.docsys.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.widget.EditText;
import android.widget.Switch;

import com.rehoshi.docsys.R;
import com.rehoshi.docsys.base.BaseActivity;
import com.rehoshi.docsys.control.Launcher;
import com.rehoshi.docsys.domain.User;
import com.rehoshi.docsys.service.UserApi;
import com.rehoshi.simple.utils.ResUtil;
import com.rehoshi.simple.utils.StringUtil;
import com.rehoshi.simple.utils.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 登录界面
 */
public class LoginActivity extends BaseActivity {

    @BindView(R.id.edTxt_userName)
    EditText edTxtUserName;

    @BindView(R.id.edTxt_password)
    EditText edTxtPassword;

    @BindView(R.id.swc_showPass)
    Switch swcShowPass;

    User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        user = User.getCurUser();
        swcShowPass.setOnCheckedChangeListener((compoundButton, b) -> {
            int selection = edTxtPassword.getSelectionEnd();

            //设置密码可以显示后 会自动将 光标移动至第一个
            edTxtPassword.setTransformationMethod(
                    b ? HideReturnsTransformationMethod.getInstance()
                            : PasswordTransformationMethod.getInstance()
            );
            //保存光标位置
            edTxtPassword.setSelection(selection);
        });

        if (user != null) {
            edTxtUserName.setText(user.getAccount());
            edTxtPassword.setText(user.getPassword());
        }
    }

    @OnClick(R.id.btn_login)
    public void login() {
        if (checkInput()) {
            $(UserApi.class)
                    .login(user)
                    .onCallSuccess((data, msg, result) -> {
                        User.saveChange(data);
                        startActivity(new Intent(getSelf(), HomeActivity.class));
                        finish();
                    }).linkStart(R.id.btn_login, this);
        }
    }


    @OnClick(R.id.txtV_register)
    public void register() {
        startActivity(new Intent(getSelf(), RegisterActivity.class));
    }

    /**
     * 验证用户登录输入
     *
     * @return
     */
    private boolean checkInput() {
        boolean flag = true;
        String msg = "";
        if (StringUtil.isNullOrEmpty(edTxtUserName)) {
            msg = ResUtil.getString(this, R.string.edTxt_userName);
            flag = false;
        } else if (StringUtil.isNullOrEmpty(edTxtPassword)) {
            msg = ResUtil.getString(this, R.string.edTxt_password);
            flag = false;
        }
        if (!flag) {
            ToastUtil.showLong(this, msg);
        } else {
            if (user == null) {
                user = new User();
            }
            user.setAccount(StringUtil.getText(edTxtUserName));
            user.setPassword(StringUtil.getText(edTxtPassword));
        }
        return flag;
    }

    @Override
    protected void onResume() {
        super.onResume();
        User curUser = User.getCurUser();
        if(curUser != null && curUser.getToken() != null){
            Launcher.getInstance().launchHome(this);
            finish();
        }
    }
}
