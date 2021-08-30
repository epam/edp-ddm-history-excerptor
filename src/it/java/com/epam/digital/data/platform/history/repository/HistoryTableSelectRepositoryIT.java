package com.epam.digital.data.platform.history.repository;

import com.epam.digital.data.platform.history.config.DataSourceEnable;
import com.epam.digital.data.platform.history.model.HistoryExcerptData;
import com.epam.digital.data.platform.history.model.HistoryExcerptRow;
import com.epam.digital.data.platform.history.model.HistoryExcerptRowDdmInfo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataSourceEnable
@SpringBootTest(classes = { HistoryTableSelectRepository.class })
class HistoryTableSelectRepositoryIT {

  @Autowired
  private HistoryTableSelectRepository historyTableSelectRepository;

  @Test
  void expectCorrectExcerptDataRetrieved() {
    var tableName = "pd_processing_consent_hst";
    var searchColumn = "consent_id";
    var entityId = UUID.fromString("3cc262c1-0cd8-4d45-be66-eb0fca821e0a");

    var actualHistoryData = historyTableSelectRepository.getHistoryData(tableName, searchColumn, entityId);

    var expectedHistoryData = createExpectedHistoryExcerptData();
    assertThat(actualHistoryData).usingRecursiveComparison().isEqualTo(expectedHistoryData);
  }

  private HistoryExcerptData createExpectedHistoryExcerptData() {
    var expected = new HistoryExcerptData();
    expected.setOperationalTableFields(List.of(
            "consent_id",
            "consent_date",
            "person_gender",
            "person_full_name",
            "person_pass_number"));
    expected.setExcerptRows(getExpectedHistoryExcerptRows());
    return expected;
  }

  private List<HistoryExcerptRow> getExpectedHistoryExcerptRows() {
    var excerptRow = new HistoryExcerptRow();
    var ddmInfo = new HistoryExcerptRowDdmInfo();
    ddmInfo.setCreatedAt("2021-08-20 18:00:00");
    ddmInfo.setCreatedBy("user");
    ddmInfo.setDmlOp("I");
    ddmInfo.setSystemId("bd223413-214a-4d6d-9dee-39813f15dad0");
    ddmInfo.setApplicationId("04a00dc5-904d-4043-b8b0-aeb2d4c73ee2");
    ddmInfo.setBusinessProcessId("b533ab28-4068-4e48-9115-e3a74fcfa243");
    ddmInfo.setBusinessProcessDefinitionId("BP_DEF");
    ddmInfo.setBusinessProcessInstanceId("d3d4db61-049c-4830-b77c-e8a7d2ebec89");
    ddmInfo.setBusinessActivity("B_ACT");
    ddmInfo.setBusinessActivityInstanceId("B_ACT_INST_ID");
    ddmInfo.setDigitalSign("DIGN_SIGN");
    ddmInfo.setDigitalSignDerived("DIGN_SIGN_DER");
    ddmInfo.setDigitalSignChecksum(
        "32b0cfd6c6e2dd5750268f634b788f845fd075103b007110d62c6fae0e94028c");
    ddmInfo.setDigitalSignDerivedChecksum(
        "6926f443a89f6aebe1fa2477e40d4c32b8f4aab524f6dc1dd2aa331304c320c4");
    excerptRow.setDdmInfo(ddmInfo);

    var operationalTableData = new HashMap<String, String>();
    operationalTableData.put("consent_id", "3cc262c1-0cd8-4d45-be66-eb0fca821e0a");
    operationalTableData.put("consent_date", "2020-01-15 12:00:01");
    operationalTableData.put("person_gender", "M");
    operationalTableData.put("person_full_name", "John Doe Patronymic");
    operationalTableData.put("person_pass_number", "AB123456");
    excerptRow.setOperationalTableData(operationalTableData);

    return Collections.singletonList(excerptRow);
  }
}
