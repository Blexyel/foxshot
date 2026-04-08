package wtf.blexyel.foxshot.network;

import java.io.File;
import okhttp3.Request;

public abstract class CustomUploadHandler {
  private static CustomUploadHandler instance;

  /**
   * Registers a CustomUploadHandler instance to be used for building upload requests. Reference
   * {@link CustomUploadHandler#buildRequest} for usage.
   *
   * @param handler The CustomUploadHandler instance to register
   * @throws IllegalArgumentException if the handler is null
   */
  public static void register(CustomUploadHandler handler) {
    instance = handler;
  }

  /**
   * @return The registered CustomUploadHandler instance, or null if none is registered
   */
  public static CustomUploadHandler getInstance() {
    return instance;
  }

  /**
   * Build a custom upload request
   *
   * <p>Example/Test implementation at {@link wtf.blexyel.foxshot.test.TestUploadHandler}
   *
   * @param filename The name of the file being uploaded
   * @param url The URL (e.g., "https://example.com")
   * @param username Username/identifier
   * @param token Authentication token
   * @param file The file to upload
   * @return A fully built OkHttp Request
   */
  public abstract Request buildRequest(
      String filename, String url, String username, String token, File file);
}
