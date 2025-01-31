/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.kylin.rest.monitor;

import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.kylin.common.metrics.service.MonitorMetric;
import org.apache.kylin.guava30.shaded.common.collect.Sets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.Getter;

public abstract class AbstractMonitorCollectTask implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(AbstractMonitorCollectTask.class);

    @Getter
    private Set<String> runningServerMode = Sets.newHashSet();

    public AbstractMonitorCollectTask(List<String> runningServerMode) {
        if (CollectionUtils.isNotEmpty(runningServerMode)) {
            this.runningServerMode.addAll(runningServerMode);
        }
    }

    protected abstract MonitorMetric collect();

    @Override
    public void run() {
        try {
            MonitorReporter.getInstance().reportMonitorMetric(collect());
        } catch (Exception e) {
            logger.error("Failed to run monitor collect task!", e);
        }
    }
}
