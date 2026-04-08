package wtf.blexyel.foxshot.network;

import static wtf.blexyel.foxshot.client.FoxshotClient.LOGGER;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.File;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import okhttp3.*;
import wtf.blexyel.foxshot.client.FoxshotClient;
import wtf.blexyel.foxshot.config.Config;
import wtf.blexyel.foxshot.misc.FileServices;

public class UploadHandler {
  public static final String modVersion =
      FabricLoader.getInstance()
          .getModContainer("foxshot")
          .get()
          .getMetadata()
          .getVersion()
          .getFriendlyString();

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

      switch (Config.service) {
        //case FOXBOX -> request = FoxboxHandler.upload(filename, furl, username, token, file);
        case CATBOX -> request = CatboxHandler.upload(filename, furl, username, token, file);
        case NULLPOINTER -> request = NullpointerUploadHandler.upload(filename, furl, username, token, file);
        case CUSTOM -> {
          CustomUploadHandler handler = CustomUploadHandler.getInstance();
          request = handler.buildRequest(filename, furl, username, token, file);
        }
      }

      // Execute
      assert request != null;
      try (Response response = client.newCall(request).execute()) {
        assert response.body() != null;
        String responseBody = response.body().string();
        LOGGER.info("status: {}\nbody: {}", response.code(), responseBody);

        if (/*Config.service != FileServices.FOXBOX*/true) {
          Minecraft.getInstance().execute(() -> FoxshotClient.toast(responseBody, true));
          Minecraft.getInstance()
              .execute(() -> FoxshotClient.sendMessage(responseBody, responseBody));
        } else {
          JsonObject json = JsonParser.parseString(responseBody).getAsJsonObject();
          String fileId = json.get("file_id").getAsString();
          // String file_url = json.get("file_url").getAsString();
          String file_url = "https://www.youtube.com/watch?v=dQw4w9WgXcQ";
          // i hate minecraft development
          Minecraft.getInstance().execute(() -> FoxshotClient.toast(fileId, true));
          Minecraft.getInstance().execute(() -> FoxshotClient.sendMessage(fileId, file_url));
        }
      }

    } catch (Exception e) {
      Minecraft.getInstance().execute((() -> FoxshotClient.toast(e.getMessage(), false)));
      Minecraft.getInstance().execute(() -> FoxshotClient.sendMessage(e.getMessage(), ""));
      e.printStackTrace();
    }
  }
}
