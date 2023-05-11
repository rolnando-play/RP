package com.noobpy.tvar;

import android.Manifest;
import android.app.Activity;
import android.app.UiModeManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.android.billingclient.api.AcknowledgePurchaseParams;
import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.ProductDetails;
import com.android.billingclient.api.ProductDetailsResponseListener;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.QueryProductDetailsParams;
import com.android.billingclient.api.QueryPurchasesParams;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.facebook.ads.AdSettings;
import com.github.ybq.android.spinkit.style.ThreeBounce;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.slider.Slider;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.common.collect.ImmutableList;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.ironsource.mediationsdk.ISBannerSize;
import com.ironsource.mediationsdk.IronSource;
import com.ironsource.mediationsdk.IronSourceBannerLayout;
import com.ironsource.mediationsdk.integration.IntegrationHelper;
import com.ironsource.mediationsdk.logger.IronSourceError;
import com.ironsource.mediationsdk.model.Placement;
import com.ironsource.mediationsdk.sdk.BannerListener;
import com.ironsource.mediationsdk.sdk.InterstitialListener;
import com.ironsource.mediationsdk.sdk.RewardedVideoListener;
import com.ironsource.mediationsdk.sdk.RewardedVideoManualListener;
import com.jakewharton.processphoenix.ProcessPhoenix;
import com.noobpy.tvar.player.PlayerActivity;
import com.noobpy.tvar.util.AES;
import com.noobpy.tvar.util.AppDatabase;
import com.noobpy.tvar.util.CanalesFav;
import com.noobpy.tvar.util.Global;
import com.noobpy.tvar.util.CanalesAdapter;
import com.noobpy.tvar.util.Canales;
import com.noobpy.tvar.util.ItemDao;
import com.noobpy.tvar.util.RecyclerviewAdapter;
import com.openmediation.sdk.InitCallback;
import com.openmediation.sdk.InitConfiguration;
import com.openmediation.sdk.OmAds;
import com.openmediation.sdk.interstitial.InterstitialAdListener;
import com.openmediation.sdk.utils.error.Error;
import com.openmediation.sdk.utils.model.Scene;
//import com.openmediation.testsuite.TestSuite;
import com.pixplicity.easyprefs.library.Prefs;
import com.startapp.sdk.ads.splash.SplashConfig;
import com.startapp.sdk.adsbase.StartAppAd;
import com.wortise.ads.AdError;
import com.wortise.ads.WortiseSdk;
import com.wortise.ads.consent.ConsentManager;
import com.wortise.ads.rewarded.models.Reward;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

import kotlin.Unit;

import org.apache.commons.io.FileUtils;


public class MainActivity extends AppCompatActivity implements RewardedVideoListener, InterstitialListener, BannerListener, RewardedVideoManualListener {
    private static final String TAG = "MyApplication";


    GridView gridView;

    List<Canales> canalesList;
    List<CanalesFav> canalesFav;
    CanalesAdapter adapter;
    SearchView searchView, search;

    ImageView imgActualizar, imgMas, imgPremium;

    ActionBar actionBar;
    UiModeManager uiModeManager;

    public static String radioName = "";

    TextView appName, txtFavInfo;
    FirebaseRemoteConfig remoteConfig;

    ProgressBar progressBar;

    int ads, listPosition, listSelect;
    private com.wortise.ads.interstitial.InterstitialAd mInterstitialRefresh;

    public static ProgressBar loading;

    public static IronSourceBannerLayout banner;

    boolean channels_view, orientation, zapping;
    SwitchMaterial switch_view, switch_orientation, switch_zapping;

    float quality;

    com.wortise.ads.interstitial.InterstitialAd mInterstitialChannel;
    com.wortise.ads.rewarded.RewardedAd mRewardedChannel;

    private boolean userPrefersAdFree;

    String premiumPrice;
    BillingClient billingClient;

    Spinner spinnerPosition;

    private RecyclerView recyclerView;
    private RecyclerviewAdapter recyclerviewAdapter;
    private AppDatabase appDatabase;
    private ItemDao itemDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uiModeManager = (UiModeManager) getSystemService(UI_MODE_SERVICE);

        if (uiModeManager.getCurrentModeType() == Configuration.UI_MODE_TYPE_TELEVISION && Build.VERSION.SDK_INT >= 28) {
            setContentView(R.layout.activity_main_tv);

            imgActualizar = findViewById(R.id.imgActualizar);
            imgMas = findViewById(R.id.imgMas);
            imgPremium = findViewById(R.id.btnPro);

            imgPremium.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    buyPro();
                }
            });

            imgActualizar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    actualizar();

                }
            });

            imgMas.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mas();
                }
            });

        } else {
            setContentView(R.layout.activity_main);
        }

        loadPrefs();
        loadBilling();
        loadAds();
        checkSubscription();
        setSearchviewRadius();

        if (uiModeManager.getCurrentModeType() != Configuration.UI_MODE_TYPE_TELEVISION) {
            if (userPrefersAdFree) {
                StartAppAd.disableSplash();
            } else {
                Random rand = new Random();
                int x = rand.nextInt(4);
                if (x == 0) {
                    StartAppAd.showSplash(this, savedInstanceState,
                            new SplashConfig()
                                    .setTheme(SplashConfig.Theme.USER_DEFINED)
                                    .setCustomScreen(R.layout.start_splash)
                                    .setOrientation(SplashConfig.Orientation.AUTO)
                    );
                } else {
                    StartAppAd.disableSplash();

                }
            }

        } else {
            StartAppAd.disableSplash();
        }

        Global.mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        gridView = findViewById(R.id.gridview);
        recyclerView = findViewById(R.id.recyclerview);
        appDatabase = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "my-database")
                .allowMainThreadQueries()
                .build();
        itemDao = appDatabase.itemDao();
// Obtener los elementos seleccionados del ListView


        crashlytics();

        actionBar = getSupportActionBar();

        if (uiModeManager.getCurrentModeType() == Configuration.UI_MODE_TYPE_TELEVISION && Build.VERSION.SDK_INT >= 28) {
            actionBar.hide();
        }

        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM,
                ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(R.layout.custom_action_bar_layout);
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorToolbarDark)));
        actionBar.setElevation(0);
        View view = getSupportActionBar().getCustomView();

        appName = view.findViewById(R.id.appName);
        //txtFavInfo = findViewById(R.id.txtFavInfo);

        canalesList = new ArrayList<>();
        canalesFav = new ArrayList<>();

        progressBar = findViewById(R.id.progressBar);
        ThreeBounce foldingCube = new ThreeBounce();
        foldingCube.setColor(Color.parseColor("#ffffff"));
        progressBar.setIndeterminateDrawable(foldingCube);


        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Canales selectedItem = (Canales) gridView.getItemAtPosition(i);
                CanalesFav selectedItemFav = new CanalesFav(selectedItem.getId(), selectedItem.getCanal(), selectedItem.getUrl(), selectedItem.getImg(), selectedItem.getLic(), selectedItem.getUa(), selectedItem.getOrigin(), selectedItem.getReferer());
                showConfirmationDialog(selectedItemFav);
                return true;
            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Canales selectedItem = (Canales) gridView.getItemAtPosition(position);
                Intent intent = new Intent(MainActivity.this, PlayerActivity.class);
                intent.putExtra("link", selectedItem.getUrl());
                intent.putExtra("name", selectedItem.getCanal());
                intent.putExtra("ua", selectedItem.getUa());
                intent.putExtra("referer", selectedItem.getReferer());
                intent.putExtra("origin", selectedItem.getOrigin());
                intent.putExtra("img", selectedItem.getImg());
                intent.putExtra("license", selectedItem.getLic());

                intent.putExtra("whichList", "listview");

                /*intent.putExtra("link", canalesList.get(position).getUrl());
                intent.putExtra("name", canalesList.get(position).getCanal());
                intent.putExtra("ua", canalesList.get(position).getUa());
                intent.putExtra("referer", canalesList.get(position).getReferer());
                intent.putExtra("origin", canalesList.get(position).getOrigin());
                intent.putExtra("img", canalesList.get(position).getImg());
                intent.putExtra("license", canalesList.get(position).getLic());*/
                intent.putParcelableArrayListExtra("channels", (ArrayList<Canales>) canalesList);

                Prefs.putInt("listPosition", position);

                IronSource.destroyBanner(banner);

                if (!userPrefersAdFree) {
                    if (selectedItem.getUrl().contains("m3u8") || selectedItem.getUrl().contains("mpd") || selectedItem.getUrl().contains("mp4")) {
                        if (uiModeManager.getCurrentModeType() != Configuration.UI_MODE_TYPE_TELEVISION) {
                            openPlayer(intent);
                        } else {
                            openPlayerTv(intent);
                        }
                    } else {
                        Intent intentWeb = new Intent(MainActivity.this, WebviewActivity.class);
                        intentWeb.putExtra("url", selectedItem.getUrl());
                        intentWeb.putExtra("isChannel", true);

                        startActivity(intentWeb);
                    }
                } else {
                    if (selectedItem.getUrl().contains("m3u8") || selectedItem.getUrl().contains("mpd") || selectedItem.getUrl().contains("mp4")) {
                        startActivity(intent);
                    } else {
                        Intent intentWeb = new Intent(MainActivity.this, WebviewActivity.class);
                        intentWeb.putExtra("url", selectedItem.getUrl());
                        intentWeb.putExtra("isChannel", true);

                        startActivity(intentWeb);
                    }
                }


            }
        });

        checkGooglePlay();
        //loadUi();

    }

    private void showConfirmationDialog(final CanalesFav item) {

        final BottomSheetDialog bottomCacheSheetDialog = new BottomSheetDialog(MainActivity.this, R.style.TransparentDialog);
        bottomCacheSheetDialog.setContentView(R.layout.custom_dialog_purchase);

        TextView message = bottomCacheSheetDialog.findViewById(R.id.txtMessage);
        TextView cancel = bottomCacheSheetDialog.findViewById(R.id.btnCancel);
        TextView agregar = bottomCacheSheetDialog.findViewById(R.id.btnComprar);
        TextView title = bottomCacheSheetDialog.findViewById(R.id.txtTitle);

        title.setText("Agregar " + item.getCanal() + " a favoritos");
        message.setText("¿Deseas agregar este canal a tu lista de favoritos?");
        agregar.setText("Agregar");
        cancel.setText("Cancelar");

        agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addItemToDatabase(item);
                bottomCacheSheetDialog.cancel();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomCacheSheetDialog.cancel();
            }
        });


        FrameLayout bottomSheet = (FrameLayout)
                bottomCacheSheetDialog.findViewById(R.id.design_bottom_sheet);
        BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        //bottomSheetDialog.getBehavior().setPeekHeight(1000);

        bottomCacheSheetDialog.show();

        bottomCacheSheetDialog.getWindow().setGravity(Gravity.CENTER_HORIZONTAL);

        Rect displayRectangle = new Rect();
        Window window = MainActivity.this.getWindow();

        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);

        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            bottomCacheSheetDialog.getWindow().setLayout((int) (displayRectangle.width() *
                    0.50f), (int) (displayRectangle.height() * 1f));
        } else {
            bottomCacheSheetDialog.getWindow().setLayout((int) (displayRectangle.width() *
                    0.90f), (int) (displayRectangle.height() * 1f));
        }
        /*AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirmación")
                .setMessage("¿Deseas agregar este elemento a la base de datos?")
                .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        addItemToDatabase(item);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();*/


    }

    private void addItemToDatabase(CanalesFav newItem) {
        // Verificar si el elemento ya está presente en itemList
        boolean isDuplicate = false;
        for (CanalesFav item : canalesFav) {
            if (item.getId() == newItem.getId()) {
                isDuplicate = true;
                break;
            }
        }

        if (!isDuplicate) {
            // Agregar el elemento a la base de datos (usando Room u otro método de almacenamiento)
            // Por ejemplo, utilizando la instancia itemDao:
            itemDao.insertItem(newItem);
            Toast.makeText(this, newItem.getCanal() + " se agregó correctamente", Toast.LENGTH_SHORT).show();

            // Verificar si el elemento ya está presente en itemList
            boolean isAlreadyAdded = false;
            for (CanalesFav item : canalesFav) {
                if (item.getId() == newItem.getId()) {
                    isAlreadyAdded = true;
                    break;
                }
            }

            if (!isAlreadyAdded) {
                // Actualizar la lista del RecyclerView
                canalesFav.add(newItem);
                adapter.notifyDataSetChanged();

                // Verificar si itemList está vacía y mostrar u ocultar las vistas correspondientes
                if (canalesFav.isEmpty()) {
                    recyclerView.setVisibility(View.GONE);
                    //txtFavInfo.setVisibility(View.VISIBLE);
                } else {
                    recyclerView.setVisibility(View.VISIBLE);
                    //txtFavInfo.setVisibility(View.GONE);
                }
            }
        } else {
            // Mostrar un mensaje indicando que el elemento ya existe
            Toast.makeText(this, newItem.getCanal() + " ya se ha agregado", Toast.LENGTH_SHORT).show();
        }
    }


    public void hideRecyclerView() {
        recyclerView.setVisibility(View.GONE);
        //txtFavInfo.setVisibility(View.VISIBLE);
    }


    private List<CanalesFav> getSelectedItems() {
        List<CanalesFav> selectedItems = new ArrayList<>();

        SparseBooleanArray checkedItems = gridView.getCheckedItemPositions();
        if (checkedItems != null) {
            for (int i = 0; i < checkedItems.size(); i++) {
                int position = checkedItems.keyAt(i);
                if (checkedItems.valueAt(i)) {
                    selectedItems.add((CanalesFav) gridView.getItemAtPosition(position));
                }
            }
        }

        return selectedItems;
    }


    private void compareAndDeleteItems() {
        List<CanalesFav> itemsToRemove = new ArrayList<>();

        for (CanalesFav item : canalesFav) {
            boolean found = false;
            for (int i = 0; i < gridView.getCount(); i++) {
                Canales listViewItem = (Canales) gridView.getItemAtPosition(i);
                if (item.getId() == listViewItem.getId()) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                itemsToRemove.add(item);
            }
        }

        for (CanalesFav item : itemsToRemove) {
            canalesFav.remove(item);
        }

        adapter.notifyDataSetChanged();
    }


    private void setSearchviewRadius() {
        // Obtener el radio de los bordes de la pantalla
        int cornerRadius = getScreenCornerRadius();
// Crear el objeto GradientDrawable y establecer los radios de las esquinas superiores
        GradientDrawable drawable = new GradientDrawable();
        float[] radii = {0, 0, 0, 0, cornerRadius, cornerRadius, cornerRadius, cornerRadius};
        drawable.setCornerRadii(radii);

        drawable.setColor(getResources().getColor(R.color.colorToolbarDark));

// Asignar el objeto drawable a una vista
        View myView = findViewById(R.id.search);
        myView.setBackground(drawable);

    }

    public int getScreenCornerRadius() {
        // Obtener los DisplayMetrics
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        // Obtener el radio de los bordes de la pantalla
        int cornerRadius = Math.round(10 * (metrics.densityDpi / 160f));

        return cornerRadius;
    }

    private void checkSubscription() {
        billingClient = BillingClient.newBuilder(this).enablePendingPurchases().setListener((billingResult, list) -> {
        }).build();
        final BillingClient finalBillingClient = billingClient;
        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingServiceDisconnected() {
            }

            @Override
            public void onBillingSetupFinished(@NonNull BillingResult billingResult) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    finalBillingClient.queryPurchasesAsync(
                            QueryPurchasesParams.newBuilder().setProductType(BillingClient.ProductType.INAPP).build(), (billingResult1, list) -> {
                                if (billingResult1.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                                    Log.d("testOffer", list.size() + " size");
                                    if (list.size() > 0) {
                                        //((Activity) MainActivity.this).runOnUiThread(() -> Toast.makeText(MainActivity.this, "PURCHASE", Toast.LENGTH_SHORT).show());
                                        Prefs.putBoolean("userPrefersAdFree", true);
                                        //prefs.setPremium(1); // set 1 to activate premium feature
                                        int i = 0;
                                        for (Purchase purchase : list) {
                                            //Here you can manage each product, if you have multiple subscription
                                            Log.d("testOffer", purchase.getOriginalJson()); // Get to see the order information
                                            Log.d("testOffer", " index" + i);
                                            i++;
                                        }
                                    } else {
                                        //((Activity) MainActivity.this).runOnUiThread(() -> Toast.makeText(MainActivity.this, "Error al restaurar tu compra", Toast.LENGTH_SHORT).show());
                                        Prefs.putBoolean("userPrefersAdFree", false);
                                        //prefs.setPremium(0); // set 0 to de-activate premium feature
                                    }
                                }
                            });
                }
            }
        });
    }

    private void loadBilling() {
        PurchasesUpdatedListener purchasesUpdatedListener = new PurchasesUpdatedListener() {
            @Override
            public void onPurchasesUpdated(BillingResult billingResult, List<Purchase> purchases) {
                // To be implemented in a later section.
            }
        };

        billingClient = BillingClient.newBuilder(this)
                .setListener(purchasesUpdatedListener)
                .enablePendingPurchases()
                .setListener(new PurchasesUpdatedListener() {
                    @Override
                    public void onPurchasesUpdated(@NonNull BillingResult billingResult, @Nullable List<Purchase> list) {
                        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && list != null) {
                            for (Purchase purchase : list) {
                                verifySubPurchase(purchase);
                            }
                        }
                    }
                })
                .build();

        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(BillingResult billingResult) {
                //((Activity)MainActivity.this).runOnUiThread(() -> Toast.makeText(MainActivity.this ,"onBillingSetupFinished", Toast.LENGTH_SHORT).show());

                /*if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    billingClient.queryPurchasesAsync(
                            QueryPurchasesParams.newBuilder()
                                    .setProductType(BillingClient.ProductType.INAPP)
                                    .build(),
                            new PurchasesResponseListener() {
                                public void onQueryPurchasesResponse(BillingResult billingResult, List purchases) {
                                    if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                                        ((Activity) MainActivity.this).runOnUiThread(() -> Toast.makeText(MainActivity.this, "OK", Toast.LENGTH_SHORT).show());

                                        // Process returned purchase list (display the in-app items the user owns).
                                    } else if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.ERROR) {
                                        // Handle the error response.
                                        ((Activity) MainActivity.this).runOnUiThread(() -> Toast.makeText(MainActivity.this, "ERROR", Toast.LENGTH_SHORT).show());

                                    } else if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.DEVELOPER_ERROR) {
                                        // Handle the developer error response.
                                        ((Activity) MainActivity.this).runOnUiThread(() -> Toast.makeText(MainActivity.this, "DEVELOPER_ERROR", Toast.LENGTH_SHORT).show());

                                    } else {
                                        // Other error codes are available, but are never returned by
                                        // the Appstore Billing Compatibility SDK.
                                    }
                                }
                            }
                    );

                }*/
            }

            @Override
            public void onBillingServiceDisconnected() {
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
            }
        });
    }

    private void verifySubPurchase(Purchase purchase) {
        AcknowledgePurchaseParams acknowledgePurchaseParams = AcknowledgePurchaseParams
                .newBuilder()
                .setPurchaseToken(purchase.getPurchaseToken())
                .build();
        billingClient.acknowledgePurchase(acknowledgePurchaseParams, billingResult -> {
            if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                //user prefs to set premium
                //Toast.makeText(MainActivity.this, "You are a premium user now", Toast.LENGTH_SHORT).show();
                ((Activity) MainActivity.this).runOnUiThread(() -> Toast.makeText(MainActivity.this, "Compra realizada correctamente", Toast.LENGTH_LONG).show());
                Prefs.putBoolean("userPrefersAdFree", true);

                ((Activity) MainActivity.this).runOnUiThread(this::restartAppPurchase);
            }
        });
        Log.d(TAG, "Purchase Token: " + purchase.getPurchaseToken());
        Log.d(TAG, "Purchase Time: " + purchase.getPurchaseTime());
        Log.d(TAG, "Purchase OrderID: " + purchase.getOrderId());
    }

    private void restartAppPurchase() {
        final BottomSheetDialog bottomCacheSheetDialog = new BottomSheetDialog(MainActivity.this, R.style.TransparentDialog);
        bottomCacheSheetDialog.setContentView(R.layout.custom_dialog_purchase);

        TextView message = bottomCacheSheetDialog.findViewById(R.id.txtMessage);
        TextView cancel = bottomCacheSheetDialog.findViewById(R.id.btnCancel);
        TextView buy = bottomCacheSheetDialog.findViewById(R.id.btnComprar);
        TextView title = bottomCacheSheetDialog.findViewById(R.id.txtTitle);

        title.setText("Compra realizada, aplicar cambios");
        message.setText("Reinicia la app para aplicar los cambios y eliminar los anuncios.");
        buy.setText("Reiniciar");
        cancel.setText("Más tarde");

        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*finish();
                startActivity(getIntent());*/
                FileUtils.deleteQuietly(getCacheDir());
                FileUtils.deleteQuietly(getExternalCacheDir());
                ProcessPhoenix.triggerRebirth(MainActivity.this);
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomCacheSheetDialog.cancel();
            }
        });


        FrameLayout bottomSheet = (FrameLayout)
                bottomCacheSheetDialog.findViewById(R.id.design_bottom_sheet);
        BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        //bottomSheetDialog.getBehavior().setPeekHeight(1000);

        bottomCacheSheetDialog.show();

        bottomCacheSheetDialog.getWindow().setGravity(Gravity.CENTER_HORIZONTAL);

        Rect displayRectangle = new Rect();
        Window window = MainActivity.this.getWindow();

        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);

        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            bottomCacheSheetDialog.getWindow().setLayout((int) (displayRectangle.width() *
                    0.50f), (int) (displayRectangle.height() * 1f));
        } else {
            bottomCacheSheetDialog.getWindow().setLayout((int) (displayRectangle.width() *
                    0.90f), (int) (displayRectangle.height() * 1f));
        }
    }


    private void crashlytics() {
        String id = Settings.Secure.getString(MainActivity.this.getContentResolver(), Settings.Secure.ANDROID_ID);
        FirebaseCrashlytics.getInstance().setUserId(id);
    }


    private void openPlayerTv(Intent intent) {
        AlertDialog dialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        // layoutinflater object and use activity to get layout inflater
        LayoutInflater inflater = MainActivity.this.getLayoutInflater();
        View v = inflater.inflate(R.layout.loading_ad, null);  // this line
        ProgressBar loadingBar = v.findViewById(R.id.progressBar);
        ThreeBounce foldingCube = new ThreeBounce();
        foldingCube.setColor(Color.parseColor("#ffffff"));
        loadingBar.setIndeterminateDrawable(foldingCube);

        builder.setView(v);
        builder.setCancelable(false);
        dialog = builder.create();

        switch (ads) {
            case 0:
                ads++;
                Prefs.putInt("ads", ads);
                dialog.show();

                if (com.openmediation.sdk.interstitial.InterstitialAd.isReady()) {
                    com.openmediation.sdk.interstitial.InterstitialAd.showAd();

                    com.openmediation.sdk.interstitial.InterstitialAd.setAdListener(new InterstitialAdListener() {

                        /**
                         * Invoked when the interstitial ad availability status is changed.
                         *
                         * @param - available is a boolean.
                         *          True: means the interstitial ad is available and you can
                         *              show the video by calling InterstitialAd.showAd().
                         *          False: means no ad are available
                         */
                        @Override
                        public void onInterstitialAdAvailabilityChanged(boolean available) {
                            // Change the interstitial ad state in app according to param available.
                        }

                        /**
                         * Invoked when the Interstitial ad view has opened.
                         * Your activity will lose focus.
                         */
                        @Override
                        public void onInterstitialAdShowed(Scene scene) {
                            // Do not perform heavy tasks till the ad is going to be closed.
                        }

                        /**
                         * Invoked when the Interstitial ad is closed.
                         * Your activity will regain focus.
                         */
                        @Override
                        public void onInterstitialAdClosed(Scene scene) {
                            dialog.cancel();
                            startActivity(intent);
                        }

                        /**
                         * Invoked when the user clicked on the Interstitial ad.
                         */
                        @Override
                        public void onInterstitialAdClicked(Scene scene) {
                        }

                        /* Invoked when the Interstitial ad has showed failed
                         * @param - error contains the reason for the failure:
                         */
                        @Override
                        public void onInterstitialAdShowFailed(Scene scene, Error error) {
                            // Interstitial ad show failed
                            dialog.cancel();
                            startActivity(intent);
                        }
                    });
                } else {
                    dialog.cancel();
                    startActivity(intent);
                }


                break;
            case 1:
                ads++;
                Prefs.putInt("ads", ads);
                startActivity(intent);
                break;

            case 2:
                ads++;
                dialog.show();
                Prefs.putInt("ads", ads);

                if (com.openmediation.sdk.video.RewardedVideoAd.isReady()) {
                    com.openmediation.sdk.video.RewardedVideoAd.showAd();

                    com.openmediation.sdk.video.RewardedVideoAd.setAdListener(new com.openmediation.sdk.video.RewardedVideoListener() {

                        /**
                         * Invoked when the ad availability status is changed.
                         *
                         * @param available is a boolean.
                         *      True: means the rewarded videos is available and
                         *          you can show the video by calling RewardedVideoAd.showAd().
                         *      False: means no videos are available
                         */
                        @Override
                        public void onRewardedVideoAvailabilityChanged(boolean available) {
                            // Change the rewarded video state according to availability in app.
                            // You could show ad right after it's was loaded here
                            /*if(available) {
                                dialog.cancel();
                                com.openmediation.sdk.video.RewardedVideoAd.showAd();
                            }*/
                        }

                        /**
                         * Invoked when the RewardedVideo ad view has opened.
                         * Your activity will lose focus.
                         */
                        @Override
                        public void onRewardedVideoAdShowed(Scene scene) {
                            // Do not perform heavy tasks till the video ad is going to be closed.
                        }

                        /**
                         * Invoked when the call to show a rewarded video has failed
                         * @param error contains the reason for the failure:
                         */
                        @Override
                        public void onRewardedVideoAdShowFailed(Scene scene, Error error) {
                            // Video Ad show failed
                            dialog.cancel();
                            startActivity(intent);
                        }

                        /**
                         * Invoked when the user clicked on the RewardedVideo ad.
                         */
                        @Override
                        public void onRewardedVideoAdClicked(Scene scene) {
                            // Video Ad is clicked
                        }

                        /**
                         * Invoked when the RewardedVideo ad is closed.
                         * Your activity will regain focus.
                         */
                        @Override
                        public void onRewardedVideoAdClosed(Scene scene) {
                            // Video Ad Closed
                            dialog.cancel();
                            startActivity(intent);
                        }

                        /**
                         * Invoked when the RewardedVideo ad start to play.
                         * NOTE:You may not receive this callback on some AdNetworks.
                         */
                        @Override
                        public void onRewardedVideoAdStarted(Scene scene) {
                            // Video Ad Started
                        }

                        /**
                         * Invoked when the RewardedVideo ad play end.
                         * NOTE:You may not receive this callback on some AdNetworks.
                         */
                        @Override
                        public void onRewardedVideoAdEnded(Scene scene) {
                            // Video Ad play end
                        }

                        /**
                         * Invoked when the video is completed and the user should be rewarded.
                         * If using server-to-server callbacks you may ignore this events and wait
                         * for the callback from the OpenMediation server.
                         */
                        @Override
                        public void onRewardedVideoAdRewarded(Scene scene) {
                            // Here you can reward the user according to your in-app settings.
                        }
                    });
                } else {
                    dialog.cancel();
                    startActivity(intent);

                }


                break;
            case 3:
            case 4:
                ads++;
                Prefs.putInt("ads", ads);
                startActivity(intent);
                break;

            case 5:
                ads = 0;
                Prefs.putInt("ads", ads);
                startActivity(intent);
                break;

            default:
                break;
        }
    }

    private void openPlayer(Intent intent) {
        AlertDialog dialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        // layoutinflater object and use activity to get layout inflater
        LayoutInflater inflater = MainActivity.this.getLayoutInflater();
        View v = inflater.inflate(R.layout.loading_ad, null);  // this line
        ProgressBar loadingBar = v.findViewById(R.id.progressBar);
        ThreeBounce foldingCube = new ThreeBounce();
        foldingCube.setColor(Color.parseColor("#ffffff"));
        loadingBar.setIndeterminateDrawable(foldingCube);

        builder.setView(v);
        builder.setCancelable(false);
        dialog = builder.create();

        switch (ads) {
            case 0:
                ads++;
                Prefs.putInt("ads", ads);
                dialog.show();


                IronSource.loadInterstitial();
                IronSource.setInterstitialListener(new InterstitialListener() {
                    @Override
                    public void onInterstitialAdReady() {
                        IronSource.showInterstitial("Interstitial_main");
                        dialog.hide();
                        dialog.cancel();
                    }

                    @Override
                    public void onInterstitialAdLoadFailed(IronSourceError ironSourceError) {
                        startActivity(intent);
                        dialog.hide();
                        dialog.cancel();

                    }

                    @Override
                    public void onInterstitialAdOpened() {
                    }

                    @Override
                    public void onInterstitialAdClosed() {
                        startActivity(intent);
                        IronSource.destroyBanner(banner);
                    }

                    @Override
                    public void onInterstitialAdShowSucceeded() {

                    }

                    @Override
                    public void onInterstitialAdShowFailed(IronSourceError ironSourceError) {

                    }

                    @Override
                    public void onInterstitialAdClicked() {

                    }
                });


                break;
            case 1:
                ads++;
                Prefs.putInt("ads", ads);
                startActivity(intent);
                break;

            case 2:
                ads++;
                dialog.show();
                Prefs.putInt("ads", ads);

                IronSource.loadRewardedVideo();
                IronSource.setManualLoadRewardedVideo(new RewardedVideoManualListener() {
                    @Override
                    public void onRewardedVideoAdReady() {
                        IronSource.showRewardedVideo("Rewarded_main");
                        dialog.hide();
                        dialog.cancel();
                    }

                    @Override
                    public void onRewardedVideoAdLoadFailed(IronSourceError ironSourceError) {
                        startActivity(intent);
                        dialog.hide();
                        dialog.cancel();
                    }

                    @Override
                    public void onRewardedVideoAdOpened() {

                    }

                    @Override
                    public void onRewardedVideoAdClosed() {
                        startActivity(intent);
                        dialog.hide();
                        dialog.cancel();
                        IronSource.destroyBanner(banner);
                    }

                    @Override
                    public void onRewardedVideoAvailabilityChanged(boolean b) {
                    }

                    @Override
                    public void onRewardedVideoAdStarted() {

                    }

                    @Override
                    public void onRewardedVideoAdEnded() {

                    }

                    @Override
                    public void onRewardedVideoAdRewarded(Placement placement) {

                    }

                    @Override
                    public void onRewardedVideoAdShowFailed(IronSourceError ironSourceError) {
                        startActivity(intent);

                    }

                    @Override
                    public void onRewardedVideoAdClicked(Placement placement) {

                    }
                });

                break;
            case 3:
            case 4:
                ads++;
                Prefs.putInt("ads", ads);
                startActivity(intent);
                break;

            case 5:
                ads++;
                dialog.show();
                Prefs.putInt("ads", ads);

                double random = Math.random();
                if (random <= 0.5) {

                    if (mInterstitialChannel.isAvailable()) {
                        mInterstitialChannel.showAd();
                        dialog.cancel();

                        mInterstitialChannel.setListener(new com.wortise.ads.interstitial.InterstitialAd.Listener() {
                            @Override
                            public void onInterstitialClicked(@NonNull com.wortise.ads.interstitial.InterstitialAd ad) {
                                // Invocado cuando el usuario hace click sobre el intersticial
                            }

                            @Override
                            public void onInterstitialDismissed(@NonNull com.wortise.ads.interstitial.InterstitialAd ad) {
                                // Invocado cuando el intersticial es cerrado
                                startActivity(intent);
                            }

                            @Override
                            public void onInterstitialFailed(@NonNull com.wortise.ads.interstitial.InterstitialAd ad,
                                                             @NonNull AdError error) {
                                // Invocado cuando la carga del intersticial ha fallado
                                // (por error o falta de inventario)
                                startActivity(intent);
                            }

                            @Override
                            public void onInterstitialLoaded(@NonNull com.wortise.ads.interstitial.InterstitialAd ad) {
                                // Invocado cuando la carga del intersticial ha sido exitosa
                                mInterstitialChannel.showAd();
                            }

                            @Override
                            public void onInterstitialShown(@NonNull com.wortise.ads.interstitial.InterstitialAd ad) {
                                // Invocado cuando se muestra el intersticial
                            }
                        });
                    } else {
                        dialog.cancel();
                        startActivity(intent);
                    }


                } else {


                    if (mRewardedChannel.isAvailable()) {
                        mRewardedChannel.showAd();
                        dialog.cancel();
                        mRewardedChannel.setListener(new com.wortise.ads.rewarded.RewardedAd.Listener() {
                            @Override
                            public void onRewardedClicked(@NonNull com.wortise.ads.rewarded.RewardedAd ad) {
                                // Invocado cuando el usuario hace click sobre el rewarded
                            }

                            @Override
                            public void onRewardedCompleted(@NonNull com.wortise.ads.rewarded.RewardedAd ad,
                                                            @NonNull Reward reward) {
                                // Invocado cuando el rewarded ha sido completado
                            }

                            @Override
                            public void onRewardedDismissed(@NonNull com.wortise.ads.rewarded.RewardedAd ad) {
                                // Invocado cuando el rewarded es cerrado
                                startActivity(intent);
                            }

                            @Override
                            public void onRewardedFailed(@NonNull com.wortise.ads.rewarded.RewardedAd ad,
                                                         @NonNull AdError error) {
                                // Invocado cuando la carga del rewarded ha fallado
                                // (por error o falta de inventario)
                                startActivity(intent);
                            }

                            @Override
                            public void onRewardedLoaded(@NonNull com.wortise.ads.rewarded.RewardedAd ad) {
                                // Invocado cuando la carga del rewarded ha sido exitosa
                                mRewardedChannel.showAd();
                            }

                            @Override
                            public void onRewardedShown(@NonNull com.wortise.ads.rewarded.RewardedAd ad) {
                                // Invocado cuando se muestra el rewarded

                            }
                        });
                    } else {
                        dialog.cancel();
                        startActivity(intent);
                    }


                }

            case 6:
            case 7:
            case 8:
                ads++;
                Prefs.putInt("ads", ads);
                startActivity(intent);
                break;

            case 9:
                ads = 0;
                Prefs.putInt("ads", ads);
                startActivity(intent);
                break;

            default:
                break;
        }
    }

    private void mas() {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this, R.style.TransparentDialog);
        bottomSheetDialog.setContentView(R.layout.layout_settings);

        FrameLayout bottomSheet = bottomSheetDialog.findViewById(R.id.design_bottom_sheet);
        BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);

        switch_view = bottomSheetDialog.findViewById(R.id.switch_view);
        switch_orientation = bottomSheetDialog.findViewById(R.id.switch_orientation);
        switch_zapping = bottomSheetDialog.findViewById(R.id.switch_zapping);

        spinnerPosition = bottomSheetDialog.findViewById(R.id.spinner);

        RelativeLayout btn_video_view_type = bottomSheetDialog.findViewById(R.id.btn_switch_view);
        RelativeLayout btn_orientation = bottomSheetDialog.findViewById(R.id.btn_switch_orientation);
        RelativeLayout btn_zapping = bottomSheetDialog.findViewById(R.id.btn_switch_zapping);
        RelativeLayout btnPosition = bottomSheetDialog.findViewById(R.id.btn_switch_position);

        btnPosition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spinnerPosition.performClick();
            }
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_item) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView textView = (TextView) view.findViewById(android.R.id.text1);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20); // Cambia el tamaño del texto aquí
                return view;
            }
        };
        adapter.add("Desactivado");
        adapter.add("Solo enfocar");
        adapter.add("Seleccionar");
        spinnerPosition.setAdapter(adapter);
        spinnerPosition.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Obtén la opción seleccionada
                String selectedOption = parent.getItemAtPosition(position).toString();
                // Realiza las acciones necesarias con la opción seleccionada
                Prefs.putInt("listSelect", position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Acciones a realizar cuando no se ha seleccionado ninguna opción
            }
        });
        spinnerPosition.setSelection(Prefs.getInt("listSelect"));


        TextView txt_apply = bottomSheetDialog.findViewById(R.id.txt_apply);

        if (uiModeManager.getCurrentModeType() != Configuration.UI_MODE_TYPE_TELEVISION) {
            btn_orientation.setVisibility(View.VISIBLE);
            btn_zapping.setVisibility(View.GONE);
        }

        ImageView btn_close = bottomSheetDialog.findViewById(R.id.btn_close);
        btn_close.setOnClickListener(view -> {
            bottomSheetDialog.cancel();
        });

        ImageView btn_info = bottomSheetDialog.findViewById(R.id.btn_info);
        btn_info.setOnClickListener(view -> {
            bottomSheetDialog.hide();
            if (uiModeManager.getCurrentModeType() != Configuration.UI_MODE_TYPE_TELEVISION) {
                IronSource.destroyBanner(banner);
                Intent intent = new Intent(MainActivity.this, InfoActivity.class);
                startActivity(intent);
            } else {
                final BottomSheetDialog bottomSheetDialogInfo = new BottomSheetDialog(this, R.style.TransparentDialog);
                bottomSheetDialogInfo.setContentView(R.layout.dialog_about);

                TextView txtAppVersion = bottomSheetDialogInfo.findViewById(R.id.txt_app_version);
                TextView txtPrivacy = bottomSheetDialogInfo.findViewById(R.id.txtPrivacy);
                TextView txtMore = bottomSheetDialogInfo.findViewById(R.id.txtMore);
                TextView txtOk = bottomSheetDialogInfo.findViewById(R.id.txtOk);

                txtAppVersion.setText(getString(R.string.msg_about_version) + " " + BuildConfig.VERSION_CODE + " (" + BuildConfig.VERSION_NAME + ")");

                txtPrivacy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (uiModeManager.getCurrentModeType() != Configuration.UI_MODE_TYPE_TELEVISION) {
                            try {
                                Intent intent = new Intent(Intent.ACTION_VIEW).setData(Uri.parse(Global.privacidad));
                                startActivity(intent);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        } else {
                            try {
                                /*Intent intent = new Intent(MainActivity.this, WebviewActivity.class);
                                startActivity(intent);*/
                                Intent intent = new Intent(MainActivity.this, WebviewActivity.class);
                                intent.putExtra("url", Global.privacidad);
                                intent.putExtra("isChannel", false);

                                startActivity(intent);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    }
                });


                String versionName = BuildConfig.VERSION_NAME;
                String appVersion = remoteConfig.getString("appVersion");
                if (!Objects.equals(appVersion, versionName)) {
                    txtMore.setVisibility(View.VISIBLE);
                }

                txtMore.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                        try {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                        } catch (android.content.ActivityNotFoundException anfe) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                        }
                    }
                });

                txtOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bottomSheetDialogInfo.hide();
                    }
                });

                FrameLayout bottomSheetInfo = (FrameLayout)
                        bottomSheetDialogInfo.findViewById(R.id.design_bottom_sheet);
                BottomSheetBehavior behaviorInfo = BottomSheetBehavior.from(bottomSheetInfo);
                behaviorInfo.setState(BottomSheetBehavior.STATE_EXPANDED);

                bottomSheetDialogInfo.show();

                bottomSheetDialogInfo.getWindow().setGravity(Gravity.CENTER_HORIZONTAL);

                Rect displayRectangle = new Rect();
                Window window = MainActivity.this.getWindow();

                window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);

                int orientation = getResources().getConfiguration().orientation;
                if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    bottomSheetDialogInfo.getWindow().setLayout((int) (displayRectangle.width() *
                            0.50f), (int) (displayRectangle.height() * 1f));
                } else {
                    bottomSheetDialogInfo.getWindow().setLayout((int) (displayRectangle.width() *
                            0.90f), (int) (displayRectangle.height() * 1f));
                }


            }
        });

        //boolean channels_view = Prefs.getBoolean("channels_view", true);
        switch_view.setChecked(channels_view);
        switch_orientation.setChecked(orientation);
        switch_zapping.setChecked(zapping);

        switch_view.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                txt_apply.setVisibility(View.VISIBLE);
            }
        });

        txt_apply.setOnClickListener(view -> {
            finish();
            startActivity(getIntent());
        });

        final TextView txtVideoViewType = bottomSheetDialog.findViewById(R.id.txt_view_type);
        if (channels_view) {
            txtVideoViewType.setText("Grilla");
            switch_view.setChecked(true);
        } else {
            txtVideoViewType.setText("Lista");
            switch_view.setChecked(false);
        }

        btn_video_view_type.setOnClickListener(view -> {
            if (switch_view.isChecked()) {
                channels_view = false;
                Prefs.putBoolean("channels_view", false);
                switch_view.setChecked(false);
                txtVideoViewType.setText("Lista");
            } else {
                channels_view = true;
                Prefs.putBoolean("channels_view", true);
                switch_view.setChecked(true);
                txtVideoViewType.setText("Grilla");
            }
        });


        final TextView txtOrientation = bottomSheetDialog.findViewById(R.id.txt_orientation);
        if (orientation) {
            txtOrientation.setText("Vertical");
            switch_orientation.setChecked(true);
        } else {
            txtOrientation.setText("Horizontal");
            switch_orientation.setChecked(false);
        }

        btn_orientation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (switch_orientation.isChecked()) {
                    orientation = false;
                    Prefs.putBoolean("orientation", false);
                    switch_orientation.setChecked(false);
                    txtOrientation.setText("Horizontal");
                } else {
                    orientation = true;
                    Prefs.putBoolean("orientation", true);
                    switch_orientation.setChecked(true);
                    txtOrientation.setText("Vertical");
                }
            }
        });

        btn_zapping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (switch_zapping.isChecked()) {
                    zapping = false;
                    Prefs.putBoolean("zapping", false);
                    switch_zapping.setChecked(false);
                } else {
                    zapping = true;
                    Prefs.putBoolean("zapping", true);
                    switch_zapping.setChecked(true);
                }
            }
        });


        Slider sliderQuality = bottomSheetDialog.findViewById(R.id.slider_quality);
        sliderQuality.setValue(quality);

        sliderQuality.addOnChangeListener(new Slider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {
                quality = value;
                Prefs.putFloat("quality", value);
            }
        });

        TextView txt_cache_size = bottomSheetDialog.findViewById(R.id.txt_cache_size);
        LinearLayout relative_cache = bottomSheetDialog.findViewById(R.id.btn_cache);

        txt_cache_size.setText(getString(R.string.sub_setting_clear_cache_start) + " " + readableFileSize((0 + getDirSize(getCacheDir())) + getDirSize(getExternalCacheDir())) + " " + getString(R.string.sub_setting_clear_cache_end));


        relative_cache.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                dialog.setMessage(R.string.msg_clear_cache);*/

                final BottomSheetDialog bottomCacheSheetDialog = new BottomSheetDialog(MainActivity.this, R.style.TransparentDialog);
                bottomCacheSheetDialog.setContentView(R.layout.custom_dialog);

                TextView txtMessage = bottomCacheSheetDialog.findViewById(R.id.txtMessage);
                TextView settings = bottomCacheSheetDialog.findViewById(R.id.settings);
                TextView cancel = bottomCacheSheetDialog.findViewById(R.id.cancel);
                ProgressBar pbCache = bottomCacheSheetDialog.findViewById(R.id.pb_cache);

                txtMessage.setText(R.string.msg_clear_cache);
                settings.setText(R.string.option_yes);
                cancel.setText(R.string.option_cancel);

                settings.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        FileUtils.deleteQuietly(getCacheDir());
                        FileUtils.deleteQuietly(getExternalCacheDir());

                        /*final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this, R.style.AlertDialogTheme_Center);
                        progressDialog.setTitle(R.string.msg_clearing_cache);
                        progressDialog.setMessage(getString(R.string.msg_please_wait));
                        progressDialog.setCancelable(false);
                        progressDialog.show();*/

                        pbCache.setVisibility(View.VISIBLE);

                        new Handler(Looper.getMainLooper()).postDelayed(() -> {
                            txt_cache_size.setText(getString(R.string.sub_setting_clear_cache_start) + " 0 Bytes " + getString(R.string.sub_setting_clear_cache_end));
                            //Snackbar.make(findViewById(android.R.id.content), getString(R.string.msg_cache_cleared), Snackbar.LENGTH_SHORT).show();
                            //progressDialog.dismiss();
                            bottomCacheSheetDialog.cancel();
                            Toast.makeText(MainActivity.this, R.string.msg_cache_cleared, Toast.LENGTH_LONG).show();
                            pbCache.setVisibility(View.GONE);

                        }, 3000);
                    }
                });

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        bottomCacheSheetDialog.cancel();
                    }
                });

                FrameLayout bottomSheet = (FrameLayout)
                        bottomCacheSheetDialog.findViewById(R.id.design_bottom_sheet);
                BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                //bottomSheetDialog.getBehavior().setPeekHeight(1000);

                bottomCacheSheetDialog.show();

                bottomCacheSheetDialog.getWindow().setGravity(Gravity.CENTER_HORIZONTAL);

                Rect displayRectangle = new Rect();
                Window window = MainActivity.this.getWindow();

                window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);

                int orientation = getResources().getConfiguration().orientation;
                if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    bottomCacheSheetDialog.getWindow().setLayout((int) (displayRectangle.width() *
                            0.50f), (int) (displayRectangle.height() * 1f));
                } else {
                    bottomCacheSheetDialog.getWindow().setLayout((int) (displayRectangle.width() *
                            0.90f), (int) (displayRectangle.height() * 1f));
                }

            }
        });


        bottomSheetDialog.show();

        bottomSheetDialog.getWindow().setGravity(Gravity.CENTER_HORIZONTAL);

        Rect displayRectangle = new Rect();
        Window window = MainActivity.this.getWindow();

        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);

        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            bottomSheetDialog.getWindow().setLayout((int) (displayRectangle.width() *
                    0.65f), (int) (displayRectangle.height() * 1f));
        } else {
            bottomSheetDialog.getWindow().setLayout((int) (displayRectangle.width() *
                    0.90f), (int) (displayRectangle.height() * 1f));
        }

    }


    public long getDirSize(File dir) {
        long size = 0;
        for (File file : dir.listFiles()) {
            if (file != null && file.isDirectory()) {
                size += getDirSize(file);
            } else if (file != null && file.isFile()) {
                size += file.length();
            }
        }
        return size;
    }

    public static String readableFileSize(long size) {
        if (size <= 0) {
            return "0 Bytes";
        }
        String[] units = new String[]{"Bytes", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10((double) size) / Math.log10(1024.0d));
        StringBuilder stringBuilder = new StringBuilder();
        DecimalFormat decimalFormat = new DecimalFormat("#,##0.#");
        double d = (double) size;
        double pow = Math.pow(1024.0d, (double) digitGroups);
        Double.isNaN(d);
        stringBuilder.append(decimalFormat.format(d / pow));
        stringBuilder.append(" ");
        stringBuilder.append(units[digitGroups]);
        return stringBuilder.toString();
    }

    private void actualizar() {
        progressBar.setVisibility(View.VISIBLE);
        canalesList.clear();

        CanalesAdapter adapter = new CanalesAdapter(canalesList, getApplicationContext());
        adapter.notifyDataSetChanged();
        gridView.invalidateViews();
        loadCanalesList(1);


        if (!userPrefersAdFree) {
            Random rand = new Random();
            int x = rand.nextInt(3);
            if (x == 0) {
                IronSource.loadInterstitial();
                IronSource.setInterstitialListener(new InterstitialListener() {
                    @Override
                    public void onInterstitialAdReady() {
                        IronSource.showInterstitial("Interstitial_refresh");
                    }

                    @Override
                    public void onInterstitialAdLoadFailed(IronSourceError ironSourceError) {
                    }

                    @Override
                    public void onInterstitialAdOpened() {
                    }

                    @Override
                    public void onInterstitialAdClosed() {
                    }

                    @Override
                    public void onInterstitialAdShowSucceeded() {
                    }

                    @Override
                    public void onInterstitialAdShowFailed(IronSourceError ironSourceError) {
                    }

                    @Override
                    public void onInterstitialAdClicked() {
                    }
                });


            }
            if (x == 1) {
                if (mInterstitialRefresh.isAvailable()) {
                    mInterstitialRefresh.showAd();
                }
            }
        }
    }


    private void checkGooglePlay() {
        if (verifyInstallerId(getApplicationContext())) {
            loadUi();
        } else {
            if (BuildConfig.DEBUG) {
                loadUi();
            } else {
                new AlertDialog.Builder(this, R.style.AlertDialogTheme_Center)
                        .setMessage("Descarga la app desde Google Play para usar esta app.")
                        .setNeutralButton("Google Play Access", (dialogInterface, i) -> {
                            LayoutInflater layoutInflaterAndroid = LayoutInflater.from(MainActivity.this);
                            View mView = layoutInflaterAndroid.inflate(R.layout.gp_access, null);
                            AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(MainActivity.this, R.style.AlertDialogTheme_Center);
                            alertDialogBuilderUserInput.setView(mView);

                            final EditText userInputDialogEditText = mView.findViewById(R.id.userInputDialog);
                            alertDialogBuilderUserInput
                                    .setCancelable(false)
                                    .setPositiveButton("Login", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialogBox, int id) {
                                            // ToDo get user input here
                                            String password = userInputDialogEditText.getText().toString();
                                            if (password.equals(BuildConfig.GP_PASSWORD)) {
                                                loadUi();
                                            } else {
                                                Toast.makeText(MainActivity.this, "Incorrect password", Toast.LENGTH_LONG).show();
                                                checkGooglePlay();
                                            }
                                        }
                                    })

                                    .setNegativeButton("Cancel",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialogBox, int id) {
                                                    Toast.makeText(MainActivity.this, "Type password", Toast.LENGTH_LONG).show();
                                                    dialogBox.cancel();
                                                    checkGooglePlay();
                                                }
                                            });

                            AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
                            alertDialogAndroid.show();
                        })
                        .setPositiveButton("Descargar", (dialogInterface, i) -> {
                            final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                            try {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                            } catch (android.content.ActivityNotFoundException anfe) {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                            }
                            finish();
                        })
                        .setNegativeButton("Salir", (dialog, which) -> finish())
                        .setCancelable(false)
                        .show();
            }

        }
    }

    boolean verifyInstallerId(Context context) {
        // A list with valid installers package name
        List<String> validInstallers = new ArrayList<>(Arrays.asList("com.android.vending", "com.google.android.feedback"));

        // The package name of the app that has installed your app
        final String installer = context.getPackageManager().getInstallerPackageName(context.getPackageName());

        // true if your app has been downloaded from Play Store
        return installer != null && validInstallers.contains(installer);
    }

    private void checkVersion() {
        String versionName = BuildConfig.VERSION_NAME;
        String appVersion = remoteConfig.getString("appVersion");
        if (!Objects.equals(appVersion, versionName)) {
               /* Toast.makeText(getApplicationContext(), "Hay una nueva versión disponible",
                        Toast.LENGTH_LONG).show();*/

            if (uiModeManager.getCurrentModeType() != Configuration.UI_MODE_TYPE_TELEVISION) {
                RelativeLayout parentView = findViewById(R.id.relative_main);

                Snackbar snackbar = Snackbar.make(parentView, "Hay una nueva versión disponible", Snackbar.LENGTH_LONG);
                snackbar.setDuration(8000);
                //snackbar.setTextMaxLines(5);
                snackbar.getView().setBackgroundColor(getResources().getColor(R.color.colorBackgroundDark));

                if (uiModeManager.getCurrentModeType() != Configuration.UI_MODE_TYPE_TELEVISION) {
                    snackbar.setAction("Actualizar", view -> {
                        final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                        try {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                        } catch (android.content.ActivityNotFoundException anfe) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                        }
                    });
                }

                TextView snackbarTextView = snackbar.getView().findViewById(com.google.android.material.R.id.snackbar_action);
                snackbarTextView.setTextColor(Color.WHITE);

                snackbar.show();
            } else {
                Toast.makeText(getApplicationContext(), "Hay una nueva versión disponible",
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    private void loadPrefs() {


        channels_view = Prefs.getBoolean("channels_view", true);
        quality = Prefs.getFloat("quality", 720);
        orientation = Prefs.getBoolean("orientation", true);
        zapping = Prefs.getBoolean("zapping", false);

        ads = Prefs.getInt("ads", 0);
        listPosition = Prefs.getInt("listPosition", 0);

        userPrefersAdFree = Prefs.getBoolean("userPrefersAdFree", false);
        premiumPrice = Prefs.getString("premiumPrice");

        listSelect = Prefs.getInt("listSelect", 0);
    }

    private void loadAds() {

        if (!userPrefersAdFree) {
            IronSource.setRewardedVideoListener(this);
            IronSource.setInterstitialListener(this);
            IronSource.setManualLoadRewardedVideo(this);

            IronSource.init(this, BuildConfig.IRON_SOURCE, IronSource.AD_UNIT.INTERSTITIAL, IronSource.AD_UNIT.REWARDED_VIDEO, IronSource.AD_UNIT.BANNER);
            //IronSource.init(this, BuildConfig.IRON_SOURCE);
            if (BuildConfig.DEBUG) {
                IntegrationHelper.validateIntegration(this);
            }

            InitConfiguration configurationOm = new InitConfiguration.Builder()
                    .appKey(BuildConfig.OM_KEY)
                    .preloadAdTypes(OmAds.AD_TYPE.INTERSTITIAL, OmAds.AD_TYPE.REWARDED_VIDEO)
                    .logEnable(false)
                    .build();
            OmAds.init(configurationOm, new InitCallback() {

                // Invoked when the initialization is successful.
                @Override
                public void onSuccess() {
                }

                @Override
                public void onError(Error error) {

                }

            });

            //TestSuite.launch(MainActivity.this, BuildConfig.OM_KEY);


            //MediationTestSuite.launch(MainActivity.this);


            AdSettings.addTestDevice("10ca23b7-245f-4dc6-90be-08927c88e0d2");
            AdSettings.addTestDevice("39c302e0-95e1-4725-9120-e1f0f77d486b");


            if (BuildConfig.DEBUG) {
                List<String> testDeviceIds = Arrays.asList("5544249B384B295EBF3FEABA2FBEDB52");
                RequestConfiguration configuration =
                        new RequestConfiguration.Builder().setTestDeviceIds(testDeviceIds).build();
                MobileAds.setRequestConfiguration(configuration);
            }

        /*IronSource.loadInterstitial();
        IronSource.loadRewardedVideo();*/

            if (uiModeManager.getCurrentModeType() != Configuration.UI_MODE_TYPE_TELEVISION) {
            /*mAdView = findViewById(R.id.adView);
            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.loadAd(adRequest);
            mAdView.setAdListener(new AdListener() {
                @Override
                public void onAdLoaded() {
                    super.onAdLoaded();
                    mAdView.setVisibility(View.VISIBLE);
                    ResponseInfo responseInfo = mAdView.getResponseInfo();
                    Log.d(TAG, responseInfo.toString());

                }

            });*/

                loadBanner();
            }

            //loadRewardedInt();

        /*AdColonyAppOptions appOptions = AdColonyAppOptions.getMoPubAppOptions();
        appOptions.setKeepScreenOn(true);*/


            WortiseSdk.initialize(this, BuildConfig.WORTISE_ID);
            WortiseSdk.wait(() -> {
                ConsentManager.requestOnce(this);
                return Unit.INSTANCE;
            });

            mInterstitialChannel = new com.wortise.ads.interstitial.InterstitialAd(this, BuildConfig.WORTISE_INTERSTITIAL);
            mInterstitialChannel.loadAd();
            mRewardedChannel = new com.wortise.ads.rewarded.RewardedAd(this, BuildConfig.WORTISE_REWARDED);
            mRewardedChannel.loadAd();

            boolean data = Prefs.getBoolean("permissions", false);
            if (!data) {
                BottomSheetDialog dialogPermission = new BottomSheetDialog(MainActivity.this, R.style.TransparentDialog);
                dialogPermission.setContentView(R.layout.custom_dialog_purchase);
                dialogPermission.setCancelable(false);
                dialogPermission.setCanceledOnTouchOutside(false);

                TextView txtTitle = dialogPermission.findViewById(R.id.txtTitle);
                txtTitle.setText("Calidad de los anuncios");
                TextView txtMessage = dialogPermission.findViewById(R.id.txtMessage);
                txtMessage.setText(R.string.wortise_info);
                TextView btnComprar = dialogPermission.findViewById(R.id.btnComprar);
                btnComprar.setText("Continuar");
                btnComprar.setOnClickListener(view -> {
                    Prefs.putBoolean("permissions", true);
                    ActivityCompat.requestPermissions(MainActivity.this, PERMISSIONS, 100);
                    dialogPermission.dismiss();
                });
                TextView btnCancel = dialogPermission.findViewById(R.id.btnCancel);
                btnCancel.setVisibility(View.GONE);

                FrameLayout bottomSheet = (FrameLayout)
                        dialogPermission.findViewById(R.id.design_bottom_sheet);
                BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                //bottomSheetDialog.getBehavior().setPeekHeight(1000);

                if (!this.isFinishing()) {
                    dialogPermission.show();
                }

                dialogPermission.getWindow().setGravity(Gravity.CENTER_HORIZONTAL);

                Rect displayRectangle = new Rect();
                Window window = MainActivity.this.getWindow();

                window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);

                int orientation = getResources().getConfiguration().orientation;
                if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    dialogPermission.getWindow().setLayout((int) (displayRectangle.width() *
                            0.65f), (int) (displayRectangle.height() * 1f));
                } else {
                    dialogPermission.getWindow().setLayout((int) (displayRectangle.width() *
                            0.90f), (int) (displayRectangle.height() * 1f));
                }

                /*AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogTheme_Center);
                builder.setMessage(R.string.wortise_info);
                builder.setCancelable(false);

                builder.setNegativeButton("Continuar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Prefs.putBoolean("permissions", true);
                        ActivityCompat.requestPermissions(MainActivity.this, PERMISSIONS, 100);
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();*/
            }

            //com.facebook.ads.internal.dynamicloading.DynamicLoaderFactory.getDynamicLoader();

            mInterstitialRefresh = new com.wortise.ads.interstitial.InterstitialAd(this, BuildConfig.WORTISE_INTERSTITIAL);
            mInterstitialRefresh.loadAd();
            mInterstitialRefresh.setListener(new com.wortise.ads.interstitial.InterstitialAd.Listener() {
                @Override
                public void onInterstitialFailed(@NonNull com.wortise.ads.interstitial.InterstitialAd interstitialAd, @NonNull AdError adError) {

                }

                @Override
                public void onInterstitialLoaded(@NonNull com.wortise.ads.interstitial.InterstitialAd interstitialAd) {

                }


                @Override
                public void onInterstitialDismissed(@NonNull com.wortise.ads.interstitial.InterstitialAd interstitialAd) {

                }

                @Override
                public void onInterstitialClicked(@NonNull com.wortise.ads.interstitial.InterstitialAd interstitialAd) {

                }

                @Override
                public void onInterstitialShown(@NonNull com.wortise.ads.interstitial.InterstitialAd interstitialAd) {
                    mInterstitialRefresh.loadAd();
                }
            });
        }


    }

    private void loadBanner() {
        if (!userPrefersAdFree) {
            final FrameLayout bannerContainer = findViewById(R.id.bannerContainer);
            banner = IronSource.createBanner(this, ISBannerSize.BANNER);
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.WRAP_CONTENT);
            bannerContainer.addView(banner, 0, layoutParams);
            banner.setBannerListener(new BannerListener() {
                @Override
                public void onBannerAdLoaded() {
                    bannerContainer.setVisibility(View.VISIBLE);
                }

                @Override
                public void onBannerAdLoadFailed(IronSourceError ironSourceError) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            bannerContainer.removeAllViews();
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
            IronSource.loadBanner(banner, "Banner_main");
        }

    }


    private static final String[] PERMISSIONS = {
            Manifest.permission.ACCESS_BACKGROUND_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.POST_NOTIFICATIONS,
            Manifest.permission.READ_PHONE_STATE
    };


    private void loadUi() {

        search = findViewById(R.id.search);

        appName.setSelected(true);
        if (uiModeManager.getCurrentModeType() != Configuration.UI_MODE_TYPE_TELEVISION) {
            appName.setTextSize(18);
        } else {
            appName.setTextSize(22);
        }
        progressBar.setVisibility(View.VISIBLE);
        loadConfigs();


        ImageView icon = search.findViewById(com.google.android.material.R.id.search_mag_icon);
        Drawable whiteIcon = icon.getDrawable();
        whiteIcon.setTint(Color.WHITE); //Whatever color you want it to be
        icon.setImageDrawable(whiteIcon);

        ImageView closeIcon = search.findViewById(com.google.android.material.R.id.search_close_btn);
        Drawable dcloseIcon = closeIcon.getDrawable();
        dcloseIcon.setTint(Color.WHITE); //Whatever color you want it to be
        closeIcon.setImageDrawable(dcloseIcon);

        View searchplate = search.findViewById(R.id.search_plate);
        searchplate.getBackground().setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);

        TextView txtSearch = search.findViewById(R.id.search_src_text);
        txtSearch.setTextColor(Color.WHITE);
        txtSearch.setHintTextColor(Color.WHITE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            txtSearch.setTextCursorDrawable(R.color.white);
        }

        closeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search.setQuery("", false);
                search.clearFocus();
                //Toast.makeText(MainActivity.this, "hola", Toast.LENGTH_SHORT).show();

            }
        });

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.onActionViewCollapsed();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                try {
                    if (TextUtils.isEmpty(newText)) {
                        adapter.filter("");
                        gridView.clearTextFilter();
                    } else {
                        adapter.filter(newText);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return false;
            }
        });
        //loadRadioList();
    }

    private void loadConfigs() {
        remoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(1)
                .build();

        //remoteConfig.setDefaultsAsync(R.xml.remote_config);
        remoteConfig.setConfigSettingsAsync(configSettings);
        remoteConfig.fetchAndActivate()
                .addOnCompleteListener(new OnCompleteListener<Boolean>() {
                    @Override
                    public void onComplete(@NonNull Task<Boolean> task) {
                        if (task.isSuccessful()) {
                            boolean updated = task.getResult();
                            Log.d(TAG, "Config params updated: " + updated);
                            Global.url = remoteConfig.getString(BuildConfig.URL);
                            Global.privacidad = remoteConfig.getString(BuildConfig.PRIVACIDAD);
                            //Global.isOnGooglePlay = Integer.parseInt(remoteConfig.getString(BuildConfig.ISONGOOGLE));


                            loadCanalesList(0);
                            checkVersion();

                            /*if (Global.isOnGooglePlay == 1) {
                                checkGooglePlay();
                            }*/

                        } else {
                            progressBar.setVisibility(View.GONE);

                            Toast.makeText(MainActivity.this, "Error al obtener los datos de la app", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }



   /* private void radioUrl() {
        url = fbRemoteConfig.getString(getUrl());
        loadRadioList();
    }*/


    private void loadCanalesList(int refresh) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Global.url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressBar.setVisibility(View.GONE);

                String decrypted = AES.decryptString(response);

                try {
                    //getting the whole json object from the response
                    JSONObject obj = new JSONObject(decrypted);

                    //we have the array named hero inside the object
                    //so here we are getting that json array
                    JSONArray tvArray = obj.getJSONArray("list");

                    //now looping through all the elements of the json array
                    for (int i = 0; i < tvArray.length(); i++) {
                        //getting the json object of the particular index inside the array
                        JSONObject tvObject = tvArray.getJSONObject(i);

                        tvObject.put("id", i + 1);

                        //creating a hero object and giving them the values from json object
                        Canales canales = new Canales(tvObject.getInt("id"), tvObject.getString("canal").replace("Ã¡", "á").replace("Ã©", "é").replace("Ã\u00AD", "í").replace("Ã³", "ó").replace("Ãº", "ú").replace("Ã±", "ñ"), tvObject.getString("url"), tvObject.getString("img"), tvObject.getString("lic"), tvObject.getString("ua"), tvObject.getString("origin"), tvObject.getString("referer"));

                        //adding the hero to herolist
                        canalesList.add(canales);
                    }

                    //creating custom adapter object
                    //ListViewAdapter adapter = new ListViewAdapter(canalesList, getApplicationContext());
                    adapter = new CanalesAdapter(canalesList, getApplicationContext());


                    //adding the adapter to listview
                    gridView.setAdapter(adapter);

                    // Hacer scroll y enfocar en el elemento deseado

                    if (refresh != 1) {
                        int positionToScroll = listPosition; // Índice del elemento al que deseas hacer scroll y enfocar
                        if (positionToScroll < canalesList.size()) {
                            gridView.post(new Runnable() {
                                @Override
                                public void run() {
                                    int position = Prefs.getInt("listSelect");
                                    if (position == 1) {
                                        gridView.setSelection(positionToScroll);
                                        gridView.requestFocusFromTouch();
                                    }
                                    if (position == 2) {
                                        gridView.performItemClick(
                                                gridView.getChildAt(positionToScroll),
                                                positionToScroll,
                                                gridView.getAdapter().getItemId(positionToScroll)
                                        );
                                    }

                                /*gridView.setSelection(positionToScroll);
                                gridView.requestFocusFromTouch();*/
                                }
                            });
                        }
                    }

                    checkRecyclerview();


// slide-up animation
                    gridView.setVisibility(View.INVISIBLE);

// slide-up animation
                    //Animation slideUp = AnimationUtils.loadAnimation(Canales247Activity.this, R.anim.slide_up);

                    LayoutAnimationController lac = new LayoutAnimationController(AnimationUtils.loadAnimation(MainActivity.this, R.anim.slide_up), 0.2f); //0.5f == time between appearance of listview items.
                    gridView.setLayoutAnimation(lac);

                    if (gridView.getVisibility() == View.INVISIBLE) {
                        gridView.setVisibility(View.VISIBLE);
                        //listView.startAnimation(slideUp);
                        gridView.startLayoutAnimation();
                    }

                    //gridView.requestFocus();


                    if (channels_view) {

                        Resources r = getResources();
                        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 185, r.getDisplayMetrics());

                        gridView.setColumnWidth(Math.round(px));
                        gridView.setNumColumns(GridView.AUTO_FIT);
                        gridView.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
                    }


                    if (uiModeManager.getCurrentModeType() != Configuration.UI_MODE_TYPE_TELEVISION) {
                        search.setVisibility(View.VISIBLE);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //displaying the error in toast if occurrs
                        // Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                        //Toast.makeText(AdultosActivity.this, "Error al cargar la lista de canales, comprueba tu conexión y vuelve a intentarlo.", Toast.LENGTH_SHORT).show();
                        Toast.makeText(MainActivity.this, "Error al cargar la lista, comprueba tu conexión y vuelve a intentarlo.", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);

                    }

                }) {
            @Override
            public Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                //params.put("User-Agent", uaCanales);
                if (uiModeManager.getCurrentModeType() == Configuration.UI_MODE_TYPE_TELEVISION) {
                    params.put("deviceType", "tv");
                } else {
                    params.put("deviceType", "mobile");
                }

                return params;
            }
        };

        //creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //adding the string request to request queue
        requestQueue.add(stringRequest);
    }

    private void checkRecyclerview() {
        List<CanalesFav> selectedItems = getSelectedItems();

        // Insertar los elementos seleccionados en la base de datos
        for (CanalesFav item : selectedItems) {
            itemDao.insertItem(item);
        }

        // Obtener todos los elementos de la base de datos
        canalesFav = itemDao.getAllItems();

        // Configurar el RecyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerviewAdapter = new RecyclerviewAdapter(this, canalesFav, itemDao);
        recyclerView.setAdapter(recyclerviewAdapter);

        // Comparar los elementos y eliminar los que no están en el ListView
        //compareAndDeleteItems();

        // Verificar si itemList está vacía y mostrar u ocultar las vistas correspondientes
        if (canalesFav.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            //txtFavInfo.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            //txtFavInfo.setVisibility(View.GONE);
        }
    }


    @Override
    public void onStart() {

        super.onStart();

    }

    @Override
    public void onStop() {


        super.onStop();
    }

    @Override
    protected void onDestroy() {

        finish();
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        IronSource.onResume(this);
        OmAds.onResume(this);
        if (uiModeManager.getCurrentModeType() != Configuration.UI_MODE_TYPE_TELEVISION) {
            loadBanner();
        }
        billingClient.queryPurchasesAsync(
                QueryPurchasesParams.newBuilder().setProductType(BillingClient.ProductType.INAPP).build(),
                (billingResult, list) -> {
                    if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                        for (Purchase purchase : list) {
                            if (purchase.getPurchaseState() == Purchase.PurchaseState.PURCHASED && !purchase.isAcknowledged()) {
                                verifySubPurchase(purchase);
                            }
                        }
                    }
                }
        );
    }

    protected void onPause() {
        super.onPause();
        OmAds.onPause(this);
        IronSource.onPause(this);
    }




    /*public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


        Canales canales = (Canales) parent.getItemAtPosition(position);
        if (canales == null) {
            return;
        }

        Intent intent = new Intent(this, PlayerActivity.class);
        intent.putExtra("link", canalesList.get(position).getUrl());
        intent.putExtra("name", canalesList.get(position).getCanal());
        intent.putExtra("ua", canalesList.get(position).getUa());
        intent.putExtra("referer", canalesList.get(position).getReferer());
        intent.putExtra("origin", canalesList.get(position).getOrigin());
        intent.putExtra("img", canalesList.get(position).getImg());
        intent.putExtra("license", canalesList.get(position).getLic());


        this.startActivity(intent);


        AdRequest adRequest = new AdRequest.Builder().build();

        switch (ads) {
            case 0:
                loading.setVisibility(View.VISIBLE);
                ads++;
                Prefs.putInt("ads", ads);
                InterstitialAd.load(this, BuildConfig.INTERSTITIAL_MAIN, adRequest,
                        new InterstitialAdLoadCallback() {
                            @Override
                            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                                // The mInterstitialAd reference will be null until
                                // an ad is loaded.

                                ResponseInfo responseInfo = interstitialAd.getResponseInfo();
                                Log.d(TAG, responseInfo.toString());

                                mInterstitialAd = interstitialAd;
                                Log.i(TAG, "onAdLoaded");
                                if (mInterstitialAd != null) {
                                    mInterstitialAd.show(MainActivity.this);
                                } else {
                                    Log.d("TAG", "The interstitial ad wasn't ready yet.");
                                }

                                interstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                                    @Override
                                    public void onAdDismissedFullScreenContent() {
                                        super.onAdDismissedFullScreenContent();
                                        if (!radioManager.isPlaying()) {
                                            radioManager.playOrPause(streamURL);
                                        }
                                    }

                                    @Override
                                    public void onAdImpression() {
                                        super.onAdImpression();
                                        if (radioManager.isPlaying()) {
                                            //radioManager.playOrPause(streamURL);
                                            RadioService.exoPlayer.pause();

                                        }
                                    }


                                });
                            }

                            @Override
                            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                                // Handle the error


                                Log.d(TAG, loadAdError.toString());
                                mInterstitialAd = null;
                                // if (!radioManager.isPlaying()){
                                radioManager.playOrPause(streamURL);
                                //}
                            }
                        });
                break;
            case 1:
                ads++;
                Prefs.putInt("ads", ads);
                radioManager.playOrPause(streamURL);

                break;
            case 2:
                loading.setVisibility(View.VISIBLE);
                ads++;
                Prefs.putInt("ads", ads);
                RewardedAd.load(this, BuildConfig.REWARDED_MAIN, adRequest,
                        new RewardedAdLoadCallback() {
                            @Override
                            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                                // Handle the error.
                                Log.d(TAG, loadAdError.getMessage());
                                mRewardedAd = null;
                                //if (!radioManager.isPlaying()){
                                radioManager.playOrPause(streamURL);
                                //  }

                            }

                            @Override
                            public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                                MainActivity.this.mRewardedAd = rewardedAd;
                                Log.d(TAG, "onAdLoaded");
                                if (mRewardedAd != null) {
                                    mRewardedAd.show(MainActivity.this, null);
                                }
                                ResponseInfo responseInfo = rewardedAd.getResponseInfo();
                                Log.d(TAG, responseInfo.toString());
                                rewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {

                                    @Override
                                    public void onAdDismissedFullScreenContent() {
                                        // Called when ad is dismissed.
                                        // Set the ad reference to null so you don't show the ad a second time.
                                        Log.d(TAG, "Ad dismissed fullscreen content.");
                                        mRewardedAd = null;

                                        if (!radioManager.isPlaying()) {
                                            radioManager.playOrPause(streamURL);
                                        }

                                    }

                                    @Override
                                    public void onAdImpression() {
                                        // Called when an impression is recorded for an ad.
                                        Log.d(TAG, "Ad recorded an impression.");
                                        if (radioManager.isPlaying()) {
                                            RadioService.exoPlayer.pause();
                                        }
                                    }

                                });
                            }
                        });
                break;
            case 3:
                ads = 0;
                Prefs.putInt("ads", ads);
                radioManager.playOrPause(streamURL);
                break;


            default:
                break;
        }

    }*/


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();

        if (uiModeManager.getCurrentModeType() != Configuration.UI_MODE_TYPE_TELEVISION) {
            inflater.inflate(R.menu.menu_main, menu);
        } else {
            inflater.inflate(R.menu.menu_main_tv, menu);
        }

        MenuItem item = menu.findItem(R.id.btnBuscar);
        searchView = (SearchView) item.getActionView();
        searchView.setFocusable(true);
        //item.setVisible(false);

        if (uiModeManager.getCurrentModeType() == Configuration.UI_MODE_TYPE_TELEVISION && Build.VERSION.SDK_INT <= 27) {
            MenuItem itemRefresh = menu.findItem(R.id.btnActualizar);
            ImageView iv = itemRefresh.getActionView().findViewById(R.id.imgActualizar);
            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    actualizar();
                }
            });

            MenuItem itemMas = menu.findItem(R.id.btnMas);
            ImageView ivMas = itemMas.getActionView().findViewById(R.id.imgMas);
            ivMas.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mas();
                }
            });


            MenuItem itemPro = menu.findItem(R.id.btnPro);
            ImageView btnPro = itemPro.getActionView().findViewById(R.id.imgPremium);
            btnPro.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    buyPro();
                }
            });

        }


        searchView.setBackground(getResources().getDrawable(R.drawable.selector_oval));
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchView.setIconified(false);
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.onActionViewCollapsed();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                try {
                    if (TextUtils.isEmpty(newText)) {
                        adapter.filter("");
                        gridView.clearTextFilter();
                    } else {
                        adapter.filter(newText);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return false;
            }
        });

        return true;
    }

    private void buyPro() {
        final BottomSheetDialog bottomCacheSheetDialog = new BottomSheetDialog(MainActivity.this, R.style.TransparentDialog);
        bottomCacheSheetDialog.setContentView(R.layout.custom_dialog_purchase);

        TextView message = bottomCacheSheetDialog.findViewById(R.id.txtMessage);
        TextView cancel = bottomCacheSheetDialog.findViewById(R.id.btnCancel);
        TextView buy = bottomCacheSheetDialog.findViewById(R.id.btnComprar);

        /*if (userPrefersAdFree){
            message.setText("Ya adquiriste el servicio Premium sin publicidad");
        }
        else {
            //<p style="color:white;font-size:18px;">This is demo text</p>
            String sourceString = "Elimina toda la publicidad de la app en un único pago." + "<p><p>Costo: <b>" + Prefs.getString(premiumPrice) + "</b> ";
            message.setText(Html.fromHtml(sourceString));
            //message.setText(Prefs.getString(premiumPrice));
        }*/


        QueryProductDetailsParams queryProductDetailsParams =
                QueryProductDetailsParams.newBuilder()
                        .setProductList(
                                ImmutableList.of(
                                        QueryProductDetailsParams.Product.newBuilder()
                                                .setProductId(BuildConfig.PURCHASE_ID)
                                                .setProductType(BillingClient.ProductType.INAPP)
                                                .build()))
                        .build();

        billingClient.queryProductDetailsAsync(
                queryProductDetailsParams,
                new ProductDetailsResponseListener() {
                    public void onProductDetailsResponse(BillingResult billingResult,
                                                         List<ProductDetails> productDetailsList) {
                        // check billingResult
                        // process returned productDetailsList
                        for (ProductDetails productDetails : productDetailsList) {
                            if (productDetails.getProductId().equals(BuildConfig.PURCHASE_ID)) {
                                if (!productDetailsList.isEmpty()) {

                                    productDetails = productDetailsList.get(0);

                                    if (userPrefersAdFree) {
                                        message.setText("Ya adquiriste el servicio Premium sin publicidad.");
                                        buy.setVisibility(View.GONE);
                                    } else {
                                        //<p style="color:white;font-size:18px;">This is demo text</p>
                                        //String sourceString = "Elimina toda la publicidad de la app en un único pago." + "<p><p>Costo: <b>" + productDetails.getOneTimePurchaseOfferDetails().getFormattedPrice() + "</b> ";
                                        buy.setText("Comprar por " + productDetails.getOneTimePurchaseOfferDetails().getFormattedPrice());
                                        message.setText("Elimina toda la publicidad de la app en un único pago.");
                                        //message.setText(Prefs.getString(premiumPrice));
                                    }

                                    ProductDetails finalProductDetails = productDetails;
                                    buy.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            //Toast.makeText(MainActivity.this, "tv", Toast.LENGTH_SHORT).show();
                                            bottomCacheSheetDialog.cancel();
                                            launchPurchaseFlow(finalProductDetails);
                                        }
                                    });

                                } else {

                                    Log.i("playsresponse", "no response from google play");
                                }
                            }
                        }
                    }
                }
        );


        /*if (userPrefersAdFree) {
            message.setText("Ya adquiriste el servicio sin anuncios.");
            buy.setVisibility(View.GONE);
        }*/

        /*buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //iapConnector.purchase(MainActivity.this, BuildConfig.PURCHASE_ID);
            }
        });*/

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomCacheSheetDialog.cancel();
            }
        });


        FrameLayout bottomSheet = (FrameLayout)
                bottomCacheSheetDialog.findViewById(R.id.design_bottom_sheet);
        BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        //bottomSheetDialog.getBehavior().setPeekHeight(1000);

        bottomCacheSheetDialog.show();

        bottomCacheSheetDialog.getWindow().setGravity(Gravity.CENTER_HORIZONTAL);

        Rect displayRectangle = new Rect();
        Window window = MainActivity.this.getWindow();

        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);

        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            bottomCacheSheetDialog.getWindow().setLayout((int) (displayRectangle.width() *
                    0.50f), (int) (displayRectangle.height() * 1f));
        } else {
            bottomCacheSheetDialog.getWindow().setLayout((int) (displayRectangle.width() *
                    0.90f), (int) (displayRectangle.height() * 1f));
        }
    }

    private void launchPurchaseFlow(ProductDetails finalProductDetails) {
        ImmutableList productDetailsParamsList =
                ImmutableList.of(
                        BillingFlowParams.ProductDetailsParams.newBuilder()
                                // retrieve a value for "productDetails" by calling queryProductDetailsAsync()
                                .setProductDetails(finalProductDetails)
                                // to get an offer token, call ProductDetails.getSubscriptionOfferDetails()
                                // for a list of offers that are available to the user
                                .build()
                );

        BillingFlowParams billingFlowParams = BillingFlowParams.newBuilder()
                .setProductDetailsParamsList(productDetailsParamsList)
                .build();

// Launch the billing flow
        BillingResult billingResult = billingClient.launchBillingFlow(MainActivity.this, billingFlowParams);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.btnActualizar) {
            actualizar();

            return true;
        }

        if (id == R.id.btnPro) {
            buyPro();
            return true;
        }

        if (id == R.id.btnMas) {
            mas();
            return true;

        }


        return super.onOptionsItemSelected(item);
    }


    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }

        // The directory is now empty so delete it
        return dir.delete();
    }

    @Override
    public void onBannerAdLoaded() {

    }

    @Override
    public void onBannerAdLoadFailed(IronSourceError ironSourceError) {

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

    @Override
    public void onInterstitialAdReady() {

    }

    @Override
    public void onInterstitialAdLoadFailed(IronSourceError ironSourceError) {

    }

    @Override
    public void onInterstitialAdOpened() {

    }

    @Override
    public void onInterstitialAdClosed() {

    }

    @Override
    public void onInterstitialAdShowSucceeded() {

    }

    @Override
    public void onInterstitialAdShowFailed(IronSourceError ironSourceError) {

    }

    @Override
    public void onInterstitialAdClicked() {

    }

    @Override
    public void onRewardedVideoAdOpened() {

    }

    @Override
    public void onRewardedVideoAdClosed() {

    }

    @Override
    public void onRewardedVideoAvailabilityChanged(boolean b) {

    }

    @Override
    public void onRewardedVideoAdStarted() {

    }

    @Override
    public void onRewardedVideoAdEnded() {

    }

    @Override
    public void onRewardedVideoAdRewarded(Placement placement) {

    }

    @Override
    public void onRewardedVideoAdShowFailed(IronSourceError ironSourceError) {

    }

    @Override
    public void onRewardedVideoAdClicked(Placement placement) {

    }

    @Override
    public void onRewardedVideoAdReady() {

    }

    @Override
    public void onRewardedVideoAdLoadFailed(IronSourceError ironSourceError) {

    }
}