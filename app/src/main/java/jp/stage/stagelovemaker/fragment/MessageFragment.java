package jp.stage.stagelovemaker.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;
import jp.stage.stagelovemaker.R;
import jp.stage.stagelovemaker.activity.MainActivity;
import jp.stage.stagelovemaker.adapter.MessageAdapter;
import jp.stage.stagelovemaker.base.BaseFragment;
import jp.stage.stagelovemaker.base.MessageService;
import jp.stage.stagelovemaker.model.ErrorModel;
import jp.stage.stagelovemaker.model.InfoRoomModel;
import jp.stage.stagelovemaker.model.MessageModel;
import jp.stage.stagelovemaker.model.UserInfoModel;
import jp.stage.stagelovemaker.network.IHttpResponse;
import jp.stage.stagelovemaker.network.NetworkManager;
import jp.stage.stagelovemaker.utils.Constants;
import jp.stage.stagelovemaker.utils.Utils;
import jp.stage.stagelovemaker.views.TitleBar;

import static android.app.Activity.RESULT_OK;
import static jp.stage.stagelovemaker.utils.Constants.ARG_CHAT_ROOMS;
import static jp.stage.stagelovemaker.utils.Constants.KEY_DATA_TWO;
import static jp.stage.stagelovemaker.utils.Constants.REQUEST_IMAGE_CAPTURE;

/**
 * Created by congnguyen on 8/27/17.
 */

public class MessageFragment extends BaseFragment implements View.OnClickListener,
        TitleBar.TitleBarCallback, IHttpResponse {
    public static final String TAG = "MessageFragment";
    NetworkManager networkManager;
    Gson gson;
    private TitleBar titleBar;
    private RecyclerView rcvListMessage;
    private MessageAdapter messageAdapter;
    private ArrayList<MessageModel> messageModels;
    private ImageView ivAdd;
    private ImageView ivKeyboard;
    private ImageView ivFace;
    private ImageView ivMic;
    private EmojiconEditText tvMessageSend;
    private EmojIconActions emojIconActions;
    private String chatRoomId;
    private String message;
    private int senderId;
    private int receiverId;

    private UserInfoModel sender;
    private UserInfoModel receiver;

    private Uri picUri;

    public static final int REQUEST_IMAGE_CAPTURE = 100;
    public static final int REQUESR_IMAGE_GALLERY = 101;

    private final DatabaseReference firebaseRef = FirebaseDatabase.getInstance().getReference();
    private final DatabaseReference chatRef = firebaseRef.child(ARG_CHAT_ROOMS);

    public static MessageFragment newInstance(UserInfoModel receiver, String chatRoomId) {
        Bundle args = new Bundle();
        args.putParcelable(Constants.KEY_DATA, receiver);
        args.putString(Constants.KEY_DATA_TWO, chatRoomId);
        MessageFragment fragment = new MessageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        networkManager = new NetworkManager(context, this);
        gson = new Gson();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        messageModels = new ArrayList<>();
        messageAdapter = new MessageAdapter(getActivity(), messageModels);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_message, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        titleBar = (TitleBar) view.findViewById(R.id.title_bar);
        rcvListMessage = (RecyclerView) view.findViewById(R.id.rcv_list_message);
        ivAdd = (ImageView) view.findViewById(R.id.iv_add);
        ivKeyboard = (ImageView) view.findViewById(R.id.iv_keyboard);
        ivFace = (ImageView) view.findViewById(R.id.iv_face);
        ivMic = (ImageView) view.findViewById(R.id.iv_mic);
        tvMessageSend = (EmojiconEditText) view.findViewById(R.id.tv_input_message);
        emojIconActions = new EmojIconActions(getActivity(), view, tvMessageSend, ivFace);
        emojIconActions.ShowEmojIcon();
        emojIconActions.setIconsIds(R.mipmap.ic_keyboard, R.mipmap.ic_emoji);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        MainActivity mainActivity = (MainActivity) getActivity();
        sender = mainActivity.getLoginModel();
        receiver = getArguments().getParcelable(Constants.KEY_DATA);
        chatRoomId = getArguments().getString(KEY_DATA_TWO);
        senderId = sender.getId();
        receiverId = receiver.getId();
        if (receiver.getAvatars() != null && !receiver.getAvatars().isEmpty()) {
            for (int i = 0; i < receiver.getAvatars().size(); i++) {
                if (!TextUtils.isEmpty(receiver.getAvatars().get(i).getUrl())) {
                    messageAdapter.setAvatarUrl(receiver.getAvatars().get(i).getUrl());
                    break;
                }
            }
        }

        titleBar.setTitle(receiver.getFirstName());
        titleBar.setIconBack(R.drawable.ic_back_red);
        titleBar.enableBackButton();
        titleBar.setIconRight(R.mipmap.icon_three_dot);
        titleBar.setCallback(this);

        rcvListMessage.setLayoutManager(new LinearLayoutManager(getActivity()));
        rcvListMessage.setAdapter(messageAdapter);
        ivAdd.setOnClickListener(this);
        ivKeyboard.setOnClickListener(this);
        ivMic.setOnClickListener(this);

        getMessageData();
        tvMessageSend.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                message = s.toString();
                if (TextUtils.isEmpty(s)) {
                    ivMic.setImageResource(R.mipmap.ic_mic);
                } else {
                    ivMic.setImageResource(R.mipmap.ic_send_chat);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        emojIconActions.setKeyboardListener(new EmojIconActions.KeyboardListener() {
            @Override
            public void onKeyboardOpen() {
                Log.e(TAG, "Keyboard opened!");
            }

            @Override
            public void onKeyboardClose() {
                Log.e(TAG, "Keyboard closed");
            }
        });
    }

    private void getMessageData() {
        Query chatQuery;
//        if (senderId < receiverId) {
//            chatQuery = chatRef.child(senderId + "_" + receiverId).limitToLast(50);
//        } else {
//            chatQuery = chatRef.child(receiverId + "_" + senderId).limitToLast(50);
//        }
        chatQuery = chatRef.child(chatRoomId).limitToLast(50);

        chatQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                MessageModel messageModel = dataSnapshot.getValue(MessageModel.class);
                MessageFragment.this.addMessage(messageModel);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_add:
                chooseImage();
                break;
            case R.id.iv_keyboard:
                break;
            case R.id.iv_mic:
                Utils.hideSoftKeyboard(getActivity());
                if (!TextUtils.isEmpty(message)) {
                    sendMessage();
                }
                break;
        }
    }

    @Override
    public void onTitleBarClicked() {
        Utils.hideSoftKeyboard(getActivity());
        if (receiver != null) {
            DetailProfileFragment detailProfileFragment = DetailProfileFragment.newInstance(receiver, false);
            replace(detailProfileFragment, DetailProfileFragment.TAG, true, true);
        }
    }

    @Override
    public void onRightButtonClicked() {
        Utils.hideSoftKeyboard(getActivity());
        ArrayList<String> menus = new ArrayList<>();
        menus.add(getString(R.string.report));
        RightMenuFragment rightMenuFragment = RightMenuFragment.newInstance(menus);
        rightMenuFragment.setDelegate(rightMenuFragmentDelagate);
        add(rightMenuFragment, "", true, false, R.id.flContainer);
    }

    public RightMenuFragment.RightMenuFragmentDelagate rightMenuFragmentDelagate = new RightMenuFragment.RightMenuFragmentDelagate() {
        @Override
        public void clickRightMenu(int index) {
            getActivity().onBackPressed();
            ReportUserFragment fragment = ReportUserFragment.newInstance(receiverId);
            fragment.setDelegate(reportUserFragmentDelegate);
            addNoneSlideIn(fragment, ReportUserFragment.TAG, true, false, R.id.flContainer);
        }
    };

    public ReportUserFragment.ReportUserFragmentDelegate reportUserFragmentDelegate = new ReportUserFragment.ReportUserFragmentDelegate() {
        @Override
        public void clickOther() {
            getActivity().onBackPressed();
            ReportOtherFragment fragment = ReportOtherFragment.newInstance(receiverId);
            fragment.setDelegate(reportOtherFragmentDelegate);
            add(fragment, ReportOtherFragment.TAG, true, false, R.id.flContainer);
        }

        @Override
        public void onReportFinished() {
            getActivity().onBackPressed();
            Toast.makeText(getActivity(), getString(R.string.report_sent), Toast.LENGTH_SHORT).show();
        }
    };

    public ReportOtherFragment.ReportOtherFragmentDelegate reportOtherFragmentDelegate = () -> {
        getActivity().onBackPressed();
        Toast.makeText(getActivity(), getString(R.string.report_sent), Toast.LENGTH_SHORT).show();
    };

    @Override
    public void onBackButtonClicked() {
        Utils.hideSoftKeyboard(getActivity());
        getActivity().runOnUiThread(() -> {
            final Handler handler = new Handler();
            handler.postDelayed(() -> {
                if (getActivity() != null) {
                    getActivity().onBackPressed();
                }
            }, 300);
        });
    }

    @Override
    public void onMiddleButtonClicked() {

    }

    private void sendMessage() {
//        DatabaseReference chatRef;
//        if (senderId < receiverId) {
//            chatRef = firebaseRef.child(ARG_CHAT_ROOMS).child(senderId + "_" + receiverId);
//        } else {
//            chatRef = firebaseRef.child(ARG_CHAT_ROOMS).child(receiverId + "_" + senderId);
//        }
//        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss", Locale.US);
//        Date date = new Date();
//        String formattedDate = sdf.format(date);
//        MessageModel model = new MessageModel();
//        model.setContent(message);
//        model.setRead(false);
//        model.setCreate(formattedDate);
//        model.setSender_id(senderId);
//        model.setReceiver_id(receiverId);
//        model.setType("text");
//
//        chatRef.push().setValue(model).addOnSuccessListener(aVoid -> {
//
//        }).addOnFailureListener(e -> Toast.makeText(MessageFragment.this.getActivity(),
//                e.getMessage(), Toast.LENGTH_SHORT).show());
        networkManager.requestApiNoProgress(networkManager.sendMessage(senderId, message, chatRoomId), Constants.ID_SEND_CHAT);
        message = "";
        tvMessageSend.setText("");
    }

    private void sendImage(final Uri file) {
//        DatabaseReference chatRef;
//        if (senderId < receiverId) {
//            chatRef = firebaseRef.child(ARG_CHAT_ROOMS).child(senderId + "_" + receiverId);
//        } else {
//            chatRef = firebaseRef.child(ARG_CHAT_ROOMS).child(receiverId + "_" + senderId);
//        }
//
//        final StorageReference storageReference = FirebaseStorage.getInstance().getReference();
//        DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_HHmmssSSS", Locale.US);
//        Date date = new Date();
//        String name = dateFormat.format(date);
//        StorageReference imageGalleryRef = storageReference.child(senderId + "_" + name + ".jpg");
//        UploadTask uploadTask = imageGalleryRef.putFile(file);
//        uploadTask.addOnFailureListener(e -> {
//        }).addOnSuccessListener(taskSnapshot -> {
//            //dinh dang thoi gian gui len firebase
//            DateFormat dateFormat1 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.US);
//            String dateModified = dateFormat1.format(new Date());
//
//            //dinh dang ten file gui len firebase
//            DateFormat dateFormatFile = new SimpleDateFormat("yyyy_MM_dd_HHmmssSSS", Locale.US);
//            String nameFile = dateFormatFile.format(new Date());
//
//            Uri downloadUrl = taskSnapshot.getDownloadUrl();
//            //FileModel fileModel = new FileModel(downloadUrl.toString(), senderId + "_" + nameFile + ".jpg");
//            MessageModel model = new MessageModel();
//            model.setSender_id(senderId);
//            model.setReceiver_id(receiverId);
//            model.setCreate(dateModified);
//            model.setContent(downloadUrl.toString());
//            model.setRead(false);
//            model.setType("image");
//            //model.setFile(fileModel);
//            chatRef.push().setValue(model);
//        });
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), file);
            networkManager.requestApiNoProgress(networkManager.sendPicture(senderId, bitmap, chatRoomId), Constants.ID_SEND_IMAGE);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void addMessage(MessageModel messageModel) {
        messageAdapter.add(messageModel);
        rcvListMessage.scrollToPosition(messageAdapter.getItemCount() - 1);
    }

    private void chooseImage() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setItems(getResources().getStringArray(R.array.choose_avatar), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    dispatchSelectImageIntent();
                } else {
                    dispatchTakePictureIntent();
                }
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
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

    private void dispatchSelectImageIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, getString(R.string.select_picture)), REQUESR_IMAGE_GALLERY);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            sendImage(picUri);
        } else if (requestCode == REQUESR_IMAGE_GALLERY && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            picUri = data.getData();
            sendImage(picUri);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        checkMessageRead(chatRoomId);
    }

    @Override
    public void onHttpComplete(String response, int idRequest) {

    }

    @Override
    public void onHttpError(String response, int idRequest, int errorCode) {
        switch (idRequest) {
            case Constants.ID_SEND_IMAGE:
//                ErrorModel errorModel = gson.fromJson(response, ErrorModel.class);
//                if (errorModel != null && !TextUtils.isEmpty(errorModel.getErrorMsg())) {
//                    Toast.makeText(getActivity(), errorModel.getErrorMsg(), Toast.LENGTH_SHORT).show();
//                }
                break;
        }
    }

    private void checkMessageReadAsync(String receiverId) {
        Runnable runnable = () -> checkMessageRead(receiverId);
        new Thread(runnable).start();
    }

    private void checkMessageRead(String chatRoomId) {
        Intent i = new Intent(getActivity(), MessageService.class);
        //i.putExtra("receiverId", receiverId);
        //i.putExtra("senderId", senderId);
        i.putExtra("chat_room_id", chatRoomId);
        getActivity().startService(i);
    }
}
