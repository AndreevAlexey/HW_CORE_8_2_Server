package Dialog;

public enum DialogQuestions {
    NAME,
    OLDER18,
    MALE;

    @Override
    public String toString() {
        String result = null;
        switch(this) {
            case NAME :
                result = "Enter your name";
                break;
            case OLDER18 :
                result = "Are you older then 18yo(y/n)?";
                break;
            case MALE :
                result = "Are you male(y/n)?";
                break;
        }
        return result;
    }
}


