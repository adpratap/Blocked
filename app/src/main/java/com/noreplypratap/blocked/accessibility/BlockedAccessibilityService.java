package com.noreplypratap.blocked.accessibility;

import android.accessibilityservice.AccessibilityService;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.ArrayList;
import java.util.List;

public class BlockedAccessibilityService extends AccessibilityService {

    public static final String BlockedKey = "abcde";

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
                    if (text.equals(BlockedKey)){
                        performGlobalAction(GLOBAL_ACTION_BACK);
                        Log.d("onAccessibilityEvent","......................"+ text);
                    }
                }
            }
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




