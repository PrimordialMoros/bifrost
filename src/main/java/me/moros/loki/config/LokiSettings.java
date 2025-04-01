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

package me.moros.loki.config;

import java.util.Map;

import me.moros.loki.util.CustomLogMonitor;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import pl.mjaron.tinyloki.Labels;
import pl.mjaron.tinyloki.TinyLoki;

@ConfigSerializable
public final class LokiSettings {
  private String url = "http://localhost:3100/loki/api/v1/push";
  private String username = "";
  private String password = "";
  private Map<String, String> labels = Map.of();

  public TinyLoki createLoki(String name) {
    return TinyLoki
      .withUrl(url)
      .withBasicAuth(username, password)
      .withGzipLogEncoder()
      .withIncrementingTimestampProvider()
      .withLabels(Labels.of(Labels.SERVICE_NAME, name).l(labels))
      .withLogMonitor(new CustomLogMonitor(name))
      .open();
  }
}
