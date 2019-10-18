package com.example.goodgame;

import com.google.android.gms.maps.model.LatLng;

public class StopDetailsList {

    private static final LatLng STOP1= new LatLng(-37.798439, 144.964270);

    private static final LatLng STOP3 = new LatLng(-37.801865, 144.963606);

    private static final LatLng STOP4 = new LatLng(-37.805391, 144.963094);

    private static final LatLng STOP5 = new LatLng(-37.814024, 144.963599);

    private static final LatLng STOP6 = new LatLng(-37.815215, 144.974316);

    private static final LatLng STOP7 = new LatLng(-37.807708, 144.962901);

    private static final LatLng STOP8 = new LatLng(-37.810035, 144.964371);

    private static final LatLng STOP9 = new LatLng(-37.805633, 144.973568);

    private static final LatLng STOP10 = new LatLng(-37.812699, 144.965442);

    private static final LatLng STOP11 = new LatLng(-37.815932, 144.966946);

    private static final LatLng STOP12 = new LatLng(-37.801947, 144.957687);

    private static final LatLng STOP13 = new LatLng(-37.817486, 144.966914);

    private static final LatLng STOP14 = new LatLng(-37.799172, 144.957713);

    private static final LatLng STOP15 = new LatLng(-37.799483, 144.955391);

    private static final LatLng STOP16 = new LatLng(-37.821415, 144.969349);

    private static final LatLng STOP17 = new LatLng(-37.824051, 144.970594);

    private static final LatLng STOP18 = new LatLng(-37.818070, 144.975794);

    private static final LatLng STOP19 = new LatLng(-37.805476, 144.977093);
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
            new StopDetails("Elizabeth St & Bourke St",
                    "Stop 5",
                    true,
                    STOP5,
                    5),
            new StopDetails("Spring St & Flinders St",
                    "Stop 6",
                    true,
                    STOP6,
                    6),
            new StopDetails("RMIT University",
                    "Stop 7",
                    false,
                    STOP7,
                    7),
            new StopDetails("Melbourne Central Station",
                    "Stop 8",
                    true,
                    STOP8,
                    8),
            new StopDetails("Melbourne Museum & Nicholson St",
                    "Stop 9",
                    false,
                    STOP9,
                    9),
            new StopDetails("Bourke Street Mall",
                    "Stop 10",
                    true,
                    STOP10,
                    10),
            new StopDetails("Collins St & Swanston St",
                    "Stop 11",
                    true,
                    STOP11,
                    11),
            new StopDetails("Haymarket & Elizabeth St",
                    "Stop 12",
                    false,
                    STOP12,
                    12),
            new StopDetails("Flinders Street Station",
                    "Stop 13",
                    true,
                    STOP13,
                    13),
            new StopDetails("Royal Melbourne Hospital/Royal Pde",
                    "Stop 14",
                    false,
                    STOP14,
                    14),
            new StopDetails("Flemington Road",
                    "Stop 15",
                    false,
                    STOP15,
                    15),
            new StopDetails("Arts Precinct/St Kilda Rd",
                    "Stop 16",
                    false,
                    STOP16,
                    16),
            new StopDetails("Grant St",
                    "Stop 17",
                    false,
                    STOP17,
                    17),
            new StopDetails("William Barak Bridge/Melbourne Park",
                    "Stop 18",
                    false,
                    STOP18,
                    18),
            new StopDetails("Gertrude St/Brunswick St",
                    "Stop 19",
                    false,
                    STOP19,
                    19),

    };
}