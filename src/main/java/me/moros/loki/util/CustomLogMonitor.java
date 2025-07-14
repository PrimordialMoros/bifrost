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

package me.moros.loki.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.mjaron.tinyloki.ILogMonitor;

public record CustomLogMonitor(Logger logger) implements ILogMonitor {
  private static final String NAME_PREFIX = "Bifrost-";

  public CustomLogMonitor(String name) {
    this(LoggerFactory.getLogger(NAME_PREFIX + name));
  }

  @Override
  public boolean isVerbose() {
    return false;
  }

  @Override
  public void logVerbose(String s) {
  }

  @Override
  public void logInfo(final String what) {
  }

  @Override
  public void logError(final String what) {
    logger.error(what);
  }

  @Override
  public void onConfigured(final String contentType, final String contentEncoding) {
  }

  @Override
  public void onEncoded(final byte[] in, final byte[] out) {
  }

  @Override
  public void send(final byte[] message) {
  }

  @Override
  public void sendOk(final int status) {
  }

  @Override
  public void sendErr(final int status, final String message) {
    logger.error("Unexpected server response status: {}: {}", status, message);
  }

  @Override
  public void onException(final Exception exception) {
    logger.error(exception.getMessage(), exception);
  }

  @Override
  public void onSync(final boolean isSuccess) {
    if (!isSuccess) {
      logger.warn("Sync operation failed.");
    }
  }

  @Override
  public void onStart() {
  }

  @Override
  public void onStop(final boolean isSuccess) {
    if (!isSuccess) {
      logger.warn("Stop operation failed.");
    }
  }
}
