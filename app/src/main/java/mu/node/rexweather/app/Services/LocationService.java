package mu.node.rexweather.app.Services;

import android.content.Context;

import com.github.privacystreams.core.UQI;
import com.github.privacystreams.core.exceptions.PSException;
import com.github.privacystreams.core.purposes.Purpose;
import com.github.privacystreams.location.Geolocation;
import com.github.privacystreams.location.LatLon;
import com.github.privacystreams.utils.Globals;

import rx.Observable;
import rx.Subscriber;

/**
 * Implement an Rx-style location service by wrapping the Android LocationManager and providing
 * the location result as an Observable.
 */
public class LocationService {
    private UQI mUqi;
    private Purpose mPurpose;

    public LocationService(Context context) {
        mUqi = new UQI(context);
        mPurpose = Purpose.FEATURE("Examples");
    }

    public Observable<LatLon> getLocation() {
        return Observable.create(new Observable.OnSubscribe<LatLon>() {
            @Override
            public void call(final Subscriber<? super LatLon> subscriber) {
                Globals.LocationConfig.useGoogleService = false;
                try {
                    LatLon latLon = mUqi.getData(Geolocation.asCurrent(Geolocation.LEVEL_CITY), mPurpose).asItem().getValueByField("lat_lon");
                    subscriber.onNext(latLon);
                    subscriber.onCompleted();
                } catch (PSException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}