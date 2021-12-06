/*
 * Copyright 2021 EPAM Systems.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.epam.digital.data.platform.history.service;

import com.epam.digital.data.platform.history.config.properties.OpenshiftProperties;
import io.fabric8.kubernetes.api.model.batch.DoneableJob;
import io.fabric8.kubernetes.api.model.batch.JobFluent;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.dsl.BatchAPIGroupDSL;
import io.fabric8.kubernetes.client.dsl.MixedOperation;
import io.fabric8.kubernetes.client.dsl.ScalableResource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.function.Supplier;

import static com.epam.digital.data.platform.history.config.properties.OpenshiftProperties.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OpenShiftServiceTest {

  private static final String JOB_NAME = "job";
  private static final String RESULT_FIELD = "result";
  private static final String RESULT = "42";

  private OpenShiftService openShiftService;

  @Mock
  private OpenshiftProperties openshiftProperties;
  @Mock
  private KubernetesClient kubernetesClient;

  private Supplier<KubernetesClient> kubernetesClientFactory;

  @BeforeEach
  void beforeEach() {
    kubernetesClientFactory = () -> kubernetesClient;
    openShiftService = new OpenShiftService(openshiftProperties, kubernetesClientFactory);
  }

  @Test
  void expectJobMetadataIsUpdatedWithResultValue() {

    var batch = mock(BatchAPIGroupDSL.class);
    var mixedOperation = mock(MixedOperation.class);
    var jobWithName = mock(ScalableResource.class);
    var doneableJob = mock(DoneableJob.class);
    var metadata = mock(JobFluent.MetadataNested.class);

    var jobProperties = new JobProperties();
    jobProperties.setName(JOB_NAME);
    jobProperties.setResultField(RESULT_FIELD);
    when(openshiftProperties.getParentJob()).thenReturn(jobProperties);
    when(openshiftProperties.isEnabled()).thenReturn(true);
    when(kubernetesClient.batch()).thenReturn(batch);
    when(batch.jobs()).thenReturn(mixedOperation);
    when(mixedOperation.withName(JOB_NAME)).thenReturn(jobWithName);
    when(jobWithName.edit()).thenReturn(doneableJob);
    when(doneableJob.editMetadata()).thenReturn(metadata);
    when(metadata.addToAnnotations(RESULT_FIELD, RESULT)).thenReturn(metadata);
    when(metadata.endMetadata()).thenReturn(doneableJob);

    openShiftService.updateParentJobWithResultValue(RESULT);

    verify(doneableJob).done();
  }

  @Test
  void skipFlowIfOpenshiftDisabled() {
    when(openshiftProperties.isEnabled()).thenReturn(false);

    openShiftService.updateParentJobWithResultValue(RESULT);

    verifyNoInteractions(kubernetesClient);
  }
}
