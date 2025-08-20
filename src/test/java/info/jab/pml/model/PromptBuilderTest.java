package info.jab.pml.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("PromptBuilder validations and behavior")
class PromptBuilderTest {

    @Test
    void build_success_withMinimalRequiredFields() {
        // Given
        Goal goal = new Goal();
        goal.getContent().add("Ship MVP");

        // When
        Prompt prompt = PromptBuilder.create()
            .withId("prompt-001")
            .withRole("assistant")
            .withGoal(goal)
            .build();

        // Then
        assertThat(prompt.getId()).isEqualTo("prompt-001");
        assertThat(prompt.getRole()).isEqualTo("assistant");
        assertThat(prompt.getGoal()).isSameAs(goal);

        assertThat(prompt.getMetadata()).isNull();
        assertThat(prompt.getTone()).isNull();
        assertThat(prompt.getContext()).isNull();
        assertThat(prompt.getConstraints()).isNull();
        assertThat(prompt.getInstructions()).isNull();
        assertThat(prompt.getExamples()).isNull();
        assertThat(prompt.getOutputFormat()).isNull();
        assertThat(prompt.getSafeguards()).isNull();
        assertThat(prompt.getVersion()).isNull();
    }

    @Test
    void build_throwsWhenIdMissing() {
        // Given
        Goal goal = new Goal();
        goal.getContent().add("Deliver feature");

        // When / Then
        assertThatThrownBy(() ->
            PromptBuilder.create()
                .withRole("assistant")
                .withGoal(goal)
                .build()
        )
        .isInstanceOf(IllegalStateException.class)
        .hasMessageContaining("id");
    }

    @Test
    void build_throwsWhenRoleMissing() {
        // Given
        Goal goal = new Goal();
        goal.getContent().add("Deliver feature");

        // When / Then
        assertThatThrownBy(() ->
            PromptBuilder.create()
                .withId("prompt-002")
                .withGoal(goal)
                .build()
        )
        .isInstanceOf(IllegalStateException.class)
        .hasMessageContaining("role");
    }

    @Test
    void build_throwsWhenGoalMissing() {
        // When / Then
        assertThatThrownBy(() ->
            PromptBuilder.create()
                .withId("prompt-003")
                .withRole("assistant")
                .build()
        )
        .isInstanceOf(IllegalStateException.class)
        .hasMessageContaining("goal");
    }

    @ParameterizedTest(name = "id ''{0}'' should be rejected")
    @NullAndEmptySource
    @ValueSource(strings = {" ", "\t", "   "})
    void build_throwsWhenIdNullEmptyOrBlank(String badId) {
        // Given
        Goal goal = new Goal();
        goal.getContent().add("Deliver feature");

        // When / Then
        assertThatThrownBy(() ->
            PromptBuilder.create()
                .withId(badId)
                .withRole("assistant")
                .withGoal(goal)
                .build()
        )
        .isInstanceOf(IllegalStateException.class)
        .hasMessageContaining("id");
    }

    @ParameterizedTest(name = "role ''{0}'' should be rejected")
    @NullAndEmptySource
    @ValueSource(strings = {" ", "\t", "   "})
    void build_throwsWhenRoleNullEmptyOrBlank(String badRole) {
        // Given
        Goal goal = new Goal();
        goal.getContent().add("Deliver feature");

        // When / Then
        assertThatThrownBy(() ->
            PromptBuilder.create()
                .withId("prompt-004")
                .withRole(badRole)
                .withGoal(goal)
                .build()
        )
        .isInstanceOf(IllegalStateException.class)
        .hasMessageContaining("role");
    }
}
