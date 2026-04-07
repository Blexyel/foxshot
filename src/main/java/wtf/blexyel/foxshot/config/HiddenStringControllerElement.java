package wtf.blexyel.foxshot.config;

import dev.isxander.yacl3.api.utils.Dimension;
import dev.isxander.yacl3.gui.YACLScreen;
import dev.isxander.yacl3.gui.controllers.string.IStringController;
import dev.isxander.yacl3.gui.controllers.string.StringControllerElement;
import dev.isxander.yacl3.gui.utils.GuiUtils;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;

// code copied and slightly modified from StringControllerElement to hide the text and show
// asterisks instead
public class HiddenStringControllerElement extends StringControllerElement {
  public HiddenStringControllerElement(
      IStringController<String> controller,
      YACLScreen parent,
      Dimension<Integer> dimension,
      boolean password) {
    super(controller, parent, dimension, password);
  }

  @Override
  protected void extractValueText(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
    String renderedValue = "*".repeat(getValueText().getString().length());
    Component valueText = Component.literal(renderedValue);
    if (!isHovered())
      valueText =
          Component.literal(
                  GuiUtils.shortenString(renderedValue, textRenderer, getMaxUnwrapLength(), "..."))
              .setStyle(valueText.getStyle());

    int textX =
        getDimension().xLimit() - textRenderer.width(valueText) + renderOffset - getXPadding();
    graphics.enableScissor(
        inputFieldBounds.x(),
        inputFieldBounds.y() - 2,
        inputFieldBounds.xLimit() + 1,
        inputFieldBounds.yLimit() + 4);
    graphics.text(textRenderer, renderedValue, textX, getTextY(), getValueColor(), true);

    if (isHovered()) {
      ticks += a;

      String text = getValueText().getString();

      graphics.fill(
          inputFieldBounds.x(),
          inputFieldBounds.yLimit(),
          inputFieldBounds.xLimit(),
          inputFieldBounds.yLimit() + 1,
          -1);
      graphics.fill(
          inputFieldBounds.x() + 1,
          inputFieldBounds.yLimit() + 1,
          inputFieldBounds.xLimit() + 1,
          inputFieldBounds.yLimit() + 2,
          0xFF404040);

      if (inputFieldFocused || focused) {
        if (caretPos > text.length()) caretPos = text.length();

        int caretX = textX + textRenderer.width(text.substring(0, caretPos));
        if (text.isEmpty()) caretX = inputFieldBounds.x() + inputFieldBounds.width() / 2;

        if (selectionLength != 0) {
          int selectionX =
              textX + textRenderer.width(text.substring(0, caretPos + selectionLength));
          graphics.fill(
              caretX,
              inputFieldBounds.y() - 2,
              selectionX,
              inputFieldBounds.yLimit() - 1,
              0x803030FF);
        }

        if (caretPos != previousCaretPos) {
          previousCaretPos = caretPos;
          caretTicks = 0;
        }

        if ((caretTicks += a) % 20 <= 10)
          graphics.fill(
              caretX, inputFieldBounds.y() - 2, caretX + 1, inputFieldBounds.yLimit() - 1, -1);
      }
    }
    graphics.disableScissor();

    if (this.isHoveredInputField(mouseX, mouseY)) {
      graphics.requestCursor(
          isAvailable()
              ? com.mojang.blaze3d.platform.cursor.CursorTypes.IBEAM
              : com.mojang.blaze3d.platform.cursor.CursorTypes.NOT_ALLOWED);
    } else if (this.hovered) {
      graphics.requestCursor(
          isAvailable()
              ? com.mojang.blaze3d.platform.cursor.CursorTypes.POINTING_HAND
              : com.mojang.blaze3d.platform.cursor.CursorTypes.NOT_ALLOWED);
    }
  }

  private boolean isHoveredInputField(double mouseX, double mouseY) {
    return inputFieldBounds.isPointInside((int) mouseX, (int) mouseY);
  }
}
