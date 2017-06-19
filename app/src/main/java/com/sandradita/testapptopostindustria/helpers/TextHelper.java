package com.sandradita.testapptopostindustria.helpers;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ClickableSpan;
import android.view.View;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by sandradita on 6/18/2017.
 */

public class TextHelper {

    private static final String REGEX_URL = "(https?:\\/\\/(?:www\\.|(?!www))[a-zA-Z0-9][a-zA-Z0-9-]+[a-zA-Z0-9]\\.[^\\s]{2,}|www\\.[a-zA-Z0-9][a-zA-Z0-9-]+[a-zA-Z0-9]\\.[^\\s]{2,}|https?:\\/\\/(?:www\\.|(?!www))[a-zA-Z0-9]\\.[^\\s]{2,}|www\\.[a-zA-Z0-9]\\.[^\\s]{2,})";

    private TextHelper() {
    }

    /**
     * Looks for URL links and makes it clickable. If there is no links, returns string as it is.
     *
     * @param text            source text.
     * @param onClickListener actions that performs when user clicks on the word.
     */
    @NonNull
    public static SpannableString makeLinksClickable(SpannableString text, OnClickSpanListener onClickListener) {
        SpannableString spannableString = new SpannableString(text);

        Pattern pattern = Pattern.compile(REGEX_URL);
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            spannableString.setSpan(createClickableSpan(onClickListener, matcher.group()),
                    matcher.start(), matcher.end(),
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        return spannableString;
    }

    /**
     * Looks for URL links and makes it clickable. If there is no links, returns string as it is.
     *
     * @param text            source text.
     * @param onClickListener actions that performs when user clicks on the word.
     */
    @NonNull
    public static SpannableString makeLinksClickable(String text, OnClickSpanListener onClickListener) {
        return makeLinksClickable(new SpannableString(text), onClickListener);
    }

    /**
     * Looks for words which starts with selected prefixes and makes it clickable. If there is no
     * words with prefix, returns string as it is.
     *
     * @param text              source text.
     * @param onClickListener   actions that performs when user clicks on the word.
     * @param clickablePrefixes strings, which can be first letter of the word, that should be clickable.
     */
    @NonNull
    public static SpannableString makeWordsClickable(SpannableString text, OnClickSpanListener onClickListener,
                                                     String... clickablePrefixes) {
        SpannableString spannableString = new SpannableString(text);

        String regex = getPrefixesRegex(clickablePrefixes);
        if (regex != null) {
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(text);
            while (matcher.find()) {
                spannableString.setSpan(createClickableSpan(onClickListener, matcher.group()),
                        matcher.start(), matcher.end(),
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }

        return spannableString;
    }

    /**
     * Looks for words which starts with selected prefixes and makes it clickable. If there is no
     * words with prefix, returns string as it is.
     *
     * @param text              source text.
     * @param onClickListener   actions that performs when user clicks on the word.
     * @param clickablePrefixes strings, which can be first letter of the word, that should be clickable.
     */
    @NonNull
    public static SpannableString makeWordsClickable(String text, OnClickSpanListener onClickListener,
                                                     String... clickablePrefixes) {
        return makeWordsClickable(new SpannableString(text), onClickListener, clickablePrefixes);
    }

    /**
     * @return the regex with selected prefixes to look for words that should start with any of
     * these prefixes.
     */
    @Nullable
    private static String getPrefixesRegex(String... prefixes) {
        if (prefixes != null && prefixes.length > 0) {
            StringBuilder stringBuilder = new StringBuilder("(");
            for (int i = 0; i < prefixes.length; i++) {
                stringBuilder.append('(');
                stringBuilder.append(prefixes[i]);
                stringBuilder.append(')');
                if (i < prefixes.length - 1) {
                    stringBuilder.append('|');
                }
            }
            stringBuilder.append(")\\w+");
            return stringBuilder.toString();
        }
        return null;
    }

    private static ClickableSpan createClickableSpan(final OnClickSpanListener onClickListener,
                                                     final String word) {
        return new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                if (onClickListener != null) onClickListener.onClickSpan(word);
            }
        };
    }

    /**
     * Actions that performs when user clicks on the word.
     */
    public interface OnClickSpanListener {
        void onClickSpan(String word);
    }

}
