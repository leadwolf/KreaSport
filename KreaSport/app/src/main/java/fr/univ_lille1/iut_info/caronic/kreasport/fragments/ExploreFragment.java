package fr.univ_lille1.iut_info.caronic.kreasport.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import org.osmdroid.util.GeoPoint;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.univ_lille1.iut_info.caronic.kreasport.R;
import fr.univ_lille1.iut_info.caronic.kreasport.maps.CustomMapView;
import fr.univ_lille1.iut_info.caronic.kreasport.maps.MapOptions;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ExploreFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExploreFragment extends Fragment {

    private static final String LOG = ExploreFragment.class.getSimpleName();


    @BindView(R.id.fragment_explore_frame_layout)
    FrameLayout frameLayout;


    private static final String KEY_DEFAULT_POINT = "kreasport.fragment_explore.keys.default_point";
    private static final String KEY_DEFAULT_ZOOM = "kreasport.fragment_explore.keys.default_zoom";
    private static final String KEY_MAP_OPTIONS = "kreasport.fragment_explore.keys.map_options";
    private static final String KEY_MAP_VIEW = "kreasport.fragment_explore.keys.map_view";

    private GeoPoint defaultPoint;
    private int defaultZoom;
    private MapOptions mMapOptions;
    private CustomMapView mMapView;

    private OnFragmentInteractionListener mListener;

    public ExploreFragment() {
        // Required empty public constructor
    }

    public static ExploreFragment newInstance(GeoPoint defaultPoint, int defaultZoom, MapOptions mMapOptions) {
        ExploreFragment fragment = new ExploreFragment();

        Bundle args = new Bundle();
        args.putSerializable(KEY_DEFAULT_POINT, defaultPoint);
        args.putInt(KEY_DEFAULT_ZOOM, defaultZoom);
        args.putSerializable(KEY_MAP_OPTIONS, mMapOptions);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            defaultPoint = (GeoPoint) getArguments().getSerializable(KEY_DEFAULT_POINT);
            defaultZoom = getArguments().getInt(KEY_DEFAULT_ZOOM);
            mMapOptions = (MapOptions) getArguments().getSerializable(KEY_MAP_OPTIONS);
            mMapView = (CustomMapView) getArguments().getSerializable(KEY_MAP_VIEW);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_explore, container, false);
        ButterKnife.bind(this, rootView);

        if (mMapView == null) {
            Log.d(LOG, "recreating MapView");
            mMapView = new CustomMapView(getActivity(), mMapOptions);
        } else {
            Log.d(LOG ,"using restored MapView");
        }

        frameLayout.addView(mMapView);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {
            Log.d(LOG, "onActivityCreated");
            int lol = savedInstanceState.getInt("lol");
            Log.d(LOG, "lol: " + lol);
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Log.d(LOG, "onSaveInstanceState");
        outState.putInt("lol", 1);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
