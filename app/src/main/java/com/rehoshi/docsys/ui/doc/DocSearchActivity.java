package com.rehoshi.docsys.ui.doc;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.rehoshi.docsys.R;
import com.rehoshi.docsys.base.BaseActivity;

import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;
import butterknife.ButterKnife;

public class DocSearchActivity extends BaseActivity {

    @BindView(R.id.edTxt_search)
    EditText edTxtSearch;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private DocFragment docFragment ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc_search);
        ButterKnife.bind(this);
        setToolbar(toolbar);
        initView() ;
    }

    private void initView() {
        docFragment = new DocFragment() ;
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.searchResult, docFragment)
                .commit() ;

        edTxtSearch.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        edTxtSearch.setOnEditorActionListener((textView, i, keyEvent) -> {
            boolean flag = false;
            if (i == EditorInfo.IME_ACTION_SEND || (keyEvent != null
                    && keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                String s = edTxtSearch.getText().toString();
                docFragment.search(s);
                flag = true;
            }
            return flag;
        });
        edTxtSearch.setText(getIntent().getStringExtra("key"));
        edTxtSearch.post(() -> {
            edTxtSearch.onEditorAction(EditorInfo.IME_ACTION_SEND);
        });
    }
}
