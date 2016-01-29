package com.uber.sdk.android.rides;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.uber.sdk.android.rides.utils.ManifestUtils;

/**
 * Created by V on 1/29/2016.
 */
public class SignInButton extends UberButton {

    private String mRedirectURI;
    private String mClientId;
    private String mClientSecret;

    public SignInButton(Context context) {
        this(context, null);
    }

    public SignInButton(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.uberButtonStyle);
    }

    public SignInButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void init(
            @NonNull Context context,
            @Nullable AttributeSet attributeSet,
            int defStyleAttrs,
            int defStyleRes) {
        Style style = Style.DEFAULT;
        if (attributeSet != null) {
            TypedArray typedArray = context.getTheme().obtainStyledAttributes(attributeSet,
                    R.styleable.RequestButton, 0, 0);
            mRedirectURI = typedArray.getString(R.styleable.SignInButton_redirect_uri);
            mClientId = ManifestUtils.getManifestData(context, "UberClientId");
            mClientId = ManifestUtils.getManifestData(context, "UberClientSecret");

            style = Style.fromInt(typedArray.getInt(R.styleable.RequestButton_style,
                    Style.DEFAULT.getValue()));
            typedArray.recycle();
        }
        // If no style specified, or just the default UberButton style, use the style attribute
        defStyleRes = defStyleRes == 0 || defStyleRes == R.style.UberButton ? style.getStyleId() : defStyleRes;

        super.init(context, attributeSet, defStyleAttrs, defStyleRes);

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mRedirectURI == null) {
                    throw new IllegalStateException("Redirect URI required to use SignInButton.");
                }
                if (mClientId == null) {
                    throw new IllegalStateException("Client Id required to use SignInButton.");
                }
                if (mClientSecret == null) {
                    throw new IllegalStateException("Client Secret required to use SignInButton.");
                }

                //TODO: LOGIN_LOGIC
            }
        });
    }


    /**
     * Encapsulates the valid values for the uber:color_scheme attribute for a {@link RequestButton}
     */
    private enum Style {
        /**
         * Black background, white text. This is the default.
         */
        BLACK(0, R.style.UberButton_SignIn),

        /**
         * White background, black text.
         */
        WHITE(1, R.style.UberButton_SignIn_White),

        /**
         * Black background, white text, Uber badge.
         */
        BLACK_BADGE(2, R.style.UberButton_SignIn_Badge),

        /**
         * White background, black text, Uber badge.
         */
        WHITE_BADGE(3, R.style.UberButton_SignIn_Badge_White);

        private static Style DEFAULT = BLACK;

        private int mIntValue;
        private int mStyleId;

        Style(int value, int styleId) {
            this.mIntValue = value;
            this.mStyleId = styleId;
        }

        /**
         * If the value is not found returns default Style.
         */
        @NonNull
        static Style fromInt(int enumValue) {
            for (Style style : values()) {
                if (style.getValue() == enumValue) {
                    return style;
                }
            }
            return DEFAULT;
        }

        private int getValue() {
            return mIntValue;
        }

        private int getStyleId() {
            return mStyleId;
        }
    }
}
