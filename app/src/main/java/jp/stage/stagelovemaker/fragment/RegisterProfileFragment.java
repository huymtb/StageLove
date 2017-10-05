package jp.stage.stagelovemaker.fragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.Calendar;
import java.util.Date;

import jp.stage.stagelovemaker.MyApplication;
import jp.stage.stagelovemaker.R;
import jp.stage.stagelovemaker.activity.MainActivity;
import jp.stage.stagelovemaker.base.BaseFragment;
import jp.stage.stagelovemaker.base.UserPreferences;
import jp.stage.stagelovemaker.model.SignUpModel;
import jp.stage.stagelovemaker.model.UserTokenModel;
import jp.stage.stagelovemaker.network.IHttpResponse;
import jp.stage.stagelovemaker.network.NetworkManager;
import jp.stage.stagelovemaker.utils.Constants;
import jp.stage.stagelovemaker.utils.Utils;
import jp.stage.stagelovemaker.views.Avatar;
import jp.stage.stagelovemaker.views.FormInputCombobox;
import jp.stage.stagelovemaker.views.FormInputText;
import jp.stage.stagelovemaker.views.LoginActionBar;

import static android.app.Activity.RESULT_OK;
import static jp.stage.stagelovemaker.utils.Constants.REQUEST_IMAGE_CAPTURE;
import static jp.stage.stagelovemaker.utils.Constants.REQUEST_IMAGE_GALLERY;

/**
 * Created by congn on 8/7/2017.
 */

public class RegisterProfileFragment extends BaseFragment implements LoginActionBar.LoginActionBarDelegate,
        FormInputText.FormInputTextDelegate, View.OnClickListener {
    public static final String TAG = "RegisterProfileFragment";

    LoginActionBar actionBar;
    Avatar ivAvatar;
    FormInputText tvFirstName;
    FormInputText tvLastName;
    FormInputCombobox tvBirthday;
    FormInputCombobox tvGender;

    Calendar now;
    String[] genders;
    int iMonth;
    int iDay;
    int iYear;
    int indexGender = -1;
    Boolean bFlagButtonNext = false;
    Uri picUri;
    CropImageView cropImageView;
    RelativeLayout layoutCropView;
    TextView cancelCropView;
    TextView chooseCropView;

    SignUpModel signUpModel;
    String firstName = "";
    String lastName = "";
    String birthday = "";
    String gender = "";
    ImageView rotateImage;
    int iRotation;
    NetworkManager networkManager;
    Gson gson;
    UserTokenModel userTokenModel;
    String urlImage;

    public static RegisterProfileFragment newInstance() {
        Bundle args = new Bundle();
        RegisterProfileFragment fragment = new RegisterProfileFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        networkManager = new NetworkManager(getActivity(), iHttpResponse);
        gson = new Gson();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register_profile, container, false);
        actionBar = (LoginActionBar) view.findViewById(R.id.action_bar);
        ivAvatar = (Avatar) view.findViewById(R.id.iv_avatar);
        tvFirstName = (FormInputText) view.findViewById(R.id.tv_first_name);
        tvLastName = (FormInputText) view.findViewById(R.id.tv_last_name);
        tvBirthday = (FormInputCombobox) view.findViewById(R.id.tv_birthday);
        tvGender = (FormInputCombobox) view.findViewById(R.id.tv_gender);
        cropImageView = (CropImageView) view.findViewById(R.id.cropImageView);
        layoutCropView = (RelativeLayout) view.findViewById(R.id.layout_cropview);
        cancelCropView = (TextView) view.findViewById(R.id.back_txt_cropview);
        chooseCropView = (TextView) view.findViewById(R.id.done_txt_cropview);
        rotateImage = (ImageView) view.findViewById(R.id.rotate_img);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        actionBar.setTitle(getString(R.string.register));
        actionBar.setDelegate(getTag(), this);
        tvFirstName.renderDara(getString(R.string.first_name), false);
        tvLastName.renderDara(getString(R.string.last_name), false);
        tvFirstName.setDelegate(this, Constants.TAG_CONTROL_INPUT_FIRSTNAME);
        tvLastName.setDelegate(this, Constants.TAG_CONTROL_INPUT_LASTNAME);
        tvBirthday.setTitle(getString(R.string.birthday));
        tvGender.setTitle(getString(R.string.gender));

        ivAvatar.setOnClickListener(this);
        tvBirthday.setOnClickListener(this);
        tvGender.setOnClickListener(this);
        cancelCropView.setOnClickListener(this);
        chooseCropView.setOnClickListener(this);
        cropImageView.setGuidelines(CropImageView.Guidelines.ON);
        layoutCropView.setVisibility(View.GONE);
        ((GradientDrawable) rotateImage.getBackground()).setStroke(0,
                ContextCompat.getColor(getContext(), R.color.very_dark_gray));
        ((GradientDrawable) rotateImage.getBackground()).setColor(ContextCompat.getColor(getContext(), R.color.very_dark_gray));
        rotateImage.setOnClickListener(this);

        genders = getResources().getStringArray(R.array.gender_profile);
        now = Calendar.getInstance();
        iMonth = now.get(Calendar.MONTH);
        iDay = now.get(Calendar.DAY_OF_MONTH);
        iYear = now.get(Calendar.YEAR) - Constants.MIN_AGE;
        getSignUpModel();
    }

    @Override
    public void didBack() {
        updateDataSignup();
        getActivity().onBackPressed();
    }

    @Override
    public void didNext() {
        nextAction();
    }

    @Override
    public void valuechange(String tag, String text) {
        switch (tag) {
            case Constants.TAG_CONTROL_INPUT_FIRSTNAME:
                firstName = text;
                break;
            case Constants.TAG_CONTROL_INPUT_LASTNAME:
                lastName = text;
                break;
        }
        validate();
    }

    @Override
    public void didReturn(String tag) {
        nextAction();
    }

    @Override
    public void inputTextFocus(Boolean b, String tag) {

    }

    @Override
    public void onClick(View v) {
        Utils.hideSoftKeyboard(getActivity());
        switch (v.getId()) {
            case R.id.tv_birthday: {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.DAY_OF_MONTH, 31);
                calendar.set(Calendar.MONTH, 11);
                calendar.set(Calendar.YEAR, now.get(Calendar.YEAR) - Constants.MIN_AGE);
                DatePickerDialog dpd;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    dpd = new DatePickerDialog(getActivity(), R.style.style_date_picker_dialog, myDateListener, iYear, iMonth, iDay);
                } else {
                    dpd = new DatePickerDialog(getActivity(), myDateListener, iYear, iMonth, iDay);
                }

                dpd.getDatePicker().setMaxDate(calendar.getTimeInMillis());
                dpd.show();
                break;
            }
            case R.id.tv_gender: {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setItems(genders, (dialog, item) -> {
                    indexGender = item;
                    gender = genders[item];
                    tvGender.setValue(genders[item]);
                    tvGender.setIssuseText("");
                    validate();
                });
                AlertDialog alert = builder.create();
                alert.show();
                break;
            }
            case R.id.iv_avatar: {
                updateDataSignup();
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setItems(getResources().getStringArray(R.array.choose_avatar), (dialog, item) -> {
                    if (item == 0) {
                        dispatchSelectImageIntent();
                    } else {
                        dispatchTakePictureIntent();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
                break;
            }
            case R.id.done_txt_cropview: {
                if (signUpModel != null && cropImageView != null && cropImageView.getCroppedImage() != null) {
                    signUpModel.setAvatar(cropImageView.getCroppedImage());
                    ivAvatar.setAvatar(cropImageView.getCroppedImage());
                }
            }
            case R.id.back_txt_cropview: {
                layoutCropView.setVisibility(View.GONE);
                break;
            }
            case R.id.rotate_img: {
                iRotation = iRotation + 90;
                if (iRotation == 360) {
                    iRotation = 0;
                }
                cropImageView.setRotatedDegrees(iRotation);
                break;
            }
        }
    }

    private void updateDataSignup() {
        if (signUpModel != null) {
            signUpModel.setFirst_name(firstName);
            signUpModel.setLast_name(lastName);
            signUpModel.setBirthday(birthday);
            signUpModel.setGender(indexGender);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (signUpModel != null) {
            birthday = signUpModel.getBirthday();
            firstName = signUpModel.getFirst_name();
            lastName = signUpModel.getLast_name();
            tvFirstName.setTitle(signUpModel.getFirst_name());
            tvLastName.setTitle(signUpModel.getLast_name());
            tvBirthday.setValue(signUpModel.getBirthday());
            if (signUpModel.getGender() != -1) {
                gender = genders[signUpModel.getGender()];
                indexGender = signUpModel.getGender();
                tvGender.setValue(gender);
            }

            if (!TextUtils.isEmpty(signUpModel.getBirthday())) {
                Date date = Utils.parseDateString(signUpModel.getBirthday());
                if (date != null) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(date);
                    iMonth = calendar.get(Calendar.MONTH);
                    iDay = calendar.get(Calendar.DAY_OF_MONTH);
                    iYear = calendar.get(Calendar.YEAR);
                }
            }

            if (signUpModel.getAvatar() != null) {
                ivAvatar.setAvatar(signUpModel.getAvatar());
            } else {
                if (!TextUtils.isEmpty(signUpModel.getLink_avatar())) {
                    ivAvatar.setAvatar(signUpModel.getLink_avatar());
                }
            }
        }
    }

    private boolean validate() {
        Boolean isValid = true;
        String blankField = getString(R.string.field_blank);
        if (bFlagButtonNext) {
            tvFirstName.setIssuseText("");
            tvLastName.setIssuseText("");
            tvBirthday.setIssuseText("");
            tvGender.setIssuseText("");
        }

        if (TextUtils.isEmpty(firstName)) {
            if (bFlagButtonNext) {
                tvFirstName.setIssuseText(blankField);
            }
            isValid = false;
        }

        if (TextUtils.isEmpty(lastName)) {
            if (bFlagButtonNext) {
                tvLastName.setIssuseText(blankField);
            }
            isValid = false;
        }

        if (TextUtils.isEmpty(birthday)) {
            if (bFlagButtonNext) {
                tvBirthday.setIssuseText(blankField);
            }
            isValid = false;
        }

        if (TextUtils.isEmpty(gender)) {
            if (bFlagButtonNext) {
                tvLastName.setIssuseText(blankField);
            }
            isValid = false;
        }

        if (isValid) {
            actionBar.setTextNextColor(Color.WHITE);
        } else {
            actionBar.setTextNextColor(ContextCompat.getColor(getContext(), R.color.color_dim_text));
        }

        if (bFlagButtonNext) {
            bFlagButtonNext = false;
        }
        return isValid;
    }

    protected void cropImage() {
        layoutCropView.setVisibility(View.VISIBLE);
        cropImageView.setImageUriAsync(picUri);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            cropImage();
        } else if (requestCode == REQUEST_IMAGE_GALLERY && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            picUri = data.getData();
            cropImage();

        }
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
            iDay = dayOfMonth;
            iMonth = monthOfYear;
            iYear = year;
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, monthOfYear, dayOfMonth);
            birthday = Utils.formatDate(getActivity(), calendar.getTime());
            tvBirthday.setValue(birthday);
            tvBirthday.setIssuseText("");
            validate();
        }
    };

    public void getSignUpModel() {
        if (getActivity() != null) {
            RegisterFragment fragment = (RegisterFragment) getActivity().getSupportFragmentManager()
                    .findFragmentByTag(RegisterFragment.TAG);
            if (fragment != null) {
                signUpModel = fragment.getSignUp();
            }
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            if (Build.VERSION.SDK_INT >= 24) {
                try {
                    picUri = FileProvider.getUriForFile(getContext(), "jp.stage.stagelovemaker.fileprovider", Utils.getOutputMediaFile(getContext()));
                } catch (Exception e) {

                }
            } else {
                picUri = Uri.fromFile(Utils.getOutputMediaFile(getContext()));
            }
            takePictureIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, picUri);
            takePictureIntent.putExtra("return-data", true);
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private void dispatchSelectImageIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, getString(R.string.select_picture)), REQUEST_IMAGE_GALLERY);
    }

    public void nextAction() {
        bFlagButtonNext = true;
        Utils.hideSoftKeyboard(getActivity());
        if (userTokenModel != null) {
            uploadAvatar();
        } else {
            if (validate()) {
                updateDataSignup();
                networkManager.requestApi(networkManager.signUp(signUpModel), Constants.ID_SIGN_UP);
            }
        }
    }

    private void uploadAvatar() {
        if (signUpModel.getAvatar() != null) {
            networkManager.requestApi(networkManager.uploadAvatar(0, signUpModel.getAvatar()),
                    Constants.ID_UPLOAD_AVATAR);
        } else {
            setMainActivity();
        }
    }

    private void setMainActivity() {
        MyApplication app = Utils.getApplication(getActivity());
        if (app != null) {
            app.setLocation(null);
        }
        Utils.writeBooleanSharedPref(getActivity(), Constants.SHARE_REF_NOTIFICATION, true);
        UserPreferences.setPrefUserAccessToken(userTokenModel.getTokenCode());
        UserPreferences.setPrefUserData(userTokenModel.getUserInfo());
        startNewActivity(MainActivity.class, new Bundle());
        ActivityCompat.finishAffinity(getActivity());
    }

    private void onHandleResponse(final int idRequest) {
        if (getActivity() == null) {
            return;
        }
        getActivity().runOnUiThread(() -> {
            switch (idRequest) {
                case Constants.ID_SIGN_UP:
                    UserPreferences.setPrefUserAccessToken(userTokenModel.getTokenCode());
                    if (signUpModel.getAvatar() != null) {
                        uploadAvatar();
                        break;
                    }
                case Constants.ID_UPLOAD_AVATAR:
                    setMainActivity();
                    break;
            }
        });
    }

    public IHttpResponse iHttpResponse = new IHttpResponse() {
        @Override
        public void onHttpComplete(String response, int idRequest) {
            switch (idRequest) {
                case Constants.ID_SIGN_UP:
                    userTokenModel = gson.fromJson(response, UserTokenModel.class);
                    if (userTokenModel != null) {
                        onHandleResponse(idRequest);
                    }
                    break;

                case Constants.ID_UPLOAD_AVATAR:
                    UserTokenModel avatarModel = gson.fromJson(response, UserTokenModel.class);
                    if (avatarModel != null) {
                        urlImage = avatarModel.getUrl();
                        onHandleResponse(idRequest);
                    }
                    break;
            }
        }

        @Override
        public void onHttpError(String response, int idRequest, int errorCode) {
            switch (idRequest) {
                case Constants.ID_UPLOAD_AVATAR:
                    setMainActivity();
                    break;
            }
        }
    };
}
