package com.rehoshi.docsys.ui.mine;

import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import com.rehoshi.docsys.R;
import com.rehoshi.docsys.base.SimpleToolbarActivity;
import com.rehoshi.docsys.domain.User;
import com.rehoshi.docsys.service.UserApi;
import com.rehoshi.docsys.util.InputValidator;
import com.rehoshi.simple.utils.FormatUtil;
import com.rehoshi.simple.utils.StringUtil;
import com.rehoshi.simple.utils.ToastUtil;
import com.rengwuxian.materialedittext.MaterialEditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UserInfoUpdateActivity extends SimpleToolbarActivity {

    @BindView(R.id.edTxt_userName)
    MaterialEditText edTxtUserName;
    @BindView(R.id.edTxt_desc)
    EditText edTxtDesc;
    @BindView(R.id.txtV_descCount)
    TextView txtVDescCount;

    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info_update);
        ButterKnife.bind(this);
        setSPToolbarTitle("修改信息");

        initView();
        setUser(User.getCurUser());
    }

    private void initView() {


        edTxtDesc.setFilters(new InputFilter[]{
                new InputFilter.LengthFilter(255)
        });


        edTxtDesc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                updateInputCount();

            }
        });


        updateInputCount();


    }

    private void updateInputCount() {
        txtVDescCount.setText(FormatUtil.formatString("%d/%d", edTxtDesc.getText().length(), 255));
    }

    @OnClick(R.id.btn_save)
    public void prepareSave() {
        if (verifyInput()) {
            $(UserApi.class)
                    .updateUserInfo(user)
                    .onCallSuccess((data, msg, result) -> {
                        ToastUtil.showLong(this, "修改成功");
                        finish();
                    }).linkStart(R.id.btn_save, this);
        }
    }

    private boolean verifyInput() {
        boolean flag = true;
        StringBuilder error = new StringBuilder();
        String userName = StringUtil.getText(edTxtUserName);
        String desc = StringUtil.getText(edTxtDesc);
        if (!InputValidator.validateTextLength("用户昵称",userName,1, 15, error)) {
            flag = false;
        }
        if (flag) {
            user.setName(userName);
            user.setDescription(desc);
        } else {
            ToastUtil.showLong(this, error);
        }
        return flag;
    }

    public void setUser(User user) {
        if (user != null) {
            this.user = user;
            edTxtUserName.setText(user.getName());
            edTxtDesc.setText(user.getDescription());
        }
    }
}
