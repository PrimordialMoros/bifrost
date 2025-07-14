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

import me.moros.loki.pipeline.Record;
import me.moros.loki.platform.PlayerProcessor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public record JoinListener(Consumer<Record> consumer) implements PlayerProcessor, Listener {
  @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
  private void onJoin(PlayerJoinEvent event) {
    Player player = event.getPlayer();
    consumer.accept(process(Record.create(player.getAddress().getAddress().getHostAddress()), player));
  }
}
