package wtf.blexyel.foxshot.network;

import java.io.File;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import wtf.blexyel.foxshot.config.Config;
import wtf.blexyel.foxshot.misc.FileServices;

public class NullpointerUploadHandler {
  public static Request upload(
      String filename, String url, String username, String token, File file) {
    if (Config.service == FileServices.NULLPOINTER) url = "https://0x0.st";

    String path = "/";

    MultipartBody requestBody =
        new MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart(
                "file",
                filename,
                RequestBody.create(file, MediaType.parse("application/octet-stream")))
            .addFormDataPart("secret", "")
            .build();

    // Build request
    return new Request.Builder()
        .url(url + path)
        .header(
            "User-Agent",
            "foxshot/" + UploadHandler.modVersion + " (https://github.com/blexyel/foxshot)")
        .post(requestBody)
        .build();
  }
}
