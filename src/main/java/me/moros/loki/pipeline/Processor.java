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

@FunctionalInterface
public interface Processor<Context> {
  String METADATA_NAME = "name";
  String METADATA_UUID = "uuid";
  String METADATA_WORLD = "world";

  Record process(Record logRecord, Context context);
}
