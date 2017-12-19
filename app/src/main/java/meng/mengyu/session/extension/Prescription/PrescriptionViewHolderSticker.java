package meng.mengyu.session.extension.Prescription;

import android.widget.FrameLayout;
import android.widget.Toast;

import com.netease.nim.uikit.business.session.viewholder.MsgViewHolderBase;
import com.netease.nim.uikit.common.ui.recyclerview.adapter.BaseMultiItemFetchLoadAdapter;
import com.netease.nim.uikit.impl.NimUIKitImpl;

import meng.mengyu.R;

/**
 * Created by zhoujianghua on 2015/8/7.
 */
public class PrescriptionViewHolderSticker extends MsgViewHolderBase {
    private String mContent;

    private FrameLayout baseView;

    public PrescriptionViewHolderSticker(BaseMultiItemFetchLoadAdapter adapter) {
        super(adapter);
    }

    @Override
    protected int getContentResId() {
        return R.layout.nim_message_item_prescription;
    }

    @Override
    protected void inflateContentView() {
        baseView = (FrameLayout) findViewById(R.id.fl_prescription);
        //        baseView.setMaxWidth(MsgViewHolderThumbBase.getImageMaxEdge());
    }

    @Override
    protected void bindContentView() {
        layoutDirection();
        PrescriptionAttachment attachment = (PrescriptionAttachment) message.getAttachment();
        mContent = attachment.getContent();

        //        if (attachment == null) {
        //            return;
        //        }
        //
        //        Glide.with(context)
        //                .load(StickerManager.getInstance().getStickerUri(attachment.getCatalog(), attachment.getChartlet()))
        //                .apply(new RequestOptions()
        //                        .error(com.netease.nim.uikit.R.drawable.nim_default_img_failed)
        //                        .diskCacheStrategy(DiskCacheStrategy.NONE))
        //                .into(baseView);
    }

    private void layoutDirection() {
        if (isReceivedMessage()) {
            baseView.setBackgroundResource(NimUIKitImpl.getOptions().messageLeftBackground);
        } else {
            baseView.setBackgroundResource(NimUIKitImpl.getOptions().messageRightBackground);
        }
    }

    @Override
    protected void onItemClick() {
        super.onItemClick();
        Toast.makeText(context, mContent, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected int leftBackground() {
        return 0;
    }

    @Override
    protected int rightBackground() {
        return 0;
    }
}
