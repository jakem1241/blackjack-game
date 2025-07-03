public class PowerUpAction {

    private final String prompt;
    private final String[] options;

    public PowerUpAction(String prompt, String[] options) {
        this.prompt = prompt;
        this.options = options;
    }

    public String getPrompt() {
        return prompt;
    }

    public String[] getOptions() {
        return options;
    }

    public int execute(GameEnvironment genv) {
        return genv.askChoice(prompt, options);
    }
}
