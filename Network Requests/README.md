# Network Requests

https://stackoverflow.com/questions/2938502/sending-post-data-in-android
https://developer.android.com/reference/java/net/HttpURLConnection
https://developer.android.com/reference/android/os/AsyncTask

## Requirements

1. Internet Permission

`AndroidManifest.xml`

```xml
    <uses-permission android:name="android.permission.INTERNET"/>
```

## Usage
```java
private TextView tv;
tv = findViewById(R.id.tv);

CallApi api = new CallApi(new CallApiClient() {
    @Override
    public void handleResponse(final String s) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tv.setText(s);
            }
        });
    }
});
api.execute("https://us-central1-deadpool-d9707.cloudfunctions.net/helloWorld", "");
```
See [CallApi.java](./CallApi.java) for implementation.
