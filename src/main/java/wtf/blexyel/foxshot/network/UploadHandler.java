package wtf.blexyel.foxshot.network;

import static wtf.blexyel.foxshot.client.FoxshotClient.LOGGER;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.File;
import net.minecraft.client.Minecraft;
import okhttp3.*;
import wtf.blexyel.foxshot.client.FoxshotClient;
import wtf.blexyel.foxshot.config.Config;
import wtf.blexyel.foxshot.misc.FileServices;

public class UploadHandler {

  private static final OkHttpClient client = new OkHttpClient();

  public static void upload(String ospath) {
    try {
      String url = Config.custom_url;
      String furl = "";
      if (!url.contains("http")) {
        if (Config.https) {
          furl = "https://" + url;
        } else {
          furl = "http://" + url;
        }
      }

      String path = "/upload";
      // username not required atm
      String username = Config.username;
      String token = Config.token;

      ospath = ospath.replaceAll("^\"|\"$", "");
      File file = new File(ospath);
      String filename = file.getName();

      while (!file.exists() || file.length() == 0) {
        try {
          Thread.sleep(100);
        } catch (InterruptedException e) {
          Thread.currentThread().interrupt();
          LOGGER.warn("Screenshot wait interrupted");
          return;
        }
      }

      Request request = null;

      if (Config.service == FileServices.FOXBOX) {
        request = FoxboxHandler.upload(filename, furl, username, token, file);
      } else if (Config.service == FileServices.CATBOX) {
        request = CatboxHandler.upload(filename, furl, username, token, file);
      }

      // Execute
      assert request != null;
      try (Response response = client.newCall(request).execute()) {
        assert response.body() != null;
        String responseBody = response.body().string();
        LOGGER.info("status: {}\nbody: {}", response.code(), responseBody);

        if (Config.service == FileServices.CATBOX) {
          Minecraft.getInstance().execute(() -> FoxshotClient.toast(responseBody, true));
          Minecraft.getInstance()
              .execute(() -> FoxshotClient.sendMessage(responseBody, responseBody));
          return;
        }
        JsonObject json = JsonParser.parseString(responseBody).getAsJsonObject();
        String fileId = json.get("file_id").getAsString();
        // String file_url = json.get("file_url").getAsString();
        String file_url = "https://www.youtube.com/watch?v=dQw4w9WgXcQ";
        // i hate minecraft development
        Minecraft.getInstance().execute(() -> FoxshotClient.toast(fileId, true));
        Minecraft.getInstance().execute(() -> FoxshotClient.sendMessage(fileId, file_url));
      }

    } catch (Exception e) {
      Minecraft.getInstance().execute((() -> FoxshotClient.toast(e.getMessage(), false)));
      Minecraft.getInstance().execute(() -> FoxshotClient.sendMessage(e.getMessage(), ""));
      e.printStackTrace();
    }
  }
}
