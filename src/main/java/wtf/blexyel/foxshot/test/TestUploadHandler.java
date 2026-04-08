package wtf.blexyel.foxshot.test;

import java.io.File;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import wtf.blexyel.foxshot.config.Config;
import wtf.blexyel.foxshot.misc.FileServices;
import wtf.blexyel.foxshot.network.CustomUploadHandler;
import wtf.blexyel.foxshot.network.UploadHandler;

public class TestUploadHandler extends CustomUploadHandler {
  @Override
  public Request buildRequest(
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
        .header(
            "User-Agent",
            "foxshot/" + UploadHandler.modVersion + " (https://github.com/blexyel/foxshot)")
        .header("Authorization", "Bearer " + token)
        .post(requestBody)
        .build();
  }
}
