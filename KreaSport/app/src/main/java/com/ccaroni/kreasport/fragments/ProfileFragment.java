package com.ccaroni.kreasport.fragments;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.auth0.android.Auth0;
import com.auth0.android.authentication.AuthenticationAPIClient;
import com.auth0.android.authentication.AuthenticationException;
import com.auth0.android.callback.BaseCallback;
import com.auth0.android.management.ManagementException;
import com.auth0.android.management.UsersAPIClient;
import com.auth0.android.result.UserProfile;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.ccaroni.kreasport.R;
import com.ccaroni.kreasport.activities.LoginActivity;
import com.ccaroni.kreasport.databinding.FragmentProfileBinding;
import com.ccaroni.kreasport.other.CredentialsManager;

public class ProfileFragment extends Fragment {

    private static final String LOG = ProfileFragment.class.getSimpleName();
    private FragmentProfileBinding binding;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false);
        View view = binding.getRoot();

        getUserData();

        return view;
    }

    private void getUserData() {
        Auth0 auth0 = new Auth0(getString(R.string.auth0_client_id), getString(R.string.auth0_domain));
        auth0.setOIDCConformant(true);

        String idToken = CredentialsManager.getCredentials(getContext()).getIdToken();
        final UsersAPIClient usersClient = new UsersAPIClient(auth0, idToken);
        AuthenticationAPIClient authClient = new AuthenticationAPIClient(auth0);

        String accessToken = CredentialsManager.getCredentials(getContext()).getAccessToken();

        if (accessToken == null) {
            // TODO return to login
        } else {
            authClient.userInfo(accessToken)
                    .start(new BaseCallback<UserProfile, AuthenticationException>() {

                        @Override
                        public void onSuccess(final UserProfile userInfo) {
                            String userId = userInfo.getId();

                            usersClient.getProfile(userId)
                                    .start(new BaseCallback<UserProfile, ManagementException>() {
                                        @Override
                                        public void onSuccess(final UserProfile profile) {
                                            getActivity().runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    setUserData(profile);
                                                }
                                            });
                                        }

                                        @Override
                                        public void onFailure(ManagementException error) {
                                            //show error
                                        }
                                    });
                        }

                        @Override
                        public void onFailure(AuthenticationException error) {
                            //show error
                        }
                    });
        }
    }

    private void setUserData(UserProfile profile) {
        Glide.with(this)
                .load(profile.getPictureURL())
                .apply(
                        new RequestOptions()
                        .placeholder(R.drawable.ic_person_outline_white_24dp)
                        .error(R.drawable.ic_person_outline_white_24dp)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                )
                .into(binding.profileImage);
        Toast.makeText(getContext(), "loaded profile image", Toast.LENGTH_SHORT).show();
    }

    //sign out method
    public void signOut() {
        CredentialsManager.deleteCredentials(getContext());

        Intent intent = new Intent(getContext(), LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        Log.d(LOG, "signed out, cleared activity stack, launching login");
        startActivity(intent);
        getActivity().finish();
    }

}
