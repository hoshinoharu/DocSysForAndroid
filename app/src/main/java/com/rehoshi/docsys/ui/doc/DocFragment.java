package com.rehoshi.docsys.ui.doc;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mcxtzhang.swipemenulib.SwipeMenuLayout;
import com.rehoshi.docsys.R;
import com.rehoshi.docsys.base.BaseFragment;
import com.rehoshi.docsys.control.Launcher;
import com.rehoshi.docsys.domain.Doc;
import com.rehoshi.docsys.domain.User;
import com.rehoshi.docsys.service.DocApi;
import com.rehoshi.docsys.smt.SmtRefreshPageController;
import com.rehoshi.docsys.widget.dialog.ConfirmDialog;
import com.rehoshi.simple.adapter.fast.HoshiAdapter;
import com.rehoshi.simple.utils.FormatUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class DocFragment extends BaseFragment {

    @BindView(R.id.smtRf_refresher)
    SmartRefreshLayout refreshLayout;

    @BindView(R.id.rclV_docList)
    RecyclerView docList;
//
//    @BindView(R.id.toolbar)
//    Toolbar toolbar ;
//

    SmtRefreshPageController<Doc, List<Doc>> controller ;

    HoshiAdapter<Doc> adapter = new HoshiAdapter<>() ;

    Unbinder bind;

    private String key = "" ;

    @Override
    protected void onFinishCreate(View contentView) {
        controller = new SmtRefreshPageController<Doc, List<Doc>>() {
            @Override
            protected List<Doc> convertApiData(List<Doc> apiData) {
                return apiData;
            }
        } ;

        adapter.setItemLayoutDelegate(viewType -> R.layout.list_item_doc);


        adapter.addDataRender(($, doc, position) -> {
            $.setText(R.id.txtVw_userName, doc.getTitle());
            $.setText(R.id.txtVw_creatorName, "（"+doc.getCreator().getName()+"）");
            $.setText(R.id.txtVw_searchContent, doc.getTag());
            $.setText(R.id.txtVw_createTime, FormatUtil.formatDate(doc.getCreateTime()));
            $.setText(R.id.txtVw_category, doc.getCategoryDesc());


            SwipeMenuLayout layout = (SwipeMenuLayout) $.findViewById(R.id.swipe);
            if(User.getCurUser().isAdmin()) {
                $.setOnClick(R.id.btn_delete, v -> {
                    ConfirmDialog.showConfirm(getFragmentManager(), "确定删除吗", confirmDialog -> {
                        $(DocApi.class)
                                .del(doc.getId())
                                .onCallSuccess((data, msg, result) -> {
                                    adapter.removeDataAt($.getAdapterPosition());
                                }).linkStart();

                    });
                });
            }else {
                layout.setSwipeEnable(false);
            }

            $.setOnClick(R.id.cstLot_container,v -> {
                Launcher.getInstance().launchDocUpdate(getBaseActivity(), doc);
            });
        });

        docList.setLayoutManager(new LinearLayoutManager(getBaseActivity()));
        docList.setAdapter(adapter);
        controller.setQueryGenerator((pageIndex, pageSize) -> $(DocApi.class).listInPage(pageIndex, pageSize, key));
        controller.control(adapter, refreshLayout, docList);
    }

    @Override
    public void onCreateContentView(Bundle savedInstanceState) {
        super.onCreateContentView(savedInstanceState);
        bind = ButterKnife.bind(this, getContentView());
    }

    @Override
    protected View createContentView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_doc, container, false);
    }

//    @OnClick(R.id.imgBtn_dynamicAdd)
    public void forwardUpdate(){
        startActivity(new Intent(getBaseActivity(), DocUpdateActivity.class));
    }

    boolean first = true ;
    @Override
    public void onResume() {
        super.onResume();
        if(!first){
        refreshLayout.autoRefresh();
        }
            first = false ;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        bind.unbind();
    }

    public void search(String key){
        this.key = key ;
        refreshLayout.autoRefresh();
    }
}
