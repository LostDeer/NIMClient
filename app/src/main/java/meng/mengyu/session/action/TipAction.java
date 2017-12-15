package meng.mengyu.session.action;


import android.content.Intent;

import com.netease.nim.uikit.business.session.actions.BaseAction;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.model.CustomMessageConfig;
import com.netease.nimlib.sdk.msg.model.IMMessage;

import meng.mengyu.R;

/**
 * Created by ${LostDeer} on 2017/12/14.
 * Github:https://github.com/LostDeer
 */

public class TipAction extends BaseAction {
    /**
     * 构造函数
     *
     * @param iconResId 图标 res id
     * @param titleId   图标标题的string res id
     */
    public TipAction() {
        super(R.mipmap.ic_launcher_round, R.string.tip);
    }

    @Override
    public void onClick() {
//        TipCustomAttachment attachment = new TipCustomAttachment();
//        IMMessage message = MessageBuilder.createCustomMessage(
//                getAccount(), getSessionType(), "我是一条Tip", attachment
//        );
        IMMessage msg = MessageBuilder.createTipMessage(getAccount(), getSessionType());
        msg.setContent("我是一条Tip消息!");

        CustomMessageConfig config = new CustomMessageConfig();
        config.enablePush = false; // 不推送
        msg.setConfig(config);

        sendMessage(msg);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
