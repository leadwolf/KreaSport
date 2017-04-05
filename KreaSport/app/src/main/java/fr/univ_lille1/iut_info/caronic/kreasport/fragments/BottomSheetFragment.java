package fr.univ_lille1.iut_info.caronic.kreasport.fragments;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
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
import fr.univ_lille1.iut_info.caronic.kreasport.databinding.FragmentBottomSheetBinding;
import fr.univ_lille1.iut_info.caronic.kreasport.orienteering.Checkpoint;
import fr.univ_lille1.iut_info.caronic.kreasport.orienteering.Race;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BottomSheetInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BottomSheetFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BottomSheetFragment extends Fragment {

    private Checkpoint boundCheckpoint;
    private Race boundRace;


    private BottomSheetBehavior mBottomSheetBehaviour;

    @BindView(R.id.ll_bottom_sheet)
    LinearLayout llBottomSheet;

    @OnClick(R.id.ll_bottom_sheet)
    public void bottomSheetOnClick() {
        mListener.onBottomSheetInteraction(null);
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

    private BottomSheetInteractionListener mListener;

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

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        FragmentBottomSheetBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_bottom_sheet, container, false);
        boundCheckpoint = new Checkpoint();
        binding.setCheckpoint(boundCheckpoint);
        binding.setRace(boundRace);

        View view = binding.getRoot();
        ButterKnife.bind(this, view);

        mBottomSheetBehaviour = BottomSheetBehavior.from(llBottomSheet);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof BottomSheetInteractionListener) {
            mListener = (BottomSheetInteractionListener) context;
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

    public void updateInfo(Race currentRace, Checkpoint currentCheckpoint) {
        boundCheckpoint = currentCheckpoint;
        boundRace = currentRace;
    }

    public interface BottomSheetInteractionListener {
        void onBottomSheetInteraction(Intent requestIntent);

        // TODO use binding for updating bottom sheet
    }

}
