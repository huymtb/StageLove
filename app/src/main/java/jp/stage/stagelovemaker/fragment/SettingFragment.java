package jp.stage.stagelovemaker.fragment;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.SwitchCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.edmodo.rangebar.RangeBar;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;

import jp.stage.stagelovemaker.MyApplication;
import jp.stage.stagelovemaker.R;
import jp.stage.stagelovemaker.activity.LoginActivity;
import jp.stage.stagelovemaker.activity.SplashActivity;
import jp.stage.stagelovemaker.base.BaseFragment;
import jp.stage.stagelovemaker.base.EventDistributor;
import jp.stage.stagelovemaker.base.UserPreferences;
import jp.stage.stagelovemaker.dialog.ContactUsDialog;
import jp.stage.stagelovemaker.dialog.DeleteAccountDialog;
import jp.stage.stagelovemaker.dialog.QuestionDialog;
import jp.stage.stagelovemaker.model.DiscoverModel;
import jp.stage.stagelovemaker.model.ErrorModel;
import jp.stage.stagelovemaker.model.SettingModel;
import jp.stage.stagelovemaker.network.IHttpResponse;
import jp.stage.stagelovemaker.network.NetworkManager;
import jp.stage.stagelovemaker.utils.Constants;
import jp.stage.stagelovemaker.utils.Utils;
import jp.stage.stagelovemaker.views.TitleBar;

/**
 * Created by congn on 7/14/2017.
 */

public class SettingFragment extends BaseFragment implements TitleBar.TitleBarCallback,
        SeekBar.OnSeekBarChangeListener, RangeBar.OnRangeBarChangeListener, View.OnClickListener {
    public static final String TAG = "SettingFragment";

    TitleBar titleBar;

    TextView tvShowMe;
    TextView tvMen;
    TextView tvWomen;
    RelativeLayout layoutShowMe;

    TextView tvMaxDistance;
    TextView tvDistance;
    SeekBar seekBarDistance;
    RelativeLayout layoutDistance;

    TextView tvAgeRange;
    TextView tvAge;
    RangeBar rangeAge;
    RelativeLayout layoutAge;

    TextView tvShowMeOnTinder;
    RelativeLayout layoutShowMeOnTinder;

    TextView tvNotification;
    TextView tvNewMatches;
    TextView tvMessages;
    TextView tvMessageLikes;
    TextView tvSuperLikes;
    RelativeLayout layoutNotification;

    TextView tvShowDistanceIn;
    TextView tvChooseDistanceUnit;
    android.widget.Button btKm;
    android.widget.Button btMi;
    RelativeLayout layoutShowDistance;

    TextView tvContactUs;
    TextView tvHelpSupport;
    RelativeLayout layoutHelpSupport;

    TextView tvShareApp;
    RelativeLayout layoutShareApp;

    TextView tvLegal;
    TextView tvLicenses;
    TextView tvPrivacy;
    TextView tvTerms;
    RelativeLayout layoutLegal;

    TextView tvLogout;
    RelativeLayout layoutLogout;

    SwitchCompat switchMan, switchWoman;
    SwitchCompat switchShowOnTinder;
    SwitchCompat switchNotifyNewMatches;
    SwitchCompat switchNotifyMessage;
    SwitchCompat switchNotifyMessageLike;
    SwitchCompat switchNotifySuperLikes;

    TextView tvDeleteAccount;
    RelativeLayout layoutDeleteAccount;

    String valueDistance;
    String unitDistance;
    int fromAge;
    int toAge;
    int radius;

    Boolean isMile;
    SettingFragmentCallback callback;
    SettingModel settingModel;
    DiscoverModel discoverModel;
    NetworkManager networkManager;
    Gson gson;


    public static SettingFragment newInstance(SettingModel settingModel, DiscoverModel discoverModel) {
        Bundle args = new Bundle();
        args.putParcelable(Constants.KEY_DATA, settingModel);
        args.putParcelable(Constants.KEY_DATA_TWO, discoverModel);
        SettingFragment fragment = new SettingFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        networkManager = new NetworkManager(getActivity(), iHttpResponse);
        gson = new Gson();
    }

    public IHttpResponse iHttpResponse = new IHttpResponse() {
        @Override
        public void onHttpComplete(String response, int idRequest) {
            switch (idRequest) {
                case Constants.ID_UPDATE_SETTING:
                    if (callback != null) {
                        callback.onSettingChanged();
                        EventDistributor.getInstance().sendMySettingUpdateBroadcast();
                        getActivity().onBackPressed();
                    }
                    break;
                case Constants.ID_DELETE_ACCOUNT:
                    Toast.makeText(getContext(), getString(R.string.account_is_deleted), Toast.LENGTH_LONG).show();
                    UserPreferences.clear();
                    Bundle bundle = new Bundle();
                    startNewActivity(SplashActivity.class, bundle);
                    ActivityCompat.finishAffinity(getActivity());
                    break;
            }
        }

        @Override
        public void onHttpError(String response, int idRequest, int errorCode) {
            if(!TextUtils.isEmpty(response)){
                ErrorModel errorModel = gson.fromJson(response, ErrorModel.class);
                if (errorModel != null && !TextUtils.isEmpty(errorModel.getErrorMsg())) {
                    Toast.makeText(getActivity(), errorModel.getErrorMsg(), Toast.LENGTH_LONG).show();
                }
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        titleBar = (TitleBar) view.findViewById(R.id.titleBar);
        tvShowMe = (TextView) view.findViewById(R.id.tv_show_me);
        tvMen = (TextView) view.findViewById(R.id.tv_men);
        tvWomen = (TextView) view.findViewById(R.id.tv_women);
        layoutShowMe = (RelativeLayout) view.findViewById(R.id.layout_show_me);

        tvMaxDistance = (TextView) view.findViewById(R.id.tv_maximum_distance);
        tvDistance = (TextView) view.findViewById(R.id.tv_distance);
        seekBarDistance = (SeekBar) view.findViewById(R.id.seekbar_distance);
        layoutDistance = (RelativeLayout) view.findViewById(R.id.layout_distance);

        tvAgeRange = (TextView) view.findViewById(R.id.tv_age_range);
        tvAge = (TextView) view.findViewById(R.id.tv_age);
        rangeAge = (RangeBar) view.findViewById(R.id.rangebar);
        layoutAge = (RelativeLayout) view.findViewById(R.id.layout_age);

        tvShowMeOnTinder = (TextView) view.findViewById(R.id.tv_show_me_on_Tinder);
        layoutShowMeOnTinder = (RelativeLayout) view.findViewById(R.id.layout_show_me_on_tinder);

        tvNotification = (TextView) view.findViewById(R.id.tv_notifications);
        tvNewMatches = (TextView) view.findViewById(R.id.tv_new_matches);
        tvMessages = (TextView) view.findViewById(R.id.tv_messages);
        tvMessageLikes = (TextView) view.findViewById(R.id.tv_message_likes);
        tvSuperLikes = (TextView) view.findViewById(R.id.tv_super_likes);
        layoutNotification = (RelativeLayout) view.findViewById(R.id.layout_notification);

        tvShowDistanceIn = (TextView) view.findViewById(R.id.tv_show_distance_in);
        tvChooseDistanceUnit = (TextView) view.findViewById(R.id.tv_choose_distance_unit);
        btKm = (android.widget.Button) view.findViewById(R.id.bt_km);
        btMi = (android.widget.Button) view.findViewById(R.id.bt_mi);
        layoutShowDistance = (RelativeLayout) view.findViewById(R.id.layout_show_distance);

        tvContactUs = (TextView) view.findViewById(R.id.tv_contact_us);
        tvHelpSupport = (TextView) view.findViewById(R.id.tv_help_support);
        layoutHelpSupport = (RelativeLayout) view.findViewById(R.id.layout_help_support);

        tvShareApp = (TextView) view.findViewById(R.id.tv_share_app);
        layoutShareApp = (RelativeLayout) view.findViewById(R.id.layout_share_app);

        tvLegal = (TextView) view.findViewById(R.id.tv_legal);
        tvLicenses = (TextView) view.findViewById(R.id.tv_licenses);
        tvPrivacy = (TextView) view.findViewById(R.id.tv_privacy);
        tvTerms = (TextView) view.findViewById(R.id.tv_tos);
        layoutLegal = (RelativeLayout) view.findViewById(R.id.layout_legal);

        tvLogout = (TextView) view.findViewById(R.id.tv_logout);
        layoutLogout = (RelativeLayout) view.findViewById(R.id.layout_logout);
        tvDeleteAccount = (TextView) view.findViewById(R.id.tv_delete);
        layoutDeleteAccount = (RelativeLayout) view.findViewById(R.id.layout_delete);

        switchMan = (SwitchCompat) view.findViewById(R.id.switch_men);
        switchWoman = (SwitchCompat) view.findViewById(R.id.switch_women);
        switchShowOnTinder = (SwitchCompat) view.findViewById(R.id.switch_show_me_on_tinder);
        switchNotifyNewMatches = (SwitchCompat) view.findViewById(R.id.switch_new_matches);
        switchNotifyMessage = (SwitchCompat) view.findViewById(R.id.switch_messages);
        switchNotifyMessageLike = (SwitchCompat) view.findViewById(R.id.switch_message_likes);
        switchNotifySuperLikes = (SwitchCompat) view.findViewById(R.id.switch_super_likes);
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable("settingModel", settingModel);
        outState.putParcelable("discoverModel", discoverModel);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        titleBar.setTitle(getString(R.string.settings_no_all_cap));
        titleBar.enableBackButton();
        titleBar.setTitleRight(getString(R.string.done));
        titleBar.setCallback(this);

        ((GradientDrawable) layoutShowMe.getBackground()).setStroke(1, ContextCompat.getColor(getContext(), R.color.gray80));
        ((GradientDrawable) layoutShowMe.getBackground()).setColor(Color.WHITE);

        ((GradientDrawable) layoutDistance.getBackground()).setStroke(1, ContextCompat.getColor(getContext(), R.color.gray80));
        ((GradientDrawable) layoutDistance.getBackground()).setColor(Color.WHITE);
        seekBarDistance.setOnSeekBarChangeListener(this);

        ((GradientDrawable) layoutAge.getBackground()).setStroke(1, ContextCompat.getColor(getContext(), R.color.gray80));
        ((GradientDrawable) layoutAge.getBackground()).setColor(Color.WHITE);
        rangeAge.setOnRangeBarChangeListener(this);

        tvShowMeOnTinder.setTypeface(Utils.getProximaSemiBold(getContext()));
        ((GradientDrawable) layoutShowMeOnTinder.getBackground()).setStroke(1, ContextCompat.getColor(getContext(), R.color.gray80));
        ((GradientDrawable) layoutShowMeOnTinder.getBackground()).setColor(Color.WHITE);

        ((GradientDrawable) layoutNotification.getBackground()).setStroke(1, ContextCompat.getColor(getContext(), R.color.gray80));
        ((GradientDrawable) layoutNotification.getBackground()).setColor(Color.WHITE);

        ((GradientDrawable) layoutShowDistance.getBackground()).setStroke(1, ContextCompat.getColor(getContext(), R.color.gray80));
        ((GradientDrawable) layoutShowDistance.getBackground()).setColor(Color.WHITE);

        chooseDistanceUnit(btKm, btMi);

        ((GradientDrawable) layoutHelpSupport.getBackground()).setStroke(1, ContextCompat.getColor(getContext(), R.color.gray80));
        ((GradientDrawable) layoutHelpSupport.getBackground()).setColor(Color.WHITE);

        ((GradientDrawable) layoutShareApp.getBackground()).setStroke(1, ContextCompat.getColor(getContext(), R.color.gray80));
        ((GradientDrawable) layoutShareApp.getBackground()).setColor(Color.WHITE);

        ((GradientDrawable) layoutLegal.getBackground()).setStroke(1, ContextCompat.getColor(getContext(), R.color.gray80));
        ((GradientDrawable) layoutLegal.getBackground()).setColor(Color.WHITE);

        ((GradientDrawable) layoutLogout.getBackground()).setStroke(1, ContextCompat.getColor(getContext(), R.color.gray80));
        ((GradientDrawable) layoutLogout.getBackground()).setColor(Color.WHITE);

        ((GradientDrawable) layoutDeleteAccount.getBackground()).setStroke(1, ContextCompat.getColor(getContext(), R.color.gray80));
        ((GradientDrawable) layoutDeleteAccount.getBackground()).setColor(Color.WHITE);

        if (savedInstanceState != null) {
            settingModel = savedInstanceState.getParcelable("settingModel");
            discoverModel = savedInstanceState.getParcelable("discoverModel");
        } else {
            settingModel = getArguments().getParcelable(Constants.KEY_DATA);
            discoverModel = getArguments().getParcelable(Constants.KEY_DATA_TWO);
        }

        unitDistance = "";
        rangeAge.setTickCount(Constants.MAX_AGE - Constants.MIN_AGE + 1);

        btMi.setOnClickListener(this);
        btKm.setOnClickListener(this);
        layoutHelpSupport.setOnClickListener(this);

        switchMan.setOnClickListener(v -> {
            if (!switchMan.isChecked()) {
                if (!switchWoman.isChecked()) {
                    switchWoman.setChecked(true);
                }
            }
        });
        switchWoman.setOnClickListener(v -> {
            if (!switchWoman.isChecked()) {
                if (!switchMan.isChecked()) {
                    switchMan.setChecked(true);
                }
            }
        });

        layoutLogout.setOnClickListener(this);
        layoutDeleteAccount.setOnClickListener(this);

        loadInfoSetting();
    }

    private void loadInfoSetting() {
        String distance = settingModel.getDistanceUnit();
        if (!TextUtils.isEmpty(distance) && distance.equals("mile")) {
            unitDistance = "mile";
            seekBarDistance.setMax(62);
            tvChooseDistanceUnit.setText(getString(R.string.mi));
            chooseDistanceUnit(btMi, btKm);
            isMile = true;
        } else {
            unitDistance = "km";
            seekBarDistance.setMax(100);
            tvChooseDistanceUnit.setText(getString(R.string.km));
            chooseDistanceUnit(btKm, btMi);
            isMile = false;
        }

        switchNotifyNewMatches.setChecked(settingModel.getNotifyNewMatches());
        switchNotifyMessage.setChecked(settingModel.getNotifyMessages());
        switchNotifyMessageLike.setChecked(settingModel.getNotifyMessageLikes());
        switchNotifySuperLikes.setChecked(settingModel.getNotifySuperLikes());
        switchShowOnTinder.setChecked(settingModel.getShowMeOnStageMaker());

        switch (discoverModel.getFilterGender()) {
            case 0:
                switchMan.setChecked(false);
                switchWoman.setChecked(true);
                break;
            case 1:
                switchMan.setChecked(true);
                switchWoman.setChecked(false);
                break;
            case 2:
                switchMan.setChecked(true);
                switchWoman.setChecked(true);
                break;
        }
    }

    @Override
    public void onResume() {
        updateData();
        super.onResume();
    }

    private void updateData() {
        if (discoverModel != null) {
            fromAge = discoverModel.getFromAge();
            if (fromAge < Constants.MIN_AGE && fromAge > Constants.MAX_AGE) {
                fromAge = Constants.MIN_AGE;
            }

            toAge = discoverModel.getToAge();
            if (toAge < Constants.MIN_AGE && toAge > Constants.MAX_AGE) {
                toAge = Constants.MAX_AGE;
            }
            int indexLeft = fromAge - Constants.MIN_AGE;
            int indexRight = toAge - Constants.MIN_AGE;
            if (indexLeft >= 0 && indexRight > indexLeft && indexRight < (Constants.MAX_AGE - Constants.MIN_AGE + 1)
                    && indexLeft < (Constants.MAX_AGE - Constants.MIN_AGE + 1)) {
                rangeAge.setThumbIndices(indexLeft, indexRight);
            }

            radius = discoverModel.getFilterDistance();
            if (radius >= 0) {
                if (isMile) {
                    int progress = (int) (radius * 0.62);
                    seekBarDistance.setProgress(progress);
                } else {
                    seekBarDistance.setProgress(radius);
                }
            } else {
                if (isMile) {
                    seekBarDistance.setProgress(62);
                } else {
                    seekBarDistance.setProgress(100);
                }
            }
        }
    }

    @Override
    public void onTitleBarClicked() {

    }

    @Override
    public void onRightButtonClicked() {
        if (switchMan.isChecked() && switchWoman.isChecked()) {
            discoverModel.setFilterGender(2);
        } else if (switchMan.isChecked() && !switchWoman.isChecked()) {
            discoverModel.setFilterGender(1);
        } else if (!switchMan.isChecked() && switchWoman.isChecked()) {
            discoverModel.setFilterGender(0);
        }

        discoverModel.setFromAge(fromAge);
        discoverModel.setToAge(toAge);
        discoverModel.setFilterDistance(radius);

        settingModel.setDistanceUnit(unitDistance);
        settingModel.setNotifyMessages(!switchNotifyMessage.isChecked());
        settingModel.setNotifyNewMatches(!switchNotifyNewMatches.isChecked());
        settingModel.setNotifyMessageLikes(!switchNotifyMessageLike.isChecked());
        settingModel.setNotifySuperLikes(!switchNotifySuperLikes.isChecked());
        settingModel.setShowMeOnStageMaker(!switchShowOnTinder.isChecked());

        updateSettings();
    }

    private void updateSettings() {
        int id = UserPreferences.getCurrentUserId();
        networkManager.requestApi(networkManager.updateSettings(id, settingModel, discoverModel), Constants.ID_UPDATE_SETTING);
    }

    @Override
    public void onBackButtonClicked() {
        getActivity().onBackPressed();
    }

    @Override
    public void onMiddleButtonClicked() {

    }

    private void calculateDistanceByUnit() {
        int progress;
        if (isMile) {
            progress = (int) (radius * 0.625);
            valueDistance = getString(R.string.mi);
        } else {
            progress = radius;
            valueDistance = getString(R.string.km);
        }
        tvDistance.setText(progress + valueDistance);
        seekBarDistance.setProgress((int) progress);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (seekBar == seekBarDistance && isMile != null) {
            if (isMile) {
                radius = (int) (progress * 1.6);
                valueDistance = getString(R.string.mi);
            } else {
                radius = progress;
                valueDistance = getString(R.string.km);
            }
            tvDistance.setText(progress + valueDistance);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onIndexChangeListener(RangeBar rangeBar, int i, int i1) {
        if (discoverModel != null) {
            fromAge = i + Constants.MIN_AGE;
            toAge = i1 + Constants.MIN_AGE;
        }
        tvAge.setText((fromAge) + "-" + (toAge));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_km:
                chooseDistanceUnit(btKm, btMi);
                isMile = false;
                tvChooseDistanceUnit.setText(getString(R.string.km));
                calculateDistanceByUnit();
                seekBarDistance.setMax(100);
                unitDistance = "km";
                break;
            case R.id.bt_mi:
                chooseDistanceUnit(btMi, btKm);
                isMile = true;
                tvChooseDistanceUnit.setText(getString(R.string.mi));
                calculateDistanceByUnit();
                unitDistance = "mile";
                seekBarDistance.setMax(60);
                break;
            case R.id.layout_logout:
                QuestionDialog dialog = new QuestionDialog(getActivity(), Utils.capitalize(getString(R.string.log_out)),
                        getString(R.string.question_logout));
                dialog.setColorTitle(Color.RED);
                dialog.setTitleButtonOK(getString(R.string.logout).toUpperCase());
                dialog.setDelegate(questionLogout, "");
                dialog.show();
                break;
            case R.id.layout_help_support:
                ContactUsDialog contactUsDialog = ContactUsDialog.newInstance();
                add(contactUsDialog, "", true, false, R.id.flContainer);
                break;
            case R.id.layout_delete:
                DeleteAccountDialog deleteAccountDialog = DeleteAccountDialog.newInstance();
                deleteAccountDialog.setCallback(callbackDelete);
                add(deleteAccountDialog, DeleteAccountDialog.TAG, true, false, R.id.flContainer);
        }
    }


    private void chooseDistanceUnit(android.widget.Button button1, android.widget.Button button2) {
        ((GradientDrawable) button1.getBackground()).setColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
        ((GradientDrawable) button1.getBackground()).setStroke(0, Color.TRANSPARENT);
        button1.setTextColor(Color.WHITE);

        ((GradientDrawable) button2.getBackground()).setColor(Color.TRANSPARENT);
        ((GradientDrawable) button2.getBackground()).setStroke(0, Color.TRANSPARENT);
        button2.setTextColor(ContextCompat.getColor(getContext(), R.color.gray37));
    }

    public QuestionDialog.QuestionRequestDialogDelegate questionLogout = (bAllow, type) -> {
        if (bAllow) {
            Toast.makeText(getContext(), getString(R.string.you_are_loggout), Toast.LENGTH_LONG).show();
            MyApplication app = Utils.getApplication(getActivity());
            int id = UserPreferences.getCurrentUserId();
            FirebaseMessaging.getInstance().unsubscribeFromTopic("/topics/topic_" + id);
            if (app != null) {
                UserPreferences.clear();
                app.setLocation(null);
            }
            startNewActivity(LoginActivity.class, null);
            ActivityCompat.finishAffinity(getActivity());
        }
    };

    public void setCallback(SettingFragmentCallback callback) {
        this.callback = callback;
    }

    public interface SettingFragmentCallback {
        void onSettingChanged();
    }

    private DeleteAccountDialog.Callback callbackDelete = new DeleteAccountDialog.Callback() {
        @Override
        public void onAccountDeleted(String username, String password) {
            int id = UserPreferences.getCurrentUserId();
            networkManager.requestApi(networkManager.deleteUser(id, username, password), Constants.ID_DELETE_ACCOUNT);
        }
    };
}
