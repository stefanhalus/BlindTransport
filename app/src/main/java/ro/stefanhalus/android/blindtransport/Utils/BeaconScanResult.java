package ro.stefanhalus.android.blindtransport.Utils;

import com.kontakt.sdk.android.common.profile.IBeaconDevice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ro.stefanhalus.android.blindtransport.Models.BeaconFoundModel;
import ro.stefanhalus.android.blindtransport.Models.LinesModel;

public class BeaconScanResult {
    public static ArrayList<ArrayList<IBeaconDevice>> found;

    BeaconScanResult() {
        found = new ArrayList<>();
    }

    public void getArrivedLines() {
//        for (IBeaconDevice beacon : found) {

//        }
    }

    public List<Map<String, List<BeaconFoundModel>>> beaconList() {
        List<Map<String, List<BeaconFoundModel>>> list = new ArrayList<>();
        Map<String, List<BeaconFoundModel>> map1 = new HashMap<>();
        List<BeaconFoundModel> arraylist1 = new ArrayList<>();
//        arraylist1.add();
        map1.put("key1", arraylist1);
        list.add(map1);
        return list;
    }
}
