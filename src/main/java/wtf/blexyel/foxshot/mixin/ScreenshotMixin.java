package wtf.blexyel.foxshot.mixin;

import static wtf.blexyel.foxshot.client.FoxshotClient.LOGGER;

import java.io.File;
import net.minecraft.client.Screenshot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import wtf.blexyel.foxshot.client.FoxshotClient;
import wtf.blexyel.foxshot.config.Config;
import wtf.blexyel.foxshot.network.UploadHandler;

@Mixin(Screenshot.class)
public class ScreenshotMixin {
  @Inject(method = "getFile", at = @At("RETURN"))
  private static void captureFileName(File picDir, CallbackInfoReturnable<File> cir) {
    if (Config.enabled) {
      File Screenshot = cir.getReturnValue();
      String ScreenshotName = Screenshot.getName();
      LOGGER.info("Filename: {} Path: {}", ScreenshotName, Screenshot);

      if (Config.auto_upload) {
        Thread.startVirtualThread(() -> UploadHandler.upload(Screenshot.toString()));
      } else {
        FoxshotClient.sendUploadMessage(ScreenshotName, Screenshot.toString());
      }
      // Thread.startVirtualThread(() -> UploadHandler.upload(Screenshot.toString(),
      // ScreenshotName));
    }
  }
}
