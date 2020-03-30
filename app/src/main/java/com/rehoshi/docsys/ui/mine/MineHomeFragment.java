package com.rehoshi.docsys.ui.mine;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.rehoshi.docsys.R;
import com.rehoshi.docsys.base.BaseFragment;
import com.rehoshi.docsys.domain.User;
import com.rehoshi.docsys.domain.UserLog;
import com.rehoshi.docsys.ui.LoginActivity;
import com.rehoshi.docsys.ui.log.UserLogActivity;
import com.rehoshi.simple.adapter.fast.HoshiAdapter;
import com.rehoshi.simple.utils.FormatUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by hoshino on 2018/12/24.
 */

public class MineHomeFragment extends BaseFragment {

    @BindView(R.id.txtV_mineTitle)
    TextView txtVMineTitle;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.imgVw_docIcon)
    CircleImageView imgVwUserHead;

    @BindView(R.id.txtV_userName)
    TextView txtVUserName;

    @BindView(R.id.txtV_desc)
    TextView txtVDesc;

    @BindView(R.id.txtV_role)
    TextView txtVRole;

    @BindView(R.id.rclV_operateItems)
    RecyclerView rclVOperateItems;

    @BindView(R.id.smtRf_refresher)
    SmartRefreshLayout smtRfRefresher;

    Unbinder unbinder;

    HoshiAdapter<OpItem> adapter;

    private class OpItem {
        int icon;
        String title;
        View.OnClickListener listener;

        public OpItem(int icon, String title, View.OnClickListener listener) {
            this.icon = icon;
            this.title = title;
            this.listener = listener;
        }
    }

    List<OpItem> opItems;

    @Override
    protected View createContentView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_mine_home, container, false);
    }

    @Override
    public void onCreateContentView(Bundle savedInstanceState) {
        super.onCreateContentView(savedInstanceState);
        unbinder = ButterKnife.bind(this, getContentView());
    }

    @Override
    protected void onFinishCreate(View contentView) {
        opItems = new ArrayList<>();
        adapter = new HoshiAdapter<>();

        adapter.setEmptyText("");

        opItems.add( new OpItem(R.drawable.my_info, "修改信息", view -> {
            startActivity(new Intent(getBaseActivity(), UserInfoUpdateActivity.class));
        }));
        if(User.getCurUser().isAdmin()){
            opItems.add( new OpItem(R.drawable.ic_word_doc, "查询日志", view -> {
                startActivity(new Intent(getBaseActivity(), UserLogActivity.class));
            }));
        }

        adapter.addDataRender(($, opItem, position) -> {
            $.setText(R.id.txtV_opTitle, opItem.title);
            ImageView view = (ImageView) $.findViewById(R.id.imgV_opIcon);
            view.setImageResource(opItem.icon);
            $.setItemClick(opItem.listener);
        });
        adapter.setItemLayoutDelegate(viewType -> R.layout.list_item_mine_op);

        rclVOperateItems.setLayoutManager(new LinearLayoutManager(getBaseActivity()));
        rclVOperateItems.setAdapter(adapter);
        adapter.resetDataSource(opItems);
    }

    @Override
    public void onResume() {
        super.onResume();
        setUser(User.getCurUser(getBaseActivity()));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.imgVw_docIcon)
    public void choosePortrait() {
//        PictureSelectUtil.selectPicture(getBaseActivity(), true, 1);
    }


    private void setUser(User user) {
//        ImageManager.getInstance().loadIntoImage(getBaseActivity(), user.getPortrait(), imgVwUserHead);
        txtVUserName.setText(user.getName());
        txtVDesc.setText(user.getDescription());
        txtVRole.setText(FormatUtil.formatString("用户"));
    }

    @OnClick(R.id.btn_logout)
    public void logout() {

        User curUser = User.getCurUser(getBaseActivity());
        curUser.logout(getBaseActivity());
        getBaseActivity().finish();
        startActivity(new Intent(getBaseActivity(), LoginActivity.class));
    }
}

