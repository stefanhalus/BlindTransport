package ro.stefanhalus.android.blindtransport.Utils;


import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;

import ro.stefanhalus.android.blindtransport.Models.LinesModel;

public class JsonTutorial {

    final String TAG = "JsonParser.java";

    static InputStream is = null;
    static JSONObject jObj = null;
    static String json = "";

    public ArrayList<LinesModel> loadJsonFromUrl() {
        String JsonFavouriteCategories = "";
        ArrayList<LinesModel> MyArraylist = new ArrayList<>();
//        HttpClient httpClient = new DefaultHttpClient();
//        HttpGet httpGet = new HttpGet("http://xxx.xxx.x.xxx:(port)/CheckBoxListView/getFavouriteCategories.php?email=tutorials@codingtrickshub.com");
//        try {
//            HttpResponse httpResponse = httpClient.execute(httpGet);
//            JsonFavouriteCategories = EntityUtils.toString(httpResponse.getEntity());
//            JSONArray jsonArray = new JSONArray(JsonFavouriteCategories);
//
//            for (int i = 0; i < jsonArray.length(); i++) {
//                ArrayList<LinesModel> genres = new ArrayList<>();
//                JSONObject MyJsonObject = jsonArray.getJSONObject(i);
//                genres.setCateogry_id(Integer.parseInt(MyJsonObject.getString("id")));
//                genres.setCategory_Name(MyJsonObject.getString("categoryName"));
//                genres.setSelected(Boolean.parseBoolean(MyJsonObject.getString("selected")));
//                MyArraylist.add(genres);
//                if (MyJsonObject.getString("selected").equals("true")) {
//                    selectedCategories.add(MyJsonObject.getString("id"));
//                }
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        return MyArraylist;
    }
}
