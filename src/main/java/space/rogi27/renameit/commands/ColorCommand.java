package space.rogi27.renameit.commands;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.DyedColorComponent;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
public class ColorCommand {
    public static int setColor(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        if (context.getSource().getEntity().isPlayer()) {
            if (context.getSource().getPlayer().getMainHandStack().isEmpty()) {
                context.getSource().sendMessage(Text.translatable("text.renameit.color_empty").formatted(Formatting.YELLOW));
                return 0;
            }

            int color = context.getArgument("color", Integer.class);
            DyedColorComponent dyedColor = context.getSource().getPlayer().getMainHandStack().getOrDefault(
                    DataComponentTypes.DYED_COLOR, new DyedColorComponent(0, true)
            );

            if (color == 0) {
                context.getSource().getPlayer().getMainHandStack().set(DataComponentTypes.DYED_COLOR, null);
            } else {
                context.getSource().getPlayer().getMainHandStack().set(DataComponentTypes.DYED_COLOR, new DyedColorComponent(color, true));
            }

            context.getSource().sendMessage(Text.translatable("text.renameit.color_changed", Text.literal(String.valueOf(color)).formatted(Formatting.WHITE)).formatted(Formatting.GREEN));
            return 1;
        } else {
            return 0;
        }
    }
}
