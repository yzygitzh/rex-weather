package mu.node.rexweather.app.Services;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;

import com.github.privacystreams.core.UQI;
import com.github.privacystreams.core.exceptions.PSException;
import com.github.privacystreams.core.purposes.Purpose;
import com.github.privacystreams.location.Geolocation;
import com.github.privacystreams.location.GeolocationOperators;
import com.github.privacystreams.location.LatLng;
import com.github.privacystreams.utils.Globals;

import rx.Observable;
import rx.Subscriber;

/**
 * Implement an Rx-style location service by wrapping the Android LocationManager and providing
 * the location result as an Observable.
 */
public class LocationService {
    //private final LocationManager mLocationManager;
    private UQI mUqi;
    private Purpose mPurpose;

    // public LocationService(LocationManager locationManager) {
    public LocationService(Context context) {
        //mLocationManager = locationManager;
        mUqi = new UQI(context);
        mPurpose = Purpose.FEATURE("Examples");
    }

    //public Observable<Location> getLocation() {
    public Observable<LatLng> getLocation() {
        //return Observable.create(new Observable.OnSubscribe<Location>() {
        return Observable.create(new Observable.OnSubscribe<LatLng>() {
            @Override
            //public void call(final Subscriber<? super Location> subscriber) {
            public void call(final Subscriber<? super LatLng> subscriber) {

                /*
                final LocationListener locationListener = new LocationListener() {
                    public void onLocationChanged(final Location location) {
                        subscriber.onNext(location);
                        subscriber.onCompleted();

                        Looper.myLooper().quit();
                    }

                    public void onStatusChanged(String provider, int status, Bundle extras) {
                    }

                    public void onProviderEnabled(String provider) {
                    }

                    public void onProviderDisabled(String provider) {
                    }
                };

                final Criteria locationCriteria = new Criteria();
                locationCriteria.setAccuracy(Criteria.ACCURACY_COARSE);
                locationCriteria.setPowerRequirement(Criteria.POWER_LOW);
                final String locationProvider = mLocationManager
                        .getBestProvider(locationCriteria, true);

                Looper.prepare();

                mLocationManager.requestSingleUpdate(locationProvider,
                        locationListener, Looper.myLooper());

                Looper.loop();
                */
                Globals.LocationConfig.useGoogleService = false;
                try {
                    LatLng latLng = mUqi.getData(Geolocation.asCurrent(Geolocation.LEVEL_EXACT), mPurpose).asItem().getValueByField("lat_lng");
                    subscriber.onNext(latLng);
                    subscriber.onCompleted();
                } catch (PSException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}