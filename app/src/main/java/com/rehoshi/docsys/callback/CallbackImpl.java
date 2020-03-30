package com.rehoshi.docsys.callback;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.rehoshi.docsys.control.Launcher;
import com.rehoshi.simple.business.net.query.QueryManager;
import com.rehoshi.simple.business.net.retrofit_2.HoshiObservable;
import com.rehoshi.simple.utils.ToastUtil;

import java.io.PrintWriter;
import java.io.StringWriter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by hoshino on 2018/12/14.
 */

public class CallbackImpl<T> extends HoshiObservable<RespData<T>> implements Observer<RespData<T>> {

    private int viewId;

    private boolean showToast = true;

    public interface OnCallResponse<T> {
        void onCallResponse(RespData<T> result);
    }

    public interface OnCallError<T> {
        void onCallError(RespData<T> result, Throwable error);
    }

    public interface OnCallComplete<T> {
        void onCallComplete(RespData<T> result);
    }

    public interface OnCallSuccess<T> {
        void onCallSuccess(T data, String msg, RespData<T> result);
    }

    public interface OnCallFail<T> {
        void onCallFail(T data, String msg, RespData<T> result);
    }

    public interface OnCallTokenTimeOut<T> {
        void onCallTokenTimeOut(T data, String msg, RespData<T> result);
    }

    private OnCallResponse<T> onCallResponse;
    private OnCallComplete<T> onCallComplete;
    private OnCallError<T> onCallError;
    private OnCallSuccess<T> onCallSuccess;
    private OnCallFail<T> onCallFail;
    private OnCallTokenTimeOut<T> onCallTokenTimeOut;


    public CallbackImpl(Observable<RespData<T>> upstream) {
        super(upstream);
    }

    public CallbackImpl<T> linkStart(int queryId) {
        if (!QueryManager.getInstance().duringQuery(queryId)) {
            this.viewId = queryId;
            QueryManager.getInstance().put(queryId);
            this.normalThreadStrategy().subscribe(this);
        }
        return this;
    }

    public CallbackImpl<T> linkStart() {
        return this.linkStart(null);
    }

    /**
     * 开始连接
     *
     * @return
     */
    public CallbackImpl<T> linkStart(Activity activity) {
        int viewId;
        if (activity == null) {
            viewId = 1;
        } else {
            viewId = activity.hashCode();
        }
//        //绑定发送事件的view不一样
//        if (!QueryManager.getInstance().duringQuery(viewId)) {
//            this.viewId = viewId;
//            QueryManager.getInstance().put(viewId, activity);
//            this.normalThreadStrategy().subscribe(this);
//        }

        linkStart(viewId, activity);

        return this;
    }

    public CallbackImpl<T> linkStart(int viewId, Activity activity) {
        //正在结束 或者 已经销毁的 Activity 不发出请求
        if (activity == null || !(activity.isFinishing() || activity.isDestroyed())) {
            //绑定发送事件的view不一样
            if (!QueryManager.getInstance().duringQuery(viewId)) {
                this.viewId = viewId;
                QueryManager.getInstance().put(viewId, activity);
                this.normalThreadStrategy().subscribe(this);
            }
        }
        return this;
    }

    public CallbackImpl<T> linkStart(int viewId, Fragment fragment) {
        if (fragment == null || judgeFragment(fragment)) {
            //绑定发送事件的view不一样
            if (!QueryManager.getInstance().duringQuery(viewId)) {
                this.viewId = viewId;
                QueryManager.getInstance().put(viewId, fragment);
                this.normalThreadStrategy().subscribe(this);
            }
        }
        return this;
    }

    /**
     * 判断 碎片 能否 发送请求
     *
     * @param fragment
     * @return
     */
    private boolean judgeFragment(Fragment fragment) {
        boolean flag = true;
        FragmentActivity activity = fragment.getActivity();
        if (fragment.isDetached()) {
            flag = false;
        } else {
            if (activity != null) {
                if (activity.isFinishing() || activity.isDestroyed()) {
                    flag = false;
                }
            }else {
                flag = false ;
            }
        }
        return flag;
    }

    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onNext(RespData<T> respData) {
        try {
            if (!QueryManager.getInstance().isValidate(this.viewId)) {
                return;
            }
            if (this.onCallResponse != null) {
                this.onCallResponse.onCallResponse(respData);
            }

            if (showToast) {
                ToastUtil.showLong(Launcher.getInstance().getTopActivity(), respData.getMsg());
                Context context = QueryManager.getInstance().getContext(viewId);
                if (context != null) { }
            }
            switch (respData.getCode()) {
                case RespData.Code.SUCCEED:
                    if (this.onCallSuccess != null) {
                        this.onCallSuccess.onCallSuccess(respData.getData(), respData.getMsg(), respData);
                    }
                    break;
                case RespData.Code.TOKEN_TIME_OUT://token过期
                    //token过期跳转登录 否则直接跳转主界面
                    Launcher.getInstance().backToLogin();
                    if (this.onCallTokenTimeOut != null) {
                        this.onCallTokenTimeOut.onCallTokenTimeOut(respData.getData(), respData.getMsg(), respData);
                    }
                    break;
                default:
                    if (this.onCallFail != null) {
                        this.onCallFail.onCallFail(respData.getData(), respData.getMsg(), respData);
                    }
                    break;
            }
            this.onComplete();
        } catch (Exception e) {
            onError(e);
        }
    }

    private void printError(Throwable e) {
        StringWriter stringWriter = new StringWriter();
        e.printStackTrace(new PrintWriter(stringWriter));
        Log.e("TAG", stringWriter.toString());
        Context context = QueryManager.getInstance().getContext(viewId);
        if (context != null) {
            ToastUtil.showLong(context, "网络错误");
        }
    }

    @Override
    public void onError(Throwable e) {
        printError(e);
        this.onComplete();
        if (this.onCallError != null) {
            this.onCallError.onCallError(null, e);
        }
    }

    @Override
    public void onComplete() {
        QueryManager.getInstance().queryComplete(viewId);
        if (this.onCallComplete != null) {
            this.onCallComplete.onCallComplete(null);
        }
    }

    public CallbackImpl<T> onCallResponse(OnCallResponse<T> onCallResponse) {
        this.onCallResponse = onCallResponse;
        return this;
    }

    public CallbackImpl<T> onCallComplete(OnCallComplete<T> onCallComplete) {
        this.onCallComplete = onCallComplete;
        return this;
    }

    public CallbackImpl<T> onCallError(OnCallError<T> onCallError) {
        this.onCallError = onCallError;
        return this;
    }

    public CallbackImpl<T> onCallSuccess(OnCallSuccess<T> onCallSuccess) {
        this.onCallSuccess = onCallSuccess;
        return this;
    }

    public CallbackImpl<T> onCallFail(OnCallFail<T> onCallFail) {
        this.onCallFail = onCallFail;
        return this;
    }

    public CallbackImpl<T> onCallTokenTimeOut(OnCallTokenTimeOut<T> onCallTokenTimeOut) {
        this.onCallTokenTimeOut = onCallTokenTimeOut;
        return this;
    }

    public CallbackImpl<T> setShowToast(boolean showToast) {
        this.showToast = showToast;
        return this;
    }
}
