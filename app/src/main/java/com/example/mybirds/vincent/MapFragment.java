package com.example.mybirds.vincent;

import android.animation.LayoutTransition;
import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import com.example.mybirds.R;
import com.example.mybirds.dan.NewPostModel;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.Polyline;
import org.osmdroid.views.overlay.ScaleBarOverlay;
import org.osmdroid.views.overlay.compass.CompassOverlay;
import org.osmdroid.views.overlay.compass.InternalCompassOrientationProvider;
import org.osmdroid.views.overlay.gestures.RotationGestureOverlay;
import org.osmdroid.views.overlay.infowindow.BasicInfoWindow;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class MapFragment extends Fragment implements Observer {

    private MapView map;
    private ConstraintLayout layout;
    private ConstraintLayout rootLayout;
    private LinearLayout linearLayout;
    TextView info;
    ImageView image;
    Polyline line;

    MyLocationNewOverlay mLocationOverlay;
    ArrayList<OverlayItem> overlayItems;
    private ArrayList<GeoPost> geoPostArrayList;
    private boolean isShowingInfo = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        NewPostModel.getInstance().addObserver(this);
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Configuration.getInstance().load(getActivity().getApplicationContext(), PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext()));

        View v = getView();

        map = v.findViewById(R.id.map);
        layout = v.findViewById(R.id.layout);
        rootLayout = v.findViewById(R.id.rootLayout);
        linearLayout = v.findViewById(R.id.itemLayout);
        info = v.findViewById(R.id.coordinate);
        image = v.findViewById(R.id.image);

        configureLayout(rootLayout, layout);

        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setBuiltInZoomControls(false);
        GeoPoint startPoint = new GeoPoint(45.961516, 6.139371);
        IMapController mapController = map.getController();
        mapController.setCenter(startPoint);
        mapController.setZoom(18.0);
        map.setMultiTouchControls(true);
        map.getOverlays().add(new RotationGestureOverlay(map));

        CompassOverlay mCompassOverlay = new CompassOverlay(getActivity(), new InternalCompassOrientationProvider(getActivity()), map);
        mCompassOverlay.enableCompass();
        map.getOverlays().add(mCompassOverlay);

        ScaleBarOverlay myScaleBarOverlay = new ScaleBarOverlay(map);
        myScaleBarOverlay.setAlignRight(true);
        map.getOverlays().add(myScaleBarOverlay);

        mLocationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(getActivity()), map);
        mLocationOverlay.enableMyLocation();
        map.getOverlays().add(mLocationOverlay);
        map.setOnTouchListener((v1, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                hideInfo();
            }

            return false;
        });

        overlayItems = new ArrayList<>();
        updateGeoPoints();
    }

    @Override
    public void onPause() {
        super.onPause();
        map.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        map.onResume();
    }

    public boolean isBlocked() {
        boolean isBlocked = isShowingInfo;
        hideInfo();
        return isBlocked;
    }

    private void hideInfo() {
        if (isShowingInfo) {
            isShowingInfo = false;
            layout.setMaxHeight(rootLayout.getHeight());
            map.getOverlayManager().remove(line);
        }
    }

    private void configureLayout(ViewGroup ...layouts) {
        for (ViewGroup item : layouts) {
            item.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
            item.getLayoutTransition().setDuration(600);
            item.getLayoutTransition().setInterpolator(LayoutTransition.CHANGING, new DecelerateInterpolator());
        }
    }

    private float getDistanceFromPoints(GeoPoint point1, GeoPoint point2) {
        Location location1 = new Location("");
        location1.setLatitude(point1.getLatitude());
        location1.setLongitude(point1.getLongitude());
        Location location2 = new Location("");
        location2.setLatitude(point2.getLatitude());
        location2.setLongitude(point2.getLongitude());
        return location1.distanceTo(location2);
    }

    @Override
    public void update(Observable o, Object arg) {
        updateGeoPoints();
    }

    private void updateGeoPoints() {
        geoPostArrayList = NewPostModel.getInstance().getGeoPosts();
        overlayItems.clear();
        for (int i = 0; i < geoPostArrayList.size(); i++ ) {
            OverlayItem item = new OverlayItem("Item " + i+1, "", geoPostArrayList.get(i).getGeoPoint());
            item.setMarker(ResourcesCompat.getDrawable(getResources(), R.drawable.pin_very_small, null));
            overlayItems.add(item);
        }
        ItemizedOverlayWithFocus<OverlayItem> mOverlay = new ItemizedOverlayWithFocus<>(getActivity().getApplicationContext(), overlayItems,
                new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public boolean onItemSingleTapUp(int index, OverlayItem item) {
                        String text = "Latitude : " + item.getPoint().getLatitude() +
                                "\nLongitude : " + item.getPoint().getLongitude();
                        if (mLocationOverlay.getMyLocation() != null) {
                            float distance = getDistanceFromPoints(new GeoPoint(item.getPoint().getLatitude(), item.getPoint().getLongitude()), mLocationOverlay.getMyLocation());
                            String unité = distance<1000 ? " m" : " Km";
                            distance = distance < 1000 ? distance : distance/1000;
                            text += "\nDistance : " + distance + unité;
                            line = new Polyline(map);
                            line.setOnClickListener((polyline, v, event) -> true);
                            line.setWidth(6f);
                            line.setGeodesic(true);
                            line.setColor(Color.RED);
                            line.addPoint(new GeoPoint(item.getPoint().getLatitude(), item.getPoint().getLongitude()));
                            line.addPoint(mLocationOverlay.getMyLocation());
                            line.setInfoWindow(new BasicInfoWindow(R.layout.bonuspack_bubble, map));
                            map.getOverlayManager().add(line);
                        }
                        info.setText(text);
                        Bitmap bitmap = null;
                        System.out.println(geoPostArrayList.size());
                        for (GeoPost geoPost : geoPostArrayList) {
                            if (geoPost.getGeoPoint().getLatitude() == item.getPoint().getLatitude() &&
                                    geoPost.getGeoPoint().getLongitude() == item.getPoint().getLongitude()) {
                                bitmap = geoPost.getBitmap();
                            }
                        }
                        //image.setImageBitmap(bitmap);
                        linearLayout.measure(0, 0);
                        int height = linearLayout.getMeasuredHeight();
                        layout.setMaxHeight((layout.getHeight() - height));
                        isShowingInfo = true;
                        return true;
                    }

                    @Override
                    public boolean onItemLongPress(int index, OverlayItem item) {
                        return false;
                    }
                });

        mOverlay.setFocusItemsOnTap(false);

        map.getOverlays().add(mOverlay);
    }
}
