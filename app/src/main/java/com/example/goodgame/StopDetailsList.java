package com.example.goodgame;

import com.google.android.gms.maps.model.LatLng;

public class StopDetailsList {

    private static final LatLng STOP1= new LatLng(-37.798439, 144.964270);

    private static final LatLng STOP3 = new LatLng(-37.801865, 144.963606);

    private static final LatLng STOP4 = new LatLng(-37.805391, 144.963094);

    private static final LatLng STOP5 = new LatLng(-37.805391, 144.963094);

    private static final LatLng STOP7 = new LatLng(-37.807708, 144.962901);

    private static final LatLng STOP8 = new LatLng(-37.810035, 144.964371);

    private static final LatLng STOP10 = new LatLng(-37.812699, 144.965442);

    private static final LatLng STOP11 = new LatLng(-37.815932, 144.966946);

    private static final LatLng STOP13 = new LatLng(-37.817486, 144.966914);

    public StopDetailsList() {
    }

    public static StopDetails[] STOPS = {
            new StopDetails("Melbourne University",
                    "Stop 1",
                    false,
                    STOP1,
                    1),
            new StopDetails("Lincoln Square",
                    "Stop 3",
                    false,
                    STOP3,
                    2),
            new StopDetails("Queensberry St & Swanston St",
                    "Stop 4",
                    false,
                    STOP4,
                    4),
            new StopDetails("RMIT University",
                    "Stop 7",
                    false,
                    STOP7,
                    7),
            new StopDetails("Melbourne Central Station",
                    "Stop 8",
                    false,
                    STOP8,
                    8),
            new StopDetails("Bourke Street Mall",
                    "Stop 10",
                    false,
                    STOP10,
                    10),
            new StopDetails("Collins St & Swanston St",
                    "Stop 11",
                    false,
                    STOP11,
                    11),
            new StopDetails("Flinders Street Station",
                    "Stop 13",
                    false,
                    STOP13,
                    13)
    };
}
