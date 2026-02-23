package info.jab.pml.model;

import java.util.Objects;

/**
 * Fluent builder for {@link Prompt}.
 */
public final class PromptBuilder {

    private Metadata metadata;
    private String role;
    private String tone;
    private String context;
    private Goal goal;
    private Constraints constraints;
    private Steps steps;
    private Examples examples;
    private OutputFormat outputFormat;
    private Safeguards safeguards;

    private PromptBuilder() {}

    public static PromptBuilder create() {
        return new PromptBuilder();
    }

    public PromptBuilder withMetadata(Metadata metadata) {
        this.metadata = metadata;
        return this;
    }

    public PromptBuilder withRole(String role) {
        this.role = role;
        return this;
    }

    public PromptBuilder withTone(String tone) {
        this.tone = tone;
        return this;
    }

    public PromptBuilder withContext(String context) {
        this.context = context;
        return this;
    }

    public PromptBuilder withGoal(Goal goal) {
        this.goal = goal;
        return this;
    }

    public PromptBuilder withConstraints(Constraints constraints) {
        this.constraints = constraints;
        return this;
    }

    public PromptBuilder withSteps(Steps steps) {
        this.steps = steps;
        return this;
    }

    public PromptBuilder withExamples(Examples examples) {
        this.examples = examples;
        return this;
    }

    public PromptBuilder withOutputFormat(OutputFormat outputFormat) {
        this.outputFormat = outputFormat;
        return this;
    }

    public PromptBuilder withSafeguards(Safeguards safeguards) {
        this.safeguards = safeguards;
        return this;
    }

    /**
     * Build a {@link Prompt} instance. Validates schema-required fields.
     *
     * <p>Required by schema: role (element), goal (element).</p>
     *
     * @return a new {@link Prompt}
     * @throws IllegalStateException if any required field is missing
     */
    public Prompt build() {
        if (Objects.isNull(role) || role.isBlank()) {
            throw new IllegalStateException("Required element 'role' is missing or empty");
        }
        if (Objects.isNull(goal)) {
            throw new IllegalStateException("Required element 'goal' is missing");
        }

        Prompt prompt = new Prompt();
        prompt.setMetadata(metadata);
        prompt.setRole(role);
        prompt.setTone(tone);
        prompt.setContext(context);
        prompt.setGoal(goal);
        prompt.setConstraints(constraints);
        prompt.setSteps(steps);
        prompt.setExamples(examples);
        prompt.setOutputFormat(outputFormat);
        prompt.setSafeguards(safeguards);
        return prompt;
    }
}
