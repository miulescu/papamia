package ro.softronic.mihai.ro.papamia.Utils;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;

public class MyRequest extends JsonArrayRequest {
    public MyRequest(String url, Response.Listener<JSONArray> listener,
                       Response.ErrorListener errorListener) {
        super(url, listener, errorListener);
        setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                100, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }
}