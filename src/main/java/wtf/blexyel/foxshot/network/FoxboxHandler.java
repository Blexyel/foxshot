package wtf.blexyel.foxshot.network;

import java.io.File;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;

public class FoxboxHandler {
  public static Request upload(
      String filename, String url, String username, String token, File file) {
    // if (Config.service == FileServices.FOXBOX) url = "https://foxbox.moe";

    String path = "/upload";

    MultipartBody requestBody =
        new MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart(
                "data",
                filename,
                RequestBody.create(file, MediaType.parse("application/octet-stream")))
            .build();

    // Build request
    return new Request.Builder()
        .url(url + path)
        .header(
            "User-Agent",
            "foxshot/" + UploadHandler.modVersion + " (https://github.com/blexyel/foxshot)")
        .header("Authorization", "Bearer " + token)
        .post(requestBody)
        .build();
  }
}
