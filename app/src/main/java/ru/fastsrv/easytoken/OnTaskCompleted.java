package ru.fastsrv.easytoken;

import java.util.Map;

public interface OnTaskCompleted {
    void onTaskCompleted(Map<String,String> result);
}
