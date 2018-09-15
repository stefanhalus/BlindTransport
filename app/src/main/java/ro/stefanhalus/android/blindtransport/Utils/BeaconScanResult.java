package ro.stefanhalus.android.blindtransport.Utils;

import com.kontakt.sdk.android.common.profile.IBeaconDevice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ro.stefanhalus.android.blindtransport.Models.BeaconFoundModel;
import ro.stefanhalus.android.blindtransport.Models.LinesModel;

public class BeaconScanResult {
    public static HashMap found;

    BeaconScanResult() {
//        found = new HashMap<>();
    }

    public void getArrivedLines() {
//        for (IBeaconDevice beacon : found) {

//        }
    }

    public HashMap<String, List<BeaconFoundModel>> beaconList() {
        HashMap<String, List<BeaconFoundModel>> list = new HashMap<>();
        Map<String, List<BeaconFoundModel>> map1 = new HashMap<>();
        List<BeaconFoundModel> arraylist1 = new ArrayList<>();
//        arraylist1.add();
        map1.put("key1", arraylist1);
        return list;
    }
}
