package space.rogi27.renameit.commands;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.LoreComponent;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.math.random.Random;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class RenameItCommand {
    public static void init() {
        CommandRegistrationCallback.EVENT.register((dispatcher, a, b) -> {
            dispatcher.register(registerWithName("renameit"));
            dispatcher.register(registerWithName("ri"));
        });
    }

    public static LiteralArgumentBuilder<ServerCommandSource> registerWithName(String name) {
        LiteralArgumentBuilder<ServerCommandSource> item = literal(name).requires(Permissions.require("renameit.use", true))
                .then(literal("name")
                        .requires(Permissions.require("renameit.name", 2))
                        .then(argument("name", StringArgumentType.greedyString())
                                .suggests((context, builder) -> builder.suggest("Item with <red>cool name</red>").buildFuture())
                                .executes(RenameCommand::setName)
                        )
                        .executes(RenameCommand::setName)
                )
                .then(literal("color")
                        .requires(Permissions.require("renameit.color", 2))
                        .then(argument("color", IntegerArgumentType.integer(0))
                                .suggests((context, builder) -> builder.suggest(Random.create().nextBetween(1, 1000)).buildFuture())
                                .executes(ColorCommand::setColor)
                        )
                )
                .then(literal("lore")
                        .requires(Permissions.require("renameit.lore", 2))
                        .then(literal("add")
                                .requires(Permissions.require("renameit.lore.add", 2))
                                .then(argument("text", StringArgumentType.greedyString())
                                        .suggests((context, builder) -> builder.suggest("Your new <red>line</red> of lore").buildFuture())
                                        .executes(LoreCommand::addLore)
                                )
                        )
                        .then(literal("set")
                                .requires(Permissions.require("renameit.lore.set", 2))
                                .then(argument("line", IntegerArgumentType.integer(1))
                                        .suggests((context, builder) -> {
                                            if (context.getSource().getEntity().isPlayer()) {
                                                if (!context.getSource().getPlayer().getMainHandStack().isEmpty()) {
                                                    LoreComponent lore = context.getSource().getPlayer().getMainHandStack().get(DataComponentTypes.LORE);
                                                    if (lore != null) {
                                                        for (int i = 0; i < lore.lines().size(); i++) {
                                                            builder.suggest(i + 1);
                                                        }
                                                    }
                                                }
                                            }
                                            return builder.buildFuture();
                                        })
                                        .then(argument("text", StringArgumentType.greedyString())
                                                .suggests((context, builder) -> builder.suggest("Your <red>changed lore</red>").buildFuture())
                                                .executes(LoreCommand::setLore)
                                        )
                                )
                        )
                        .then(literal("delete")
                                .requires(Permissions.require("renameit.lore.delete", 2))
                                .then(argument("line", IntegerArgumentType.integer(1))
                                        .suggests((context, builder) -> {
                                            if (context.getSource().getEntity().isPlayer()) {
                                                if (!context.getSource().getPlayer().getMainHandStack().isEmpty()) {
                                                    LoreComponent lore = context.getSource().getPlayer().getMainHandStack().get(DataComponentTypes.LORE);
                                                    if (lore != null) {
                                                        for (int i = 0; i < lore.lines().size(); i++) {
                                                            builder.suggest(i + 1);
                                                        }
                                                    }
                                                }
                                            }
                                            return builder.buildFuture();
                                        })
                                        .executes(LoreCommand::deleteLore)
                                )
                        )
                );
        return item;
    }
}
