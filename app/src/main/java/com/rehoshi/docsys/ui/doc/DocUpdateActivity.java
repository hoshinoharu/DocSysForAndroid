package com.rehoshi.docsys.ui.doc;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.dfqin.grantor.PermissionListener;
import com.github.dfqin.grantor.PermissionsUtil;
import com.leon.lfilepickerlibrary.LFilePicker;
import com.rehoshi.docsys.MainActivity;
import com.rehoshi.docsys.R;
import com.rehoshi.docsys.base.BaseActivity;
import com.rehoshi.docsys.domain.Doc;
import com.rehoshi.docsys.domain.User;
import com.rehoshi.docsys.service.DocApi;
import com.rehoshi.simple.utils.JsonUtil;
import com.rehoshi.simple.utils.StringUtil;
import com.rehoshi.simple.utils.ToastUtil;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import droidninja.filepicker.FilePickerBuilder;
import droidninja.filepicker.FilePickerConst;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class DocUpdateActivity extends BaseActivity {

    @BindView(R.id.edTxt_tag)
    MaterialEditText edTxtTag;

    @BindView(R.id.spn_category)
    Spinner spnCategory;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.edTxt_title)
    EditText edTxt_title;

    @BindView(R.id.btn_submit)
    Button btn_submit;

    @BindView(R.id.txtV_toolbarTitle)
    TextView txtV_toolbarTitle;

    @BindView(R.id.txtVw_originPath)
    TextView originPath;

    @BindView(R.id.txtVw_docContent)
    TextView docContent;

    private Doc doc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc_update);
        ButterKnife.bind(this);
        initView();
        Doc doc = JsonUtil.readFromIntent(Doc.class, getIntent());
        setDoc(doc);
        if(doc != null){
            txtV_toolbarTitle.setText("文档详情");
            if(User.getCurUser().isAdmin()){
                btn_submit.setText("保存文档");
            }else {
                btn_submit.setVisibility(View.GONE);
            }
        }

    }

    private void initView() {
        txtV_toolbarTitle.setText("上传文档");
        setToolbar(toolbar);

        spnCategory.setAdapter(new ArrayAdapter<>(getSelf(), android.R.layout.simple_spinner_dropdown_item
                , Arrays.asList(
                "新闻",
                "政策",
                "养老金",
                "其他"
        )));

        spnCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        getDoc().setCategory(Doc.Category.NEWS);
                        break;
                    case 1:
                        getDoc().setCategory(Doc.Category.POLICY);
                        break;
                    case 2:
                        getDoc().setCategory(Doc.Category.PENSION);
                        break;
                    case 3:
                        getDoc().setCategory(Doc.Category.OTHERS);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayList<String> fileList = new ArrayList<>();

        originPath.setOnClickListener(v -> {
            PermissionsUtil.requestPermission(this, new PermissionListener() {
                        @Override
                        public void permissionGranted(@NonNull String[] permission) {
//                            FilePickerBuilder.getInstance()
//                                    .setSelectedFiles(fileList)
//                                    .setMaxCount(1)
//                                    .pickFile(getSelf());
                            new LFilePicker()
                                    .withActivity(getSelf())
                                    .withRequestCode(9527)
                                    .withStartPath("/storage/emulated/0/Download")
                                    .withIsGreater(false)
                                    .withFileSize(500 * 1024)
                                    .withMutilyMode(false)
                                    .start();
                        }

                        @Override
                        public void permissionDenied(@NonNull String[] permission) {
                        }
                    },
                    Manifest.permission.WRITE_EXTERNAL_STORAGE);
        });

        docContent.setMovementMethod(ScrollingMovementMethod.getInstance());
    }

    @OnClick({R.id.btn_submit})
    public void prepareSubmit() {
        if (checkInput()) {
            if (doc.getId() != null) {
                $(DocApi.class)
                        .update(doc)
                        .onCallSuccess((data, msg, result) -> {
//                            ToastUtil.showLong(getSelf(), "修改成功");
                        })
                        .linkStart();
            } else {
                doc.setCreatorId(User.getCurUser().getId());
                doc.setCreateTime(new Date());
                $(DocApi.class)
                        .add(doc)
                        .onCallSuccess((data, msg, result) -> {
                            ToastUtil.showLong(getSelf(), "上传成功");
                        })
                        .linkStart();
            }
        }
    }

    private boolean checkInput() {
        String title = StringUtil.getText(edTxt_title);
        if (title.equals("")) {
            ToastUtil.showLong(getSelf(), "请输入标题");
            return false;
        }

        String tag = StringUtil.getText(edTxtTag);
        if (tag.equals("")) {
            ToastUtil.showLong(getSelf(), "请输入标签");
            return false;
        }

        String path = StringUtil.getText(originPath);
        if (path.equals("")) {
            ToastUtil.showLong(getSelf(), "请选择一个文档");
            return false;
        }

        getDoc().setTitle(title);
        getDoc().setTag(tag);
        getDoc().setOriginPath(path);
        return true;
    }

    public void setDoc(Doc doc) {
        this.doc = doc;
        if (doc != null) {
            edTxt_title.setText(doc.getTitle());
            edTxtTag.setText(doc.getTag());
            originPath.setText(doc.getOriginPath());
            docContent.setText(doc.getContent());
            switch (doc.getCategory()){
                case Doc.Category.NEWS:
                    spnCategory.setSelection(0);
                    break;
                case Doc.Category.POLICY:
                    spnCategory.setSelection(1);
                    break;
                case Doc.Category.PENSION:
                    spnCategory.setSelection(2);
                    break;
                case Doc.Category.OTHERS:
                    spnCategory.setSelection(3);
                    break;

            }
        } else {
            edTxt_title.setText("");
            edTxtTag.setText("");
            originPath.setText("");
            docContent.setText("");
        }
    }

    public Doc getDoc() {
        if (doc == null) {
            doc = new Doc();
        }
        return doc;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 9527:
            case FilePickerConst.REQUEST_CODE_DOC:
                if (resultCode == Activity.RESULT_OK && data != null) {
                    ArrayList<String> list = data.getStringArrayListExtra("paths");
                    if (list != null && list.size() > 0) {
                        String path = list.get(0);
                        originPath.setText(path);

                        File file = new File(path);
                        edTxt_title.setText(file.getName());
                        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                            String urlName = "file";
                        try {
                            urlName = URLEncoder.encode(file.getName(), "UTF-8");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        MultipartBody.Part body = MultipartBody.Part.createFormData("file",urlName , requestFile);
                        ToastUtil.showLong(this, "上传文件中");
                        $(DocApi.class)
                                .upload(body)
                                .onCallSuccess((wrapper, msg, result) -> {
                                    docContent.setText(wrapper.getContent());
                                    getDoc().setDocUrl(wrapper.getPathAtServer());
                                }).linkStart();
                    }
                }
                break;
        }
    }

    public static byte[] getBytesByFile(String filePath) {
        try {
            File file = new File(filePath);
            //获取输入流
            FileInputStream fis = new FileInputStream(file);

            //新的 byte 数组输出流，缓冲区容量1024byte
            ByteArrayOutputStream bos = new ByteArrayOutputStream(1024);
            //缓存
            byte[] b = new byte[1024];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            //改变为byte[]
            byte[] data = bos.toByteArray();
            //
            bos.close();
            return data;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
