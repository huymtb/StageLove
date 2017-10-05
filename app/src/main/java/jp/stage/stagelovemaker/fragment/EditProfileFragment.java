package jp.stage.stagelovemaker.fragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.github.siyamed.shapeimageview.RoundedImageView;
import com.google.gson.Gson;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;

import jp.stage.stagelovemaker.R;
import jp.stage.stagelovemaker.base.BaseFragment;
import jp.stage.stagelovemaker.base.EventDistributor;
import jp.stage.stagelovemaker.base.UserPreferences;
import jp.stage.stagelovemaker.model.AvatarModel;
import jp.stage.stagelovemaker.model.ErrorModel;
import jp.stage.stagelovemaker.model.InstagramUserModel;
import jp.stage.stagelovemaker.model.UserInfoModel;
import jp.stage.stagelovemaker.network.IHttpResponse;
import jp.stage.stagelovemaker.network.NetworkManager;
import jp.stage.stagelovemaker.utils.Constants;
import jp.stage.stagelovemaker.utils.Utils;
import jp.stage.stagelovemaker.views.FormInputCombobox;
import jp.stage.stagelovemaker.views.FormInputText;
import jp.stage.stagelovemaker.views.OnSingleClickListener;
import jp.stage.stagelovemaker.views.TitleBar;
import jp.stage.stagelovemaker.views.TitleTextView;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import jp.wasabeef.glide.transformations.CropSquareTransformation;
import jp.wasabeef.glide.transformations.CropTransformation;
import jp.wasabeef.glide.transformations.GrayscaleTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import static android.app.Activity.RESULT_OK;
import static jp.stage.stagelovemaker.utils.Constants.REQUEST_IMAGE_CAPTURE;
import static jp.stage.stagelovemaker.utils.Constants.REQUEST_IMAGE_GALLERY;

/**
 * Created by congn on 7/12/2017.
 */

public class EditProfileFragment extends BaseFragment implements TitleBar.TitleBarCallback,
        View.OnClickListener, InstagramFragment.InstagramFragmentDelegate, FormInputText.FormInputTextDelegate {
    public static final String TAG = "EditProfileFragment";
    TitleBar titleBar;
    FormInputText tvFirstName;
    FormInputText tvLastName;
    FormInputCombobox tvBirthday;
    FormInputCombobox tvGender;
    FormInputText tvCurrentWork;
    FormInputText tvSchool;
    FormInputText tvAbout;
    TitleTextView tvInstagram;

    ArrayList<RoundedImageView> avatarImageView = new ArrayList<>();
    ArrayList<ImageView> removeImageView = new ArrayList<>();
    ArrayList<ImageView> addImageView = new ArrayList<>();

    int indexGender = -1;
    int indexChange;
    Uri picUri;
    CropImageView cropImageView;
    RelativeLayout layoutCropView;
    TextView cancelCropView;
    TextView chooseCropView;
    ImageView rotateImage;
    int iRotation;

    String[] genders;
    String genderValue;
    Calendar now;
    int iMonth;
    int iDay;
    int iYear;
    String firstName = "";
    String lastName = "";
    String birthday = "";
    String gender = "";
    NetworkManager networkManager;
    Gson gson;
    UserInfoModel userInfoModel;

    public static EditProfileFragment newInstance(UserInfoModel userInfoModel) {
        Bundle args = new Bundle();
        args.putParcelable(Constants.KEY_DATA, userInfoModel);
        EditProfileFragment fragment = new EditProfileFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public IHttpResponse iHttpResponse = new IHttpResponse() {
        @Override
        public void onHttpComplete(String response, int idRequest) {
            switch (idRequest) {
                case Constants.ID_UPLOAD_AVATAR:
                    EventDistributor.getInstance().sendMyProfileUpdateBroadcast();
                    break;
                case Constants.ID_DELETE_AVATAR:
                    removeAvatar(avatarImageView.get(indexChange), addImageView.get(indexChange), removeImageView.get(indexChange));
                    EventDistributor.getInstance().sendMyProfileUpdateBroadcast();
                    break;
                case Constants.ID_UPDATE_USER:
                    EventDistributor.getInstance().sendMyProfileUpdateBroadcast();
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            getActivity().onBackPressed();
                        }
                    }, 200);
                    break;
            }
        }

        @Override
        public void onHttpError(String response, int idRequest, int errorCode) {
            switch (idRequest) {
                case Constants.ID_UPLOAD_AVATAR:
                    ErrorModel errorModel = gson.fromJson(response, ErrorModel.class);
                    if (errorModel != null && !TextUtils.isEmpty(errorModel.getErrorMsg())) {
                        Toast.makeText(getActivity(), errorModel.getErrorMsg(), Toast.LENGTH_SHORT).show();
                    }
                    break;
                case Constants.ID_DELETE_AVATAR:
                    ErrorModel errorDelete = gson.fromJson(response, ErrorModel.class);
                    if (errorDelete != null && !TextUtils.isEmpty(errorDelete.getErrorMsg())) {
                        Toast.makeText(getActivity(), errorDelete.getErrorMsg(), Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    };

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        networkManager = new NetworkManager(getActivity(), iHttpResponse);
        gson = new Gson();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userInfoModel = getArguments().getParcelable(Constants.KEY_DATA);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        titleBar = (TitleBar) view.findViewById(R.id.titleBar);
        avatarImageView.add((RoundedImageView) view.findViewById(R.id.imageView_1));
        avatarImageView.add((RoundedImageView) view.findViewById(R.id.imageView_2));
        avatarImageView.add((RoundedImageView) view.findViewById(R.id.imageView_3));
        avatarImageView.add((RoundedImageView) view.findViewById(R.id.imageView_4));
        avatarImageView.add((RoundedImageView) view.findViewById(R.id.imageView_5));
        avatarImageView.add((RoundedImageView) view.findViewById(R.id.imageView_6));

        addImageView.add((ImageView) view.findViewById(R.id.add_imageView_1));
        addImageView.add((ImageView) view.findViewById(R.id.add_imageView_2));
        addImageView.add((ImageView) view.findViewById(R.id.add_imageView_3));
        addImageView.add((ImageView) view.findViewById(R.id.add_imageView_4));
        addImageView.add((ImageView) view.findViewById(R.id.add_imageView_5));
        addImageView.add((ImageView) view.findViewById(R.id.add_imageView_6));

        removeImageView.add((ImageView) view.findViewById(R.id.remove_imageView_1));
        removeImageView.add((ImageView) view.findViewById(R.id.remove_imageView_2));
        removeImageView.add((ImageView) view.findViewById(R.id.remove_imageView_3));
        removeImageView.add((ImageView) view.findViewById(R.id.remove_imageView_4));
        removeImageView.add((ImageView) view.findViewById(R.id.remove_imageView_5));
        removeImageView.add((ImageView) view.findViewById(R.id.remove_imageView_6));

        cropImageView = (CropImageView) view.findViewById(R.id.cropImageView);
        layoutCropView = (RelativeLayout) view.findViewById(R.id.layout_cropview);
        cancelCropView = (TextView) view.findViewById(R.id.back_txt_cropview);
        chooseCropView = (TextView) view.findViewById(R.id.done_txt_cropview);
        rotateImage = (ImageView) view.findViewById(R.id.rotate_img);

        tvBirthday = (FormInputCombobox) view.findViewById(R.id.tv_birthday);
        tvFirstName = (FormInputText) view.findViewById(R.id.tv_first_name);
        tvLastName = (FormInputText) view.findViewById(R.id.tv_last_name);
        tvAbout = (FormInputText) view.findViewById(R.id.tv_about);
        tvCurrentWork = (FormInputText) view.findViewById(R.id.tv_current_work);
        tvSchool = (FormInputText) view.findViewById(R.id.tv_school);
        tvGender = (FormInputCombobox) view.findViewById(R.id.tv_gender);
        tvInstagram = (TitleTextView) view.findViewById(R.id.tv_instagram);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        titleBar.setTitle(getString(R.string.edit_profile));
        titleBar.enableBackButton();
        titleBar.setTitleRight(getString(R.string.done));
        titleBar.setCallback(this);

        tvFirstName.renderDara(getString(R.string.first_name), false);
        tvFirstName.setTitleInputText(getString(R.string.first_name));
        tvLastName.renderDara(getString(R.string.last_name), false);
        tvLastName.setTitleInputText(getString(R.string.last_name));
        tvAbout.renderDara(getString(R.string.about_me), false);
        tvAbout.setTitleInputText(getString(R.string.about_me));
        tvCurrentWork.renderDara(getString(R.string.current_work), false);
        tvCurrentWork.setTitleInputText(getString(R.string.current_work));
        tvSchool.renderDara(getString(R.string.school), false);
        tvSchool.setTitleInputText(getString(R.string.school));
        tvFirstName.setDelegate(this, Constants.TAG_CONTROL_INPUT_FIRSTNAME);
        tvLastName.setDelegate(this, Constants.TAG_CONTROL_INPUT_LASTNAME);
        tvAbout.setDelegate(this, Constants.TAG_CONTROL_INPUT_ABOUT_ME);
        tvCurrentWork.setDelegate(this, Constants.TAG_CONTROL_INPUT_WORK);
        tvSchool.setDelegate(this, Constants.TAG_CONTROL_INPUT_SCHOOL);
        tvBirthday.setTitle(getString(R.string.birthday));
        tvGender.setTitle(getString(R.string.gender));
        tvInstagram.setTitle(getString(R.string.instagram));

        cancelCropView.setOnClickListener(mySingleListener);
        chooseCropView.setOnClickListener(mySingleListener);
        cropImageView.setGuidelines(CropImageView.Guidelines.ON);
        tvBirthday.setOnClickListener(this);
        tvGender.setOnClickListener(this);
        tvAbout.setMaxLength(500);
        tvInstagram.setOnClickListener(this);
        layoutCropView.setVisibility(View.GONE);
        ((GradientDrawable) rotateImage.getBackground()).setStroke(0,
                ContextCompat.getColor(getContext(), R.color.very_dark_gray));
        ((GradientDrawable) rotateImage.getBackground()).setColor(ContextCompat.getColor(getContext(), R.color.very_dark_gray));
        rotateImage.setOnClickListener(this);
        for (int i = 0; i < avatarImageView.size(); i++) {
            avatarImageView.get(i).setOnClickListener(mySingleListener);
            addImageView.get(i).setOnClickListener(mySingleListener);
            removeImageView.get(i).setOnClickListener(this);
        }

        genders = getResources().getStringArray(R.array.gender_profile);
        now = Calendar.getInstance();
        iMonth = now.get(Calendar.MONTH);
        iDay = now.get(Calendar.DAY_OF_MONTH);
        iYear = now.get(Calendar.YEAR) - Constants.MIN_AGE;
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

    @Override
    public void onResume() {
        super.onResume();
        if (userInfoModel != null) {
            updateAppearance();
        }
    }

    private void updateAppearance() {
        tvFirstName.setTitle(userInfoModel.getFirstName());
        tvLastName.setTitle(userInfoModel.getLastName());

        if (!TextUtils.isEmpty(userInfoModel.getMeta().getAboutMe())) {
            tvAbout.setTitle(userInfoModel.getMeta().getAboutMe());
        }

        Date birthday = Utils.formatLocalDateString(userInfoModel.getMeta().getBirthday());
        Calendar now = Calendar.getInstance();
        if (birthday != null) {
            now.setTime(birthday);
            iMonth = now.get(Calendar.MONTH);
            iDay = now.get(Calendar.DAY_OF_MONTH);
            iYear = now.get(Calendar.YEAR);
            tvBirthday.setValue(Utils.formatDateLocal(getContext(), birthday));
        } else {
            iMonth = now.get(Calendar.MONTH);
            iDay = now.get(Calendar.DAY_OF_MONTH);
            iYear = now.get(Calendar.YEAR) - Constants.MIN_AGE;
        }

        int indexGender = Utils.getIndexGender(userInfoModel.getGender());
        genderValue = genders[indexGender];
        tvGender.setValue(genderValue);

        tvCurrentWork.setTitle(userInfoModel.getMeta().getCurrentWork());
        tvSchool.setTitle(userInfoModel.getMeta().getSchool());

        if (userInfoModel.getAvatars() != null && !userInfoModel.getAvatars().isEmpty()) {
            ArrayList<AvatarModel> avatars = new ArrayList<>(userInfoModel.getAvatars());
            for (int i = 0; i < avatars.size(); i++) {
                AvatarModel avatarModel = avatars.get(i);
                if (avatarModel != null && !TextUtils.isEmpty(avatarModel.getUrl())) {
                    updateAvatar(avatarImageView.get(avatarModel.getNumberIndex()),
                            addImageView.get(avatarModel.getNumberIndex()),
                            removeImageView.get(avatarModel.getNumberIndex()), avatarModel);
                } else {
                    removeAvatar(avatarImageView.get(i), addImageView.get(i), removeImageView.get(i));
                }
            }
        }
    }

    @Override
    public void onTitleBarClicked() {

    }

    @Override
    public void onRightButtonClicked() {
        Utils.hideSoftKeyboard(getActivity());
        if (validate()) {
            requestUpdateProfile();
        }
    }

    @Override
    public void onBackButtonClicked() {
        getActivity().onBackPressed();
    }

    @Override
    public void onMiddleButtonClicked() {

    }

    @Override
    public void onClick(View v) {
        Utils.hideSoftKeyboard(getActivity());
        switch (v.getId()) {
            case R.id.tv_birthday:
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.DAY_OF_MONTH, 31);
                calendar.set(Calendar.MONTH, 11);
                calendar.set(Calendar.YEAR, now.get(Calendar.YEAR) - Constants.MIN_AGE);
                DatePickerDialog dpd;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    dpd = new DatePickerDialog(getActivity(),
                            R.style.style_date_picker_dialog, myDateListener, iYear, iMonth, iDay);
                } else {
                    dpd = new DatePickerDialog(getActivity(), myDateListener, iYear, iMonth, iDay);
                }

                dpd.getDatePicker().setMaxDate(calendar.getTimeInMillis());
                dpd.show();
                break;
            case R.id.tv_gender:
                Utils.hideSoftKeyboard(getActivity());
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setItems(genders, (dialog, item) -> {
                    indexGender = item;
                    gender = genders[item];
                    tvGender.setValue(genders[item]);
                    Boolean isMale = item == 1;
                    userInfoModel.setGender(isMale);
                    tvGender.setIssuseText("");
                });
                AlertDialog alert = builder.create();
                alert.show();
                break;
            case R.id.rotate_img: {
                iRotation = iRotation + 90;
                if (iRotation == 360) {
                    iRotation = 0;
                }
                cropImageView.setRotatedDegrees(iRotation);
                break;
            }
            case R.id.remove_imageView_1: {
                indexChange = 0;
                deleteImage();
                break;
            }
            case R.id.remove_imageView_2: {
                indexChange = 1;
                deleteImage();
                break;
            }
            case R.id.remove_imageView_3: {
                indexChange = 2;
                deleteImage();
                break;
            }
            case R.id.remove_imageView_4: {
                indexChange = 3;
                deleteImage();
                break;
            }
            case R.id.remove_imageView_5: {
                indexChange = 4;
                deleteImage();
                break;
            }
            case R.id.remove_imageView_6: {
                indexChange = 5;
                deleteImage();
                break;
            }
            case R.id.tv_instagram:
                InstagramFragment fragment = InstagramFragment.createInstance();
                fragment.setDelegate(this);
                add(fragment, InstagramFragment.TAG, true, false);
                break;
        }
    }

    private OnSingleClickListener mySingleListener = new OnSingleClickListener() {
        @Override
        public void onSingleClick(View v) {
            Utils.hideSoftKeyboard(getActivity());
            switch (v.getId()) {
                case R.id.imageView_1:
                case R.id.add_imageView_1: {
                    indexChange = 0;
                    chooseAvatar();
                    break;
                }
                case R.id.imageView_2:
                case R.id.add_imageView_2: {
                    indexChange = 1;
                    chooseAvatar();
                    break;
                }
                case R.id.imageView_3:
                case R.id.add_imageView_3: {
                    indexChange = 2;
                    chooseAvatar();
                    break;
                }
                case R.id.imageView_4:
                case R.id.add_imageView_4: {
                    indexChange = 3;
                    chooseAvatar();
                    break;
                }
                case R.id.imageView_5:
                case R.id.add_imageView_5: {
                    indexChange = 4;
                    chooseAvatar();
                    break;
                }
                case R.id.imageView_6:
                case R.id.add_imageView_6: {
                    indexChange = 5;
                    chooseAvatar();
                    break;
                }
                case R.id.done_txt_cropview: {
                    setImageForUser(cropImageView.getCroppedImage());
                }
                case R.id.back_txt_cropview: {
                    layoutCropView.setVisibility(View.GONE);
                    break;
                }
            }
        }
    };

    void setImageForUser(Bitmap imageBitmap) {
        if (imageBitmap != null) {
            removeImageView.get(indexChange).setVisibility(View.VISIBLE);
            avatarImageView.get(indexChange).setImageBitmap(imageBitmap);
            addImageView.get(indexChange).setVisibility(View.GONE);
            networkManager.requestApi(networkManager.uploadAvatar(indexChange, imageBitmap), Constants.ID_UPLOAD_AVATAR);
        }
    }

    void deleteImage() {
        int id = UserPreferences.getCurrentUserId();
        networkManager.requestApi(networkManager.deleteAvatar(id, indexChange), Constants.ID_DELETE_AVATAR);
    }

    void chooseAvatar() {
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
    }

    private void dispatchSelectImageIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, getString(R.string.select_picture)), REQUEST_IMAGE_GALLERY);
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            if (Build.VERSION.SDK_INT >= 24) {
                picUri = FileProvider.getUriForFile(getContext(), "jp.stage.stagelovemaker.fileprovider", Utils.getOutputMediaFile(getContext()));
            } else {
                picUri = Uri.fromFile(Utils.getOutputMediaFile(getContext()));
            }
            takePictureIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, picUri);
            takePictureIntent.putExtra("return-data", true);
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    protected void cropImage() {
        iRotation = 0;
        layoutCropView.setVisibility(View.VISIBLE);
        cropImageView.setImageUriAsync(picUri);
        removeImageView.get(indexChange).setImageResource(R.drawable.delete_image);
    }

    void updateAvatar(RoundedImageView imageView, ImageView addImage, ImageView removeImage, AvatarModel model) {
        removeImage.setVisibility(View.VISIBLE);
        addImage.setVisibility(View.GONE);
        imageView.setBorderColor(ContextCompat.getColor(getActivity(), R.color.very_light_gray));
        Glide.with(getContext())
                .load(model.getUrl())
                .asBitmap()
                .placeholder(R.mipmap.ic_holder)
                .error(R.mipmap.ic_holder)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontTransform()
                .centerCrop()
                .dontAnimate()
                .into(imageView);
    }

    void removeAvatar(RoundedImageView imageView, ImageView addImage, ImageView removeImage) {
        removeImage.setVisibility(View.GONE);
        addImage.setVisibility(View.VISIBLE);
        //imageView.setImageResource(R.drawable.ic_add);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_add);
        imageView.setImageBitmap(bitmap);
        imageView.setBorderColor(Color.TRANSPARENT);
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
            userInfoModel.getMeta().setBirthday(birthday);
        }
    };

    @Override
    public void instagramUserInfo(String model) {
        tvInstagram.setContent(model);
    }

    @Override
    public void valuechange(String tag, String text) {
        switch (tag) {
            case Constants.TAG_CONTROL_INPUT_FIRSTNAME:
                userInfoModel.setFirstName(text);
                validate();
                break;
            case Constants.TAG_CONTROL_INPUT_LASTNAME:
                userInfoModel.setLastName(text);
                validate();
                break;
            case Constants.TAG_CONTROL_INPUT_ABOUT_ME:
                userInfoModel.getMeta().setAboutMe(text);
                break;
            case Constants.TAG_CONTROL_INPUT_WORK:
                userInfoModel.getMeta().setCurrentWork(text);
                break;
            case Constants.TAG_CONTROL_INPUT_SCHOOL:
                userInfoModel.getMeta().setSchool(text);
                break;
        }
    }

    Boolean validate() {
        Boolean validate = true;
        String blankField = getString(R.string.field_blank);
        tvFirstName.setIssuseText("");
        tvLastName.setIssuseText("");

        View view = null;

        if (TextUtils.isEmpty(userInfoModel.getFirstName())) {
            tvFirstName.setIssuseText(blankField);
            view = tvFirstName;
            validate = false;
        }

        if (TextUtils.isEmpty(userInfoModel.getLastName())) {
            tvLastName.setIssuseText(blankField);
            if (view == null) {
                view = tvLastName;
            }
            validate = false;
        }

        if (!validate) {
            view.requestFocus();
        }
        return validate;
    }

    @Override
    public void didReturn(String tag) {

    }

    @Override
    public void inputTextFocus(Boolean b, String tag) {

    }

    private void requestUpdateProfile() {
        networkManager.requestApi(networkManager.updateUserProfile(userInfoModel), Constants.ID_UPDATE_USER);
    }
}
