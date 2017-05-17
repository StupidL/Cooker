package me.stupidme.cooker.util;

import java.util.Random;

import me.stupidme.cooker.R;

/**
 * Created by StupidL on 2017/5/16.
 */

public class ImageUtil {

    private static final int[] images = {
            R.drawable.bg1, R.drawable.bg2, R.drawable.bg3,
            R.drawable.bg4, R.drawable.bg5, R.drawable.bg6,
            R.drawable.bg7, R.drawable.bg8, R.drawable.bg9
    };

    public static int nextImageResId() {
        int result = new Random().nextInt(10);
        return images[result % 9];
    }

    public static int placeHolder() {
        return images[3];
    }
}
