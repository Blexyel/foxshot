package wtf.blexyel.foxshot.config;

import com.google.gson.GsonBuilder;
import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resources.Identifier;

public class Config {
  public static final ConfigClassHandler<Config> HANDLER =
      ConfigClassHandler.createBuilder(Config.class)
          .id(Identifier.tryParse("foxshot:config"))
          .serializer(
              config ->
                  GsonConfigSerializerBuilder.create(config)
                      .setPath(FabricLoader.getInstance().getConfigDir().resolve("foxshot.json5"))
                      .appendGsonBuilder(GsonBuilder::setPrettyPrinting)
                      .setJson5(true)
                      .build())
          .build();

  // ### START TOGGLES ### //
  @SerialEntry public static boolean enabled = true;
  @SerialEntry public static boolean auto_upload = false;
  // ### END TOGGLES ### //

  // ### START UPLOAD CONFIG ### //
  @SerialEntry public static String username = "";
  @SerialEntry public static String token = "";
  @SerialEntry public static String url = "https://foxbox.moe";
  @SerialEntry public static boolean https = true;
  // ### END UPLOAD CONFIG ### //
}
