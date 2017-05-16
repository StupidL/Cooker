package me.stupidme.cooker.view.about;

import android.content.Context;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import me.stupidme.cooker.R;

/**
 * Created by StupidL on 2017/5/16.
 */

public class ContentHelper {

    private WeakReference<Context> mContextRef;

    ContentHelper(Context context) {
        mContextRef = new WeakReference<>(context);
    }

    public List<BaseAboutBean> contents() {
        List<BaseAboutBean> list = new ArrayList<>();
        Context context = mContextRef.get();
        if (context == null)
            throw new RuntimeException("Can not get resource, because context is null.");

        String groupTitleUsage = context.getResources().getString(R.string.about_group_title_usage);
        list.add(new AboutGroupItem(groupTitleUsage));

        String cardTitleCreateCooker = context.getResources().getString(R.string.about_card_title_create_cooker);
        String cardContentCreateCooker = context.getResources().getString(R.string.about_card_content_create_cooker);
        AboutCardItem createCookerCard = new AboutCardItem(new CardBean(cardTitleCreateCooker, cardContentCreateCooker));
        list.add(createCookerCard);

        String cardTitleCreateBook = context.getResources().getString(R.string.about_card_title_create_book);
        String cardContentCreateBook = context.getResources().getString(R.string.about_card_content_create_book);
        AboutCardItem createBookCard = new AboutCardItem(new CardBean(cardTitleCreateBook, cardContentCreateBook));
        list.add(createBookCard);

        String groupTitleThanks = context.getResources().getString(R.string.about_group_title_thanks);
        list.add(new AboutGroupItem(groupTitleThanks));

        String cardTitleRxJava = context.getResources().getString(R.string.about_card_title_reactive_java);
        String cardContentRxJava = context.getResources().getString(R.string.about_card_content_reactive_java);
        AboutCardItem rxJavaCard = new AboutCardItem(new CardBean(cardTitleRxJava, cardContentRxJava));
        list.add(rxJavaCard);

        String cardTitleRetrofit = context.getResources().getString(R.string.about_card_title_retrofit);
        String cardContentRetrofit = context.getResources().getString(R.string.about_card_content_retrofit);
        AboutCardItem retrofitCard = new AboutCardItem(new CardBean(cardTitleRetrofit, cardContentRetrofit));
        list.add(retrofitCard);

        String cardTitleGlide = context.getResources().getString(R.string.about_card_title_glide);
        String cardContentGlide = context.getResources().getString(R.string.about_card_content_glide);
        AboutCardItem glideCard = new AboutCardItem(new CardBean(cardTitleGlide, cardContentGlide));
        list.add(glideCard);

        String cardTitleRetroLambda = context.getResources().getString(R.string.about_card_title_retrolambbda);
        String cardContentRetroLambda = context.getResources().getString(R.string.about_card_content_retrolambda);
        AboutCardItem retroLambdaCard = new AboutCardItem(new CardBean(cardTitleRetroLambda, cardContentRetroLambda));
        list.add(retroLambdaCard);

        return list;
    }
}
