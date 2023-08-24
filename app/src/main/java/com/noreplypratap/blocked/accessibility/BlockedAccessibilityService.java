package com.noreplypratap.blocked.accessibility;

import android.accessibilityservice.AccessibilityService;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Button;
import android.widget.Toast;

import com.noreplypratap.blocked.R;
import com.noreplypratap.blocked.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class BlockedAccessibilityService extends AccessibilityService {
    WindowManager windowManager;
    View overlayView;

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        String pm = event.getPackageName().toString();
        logger("onAccessibilityEvent .. " + pm);

        if (pm.equals(Constants.youtube) || pm.equals(Constants.youtubeVanced)) {
            int eventType = event.getEventType();
            if (eventType == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED ||
                    eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
                AccessibilityNodeInfo rootNode = getRootInActiveWindow();
                if (rootNode != null) {
                    traverseNodeHierarchy(rootNode);
                    rootNode.recycle();
                }
            }
        }

    }

    private void showOverlayDialog() {
        if (overlayView == null) {
            performGlobalAction(GLOBAL_ACTION_BACK);
            WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSLUCENT);

            overlayView = LayoutInflater.from(this).inflate(R.layout.overlay_layout, null);
            windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

            Button dismissButton = overlayView.findViewById(R.id.olButton);
            dismissButton.setOnClickListener(v -> {
                if (overlayView != null && windowManager != null) {
                    windowManager.removeView(overlayView);
                    performGlobalAction(GLOBAL_ACTION_BACK);
                    overlayView = null;
                }
            });
            windowManager.addView(overlayView, params);
        }
    }

    private void traverseNodeHierarchy(AccessibilityNodeInfo nodeInfo) {
        if (nodeInfo == null) {
            return;
        }

        if (nodeInfo.getText() != null) {
            CharSequence text = nodeInfo.getText();
            logger(text.toString());
            if (text.toString().equals(Constants.BlockedKey)){
                showOverlayDialog();
                return;
            }
        }

        for (int i = 0; i < nodeInfo.getChildCount(); i++) {
            AccessibilityNodeInfo childNode = nodeInfo.getChild(i);
            if (childNode != null) {
                traverseNodeHierarchy(childNode);
                childNode.recycle();
            }
        }

    }

    @Override
    public void onInterrupt() {
        logger("onInterrupt");
    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        logger("onServiceConnected");
    }

    private void logger(String msg) {
        Log.d(Constants.TAG,msg);
    }

}




