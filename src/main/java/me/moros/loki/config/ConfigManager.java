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

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.reference.ConfigurationReference;
import org.spongepowered.configurate.reference.WatchServiceListener;
import org.spongepowered.configurate.yaml.NodeStyle;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

public final class ConfigManager implements AutoCloseable {
  private final WatchServiceListener listener;
  private final ConfigurationReference<? extends ConfigurationNode> reference;

  public ConfigManager(Logger logger, Path configPath) throws IOException {
    this.listener = WatchServiceListener.create();
    Files.createDirectories(configPath.getParent());
    reference = listener.listenToConfiguration(this::createLoader, configPath);
    reference.errors().subscribe(e -> logger.warn(e.getValue().getMessage(), e.getValue()));
  }

  private YamlConfigurationLoader createLoader(Path path) {
    return YamlConfigurationLoader.builder()
      .indent(2)
      .nodeStyle(NodeStyle.BLOCK)
      .path(path)
      .build();
  }

  public <T> T load(Supplier<T> supplier) {
    return load(List.of(), supplier);
  }

  @SuppressWarnings("unchecked")
  public <T> T load(Iterable<String> node, Supplier<T> supplier) {
    T def = supplier.get();
    try {
      return (T) reference.node().node(node).get(def.getClass(), def);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  @Override
  public void close() throws IOException {
    reference.save();
    reference.close();
    listener.close();
  }
}
