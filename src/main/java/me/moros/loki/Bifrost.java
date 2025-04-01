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

package me.moros.loki;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import me.moros.loki.config.ConfigManager;
import me.moros.loki.config.LokiSettings;
import me.moros.loki.listener.ChatListener;
import me.moros.loki.listener.CommandListener;
import me.moros.loki.pipeline.Record;
import me.moros.loki.service.LokiService;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;
import org.bukkit.plugin.java.JavaPlugin;
import org.slf4j.Logger;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

public class Bifrost extends JavaPlugin {
  private final Logger logger;
  private final Collection<LokiService> lokiServices;

  public Bifrost(Logger logger, Path path) {
    this.logger = logger;
    try (ConfigManager configManager = new ConfigManager(this.logger, path.resolve("config.yml"))) {
      this.lokiServices = configManager.load(Config::new).instances.entrySet().stream()
        .map(e -> LokiService.create(e.getKey(), e.getValue()))
        .toList();
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  @Override
  public void onEnable() {
    if (lokiServices.isEmpty()) {
      logger.warn("No Loki output found.");
      return;
    }
    String output = lokiServices.stream()
      .map(Keyed::key)
      .map(Key::value)
      .collect(Collectors.joining(", ", "Configured loki outputs: [", "]"));
    logger.info(output);
    getServer().getPluginManager().registerEvents(new ChatListener(multiSend(LokiService.CHAT_KEY)), this);
    getServer().getPluginManager().registerEvents(new CommandListener(multiSend(LokiService.COMMAND_KEY)), this);
  }

  private Consumer<Record> multiSend(Key key) {
    return logRecord -> lokiServices.forEach(ls -> ls.send(key, logRecord));
  }

  @Override
  public void onDisable() {
    lokiServices.forEach(LokiService::close);
  }

  @ConfigSerializable
  private static final class Config {
    @Setting(nodeFromParent = true)
    private Map<String, LokiSettings> instances = Map.of("minecraft", new LokiSettings());
  }
}
