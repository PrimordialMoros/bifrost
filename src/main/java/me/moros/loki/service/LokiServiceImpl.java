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

package me.moros.loki.service;

import java.util.Map;
import java.util.Map.Entry;

import me.moros.loki.pipeline.Record;
import net.kyori.adventure.key.Key;
import pl.mjaron.tinyloki.ILogStream;
import pl.mjaron.tinyloki.Labels;
import pl.mjaron.tinyloki.TinyLoki;

record LokiServiceImpl(Key key, TinyLoki loki, Map<Key, ILogStream> streams) implements LokiService {
  LokiServiceImpl(Key key, TinyLoki tinyLoki) {
    this(key, tinyLoki, Map.ofEntries(
      streamEntry(CHAT_KEY, tinyLoki),
      streamEntry(COMMAND_KEY, tinyLoki)
    ));
  }

  private static Entry<Key, ILogStream> streamEntry(Key key, TinyLoki tinyLoki) {
    return Map.entry(key, tinyLoki.openStream(Map.of(key.namespace(), key.value())));
  }

  @Override
  public void send(Record logRecord) {
    Labels labels = Labels.of(logRecord.metadata());
    streams.values().forEach(stream -> stream.log(logRecord.timestamp(), logRecord.message(), labels));
  }

  @Override
  public boolean send(Key key, Record logRecord) {
    ILogStream stream = streams.get(key);
    if (stream != null) {
      stream.log(logRecord.timestamp(), logRecord.message(), Labels.of(logRecord.metadata()));
      return true;
    }
    return false;
  }

  @Override
  public void close() {
    try {
      loki.closeSync();
    } catch (InterruptedException e) {
      loki.getLogMonitor().onException(e);
    }
  }
}
