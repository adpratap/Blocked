package com.noreplypratap.blocked.accessibility;

import android.accessibilityservice.AccessibilityService;
import android.graphics.PixelFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Button;

import com.noreplypratap.blocked.R;

import java.util.ArrayList;
import java.util.List;

public class BlockedAccessibilityService extends AccessibilityService {

    public static final String BlockedKey = "abcde";
    WindowManager windowManager;
    View overlayView;

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {

        int eventType = event.getEventType();
        if (eventType == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED ||
                eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            AccessibilityNodeInfo rootNode = getRootInActiveWindow();
            if (rootNode != null) {
                List<String> allText = new ArrayList<>();
                traverseNodeHierarchy(rootNode, allText);
                rootNode.recycle();
                for (String text : allText) {
                    if (text.equals(BlockedKey)) {
                        showOverlayDialog();
                    }
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

    private void traverseNodeHierarchy(AccessibilityNodeInfo nodeInfo, List<String> textList) {
        if (nodeInfo == null) {
            return;
        }
        if (nodeInfo.getText() != null) {
            CharSequence text = nodeInfo.getText();
            textList.add(text.toString());
        }
        for (int i = 0; i < nodeInfo.getChildCount(); i++) {
            AccessibilityNodeInfo childNode = nodeInfo.getChild(i);
            if (childNode != null) {
                traverseNodeHierarchy(childNode, textList);
                childNode.recycle();
            }
        }
    }

    @Override
    public void onInterrupt() {
        //Nothing
    }

}




