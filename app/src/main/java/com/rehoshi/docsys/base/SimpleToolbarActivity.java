package com.rehoshi.docsys.base;

import android.widget.TextView;

import com.rehoshi.docsys.R;


/**
 * Created by hoshino on 2018/12/28.
 */

public class SimpleToolbarActivity extends BaseActivity {


    public void setSPToolbarTitle(CharSequence titleString) {
        setSPToolbarText(titleString);
    }

    public void setSPToolbarText(CharSequence titleString) {
        this.setToolbar(this.findViewById(R.id.toolbar));
        TextView title = this.findViewById(R.id.txtV_toolbarTitle);
        if (titleString.length() > 7) {
            titleString = titleString.subSequence(0, 7) + "...";
        }
        title.setText(titleString);
    }
}
