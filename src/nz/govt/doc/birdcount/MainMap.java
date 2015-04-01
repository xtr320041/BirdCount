package nz.govt.doc.birdcount;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import nz.govt.doc.birdcount.domain.Station;

import org.osmdroid.DefaultResourceProxyImpl;
import org.osmdroid.events.MapListener;
import org.osmdroid.events.ScrollEvent;
import org.osmdroid.events.ZoomEvent;
import org.osmdroid.tileprovider.MapTileProviderBasic;
import org.osmdroid.tileprovider.tilesource.ITileSource;
import org.osmdroid.tileprovider.tilesource.XYTileSource;
import org.osmdroid.util.GeoPoint;

import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.ScaleBarOverlay;
import org.osmdroid.views.overlay.TilesOverlay;

import android.support.v7.app.ActionBarActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public class MainMap extends ActionBarActivity {
	private MapView mapView;
	private TilesOverlay nzTopo;
	private TilesOverlay wellingtonOsm;
	private ScaleBarOverlay scaleBar;
	private Location currentLocation;
	
	
    LocationManager locationManager;
    LocationListener locationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);		
		getSupportActionBar().hide();		

		//setContentView(R.layout.activity_main_map);		
        mapView = new MapView(this, 256);
        mapView.setUseDataConnection(false); 
        setContentView(mapView);

        mapView.setBuiltInZoomControls(true);
        mapView.setMinZoomLevel(6);
        mapView.setMaxZoomLevel(16);
        
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);  
		locationListener = new GPSLocationListener();   
		
		scaleBar = GetScaleBar();
		nzTopo = getTileOverlay("NZTopo", 6, 11, ".png");
		wellingtonOsm = getTileOverlay("Wellington", 12, 16, ".png");


        mapView.getOverlays().add(nzTopo);
        mapView.getOverlays().add(scaleBar);
        mapView.getController().setZoom(6);
        
        
        new Handler(Looper.getMainLooper()).postDelayed(
        	    new Runnable() {
        	        public void run() {
        	            Location lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        	            currentLocation = lastLocation;
        	            if(lastLocation != null){        	
        	            	updateLoc(lastLocation);
        	            }           	            
        	        }
        	    }, 2000
        	);
        
	     mapView.setMapListener(new MapListener() {

			@Override
			public boolean onScroll(ScrollEvent arg0) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean onZoom(ZoomEvent arg0) {
				// TODO Auto-generated method stub
				int zoomLevel = arg0.getZoomLevel();
				if (zoomLevel >= 12) {
					mapView.getOverlayManager().clear();
			        mapView.getOverlays().add(wellingtonOsm);
			        mapView.getOverlays().add(scaleBar);
			        mapView.invalidate();					
				}
				else
				{
					mapView.getOverlayManager().clear();
			        mapView.getOverlays().add(nzTopo);
			        mapView.getOverlays().add(scaleBar);
			        mapView.invalidate();
				}
//			     Toast toast = Toast.makeText(mapView.getContext(), "zoom changed to " + String.valueOf(zoomLevel), Toast.LENGTH_SHORT);
//		         toast.show();
				return false;
			}} 
	     );
	      
       
        
        //mapView.invalidate();

    }

	private ItemizedIconOverlay GetCurrentLocation(){
		DefaultResourceProxyImpl resourceProxy = new DefaultResourceProxyImpl(getApplicationContext());	
		GeoPoint currentGeoPoint = new GeoPoint(currentLocation.getLatitude(), currentLocation.getLongitude());
        OverlayItem myLocationOverlayItem = new OverlayItem("Here", "Current Position", currentGeoPoint);
        Drawable myCurrentLocationMarker = this.getResources().getDrawable(R.drawable.iamhere);
        myLocationOverlayItem.setMarker(myCurrentLocationMarker);
        final ArrayList<OverlayItem> items = new ArrayList<OverlayItem>();
        items.add(myLocationOverlayItem);
        
        ItemizedIconOverlay currentLocationOverlay = new ItemizedIconOverlay<OverlayItem>(items,
                new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
                    public boolean onItemSingleTapUp(final int index, final OverlayItem item) {
                        return true;
                    }
                    public boolean onItemLongPress(final int index, final OverlayItem item) {
                        return true;
                    }
                }, resourceProxy);
	        
        return currentLocationOverlay;
	}
	
	private ItemizedIconOverlay GetStationLayer(){
		final List<Station> stationList = ((BirdCountApp)this.getApplication()).getStationList();		
		DefaultResourceProxyImpl resourceProxy = new DefaultResourceProxyImpl(getApplicationContext());	
        final ArrayList<OverlayItem> items = new ArrayList<OverlayItem>();
        for(Station s:stationList){
    		GeoPoint currentGeoPoint = new GeoPoint(s.getLatitude(), s.getLongitude());
            OverlayItem myLocationOverlayItem = new OverlayItem(s.getShortName(), s.getName(), currentGeoPoint);
            Drawable myCurrentLocationMarker = this.getResources().getDrawable(R.drawable.station);
            myLocationOverlayItem.setMarker(myCurrentLocationMarker);
        	
        	items.add(myLocationOverlayItem);
        
        }
        ItemizedIconOverlay currentLocationOverlay = new ItemizedIconOverlay<OverlayItem>(items,
                new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
                    public boolean onItemSingleTapUp(final int index, final OverlayItem item) {
                        Toast.makeText(getBaseContext(), stationList.get(index).getSpinnerText(), 
                        Toast.LENGTH_SHORT).show();                        
                    	return true;
                    }
                    public boolean onItemLongPress(final int index, final OverlayItem item) {
                        Toast.makeText(getBaseContext(), stationList.get(index).getSpinnerText(), 
                        Toast.LENGTH_SHORT).show();                      	
                        return true;
                    }
                }, resourceProxy);
	        
        return currentLocationOverlay;
	}	
    
    private TilesOverlay getTileOverlay(String mapName, int minZoom, int maxZoom, String fileExt){
        final MapTileProviderBasic tileProvider = new MapTileProviderBasic(getApplicationContext());
        final ITileSource tileSource = new XYTileSource(mapName, null, minZoom, maxZoom, 256, fileExt,
                        new String[]{"http://192.168.1.5/mapcache/tms/1.0.0/ms-base@GoogleMapsCompatible/"});
        //mapView.setTileSource((new XYTileSource("localMapnik", Resource, 0, 18, 256, ".png", 
            //  "http://tile.openstreetmap.org/"))); 
        tileProvider.setTileSource(tileSource);
        final TilesOverlay tilesOverlay = new TilesOverlay(tileProvider, this.getBaseContext());
        return tilesOverlay;
	}
	
	private ScaleBarOverlay GetScaleBar(){
		Paint  mPaint = new Paint();  
		mPaint.setDither(true);  
		mPaint.setColor(0xFFFFFF00);  
		mPaint.setStyle(Paint.Style.FILL);  
		mPaint.setStrokeJoin(Paint.Join.BEVEL);  
		mPaint.setStrokeCap(Paint.Cap.BUTT);  
		mPaint.setStrokeWidth(10); 
        ScaleBarOverlay myScaleBarOverlay = new ScaleBarOverlay(this);
        myScaleBarOverlay.drawLatitudeScale(true);
        myScaleBarOverlay.drawLongitudeScale(false);
        myScaleBarOverlay.setScaleBarOffset(35, 35);
        myScaleBarOverlay.setTextSize(20);
        //myScaleBarOverlay.setCentred(true);
        myScaleBarOverlay.setLineWidth(5);
        myScaleBarOverlay.setMinZoom(8);
        myScaleBarOverlay.setBarPaint(mPaint);	
        return myScaleBarOverlay;
	}
	
	 private void updateLoc(Location loc){
	     GeoPoint locGeoPoint = new GeoPoint(loc.getLatitude(), loc.getLongitude());
//	     String msg = "Center Point Latitude : " + locGeoPoint.getLatitudeE6()/1E6 + " - " + "Longitude : " + locGeoPoint.getLongitudeE6()/1E6;
//	     Toast toast = Toast.makeText(mapView.getContext(), msg, Toast.LENGTH_SHORT);
//       toast.show();	     
	     
//	     mapView.getController().setCenter(locGeoPoint);	          
//	     mapView.invalidate();
	     
         mapView.getController().animateTo(locGeoPoint);
         mapView.invalidate();	     
	    }
	
	 
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_map, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.action_show_current) {
        	ShowCurrentLocation();
        	return true;
        	
        }   
        if (id == R.id.action_show_station) {
        	ShowStation();
        	return true;
        	
        }   
        if (id == R.id.action_start_count) {
            Intent i = new Intent(MainMap.this, BirdCountHeader.class);
            startActivity(i);
        	return true;        	
        } 
        

        return super.onOptionsItemSelected(item);
    }
    
    private void ShowCurrentLocation(){
        if (currentLocation != null)
        {
         GeoPoint point = new GeoPoint((int) (currentLocation.getLatitude() * 1E6),(int) (currentLocation.getLongitude() * 1E6));
         Toast.makeText(getBaseContext(),"Latitude: " + currentLocation.getLatitude() + " Longitude: " + currentLocation.getLongitude(), 
         Toast.LENGTH_LONG).show();
         
         if (mapView.getZoomLevel() <= 11)
        	 mapView.getController().setZoom(11);
         mapView.getOverlays().add(GetCurrentLocation());
         mapView.getController().animateTo(point);
         mapView.invalidate();
        }
    }
    
    private void ShowStation(){

//         GeoPoint point = new GeoPoint((int) (currentLocation.getLatitude() * 1E6),(int) (currentLocation.getLongitude() * 1E6));
//         Toast.makeText(getBaseContext(),"Latitude: " + currentLocation.getLatitude() + " Longitude: " + currentLocation.getLongitude(), 
//         Toast.LENGTH_LONG).show();
         
         if (mapView.getZoomLevel() <= 11)
        	 mapView.getController().setZoom(11);
         mapView.getOverlays().add(GetStationLayer());
         //mapView.getController().animateTo(point);
         mapView.invalidate();

    }
    
    private class GPSLocationListener implements LocationListener 
    {
     public void onLocationChanged(Location location)
       {
        if (location != null)
         {
          GeoPoint point = new GeoPoint((int) (location.getLatitude() * 1E6),(int) (location.getLongitude() * 1E6));

          Toast.makeText(getBaseContext(),"Latitude: " + location.getLatitude() + " Longitude: " + location.getLongitude(), 
          Toast.LENGTH_LONG).show();

          mapView.getController().animateTo(point);
          mapView.getController().setZoom(15);
          mapView.invalidate();
         }

     if (location != null)
        {
          GeoPoint point=null;
          String address = ConvertPointToLocation(point);
          Toast.makeText(getBaseContext(), address, Toast.LENGTH_SHORT).show();

        }
    }

   public String ConvertPointToLocation(GeoPoint point) {   
          String address = "";
          Geocoder geoCoder = new Geocoder(getBaseContext(), Locale.getDefault());
          try {
                List<Address> addresses = geoCoder.getFromLocation(point.getLatitudeE6()  / 1E6, 
                point.getLongitudeE6() / 1E6, 1);

      if (addresses.size() > 0) {
             for (int index = 0; 
               index < addresses.get(0).getMaxAddressLineIndex(); index++)
               address += addresses.get(0).getAddressLine(index) + " ";
           }
       }
   catch (IOException e) {        
               e.printStackTrace();
        }   

return address;
}

@Override
public void onStatusChanged(String provider, int status, Bundle extras) {
	// TODO Auto-generated method stub
	
}

@Override
public void onProviderEnabled(String provider) {
	// TODO Auto-generated method stub
	
}

@Override
public void onProviderDisabled(String provider) {
	// TODO Auto-generated method stub
	
}
   
// private void setOverlayLoc(Location overlayloc){
//	  GeoPoint overlocGeoPoint = new GeoPoint(overlayloc);
//	  //---
//	     //overlayItemArray.clear();
//	     
//	     OverlayItem newMyLocationItem = new OverlayItem(
//	       "My Location", "My Location", overlocGeoPoint);
//	     mapView.getOverlays().add(newMyLocationItem);
//	     //---
//	 }
 
    }
    
}


