package com.rehoshi.docsys.ui.log;

import android.os.Bundle;
import android.widget.TextView;

import com.rehoshi.docsys.R;
import com.rehoshi.docsys.base.BaseActivity;
import com.rehoshi.docsys.domain.UserLog;
import com.rehoshi.docsys.service.UserLogApi;
import com.rehoshi.docsys.smt.SmtRefreshPageController;
import com.rehoshi.simple.adapter.fast.HoshiAdapter;
import com.rehoshi.simple.utils.FormatUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.List;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class UserLogActivity extends BaseActivity {

    @BindView(R.id.txtV_mineTitle)
    TextView txtVMineTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.rclV_logList)
    RecyclerView rclVLogList;
    @BindView(R.id.smtRf_refresher)
    SmartRefreshLayout smtRfRefresher;

    HoshiAdapter<UserLog> adapter = new HoshiAdapter<>() ;

    SmtRefreshPageController<UserLog, List<UserLog>> controller ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_log);
        ButterKnife.bind(this);
        setToolbar(toolbar);
        initView();
    }

    private void initView() {

        adapter.setItemLayoutDelegate(viewType -> R.layout.list_item_user_log);

        adapter.addDataRender(($, userLog, position) -> {
            $.setText(R.id.txtVw_userName, userLog.getUser().getName());
            $.setText(R.id.txtVw_searchContent, userLog.getSearchContent());
            $.setText(R.id.txtVw_createTime, FormatUtil.formatDate(userLog.getSearchTime()));
        });

        controller = new SmtRefreshPageController<UserLog, List<UserLog>>() {
            @Override
            protected List<UserLog> convertApiData(List<UserLog> apiData) {
                return apiData ;
            }
        };

        controller.setQueryGenerator((pageIndex, pageSize) -> $(UserLogApi.class).list(pageIndex, pageSize));

        rclVLogList.setAdapter(adapter);
        rclVLogList.setLayoutManager(new LinearLayoutManager(this));
        controller.control(adapter, smtRfRefresher, rclVLogList);
    }
}
