package jp.stage.stagelovemaker.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import java.lang.reflect.Field;

/**
 * Created by congn on 7/11/2017.
 */

public class BaseFragment extends Fragment {
    public void startNewActivity(Class<?> cls, Bundle bundle) {

        if ((getActivity()) != null) {

            ((CommonActivity) getActivity()).startNewActivity(cls, bundle);

        }

    }



    public void replace(BaseFragment fragment, String tag, boolean backstack, boolean animation) {

        if ((getActivity()) != null) {

            ((CommonActivity) getActivity()).replace(fragment, tag, backstack, animation);

        }

    }



    public void replace(BaseFragment fragment, String tag, boolean backstack, boolean animation, int idResourceView) {

        if ((getActivity()) != null) {

            ((CommonActivity) getActivity()).replace(fragment, tag, backstack, animation, idResourceView);

        }

    }



    public void add(BaseFragment fragment, String tag, boolean backstack, boolean animation) {

        if ((getActivity()) != null) {

            ((CommonActivity) getActivity()).add(fragment, tag, backstack, animation);

        }

    }



    public void add(BaseFragment fragment, String tag, boolean backstack, boolean animation, int idResourceView) {

        if ((getActivity()) != null) {

            ((CommonActivity) getActivity()).add(fragment, tag, backstack, animation, idResourceView);

        }

    }



    public void addNoneSlideIn(BaseFragment fragment, String tag, boolean backstack, boolean animation, int idResourceView) {

        if ((getActivity()) != null) {

            ((CommonActivity) getActivity()).addNoneSlideIn(fragment, tag, backstack, animation, idResourceView);

        }

    }



    @Override

    public void onDetach() {

        super.onDetach();



        try {

            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");

            childFragmentManager.setAccessible(true);

            childFragmentManager.set(this, null);



        } catch (NoSuchFieldException e) {

            throw new RuntimeException(e);

        } catch (IllegalAccessException e) {

            throw new RuntimeException(e);

        }

    }
}
