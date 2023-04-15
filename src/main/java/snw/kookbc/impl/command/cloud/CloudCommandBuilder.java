/*
 *     KookBC -- The Kook Bot Client & JKook API standard implementation for Java.
 *     Copyright (C) 2022 - 2023 KookBC contributors
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published
 *     by the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package snw.kookbc.impl.command.cloud;

import cloud.commandframework.annotations.AnnotationParser;
import cloud.commandframework.arguments.parser.ParserParameters;
import cloud.commandframework.meta.CommandMeta;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.NotNull;
import snw.jkook.command.CommandSender;
import snw.jkook.plugin.Plugin;

import java.util.function.Function;

/**
 * @author huanmeng_qwq
 */
public interface CloudCommandBuilder {
    static @NotNull CloudBasedCommandManager createManager(Plugin plugin) {
        return new CloudBasedCommandManager(plugin);
    }

    static @NotNull CloudAnnotationParser createParser(@NotNull CloudBasedCommandManager commandManager,
                                                       @NotNull Function<@NonNull ParserParameters, @NonNull CommandMeta> metaMapper) {
        return new CloudAnnotationParser(new AnnotationParser<>(commandManager, CommandSender.class, metaMapper));
    }
}
