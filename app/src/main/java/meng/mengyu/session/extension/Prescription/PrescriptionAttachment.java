package meng.mengyu.session.extension.Prescription;

import com.alibaba.fastjson.JSONObject;

import meng.mengyu.session.extension.CustomAttachment;
import meng.mengyu.session.extension.CustomAttachmentType;

/**
 * Created by ${LostDeer} on 2017/12/14.
 * Github:https://github.com/LostDeer
 */

public class PrescriptionAttachment extends CustomAttachment {


    private String tag;//1.医生

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public PrescriptionAttachment(String type, String content) {
        this();
        this.tag = type;
        this.content = content;
    }


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    private String content;//内容

    public PrescriptionAttachment() {
        super(CustomAttachmentType.Prescription);
    }


    private final String KEY_TAG ="KEY_TAG";
    private final String KEY_CONTENT="KEY_CONTENT";
    @Override
    protected void parseData(JSONObject data) {
        this.tag = data.getString(KEY_TAG);
        this.content = data.getString(KEY_CONTENT);
    }

    @Override
    protected JSONObject packData() {
        JSONObject data = new JSONObject();
        data.put(KEY_TAG, type);
        data.put(KEY_CONTENT, content);
        return data;
    }
}
