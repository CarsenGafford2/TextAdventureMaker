class ChoiceFactory {
    Choice create(String description, String target) {
        Choice choice = new Choice();
        choice.description = description;
        choice.targetNodeId = target;
        return choice;
    }
}
