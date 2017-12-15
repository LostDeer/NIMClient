package meng.mengyu.session.extension.Tip;

import com.alibaba.fastjson.JSONObject;

import meng.mengyu.session.extension.CustomAttachment;
import meng.mengyu.session.extension.CustomAttachmentType;

/**
 * Created by ${LostDeer} on 2017/12/14.
 * Github:https://github.com/LostDeer
 * 第三步，继承这个基类，实现“骰子”的附件类型。
 */
public class TipCustomAttachment extends CustomAttachment {
    private String mTip;
    private final String TIP_KEY = "TIP_KEY";
    TipCustomAttachment() {
        super(CustomAttachmentType.Tip);
    }
    public TipCustomAttachment(String tip) {
        this();
        mTip = tip;
    }

    public String getTip() {
        return mTip;
    }

    public void setTip(String tip) {
        mTip = tip;
    }

    @Override
    protected void parseData(JSONObject data) {
        this.mTip = data.getString(TIP_KEY);

    }

    @Override
    protected JSONObject packData() {
        JSONObject data = new JSONObject();
        data.put(TIP_KEY, mTip);
        return data;
    }
}
