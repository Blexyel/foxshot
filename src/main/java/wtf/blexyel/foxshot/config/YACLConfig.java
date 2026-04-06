package wtf.blexyel.foxshot.config;

import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.api.YetAnotherConfigLib;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class YACLConfig {
  public static Screen create(Screen parent) {
    Config.HANDLER.load();
    return YetAnotherConfigLib.createBuilder()
        .title(Component.literal("Foxshot Config"))
        // ### START TOGGLES ### //
        .category(
            ConfigCategory.createBuilder()
                .name(Component.literal("General"))
                .option(
                    YACLConfigHelper.booleanOption(
                        true, "Enabled", "Enables/Disables foxshot", ConfigEnums.ENABLED))
                .option(
                    YACLConfigHelper.booleanOption(
                        true,
                        "Auto Upload",
                        "Upload screenshots to foxbox automatically",
                        ConfigEnums.AUTO_UPLOAD))
                .build())
        // ### END TOGGLES ### //
        // ### START UPLOAD CONFIG ### //
        .category(
            ConfigCategory.createBuilder()
                .name(Component.literal("Uploads"))
                .option(
                    YACLConfigHelper.stringOption(
                        "", "Username", "Username for foxbox", ConfigEnums.USERNAME))
                .option(
                    YACLConfigHelper.stringOption(
                        "", "Token", "Token for foxbox", ConfigEnums.TOKEN))
                .option(
                    YACLConfigHelper.stringOption(
                        "https://foxbox.moe", "URL", "URL for foxbox", ConfigEnums.URL))
                .option(
                    YACLConfigHelper.booleanOption(
                        true, "HTTPS", "enable HTTPS", ConfigEnums.HTTPS))
                .build())
        // ### END UPLOAD CONFIG ### //
        .save(Config.HANDLER::save)
        .build()
        .generateScreen(parent);
  }

  public static final List<Integer> indices =
      IntStream.range(0, getMaxIndex()).boxed().collect(Collectors.toList());

  public static int getMaxIndex() {
    int count = 0;
    for (Field field : Config.class.getDeclaredFields()) {
      if (field.isAnnotationPresent(SerialEntry.class)) {
        if (field.getName().toLowerCase().contains("index")) {
          count++;
        }
      }
    }
    return count;
  }
}
