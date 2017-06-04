package core;

import enumeration.ComboDeep;
import enumeration.Deep;

public class Config {

    public final static int[] scoreOpen = {0, 1, 100, 1000, 100000, 10000000};

    public final static int[] scoreClose = {0, 1, 50, 500, 1000, 1000, 10000000};

    public final static int size = 15;

    public static Deep searchDeep = Deep.FOUR;

    public static ComboDeep comboDeep = ComboDeep.TEN;

    public static boolean debug = false;

    public static boolean scoreCacheEnable = true;
}
