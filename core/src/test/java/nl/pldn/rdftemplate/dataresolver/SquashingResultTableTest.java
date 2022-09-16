package nl.pldn.rdftemplate.dataresolver;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;

import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.Test;

class SquashingResultTableTest {

  @Test
  void givenKeylessTableAndInput_whenGetResults_thenReturnUngroupedResults() {
    // Given
    var squashingResultTable = SquashingResultTable.create();
    addTestData(squashingResultTable);
    // When
    var results = squashingResultTable.getResults();

    // Then
    assertThat(results.size(), is(3));
    assertThat(results, contains(Map.of("foo", "foo", "bar", "bar", "baz", "baz"),
        Map.of("foo", "foo", "bar", "bar", "baz", "bazBaz"), Map.of("foo", "foo", "bar", "barBar", "baz", "baz")));
  }

  @Test
  void givenKeyedTableAndInput_whenGetResults_thenReturnResultsGroupedByKey() {
    // Given
    var squashingResultTable = SquashingResultTable.create(Set.of("foo", "bar"));
    addTestData(squashingResultTable);
    // When
    var results = squashingResultTable.getResults();

    // Then
    assertThat(results.size(), is(2));
    assertThat(results, contains(Map.of("foo", Set.of("foo"), "bar", Set.of("bar"), "baz", Set.of("baz", "bazBaz")),
        Map.of("foo", Set.of("foo"), "bar", Set.of("barBar"), "baz", Set.of("baz"))));
  }

  private void addTestData(SquashingResultTable squashingResultTable) {
    squashingResultTable.add(Map.of("foo", "foo", "bar", "bar", "baz", "baz"));
    squashingResultTable.add(Map.of("foo", "foo", "bar", "bar", "baz", "bazBaz"));
    squashingResultTable.add(Map.of("foo", "foo", "bar", "barBar", "baz", "baz"));
  }
}
