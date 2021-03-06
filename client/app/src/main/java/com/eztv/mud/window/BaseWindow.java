package com.eztv.mud.window;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import com.eztv.mud.R;

import static com.eztv.mud.Constant.popupWindows;
import static com.eztv.mud.util.BScreen.getScreenHeight;
import static com.eztv.mud.util.BScreen.getScreenWidth;

public class BaseWindow implements PopupWindow.OnDismissListener {
    PopupWindow popupWindow;
    View view;
    View targetView;


    public BaseWindow setViewAndTarget(View view,View target){
        this.view = view;
        this.targetView = target;
        return this;
    }

    public BaseWindow setAnim(int anim){
        popupWindow.setAnimationStyle(anim);
        return this;
    }

    public void show(){
        popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.update();
        popupWindow.showAtLocation(targetView, Gravity.CENTER , 0, 0);
        popupWindow.setOnDismissListener(this);
        popupWindows.add(popupWindow);
    }

    public void showByDimension(int width,int height){
        popupWindow = new PopupWindow(view, width, height);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.update();
        popupWindow.showAtLocation(targetView, Gravity.CENTER , 0, 0);
        popupWindow.setOnDismissListener(this);
        popupWindows.add(popupWindow);
    }


    public void showBySimpleSize(Activity activity){
        popupWindow = new PopupWindow(view, getScreenWidth(activity)*9/10, -2);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupWindow.setAnimationStyle(R.style.pop_animation);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.update();
        popupWindow.showAtLocation(targetView, Gravity.CENTER , 0, 0);
        popupWindow.setOnDismissListener(this);
        popupWindows.add(popupWindow);
    }
    public void showBySkill(Activity activity,String role){
        popupWindow = new PopupWindow(view, getScreenWidth(activity)*9/10, -2);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupWindow.setAnimationStyle(R.style.pop_animation);
        role= role==null?"":role;
        if(role.equals("unHand")){
            popupWindow.setFocusable(false);
            popupWindow.setOutsideTouchable(false);
        }else {
            popupWindow.setFocusable(true);
            popupWindow.setOutsideTouchable(true);
        }
        popupWindow.update();
        popupWindow.showAtLocation(targetView, Gravity.TOP , 0, (int)(getScreenHeight(activity)*0.65));
        popupWindow.setOnDismissListener(this);
        popupWindows.add(popupWindow);
    }
    public void showFull(Activity activity){
        popupWindow = new PopupWindow(view, getScreenWidth(activity), getScreenHeight(activity));
        popupWindow.setAnimationStyle(R.style.pop_animation);
        popupWindow.setFocusable(false);
        popupWindow.setOutsideTouchable(false);
        popupWindow.update();
        popupWindow.showAtLocation(targetView, Gravity.CENTER , 0, 0);
        popupWindow.setOnDismissListener(this);
        popupWindows.add(popupWindow);
    }

    public void closeAll(){
        for(int i = 0;i<popupWindows.size() ;i++){
            popupWindows.get(i).dismiss();
        }
        popupWindows.clear();
    }


    @Override
    public void onDismiss() {
        //popupWindows.remove(this.popupWindow);
    }
}
