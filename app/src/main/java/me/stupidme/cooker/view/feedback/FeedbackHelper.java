package me.stupidme.cooker.view.feedback;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by StupidL on 2017/4/5.
 */

public class FeedbackHelper {

    private FeedbackIntentInfo mInfo;
    private Context mContext;

    public static class Builder {
        private int colorPrimary;
        private int colorPrimaryDark;
        private String email;
        private Context context;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder colorPrimary(int color) {
            this.colorPrimary = color;
            return this;
        }

        public Builder colorPrimaryDark(int color) {
            this.colorPrimaryDark = color;
            return this;
        }

        public Builder emailTo(String email) {
            this.email = email;
            return this;
        }

        public FeedbackHelper build() {
            FeedbackHelper helper = new FeedbackHelper();
            FeedbackIntentInfo info = new FeedbackIntentInfo();
            info.setColorPrimary(this.colorPrimary);
            info.setColorPrimaryDark(this.colorPrimaryDark);
            info.setEmailTo(this.email);
            helper.mInfo = info;
            helper.mContext = this.context;
            return helper;
        }
    }

    public void start() {
        Intent intent = new Intent(mContext, FeedbackActivity.class);
        intent.setAction(FeedbackConstants.ACTION_START_FEEDBACK);
        Bundle bundle = new Bundle();
        bundle.putInt(FeedbackConstants.KEY_COLOR_PRIMARY, mInfo.getColorPrimary());
        bundle.putInt(FeedbackConstants.KEY_COLOR_PRIMARY_DARK, mInfo.getColorPrimaryDark());
        bundle.putString(FeedbackConstants.KEY_EMAIL_TO, mInfo.getEmailTo());
        intent.putExtras(bundle);
        mContext.startActivity(intent);
    }
}
