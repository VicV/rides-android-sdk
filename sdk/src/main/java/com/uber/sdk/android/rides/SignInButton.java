package com.uber.sdk.android.rides;

import android.content.Context;
import android.content.res.TypedArray;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.api.client.auth.oauth2.Credential;
import com.uber.sdk.android.rides.utils.ManifestUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.POST;
import retrofit2.http.Query;

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
            @NonNull final Context context,
            @Nullable AttributeSet attributeSet,
            int defStyleAttrs,
            int defStyleRes) {
        Style style = Style.DEFAULT;
        if (attributeSet != null) {
            TypedArray typedArray = context.getTheme().obtainStyledAttributes(attributeSet,
                    R.styleable.RequestButton, 0, 0);
            mRedirectURI = ManifestUtils.getManifestData(context, "UberRedirectURI");
            mClientId = ManifestUtils.getManifestData(context, "UberClientId");
            mClientSecret = ManifestUtils.getManifestData(context, "UberClientSecret");

            style = Style.fromInt(typedArray.getInt(R.styleable.SignInButton_signin_style,
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

                showLoginWebview(context);

            }
        });
    }

    private AlertDialog loginDialog = null;

    private void showLoginWebview(final Context c) {
        final AlertDialog.Builder alert = new AlertDialog.Builder(c, R.style.SignIn_Dialog_Theme);
        WebView wv = new WebView(c);
        wv.getSettings().setJavaScriptEnabled(true);
        wv.loadUrl(getAuthURL(c));
        wv.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.startsWith(mRedirectURI)) {
                    Uri uri = Uri.parse(url);
                    new Retrofit.Builder().baseUrl("https://login.uber.com/").addConverterFactory(GsonConverterFactory.create())
                            .build().create(UberAuthInterface.class).getToken(mClientSecret, mClientId,
                            "authorization_code", uri.getQueryParameter("code"), mRedirectURI).enqueue(
                            new Callback<AuthInfo>() {
                                @Override
                                public void onResponse(Response<AuthInfo> response) {
                                    loginDialog.hide();
                                    //TODO: YOU NOW HAVE TOKENS AND THINGS. SEND TO THE JAVA SDK AND HAVE FUN.
                                    Credential credential =
                                }

                                @Override
                                public void onFailure(Throwable t) {


                                }
                            });
                    return true;
                }
                return false;
            }
        });

        LinearLayout wrapper = new LinearLayout(c);
        EditText keyboardHelper = new EditText(c);
        keyboardHelper.setVisibility(View.GONE);
        wrapper.setOrientation(LinearLayout.VERTICAL);
        wrapper.addView(wv, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        wrapper.addView(keyboardHelper, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        alert.setView(wrapper);
        loginDialog = alert.show();
    }

    private interface UberAuthInterface {

        @POST("/oauth/token")
        Call<AuthInfo> getToken(@Query("client_secret") String clientSecret,
                                @Query("client_id") String clientId,
                                @Query("grant_type") String grantType,
                                @Query("code") String code,
                                @Query("redirect_uri") String redirectUrl);
    }

    private String getAuthURL(Context c) {
        Uri.Builder uriBuilder = Uri.parse("https://login.uber.com/oauth/authorize").buildUpon();
        uriBuilder.appendQueryParameter("response_type", "code");
        uriBuilder.appendQueryParameter("client_id", mClientId);
        uriBuilder.appendQueryParameter("scope", "history_lite history profile");
        uriBuilder.appendQueryParameter("redirect_uri", mRedirectURI);
        return uriBuilder.build().toString().replace("%20", "+");
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
