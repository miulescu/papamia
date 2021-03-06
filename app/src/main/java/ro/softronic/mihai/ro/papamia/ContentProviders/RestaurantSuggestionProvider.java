package ro.softronic.mihai.ro.papamia.ContentProviders;

import android.app.SearchManager;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.provider.BaseColumns;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.List;

import ro.softronic.mihai.ro.papamia.Activities.MainActivity;
import ro.softronic.mihai.ro.papamia.Application.AppController;
import ro.softronic.mihai.ro.papamia.Fragments.RestaurantsFragment;
import ro.softronic.mihai.ro.papamia.POJOs.Restaurant;

public class RestaurantSuggestionProvider extends ContentProvider {
    private static final int SEARCH_SUGGEST = 0;
    private static final int SHORTCUT_REFRESH = 1;
    List<Restaurant> restaurants;

    public static String AUTHORITY =
            "ro.softronic.mihai.ro.papamia.ContentProviders.RestaurantSuggestionProvider";

    private static final UriMatcher sURIMatcher = buildUriMatcher();
    private static UriMatcher buildUriMatcher()
    {
        UriMatcher matcher =
                new UriMatcher(UriMatcher.NO_MATCH);

        matcher.addURI(AUTHORITY,
                SearchManager.SUGGEST_URI_PATH_QUERY,
                SEARCH_SUGGEST);
        matcher.addURI(AUTHORITY,
                SearchManager.SUGGEST_URI_PATH_QUERY +
                        "/*",
                SEARCH_SUGGEST);
        matcher.addURI(AUTHORITY,
                SearchManager.SUGGEST_URI_PATH_SHORTCUT,
                SHORTCUT_REFRESH);
        matcher.addURI(AUTHORITY,
                SearchManager.SUGGEST_URI_PATH_SHORTCUT +
                        "/*",
                SHORTCUT_REFRESH);
        return matcher;
    }
    @Override
    public boolean onCreate() {

        Log.d("***","onCreate called");
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
//        if (restaurants == null || restaurants.isEmpty()) {
            Context context = AppController.getInstance();
            MainActivity act = (MainActivity)AppController.getCurrentActivity();

            RestaurantsFragment fragment_obj = (RestaurantsFragment)act.
                        getSupportFragmentManager().findFragmentByTag("restaurants_frag");
            restaurants = fragment_obj.getAdapter().getList();

//        }
            MatrixCursor cursor = new MatrixCursor(
                    new String[]{
                            BaseColumns._ID,
                            SearchManager.SUGGEST_COLUMN_TEXT_1,
                            SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID
                    }
            );
            if (restaurants != null) {
                String query = uri.getLastPathSegment().toUpperCase();
                int limit = Integer.parseInt(uri.getQueryParameter(SearchManager.SUGGEST_PARAMETER_LIMIT));

                int lenght = restaurants.size();
                for (int i = 0; i < lenght && cursor.getCount() < limit; i++) {
                    Restaurant r = restaurants.get(i);
                    if (r.getName().toUpperCase().contains(query)) {
                        cursor.addRow(new Object[]{i, r.getName(), r.getRestID()});
                    }
                }
            }
            return cursor;
        }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        return null;
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }


}
