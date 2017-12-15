package meng.mengyu.session.action;

import com.netease.nim.uikit.business.session.actions.BaseAction;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.model.IMMessage;

import meng.mengyu.R;
import meng.mengyu.session.extension.Prescription.PrescriptionAttachment;

/**
 * Created by ${LostDeer} on 2017/12/14.
 * Github:https://github.com/LostDeer
 * 开药方
 */

public class PrescriptionAction extends BaseAction {
    /**
     * 构造函数
     *
     * @param iconResId 图标 res id
     * @param titleId   图标标题的string res id
     */
    public PrescriptionAction() {
        super(R.mipmap.ic_launcher_round, R.string.prescription);
    }

    @Override
    public void onClick() {
        PrescriptionAttachment prescriptionAttachment = new PrescriptionAttachment();
        IMMessage message = MessageBuilder.createCustomMessage(
                                getAccount(), getSessionType(), prescriptionAttachment
                        );
        sendMessage(message);
    }
}
