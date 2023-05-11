package com.noobpy.tvar.player;

import static com.unity3d.services.core.device.Device.getScreenBrightness;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Lifecycle;

import android.annotation.SuppressLint;
import android.app.PictureInPictureParams;
import android.app.UiModeManager;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.ContentObserver;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.text.TextUtils;
import android.transition.Fade;
import android.util.Log;
import android.util.Rational;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.CheckedTextView;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.github.ybq.android.spinkit.style.ThreeBounce;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.PlaybackException;
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector;
import com.google.android.exoplayer2.trackselection.TrackSelectionParameters;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.drm.DrmSessionManager;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.source.dash.DashChunkSource;
import com.google.android.exoplayer2.source.dash.DashMediaSource;
import com.google.android.exoplayer2.source.dash.DefaultDashChunkSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.upstream.DefaultDataSource;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource;
import com.google.android.exoplayer2.upstream.HttpDataSource;
import com.google.android.exoplayer2.util.Util;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.ironsource.mediationsdk.ISBannerSize;
import com.ironsource.mediationsdk.IronSource;
import com.ironsource.mediationsdk.IronSourceBannerLayout;
import com.ironsource.mediationsdk.logger.IronSourceError;
import com.ironsource.mediationsdk.sdk.BannerListener;
import com.noobpy.tvar.BuildConfig;
import com.noobpy.tvar.MainActivity;
import com.noobpy.tvar.R;
import com.noobpy.tvar.util.Canales;
import com.noobpy.tvar.util.CanalesFav;
import com.noobpy.tvar.util.Global;
import com.pixplicity.easyprefs.library.Prefs;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class PlayerActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "PlayerActivity";
    String videoUrl;
    String name, img, license, key, origin, referer, userAgent, whichList;
    private PlayerView playerView;
    public static ExoPlayer exoPlayer;
    private DefaultDataSource.Factory dataSourceFactory;
    private HttpDataSource.Factory httpDataSourceFactory;
    private DashChunkSource.Factory dashChunkSourceFactory;
    private ProgressBar progressBar;
    boolean fullscreen = false;
    private ImageView fullscreenButton;
    ImageView imgCanal;
    TextView txtCanal;
    ImageButton btnBack;
    private DrmSessionManager drmSessionManager;
    ImageView imgQuality;

    private boolean isShowingTrackSelectionDialog;
    DefaultTrackSelector trackSelector;

    UiModeManager uiModeManager;

    LinearLayout linearPlayer;

    int clickRotation = 1;
    final String AMAZON_FEATURE_FIRE_TV = "amazon.hardware.fire_tv";

    ImageView aspectRatio;
    int clickcount = 0;

    ImageButton exoPlay, exoPause;
    ImageView pip;
    boolean subtitle, animations;

    int orientation, audio, selectedSub;
    float quality;

    PictureInPictureParams.Builder setPictureInPictureParams;
    boolean pipSetting;

    int defaultAnim;
    int ads;
    private InterstitialAd mInterstitialAd;
    private RewardedAd mRewardedAd;

    IronSourceBannerLayout bannerPlayer;

    boolean orientation_sp, zapping;
    int quality_res, id;

    ImageView previousButton, nextButton;
    private List<Canales> channels;
    private List<CanalesFav> channelsFav;

    SeekBar seekbarVolume, seekBarBrightness;
    LinearLayout relative_brightness_volume;


    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

        setContentView(R.layout.activity_player);
        uiModeManager = (UiModeManager) getSystemService(UI_MODE_SERVICE);

        //  getPrefs();

        aspectRatio = findViewById(R.id.exo_aspect_ratio);

        setOrientation();
        getSupportActionBar().hide();
        //actionBar.hide();

        videoUrl = getIntent().getStringExtra("link");
        name = getIntent().getStringExtra("name");
        origin = getIntent().getStringExtra("origin");
        referer = getIntent().getStringExtra("referer");
        img = getIntent().getStringExtra("img");
        userAgent = getIntent().getStringExtra("ua");
        license = getIntent().getStringExtra("license");
        whichList = getIntent().getStringExtra("whichList");

        channels = getIntent().getParcelableArrayListExtra("channels");
        channelsFav = getIntent().getParcelableArrayListExtra("channelsFav");


        Fade fade = new Fade();
        fade.excludeTarget(R.id.parent_view, true);
        fade.excludeTarget(android.R.id.statusBarBackground, true);
        fade.excludeTarget(android.R.id.navigationBarBackground, true);
        getWindow().setEnterTransition(fade);
        getWindow().setExitTransition(fade);

        progressBar = findViewById(R.id.progressBar);
        ThreeBounce foldingCube = new ThreeBounce();
        foldingCube.setColor(Color.parseColor("#ffffff"));
        progressBar.setIndeterminateDrawable(foldingCube);

        loadPrefs();
        loadAds();
        //trustAll();

        HttpDataSource.Factory httpDataSourceFactory = new DefaultHttpDataSource.Factory()
                .setAllowCrossProtocolRedirects(true)
                .setUserAgent(userAgent);

        Map<String, String> refererMapNull = new HashMap<>();
        refererMapNull.put("Referer", "null ");

        Map<String, String> refererMap = new HashMap<>();
        refererMap.put("Referer", referer);

        Map<String, String> originMapNull = new HashMap<>();
        originMapNull.put("Origin", "null ");

        Map<String, String> originMap = new HashMap<>();
        originMap.put("Origin", origin);

        if (videoUrl.contains("mpd")) {
            if (referer.isEmpty()) {
                httpDataSourceFactory.setDefaultRequestProperties(refererMapNull);
            } else {
                httpDataSourceFactory.setDefaultRequestProperties(refererMap);
            }


            if (origin.isEmpty()) {
                httpDataSourceFactory.setDefaultRequestProperties(originMapNull);
            } else {
                httpDataSourceFactory.setDefaultRequestProperties(originMap);
            }
        }


        dashChunkSourceFactory = new DefaultDashChunkSource.Factory(httpDataSourceFactory);

        //dataSourceFactory = new DefaultDataSourceFactory(getApplicationContext(), getUserAgent());
        dataSourceFactory = new DefaultDataSource.Factory(getApplicationContext(),
                httpDataSourceFactory);

        LoadControl loadControl = new DefaultLoadControl();

        AdaptiveTrackSelection.Factory trackSelectionFactory = new AdaptiveTrackSelection.Factory();
        trackSelector = new DefaultTrackSelector(this, trackSelectionFactory);


        trackSelector.setParameters(
                trackSelector.buildUponParameters()
                        .setMaxVideoSize(1920, quality_res).build());

        exoPlayer = new ExoPlayer.Builder(this)
                .setTrackSelector(trackSelector)
                .setLoadControl(loadControl)
                .setSeekForwardIncrementMs(10000)
                .setSeekBackIncrementMs(10000)
                .build();

        MediaSessionCompat mediaSessionCompat = new MediaSessionCompat(PlayerActivity.this, TAG);

        mediaSessionCompat.setActive(true);

        MediaSessionConnector connector = new MediaSessionConnector(mediaSessionCompat);

        connector.setEnabledPlaybackActions(
                PlaybackStateCompat.ACTION_PLAY_PAUSE
                        | PlaybackStateCompat.ACTION_PLAY
                        | PlaybackStateCompat.ACTION_PAUSE
                        | PlaybackStateCompat.ACTION_STOP
        );

        connector.setPlayer(exoPlayer);

        // exoPlayer = ExoPlayerFactory.newSimpleInstance(/* context= */ FullscreenActivity.this, renderersFactory, trackSelector, defaultLoadControl);

        playerView = findViewById(R.id.exoPlayerView);
        playerView.setPlayer(exoPlayer);
        playerView.setUseController(true);
        playerView.requestFocus();


        playerView.addOnLayoutChangeListener((v, left, top, right, bottom,
                                              oldLeft, oldTop, oldRight, oldBottom) -> {

            if (left != oldLeft || right != oldRight || top != oldTop
                    || bottom != oldBottom) {
                // The playerView’s bounds changed, update the source hint rect to
                // reflect its new bounds.
                final Rect sourceRectHint = new Rect();
                playerView.getGlobalVisibleRect(sourceRectHint);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    setPictureInPictureParams = new PictureInPictureParams.Builder();
                    setPictureInPictureParams.setSourceRectHint(sourceRectHint);
                    setPictureInPictureParams.build();

                }

            }
        });


        // trackSelector = new DefaultTrackSelector(this);


        imgCanal = findViewById(R.id.imgCanal);
        txtCanal = findViewById(R.id.txtCanal);
        btnBack = findViewById(R.id.btnBack);
        imgQuality = findViewById(R.id.exo_quality);
        fullscreenButton = playerView.findViewById(R.id.exo_fullscreen_icon);
        linearPlayer = findViewById(R.id.linear_player);
        exoPlay = findViewById(R.id.exo_play);
        exoPause = findViewById(R.id.exo_pause);
        pip = findViewById(R.id.exo_pip);

        previousButton = playerView.findViewById(R.id.exo_anterior);
        nextButton = playerView.findViewById(R.id.exo_siguiente);

        seekbarVolume = findViewById(R.id.seek_volume);
        seekBarBrightness = findViewById(R.id.seek_brightness);
        relative_brightness_volume = findViewById(R.id.relative_brightness_volume);

        btnBack.setOnClickListener(this);
        imgQuality.setOnClickListener(this);
        fullscreenButton.setOnClickListener(this);
        pip.setOnClickListener(this);
        aspectRatio.setOnClickListener(this);

        previousButton.setOnClickListener(view -> playPreviousChannel());
        nextButton.setOnClickListener(view -> playNextChannel());

        if (uiModeManager.getCurrentModeType() == Configuration.UI_MODE_TYPE_TELEVISION || getPackageManager().hasSystemFeature(AMAZON_FEATURE_FIRE_TV)) {
            fullscreenButton.setVisibility(View.GONE);
            pip.setVisibility(View.GONE);
            relative_brightness_volume.setVisibility(View.GONE);
        } else {
            fullscreenButton.setVisibility(View.VISIBLE);
            //pip.setVisibility(View.VISIBLE);
        }


        txtCanal.setText(getIntent().getStringExtra("name"));

        Glide.with(this)
                .load(img)
                .thumbnail(0.3f)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.ic_baseline_image_24)
                .fitCenter()
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        supportStartPostponedEnterTransition();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        supportStartPostponedEnterTransition();
                        return false;
                    }
                })
                .into(imgCanal);

        MediaSource mediaSource = buildMediaSource(Uri.parse(videoUrl), null);
        initializePlayer(mediaSource);

        float initialVolume = 1f; // Volumen inicial (rango de 0 a 1)
        exoPlayer.setVolume(initialVolume);

        int initialProgress = (int) (initialVolume * 100);
        seekbarVolume.setProgress(initialProgress);

        seekbarVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // Cambiar el brillo del reproductor ExoPlayer
                float volume = (float) progress / 100;
                exoPlayer.setVolume(volume);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        int currentBrightnessMode;

        try {
            currentBrightnessMode = Settings.System.getInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE);
        } catch (Settings.SettingNotFoundException e) {
            currentBrightnessMode = Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL;
        }

// Configurar la vista SeekBar según el modo de brillo actual

        if (currentBrightnessMode == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC) {
            seekBarBrightness.setEnabled(false);
        } else {
            seekBarBrightness.setProgress(getScreenBrightness());
            seekBarBrightness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    float brightness = (float) progress / 100;
                    WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
                    layoutParams.screenBrightness = brightness;
                    getWindow().setAttributes(layoutParams);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                }
            });
        }

// Agregar un listener para detectar cambios en el modo de brillo
        getContentResolver().registerContentObserver(Settings.System.getUriFor(Settings.System.SCREEN_BRIGHTNESS_MODE), true, new ContentObserver(new Handler()) {
            @Override
            public void onChange(boolean selfChange) {
                super.onChange(selfChange);
                // Configurar la vista SeekBar según el nuevo modo de brillo
                int currentBrightnessMode;
                try {
                    currentBrightnessMode = Settings.System.getInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE);
                } catch (Settings.SettingNotFoundException e) {
                    currentBrightnessMode = Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL;
                }
                if (currentBrightnessMode == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC) {
                    seekBarBrightness.setEnabled(false);
                } else {
                    seekBarBrightness.setEnabled(true);
                    seekBarBrightness.setProgress(getScreenBrightness());
                }
            }
        });

    }


    /*private void trustAll() {
        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            public void checkClientTrusted(X509Certificate[] certs, String authType) {
                //
            }

            public void checkServerTrusted(X509Certificate[] certs, String authType) {
                //
            }
        }};

//Install the all-trusting trust manager
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (KeyManagementException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }*/

    /*@SuppressLint("TrustAllX509TrustManager")
    private void trustAll() {
        try {
            HttpsURLConnection.setDefaultHostnameVerifier(new NullHostNameVerifier());
            SSLContext mSslContext = SSLContext.getInstance("TLS");
            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[]{};
                }

                public void checkClientTrusted(X509Certificate[] chain, String authType) {
                }

                public void checkServerTrusted(X509Certificate[] chain, String authType) {
                }
            }};
            mSslContext.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(mSslContext.getSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class NullHostNameVerifier implements HostnameVerifier {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }*/


    private void initializePlayer(MediaSource mediaSource) {
        //Uri uri = Uri.parse(url);

        //MediaSource mediaSource = buildMediaSource(Uri.parse(videoUrl), null);


        exoPlayer.setMediaSource(mediaSource);
        exoPlayer.prepare();
        exoPlayer.setPlayWhenReady(true);

        exoPlayer.addListener(new Player.Listener() {
            @Override
            public void onPlaybackStateChanged(int playbackState) {
                Player.Listener.super.onPlaybackStateChanged(playbackState);
            }
        });

        exoPlayer.addListener(new Player.Listener() {

            @Override
            public void onTimelineChanged(@NonNull Timeline timeline, int reason) {

            }

            @Override
            public void onPlaybackStateChanged(int state) {

                if (state == PlaybackStateCompat.STATE_PLAYING) {
                    previousButton.setEnabled(true);
                    nextButton.setEnabled(true);
                    progressBar.setVisibility(View.GONE);

                    boolean supportPip = getPackageManager().hasSystemFeature(PackageManager.FEATURE_PICTURE_IN_PICTURE);

                    if (supportPip & uiModeManager.getCurrentModeType() != Configuration.UI_MODE_TYPE_TELEVISION) {
                        pip.setVisibility(View.VISIBLE);
                        relative_brightness_volume.setVisibility(View.VISIBLE);
                    }

                }
                if (state == PlaybackStateCompat.STATE_BUFFERING) {
                    previousButton.setEnabled(true);
                    nextButton.setEnabled(true);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        if (isInPictureInPictureMode()) {
                            playerView.hideController();
                            progressBar.setVisibility(View.GONE);
                        }
                    }

                    progressBar.setVisibility(View.VISIBLE);
                }
                if (state == PlaybackStateCompat.STATE_PAUSED) {
                    previousButton.setEnabled(true);
                    nextButton.setEnabled(true);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        if (isInPictureInPictureMode()) {
                            playerView.hideController();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                    progressBar.setVisibility(View.GONE);

                }
            }

            @Override
            public void onPlayWhenReadyChanged(boolean playWhenReady, int reason) {

            }

            @Override
            public void onIsPlayingChanged(boolean isPlaying) {
                if (!isPlaying) {
                    progressBar.setVisibility(View.GONE);

                }
            }

            @Override
            public void onRepeatModeChanged(int repeatMode) {

            }

            @Override
            public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

            }


            @Override
            public void onPlayerError(@NonNull PlaybackException error) {
                previousButton.setEnabled(true);
                nextButton.setEnabled(true);
                Player.Listener.super.onPlayerError(error);
                exoPlayer.stop();
                errorDialog(error.toString());
                Log.e("TAG", "ERROR IS " + error.getCause());


            }
        });

        /*CustomEventListener eventListener = new CustomEventListener(previousButton, nextButton, this);
        exoPlayer.addListener(eventListener);*/
    }


    private void loadAds() {
        if (uiModeManager.getCurrentModeType() != Configuration.UI_MODE_TYPE_TELEVISION) {

            FrameLayout bannerAbout = findViewById(R.id.bannerPlayer);
            bannerPlayer = IronSource.createBanner(this, ISBannerSize.BANNER);
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.WRAP_CONTENT);
            bannerAbout.addView(bannerPlayer, 0, layoutParams);
            bannerPlayer.setBannerListener(new BannerListener() {
                @Override
                public void onBannerAdLoaded() {
                    bannerAbout.setVisibility(View.VISIBLE);
                }

                @Override
                public void onBannerAdLoadFailed(IronSourceError ironSourceError) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            bannerAbout.removeAllViews();
                        }
                    });
                }

                @Override
                public void onBannerAdClicked() {

                }

                @Override
                public void onBannerAdScreenPresented() {

                }

                @Override
                public void onBannerAdScreenDismissed() {

                }

                @Override
                public void onBannerAdLeftApplication() {

                }
            });
            IronSource.loadBanner(bannerPlayer, "Banner_player");
        }
    }

    private void loadPrefs() {
        ads = Prefs.getInt("ads", 0);

        quality = Prefs.getFloat("quality", 720);
        orientation_sp = Prefs.getBoolean("orientation", true);
        zapping = Prefs.getBoolean("zapping");

        quality_res = Math.round(quality);

        if (uiModeManager.getCurrentModeType() != Configuration.UI_MODE_TYPE_TELEVISION) {
            if (orientation_sp) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                aspectRatio.setVisibility(View.GONE);
            } else {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                clickRotation = 0;
                aspectRatio.setVisibility(View.VISIBLE);
            }
        }
    }

    private void setOrientation() {
        if (orientation == 0) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER);
            aspectRatio.setVisibility(View.GONE);
        }
        if (orientation == 1) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            clickRotation = 0;
            aspectRatio.setVisibility(View.VISIBLE);
        }
        if (orientation == 2) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            aspectRatio.setVisibility(View.GONE);

        }
    }

   /* private void getPrefs() {
        quality = Prefs.getInt("default_quality", 0);
        orientation = Prefs.getInt("orientation", 0);
        pipSetting = Prefs.getBoolean("pip", true);
        audio = Prefs.getInt("audio", 1);
        subtitle = Prefs.getBoolean("subtitle", true);
        selectedSub = Prefs.getInt("selectedSub", 0);

        animations = Prefs.getBoolean("animations", true);

        if (animations){
            defaultAnim = R.style.SlidingDialogAnimation;
        }
        else {
            defaultAnim = R.style.NoAnimation;
        }


    }*/




    /*private void getOrientation() {
        int orientation = this.getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            aspectRatio.setVisibility(View.VISIBLE);
        }
        else {
            aspectRatio.setVisibility(View.GONE);
        }
    }*/

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            aspectRatio.setVisibility(View.VISIBLE);
        } else {
            aspectRatio.setVisibility(View.GONE);
        }
    }

    private void qualitySelector() {
        isShowingTrackSelectionDialog = true;
        if (exoPlayer.isPlaying()) {
            TrackSelectionDialog trackSelectionDialog =
                    TrackSelectionDialog.createForTrackSelector(
                            trackSelector,
                            dismissedDialog -> isShowingTrackSelectionDialog = false);
            trackSelectionDialog.show(getSupportFragmentManager(), null);
        }
    }


    //MediaItem mMediaItem;
    private MediaSource buildMediaSource(Uri uri, String overrideExtension) {

        //Toast.makeText(PlayerActivity.this, String.valueOf(uri), Toast.LENGTH_SHORT).show();
        //drmSessionManager = null;

        UUID drmSchemeUuid;
        if (license.contains("cc")) {
            drmSchemeUuid = Util.getDrmUuid(String.valueOf(C.CLEARKEY_UUID));
        } else {
            drmSchemeUuid = Util.getDrmUuid(String.valueOf(C.WIDEVINE_UUID));
        }

        MediaItem mMediaItem = MediaItem.fromUri(Uri.parse(String.valueOf(uri)));


        int type = TextUtils.isEmpty(overrideExtension) ? Util.inferContentType(uri) : Util.inferContentType("." + overrideExtension);
        switch (type) {
            case C.CONTENT_TYPE_DASH:
                MediaItem mediaItem;


                if (license.contains("cc")) {
                    Map<String, String> params = new HashMap<>();
                    params.put("Referer", BuildConfig.JSON_USERAGENT);

                    assert drmSchemeUuid != null;
                    mediaItem = new MediaItem.Builder()
                            .setUri(Uri.parse(String.valueOf(uri)))
                            // Protect content.
                            .setDrmConfiguration(
                                    new MediaItem.DrmConfiguration.Builder(C.CLEARKEY_UUID)
                                            .setLicenseUri(license)
                                            .setLicenseRequestHeaders(params)
                                            .setMultiSession(true)
                                            .setForceDefaultLicenseUri(true)
                                            .setScheme(C.CLEARKEY_UUID)
                                            .build())
                            // Clip the media.
                            .build();
                } else {
                    Map<String, String> params = new HashMap<>();
                    params.put("User-Agent", userAgent);

                    mediaItem = new MediaItem.Builder()
                            .setUri(Uri.parse(String.valueOf(uri)))
                            // Protect content.
                            .setDrmConfiguration(
                                    new MediaItem.DrmConfiguration.Builder(C.WIDEVINE_UUID)
                                            .setLicenseUri(license)
                                            .setLicenseRequestHeaders(params)
                                            .setMultiSession(true)
                                            .setForceDefaultLicenseUri(false)
                                            .setScheme(C.WIDEVINE_UUID)
                                            .build())
                            // Clip the media.
                            .build();
                }


                return new DashMediaSource.Factory(dashChunkSourceFactory, dataSourceFactory)
                        .createMediaSource(mediaItem);

            case C.CONTENT_TYPE_HLS:

                return new HlsMediaSource.Factory(dataSourceFactory)
                        .setAllowChunklessPreparation(true)
                        .createMediaSource(mMediaItem);
            case C.CONTENT_TYPE_OTHER:
                if (uri.toString().contains("m3u8")) {
                    return new HlsMediaSource.Factory(dataSourceFactory).createMediaSource(MediaItem.fromUri(uri));
                    //return new ProgressiveMediaSource.Factory(dataSourceFactory, new DefaultExtractorsFactory()).createMediaSource(mMediaItem);
                } else {
                    return new ProgressiveMediaSource.Factory(dataSourceFactory, new DefaultExtractorsFactory())
                            .createMediaSource(mMediaItem);
                }


            default: {
                throw new IllegalStateException("Unsupported type: " + type);
            }
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (!playerView.isControllerVisible()) {
            playerView.showController();
            if (exoPlayer.isPlaying()) {
                exoPlay.requestFocus();
            } else {
                exoPause.requestFocus();
            }
        }

        if (keyCode == KeyEvent.KEYCODE_MENU) {
            qualitySelector();
            return true;
        }

        if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
            if (zapping & uiModeManager.getCurrentModeType() == Configuration.UI_MODE_TYPE_TELEVISION) {
                playPreviousChannel();
            }
        }

        if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
            if (zapping & uiModeManager.getCurrentModeType() == Configuration.UI_MODE_TYPE_TELEVISION) {
                playNextChannel();
            }
        }


        return super.onKeyDown(keyCode, event);
    }


    private String getUserAgent() {
        StringBuilder result;
        result = new StringBuilder(64);
        result.append(userAgent);
        return result.toString();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        exoPlayer.stop();

        Intent returnBtn = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(returnBtn);

        IronSource.destroyBanner(bannerPlayer);

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            if (isInPictureInPictureMode()) {
                exoPlayer.setPlayWhenReady(true);
            } else {
                exoPlayer.setPlayWhenReady(false);
            }
            exoPlayer.getPlaybackState();

        } else {
            exoPlayer.setPlayWhenReady(false);
            exoPlayer.getPlaybackState();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        exoPlayer.setPlayWhenReady(true);
        exoPlayer.getPlaybackState();


    }


    public void errorDialog(String error) {

        /*AlertDialog dialogError = new AlertDialog.Builder(PlayerActivity.this, R.style.CustomAlertDialog).create();

        View customLayoutPermission = getLayoutInflater().inflate(R.layout.custom_dialog_permission, null);*/

        BottomSheetDialog customLayoutPermission = new BottomSheetDialog(PlayerActivity.this, R.style.TransparentDialog);
        customLayoutPermission.setContentView(R.layout.custom_dialog_permission);


        TextView txtTitle = customLayoutPermission.findViewById(R.id.txtTitle);
        txtTitle.setText("Error");
        TextView txtMessage = customLayoutPermission.findViewById(R.id.txtMessage);
        txtMessage.setText("No se pudo reproducir este canal");
        TextView txt_previous = customLayoutPermission.findViewById(R.id.previous_channel);
        TextView txt_next = customLayoutPermission.findViewById(R.id.next_channel);

        progressBar.setVisibility(View.GONE);

        txt_previous.setOnClickListener(view -> {
            playPreviousChannel();
        });

        txt_next.setOnClickListener(view -> {
            playNextChannel();
        });

        TextView btnSettings = customLayoutPermission.findViewById(R.id.settings);
        btnSettings.setText("Reintentar");
        btnSettings.setOnClickListener(view1 -> {
            retryLoad(videoUrl);
            customLayoutPermission.dismiss();
            progressBar.setVisibility(View.VISIBLE);

        });

        TextView btnCancel = customLayoutPermission.findViewById(R.id.cancel);
        btnCancel.setText("Volver");
        btnCancel.setOnClickListener(view12 -> {
            //finish();
            /*IronSource.destroyBanner(bannerPlayer);

            Intent returnBtn = new Intent(getApplicationContext(),
                    MainActivity.class);

            startActivity(returnBtn);*/
            customLayoutPermission.dismiss();
        });

        if (!this.isFinishing()) {
            customLayoutPermission.show();
        }

    }

    public void retryLoad(String videoUrl) {
        //Uri uri = Uri.parse(this.videoUrl);
        MediaSource mediaSource = buildMediaSource(Uri.parse(videoUrl), null);
        exoPlayer.setMediaSource(mediaSource);
        exoPlayer.prepare();
        exoPlayer.setPlayWhenReady(true);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId() /*to get clicked view id**/) {
            case R.id.exo_aspect_ratio:

                clickcount = clickcount + 1;
                switch (clickcount) {
                    case 1:
                        playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FILL);
                        break;
                    case 2:
                        playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_ZOOM);
                        break;
                    case 3:
                        playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIT);
                        clickcount = 0;
                        break;
                    default:
                        playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIT);
                        break;

                }
                break;
            case R.id.exo_fullscreen_icon:
                clickRotation = clickRotation + 1;
                // Toast.makeText(PlayerActivity.this, String.valueOf(clickRotation) , Toast.LENGTH_LONG).show();

                switch (clickRotation) {
                    case 1:
                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                        playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIT);
                        fullscreenButton.setImageDrawable(ContextCompat.getDrawable(PlayerActivity.this, R.drawable.ic_fullscreen_open));
                        break;
                    case 2:
                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                        clickRotation = 0;
                        fullscreenButton.setImageDrawable(ContextCompat.getDrawable(PlayerActivity.this, R.drawable.ic_fullscreen_close));

                        break;
                    default:
                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                        fullscreenButton.setImageDrawable(ContextCompat.getDrawable(PlayerActivity.this, R.drawable.ic_fullscreen_close));

                        break;

                }
                break;


            case R.id.btnBack:
                exoPlayer.stop();
                //finish();
                Intent returnBtn = new Intent(getApplicationContext(),
                        MainActivity.class);

                startActivity(returnBtn);

                IronSource.destroyBanner(bannerPlayer);

                break;

            case R.id.exo_quality:
                qualitySelector();
                break;

            case R.id.exo_pip:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

                    Rational aspectRatio = new Rational(Objects.requireNonNull(exoPlayer.getVideoFormat()).width, exoPlayer.getVideoFormat().height);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        setPictureInPictureParams.setAspectRatio(aspectRatio);

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                            setPictureInPictureParams.setAutoEnterEnabled(true);

                        }
                        //setPictureInPictureParams.build();
                        PlayerActivity.this.enterPictureInPictureMode(setPictureInPictureParams.build());
                    } else {
                        PlayerActivity.this.enterPictureInPictureMode();
                    }


                    playerView.hideController();
                }
                break;
            default:
                break;
        }

    }


    private void playNextChannel() {
        int currentIndex;
        if (whichList.equals("listview")){
            currentIndex = getChannelIndexByVideoUrl(videoUrl);
            // Sumar uno al índice del canal actual
            currentIndex++;
            // Verificar que el índice del canal actual esté dentro de los límites del canal
            if (currentIndex >= channels.size()) {
                currentIndex = 0;
            }
            // Obtener el canal actual
            Canales currentChannel = channels.get(currentIndex);
            // Verificar si la URL del canal actual es una URL de streaming
            while (!isStreamingUrl(currentChannel.getUrl())) {
                // Sumar uno al índice del canal actual
                currentIndex++;
                if (currentIndex >= channels.size()) {
                    currentIndex = 0;
                }
                currentChannel = channels.get(currentIndex);
            }
            // Actualizar la URL del canal
            videoUrl = currentChannel.getUrl();
            // Reproducir el canal actual
            setNewChannel(currentChannel);
            Prefs.putInt("listPosition", currentIndex);
        }
        else{
            currentIndex = getChannelIndexByVideoUrlRV(videoUrl);
            // Sumar uno al índice del canal actual
            currentIndex++;
            // Verificar que el índice del canal actual esté dentro de los límites del canal
            if (currentIndex >= channelsFav.size()) {
                currentIndex = 0;
            }
            // Obtener el canal actual
            CanalesFav currentChannel = channelsFav.get(currentIndex);
            // Verificar si la URL del canal actual es una URL de streaming
            while (!isStreamingUrl(currentChannel.getUrl())) {
                // Sumar uno al índice del canal actual
                currentIndex++;
                if (currentIndex >= channelsFav.size()) {
                    currentIndex = 0;
                }
                currentChannel = channelsFav.get(currentIndex);
            }
            // Actualizar la URL del canal
            videoUrl = currentChannel.getUrl();
            // Reproducir el canal actual
            setNewChannelRV(currentChannel);
            Prefs.putInt("listPosition", currentIndex);
        }


    }



    private void playPreviousChannel() {
        int currentIndex;
        if (whichList.equals("listview")){
            currentIndex = getChannelIndexByVideoUrl(videoUrl);
            // Decrementar el índice del canal actual
            currentIndex--;
            // Verificar que el índice del canal actual esté dentro de los límites del canal
            if (currentIndex < 0) {
                currentIndex = channels.size() - 1;
            }
            // Obtener el canal actual
            Canales currentChannel = channels.get(currentIndex);
            // Verificar si la URL del canal actual es una URL de streaming
            for (int i = currentIndex; i >= 0; i--) {
                Canales currentChannels = channels.get(i);
                if (isStreamingUrl(currentChannels.getUrl())) {
                    currentIndex = i;
                    break;
                }
            }
            // Actualizar la URL del canal
            videoUrl = currentChannel.getUrl();
            // Reproducir el canal actual
            //setNewChannel(currentChannel);
            if (exoPlayer.getPlaybackState() == Player.STATE_IDLE || exoPlayer.getPlaybackState() == Player.STATE_ENDED || exoPlayer.getPlaybackState() == Player.STATE_READY) {
                setNewChannel(currentChannel);
            } else {
                exoPlayer.seekTo(0);
                exoPlayer.setPlayWhenReady(false);
                setNewChannel(currentChannel);
            }
            Prefs.putInt("listPosition", currentIndex);
        }
        else{
            currentIndex = getChannelIndexByVideoUrlRV(videoUrl);
            // Decrementar el índice del canal actual
            currentIndex--;
            // Verificar que el índice del canal actual esté dentro de los límites del canal
            if (currentIndex < 0) {
                currentIndex = channelsFav.size() - 1;
            }
            // Obtener el canal actual
            CanalesFav currentChannel = channelsFav.get(currentIndex);
            // Verificar si la URL del canal actual es una URL de streaming
            while (!isStreamingUrl(currentChannel.getUrl())) {
                currentIndex--;
                if (currentIndex < 0) {
                    currentIndex = channelsFav.size() - 1;
                }
                currentChannel = channelsFav.get(currentIndex);
            }
            // Actualizar la URL del canal
            videoUrl = currentChannel.getUrl();
            // Reproducir el canal actual
            //setNewChannel(currentChannel);
            if (exoPlayer.getPlaybackState() == Player.STATE_IDLE || exoPlayer.getPlaybackState() == Player.STATE_ENDED || exoPlayer.getPlaybackState() == Player.STATE_READY) {
                setNewChannelRV(currentChannel);
            } else {
                exoPlayer.seekTo(0);
                exoPlayer.setPlayWhenReady(false);
                setNewChannelRV(currentChannel);
            }
            Prefs.putInt("listPosition", currentIndex);
        }

    }

    private void setNewChannel(Canales currentChannel) {

        videoUrl = currentChannel.getUrl();
        name = currentChannel.getCanal();
        origin = currentChannel.getOrigin();
        referer = currentChannel.getReferer();
        img = currentChannel.getImg();
        userAgent = currentChannel.getUa();
        license = currentChannel.getLic();
        txtCanal.setText(name);

        //Toast.makeText(this, this.videoUrl + " - " + origin+ " - " + referer+ " - " + img+ " - " + userAgent+ " - " + license, Toast.LENGTH_LONG).show();

        Glide.with(this)
                .load(img)
                .thumbnail(0.3f)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.ic_baseline_image_24)
                .fitCenter()
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        supportStartPostponedEnterTransition();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        supportStartPostponedEnterTransition();
                        return false;
                    }
                })
                .into(imgCanal);
        //retryLoad(videoUrl);
        //initializePlayer(videoUrl);
        MediaSource mediaSource = buildMediaSource(Uri.parse(videoUrl), null);
        exoPlayer.setMediaSource(mediaSource);
        exoPlayer.prepare();
        exoPlayer.setPlayWhenReady(true);
    }

    private void setNewChannelRV(CanalesFav currentChannel) {

        videoUrl = currentChannel.getUrl();
        name = currentChannel.getCanal();
        origin = currentChannel.getOrigin();
        referer = currentChannel.getReferer();
        img = currentChannel.getImg();
        userAgent = currentChannel.getUa();
        license = currentChannel.getLic();
        txtCanal.setText(name);

        //Toast.makeText(this, this.videoUrl + " - " + origin+ " - " + referer+ " - " + img+ " - " + userAgent+ " - " + license, Toast.LENGTH_LONG).show();

        Glide.with(this)
                .load(img)
                .thumbnail(0.3f)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.ic_baseline_image_24)
                .fitCenter()
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        supportStartPostponedEnterTransition();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        supportStartPostponedEnterTransition();
                        return false;
                    }
                })
                .into(imgCanal);
        //retryLoad(videoUrl);
        //initializePlayer(videoUrl);
        MediaSource mediaSource = buildMediaSource(Uri.parse(videoUrl), null);
        exoPlayer.setMediaSource(mediaSource);
        exoPlayer.prepare();
        exoPlayer.setPlayWhenReady(true);
    }


    private boolean isStreamingUrl(String url) {
        // Verificar si la URL termina con alguna de las extensiones de archivo de streaming conocidas
        return url.endsWith(".m3u8") || url.endsWith(".mpd") || url.endsWith(".ism") || url.endsWith(".mp4");
    }

    /*private int getChannelIndexByVideoUrl(String videoUrl) {
        for (int i = 0; i < channels.size(); i++) {
            Log.d("DEBUG", "Index: " + i + ", URL: " + channels.get(i).getUrl());
            if (channels.get(i).getUrl().equals(videoUrl)) {
                return i;
            }
        }
        return -1;
    }*/

    private int getChannelIndexByVideoUrl(String videoUrl) {
        for (int i = 0; i < channels.size(); i++) {
            Log.d("DEBUG", "Index: " + i + ", URL: " + channels.get(i).getUrl());
            if (videoUrl.startsWith(channels.get(i).getUrl())) {
                return i;
            }
        }
        return -1;
    }


    private int getChannelIndexByVideoUrlRV(String videoUrl) {
        for (int i = 0; i < channelsFav.size(); i++) {
            Log.d("DEBUG", "Index: " + i + ", URL: " + channelsFav.get(i).getUrl());
            if (channelsFav.get(i).getUrl().equals(videoUrl)) {
                return i;
            }
        }
        return -1;
    }



    @Override
    public void onPictureInPictureModeChanged(boolean isInPictureInPictureMode, Configuration newConfig) {


        if (isInPictureInPictureMode) {
            playerView.hideController();
            progressBar.setVisibility(View.GONE);
        } else {
            if (getLifecycle().getCurrentState() == Lifecycle.State.CREATED) {
                exoPlayer.stop();
            }
        }

        super.onPictureInPictureModeChanged(isInPictureInPictureMode, newConfig);
    }

    @Override
    public void onUserLeaveHint() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

            if (uiModeManager.getCurrentModeType() != Configuration.UI_MODE_TYPE_TELEVISION || !getPackageManager().hasSystemFeature(AMAZON_FEATURE_FIRE_TV)) {
                try {
                    Rational aspectRatio = new Rational(exoPlayer.getVideoFormat().width, exoPlayer.getVideoFormat().height);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        setPictureInPictureParams.setAspectRatio(aspectRatio);

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                            setPictureInPictureParams.setAutoEnterEnabled(true);
                        }
                        //setPictureInPictureParams.build();
                        PlayerActivity.this.enterPictureInPictureMode(setPictureInPictureParams.build());
                    } else {
                        PlayerActivity.this.enterPictureInPictureMode();
                    }
                    playerView.hideController();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

        }
    }
}