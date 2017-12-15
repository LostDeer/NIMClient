package meng.mengyu.session;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.api.model.recent.RecentCustomization;
import com.netease.nim.uikit.api.model.session.SessionCustomization;
import com.netease.nim.uikit.api.model.session.SessionEventListener;
import com.netease.nim.uikit.api.wrapper.NimMessageRevokeObserver;
import com.netease.nim.uikit.business.session.actions.BaseAction;
import com.netease.nim.uikit.business.session.module.MsgForwardFilter;
import com.netease.nim.uikit.business.session.module.MsgRevokeFilter;
import com.netease.nim.uikit.impl.customization.DefaultRecentCustomization;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.avchat.model.AVChatAttachment;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.attachment.MsgAttachment;
import com.netease.nimlib.sdk.msg.constant.AttachStatusEnum;
import com.netease.nimlib.sdk.msg.constant.MsgDirectionEnum;
import com.netease.nimlib.sdk.msg.constant.MsgTypeEnum;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.msg.model.RecentContact;
import com.netease.nimlib.sdk.robot.model.RobotAttachment;

import java.util.ArrayList;

import meng.mengyu.R;
import meng.mengyu.config.NimCache;
import meng.mengyu.session.action.PrescriptionAction;
import meng.mengyu.session.action.TipAction;
import meng.mengyu.session.extension.CustomAttachParser;
import meng.mengyu.session.extension.GuessAttachment;
import meng.mengyu.session.extension.Prescription.PrescriptionAttachment;
import meng.mengyu.session.extension.Prescription.PrescriptionViewHolderSticker;
import meng.mengyu.session.extension.RTSAttachment;
import meng.mengyu.session.extension.RedPacketAttachment;
import meng.mengyu.session.extension.RedPacketOpenedAttachment;
import meng.mengyu.session.extension.SnapChatAttachment;
import meng.mengyu.session.extension.Sticker.MsgViewHolderSticker;
import meng.mengyu.session.extension.Sticker.StickerAttachment;
import meng.mengyu.session.extension.Tip.MsgViewHolderTip;

/**
 * Created by ${LostDeer} on 2017/12/14.
 * Github:https://github.com/LostDeer
 */

public class SessionHelper {

    public static void init() {
        // 注册自定义消息附件解析器
        NIMClient.getService(MsgService.class).registerCustomAttachmentParser(new CustomAttachParser());
        // 注册各种扩展消息类型的显示ViewHolder
        registerViewHolders();
        // 设置会话中点击事件响应处理
        setSessionListener();
        // 注册消息转发过滤器
        registerMsgForwardFilter();

        // 注册消息撤回过滤器
        registerMsgRevokeFilter();

        // 注册消息撤回监听器
        registerMsgRevokeObserver();


        NimUIKit.setCommonP2PSessionCustomization(getP2pCustomization());

        NimUIKit.setRecentCustomization(getRecentCustomization());
    }

    private static RecentCustomization getRecentCustomization() {
        DefaultRecentCustomization customization = new DefaultRecentCustomization() {
            @Override
            public String getDefaultDigest(RecentContact recent) {
                MsgAttachment attachment = recent.getAttachment();
                if (attachment instanceof GuessAttachment) {
                    GuessAttachment guess = (GuessAttachment) attachment;
                    return guess.getValue().getDesc();
                } else if (attachment instanceof RTSAttachment) {
                    return "[白板]";
                } else if (attachment instanceof StickerAttachment) {
                    return "[贴图]";
                } else if (attachment instanceof SnapChatAttachment) {
                    return "[阅后即焚]";
                } else if (attachment instanceof RedPacketAttachment) {
                    return "[红包]";
                } else if (attachment instanceof RedPacketOpenedAttachment) {
                    return ((RedPacketOpenedAttachment) attachment).getDesc(recent.getSessionType(), recent.getContactId());
                } else if(attachment instanceof PrescriptionAttachment){
                    return "[药方]";
                }
                return super.getDefaultDigest(recent);
            }
        };
        return customization;
    }

    /**
     * 消息转发过滤器
     */
    private static void registerMsgForwardFilter() {
        NimUIKit.setMsgForwardFilter(new MsgForwardFilter() {
            @Override
            public boolean shouldIgnore(IMMessage message) {
                if (message.getDirect() == MsgDirectionEnum.In
                        && (message.getAttachStatus() == AttachStatusEnum.transferring
                        || message.getAttachStatus() == AttachStatusEnum.fail)) {
                    // 接收到的消息，附件没有下载成功，不允许转发
                    return true;
                } else if (message.getMsgType() == MsgTypeEnum.custom && message.getAttachment() != null
                        && (message.getAttachment() instanceof SnapChatAttachment
                        || message.getAttachment() instanceof RTSAttachment
                        || message.getAttachment() instanceof RedPacketAttachment)) {
                    // 白板消息和阅后即焚消息，红包消息 不允许转发
                    return true;
                } else if (message.getMsgType() == MsgTypeEnum.robot && message.getAttachment() != null && ((RobotAttachment) message.getAttachment()).isRobotSend()) {
                    return true; // 如果是机器人发送的消息 不支持转发
                }
                return false;
            }
        });
    }

    /**
     * 消息撤回过滤器
     */
    private static void registerMsgRevokeFilter() {
        NimUIKit.setMsgRevokeFilter(new MsgRevokeFilter() {
            @Override
            public boolean shouldIgnore(IMMessage message) {
                if (message.getAttachment() != null
                        && (message.getAttachment() instanceof AVChatAttachment
                        || message.getAttachment() instanceof RTSAttachment
                        || message.getAttachment() instanceof RedPacketAttachment)) {
                    // 视频通话消息和白板消息，红包消息 不允许撤回
                    return true;
                } else if (NimCache.getAccount().equals(message.getSessionId())) {
                    // 发给我的电脑 不允许撤回
                    return true;
                }
                return false;
            }
        });
    }

    private static void registerMsgRevokeObserver() {
        NIMClient.getService(MsgServiceObserve.class).observeRevokeMessage(new NimMessageRevokeObserver(), true);
    }

    private static void setSessionListener() {
        SessionEventListener listener = new SessionEventListener() {
            @Override
            public void onAvatarClicked(Context context, IMMessage message) {
                // 一般用于打开用户资料页面
                Toast.makeText(context, "点我干什么???", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAvatarLongClicked(Context context, IMMessage message) {
                // 一般用于群组@功能，或者弹出菜单，做拉黑，加好友等功能
            }
        };
        // 在Application初始化中设置
        NimUIKit.setSessionListener(listener);
    }

    private static void registerViewHolders() {

        NimUIKit.registerMsgItemViewHolder(StickerAttachment.class, MsgViewHolderSticker.class);
        NimUIKit.registerMsgItemViewHolder(PrescriptionAttachment.class, PrescriptionViewHolderSticker.class);
        NimUIKit.registerTipMsgViewHolder(MsgViewHolderTip.class);
    }

    // 定制化单聊界面。如果使用默认界面，返回null即可
    private static SessionCustomization getP2pCustomization() {
        //设置聊天面板内容
        SessionCustomization sessionCustomization = new SessionCustomization(){
            // 由于需要Activity Result， 所以重载该函数。
            @Override
            public void onActivityResult(final Activity activity, int requestCode, int resultCode, Intent data) {
                super.onActivityResult(activity, requestCode, resultCode, data);

            }

            @Override
            public MsgAttachment createStickerAttachment(String category, String item) {
                return new StickerAttachment(category, item);
            }
        };
        ArrayList<BaseAction> actions = new ArrayList<>();
        actions.add(new PrescriptionAction());
        actions.add(new TipAction());
        sessionCustomization.actions = actions;
        sessionCustomization.withSticker=true;

        //设置右边toolbar内容
        ArrayList<SessionCustomization.OptionsButton> buttons = new ArrayList<>();
        SessionCustomization.OptionsButton cloudMsgButton = new SessionCustomization.OptionsButton() {
            @Override
            public void onClick(Context context, View view, String sessionId) {
                Toast.makeText(context, "toolbar", Toast.LENGTH_SHORT).show();
            }
        };

        cloudMsgButton.iconId = R.mipmap.ic_launcher_round;

        buttons.add(cloudMsgButton);
        sessionCustomization.buttons = buttons;

        return sessionCustomization;
    }

    public static void startP2PSession(Context context, String account) {
        startP2PSession(context, account, null);
    }

    public static void startP2PSession(Context context, String account, IMMessage anchor) {
        if (!NimCache.getAccount().equals(account)) {
            if (NimUIKit.getRobotInfoProvider().getRobotByAccount(account) != null) {
                NimUIKit.startChatting(context, account, SessionTypeEnum.P2P, getP2pCustomization(), anchor);
            } else {
                NimUIKit.startP2PSession(context, account, anchor);
            }
        } else {
            NimUIKit.startChatting(context, account, SessionTypeEnum.P2P, getP2pCustomization(), anchor);
        }
    }
}
