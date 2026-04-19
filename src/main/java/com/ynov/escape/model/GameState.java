package com.ynov.escape.model;

public class GameState {

    private Step step = Step.INTRO_DIALOGUE;
    private int dialogueIndex = 0;
    private int quizScore = 0;

    public Step getStep() {
        return step;
    }

    public void setStep(Step step) {
        this.step = step;
        this.dialogueIndex = 0;
    }

    public int getDialogueIndex() {
        return dialogueIndex;
    }

    public void setDialogueIndex(int dialogueIndex) {
        this.dialogueIndex = dialogueIndex;
    }

    public int getQuizScore() {
        return quizScore;
    }

    public void setQuizScore(int quizScore) {
        this.quizScore = quizScore;
    }

    public void reset() {
        step = Step.INTRO_DIALOGUE;
        dialogueIndex = 0;
        quizScore = 0;
    }
}
