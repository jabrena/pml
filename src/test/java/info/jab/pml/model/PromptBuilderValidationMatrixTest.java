package info.jab.pml.model;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class PromptBuilderValidationMatrixTest {

    @ParameterizedTest(name = "id=''{0}'' role=''{1}'' should fail on {2}")
    @CsvSource({
        ", assistant, id",
        "'', assistant, id",
        "'  ', assistant, id",
        "prompt-1, , role",
        "prompt-1, '', role",
        "prompt-1, '  ', role"
    })
    void invalidCombinations(String id, String role, String expectedFieldInMessage) {
        Goal goal = new Goal();
        goal.getContent().add("Deliver feature");

        assertThatThrownBy(() ->
            PromptBuilder.create()
                .withId(id)
                .withRole(role)
                .withGoal(goal)
                .build()
        )
        .isInstanceOf(IllegalStateException.class)
        .hasMessageContaining(expectedFieldInMessage);
    }
}
