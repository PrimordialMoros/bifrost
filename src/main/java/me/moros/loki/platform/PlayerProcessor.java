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

package me.moros.loki.platform;

import java.util.Map;

import me.moros.loki.pipeline.Processor;
import me.moros.loki.pipeline.Record;
import org.bukkit.entity.Player;
import pl.mjaron.tinyloki.Labels;

import static java.util.Map.entry;

public interface PlayerProcessor extends Processor<Player> {
  String METADATA_NAME = "name";
  String METADATA_UUID = "uuid";
  String METADATA_WORLD = "world";

  private static Map<String, String> createStructuredMetadata(Player player) {
    return Map.ofEntries(
      entry(METADATA_NAME, player.getName()),
      entry(METADATA_UUID, player.getUniqueId().toString()),
      entry(METADATA_WORLD, player.getWorld().key().asString()),
      entry(Labels.LEVEL, Labels.INFO)
    );
  }

  @Override
  default me.moros.loki.pipeline.Record process(Record logRecord, Player context) {
    Map<String, String> metadata = createStructuredMetadata(context);
    logRecord.metadata().putAll(metadata);
    return logRecord;
  }
}
