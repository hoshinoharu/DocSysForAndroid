package com.rehoshi.docsys.widget.dialog;

import android.view.View;
import android.widget.TextView;

import com.rehoshi.docsys.R;
import com.shehuan.nicedialog.BaseNiceDialog;
import com.shehuan.nicedialog.ViewHolder;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by hoshino on 2019/1/19.
 */

public class ConfirmDialog extends BaseNiceDialog implements View.OnClickListener {


    public interface OnCancelListener {
        void onCancel(ConfirmDialog confirmDialog);
    }

    public interface OnConfirmListener {
        void onConfirm(ConfirmDialog confirmDialog);
    }

    @BindView(R.id.txtV_title)
    TextView txtVTitle;
    @BindView(R.id.txtV_info)
    TextView txtVInfo;
    @BindView(R.id.btn_cancel)
    TextView btnCancel;
    @BindView(R.id.btn_confirm)
    TextView btnConfirm;

    Unbinder unbinder;
    private String title;
    private String info;
    private OnCancelListener onCancelListener;
    private OnConfirmListener onConfirmListener;

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private static ConfirmDialog instance;

    @Override
    public int intLayoutId() {
        return R.layout.dialog_confirm;
    }

    @Override
    public void convertView(ViewHolder viewHolder, BaseNiceDialog baseNiceDialog) {
        View container = viewHolder.getView(R.id.container);
        unbinder = ButterKnife.bind(this, container);
        container.setOnClickListener(view -> dismiss());
        txtVTitle.setText(title);
        txtVInfo.setText(info);
        btnCancel.setOnClickListener(this);
        btnConfirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_cancel:
                if (this.onCancelListener != null) {
                    this.onCancelListener.onCancel(this);
                }
                break;
            case R.id.btn_confirm:
                if (this.onConfirmListener != null) {
                    this.onConfirmListener.onConfirm(this);
                }
                break;
        }
        dismiss();
    }

    public static ConfirmDialog getInstance() {
        if (instance == null) {
            instance = new ConfirmDialog();
        }
        return instance;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public void setOnCancelListener(OnCancelListener onCancelListener) {
        this.onCancelListener = onCancelListener;
    }

    public void setOnConfirmListener(OnConfirmListener onConfirmListener) {
        this.onConfirmListener = onConfirmListener;
    }

    public static void show(FragmentManager fragmentManager, String title, String info, OnCancelListener onCancelListener, OnConfirmListener onConfirmListener) {
        ConfirmDialog instance = getInstance();
        instance.setTitle(title);
        instance.setInfo(info);
        instance.setOnCancelListener(onCancelListener);
        instance.setOnConfirmListener(onConfirmListener);
        instance.show(fragmentManager);
    }

    public static void show(FragmentActivity activity, String title, String info, OnCancelListener onCancelListener, OnConfirmListener onConfirmListener) {
        show(activity.getSupportFragmentManager(), title, info, onCancelListener, onConfirmListener);
    }

    public static void showConfirm(FragmentManager fragmentManager, String inof, OnConfirmListener onConfirmListener) {
        show(fragmentManager, "提示", inof, null, onConfirmListener);
    }
}
