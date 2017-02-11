package net.winnerawan.openmadiun.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import net.winnerawan.openmadiun.adapter.ListAdapter;
import net.winnerawan.openmadiun.R;
import net.winnerawan.openmadiun.adapter.SleepAdapter;
import net.winnerawan.openmadiun.helper.AppInterface;
import net.winnerawan.openmadiun.helper.AppRequest;
import net.winnerawan.openmadiun.helper.ChildAnimationExample;
import net.winnerawan.openmadiun.helper.ItemClickSupport;
import net.winnerawan.openmadiun.response.Response;
import net.winnerawan.openmadiun.helper.SliderLayout;
import net.winnerawan.openmadiun.model.Place;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final String TAG = MainActivity.class.getSimpleName();

    private SliderLayout slider;
    private AppInterface api;
    private AppRequest request;
    private List<Place> places;
    private TextSliderView sliderView;
    private MapView mapView;
    private GoogleMap googleMap;
    private ImageView btnMore;
    private Toolbar toolbar;
    private RecyclerView recyclerViewTour, recyclerViewKost;
    ArrayList arraylist = new ArrayList<HashMap<String, String>>();
    HashMap<String, String> url_maps = new HashMap<String, String>();
    String url_img,title;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initLayout();
        setSupportActionBar(toolbar);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setTitle("");
        }
        mapView.onCreate(savedInstanceState);
        request = new AppRequest();
        api = request.Guide().create(AppInterface.class);

        setSlider();
        mapView.getMapAsync(this);
        getTourList();
        getKostList();

        btnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.image_click));
                Intent i = new Intent(MainActivity.this, MoreActivity.class);
                startActivity(i);
            }
        });
    }

    private void initLayout() {
        slider = (SliderLayout) findViewById(R.id.slider);
        sliderView = new TextSliderView(this);
        mapView = (MapView) findViewById(R.id.map);
        recyclerViewTour = (RecyclerView) findViewById(R.id.tourList);
        recyclerViewTour.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewKost = (RecyclerView) findViewById(R.id.kostList);
        recyclerViewKost.setLayoutManager(new LinearLayoutManager(this));
        this.btnMore = (ImageView) findViewById(R.id.moreAction);
        this.toolbar = (Toolbar) findViewById(R.id.toolbar);
    }

    private void setSlider() {
        Call<Response> response = api.getPlaces();
        response.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                double lat = response.body().getPlace().get(0).getLocation().getLatitude();
                double lng = response.body().getPlace().get(0).getLocation().getLongitude();
                showMap(lat, lng);
                for (int i=1; i<response.body().getPlace().size(); i++) {

                    url_maps.put(response.body().getPlace().get(i).getTitle(),
                            response.body().getPlace().get(i).getImgUrl());
                    title = response.body().getPlace().get(i).getTitle();
                    url_img = response.body().getPlace().get(i).getImgUrl();
                    //url_maps.put(response.body().getPlace().get(2).getTitle(),
                    //        response.body().getPlace().get(2).getImgUrl());
                    Log.d(TAG, "log"+url_maps.toString());
                }

                        sliderView
                                .description(title.toUpperCase())
                                .image(url_img)
                                .setScaleType(BaseSliderView.ScaleType.Fit);
                        sliderView.bundle(new Bundle());
                slider.addSlider(sliderView);
                slider.setPresetTransformer(SliderLayout.Transformer.Default);
                slider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
                slider.setCustomAnimation(new ChildAnimationExample());
                slider.setDuration(4000);

            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {
                Log.e(TAG, t.getMessage());
            }
        });
    }

    private void showMap(double lat, double lng) {
        if (mapView != null) {
            googleMap = mapView.getMap();
            googleMap.getUiSettings().setZoomControlsEnabled(false);
            googleMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE))
                    .anchor(0.0f, 1.0f)
                    .position(new LatLng(55.854049, 13.661331)));
            googleMap.getUiSettings().setMyLocationButtonEnabled(false);
            if (ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            }
            googleMap.setMyLocationEnabled(true);
            googleMap.getUiSettings().setZoomControlsEnabled(true);
            MapsInitializer.initialize(MainActivity.this);
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            //
            builder.include(new LatLng(lat, lng));
            //builder.include(new LatLng(36.448311, 6.62555555));
            LatLngBounds bounds = builder.build();

// begin new code:
            int width = getResources().getDisplayMetrics().widthPixels;
            int height = getResources().getDisplayMetrics().heightPixels;
            int padding = (int) (0); // offset from edges of the map 12% of screen

            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding);
// end of new code
            //
            googleMap.moveCamera(cu);
        }
    }

    @Override
    public void onMapReady(GoogleMap map) {
//DO WHATEVER YOU WANT WITH GOOGLEMAP
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        map.setTrafficEnabled(true);
        map.setPadding(0,0,0,0);
        map.setIndoorEnabled(true);
        map.setBuildingsEnabled(true);
        map.getUiSettings().setZoomControlsEnabled(true);
        if (mapView != null) {
            googleMap = mapView.getMap();
            googleMap.getUiSettings().setZoomControlsEnabled(false);
            googleMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE))
                    .anchor(0.0f, 1.0f)
                    .position(new LatLng(55.854049, 13.661331)));
            googleMap.getUiSettings().setMyLocationButtonEnabled(false);
            if (ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            }
            googleMap.setMyLocationEnabled(true);
            googleMap.getUiSettings().setZoomControlsEnabled(true);
            MapsInitializer.initialize(MainActivity.this);
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            //
            //builder.include(new LatLng(lat, lng));
            builder.include(new LatLng(36.448311, 6.62555555));
            LatLngBounds bounds = builder.build();

// begin new code:
            int width = getResources().getDisplayMetrics().widthPixels;
            int height = getResources().getDisplayMetrics().heightPixels;
            int padding = (int) (0); // offset from edges of the map 12% of screen

            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding);
// end of new code
            //
            googleMap.moveCamera(cu);
        }
    }

    private void getTourList() {
        Call<Response> tours = api.getPlaceTour();
        tours.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                recyclerViewTour.setAdapter(new ListAdapter(response.body().getPlace(), R.layout.list_row, getApplicationContext()));
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {

            }
        });

        ItemClickSupport.addTo(recyclerViewTour).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                AnimationUtils.loadAnimation(getApplicationContext(), R.anim.image_click);
            }
        });
    }

    private void getKostList() {
        Call<Response> kost = api.getKost();
        kost.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                recyclerViewKost.setAdapter(new SleepAdapter(response.body().getPlace(), R.layout.list_row, getApplicationContext()));
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.menu_more) {
            Intent i = new Intent(MainActivity.this, MoreActivity.class);
            startActivity(i);
        }

        //noinspection SimplifiableIfStatement
        //if (id == R.id.action_disclaimer) {
        //    Intent i = new Intent(MainActivity.this, AboutActivity.class);
        //    startActivity(i);
        return super.onOptionsItemSelected(item);
    }
}
