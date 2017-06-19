package com.sandradita.testapptopostindustria.ui.adapters;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.sandradita.testapptopostindustria.R;
import com.sandradita.testapptopostindustria.helpers.TextHelper;
import com.sandradita.testapptopostindustria.helpers.TwitterDateHelper;
import com.sandradita.testapptopostindustria.managers.TwitterDataManager;
import com.sandradita.testapptopostindustria.model.Tweet;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by sandradita on 6/16/2017.
 */

public class TweetViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    @BindView(R.id.iv_tweet_avatar)
    ImageView ivAvatar;
    @BindView(R.id.tv_tweet_author)
    TextView tvAuthor;
    @BindView(R.id.tv_tweet_date)
    TextView tvDate;
    @BindView(R.id.tv_tweet_text)
    TextView tvText;
    @BindView(R.id.ib_tweet_like)
    ImageButton ibLike;

    private View mView;
    private Tweet mItem;
    private OnChangeFavouriteStatus mOnChangeFavouriteStatus;
    private TextHelper.OnClickSpanListener mOnClickSpanListener;

    public static View inflateView(ViewGroup parent) {
        return LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tweet, parent, false);
    }

    public TweetViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        mView = itemView;
        ibLike.setOnClickListener(this);
        tvText.setMovementMethod(LinkMovementMethod.getInstance());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_tweet_like:
                changeFavouriteStatus();
                break;
        }
    }

    /**
     * Shows item data in view.
     *
     * @param item current item
     */
    public void setItem(@Nullable Tweet item) {
        this.mItem = item;
        String imageUrl = null;
        String author = null;
        String date = null;
        SpannableString text = null;
        boolean isFavourite = false;
        if (item != null) {
            imageUrl = item.getAuthorImageUrl();
            author = item.getAuthorName();
            date = TwitterDateHelper.parseLongDateToShort(item.getCreatedAt());
            text = TextHelper.makeWordsClickable(item.getText(), mOnClickSpanListener, "@", "#");
            text = TextHelper.makeLinksClickable(text, mOnClickLinkSpanListener);
            isFavourite = item.isFavourite();
        }
        tvAuthor.setText(author);
        tvDate.setText(date);
        tvText.setText(text);
        Picasso.with(ivAvatar.getContext()).load(imageUrl).placeholder(R.mipmap.ic_launcher).into(ivAvatar);
        applyStyleToImageButton(isFavourite);
    }

    public void setOnChangeFavouriteStatus(OnChangeFavouriteStatus onChangeFavouriteStatus) {
        this.mOnChangeFavouriteStatus = onChangeFavouriteStatus;
    }

    public void setOnClickSpanListener(TextHelper.OnClickSpanListener onClickSpanListener) {
        this.mOnClickSpanListener = onClickSpanListener;
    }

    /**
     * Changes item view when user makes tweet favourite or unfavourite
     */
    private void changeFavouriteStatus() {
        if (mItem == null) return;

        boolean isFavourite = !mItem.isFavourite();
        mItem.setFavourite(isFavourite);
        applyStyleToImageButton(isFavourite);
        TwitterDataManager.getInstance().setTweetFavourite(mItem, isFavourite);

        int message = isFavourite ? R.string.message_tweet_favourite : R.string.message_tweet_unfavourite;
        Snackbar.make(tvText, message, Snackbar.LENGTH_SHORT).show();

        if (mOnChangeFavouriteStatus != null) {
            mOnChangeFavouriteStatus.onChangeStatus(mItem, isFavourite);
        }
    }

    /**
     * Configures like button colors by selected user favourite status
     */
    private void applyStyleToImageButton(boolean isFavourite) {
        @ColorRes int btnColor = R.color.colorIconButton;
        @DrawableRes int backgroundRes = R.drawable.btn_ripple_background;
        if (isFavourite) {
            btnColor = R.color.colorAccent;
            backgroundRes = R.drawable.btn_ripple_background_accent;
        }
        ibLike.setColorFilter(ibLike.getContext().getResources().getColor(btnColor));
        ibLike.setBackgroundResource(backgroundRes);
    }

    /**
     * Is called when user clicks on URL link. Starts browser with selected URL.
     */
    private final TextHelper.OnClickSpanListener mOnClickLinkSpanListener = new TextHelper.OnClickSpanListener() {
        @Override
        public void onClickSpan(String word) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(word));
            mView.getContext().startActivity(intent);
        }
    };

    public interface OnChangeFavouriteStatus {
        void onChangeStatus(Tweet tweet, boolean isFavourite);
    }

}
