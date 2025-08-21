package info.jab.pml.model;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class PromptBuilderAllFieldsTest {

    @Test
    void build_success_withAllFields() {
        Goal goal = new Goal();
        goal.getContent().add("Ship MVP");

        Metadata metadata = new Metadata();
        Constraints constraints = new Constraints();
        Instructions instructions = new Instructions();
        Examples examples = new Examples();
        OutputFormat outputFormat = new OutputFormat();
        Safeguards safeguards = new Safeguards();

        Prompt prompt = PromptBuilder.create()
            .withRole("assistant")
            .withGoal(goal)
            .withMetadata(metadata)
            .withTone("formal")
            .withContext("context")
            .withConstraints(constraints)
            .withInstructions(instructions)
            .withExamples(examples)
            .withOutputFormat(outputFormat)
            .withSafeguards(safeguards)
            .build();

        assertThat(prompt.getRole()).isEqualTo("assistant");
        assertThat(prompt.getGoal()).isSameAs(goal);
        assertThat(prompt.getTone()).isEqualTo("formal");
        assertThat(prompt.getContext()).isEqualTo("context");

        assertThat(prompt.getMetadata()).isSameAs(metadata);
        assertThat(prompt.getConstraints()).isSameAs(constraints);
        assertThat(prompt.getInstructions()).isSameAs(instructions);
        assertThat(prompt.getExamples()).isSameAs(examples);
        assertThat(prompt.getOutputFormat()).isSameAs(outputFormat);
        assertThat(prompt.getSafeguards()).isSameAs(safeguards);
    }
}
