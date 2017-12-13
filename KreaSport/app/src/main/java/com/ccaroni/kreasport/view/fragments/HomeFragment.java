package com.ccaroni.kreasport.view.fragments;

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
import com.ccaroni.kreasport.databinding.FragmentHomeBinding;
import com.ccaroni.kreasport.view.activities.BaseActivity;

public class HomeFragment extends Fragment {

    private static final String LOG = HomeFragment.class.getSimpleName();

    public static final String DOWNLOAD_REQUEST = "kreasport.fragment_home.request_reason.download";

    public static final String DOWNLOAD_PRIVATE_RACE = "kreasport.frag_request_code.download_private_race";
    public static final String DOWNLOAD_PRIVATE_RACE_KEY = "kreasport.frag_request_code.download_private_race_key";
    public static final String DOWNLOAD_LOCAL_HOST = "kreasport.frag_request_code.download_localhost";


    private FragmentHomeBinding binding;

    EditText etKey;


    private HomeInteractionListener mListener;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment HomeActivity.
     */
    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
                downloadPublicRace(false);
            }
        });
        binding.fragmentHomeTvDownloadPublicRacesLocalServer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downloadPublicRace(true);
            }
        });
        binding.fragmentHomeButtonDownloadFromKey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadPrivateRace();
            }
        });
    }

    private void downloadPrivateRace() {
        Intent downloadIntent = createDownloadIntent(true);
        if (downloadIntent != null) {
            mListener.onHomeInteraction(downloadIntent);
        }
    }

    private void downloadPublicRace(boolean local) {
        Intent downloadIntent = createDownloadIntent(false);
        if (downloadIntent != null) {
            downloadIntent.putExtra(DOWNLOAD_LOCAL_HOST, local);
            mListener.onHomeInteraction(downloadIntent);
        }
    }

    private Intent createDownloadIntent(boolean privateRace) {
        if (mListener != null) {

            Intent intent = new Intent();
            intent.putExtra(BaseActivity.CALLBACK_KEY, DOWNLOAD_REQUEST);

            if (privateRace) {
                if (validatePrivateKey()) {
                    etKey.setError(null);
                    intent.putExtra(DOWNLOAD_PRIVATE_RACE, true);
                    intent.putExtra(DOWNLOAD_PRIVATE_RACE_KEY, etKey.getText().toString());
                } else {
                    Log.d(LOG, "could create private download intent because key was not valid");
                    return intent;
                }
            } else {
                intent.putExtra(DOWNLOAD_PRIVATE_RACE, false);
            }
            return intent;
        } else {
            Log.d(LOG, "could not send download request as mListener was null");
        }
        return null;
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
                    + " must implement HomeInteractionListener");
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
