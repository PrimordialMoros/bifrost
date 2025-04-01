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

package me.moros.loki.pipeline;

import java.util.HashMap;
import java.util.Map;

import pl.mjaron.tinyloki.ILogStream;

public sealed interface Record permits RecordImpl {
  long timestamp();

  Map<String, String> metadata();

  String message();

  static Record create(String message) {
    return create(ILogStream.TIMESTAMP_NONE, new HashMap<>(), message);
  }

  static Record create(Map<String, String> metadata, String message) {
    return create(ILogStream.TIMESTAMP_NONE, metadata, message);
  }

  static Record create(long timestamp, Map<String, String> metadata, String message) {
    return new RecordImpl(timestamp, metadata, message);
  }
}
