package ro.softronic.mihai.ro.papamia.Adapters;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import ro.softronic.mihai.ro.papamia.Activities.AddItemActivity;
import ro.softronic.mihai.ro.papamia.Fragments.AddItemFragment;
import ro.softronic.mihai.ro.papamia.Fragments.ItemExtraFragment1;

public class TabsPagerAdapter extends FragmentPagerAdapter {
//    private String tabTitles[] = new String[] { "Extras", "Bauturi" };
    private Bundle mybundle;
    private String itemID;

    public TabsPagerAdapter(FragmentManager fm, Context context ) {
        super(fm);
        AddItemActivity activity = (AddItemActivity)context;
        itemID = activity.getItemID();
       // tabs_no = activity.getTabsNo();
    }

    @Override
    public Fragment getItem(int index) {

        switch (index) {
            case 0:
                // Fragmentul principal
                Bundle args = new Bundle();
                args.putBoolean("has_extras", true);
                AddItemFragment afr = new AddItemFragment();
                afr.setArguments(args);
                return afr;

            case 1:
                // Fragmentul cu sosuri sau paine si ardei
                Bundle args1 = new Bundle();
                args1.putBoolean("has_extras", true);
                ItemExtraFragment1 ief = new ItemExtraFragment1();
                ief.setArguments(args1);
                return ief;
//                //fragmentul cu bauturi
//            case 2:
//                return new ExtraItemFragment2();
        }
        return null;
    }

    @Override
    public int getCount() {
        // get item count - equal to number of tabs
        return 2;
    }

//    @Override
//    public CharSequence getPageTitle(int position) {
//        // Generate title based on item position
//        return tabTitles[position];
//    }

//    @Override
//    public int getItemPosition(Object object) {
//        return POSITION_NONE;
//    }

    public Bundle getMybundle(){
        return mybundle;
    }

    public void  setMybundle(Bundle bundle){
        this.mybundle = bundle;
//        notifyDataSetChanged();
    }
}