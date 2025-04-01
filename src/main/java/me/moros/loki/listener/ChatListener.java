/*
 * Copyright 2024-2025 Moros
 *
 * This file is part of Bifrost.
 *
 * Bifrost is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Bifrost is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with Bifrost. If not, see <https://www.gnu.org/licenses/>.
 */

package me.moros.loki.listener;

import java.util.function.Consumer;

import io.papermc.paper.event.player.AsyncChatEvent;
import me.moros.loki.pipeline.Record;
import me.moros.loki.platform.PlayerProcessor;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public record ChatListener(Consumer<Record> consumer) implements PlayerProcessor, Listener {
  @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
  private void onChat(AsyncChatEvent event) {
    String message = PlainTextComponentSerializer.plainText().serialize(event.message());
    consumer.accept(process(Record.create(message), event.getPlayer()));
  }
}
