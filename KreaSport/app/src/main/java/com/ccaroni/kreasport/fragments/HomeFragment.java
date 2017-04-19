package com.ccaroni.kreasport.fragments;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.ccaroni.kreasport.R;
import com.ccaroni.kreasport.activities.MainActivity;
import com.ccaroni.kreasport.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private static final String LOG = HomeFragment.class.getSimpleName();

    public static final String DOWNLOAD_REQUEST = "kreasport.fragment_home.request_reason.download";

    public static final String DOWNLOAD_PRIVATE_RACE = "kreasport.frag_request_code.download_private_race";
    public static final String DOWNLOAD_PRIVATE_RACE_KEY = "kreasport.frag_request_code.download_private_race_key";


    private FragmentHomeBinding binding;

    EditText etKey;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private HomeInteractionListener mListener;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeActivity.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        View view = binding.getRoot();

        setBindings();

        return view;
    }

    private void setBindings() {
        etKey = binding.fragmentHomeEtKey;

        binding.fragmentHomeTvDownloadPublicRaces.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadRace(false);
            }
        });
        binding.fragmentHomeButtonDownloadFromKey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadRace(true);
            }
        });
    }

    private void downloadRace(boolean privateRace) {
        if (mListener != null) {

            Intent intent = new Intent();
            intent.putExtra(MainActivity.CALLBACK_KEY, DOWNLOAD_REQUEST);

            if (privateRace) {
                if (validatePrivateKey()) {
                    etKey.setError(null);
                    intent.putExtra(DOWNLOAD_PRIVATE_RACE, true);
                    intent.putExtra(DOWNLOAD_PRIVATE_RACE_KEY, etKey.getText().toString());
                } else {
                    return;
                }
            } else if (!privateRace) {
                intent.putExtra(DOWNLOAD_PRIVATE_RACE, false);
            }

            mListener.onHomeInteraction(intent);
        } else {
            Log.d(LOG, "could not send download request as mListener was null");
        }
    }

    private boolean validatePrivateKey() {

        // TODO actual validation for key format

        if (etKey.getText().toString().length() != 6) {
            etKey.setError(getString(R.string.fragment_home_private_key_error));
            return false;
        } else {
            etKey.setError(null);
            return true;
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof HomeInteractionListener) {
            mListener = (HomeInteractionListener) context;
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

    public interface HomeInteractionListener {
        void onHomeInteraction(Intent requestIntent);
    }
}
