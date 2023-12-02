package nl.pldn.rdftemplate.dataresolver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import nl.pldn.rdftemplate.RdfTemplateException;

public class SquashingResultTable {

  private final Set<String> keys;

  private final Map<Set<String>, Map<String, ?>> result;

  public static SquashingResultTable create() {
    return create(Set.of());
  }

  public static SquashingResultTable create(Set<String> keys) {
    return new SquashingResultTable(keys);
  }

  private SquashingResultTable(Set<String> keys) {
    this.keys = keys;
    this.result = new HashMap<>();
  }

  public void add(Map<String, String> rowData) {
    if (keys.isEmpty()) {
      addSimple(rowData);
    } else {
      addSquashingBy(rowData);
    }
  }

  public List<Map<String, ?>> getResults() {
    return new ArrayList<>(result.values());
  }

  private void addSimple(Map<String, String> rowData) {
    result.put(getKey(rowData), rowData);
  }

  @SuppressWarnings("unchecked")
  private void addSquashingBy(Map<String, String> rowData) {
    var rowKey = getKey(rowData);
    Map<String, Set<String>> resultRowData = new HashMap<>();
    if (result.containsKey(rowKey)) {
      resultRowData = (Map<String, Set<String>>) result.get(rowKey);
    }

    for (var entry : rowData.entrySet()) {
      var column = entry.getKey();
      var value = entry.getValue();
      var currentValue = resultRowData.get(column);
      if (currentValue == null) {
        Set<String> valueList = new LinkedHashSet<>();
        valueList.add(value);
        resultRowData.put(column, valueList);
      } else {
        currentValue.add(value);
      }
    }
    result.put(rowKey, resultRowData);
  }

  private Set<String> getKey(Map<String, String> rowData) {
    if (keys.isEmpty()) {
      return Set.of(String.format("%s", result.size() + 1));
    }

    var rowDataKey = keys.stream()
        .map(rowData::get)
        .collect(Collectors.toSet());

    if (rowDataKey.size() != keys.size()) {
      throw new RdfTemplateException(String.format("Could not find all keys %s in row %s", keys, rowData));
    }

    return rowDataKey;
  }

}
