package jp.stage.stagelovemaker.fragment;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import jp.stage.stagelovemaker.R;
import jp.stage.stagelovemaker.base.BaseFragment;
import jp.stage.stagelovemaker.utils.Constants;
import jp.stage.stagelovemaker.utils.Utils;
import jp.stage.stagelovemaker.views.OnSingleClickListener;

/**
 * Created by congn on 8/29/2017.
 */

public class RightMenuFragment extends BaseFragment {

    RelativeLayout reportLayout;
    LinearLayout rightMenuLayout;
    RightMenuFragmentDelagate delegate;
    ArrayList<String> menus;
    private OnSingleClickListener mySingleListener = new OnSingleClickListener() {
        @Override
        public void onSingleClick(View view) {
            switch (view.getId()) {
                case R.id.report_layout: {
                    getActivity().onBackPressed();
                    break;
                }
                default: {
                    if (delegate != null) {
                        delegate.clickRightMenu((Integer) view.getTag());
                    }
                    break;
                }
            }
        }
    };

    public static RightMenuFragment newInstance(ArrayList<String> menus) {
        Bundle args = new Bundle();
        args.putStringArrayList(Constants.KEY_DATA, menus);
        RightMenuFragment fragment = new RightMenuFragment();
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
        View view = inflater.inflate(R.layout.fragment_menu_right, container, false);
        rightMenuLayout = (LinearLayout) view.findViewById(R.id.menu_right_layout);
        reportLayout = (RelativeLayout) view.findViewById(R.id.report_layout);
        return view;
    }

    public void setDelegate(RightMenuFragmentDelagate fragment) {
        delegate = fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        reportLayout.setOnClickListener(mySingleListener);
        menus = getArguments().getStringArrayList(Constants.KEY_DATA);
        final int height_cell = (int) Utils.dip2px(getContext(), 45);
        final int paddingLeft = (int) Utils.dip2px(getContext(), 27);
        int[] attrs = new int[]{R.attr.selectableItemBackground};
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs);
        int backgroundResource = typedArray.getResourceId(0, 0);

        for (int i = 0; i < menus.size(); i++) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height_cell);
            TextView textView = new TextView(getContext());
            textView.setLayoutParams(params);
            textView.setPadding(paddingLeft, 0, 0, 0);
            textView.setText(menus.get(i));
            textView.setTextColor(Color.BLACK);
            textView.setGravity(Gravity.CENTER | Gravity.LEFT);
            textView.setOnClickListener(mySingleListener);
            textView.setBackgroundResource(backgroundResource);
            textView.setTag(i);
            rightMenuLayout.addView(textView);
        }

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) rightMenuLayout.getLayoutParams();
        params.height = menus.size() * height_cell;
        rightMenuLayout.requestLayout();
    }

    public interface RightMenuFragmentDelagate {
        void clickRightMenu(int index);
    }
}
