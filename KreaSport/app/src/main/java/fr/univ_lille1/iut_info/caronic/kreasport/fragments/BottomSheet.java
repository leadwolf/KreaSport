package fr.univ_lille1.iut_info.caronic.kreasport.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fr.univ_lille1.iut_info.caronic.kreasport.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BottomSheet#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BottomSheet extends Fragment {


    private BottomSheetBehavior mBottomSheetBehaviour;

    @BindView(R.id.ll_bottom_sheet)
    LinearLayout llBottomSheet;

    @OnClick(R.id.ll_bottom_sheet)
    public void bottomSheetOnClick() {
        if (mBottomSheetBehaviour.getState() == BottomSheetBehavior.STATE_COLLAPSED)
            mBottomSheetBehaviour.setState(BottomSheetBehavior.STATE_EXPANDED);
        else if (mBottomSheetBehaviour.getState() == BottomSheetBehavior.STATE_EXPANDED)
            mBottomSheetBehaviour.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public BottomSheet() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BottomSheet.
     */
    // TODO: Rename and change types and number of parameters
    public static BottomSheet newInstance(String param1, String param2) {
        BottomSheet fragment = new BottomSheet();
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

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bottom_sheet, container, false);
        ButterKnife.bind(this, view);

        mBottomSheetBehaviour = BottomSheetBehavior.from(llBottomSheet);

        return view;
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
