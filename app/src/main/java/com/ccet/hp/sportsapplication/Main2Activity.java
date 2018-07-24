package com.ccet.hp.sportsapplication;

import android.support.v7.app.AppCompatActivity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import static java.lang.Integer.parseInt;

public class Main2Activity extends AppCompatActivity {


    public SectionsPagerAdapter mSectionsPagerAdapter;
    public ViewPager mViewPager;

    public String[] event_male;
    public String[] event_female;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_main2);

        mSectionsPagerAdapter = new SectionsPagerAdapter (getSupportFragmentManager ());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById (R.id.container);
        mViewPager.setAdapter (mSectionsPagerAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater ().inflate (R.menu.menu_main2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId ();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected (item);
    }

    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment ();
            Bundle args = new Bundle ();
            args.putInt (ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments (args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate (R.layout.fragment_main2, container, false);
            TextView textView = (TextView) rootView.findViewById (R.id.section_label);
//            textView.setText (getString (R.string.section_format, getArguments ().getInt (ARG_SECTION_NUMBER)));

            String[] male_events = getActivity ().getResources ().getStringArray (R.array.event_list_male);
            String[] female_events = getActivity ().getResources ().getStringArray (R.array.event_list_female);
            String[] inter_year_events = getActivity ().getResources ().getStringArray (R.array.event_list_inter_year);

            ListView event_list_view = (ListView)rootView.findViewById (R.id.fragment_list);

            if(getArguments ().getInt (ARG_SECTION_NUMBER) == 1){
                ArrayAdapter<String> events = new ArrayAdapter<String> (getContext (), R.layout.event_list_final, R.id.fragment_list_text, male_events);
                event_list_view.setAdapter (events);
                textView.setText ("Events for Athletic Meet - Boys");
//                Toast.makeText (getContext (),getArguments ().getInt (ARG_SECTION_NUMBER),Toast.LENGTH_SHORT ).show();
            }else if(getArguments ().getInt (ARG_SECTION_NUMBER) == 2){
                ArrayAdapter<String> events = new ArrayAdapter<String> (getContext (), R.layout.event_list_final, R.id.fragment_list_text, female_events);
                event_list_view.setAdapter (events);
                textView.setText ("Events for Atheletic Meet - Girls");
//                Toast.makeText (getContext (),getArguments ().getInt (ARG_SECTION_NUMBER),Toast.LENGTH_SHORT ).show();
            }
            else if(getArguments ().getInt (ARG_SECTION_NUMBER) == 3){
                ArrayAdapter<String> events = new ArrayAdapter<String> (getContext (), R.layout.event_list_final, R.id.fragment_list_text, inter_year_events);
                event_list_view.setAdapter (events);
                textView.setText ("Events for Inter Year");
//                Toast.makeText (getContext (),getArguments ().getInt (ARG_SECTION_NUMBER),Toast.LENGTH_SHORT ).show();
            }

            return rootView;
        }
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super (fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance (position + 1);
        }

        @Override
        public int getCount() {

            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "SECTION 1";
                case 1:
                    return "SECTION 2";
                case 2:
                    return "SECTION 3";
            }
            return null;
        }
    }
}

