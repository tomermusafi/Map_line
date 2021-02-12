# Map_line library
[![](https://jitpack.io/v/tomermusafi/Map_line.svg)](https://jitpack.io/#tomermusafi/Map_line)

 With this libray you can create route and heatmaps in simple way

## Prerequisites
```java
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```

## Dependency
Add this to your module's build.gradle file (make sure the version matches the JitPack badge above):
 ```java
  dependencies {
	        implementation 'com.github.tomermusafi:Map_line:Tag'
	}
```

## Usage
### create Route
```java
  route = new Route(MainActivity.this, googleMap, new Route.Distance_CallBack() {
                @Override
                public void distance(float distance) {
                    main_txt_distance.setText("Distance: "+distance+"m");
                }
            });
```
### create HeatMap
```java
        heat_map = new Heat_Map(generateHeatMapData2(), gMap);
        heat_map.setRadius(50).setColors(new int[]{Color.BLUE,Color.YELLOW, Color.parseColor("#FF0000")}, new float[]{0.2f,0.6f, 1f}).show();
```
### Implement in onMapReady
```java
 @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;
        if(route == null) {
            route = new Route(MainActivity.this, googleMap, new Route.Distance_CallBack() {
                @Override
                public void distance(float distance) {
                    main_txt_distance.setText("Distance: "+distance+"m");
                }
            });
        }
        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                route.addRoute(latLng, R.color.purple_700,R.drawable.ic_baseline_brightness_1_24,R.drawable.walk_person );
            }
        });

        heat_map = new Heat_Map(generateHeatMapData2(), gMap);
        heat_map.setRadius(50).setColors(new int[]{Color.BLUE,Color.YELLOW, Color.parseColor("#FF0000")}, new float[]{0.2f,0.6f, 1f}).show();

    }
```

### ScreenShoot
<img src="https://drive.google.com/uc?export=view&id=1TkjEVocZaA9J-RtG_5S_oPmbj5ERTC5v" alt="drawing" width="300"/>

<img src="https://drive.google.com/uc?export=view&id=14OfeOTYNubJgI3D_nzzZi4gA32knSnp4" alt="drawing" width="300"/>



