package ravtech.co.il.httpclient.model;

import android.net.Uri;

/**
 * Created by maytav on 8/11/2016.
 */
public class DataPart {

    private String fileName;
    private Uri filePath;
    private byte[] content;

    private String type;


    public Uri getFilePath() {
        return filePath;
    }

    public void setFilePath(Uri filePath) {
        this.filePath = filePath;
    }

    public DataPart(String name, byte[] data) {
        fileName = name;
        content = data;
    }

    public DataPart(String name, byte[] data, String mimeType) {
        fileName = name;
        content = data;
        type = mimeType;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
