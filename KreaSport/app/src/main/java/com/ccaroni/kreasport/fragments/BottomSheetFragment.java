package com.ccaroni.kreasport.fragments;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import com.ccaroni.kreasport.R;
import com.ccaroni.kreasport.activities.ExploreActivity;
import com.ccaroni.kreasport.databinding.FragmentBottomSheetBinding;
import com.ccaroni.kreasport.map.models.Race;
import com.ccaroni.kreasport.other.PreferenceManager;
import com.ccaroni.kreasport.map.viewmodels.RaceVM;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BottomSheetInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BottomSheetFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BottomSheetFragment extends Fragment {

    private static final String LOG = BottomSheetFragment.class.getSimpleName();


    private FragmentBottomSheetBinding binding;

    private RaceVM raceVM;


    private BottomSheetBehavior mBottomSheetBehaviour;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private BottomSheetInteractionListener mListener;
    private List<Race> raceList;
    private PreferenceManager preferenceManager;

    public BottomSheetFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BottomSheetFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BottomSheetFragment newInstance(String param1, String param2) {
        BottomSheetFragment fragment = new BottomSheetFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        raceVM = ((ExploreActivity) getActivity()).getRaceVM();

        preferenceManager = new PreferenceManager(getContext(), getClass().getSimpleName());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_bottom_sheet, container, false);

        setBindings();

        View view = binding.getRoot();

        mBottomSheetBehaviour = BottomSheetBehavior.from(binding.llBottomSheet);

        return view;
    }

    private void setBindings() {
        binding.setBottomSheetVM(raceVM);

        binding.llBottomSheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onBottomSheetInteraction(null);
                if (mBottomSheetBehaviour.getState() == BottomSheetBehavior.STATE_COLLAPSED)
                    mBottomSheetBehaviour.setState(BottomSheetBehavior.STATE_EXPANDED);
                else if (mBottomSheetBehaviour.getState() == BottomSheetBehavior.STATE_EXPANDED)
                    mBottomSheetBehaviour.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });

        binding.executePendingBindings();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof BottomSheetInteractionListener) {
            mListener = (BottomSheetInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement BottomSheetInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface BottomSheetInteractionListener {
        void onBottomSheetInteraction(Intent requestIntent);

        // TODO use binding for updating bottom sheet
    }

}
