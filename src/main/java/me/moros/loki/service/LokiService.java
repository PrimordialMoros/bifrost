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

import java.io.Closeable;

import me.moros.loki.config.LokiSettings;
import me.moros.loki.pipeline.Record;
import net.kyori.adventure.key.InvalidKeyException;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;

public sealed interface LokiService extends Keyed, Closeable permits LokiServiceImpl {
  String LABEL_NAME_TYPE = "type";
  String LABEL_VALUE_CHAT = "chat";
  String LABEL_VALUE_COMMAND = "command";

  Key CHAT_KEY = Key.key(LokiServiceImpl.LABEL_NAME_TYPE, LokiServiceImpl.LABEL_VALUE_CHAT);
  Key COMMAND_KEY = Key.key(LokiServiceImpl.LABEL_NAME_TYPE, LokiServiceImpl.LABEL_VALUE_COMMAND);

  void send(Record logRecord);

  boolean send(Key key, Record logRecord);

  @Override
  void close();

  static LokiService create(String name, LokiSettings lokiSettings) throws InvalidKeyException {
    // Key validation can throw exception if name is illegal
    Key key = Key.key("bifrost", name);
    return new LokiServiceImpl(key, lokiSettings.createLoki(key.value()));
  }
}
