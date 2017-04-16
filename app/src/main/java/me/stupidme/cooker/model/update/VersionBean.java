package me.stupidme.cooker.model.update;

/**
 * Created by StupidL on 2017/4/16.
 */

public class VersionBean {

    private String versionName;

    private String changes;

    private String url;

    public String getVersionName() {
        return versionName;
    }

    public String getChanges() {
        return changes;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public String toString() {
        return String.format("[\n" +
                "versionName: %s\n" +
                "changes: %s\n" +
                "]", versionName, changes);
    }

}
