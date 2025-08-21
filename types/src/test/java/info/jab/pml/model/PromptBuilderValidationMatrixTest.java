package info.jab.pml.model;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class PromptBuilderValidationMatrixTest {

    @ParameterizedTest(name = "role=''{0}'' should fail on {1}")
    @CsvSource({
        ", role",
        "'', role",
        "'  ', role"
    })
    void invalidRoleCombinations(String role, String expectedFieldInMessage) {
        Goal goal = new Goal();
        goal.getContent().add("Deliver feature");

        assertThatThrownBy(() ->
            PromptBuilder.create()
                .withRole(role)
                .withGoal(goal)
                .build()
        )
        .isInstanceOf(IllegalStateException.class)
        .hasMessageContaining(expectedFieldInMessage);
    }
}
