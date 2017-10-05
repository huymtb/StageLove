package jp.stage.stagelovemaker.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import java.util.ArrayList;

import jp.stage.stagelovemaker.R;
import jp.stage.stagelovemaker.adapter.ImageInstagramAdapter;
import jp.stage.stagelovemaker.base.BaseFragment;
import jp.stage.stagelovemaker.model.InstagramPhoto;
import jp.stage.stagelovemaker.utils.Constants;
import jp.stage.stagelovemaker.utils.Utils;

/**
 * Created by congn on 8/7/2017.
 */

public class InstagramImageFragment extends BaseFragment {
    GridView instagramGridView;
    ArrayList<InstagramPhoto> arrayImage;
    int numberColumn;
    String userNameInstagram;

    public static InstagramImageFragment newInstance(ArrayList<InstagramPhoto> arrayImage, int column, String userName) {
        InstagramImageFragment fragment = new InstagramImageFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(Constants.KEY_DATA, arrayImage);
        args.putInt(Constants.KEY_DATA_TWO, column);
        args.putString(Constants.KEY_DATA_THREE, userName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_instagram_image, container, false);
        instagramGridView = (GridView) view.findViewById(R.id.instagram_image_grid);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        arrayImage = getArguments().getParcelableArrayList(Constants.KEY_DATA);
        numberColumn = getArguments().getInt(Constants.KEY_DATA_TWO);
        userNameInstagram = getArguments().getString(Constants.KEY_DATA_THREE);

        int width = Utils.getiWidthScreen(getActivity());
        int margin = (int) getResources().getDimension(R.dimen.form_margin);
        int paddingButton = (int) getResources().getDimension(R.dimen.padding_interest_button);
        int widthButton = (width - (margin * 2) - (paddingButton * 2)) / 3;
        instagramGridView.setNumColumns(numberColumn);
        instagramGridView.setColumnWidth(widthButton);
        ImageInstagramAdapter interestAdapter = new ImageInstagramAdapter(getContext(), arrayImage, widthButton, userNameInstagram);
        instagramGridView.setAdapter(interestAdapter);
    }
}
