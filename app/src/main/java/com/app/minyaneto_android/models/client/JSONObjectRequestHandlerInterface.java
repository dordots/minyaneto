package com.app.minyaneto_android.models.client;

import org.json.JSONObject;

public interface JSONObjectRequestHandlerInterface<T> {
    public boolean isProcessReceivedDataImplemented();
    public boolean isExecuteCommandsImplemented();

    public T processReceivedData(JSONObject jsObj);
    public boolean executeCommands(T processedData);
}
