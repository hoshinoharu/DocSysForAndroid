package com.rehoshi.docsys.ui.home;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.mcxtzhang.swipemenulib.SwipeMenuLayout;
import com.rehoshi.docsys.R;
import com.rehoshi.docsys.base.BaseFragment;
import com.rehoshi.docsys.control.Launcher;
import com.rehoshi.docsys.domain.CategoryItem;
import com.rehoshi.docsys.domain.Doc;
import com.rehoshi.docsys.domain.User;
import com.rehoshi.docsys.service.DocApi;
import com.rehoshi.simple.adapter.fast.HoshiAdapter;
import com.rehoshi.simple.utils.FormatUtil;
import com.youth.banner.Banner;
import com.youth.banner.loader.ImageLoaderInterface;

import java.util.Arrays;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class HomeFragment extends BaseFragment {


    @BindView(R.id.banner)
    Banner banner;

    @BindView(R.id.rclVw_chooseList)
    RecyclerView rclVwChooseList;

    @BindView(R.id.rclVw_recommendList)
    RecyclerView rclVwRecommendList;

    @BindView(R.id.imgvw_add)
    ImageView imgVwAdd ;

    @BindView(R.id.edTxt_docSearch)
    EditText docSearch ;

    Unbinder unbinder;

    private HoshiAdapter<CategoryItem> chooseAdapter = new HoshiAdapter<>() ;
    private HoshiAdapter<Doc> recommendAdapter = new HoshiAdapter<>() ;

    @Override
    protected void onFinishCreate(View contentView) {
       unbinder = ButterKnife.bind(this, contentView);

        banner.setImageLoader(new ImageLoaderInterface<ImageView>() {
            @Override
            public void displayImage(Context context, Object path, ImageView imageView) {
                Glide.with(context).load(path.toString()).into(imageView);
            }

            @Override
            public ImageView createImageView(Context context) {
                ImageView imageView = new ImageView(context);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                return imageView;
            }
        });
        banner.setImages(Arrays.asList(
            "http://img4.imgtn.bdimg.com/it/u=3374023079,3459675603&fm=15&gp=0.jpg",
                "http://img5.imgtn.bdimg.com/it/u=3272017232,3965067475&fm=26&gp=0.jpg",
                "http://img1.imgtn.bdimg.com/it/u=2079863409,844895434&fm=26&gp=0.jpg",
                "http://img3.imgtn.bdimg.com/it/u=68805881,936287962&fm=11&gp=0.jpg",
                "http://img1.imgtn.bdimg.com/it/u=3642552853,2486189152&fm=26&gp=0.jpg"
        )) ;
        banner.setOnBannerListener(position -> {
//           Launcher.getInstance().launchDocSearch(getBaseActivity(), "养老");
            Uri uri = Uri.parse("http://www.yanglaocn.com/");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        });
        banner.start() ;

        chooseAdapter.addData(Arrays.asList(
                new CategoryItem(Color.rgb(252, 133, 38),"新闻", Doc.Category.NEWS),
                new CategoryItem(Color.rgb(108, 199, 131),"政策", Doc.Category.POLICY),
                new CategoryItem(Color.rgb(239, 83, 180),"养老金", Doc.Category.PENSION),
                new CategoryItem(Color.rgb(44, 155, 253),"其他", Doc.Category.OTHERS)
        ));
        chooseAdapter.setItemLayoutDelegate(viewType -> R.layout.list_item_home_choose);
        chooseAdapter.addDataRender(($, item, position) -> {
            $.setText(R.id.txtVw_title, item.getTitle());
            ImageView img = (ImageView) $.findViewById(R.id.imgVw_back);
            img.setImageDrawable(new ColorDrawable(item.getColor()));

            $.setOnClick(R.id.imgVw_back, v -> {
                Launcher.getInstance().launchDocSearch(getBaseActivity(), item.getTitle());
            });
        });
        rclVwChooseList.setAdapter(chooseAdapter);
        rclVwChooseList.setLayoutManager(new LinearLayoutManager(getBaseActivity(),LinearLayoutManager.HORIZONTAL, false));
        recommendAdapter.setItemLayoutDelegate(viewType -> R.layout.list_item_doc);
        recommendAdapter.addDataRender(($, doc, position) -> {
            SwipeMenuLayout layout= (SwipeMenuLayout) $.findViewById(R.id.swipe);
            layout.setSwipeEnable(false);
            $.setText(R.id.txtVw_userName, doc.getTitle());
            $.setText(R.id.txtVw_searchContent, doc.getTag());
            $.setText(R.id.txtVw_creatorName, "（"+doc.getCreator().getName()+"）");
            $.setText(R.id.txtVw_createTime, FormatUtil.formatDate(doc.getCreateTime()));
            $.setText(R.id.txtVw_category, doc.getCategoryDesc());
            $.setOnClick(R.id.cstLot_container, v -> {
                Launcher.getInstance().launchDocUpdate(getBaseActivity(), doc);
            });
        });
        rclVwRecommendList.setAdapter(recommendAdapter);
        rclVwRecommendList.setLayoutManager(new LinearLayoutManager(getBaseActivity()));
        docSearch.setOnClickListener(v -> {
            Launcher.getInstance().launchDocSearch(getBaseActivity(), "") ;
        });

        if(User.getCurUser().isAdmin()){
            imgVwAdd.setOnClickListener(v -> {
                Launcher.getInstance().launchDocAdd(getBaseActivity());
            });
        }else {
            imgVwAdd.setVisibility(View.GONE);
        }
    }

    @Override
    protected View createContentView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    public void onResume() {
        super.onResume();
        $(DocApi.class).recommendList(0, 20)
                .onCallSuccess((data, msg, result) -> {
                    recommendAdapter.resetDataSource(data);
                }).linkStart();
    }
}
