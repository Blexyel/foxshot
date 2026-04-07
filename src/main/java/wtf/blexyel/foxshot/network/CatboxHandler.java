package wtf.blexyel.foxshot.network;

import java.io.File;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import wtf.blexyel.foxshot.config.Config;
import wtf.blexyel.foxshot.misc.FileServices;

public class CatboxHandler {
  public static Request upload(
      String filename, String url, String username, String token, File file) {
    if (Config.service == FileServices.CATBOX) url = "https://catbox.moe";

    String path = "/user/api.php";

    MultipartBody requestBody =
        new MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("reqtype", "fileupload")
            .addFormDataPart("userhash", username)
            .addFormDataPart(
                "fileToUpload",
                filename,
                RequestBody.create(file, MediaType.parse("application/octet-stream")))
            .build();

    // Build request
    return new Request.Builder()
        .url(url + path)
        .header("Authorization", "Bearer " + token)
        .post(requestBody)
        .build();
  }
}
